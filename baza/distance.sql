
CREATE FUNCTION distance
(
	@IdAdresa1 int,
	@IdAdresa2 int
)
RETURNS decimal(20,10)
AS
BEGIN
	declare @Razdaljina decimal(20, 10)
	declare @x1 int, @y1 int, @x2 int, @y2 int

	select @x1 = x, @y1 = y
	from Adresa
	where IdAdresa = @IdAdresa1

	select @x2 = x, @y2 = y
	from Adresa
	where IdAdresa = @IdAdresa2

	set @Razdaljina = SQRT((@x1-@x2)*(@x1-@x2) + (@y1-@y2)*(@y1-@y2))

	return @Razdaljina

END
GO

