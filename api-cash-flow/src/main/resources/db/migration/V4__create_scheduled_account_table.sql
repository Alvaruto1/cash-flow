CREATE TABLE public.scheduled_accounts
(
    id                uuid      NOT NULL PRIMARY KEY,
    start_date        timestamp NOT NULL,
    end_date          timestamp NOT NULL,
    periodicity       varchar   NOT NULL DEFAULT '':: character varying,
    income_expense_id uuid      NOT NULL,
    CONSTRAINT fk_income_expense
        FOREIGN KEY (income_expense_id)
            REFERENCES public.incomes_expenses (id)
);