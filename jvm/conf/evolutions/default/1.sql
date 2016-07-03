# --- !Ups

CREATE TABLE "Task" (
    "id" BIGINT PRIMARY KEY AUTO_INCREMENT,
    "title" VARCHAR(255) NOT NULL,
    "completed" BOOLEAN NOT NULL
);

INSERT INTO "Task"("title", "completed") VALUES('Learn Scala', true);
INSERT INTO "Task"("title", "completed") VALUES('Learn AngularJS', false);
INSERT INTO "Task"("title", "completed") VALUES('Setup Scala.js', false);
INSERT INTO "Task"("title", "completed") VALUES('Setup scalajs-angular', false);
INSERT INTO "Task"("title", "completed") VALUES('Make something useful!', false);

# --- !Downs

DROP TABLE "Task";
