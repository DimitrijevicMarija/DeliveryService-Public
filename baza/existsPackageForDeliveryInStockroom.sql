CREATE FUNCTION existsPackageForDeliveryInStockroom 
(
	@IdMagacin int,
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
	from Paket P join JeUMagacinu JUM on(P.IdPaket = JUM.IdPaket)
	where P.Status = 2 and JUM.IdMagacin = @IdMagacin and P.Tezina <= @Slobodno and P.Isplaniran = 0
	group by P.IdPaket, P.VremeKreiranja

	if (@IdPaket is null) return -1
	return @IdPaket
	
END

