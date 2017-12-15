CREATE TABLE LockQueue (
	file_id INT NOT NULL,
	user_id INT NOT NULL,
	timestamp BIGINT NOT NULL,
	PRIMARY KEY (file_id, user_id, timestamp),
	FOREIGN KEY (file_id) REFERENCES File(id)
);