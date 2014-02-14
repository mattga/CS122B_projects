delimiter //
CREATE PROCEDURE moviedb.add_movie (title varchar(100),
									year int,
									director varchar(100),
									star_fn varchar(50),
									star_ln varchar(50),
									genre_name varchar(32))
BEGIN
	SET @genre_id = -1;
	SET @star_id = -1;

	SELECT id INTO @genre_id FROM genres 
		WHERE name=genre_name;
	IF @genre_id < 0 THEN
		SELECT "Genre does not exist. Creating...";
		INSERT INTO genres (name) VALUES (genre_name);
		SET @genre_id = LAST_INSERT_ID();
		SELECT CONCAT("Genre created with id ",@genre_id);
	ELSE
		SELECT CONCAT("Genre already exists with id ",@genre_id);
	END IF;

	SELECT id INTO @star_id FROM stars 
		WHERE first_name=star_fn AND last_name=star_ln;
	IF @star_id < 0 THEN
		SELECT "Star does not exist. Creating...";
		INSERT INTO stars (first_name, last_name) VALUES (star_fn, star_ln);
		SET @star_id = LAST_INSERT_ID();
		SELECT CONCAT("Star created with id ",@star_id);
	ELSE
		SELECT CONCAT("Star already exists with id ",@star_id);
	END IF;

	INSERT INTO movies (title, year, director) 
	VALUES (title, year, director);
	SET @movie_id = LAST_INSERT_ID();
	SELECT "Movie added with id @movie_id";

	INSERT INTO stars_in_movies VALUES (@star_id, @movie_id);
	SELECT "Star (id @star_id) and movie (id @movie_id) linked";
	INSERT INTO genres_in_movies VALUES (@genre_id, @movie_id);
	SELECT "Genre (id @genre_id) and movie (id @movie_id) linked";
END//
delimiter ;