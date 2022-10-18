CREATE FUNCTION existsPackageForDeliveryInCity
(
	@IdGrad int,
	@RegistracioniBroj varchar(100)
)
RETURNS int
AS
BEGIN
	declare @Slobodno decimal(10,3)
	declare @IdPaket int, @VremeKreiranja datetime

	select @Slobodno = Slobodno
	from Vozilo
	where RegistracioniBroj = @RegistracioniBroj

	select @VremeKreiranja = min(P.VremeKreiranja), @IdPaket = P.IdPaket
	from Paket P join Adresa A on (P.IdAdresaPreuzimanje = A.IdAdresa)
	where P.Status = 1 and A.IdGrad = @IdGrad and P.Tezina <= @Slobodno and P.Isplaniran = 0
	group by P.IdPaket, P.VremeKreiranja

	if (@IdPaket is null) return -1
	return @IdPaket
	
END


