-- 1
INSERT INTO public.users (id, login, password, email, chat_id)
VALUES (nextval('user_id_seq'), 'Alex', 'secure123', 'a.piatrovich@gmail.com', '495137766');

-- 2
INSERT INTO public.users (id, login, password, email, chat_id)
VALUES (nextval('user_id_seq'), 'Pavel', 'Pa$$w0rd', 'pavel.kuchkovskii@gmail.com', '6967735001');

-- 3
INSERT INTO public.users (id, login, password, email, chat_id)
VALUES (nextval('user_id_seq'), 'Dmitry', 'qwerty', 'test@gmail.com', '491799010');

-- 4
INSERT INTO public.users (id, login, password, email, chat_id)
VALUES (nextval('user_id_seq'), 'Egor', 'qwerty123', 'test@gmail.com', '1266739818');

