CREATE TABLE channel(
                     youtube_id VARCHAR PRIMARY KEY,
                     username VARCHAR,
                     description VARCHAR,
                     thumbnail VARCHAR,
                     uploads_id VARCHAR,
                     given_reference VARCHAR
);

CREATE TABLE playlist(
                         youtube_id VARCHAR PRIMARY KEY,
                         owner_id VARCHAR REFERENCES channel(youtube_id)
);

CREATE TABLE video(
                      youtube_id VARCHAR PRIMARY KEY,
                      title VARCHAR NOT NULL,
                      description VARCHAR,
                      owner_id VARCHAR REFERENCES channel(youtube_id),
                      thumbnail VARCHAR,
                      view_count BIGINT DEFAULT 0,
                      like_count BIGINT DEFAULT 0,
                      comment_count BIGINT DEFAULT 0
);

CREATE TABLE playlist_videos(
                                playlist_id VARCHAR REFERENCES playlist(youtube_id),
                                video_id VARCHAR REFERENCES video(youtube_id)
);


CREATE TABLE statistics(
                           id BIGSERIAL PRIMARY KEY,
                           owner_id VARCHAR REFERENCES channel(youtube_id),
                           view_count BIGINT,
                           subscriber_count BIGINT,
                           video_count BIGINT
);

CREATE TABLE company(
                        id BIGSERIAL PRIMARY KEY,
                        title VARCHAR NOT NULL,
                        logo_url VARCHAR
);

CREATE TABLE videos_companies(
    video_id VARCHAR references video(youtube_id),
    company_id BIGINT references company(id),
    link_id BIGINT references link(id)
);

CREATE TABLE link(
                     id BIGSERIAL PRIMARY KEY,
                     video_id VARCHAR REFERENCES video(youtube_id),
                     owner_id VARCHAR REFERENCES channel(youtube_id),
                     raw_value VARCHAR NOT NULL,
                     value VARCHAR,
                     is_blacklisted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE blacklisted_link(
    id BIGSERIAL PRIMARY KEY,
    value VARCHAR,
    channel_id VARCHAR REFERENCES channel(youtube_id)
);
