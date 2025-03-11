create database if not exists campus_trade;
use campus_trade;

create table admin
(
    id           int auto_increment
        primary key,
    username     varchar(20)                        null,
    password     varchar(20)                        null,
    location_id  int                                null comment '该管理员能够管理的地点，0表示为超级管理员',
    created_time datetime default CURRENT_TIMESTAMP null,
    constraint admin_pk
        unique (username)
);

create table comment
(
    id           int auto_increment
        primary key,
    post_id      int                      null,
    commenter_id int                      null,
    content      text                     not null,
    `like`       int      default 0       not null,
    create_time  datetime default (now()) null,
    update_time  datetime default (now()) null
);

create table detail_comment
(
    id          int auto_increment
        primary key,
    detail_id   varchar(255)             null,
    username    varchar(20)              null,
    content     text                     null,
    create_time datetime default (now()) null
);

create table images
(
    id          int auto_increment
        primary key,
    post_id     int                      null,
    url         varchar(255)             null,
    create_time datetime default (now()) null
);

create table likes
(
    id          int auto_increment
        primary key,
    comment_id  int                      null,
    liker_id    int                      null,
    create_time datetime default (now()) null
);

create table location
(
    id           bigint auto_increment
        primary key,
    name         varchar(255)                       not null,
    description  text                               null,
    image        varchar(255)                       null,
    detail_id    varchar(255)                       null,
    category     varchar(255)                       null,
    x            double                             null,
    y            double                             null,
    created_time datetime default CURRENT_TIMESTAMP null
);

create table location_detail
(
    id          int auto_increment
        primary key,
    detail_id   varchar(255)             null,
    name        varchar(255)             not null,
    description text                     null,
    create_time datetime default (now()) null
);

create table location_images
(
    id                 int auto_increment
        primary key,
    url                varchar(255)             null,
    create_time        datetime default (now()) null,
    location_detail_id int                      null comment 'location_detail_id'
);

create table location_videos
(
    id                 int auto_increment
        primary key,
    url                varchar(255)             null,
    create_time        datetime default (now()) null,
    location_detail_id int                      null
);

create table notice
(
    id          int auto_increment
        primary key,
    title       varchar(255)             not null,
    content     text                     not null,
    type        varchar(50)              not null,
    publisher   int                      not null,
    create_time datetime default (now()) not null
);

create table post
(
    id          int auto_increment
        primary key,
    user_id     int                      null,
    category    int      default 0       null,
    title       varchar(35)              null,
    content     text                     null,
    create_time datetime default (now()) null,
    update_time datetime default (now()) null
);

create table post_collect
(
    id      int auto_increment
        primary key,
    post_id int null,
    user_id int null
);

create index post_collect_user_id_post_id_index
    on post_collect (user_id, post_id);

create table post_likes
(
    id      int auto_increment
        primary key,
    post_id int null,
    user_id int null
);

create table reply
(
    id           int auto_increment
        primary key,
    father_id    int                      null,
    commenter_id int                      null,
    replier_id   int                      null,
    content      varchar(255)             not null,
    create_time  datetime default (now()) null
);

create table statistics
(
    date  date             not null comment '日期',
    count bigint default 0 null comment '当日访问量'
)
    comment '统计访问量';

create table user
(
    id           int auto_increment
        primary key,
    username     varchar(20)              not null,
    password     varchar(20)              null,
    nickname     varchar(20)              null,
    avatar       varchar(255)             null,
    age          int                      null,
    gender       char charset utf8mb3     null,
    school       varchar(20)              null,
    phone_number varchar(11)              null,
    email        varchar(20)              null,
    create_time  datetime default (now()) null,
    update_time  datetime default (now()) null,
    constraint users_pk
        unique (username)
);

