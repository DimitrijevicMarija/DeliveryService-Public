
CREATE PROCEDURE putPackageInPlanning
	@IdPaket int,
	@KorisnickoIme varchar(100),
	@RegistracioniBroj varchar(100),
	@Akcija int,
	@RedniBroj int,
	@Slobodno decimal(10, 3),
	@IdAdresaPreuzimanja int
AS
BEGIN

	insert into Planiranje(IdPaket, KorisnickoIme, Akcija, RedniBroj, IdAdresa)
	values (@IdPaket, @KorisnickoIme, @Akcija, @RedniBroj, @IdAdresaPreuzimanja)

	update Paket 
	set Isplaniran = 1 
	where IdPaket = @IdPaket

	update Vozilo
	set Slobodno = @Slobodno 
	where RegistracioniBroj = @RegistracioniBroj
END
GO
