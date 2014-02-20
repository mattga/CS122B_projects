DROP TABLE IF EXISTS tbl_genres CASCADE;
CREATE TABLE tbl_genres (
	id  INTEGER  NOT NULL AUTO_INCREMENT,
	genre_name   VARCHAR(20),
	PRIMARY KEY (id)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS tbl_people CASCADE;
CREATE TABLE tbl_people (
	id   INTEGER  NOT NULL AUTO_INCREMENT,
	name         VARCHAR(61) CHARACTER SET utf8,
	PRIMARY KEY (id)
)ENGINE=InnoDB;

DROP TABLE IF EXISTS tbl_booktitle CASCADE;
CREATE TABLE tbl_booktitle  (
	id  INTEGER   NOT NULL AUTO_INCREMENT,
	title        VARCHAR(300) CHARACTER SET utf8,
	PRIMARY KEY (id)
)ENGINE=InnoDB;

DROP TABLE IF EXISTS tbl_publisher CASCADE;
CREATE TABLE tbl_publisher (
	id   INTEGER    NOT NULL AUTO_INCREMENT,
	publisher_name varchar(300),
	PRIMARY KEY (id)
)ENGINE=InnoDB;

DROP TABLE IF EXISTS tbl_dblp_document CASCADE;
CREATE TABLE tbl_dblp_document (
	id   INTEGER  NOT NULL AUTO_INCREMENT,
	title         VARCHAR(300) CHARACTER SET utf8,
	start_page    INTEGER,
	end_page      INTEGER,
	year          INTEGER,
	volume        INTEGER,
	number        INTEGER,
	url           VARCHAR(200),
	ee            VARCHAR(100),
	cdrom         VARCHAR(75),
	cite          VARCHAR(75),
	crossref      VARCHAR(75),
	isbn          VARCHAR(21),
	series        VARCHAR(100), 
	editor_id    INTEGER NULL,
	booktitle_id    INTEGER NULL,
	genre_id    INTEGER NULL,
	publisher_id    INTEGER NULL,        
	FOREIGN KEY (editor_id)     REFERENCES tbl_people(id),
	FOREIGN KEY (booktitle_id)  REFERENCES tbl_booktitle(id),
	FOREIGN KEY (genre_id)      REFERENCES tbl_genres(id),
	FOREIGN KEY (publisher_id)  REFERENCES tbl_publisher(id),
	PRIMARY KEY (id)
)ENGINE=InnoDB;


DROP TABLE IF EXISTS tbl_author_document_mapping CASCADE;
CREATE TABLE tbl_author_document_mapping (
    id   INTEGER   NOT NULL AUTO_INCREMENT,
    doc_id    INTEGER NOT NULL,
    author_id    INTEGER NOT NULL,
    FOREIGN KEY (doc_id)        REFERENCES tbl_dblp_document(id),
    FOREIGN KEY (author_id)     REFERENCES tbl_people(id) ,
    PRIMARY KEY (id)
)ENGINE=InnoDB;
