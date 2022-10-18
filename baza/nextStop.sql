CREATE PROCEDURE nextStop 
	@KorisnickoIme varchar(100),
	@Rezultat int out
AS
BEGIN
	set @Rezultat = -1

	if not exists(select * from Vozi where KorisnickoIme = @KorisnickoIme)
	begin
		-- vraca -3 ako uopste ni ne vozi
		set @Rezultat = -3
		return
	end

	declare @IdTrenutneAdrese int, @IdVoznja int, @RegistracioniBroj varchar(100), @Status int
	declare @IdPaket int, @Akcija int, @IdAdresa int
	declare @IdMagacin int
	declare @Kursor cursor
	declare @IdNovaAdresa int

	-- dohvatanje podataka o voznji
	select @IdTrenutneAdrese = IdTrenutneAdrese, @IdVoznja = IdVoznja, @RegistracioniBroj = RegistracioniBroj
	from Vozi
	where KorisnickoIme = @KorisnickoIme

	select @IdMagacin = IdMagacin,  @Status = Status
	from Voznja 
	where IdVoznja = @IdVoznja


	--moze se vise paketa kupiti iz magacina npr pa treba kursor
	set @Kursor = cursor for
	select IdPaket, Akcija, IdAdresa
	from Planiranje
	where KorisnickoIme = @KorisnickoIme and RedniBroj = @Status

	open @Kursor
	fetch from @Kursor into @IdPaket, @Akcija, @IdAdresa

	while @@FETCH_STATUS = 0
	begin

		if(@Akcija = 1 or @Akcija = 3)
		begin
			exec dbo.putPackageInVehicle @IdPaket, @KorisnickoIme
			set @Rezultat = -2
			set @IdNovaAdresa = @IdAdresa
		end
		
		if (@Akcija = 2)
		begin
			exec dbo.delieverPackage @IdPaket, @KorisnickoIme
			set @Rezultat = @IdPaket
			set @IdNovaAdresa = @IdAdresa
		end


		fetch from @Kursor into @IdPaket, @Akcija, @IdAdresa
	end
	close @Kursor
	deallocate @Kursor

	--nije nijednom usao gore, znaci da je kraj voznje
	if (@Rezultat = -1) set @IdNovaAdresa = (select IdAdresa
											 from Magacin M 
											 where IdMagacin = @IdMagacin)

	declare @Razdaljina decimal(20,10)
	set @Razdaljina = dbo.distance(@IdTrenutneAdrese, @IdNovaAdresa)

	update Vozi
	set PredjenoKm = PredjenoKm + @Razdaljina, IdTrenutneAdrese = @IdNovaAdresa
	where KorisnickoIme = @KorisnickoIme
	
	update Voznja
	set Status = Status + 1
	where IdVoznja = @IdVoznja
	
	if (@Rezultat = -1)
	begin
		update Paket set Isplaniran = 0 where IdPaket in (select IdPaket from SePrevozi)
		

		insert into JeParkirano(RegistracioniBroj, IdMagacin) values(@RegistracioniBroj, @IdMagacin)

		insert into JeUMagacinu
		select IdPaket, @IdMagacin
		from SePrevozi
		where KorisnickoIme = @KorisnickoIme

		delete from SePrevozi where KorisnickoIme = @KorisnickoIme
		delete from Planiranje where KorisnickoIme = @KorisnickoIme
		
		declare @Zaradjeno decimal(10,3), @PredjenoKm decimal(10,3), @Potrosnja decimal(10,3), @TipGoriva int
		select @PredjenoKm = PredjenoKm, @Zaradjeno = Zaradjeno from Vozi where KorisnickoIme = @KorisnickoIme
		select @TipGoriva = TipGoriva, @Potrosnja = Potrosnja from Vozilo where RegistracioniBroj = @RegistracioniBroj
		
		declare @Profit decimal(10,3)
		set @Profit = dbo.countProfit(@Zaradjeno, @PredjenoKm, @Potrosnja, @TipGoriva)

		update Voznja
		set Profit = @Profit
		where IdVoznja = @IdVoznja

		update Kurir 
		set Status = 0, Profit = Profit + @Profit
		where KorisnickoIme = @KorisnickoIme

		delete from Vozi where KorisnickoIme = @KorisnickoIme
	end

END
GO
