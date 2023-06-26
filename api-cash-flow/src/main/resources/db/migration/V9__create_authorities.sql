INSERT INTO public.authorities(permission, endpoint)
VALUES
    ('CREATE', 'Account'),
    ('READ', 'Account'),
    ('DELETE', 'Account'),

    ('CREATE', 'IncomeExpense'),
    ('READ', 'IncomeExpense'),
    ('DELETE', 'IncomeExpense'),

    ('CREATE', 'ScheduledAccount'),
    ('READ', 'ScheduledAccount'),
    ('DELETE', 'ScheduledAccount')
;
