-- 1
INSERT INTO public.users (id, name, password, email, telegram_user_id, o_auth_provider)
VALUES (nextval('user_id_seq'), 'Alex', 'secure123', 'a.piatrovich@gmail.com', '0', 'github');

-- 2
INSERT INTO public.users (id, name, password, email, telegram_user_id, o_auth_provider)
VALUES (nextval('user_id_seq'), 'Pavel', 'Pa$$w0rd', 'dmitry2020@gmail.com', '6967735001', 'github');

-- 3
INSERT INTO public.users (id, name, password, email, telegram_user_id, o_auth_provider)
VALUES (nextval('user_id_seq'), 'Dmitry', 'qwerty', 'dmitry2020@gmail.com', '0', 'github');

-- 4
INSERT INTO public.users (id, name, password, email, telegram_user_id, o_auth_provider)
VALUES (nextval('user_id_seq'), 'Egor', 'qwerty123', 'egorKK@gmail.com', '1266739818', 'github');

-- 5
INSERT INTO public.users (id, name, password, email, telegram_user_id, o_auth_provider)
VALUES (nextval('user_id_seq'), 'Alfred', 'myPass123', 'hisoka-gg@mail.ru', '0', 'github');

