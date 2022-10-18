CREATE PROCEDURE planTakingPackagesFromCity

	@IdGrad int,
	@KorisnickoIme varchar(100),
	@RegistracioniBroj varchar(100),
	@Akcija int

AS
BEGIN
	declare @Tabela table(IdPaket int, IdAdresa int)
	declare @Kursor cursor
	declare @IdPaket int, @Tezina decimal(10,3), @IdAdresaIsporuke int, @IdAdresaPreuzimanja int
	declare @RedniBroj int, @Slobodno decimal(10,3)

	select @Slobodno = Slobodno
	from Vozilo
	where RegistracioniBroj = @RegistracioniBroj

	select @RedniBroj = coalesce(max(RedniBroj), 0)
	from Planiranje
	where KorisnickoIme = @KorisnickoIme
	
	set @Kursor = cursor for
		select IdPaket, Tezina, IdAdresaIsporuke, IdAdresaPreuzimanje
		from Paket P join Adresa A on (P.IdAdresaPreuzimanje = A.IdAdresa)
		where P.Status = 1 and A.IdGrad = @IdGrad and P.Isplaniran = 0
		order by VremeKreiranja

	open @Kursor
	fetch from @Kursor into @IdPaket, @Tezina, @IdAdresaIsporuke, @IdAdresaPreuzimanja
	while @@FETCH_STATUS = 0
	begin
		if (@Tezina <= @Slobodno)
		begin
			
			insert into @Tabela
			values (@IdPaket, @IdAdresaIsporuke)

			set @Slobodno = @Slobodno - @Tezina
			set @RedniBroj = @RedniBroj + 1

			--izmeni u bazi
			exec dbo.putPackageInPlanning  @IdPaket, @KorisnickoIme, @RegistracioniBroj, @Akcija, @RedniBroj, @Slobodno, @IdAdresaPreuzimanja
			
			
		end
		if (@Slobodno = 0) break


		fetch from @Kursor into @IdPaket, @Tezina, @IdAdresaIsporuke, @IdAdresaPreuzimanja
	end
	close @Kursor
	deallocate @Kursor


	select * from @Tabela
END
GO