
CREATE TABLE algebra_operator
(
  algebra_id bigint NOT NULL,
  operator_id bigint NOT NULL,
  CONSTRAINT "PK_ALGEBRA_OPERATOR" PRIMARY KEY (algebra_id, operator_id),
  CONSTRAINT "FK_ALGEBRA_ID" FOREIGN KEY (algebra_id)
      REFERENCES algebra (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "FK_OPERATOR_ID" FOREIGN KEY (operator_id)
      REFERENCES "operator" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE algebra_operator OWNER TO zeisss;
 -- Table: "operator"

-- DROP TABLE "operator";

CREATE TABLE "operator"
(
  id bigint NOT NULL,
  arity integer,
  description text,
  "comment" text,
  symbol_intern text,
  CONSTRAINT "PK_OPERATOR_ID" PRIMARY KEY (id),
  CONSTRAINT operator_symbol_intern_key UNIQUE (symbol_intern)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE "operator" OWNER TO zeisss;

-- Table: operator_syntax_format

-- DROP TABLE operator_syntax_format;

CREATE TABLE operator_syntax_format
(
  "name" text NOT NULL,
  binding integer,
  notation text,
  symbol text,
  operator_id integer NOT NULL,
  CONSTRAINT "PK_OPERATOR_SYNTAX" PRIMARY KEY (operator_id, name),
  CONSTRAINT "FK_OPERATOR_SYNTAX" FOREIGN KEY (operator_id)
      REFERENCES "operator" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE operator_syntax_format OWNER TO zeisss;

