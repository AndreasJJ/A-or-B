CREATE TABLE tickets (
	id SERIAL NOT NULL PRIMARY KEY,
	token TEXT NOT NULL,
	timestamp TIMESTAMP NOT NULL
);