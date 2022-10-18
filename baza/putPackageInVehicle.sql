CREATE PROCEDURE putPackageInVehicle
	@IdPaket int,
	@KorisnickoIme varchar(100)
AS
BEGIN

	insert into SePrevozi(IdPaket, KorisnickoIme)
	values (@IdPaket, @KorisnickoIme)

	update Paket
	set Status = 2
	where IdPaket = @IdPaket

	-- svakako se nece izvrsiti ako nije u magacinu
	delete from JeUMagacinu
	where IdPaket = @IdPaket
	
END
GO
