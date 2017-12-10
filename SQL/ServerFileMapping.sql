CREATE TABLE ServerFileMapping (
	file_server_id INT NOT NULL,
	file_id INT NOT NULL,
	PRIMARY KEY(file_server_id, file_id),
  	FOREIGN KEY (file_id) REFERENCES File(id),
  	FOREIGN KEY (file_server_id) REFERENCES FileServer(id)
);