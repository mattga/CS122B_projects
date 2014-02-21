CREATE USER 'testuser'@'localhost' IDENTIFIED BY 'testpass';
GRANT ALL PRIVILEGES ON documentdb.* TO 'testuser'@'localhost';
FLUSH PRIVILEGES;