
DROP TABLE [Administrator]
go

DROP TABLE [Zahtev]
go

DROP TABLE [JeParkirano]
go

DROP TABLE [SePrevozi]
go

DROP TABLE [JeUMagacinu]
go

DROP TABLE [Planiranje]
go

DROP TABLE [Vozi]
go

DROP TABLE [Voznja]
go

DROP TABLE [Magacin]
go

DROP TABLE [Kurir]
go

DROP TABLE [Vozilo]
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
	[KorisnickoIme]      varchar(100)  NOT NULL 
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
	[PostanskiBroj]      varchar(100)  NULL 
)
go

CREATE TABLE [JeParkirano]
( 
	[IdMagacin]          integer  NULL ,
	[RegistracioniBroj]  varchar(100)  NOT NULL 
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
	[Ime]                varchar(100)  NULL ,
	[Prezime]            varchar(100)  NULL ,
	[Sifra]              varchar(100)  NULL ,
	[IdAdresa]           integer  NOT NULL ,
	[KorisnickoIme]      varchar(100)  NOT NULL 
)
go

CREATE TABLE [Kurir]
( 
	[BrojVozackeDozvole] varchar(100)  NULL ,
	[BrojIsporucenihPaketa] integer  NULL ,
	[Profit]             decimal(10,3)  NULL ,
	[Status]             integer  NULL 
	CONSTRAINT [0_1_1509658991]
		CHECK  ( [Status]=0 OR [Status]=1 ),
	[KorisnickoIme]      varchar(100)  NOT NULL 
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
	[VremePrihvatanja]   datetime  NULL ,
	[KorisnickoIme]      varchar(100)  NOT NULL ,
	[Isplaniran]         integer  NULL 
)
go

CREATE TABLE [Planiranje]
( 
	[IdPaket]            integer  NOT NULL ,
	[KorisnickoIme]      varchar(100)  NOT NULL ,
	[Akcija]             integer  NULL ,
	[RedniBroj]          integer  NULL ,
	[IdPlaniranje]       integer  IDENTITY  NOT NULL ,
	[IdAdresa]           integer  NOT NULL 
)
go

CREATE TABLE [SePrevozi]
( 
	[IdPaket]            integer  NOT NULL ,
	[VracaSeUMagacin]    integer  NULL ,
	[KorisnickoIme]      varchar(100)  NOT NULL 
)
go

CREATE TABLE [Vozi]
( 
	[IdTrenutneAdrese]   integer  NOT NULL ,
	[IdVoznja]           integer  NOT NULL ,
	[KorisnickoIme]      varchar(100)  NOT NULL ,
	[RegistracioniBroj]  varchar(100)  NOT NULL ,
	[PredjenoKm]         decimal(10,3)  NULL ,
	[Zaradjeno]          decimal(10,3)  NULL 
)
go

CREATE TABLE [Vozilo]
( 
	[TipGoriva]          integer  NULL 
	CONSTRAINT [0_2_1619657778]
		CHECK  ( TipGoriva BETWEEN 0 AND 2 ),
	[Potrosnja]          decimal(10,3)  NULL ,
	[Nosivost]           decimal(10,3)  NULL ,
	[RegistracioniBroj]  varchar(100)  NOT NULL ,
	[Slobodno]           decimal(10,3)  NULL 
)
go

CREATE TABLE [Voznja]
( 
	[IdVoznja]           integer  IDENTITY  NOT NULL ,
	[Profit]             decimal(10,3)  NULL ,
	[IdMagacin]          integer  NOT NULL ,
	[KorisnickoIme]      varchar(100)  NOT NULL ,
	[RegistracioniBroj]  varchar(100)  NOT NULL ,
	[Status]             integer  NULL 
)
go

CREATE TABLE [Zahtev]
( 
	[BrojVozackeDozvole] varchar(100)  NULL ,
	[KorisnickoIme]      varchar(100)  NOT NULL 
)
go

ALTER TABLE [Administrator]
	ADD CONSTRAINT [XPKAdministrator] PRIMARY KEY  CLUSTERED ([KorisnickoIme] ASC)
go

ALTER TABLE [Adresa]
	ADD CONSTRAINT [XPKAdresa] PRIMARY KEY  CLUSTERED ([IdAdresa] ASC)
go

ALTER TABLE [Grad]
	ADD CONSTRAINT [XPKGrad] PRIMARY KEY  CLUSTERED ([IdGrad] ASC)
go

ALTER TABLE [Grad]
	ADD CONSTRAINT [XAK1Grad] UNIQUE ([PostanskiBroj]  ASC)
go

ALTER TABLE [JeParkirano]
	ADD CONSTRAINT [XPKJeParkirano] PRIMARY KEY  CLUSTERED ([RegistracioniBroj] ASC)
go

ALTER TABLE [JeUMagacinu]
	ADD CONSTRAINT [XPKJeUMagacinu] PRIMARY KEY  CLUSTERED ([IdPaket] ASC)
go

ALTER TABLE [Korisnik]
	ADD CONSTRAINT [XPKKorisnik] PRIMARY KEY  CLUSTERED ([KorisnickoIme] ASC)
go

ALTER TABLE [Kurir]
	ADD CONSTRAINT [XPKKurir] PRIMARY KEY  CLUSTERED ([KorisnickoIme] ASC)
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

ALTER TABLE [Planiranje]
	ADD CONSTRAINT [XPKPlaniranje] PRIMARY KEY  CLUSTERED ([IdPlaniranje] ASC)
go

ALTER TABLE [SePrevozi]
	ADD CONSTRAINT [XPKSePrevozi] PRIMARY KEY  CLUSTERED ([IdPaket] ASC)
go

ALTER TABLE [Vozi]
	ADD CONSTRAINT [XPKVozi] PRIMARY KEY  CLUSTERED ([KorisnickoIme] ASC)
go

ALTER TABLE [Vozilo]
	ADD CONSTRAINT [XPKVozilo] PRIMARY KEY  CLUSTERED ([RegistracioniBroj] ASC)
go

ALTER TABLE [Vozilo]
	ADD CONSTRAINT [XAK1Vozilo] UNIQUE ([RegistracioniBroj]  ASC)
go

ALTER TABLE [Voznja]
	ADD CONSTRAINT [XPKVoznja] PRIMARY KEY  CLUSTERED ([IdVoznja] ASC)
go

ALTER TABLE [Zahtev]
	ADD CONSTRAINT [XPKZahtev] PRIMARY KEY  CLUSTERED ([KorisnickoIme] ASC)
go

ALTER TABLE [Zahtev]
	ADD CONSTRAINT [XAK1Zahtev] UNIQUE ([BrojVozackeDozvole]  ASC)
go


ALTER TABLE [Administrator]
	ADD CONSTRAINT [R_6] FOREIGN KEY ([KorisnickoIme]) REFERENCES [Korisnik]([KorisnickoIme])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go


ALTER TABLE [Adresa]
	ADD CONSTRAINT [R_1] FOREIGN KEY ([IdGrad]) REFERENCES [Grad]([IdGrad])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [JeParkirano]
	ADD CONSTRAINT [R_17] FOREIGN KEY ([RegistracioniBroj]) REFERENCES [Vozilo]([RegistracioniBroj])
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
	ADD CONSTRAINT [R_5] FOREIGN KEY ([KorisnickoIme]) REFERENCES [Korisnik]([KorisnickoIme])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go


ALTER TABLE [Magacin]
	ADD CONSTRAINT [R_2] FOREIGN KEY ([IdAdresa]) REFERENCES [Adresa]([IdAdresa])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Paket]
	ADD CONSTRAINT [R_13] FOREIGN KEY ([KorisnickoIme]) REFERENCES [Korisnik]([KorisnickoIme])
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


ALTER TABLE [Planiranje]
	ADD CONSTRAINT [R_28] FOREIGN KEY ([IdPaket]) REFERENCES [Paket]([IdPaket])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Planiranje]
	ADD CONSTRAINT [R_29] FOREIGN KEY ([KorisnickoIme]) REFERENCES [Vozi]([KorisnickoIme])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Planiranje]
	ADD CONSTRAINT [R_30] FOREIGN KEY ([IdAdresa]) REFERENCES [Adresa]([IdAdresa])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [SePrevozi]
	ADD CONSTRAINT [R_19] FOREIGN KEY ([IdPaket]) REFERENCES [Paket]([IdPaket])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [SePrevozi]
	ADD CONSTRAINT [R_25] FOREIGN KEY ([KorisnickoIme]) REFERENCES [Vozi]([KorisnickoIme])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Vozi]
	ADD CONSTRAINT [R_10] FOREIGN KEY ([RegistracioniBroj]) REFERENCES [Vozilo]([RegistracioniBroj])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Vozi]
	ADD CONSTRAINT [R_11] FOREIGN KEY ([KorisnickoIme]) REFERENCES [Kurir]([KorisnickoIme])
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
	ADD CONSTRAINT [R_8] FOREIGN KEY ([KorisnickoIme]) REFERENCES [Kurir]([KorisnickoIme])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Voznja]
	ADD CONSTRAINT [R_9] FOREIGN KEY ([RegistracioniBroj]) REFERENCES [Vozilo]([RegistracioniBroj])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Voznja]
	ADD CONSTRAINT [R_24] FOREIGN KEY ([IdMagacin]) REFERENCES [Magacin]([IdMagacin])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Zahtev]
	ADD CONSTRAINT [R_7] FOREIGN KEY ([KorisnickoIme]) REFERENCES [Korisnik]([KorisnickoIme])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go
