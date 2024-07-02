create table reminder
(
    id          bigint        not null
        primary key,
    remind      timestamp(6)  not null,
    user_id     bigint        not null,
    description varchar(4096) not null,
    title       varchar(255)  not null
);

create sequence reminder_id_seq
    start with 1
    increment by 10
    minvalue 1;
