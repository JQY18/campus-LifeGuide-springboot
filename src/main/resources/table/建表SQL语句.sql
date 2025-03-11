create database if not exists campus_trade;
use campus_trade;


CREATE TABLE notice
(
    id           INT AUTO_INCREMENT PRIMARY KEY, -- 假设ID是自增主键
    title        VARCHAR(255) NOT NULL,          -- 标题，假设最大长度为255字符
    content      TEXT         NOT NULL,          -- 内容，使用TEXT类型存储可能较长的内容
    type         VARCHAR(50)  NOT NULL,          -- 类型，假设最大长度为50字符
    publisher INT          NOT NULL,          -- 发布人id，关联到发布人的用户表
    create_time DATETIME     NOT NULL           -- 发布时间，记录日期和时间
);



create table post_collect
(
    id      int primary key auto_increment,
    post_id int,
    user_id int
);



drop table if exists admin;

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


drop table if exists comment;

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

drop table if exists detail_comment;


create table detail_comment
(
    id          int auto_increment
        primary key,
    detail_id   varchar(255)             null,
    username    varchar(20)              null,
    content     text                     null,
    create_time datetime default (now()) null
);


drop table if exists images;

create table images
(
    id          int auto_increment
        primary key,
    post_id     int                      null,
    url         varchar(255)             null,
    create_time datetime default (now()) null
);

drop table if exists likes;


create table likes
(
    id          int auto_increment
        primary key,
    comment_id  int                      null,
    liker_id    int                      null,
    create_time datetime default (now()) null
);

drop table if exists location;


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

drop table if exists location_detail;


create table location_detail
(
    id          int auto_increment
        primary key,
    detail_id   varchar(255)             null,
    name        varchar(255)             not null,
    description text                     null,
    create_time datetime default (now()) null
);

drop table if exists location_images;


create table location_images
(
    id                 int auto_increment
        primary key,
    url                varchar(255)             null,
    create_time        datetime default (now()) null,
    location_detail_id int                      null comment 'location_detail_id'
);

drop table if exists location_videos;


create table location_videos
(
    id                 int auto_increment
        primary key,
    url                varchar(255)             null,
    create_time        datetime default (now()) null,
    location_detail_id int                      null
);

drop table if exists post;

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


drop table if exists post_likes;

create table post_likes
(
    id      int auto_increment
        primary key,
    post_id int null,
    user_id int null
);

drop table if exists reply;

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

drop table if exists statistics;

create table statistics
(
    date  date             not null comment '日期',
    count bigint default 0 null comment '当日访问量'
)
    comment '统计访问量';


drop table if exists user;

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

