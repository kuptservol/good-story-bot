CREATE DATABASE good_story;

CREATE USER good_story WITH password 'good_story';

GRANT ALL privileges ON DATABASE good_story TO good_story;

CREATE TABLE story
(
  id       BIGINT            NOT NULL
    CONSTRAINT story_pkey
    PRIMARY KEY,
  author   VARCHAR(255),
  genre    VARCHAR(20)       NOT NULL,
  language VARCHAR(20)       NOT NULL,
  length   VARCHAR(20)       NOT NULL,
  rating   INTEGER,
  text     TEXT              NOT NULL,
  type     VARCHAR(20)       NOT NULL,
  link     VARCHAR(250),
  name     VARCHAR(200)      NOT NULL,
  part     INTEGER DEFAULT 1 NOT NULL,
  year     INTEGER
);

CREATE INDEX story_language_length_genre_type_index
  ON story (language, length, genre, type);

CREATE TABLE story_seen
(
  story_id BIGINT  NOT NULL
    CONSTRAINT story_seen_story_id_fk
    REFERENCES story,
  user_id  INTEGER NOT NULL
);

CREATE UNIQUE INDEX story_seen_story_id_user_id_uindex
  ON story_seen (story_id, user_id);

