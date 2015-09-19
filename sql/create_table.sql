create table user_info (
    user_id                 bigserial primary key
    , username              varchar(32) not null
    , password              varchar(256) not null
    , mail_address          varchar(256) not null
    , enabled               smallint not null default 1
    , cnt_badcredentials    smallint not null default 0
    , expired_account       timestamp not null default now() + interval '90 day'
    , expired_password      timestamp not null default now() + interval '30 day'
);
create index user_info_idx_01 on user_info(mail_address);

create table user_role (
    role_id                 bigserial primary key
    , user_id               bigint not null references user_info(user_id) on delete cascade
    , role                  varchar(32) not null
);

create table lending_app (
    lending_app_id          bigserial primary key
    , status                varchar(1) not null
    , lending_user_id       bigint not null references user_info(user_id)
    , approval_user_id      bigint references user_info(user_id)
);

create table lending_book (
    lending_book_id         bigserial primary key
    , lending_app_id        bigint not null references lending_app(lending_app_id) on delete cascade
    , isbn                  varchar(17) not null
    , book_name             varchar(128) not null
    , lending_state         varchar(16)
    , lending_app_flg       varchar(1)
    , lending_app_reason    varchar(128)
    , approval_result       varchar(1)
    , approval_reason       varchar(128)
);

create table library_forsearch (
      systemid              text primary key
    , formal                text
);
