-- 1
INSERT INTO public.users (id, login, password, email, chat_id)
VALUES (nextval('user_id_seq'), 'user123', 'secure123', 'a.piatrovich@gmail.com', '495137766');

-- 2
INSERT INTO public.users (id, login, password, email, chat_id)
VALUES (nextval('user_id_seq'), 'pashtet', 'Pa$$w0rd', 'a.piatrovich@gmail.com', '6967735001');

-- 3
INSERT INTO public.users (id, login, password, email, chat_id)
VALUES (nextval('user_id_seq'), 'dimas', 'qwerty', 'a.piatrovich@gmail.com', '491799010');

