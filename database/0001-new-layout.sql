--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

SET search_path = public, pg_catalog;

--
-- Name: SEQ_ACCOUNT_ID; Type: SEQUENCE; Schema: public; Owner: zeisss
--

CREATE SEQUENCE "SEQ_ACCOUNT_ID"
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public."SEQ_ACCOUNT_ID" OWNER TO zeisss;

--
-- Name: SEQ_ACCOUNT_ID; Type: SEQUENCE SET; Schema: public; Owner: zeisss
--

SELECT pg_catalog.setval('"SEQ_ACCOUNT_ID"', 230, true);


--
-- Name: SEQ_ALGEBRA_ID; Type: SEQUENCE; Schema: public; Owner: zeisss
--

CREATE SEQUENCE "SEQ_ALGEBRA_ID"
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public."SEQ_ALGEBRA_ID" OWNER TO zeisss;

--
-- Name: SEQ_ALGEBRA_ID; Type: SEQUENCE SET; Schema: public; Owner: zeisss
--

SELECT pg_catalog.setval('"SEQ_ALGEBRA_ID"', 496, true);


--
-- Name: SEQ_ATP_ID; Type: SEQUENCE; Schema: public; Owner: zeisss
--

CREATE SEQUENCE "SEQ_ATP_ID"
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public."SEQ_ATP_ID" OWNER TO zeisss;

--
-- Name: SEQ_ATP_ID; Type: SEQUENCE SET; Schema: public; Owner: zeisss
--

SELECT pg_catalog.setval('"SEQ_ATP_ID"', 1, false);


--
-- Name: SEQ_FORMULA_ID; Type: SEQUENCE; Schema: public; Owner: zeisss
--

CREATE SEQUENCE "SEQ_FORMULA_ID"
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public."SEQ_FORMULA_ID" OWNER TO zeisss;

--
-- Name: SEQ_FORMULA_ID; Type: SEQUENCE SET; Schema: public; Owner: zeisss
--

SELECT pg_catalog.setval('"SEQ_FORMULA_ID"', 514, true);


--
-- Name: SEQ_PROOF_ID; Type: SEQUENCE; Schema: public; Owner: zeisss
--

CREATE SEQUENCE "SEQ_PROOF_ID"
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public."SEQ_PROOF_ID" OWNER TO zeisss;

--
-- Name: SEQ_PROOF_ID; Type: SEQUENCE SET; Schema: public; Owner: zeisss
--

SELECT pg_catalog.setval('"SEQ_PROOF_ID"', 118, true);


--
-- Name: SEQ_QUEUEJOB_ID; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE "SEQ_QUEUEJOB_ID"
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public."SEQ_QUEUEJOB_ID" OWNER TO postgres;

--
-- Name: SEQ_QUEUEJOB_ID; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('"SEQ_QUEUEJOB_ID"', 302, true);


SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: account; Type: TABLE; Schema: public; Owner: zeisss; Tablespace: 
--

CREATE TABLE account (
    id bigint NOT NULL,
    display_name character varying,
    login_name character varying(255),
    login_password character varying,
    email character varying
);


ALTER TABLE public.account OWNER TO zeisss;

--
-- Name: algebra; Type: TABLE; Schema: public; Owner: zeisss; Tablespace: 
--

CREATE TABLE algebra (
    id bigint NOT NULL,
    name character varying(30) NOT NULL
);


ALTER TABLE public.algebra OWNER TO zeisss;

--
-- Name: algebra_formula; Type: TABLE; Schema: public; Owner: zeisss; Tablespace: 
--

CREATE TABLE algebra_formula (
    algebra_id bigint NOT NULL,
    formula_id bigint NOT NULL,
    axiom boolean
);


ALTER TABLE public.algebra_formula OWNER TO zeisss;

--
-- Name: atp; Type: TABLE; Schema: public; Owner: zeisss; Tablespace: 
--

CREATE TABLE atp (
    id bigint NOT NULL,
    name character varying(255),
    path character varying(255)
);


ALTER TABLE public.atp OWNER TO zeisss;

--
-- Name: atp_option; Type: TABLE; Schema: public; Owner: zeisss; Tablespace: 
--

CREATE TABLE atp_option (
    atp_id bigint NOT NULL,
    option_name character varying(40) NOT NULL,
    option_description character varying(255),
    option_value character varying(255)
);


ALTER TABLE public.atp_option OWNER TO zeisss;

--
-- Name: formula; Type: TABLE; Schema: public; Owner: zeisss; Tablespace: 
--

CREATE TABLE formula (
    id bigint NOT NULL,
    name character varying(255),
    comment text,
    formula_text text
);


ALTER TABLE public.formula OWNER TO zeisss;

--
-- Name: formula_reference; Type: TABLE; Schema: public; Owner: zeisss; Tablespace: 
--

CREATE TABLE formula_reference (
    formula_id bigint NOT NULL,
    abbreviation character varying(30) NOT NULL,
    authors character varying(255),
    title character varying(255),
    year integer,
    CONSTRAINT "EK_FORMULA_REFERENCES_YEAR" CHECK ((year > 0))
);


ALTER TABLE public.formula_reference OWNER TO zeisss;

--
-- Name: proof; Type: TABLE; Schema: public; Owner: zeisss; Tablespace: 
--

CREATE TABLE proof (
    id bigint NOT NULL,
    "timestamp" timestamp with time zone
);


ALTER TABLE public.proof OWNER TO zeisss;

--
-- Name: proof_detail; Type: TABLE; Schema: public; Owner: zeisss; Tablespace: 
--

CREATE TABLE proof_detail (
    proof_id bigint NOT NULL,
    detail_name character varying(100) NOT NULL,
    detail_value character varying
);


ALTER TABLE public.proof_detail OWNER TO zeisss;

--
-- Name: proof_formula_proves; Type: TABLE; Schema: public; Owner: zeisss; Tablespace: 
--

CREATE TABLE proof_formula_proves (
    proof_id bigint NOT NULL,
    formula_id bigint NOT NULL
);


ALTER TABLE public.proof_formula_proves OWNER TO zeisss;

--
-- Name: proof_formula_used; Type: TABLE; Schema: public; Owner: zeisss; Tablespace: 
--

CREATE TABLE proof_formula_used (
    formula_id bigint NOT NULL,
    proof_id bigint NOT NULL
);


ALTER TABLE public.proof_formula_used OWNER TO zeisss;

--
-- Name: proof_step; Type: TABLE; Schema: public; Owner: zeisss; Tablespace: 
--

CREATE TABLE proof_step (
    proof_id bigint NOT NULL,
    line bigint NOT NULL,
    formula character varying,
    reasoning character varying
);


ALTER TABLE public.proof_step OWNER TO zeisss;

--
-- Name: queuejob; Type: TABLE; Schema: public; Owner: zeisss; Tablespace: 
--

CREATE TABLE queuejob (
    id bigint NOT NULL,
    atp_id bigint,
    goal_formula text,
    proof_id bigint,
    account_id bigint,
    status integer,
    "timestamp" timestamp with time zone
);


ALTER TABLE public.queuejob OWNER TO zeisss;

--
-- Name: queuejob_formulas; Type: TABLE; Schema: public; Owner: zeisss; Tablespace: 
--

CREATE TABLE queuejob_formulas (
    queuejob_id bigint NOT NULL,
    formula_id bigint NOT NULL
);


ALTER TABLE public.queuejob_formulas OWNER TO zeisss;

--
-- Data for Name: account; Type: TABLE DATA; Schema: public; Owner: zeisss
--

COPY account (id, display_name, login_name, login_password, email) FROM stdin;
\.


--
-- Data for Name: algebra; Type: TABLE DATA; Schema: public; Owner: zeisss
--

COPY algebra (id, name) FROM stdin;
\.


--
-- Data for Name: algebra_formula; Type: TABLE DATA; Schema: public; Owner: zeisss
--

COPY algebra_formula (algebra_id, formula_id, axiom) FROM stdin;
\.


--
-- Data for Name: atp; Type: TABLE DATA; Schema: public; Owner: zeisss
--

COPY atp (id, name, path) FROM stdin;
\.


--
-- Data for Name: atp_option; Type: TABLE DATA; Schema: public; Owner: zeisss
--

COPY atp_option (atp_id, option_name, option_description, option_value) FROM stdin;
\.


--
-- Data for Name: formula; Type: TABLE DATA; Schema: public; Owner: zeisss
--

COPY formula (id, name, comment, formula_text) FROM stdin;
\.


--
-- Data for Name: formula_reference; Type: TABLE DATA; Schema: public; Owner: zeisss
--

COPY formula_reference (formula_id, abbreviation, authors, title, year) FROM stdin;
\.


--
-- Data for Name: proof; Type: TABLE DATA; Schema: public; Owner: zeisss
--

COPY proof (id, "timestamp") FROM stdin;
\.


--
-- Data for Name: proof_detail; Type: TABLE DATA; Schema: public; Owner: zeisss
--

COPY proof_detail (proof_id, detail_name, detail_value) FROM stdin;
\.


--
-- Data for Name: proof_formula_proves; Type: TABLE DATA; Schema: public; Owner: zeisss
--

COPY proof_formula_proves (proof_id, formula_id) FROM stdin;
\.


--
-- Data for Name: proof_formula_used; Type: TABLE DATA; Schema: public; Owner: zeisss
--

COPY proof_formula_used (formula_id, proof_id) FROM stdin;
\.


--
-- Data for Name: proof_step; Type: TABLE DATA; Schema: public; Owner: zeisss
--

COPY proof_step (proof_id, line, formula, reasoning) FROM stdin;
\.


--
-- Data for Name: queuejob; Type: TABLE DATA; Schema: public; Owner: zeisss
--

COPY queuejob (id, atp_id, goal_formula, proof_id, account_id, status, "timestamp") FROM stdin;
\.


--
-- Data for Name: queuejob_formulas; Type: TABLE DATA; Schema: public; Owner: zeisss
--

COPY queuejob_formulas (queuejob_id, formula_id) FROM stdin;
\.


--
-- Name: PK_ACCOUNT_ID; Type: CONSTRAINT; Schema: public; Owner: zeisss; Tablespace: 
--

ALTER TABLE ONLY account
    ADD CONSTRAINT "PK_ACCOUNT_ID" PRIMARY KEY (id);


--
-- Name: PK_ALGEBRA_FORMULA; Type: CONSTRAINT; Schema: public; Owner: zeisss; Tablespace: 
--

ALTER TABLE ONLY algebra_formula
    ADD CONSTRAINT "PK_ALGEBRA_FORMULA" PRIMARY KEY (algebra_id, formula_id);


--
-- Name: PK_ALGEBRA_ID; Type: CONSTRAINT; Schema: public; Owner: zeisss; Tablespace: 
--

ALTER TABLE ONLY algebra
    ADD CONSTRAINT "PK_ALGEBRA_ID" PRIMARY KEY (id);


--
-- Name: PK_ATP_ID; Type: CONSTRAINT; Schema: public; Owner: zeisss; Tablespace: 
--

ALTER TABLE ONLY atp
    ADD CONSTRAINT "PK_ATP_ID" PRIMARY KEY (id);


--
-- Name: PK_ATP_OPTION_ID; Type: CONSTRAINT; Schema: public; Owner: zeisss; Tablespace: 
--

ALTER TABLE ONLY atp_option
    ADD CONSTRAINT "PK_ATP_OPTION_ID" PRIMARY KEY (atp_id, option_name);


--
-- Name: PK_FORMULA_ID; Type: CONSTRAINT; Schema: public; Owner: zeisss; Tablespace: 
--

ALTER TABLE ONLY formula
    ADD CONSTRAINT "PK_FORMULA_ID" PRIMARY KEY (id);


--
-- Name: PK_FORMULA_REFERENCES; Type: CONSTRAINT; Schema: public; Owner: zeisss; Tablespace: 
--

ALTER TABLE ONLY formula_reference
    ADD CONSTRAINT "PK_FORMULA_REFERENCES" PRIMARY KEY (formula_id, abbreviation);


--
-- Name: PK_PROOF_DETAIL; Type: CONSTRAINT; Schema: public; Owner: zeisss; Tablespace: 
--

ALTER TABLE ONLY proof_detail
    ADD CONSTRAINT "PK_PROOF_DETAIL" PRIMARY KEY (proof_id, detail_name);


--
-- Name: PK_PROOF_FORMULA_PROVES; Type: CONSTRAINT; Schema: public; Owner: zeisss; Tablespace: 
--

ALTER TABLE ONLY proof_formula_proves
    ADD CONSTRAINT "PK_PROOF_FORMULA_PROVES" PRIMARY KEY (proof_id, formula_id);


--
-- Name: PK_PROOF_STEPS; Type: CONSTRAINT; Schema: public; Owner: zeisss; Tablespace: 
--

ALTER TABLE ONLY proof_step
    ADD CONSTRAINT "PK_PROOF_STEPS" PRIMARY KEY (proof_id, line);


--
-- Name: PK_PROVE_FORMULAS_USED; Type: CONSTRAINT; Schema: public; Owner: zeisss; Tablespace: 
--

ALTER TABLE ONLY proof_formula_used
    ADD CONSTRAINT "PK_PROVE_FORMULAS_USED" PRIMARY KEY (formula_id, proof_id);


--
-- Name: PK_PROVE_ID; Type: CONSTRAINT; Schema: public; Owner: zeisss; Tablespace: 
--

ALTER TABLE ONLY proof
    ADD CONSTRAINT "PK_PROVE_ID" PRIMARY KEY (id);


--
-- Name: PK_QUEUEJOB_FORMULAS_ID; Type: CONSTRAINT; Schema: public; Owner: zeisss; Tablespace: 
--

ALTER TABLE ONLY queuejob_formulas
    ADD CONSTRAINT "PK_QUEUEJOB_FORMULAS_ID" PRIMARY KEY (queuejob_id, formula_id);


--
-- Name: PK_QUEUEJOB_ID; Type: CONSTRAINT; Schema: public; Owner: zeisss; Tablespace: 
--

ALTER TABLE ONLY queuejob
    ADD CONSTRAINT "PK_QUEUEJOB_ID" PRIMARY KEY (id);


--
-- Name: UI_ACCOUNT_DISPLAYNAME; Type: CONSTRAINT; Schema: public; Owner: zeisss; Tablespace: 
--

ALTER TABLE ONLY account
    ADD CONSTRAINT "UI_ACCOUNT_DISPLAYNAME" UNIQUE (display_name);


--
-- Name: UI_ACCOUNT_NAME; Type: CONSTRAINT; Schema: public; Owner: zeisss; Tablespace: 
--

ALTER TABLE ONLY account
    ADD CONSTRAINT "UI_ACCOUNT_NAME" UNIQUE (login_name);


--
-- Name: FK_ALGEBRA_FORMULA_ALGEBRA_ID; Type: FK CONSTRAINT; Schema: public; Owner: zeisss
--

ALTER TABLE ONLY algebra_formula
    ADD CONSTRAINT "FK_ALGEBRA_FORMULA_ALGEBRA_ID" FOREIGN KEY (algebra_id) REFERENCES algebra(id);


--
-- Name: FK_ALGEBRA_FORMULA_FORMULA_ID; Type: FK CONSTRAINT; Schema: public; Owner: zeisss
--

ALTER TABLE ONLY algebra_formula
    ADD CONSTRAINT "FK_ALGEBRA_FORMULA_FORMULA_ID" FOREIGN KEY (formula_id) REFERENCES formula(id);


--
-- Name: FK_FORMULA_REFERENCES_FORMULA_ID; Type: FK CONSTRAINT; Schema: public; Owner: zeisss
--

ALTER TABLE ONLY formula_reference
    ADD CONSTRAINT "FK_FORMULA_REFERENCES_FORMULA_ID" FOREIGN KEY (formula_id) REFERENCES formula(id) ON DELETE CASCADE;


--
-- Name: FK_PROOF_DETAIL_PROOF_ID; Type: FK CONSTRAINT; Schema: public; Owner: zeisss
--

ALTER TABLE ONLY proof_detail
    ADD CONSTRAINT "FK_PROOF_DETAIL_PROOF_ID" FOREIGN KEY (proof_id) REFERENCES proof(id) ON DELETE CASCADE;


--
-- Name: FK_PROOF_FORMULA_FORMULA; Type: FK CONSTRAINT; Schema: public; Owner: zeisss
--

ALTER TABLE ONLY proof_formula_proves
    ADD CONSTRAINT "FK_PROOF_FORMULA_FORMULA" FOREIGN KEY (formula_id) REFERENCES formula(id);


--
-- Name: FK_PROOF_FORMULA_PROVE_ID; Type: FK CONSTRAINT; Schema: public; Owner: zeisss
--

ALTER TABLE ONLY proof_formula_proves
    ADD CONSTRAINT "FK_PROOF_FORMULA_PROVE_ID" FOREIGN KEY (proof_id) REFERENCES proof(id);


--
-- Name: FK_PROOF_STEP_PROOF_ID; Type: FK CONSTRAINT; Schema: public; Owner: zeisss
--

ALTER TABLE ONLY proof_step
    ADD CONSTRAINT "FK_PROOF_STEP_PROOF_ID" FOREIGN KEY (proof_id) REFERENCES proof(id) ON DELETE CASCADE;


--
-- Name: FK_PROVE_FORMULA_ID; Type: FK CONSTRAINT; Schema: public; Owner: zeisss
--

ALTER TABLE ONLY proof_formula_used
    ADD CONSTRAINT "FK_PROVE_FORMULA_ID" FOREIGN KEY (formula_id) REFERENCES formula(id);


--
-- Name: FK_PROVE_PROVE_ID; Type: FK CONSTRAINT; Schema: public; Owner: zeisss
--

ALTER TABLE ONLY proof_formula_used
    ADD CONSTRAINT "FK_PROVE_PROVE_ID" FOREIGN KEY (proof_id) REFERENCES proof(id);


--
-- Name: FK_QUEUEJOB_ACCOUNT_ID; Type: FK CONSTRAINT; Schema: public; Owner: zeisss
--

ALTER TABLE ONLY queuejob
    ADD CONSTRAINT "FK_QUEUEJOB_ACCOUNT_ID" FOREIGN KEY (account_id) REFERENCES account(id);


--
-- Name: FK_QUEUEJOB_ATP_ID; Type: FK CONSTRAINT; Schema: public; Owner: zeisss
--

ALTER TABLE ONLY queuejob
    ADD CONSTRAINT "FK_QUEUEJOB_ATP_ID" FOREIGN KEY (atp_id) REFERENCES atp(id);


--
-- Name: FK_QUEUEJOB_PROVE_ID; Type: FK CONSTRAINT; Schema: public; Owner: zeisss
--

ALTER TABLE ONLY queuejob
    ADD CONSTRAINT "FK_QUEUEJOB_PROVE_ID" FOREIGN KEY (proof_id) REFERENCES proof(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: zeisss
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM zeisss;
GRANT ALL ON SCHEMA public TO zeisss;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

