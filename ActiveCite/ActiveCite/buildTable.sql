drop table RefInfo;

drop table Paper;

drop table CitedInfo;

drop table Contextual_Info;

CREATE TABLE RefInfo
(
	refPos INTEGER NOT NULL,
	title  char(200)
);

CREATE TABLE Paper
(
	title char(200),
	author char(200),
	conference char(100),
	publication_year char(10),
	abstract char(200),
	Analysis char(200)
);

CREATE TABLE CitedInfo
(
	sourcePaper char(200),
	targetPaper char(200)
);

CREATE TABLE Contextual_Info
(
	refPos INTEGER,
	keywords char(200),
	keySentences char(200),
	publication_year char(10),
	conference char(100),
	author char(200)
	
);
