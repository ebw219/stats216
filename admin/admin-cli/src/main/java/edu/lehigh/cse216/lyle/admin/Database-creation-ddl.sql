CREATE TABLE IF NOT EXISTS tblUser (
  user_id SERIAL PRIMARY KEY,
  username VARCHAR(255),
  realname VARCHAR(255),
  email VARCHAR(255),
  salt BYTEA,
  password BYTEA
);
CREATE TABLE IF NOT EXISTS tblMessage (
  message_id SERIAL PRIMARY KEY,
  user_id INTEGER, title VARCHAR(50),
  body VARCHAR(140),
--   # Need to add creation date/time
  FOREIGN KEY (user_id) REFERENCES tblUser (user_id)
);
CREATE TABLE IF NOT EXISTS tblComments (
  comment_id SERIAL PRIMARY KEY,
  user_id INTEGER,
  message_id INTEGER,
  comment_text VARCHAR(255),
  # Need to add creation date/time
  FOREIGN KEY (user_id) REFERENCES tblUser (user_id),
  FOREIGN KEY (message_id) REFERENCES tblMessage (message_id)
);
CREATE TABLE IF NOT EXISTS tblUpVotes (
  user_id INTEGER,
  message_id INTEGER,
  FOREIGN KEY (user_id) REFERENCES tblUser (user_id),
  FOREIGN KEY (message_id) REFERENCES tblMessage (message_id),
  PRIMARY KEY (user_id, message_id)
);
CREATE TABLE IF NOT EXISTS tblDownVotes (
  user_id INTEGER,
  message_id INTEGER,
  FOREIGN KEY (user_id) REFERENCES tblUser (user_id),
  FOREIGN KEY (message_id) REFERENCES tblMessage (message_id),
  PRIMARY KEY (user_id, message_id)
);
CREATE TABLE IF NOT EXISTS tblDocs (
  doc_id INTEGER PRIMARY KEY,
  FOREIGN KEY (owner_id) REFERENCES tblUser (user_id)
);