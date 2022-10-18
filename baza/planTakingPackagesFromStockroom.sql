CREATE PROCEDURE planTakingPackagesFromStockroom

	@IdMagacin int,
	@KorisnickoIme varchar(100),
	@RegistracioniBroj varchar(100),
	@Akcija int

AS
BEGIN
	declare @Tabela table(IdPaket int, IdAdresa int)
	declare @Kursor cursor
	declare @IdPaket int, @Tezina decimal(10,3), @IdAdresaIsporuke int, @IdAdresaPreuzimanja int
	declare @RedniBroj int, @Slobodno decimal(10,3)

	-- ADRESA PREUZIMANJA JE ADRESA MAGACINA !!!!!!!!
	select @IdAdresaPreuzimanja = IdAdresa
	from Magacin
	where IdMagacin = @IdMagacin

	select @Slobodno = Slobodno
	from Vozilo
	where RegistracioniBroj = @RegistracioniBroj

	select @RedniBroj = coalesce(max(RedniBroj), 0) + 1
	from Planiranje
	where KorisnickoIme = @KorisnickoIme
	
	set @Kursor = cursor for
		select P.IdPaket, P.Tezina, P.IdAdresaIsporuke
		from Paket P join JeUMagacinu JUM on(P.IdPaket = JUM.IdPaket)
		where P.Status = 2 and JUM.IdMagacin = @IdMagacin and P.Isplaniran = 0
		order by VremeKreiranja

	open @Kursor
	fetch from @Kursor into @IdPaket, @Tezina, @IdAdresaIsporuke
	while @@FETCH_STATUS = 0
	begin
		if (@Tezina <= @Slobodno)
		begin

			insert into @Tabela
			values (@IdPaket, @IdAdresaIsporuke)

			set @Slobodno = @Slobodno - @Tezina
			-- sve iz magacina stavljam pod isti redni broj!!!!!!!
			-- set @RedniBroj = @RedniBroj + 1

			--treba sa adrese magacina da se preuzme!!!!!
			exec dbo.putPackageInPlanning  @IdPaket, @KorisnickoIme, @RegistracioniBroj, @Akcija, @RedniBroj, @Slobodno, @IdAdresaPreuzimanja
			
			
		end
		if (@Slobodno = 0) break


		fetch from @Kursor into @IdPaket, @Tezina, @IdAdresaIsporuke
	end
	close @Kursor
	deallocate @Kursor


	select * from @Tabela
END
GO