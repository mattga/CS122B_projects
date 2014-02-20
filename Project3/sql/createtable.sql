CREATE DATABASE IF NOT EXISTS moviedb;

USE moviedb;

CREATE TABLE IF NOT EXISTS movies (
	id int PRIMARY KEY auto_increment,
	title varchar(100) NOT NULL,
	year int NOT NULL,
	director varchar(100) NOT NULL,
	banner_url varchar(200),
	trailer_url varchar(200)
);

CREATE TABLE IF NOT EXISTS stars (
	id int PRIMARY KEY auto_increment,
	first_name varchar(50) NOT NULL,
	last_name varchar(50) NOT NULL,
	dob DATE,
	photo_url varchar(200)
);

CREATE TABLE IF NOT EXISTS stars_in_movies (
	star_id int NOT NULL,
	movie_id int NOT NULL,
	FOREIGN KEY(star_id) REFERENCES stars (id),
	FOREIGN KEY(movie_id) REFERENCES movies (id)
);

CREATE TABLE IF NOT EXISTS genres (
	id int PRIMARY KEY auto_increment, 
	name varchar(32) NOT NULL
);

CREATE TABLE IF NOT EXISTS genres_in_movies (
	genre_id int NOT NULL,
	movie_id int NOT NULL,
	FOREIGN KEY(genre_id) REFERENCES genres (id),
	FOREIGN KEY(movie_id) REFERENCES movies (id)
);

CREATE TABLE IF NOT EXISTS creditcards (
	id varchar(20) PRIMARY KEY,
	first_name varchar(50) NOT NULL,
	last_name varchar(50) NOT NULL,
	expiration DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS customers (
	id int PRIMARY KEY auto_increment,
	first_name varchar(50) NOT NULL,
	last_name varchar(50) NOT NULL,
	cc_id varchar(20) NOT NULL,
	address varchar(200) NOT NULL,
	email varchar(50) NOT NULL,
	password varchar(20) NOT NULL,
	FOREIGN KEY(cc_id) REFERENCES creditcards (id)
);

CREATE TABLE IF NOT EXISTS sales (
	id int PRIMARY KEY auto_increment,
	customer_id int NOT NULL,
	movie_id int NOT NULL,
	sale_date DATE NOT NULL,
	FOREIGN KEY(customer_id) REFERENCES customers (id),
	FOREIGN KEY(movie_id) REFERENCES movies (id)
);

CREATE TABLE IF NOT EXISTS employees (
	email varchar(50) PRIMARY KEY,
	password varchar(20) NOT NULL,
	fullname varchar(100)
);