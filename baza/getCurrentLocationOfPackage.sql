CREATE FUNCTION getCurrentLocationOfPackage
(
	@IdPaket int
)
RETURNS int
AS
BEGIN
	declare @IdGrad int
	declare @Status int, @IdAdresaPreuzimanje int, @IdAdresaIsporuke int

	--ako ne postoji paket vratimo -2, to ne pise ali tako sam uradila
	if not exists(select * from Paket where IdPaket = @IdPaket) return -2;

	select @Status = Status, @IdAdresaIsporuke = IdAdresaIsporuke, @IdAdresaPreuzimanje = IdAdresaPreuzimanje
	from Paket
	where IdPaket = @IdPaket

	--ako je prihvacen ili odbijen ili kreiran na pocetnoj je adresi
	if (@Status = 0 or @Status = 1 or @Status = 4) return (select IdGrad from Adresa where IdAdresa = @IdAdresaPreuzimanje)
	--ako je isporucen na krajnjoj je adresi
	if (@Status = 3) return (select IdGrad from Adresa where IdAdresa = @IdAdresaIsporuke)

	--ovde je status 2
	declare @IdMagacin int

	select @IdMagacin = IdMagacin
	from JeUMagacinu
	where IdPaket = @IdPaket

	-- mora top 1, ne zna upit da ce biti samo 1 vrednost vracena!!
	if (@IdMagacin is not null) return (select top 1 A.IdGrad
										from Magacin M join Adresa A on (M.IdAdresa = A.IdAdresa)
										where IdMagacin = @IdMagacin)
	

	--ako se prevozi
	return -1
END
GO

