
CREATE PROCEDURE getAllPackagesCurrentlyAtCity 
	@IdGrad int 
AS
BEGIN
	declare @Kursor cursor
	declare @IdPaket int
	declare @Tabela TABLE(IdPaket int)

	--ako treba i kreirane i odbijene samo izbrisi where
	set @Kursor = cursor for
	select IdPaket
	from Paket
	where Status in (1, 2, 3)

	open @Kursor
	fetch from @Kursor into @IdPaket


	while @@FETCH_STATUS = 0
	begin
		if (dbo.getCurrentLocationOfPackage(@IdPaket) = @IdGrad) insert into @Tabela values(@IdPaket)

		fetch from @Kursor into @IdPaket
	end

	close @Kursor
	deallocate @Kursor
	
	 select *
	 from @Tabela
END
GO
