CREATE TABLE movies (id int PRIMARY KEY, title TEXT NOT NULL, year int NOT NULL, director TEXT NOT NULL, banner_url TEXT, trailer_url TEXT);
CREATE TABLE stars (id int PRIMARY KEY, first_name TEXT NOT NULL, last_name TEXT NOT NULL, dob TEXT, photo_url TEXT);
CREATE TABLE stars_in_movies (star_id int NOT NULL, movie_id int NOT NULL);
CREATE TABLE genres (id int PRIMARY KEY, name TEXT NOT NULL);
CREATE TABLE genres_in_movies (genre_id int NOT NULL, movie_id int NOT NULL);