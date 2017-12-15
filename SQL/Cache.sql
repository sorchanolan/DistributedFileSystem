CREATE TABLE Cache (
	file_id INT NOT NULL,
	user_id INT NOT NULL,
	PRIMARY KEY (file_id, user_id),
	FOREIGN KEY (file_id) REFERENCES File(id),
	FOREIGN KEY (user_id) REFERENCES Client(id)
);