CREATE PROCEDURE delieverPackage
	@IdPaket int,
	@KorisnickoIme varchar(100)	
AS
BEGIN
	declare @Cena decimal(10, 3)

	select @Cena = Cena
	from Paket
	where @IdPaket = IdPaket

	update Vozi
	set Zaradjeno = Zaradjeno + @Cena
	where KorisnickoIme = @KorisnickoIme
	
	update Paket
	set Status = 3
	where IdPaket = @IdPaket

	update Kurir
	set BrojIsporucenihPaketa = BrojIsporucenihPaketa + 1
	where KorisnickoIme = @KorisnickoIme

	delete from SePrevozi
	where IdPaket = @IdPaket

END
GO
