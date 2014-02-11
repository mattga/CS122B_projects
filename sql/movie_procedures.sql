delimiter //
CREATE PROCEDURE moviedb.add_movie (title varchar(100),
									year int,
									director varchar(100),
									star_fn varchar(50),
									star_ln varchar(50),
									genre_name varchar(32))
BEGIN
	INSERT INTO genres (name)
	SELECT genre_name FROM DUAL
	WHERE NOT EXISTS (SELECT * FROM genres
		WHERE name=genre_name);
	SET @genre_id = LAST_INSERT_ID();

	INSERT INTO stars (first_name, last_name)
	SELECT star_fn, star_ln FROM DUAL
	WHERE NOT EXISTS (SELECT * FROM stars
		WHERE first_name=star_fn AND last_name=star_ln);
	SET @star_id = LAST_INSERT_ID();

	INSERT INTO movies (title, year) VALUES (title, year);
	SET @movie_id = LAST_INSERT_ID();

	INSERT INTO stars_in_movies VALUES (@star_id, @movie_id);
	INSERT INTO genres_in_movies VALUES (@genre_id, @movie_id);
END//
delimiter ;