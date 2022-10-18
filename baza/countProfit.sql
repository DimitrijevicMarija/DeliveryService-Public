
CREATE FUNCTION countProfit
(
	@Zaradjeno decimal(10,3),
	@PredjenoKm decimal(10,3),
	@Potrosnja decimal(10,3),
	@TipGoriva int
)
RETURNS decimal(10,3)
AS
BEGIN
	declare @Profit decimal(10,3)
	declare @PotrosenoGoriva decimal(10,3)

	set @PotrosenoGoriva = case @TipGoriva 
							  when 0 then 15 * @PredjenoKm * @Potrosnja
							  when 1 then 32 * @PredjenoKm * @Potrosnja
							  when 2 then 36 * @PredjenoKm * @Potrosnja
						end
	
	set @Profit = @Zaradjeno - @PotrosenoGoriva
	return @Profit
END
GO

