CREATE TABLE public.users (
    id uuid NOT NULL PRIMARY KEY,
    username varchar NOT NULL DEFAULT ''::character varying,
    password varchar NOT NULL DEFAULT ''::character varying
);

CREATE TABLE public.authorities (
    id uuid NOT NULL PRIMARY KEY,
    permission varchar NOT NULL DEFAULT ''::character varying,
    endpoint varchar NOT NULL DEFAULT ''::character varying
);

CREATE TABLE public.user_authority (
    user_id uuid NOT NULL REFERENCES public.users(id) ON DELETE CASCADE,
    authority_id uuid NOT NULL REFERENCES public.authorities(id) ON DELETE CASCADE,
    CONSTRAINT user_authority_pkey PRIMARY KEY (user_id, authority_id)
);