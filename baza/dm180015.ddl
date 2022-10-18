
DROP TABLE [Administrator]
go

DROP TABLE [Zahtev]
go

DROP TABLE [JeParkirano]
go

DROP TABLE [SePrevozi]
go

DROP TABLE [Vozi]
go

DROP TABLE [Voznja]
go

DROP TABLE [Kurir]
go

DROP TABLE [Vozilo]
go

DROP TABLE [JeUMagacinu]
go

DROP TABLE [Magacin]
go

DROP TABLE [Paket]
go

DROP TABLE [Korisnik]
go

DROP TABLE [Adresa]
go

DROP TABLE [Grad]
go

CREATE TABLE [Administrator]
( 
	[IdKorisnik]         integer  NOT NULL 
)
go

CREATE TABLE [Adresa]
( 
	[IdAdresa]           integer  IDENTITY  NOT NULL ,
	[Ulica]              varchar(100)  NULL ,
	[Broj]               integer  NULL ,
	[x]                  integer  NULL ,
	[y]                  integer  NULL ,
	[IdGrad]             integer  NOT NULL 
)
go

CREATE TABLE [Grad]
( 
	[IdGrad]             integer  IDENTITY  NOT NULL ,
	[Naziv]              varchar(100)  NULL ,
	[PostanskiBroj]      integer  NULL 
)
go

CREATE TABLE [JeParkirano]
( 
	[IdVozilo]           integer  NOT NULL ,
	[IdMagacin]          integer  NULL 
)
go

CREATE TABLE [JeUMagacinu]
( 
	[IdPaket]            integer  NOT NULL ,
	[IdMagacin]          integer  NOT NULL 
)
go

CREATE TABLE [Korisnik]
( 
	[IdKorisnik]         integer  IDENTITY  NOT NULL ,
	[Ime]                varchar(100)  NULL ,
	[Prezime]            varchar(100)  NULL ,
	[Sifra]              varchar(100)  NULL ,
	[IdAdresa]           integer  NOT NULL ,
	[KorisnickoIme]      varchar(100)  NULL 
)
go

CREATE TABLE [Kurir]
( 
	[IdKorisnik]         integer  NOT NULL ,
	[BrojVozackeDozvole] varchar(100)  NULL ,
	[BrojIsporucenihPaketa] integer  NULL ,
	[Profit]             decimal(10,3)  NULL ,
	[Status]             integer  NULL 
	CONSTRAINT [0_1_1509658991]
		CHECK  ( [Status]=0 OR [Status]=1 )
)
go

CREATE TABLE [Magacin]
( 
	[IdMagacin]          integer  IDENTITY  NOT NULL ,
	[IdAdresa]           integer  NOT NULL 
)
go

CREATE TABLE [Paket]
( 
	[IdPaket]            integer  IDENTITY  NOT NULL ,
	[IdKorisnik]         integer  NOT NULL ,
	[IdAdresaPreuzimanje] integer  NOT NULL ,
	[IdAdresaIsporuke]   integer  NOT NULL ,
	[Tip]                integer  NULL 
	CONSTRAINT [0_3_705358585]
		CHECK  ( Tip BETWEEN 0 AND 3 ),
	[Tezina]             decimal(10,3)  NULL ,
	[Cena]               decimal(10,3)  NULL ,
	[Status]             integer  NULL 
	CONSTRAINT [0_4_1391958110]
		CHECK  ( Status BETWEEN 0 AND 4 ),
	[VremeKreiranja]     datetime  NULL ,
	[VremePrihvatanja]   datetime  NULL 
)
go

CREATE TABLE [SePrevozi]
( 
	[IdPaket]            integer  NOT NULL ,
	[IdTrenutneAdrese]   integer  NOT NULL ,
	[IdKorisnik]         integer  NOT NULL 
)
go

CREATE TABLE [Vozi]
( 
	[IdVozilo]           integer  NOT NULL ,
	[IdKorisnik]         integer  NOT NULL ,
	[IdTrenutneAdrese]   integer  NOT NULL ,
	[IdVoznja]           integer  NULL 
)
go

CREATE TABLE [Vozilo]
( 
	[IdVozilo]           integer  IDENTITY  NOT NULL ,
	[TipGoriva]          integer  NULL 
	CONSTRAINT [0_2_1619657778]
		CHECK  ( TipGoriva BETWEEN 0 AND 2 ),
	[Potrosnja]          decimal(10,3)  NULL ,
	[Nosivost]           decimal(10,3)  NULL ,
	[RegistracioniBroj]  varchar(100)  NULL 
)
go

CREATE TABLE [Voznja]
( 
	[IdVoznja]           integer  IDENTITY  NOT NULL ,
	[IdKorisnik]         integer  NOT NULL ,
	[IdVozilo]           integer  NOT NULL ,
	[Profit]             decimal(10,3)  NULL ,
	[IdMagacin]          integer  NOT NULL 
)
go

CREATE TABLE [Zahtev]
( 
	[IdZahtev]           integer  IDENTITY  NOT NULL ,
	[BrojVozackeDozvole] varchar(100)  NULL ,
	[IdKorisnik]         integer  NOT NULL 
)
go

ALTER TABLE [Administrator]
	ADD CONSTRAINT [XPKAdministrator] PRIMARY KEY  CLUSTERED ([IdKorisnik] ASC)
go

ALTER TABLE [Adresa]
	ADD CONSTRAINT [XPKAdresa] PRIMARY KEY  CLUSTERED ([IdAdresa] ASC)
go

ALTER TABLE [Grad]
	ADD CONSTRAINT [XPKGrad] PRIMARY KEY  CLUSTERED ([IdGrad] ASC)
go

ALTER TABLE [JeParkirano]
	ADD CONSTRAINT [XPKJeParkirano] PRIMARY KEY  CLUSTERED ([IdVozilo] ASC)
go

ALTER TABLE [JeUMagacinu]
	ADD CONSTRAINT [XPKJeUMagacinu] PRIMARY KEY  CLUSTERED ([IdPaket] ASC)
go

ALTER TABLE [Korisnik]
	ADD CONSTRAINT [XPKKorisnik] PRIMARY KEY  CLUSTERED ([IdKorisnik] ASC)
go

ALTER TABLE [Korisnik]
	ADD CONSTRAINT [XAK1Korisnik] UNIQUE ([KorisnickoIme]  ASC)
go

ALTER TABLE [Kurir]
	ADD CONSTRAINT [XPKKurir] PRIMARY KEY  CLUSTERED ([IdKorisnik] ASC)
go

ALTER TABLE [Kurir]
	ADD CONSTRAINT [XAK1Kurir] UNIQUE ([BrojVozackeDozvole]  ASC)
go

ALTER TABLE [Magacin]
	ADD CONSTRAINT [XPKMagacin] PRIMARY KEY  CLUSTERED ([IdMagacin] ASC)
go

ALTER TABLE [Paket]
	ADD CONSTRAINT [XPKPaket] PRIMARY KEY  CLUSTERED ([IdPaket] ASC)
go

ALTER TABLE [SePrevozi]
	ADD CONSTRAINT [XPKSePrevozi] PRIMARY KEY  CLUSTERED ([IdPaket] ASC)
go

ALTER TABLE [Vozi]
	ADD CONSTRAINT [XPKVozi] PRIMARY KEY  CLUSTERED ([IdKorisnik] ASC)
go

ALTER TABLE [Vozilo]
	ADD CONSTRAINT [XPKVozilo] PRIMARY KEY  CLUSTERED ([IdVozilo] ASC)
go

ALTER TABLE [Vozilo]
	ADD CONSTRAINT [XAK1Vozilo] UNIQUE ([RegistracioniBroj]  ASC)
go

ALTER TABLE [Voznja]
	ADD CONSTRAINT [XPKVoznja] PRIMARY KEY  CLUSTERED ([IdVoznja] ASC)
go

ALTER TABLE [Zahtev]
	ADD CONSTRAINT [XPKZahtev] PRIMARY KEY  CLUSTERED ([IdZahtev] ASC)
go


ALTER TABLE [Administrator]
	ADD CONSTRAINT [R_6] FOREIGN KEY ([IdKorisnik]) REFERENCES [Korisnik]([IdKorisnik])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go


ALTER TABLE [Adresa]
	ADD CONSTRAINT [R_1] FOREIGN KEY ([IdGrad]) REFERENCES [Grad]([IdGrad])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [JeParkirano]
	ADD CONSTRAINT [R_17] FOREIGN KEY ([IdVozilo]) REFERENCES [Vozilo]([IdVozilo])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [JeParkirano]
	ADD CONSTRAINT [R_18] FOREIGN KEY ([IdMagacin]) REFERENCES [Magacin]([IdMagacin])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [JeUMagacinu]
	ADD CONSTRAINT [R_21] FOREIGN KEY ([IdPaket]) REFERENCES [Paket]([IdPaket])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [JeUMagacinu]
	ADD CONSTRAINT [R_22] FOREIGN KEY ([IdMagacin]) REFERENCES [Magacin]([IdMagacin])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Korisnik]
	ADD CONSTRAINT [R_3] FOREIGN KEY ([IdAdresa]) REFERENCES [Adresa]([IdAdresa])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Kurir]
	ADD CONSTRAINT [R_5] FOREIGN KEY ([IdKorisnik]) REFERENCES [Korisnik]([IdKorisnik])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go


ALTER TABLE [Magacin]
	ADD CONSTRAINT [R_2] FOREIGN KEY ([IdAdresa]) REFERENCES [Adresa]([IdAdresa])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Paket]
	ADD CONSTRAINT [R_13] FOREIGN KEY ([IdKorisnik]) REFERENCES [Korisnik]([IdKorisnik])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Paket]
	ADD CONSTRAINT [R_14] FOREIGN KEY ([IdAdresaPreuzimanje]) REFERENCES [Adresa]([IdAdresa])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Paket]
	ADD CONSTRAINT [R_15] FOREIGN KEY ([IdAdresaIsporuke]) REFERENCES [Adresa]([IdAdresa])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [SePrevozi]
	ADD CONSTRAINT [R_19] FOREIGN KEY ([IdPaket]) REFERENCES [Paket]([IdPaket])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [SePrevozi]
	ADD CONSTRAINT [R_23] FOREIGN KEY ([IdTrenutneAdrese]) REFERENCES [Adresa]([IdAdresa])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [SePrevozi]
	ADD CONSTRAINT [R_25] FOREIGN KEY ([IdKorisnik]) REFERENCES [Vozi]([IdKorisnik])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Vozi]
	ADD CONSTRAINT [R_10] FOREIGN KEY ([IdVozilo]) REFERENCES [Vozilo]([IdVozilo])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Vozi]
	ADD CONSTRAINT [R_11] FOREIGN KEY ([IdKorisnik]) REFERENCES [Kurir]([IdKorisnik])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Vozi]
	ADD CONSTRAINT [R_26] FOREIGN KEY ([IdTrenutneAdrese]) REFERENCES [Adresa]([IdAdresa])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Vozi]
	ADD CONSTRAINT [R_27] FOREIGN KEY ([IdVoznja]) REFERENCES [Voznja]([IdVoznja])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Voznja]
	ADD CONSTRAINT [R_8] FOREIGN KEY ([IdKorisnik]) REFERENCES [Kurir]([IdKorisnik])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Voznja]
	ADD CONSTRAINT [R_9] FOREIGN KEY ([IdVozilo]) REFERENCES [Vozilo]([IdVozilo])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Voznja]
	ADD CONSTRAINT [R_24] FOREIGN KEY ([IdMagacin]) REFERENCES [Magacin]([IdMagacin])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Zahtev]
	ADD CONSTRAINT [R_7] FOREIGN KEY ([IdKorisnik]) REFERENCES [Korisnik]([IdKorisnik])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go
