CREATE TRIGGER TR_Transport_Offer
   ON  Paket 
   AFTER INSERT, UPDATE
AS 
BEGIN
	declare @Kursor cursor
	declare @IdPaket int
	declare @Tip int, @IdAdresaPreuzimanje int, @IdAdresaIsporuke int
	declare @Tezina decimal(10,3)
	declare @Cena decimal(10,3)
	declare @Razdaljina decimal(20, 10)

	set @Kursor = cursor for
	select Tip, Tezina, IdAdresaPreuzimanje, IdAdresaIsporuke, IdPaket
	from inserted
	where Status = 0

	open @Kursor
	fetch from @Kursor into @Tip, @Tezina, @IdAdresaPreuzimanje, @IdAdresaIsporuke, @IdPaket

	while @@FETCH_STATUS = 0
	begin

		set @Razdaljina = dbo.distance(@IdAdresaPreuzimanje, @IdAdresaIsporuke)

		set @Cena = case @Tip when 0 then 115 * @Razdaljina
							  when 1 then (175 + 100 * @Tezina) * @Razdaljina
							  when 2 then (250 + 100 * @Tezina) * @Razdaljina
							  else (350 + 500 * @Tezina) * @Razdaljina
						end

		update Paket 
		set Cena = @Cena
		where IdPaket = @IdPaket

		fetch from @Kursor into @Tip, @Tezina, @IdAdresaPreuzimanje, @IdAdresaIsporuke, @IdPaket
	end


	close @Kursor
	deallocate @Kursor
END
