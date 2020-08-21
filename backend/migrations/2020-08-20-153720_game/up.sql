CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE games (
	id uuid NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
	owner TEXT NOT NULL,
	timestamp TIMESTAMP NOT NULL,
  title TEXT NOT NULL,
  left_text TEXT NOT NULL,
  right_text TEXT NOT NULL
);