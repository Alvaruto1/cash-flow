ALTER TABLE public.authorities
    ALTER COLUMN id SET DEFAULT uuid_generate_v4();