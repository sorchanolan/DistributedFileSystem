CREATE TABLE FileLock (
	id INT NOT NULL PRIMARY KEY,
	file_id INT NOT NULL,
	status TINYINT(1) NOT NULL,
	valid_until BIGINT NOT NULL,
	user_id INT NOT NULL,
	FOREIGN KEY (file_id) REFERENCES File(id)
);