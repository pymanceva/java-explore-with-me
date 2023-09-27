DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS locations CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS requests CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS compilations_events CASCADE;
DROP TABLE IF EXISTS comments CASCADE;

CREATE TABLE IF NOT EXISTS users (
user_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
user_name VARCHAR NOT NULL,
user_email VARCHAR NOT NULL,
CONSTRAINT email UNIQUE (user_email)
);

CREATE TABLE IF NOT EXISTS categories (
category_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
category_name VARCHAR NOT NULL,
CONSTRAINT category_name UNIQUE (category_name)
);

CREATE TABLE IF NOT EXISTS locations (
location_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
location_lat FLOAT NOT NULL,
location_lot FLOAT NOT NULL
);

CREATE TABLE IF NOT EXISTS events (
event_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
event_annotation VARCHAR NOT NULL,
event_category_id BIGINT NOT NULL,
event_created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
event_description VARCHAR NOT NULL,
event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
event_initiator_id BIGINT NOT NULL,
event_location_id BIGINT NOT NULL,
event_paid BOOLEAN DEFAULT FALSE,
event_participant_limit INT DEFAULT 0,
event_published_on TIMESTAMP WITHOUT TIME ZONE,
event_request_moderation BOOLEAN DEFAULT TRUE,
event_state VARCHAR NOT NULL,
event_name VARCHAR NOT NULL,

CONSTRAINT fk_event_category_id FOREIGN KEY (event_category_id) REFERENCES categories (category_id),
CONSTRAINT fk_event_initiator_id FOREIGN KEY (event_initiator_id) REFERENCES users (user_id),
CONSTRAINT fk_event_location_id FOREIGN KEY (event_location_id) REFERENCES locations (location_id)
);

CREATE TABLE IF NOT EXISTS requests (
request_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
request_created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
request_event_id BIGINT NOT NULL,
request_requester_id BIGINT NOT NULL,
request_state VARCHAR NOT NULL,

CONSTRAINT fk_request_event_id FOREIGN KEY (request_event_id) REFERENCES events (event_id),
CONSTRAINT fk_request_requester_id FOREIGN KEY (request_requester_id) REFERENCES users (user_id),
CONSTRAINT uc_unique_requester_event UNIQUE (request_requester_id, request_event_id)
);

CREATE TABLE IF NOT EXISTS compilations (
compilation_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
compilation_pinned BOOLEAN DEFAULT FALSE,
compilation_title VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS compilations_events (
compilation_id BIGINT,
event_id       BIGINT,

PRIMARY KEY (compilation_id, event_id),
CONSTRAINT fk_compilation_id FOREIGN KEY (compilation_id) REFERENCES compilations (compilation_id),
CONSTRAINT fk_event_id FOREIGN KEY (event_id) REFERENCES events (event_id)
);

CREATE TABLE IF NOT EXISTS comments (
comment_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
comment_text VARCHAR NOT NULL,
comment_author_id BIGINT NOT NULL,
comment_event_id BIGINT NOT NULL,
comment_created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,

CONSTRAINT fk_comment_author_id FOREIGN KEY (comment_author_id) REFERENCES users (user_id),
CONSTRAINT fk_comment_event_id FOREIGN KEY (comment_event_id) REFERENCES events (event_id)
);