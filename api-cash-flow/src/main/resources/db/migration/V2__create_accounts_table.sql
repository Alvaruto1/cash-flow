CREATE TABLE public.accounts (
    id uuid NOT NULL PRIMARY KEY,
    description varchar NOT NULL DEFAULT ''::character varying
);

CREATE TABLE public.user_account (
    user_id uuid NOT NULL REFERENCES public.users(id) ON DELETE CASCADE,
    account_id uuid NOT NULL REFERENCES public.accounts(id) ON DELETE CASCADE,
    CONSTRAINT user_account_pkey PRIMARY KEY (user_id, account_id)
);