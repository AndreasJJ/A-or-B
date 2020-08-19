DROP TABLE tickets;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE tickets (
	id uuid NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
	token TEXT NOT NULL,
	timestamp TIMESTAMP NOT NULL
);