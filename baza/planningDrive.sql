use dm180015
GO

CREATE PROCEDURE planningDrive 
	@KorisnickoIme varchar(100),
	@PocinjeVoznja int out
AS
BEGIN
	declare @IdMagacin int
	declare @IdAdresaMagacina int
	declare @RegistracioniBroj varchar(100)
	declare @IdGrad int

	--provera da li kurir trenutno vozi
	if not exists(select * from Kurir where KorisnickoIme = @KorisnickoIme) or exists (select * from Vozi where KorisnickoIme = @KorisnickoIme)
	begin
		set @PocinjeVoznja = 0
		return
	end

	--grad u kome zivi kurir
	select @IdGrad = IdGrad
	from Korisnik K join Adresa A1 on (K.IdAdresa = A1.IdAdresa)
	where KorisnickoIme = @KorisnickoIme

	--magacin u tom gradu
	select @IdMagacin = IdMagacin, @IdAdresaMagacina = M.IdAdresa
	from Magacin M join Adresa A on (M.IdAdresa = A.IdAdresa)
	where A.IdGrad = @IdGrad

	if (@IdMagacin is null)
	begin
		set @PocinjeVoznja = 0
		return
	end

	--vozilo parkirano u tom magacinu
	select top 1 @RegistracioniBroj = V.RegistracioniBroj
	from Vozilo V join JeParkirano JP on (V.RegistracioniBroj = JP.RegistracioniBroj)
	where IdMagacin = @IdMagacin

	if (@RegistracioniBroj is null)
	begin
		set @PocinjeVoznja = 0
		return
	end
	
	--obavezno mora pre provere dole da se postavi
	update Vozilo
	set Slobodno = Nosivost 
	where RegistracioniBroj = @RegistracioniBroj

	--postoje paketi za prevoz u tom gradu
	if (dbo.existsPackageForDeliveryInCity(@IdGrad, @RegistracioniBroj) = -1 and dbo.existsPackageForDeliveryInStockroom(@IdMagacin, @RegistracioniBroj) = -1)
	begin
		set @PocinjeVoznja = 0
		return
	end

	update Kurir
	set Status = 1
	where KorisnickoIme = @KorisnickoIme

	--status 1 => Voznja isplanirana
	insert into Voznja(KorisnickoIme, RegistracioniBroj, IdMagacin, Status, Profit)
	values (@KorisnickoIme, @RegistracioniBroj, @IdMagacin, 1, 0)

	insert into Vozi(KorisnickoIme, RegistracioniBroj, IdTrenutneAdrese, IdVoznja, PredjenoKm, Zaradjeno) --tako dohvatam id ove insertovane voznje
	values (@KorisnickoIme, @RegistracioniBroj, @IdAdresaMagacina, (select IdVoznja from Voznja where KorisnickoIme=@KorisnickoIme and Status=1), 0 , 0)

	delete from JeParkirano
	where RegistracioniBroj = @RegistracioniBroj


	----------------------------------------------------------------------------------------------------------------
	--	biranje paketa koje ce se prevesti --
	
	declare @Kursor cursor
	declare @Tabela table(IdPaket int, IdAdresa int)
	declare @Slobodno decimal(10,3), @RedniBroj int


	-- I FAZA PLANIRANJA --
	-- iz grada preuzima --
	insert into @Tabela  
	exec dbo.planTakingPackagesFromCity @IdGrad, @KorisnickoIme, @RegistracioniBroj, 1

	-- iz magacina preuzima --
	insert into @Tabela  
	exec dbo.planTakingPackagesFromStockroom @IdMagacin, @KorisnickoIme, @RegistracioniBroj, 1


	set @Slobodno = (select Slobodno from Vozilo where RegistracioniBroj = @RegistracioniBroj)
	select @RedniBroj = coalesce(max(RedniBroj), 0) from Planiranje where KorisnickoIme = @KorisnickoIme

	-- II FAZA PLANIRANJA --
	declare @IdTrenutneAdrese int --adresa gde smo preuzeli poslednji paket

	set @IdTrenutneAdrese = (select top 1 IdAdresa
							from Planiranje
							where RedniBroj = @RedniBroj and KorisnickoIme = @KorisnickoIme)

	declare @IdAdresa2 int, @IdPaket2 int, @Razdaljina decimal(20,10)		
	declare @IdPaketSled int, @MinRazdaljina decimal(20,10), @BrPonavljanja int, @IdAdresaIsporuke int
	 

	select @BrPonavljanja = count(*) from @Tabela
	
	while (@BrPonavljanja > 0)
	begin
		set @IdPaketSled = -1
		set @MinRazdaljina = -1

		set @Kursor = cursor for
		select IdPaket, IdAdresa
		from @Tabela

		open @Kursor
		fetch from @Kursor into @IdPaket2, @IdAdresa2
		while @@FETCH_STATUS = 0
		begin
			set @Razdaljina = dbo.distance(@IdTrenutneAdrese, @IdAdresa2)
			if (@IdPaketSled = -1 or @Razdaljina < @MinRazdaljina)
			begin
				set @MinRazdaljina = @Razdaljina
				set @IdPaketSled = @IdPaket2
				set @IdAdresaIsporuke = @IdAdresa2
			end	
			fetch from @Kursor into @IdPaket2, @IdAdresa2
		end

		close @Kursor
		deallocate @Kursor

		
		delete from @Tabela where IdPaket = @IdPaketSled
		
		set @Slobodno = @Slobodno + (select Tezina from Paket where IdPaket = @IdPaketSled)
		set @RedniBroj = @RedniBroj + 1

		insert into Planiranje(IdPaket, KorisnickoIme, Akcija, RedniBroj, IdAdresa)
		values (@IdPaketSled, @KorisnickoIme, 2, @RedniBroj, @IdAdresaIsporuke)
		update Vozilo set Slobodno = @Slobodno where RegistracioniBroj = @RegistracioniBroj
		



		-- iz grada i magacina preuzima nove, njih ne dodajemo u tabelu za preuzimanje --
		--gledamo koji su to magacin i grad
		declare @IdGrad2 int, @IdMagacin2 int

		select @IdGrad2 = IdGrad
		from Adresa
		where IdAdresa = @IdAdresaIsporuke

		select @IdMagacin2 = IdMagacin
		from Magacin M join Adresa A on (M.IdAdresa = A.IdAdresa)
		where A.IdGrad = @IdGrad2

		exec dbo.planTakingPackagesFromCity @IdGrad2, @KorisnickoIme, @RegistracioniBroj, 3
		if @IdMagacin2 is not null exec dbo.planTakingPackagesFromStockroom @IdMagacin2, @KorisnickoIme, @RegistracioniBroj, 3


		--moraju da se apdejtuju ove stvari
		select @RedniBroj = max(RedniBroj) from Planiranje where KorisnickoIme = @KorisnickoIme
		set @Slobodno = (select Slobodno from Vozilo where RegistracioniBroj = @RegistracioniBroj)


		select top 1 @IdTrenutneAdrese = IdAdresa
		from Planiranje
		where RedniBroj = @RedniBroj and KorisnickoIme = @KorisnickoIme

		set @BrPonavljanja = @BrPonavljanja - 1
	end




	
	set @PocinjeVoznja = 1
END
