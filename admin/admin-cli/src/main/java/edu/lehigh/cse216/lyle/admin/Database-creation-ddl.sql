CREATE TABLE IF NOT EXISTS tblUser (
  user_id SERIAL PRIMARY KEY,
  username VARCHAR(255) NOT NULL UNIQUE ,
  realname VARCHAR(255) NOT NULL ,
  email VARCHAR(255) NOT NULL UNIQUE
);
CREATE TABLE IF NOT EXISTS tblMessage (
  message_id SERIAL PRIMARY KEY UNIQUE,
  user_id INTEGER,
  title VARCHAR(50) NOT NULL,
  body VARCHAR(140) NOT NULL,
  date_created DATE DEFAULT now(),
  FOREIGN KEY (user_id) REFERENCES tblUser (user_id) ON DELETE CASCADE,
  pdf VARCHAR(75),
  link VARCHAR(140),
  image VARCHAR(50)
);
-- ALTER TABLE tblMessage
-- ADD  FOREIGN KEY (user_id) REFERENCES tblUser (user_id)
-- ON DELETE CASCADE;

CREATE TABLE IF NOT EXISTS tblComments (
  comment_id SERIAL PRIMARY KEY,
  UNIQUE (comment_id),
  user_id INTEGER,
  message_id INTEGER,
  comment_text VARCHAR(255) NOT NULL,
  date_created DATE DEFAULT now(),
  FOREIGN KEY (user_id) REFERENCES tblUser (user_id) ON DELETE CASCADE,
  FOREIGN KEY (message_id) REFERENCES tblMessage (message_id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS tblUpVotes (
  user_id INTEGER,
  message_id INTEGER,
  date_created DATE DEFAULT now(),
  FOREIGN KEY (user_id) REFERENCES tblUser (user_id) ON DELETE CASCADE ,
  FOREIGN KEY (message_id) REFERENCES tblMessage (message_id) ON DELETE CASCADE ,
  PRIMARY KEY (user_id, message_id)
);
CREATE TABLE IF NOT EXISTS tblDownVotes (
  user_id INTEGER,
  message_id INTEGER,
  date_created DATE DEFAULT now(),
  FOREIGN KEY (user_id) REFERENCES tblUser (user_id) ON DELETE CASCADE ,
  FOREIGN KEY (message_id) REFERENCES tblMessage (message_id) ON DELETE CASCADE ,
  PRIMARY KEY (user_id, message_id)
);
CREATE TABLE IF NOT EXISTS tblDocs (
  doc_owner_id INTEGER,
  doc_id INTEGER PRIMARY KEY UNIQUE ,
  doc_title VARCHAR(140) NOT NULL,
  date_created DATE DEFAULT now(),
  FOREIGN KEY (doc_owner_id) REFERENCES tblUser (user_id) ON DELETE CASCADE
);



-- ALTER TABLE "public"."tblmessage" ADD COLUMN "pdf_id" character varying(75);
