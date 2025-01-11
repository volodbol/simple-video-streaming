CREATE TABLE IF NOT EXISTS "videos"
(
    id          uuid PRIMARY KEY,
    created_by  varchar(255),
    created_at  bigint,
    updated_at  bigint,
    bucket_link varchar(512),
    title       varchar(255),
    synopsis    varchar(255),
    director    varchar(255),
    main_actor  varchar(255),
    actor_cast  text[],
    genre       varchar(255),
    year        integer,
    duration    integer,
    hidden      bool
);

CREATE TABLE IF NOT EXISTS "video_engagements"
(
    id          uuid PRIMARY KEY,
    video_id    uuid,
    impressions integer,
    views       integer
);