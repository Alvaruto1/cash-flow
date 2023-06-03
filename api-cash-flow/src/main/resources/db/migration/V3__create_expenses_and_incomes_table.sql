CREATE TABLE public.incomes_expenses
(
    id             uuid      NOT NULL PRIMARY KEY,
    description    varchar   NOT NULL DEFAULT '':: character varying,
    payment_method varchar   NOT NULL DEFAULT '':: character varying,
    category       varchar   NOT NULL DEFAULT '':: character varying,
    date           timestamp NOT NULL,
    type           varchar   NOT NULL DEFAULT '':: character varying,
    account_id     uuid      NOT NULL,
    CONSTRAINT fk_account
        FOREIGN KEY (account_id)
            REFERENCES public.accounts (id)
);
