--liquibase formatted sql
--changeset LuckyPuppy514:1
--comment: 创建系统用户表
CREATE TABLE IF NOT EXISTS system_user
(
   -- 主键
   id INTEGER NOT NULL PRIMARY KEY,
   -- 用户名
   username TEXT NOT NULL,
   -- 密码
   password TEXT DEFAULT NULL,
   -- 角色
   role TEXT DEFAULT 'admin',
   -- 有效状态（有效：1，无效：0，默认：1）
   valid_status INTEGER DEFAULT 1 NOT NULL,
   -- 创建时间
   create_time DATETIME DEFAULT
   (
      DATETIME
      (
         CURRENT_TIMESTAMP,
         'localtime'
      )
   ),
   -- 更新时间
   update_time DATETIME DEFAULT
   (
      DATETIME
      (
         CURRENT_TIMESTAMP,
         'localtime'
      )
   )
);
--rollback DROP TABLE system_user;
--changeset LuckyPuppy514:2
--comment: 插入默认系统用户记录
INSERT INTO system_user
(
   id,
   username,
   password,
   role
)
VALUES
(
   1,
   'jproxy',
   '765EB35667C4323E7CCB88C94C223202',
   'admin'
);
--rollback DELETE FROM system_user;
--changeset LuckyPuppy514:3
--comment: 创建系统配置表
CREATE TABLE IF NOT EXISTS system_config
(
   -- 主键
   id INTEGER NOT NULL PRIMARY KEY,
   -- 键
   "key" TEXT NOT NULL,
   -- 值
   value TEXT DEFAULT NULL,
   -- 有效状态（有效：1，无效：0，默认：1）
   valid_status INTEGER DEFAULT 1 NOT NULL,
   -- 创建时间
   create_time DATETIME DEFAULT
   (
      DATETIME
      (
         CURRENT_TIMESTAMP,
         'localtime'
      )
   ),
   -- 更新时间
   update_time DATETIME DEFAULT
   (
      DATETIME
      (
         CURRENT_TIMESTAMP,
         'localtime'
      )
   )
);
--rollback DROP TABLE system_config;
--changeset LuckyPuppy514:4
--comment: 插入默认系统配置记录
INSERT INTO system_config
(
   id,
   "key",
   value,
   valid_status
)
VALUES
(
   1,
   'sonarrUrl',
   '',
   0
),

(
   2,
   'sonarrApikey',
   '',
   0
),

(
   3,
   'sonarrIndexerFormat',
   '{title} {season}{episode} {language}{subtitle}{resolution}{quality}{dynamic_range}{group}',
   1
),

(
   4,
   'sonarrDownloaderFormat',
   '{season}{episode} {language}{group}',
   1
),

(
   5,
   'sonarrLanguage1',
   'zh-CN',
   1
),

(
   6,
   'sonarrLanguage2',
   'zh-TW',
   1
),

(
   7,
   'radarrUrl',
   '',
   0
),

(
   8,
   'radarrApikey',
   '',
   0
),

(
   9,
   'radarrIndexerFormat',
   '{title} {year} {language}{subtitle}{resolution}{quality}{dynamic_range}{group}',
   1
),

(
   10,
   'jackettUrl',
   '',
   0
),

(
   11,
   'prowlarrUrl',
   '',
   0
),

(
   12,
   'qbittorrentUrl',
   '',
   0
),

(
   13,
   'transmissionUrl',
   '',
   0
),

(
   14,
   'tmdbUrl',
   'https://api.themoviedb.org',
   1
),

(
   15,
   'tmdbApikey',
   '',
   0
),

(
   16,
   'cleanTitleRegex',
   '[@"!?`_:\s\[\]\-\.''，、。！？（）—♀]',
   1
),

(
   17,
   'ruleSyncAuthors',
   'ALL',
   1
);
--rollback DELETE FROM system_config;
--changeset LuckyPuppy514:5
--comment: 创建剧集标题表
CREATE TABLE IF NOT EXISTS sonarr_title
(
   -- 主键
   id INTEGER NOT NULL PRIMARY KEY,
   -- TVDB 编号
   tvdb_id INTEGER NOT NULL,
   -- 序号
   sno INTEGER DEFAULT 0 NOT NULL,
   -- 主标题
   main_title TEXT NOT NULL,
   -- 标题
   title TEXT NOT NULL,
   -- 净标题
   clean_title TEXT NOT NULL,
   -- 季数
   season_number INTEGER DEFAULT 1 NOT NULL,
   -- 监控状态（监控中：1，未监控：0，默认：1）
   monitored INTEGER DEFAULT 1 NOT NULL,
   -- 有效状态（有效：1，无效：0，默认：1）
   valid_status INTEGER DEFAULT 1 NOT NULL,
   -- 创建时间
   create_time DATETIME DEFAULT
   (
      DATETIME
      (
         CURRENT_TIMESTAMP,
         'localtime'
      )
   ),
   -- 更新时间
   update_time DATETIME DEFAULT
   (
      DATETIME
      (
         CURRENT_TIMESTAMP,
         'localtime'
      )
   )
);
CREATE INDEX sonarr_title_tvdb_id_idx ON sonarr_title (tvdb_id);
CREATE INDEX sonarr_title_clean_title_idx ON sonarr_title (clean_title);
--rollback DROP TABLE sonarr_title;
--changeset LuckyPuppy514:6
--comment: 创建 TMDB 标题表
CREATE TABLE IF NOT EXISTS tmdb_title
(
   -- 主键
   id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
   -- TVDB 编号
   tvdb_id INTEGER NOT NULL,
   -- TMDB 编号
   tmdb_id INTEGER DEFAULT NULL,
   -- 语言代码
   language VARCHAR (8) NOT NULL,
   -- 标题
   title TEXT NOT NULL,
   -- 有效状态（有效：1，无效：0，默认：1）
   valid_status INTEGER DEFAULT 1 NOT NULL,
   -- 创建时间
   create_time DATETIME DEFAULT
   (
      DATETIME
      (
         CURRENT_TIMESTAMP,
         'localtime'
      )
   ),
   -- 更新时间
   update_time DATETIME DEFAULT
   (
      DATETIME
      (
         CURRENT_TIMESTAMP,
         'localtime'
      )
   )
);
CREATE INDEX tmdb_title_tvdb_id_idx ON tmdb_title (tvdb_id);
CREATE INDEX tmdb_title_tmdb_id_idx ON tmdb_title (tmdb_id);
--rollback DROP TABLE tmdb_title;
--changeset LuckyPuppy514:7
--comment: 创建剧集规则表
CREATE TABLE IF NOT EXISTS sonarr_rule
(
   -- 主键
   id TEXT NOT NULL PRIMARY KEY,
   -- 标记
   token TEXT NOT NULL,
   -- 优先级（同一标记越小优先级越高）
   priority INTEGER DEFAULT 1000 NOT NULL,
   -- 匹配正则
   regex TEXT NOT NULL,
   -- 替换内容
   replacement TEXT DEFAULT '' NOT NULL,
   -- 偏移量
   offset INTEGER DEFAULT 0 NOT NULL,
   -- 范例
   example TEXT DEFAULT '' NOT NULL,
   -- 备注
   remark TEXT DEFAULT NULL,
   -- 作者
   author TEXT DEFAULT NULL,
   -- 有效状态（有效：1，无效：0，默认：1）
   valid_status INTEGER DEFAULT 1 NOT NULL,
   -- 创建时间
   create_time DATETIME DEFAULT
   (
      DATETIME
      (
         CURRENT_TIMESTAMP,
         'localtime'
      )
   ),
   -- 更新时间
   update_time DATETIME DEFAULT
   (
      DATETIME
      (
         CURRENT_TIMESTAMP,
         'localtime'
      )
   )
);
--rollback DROP TABLE sonarr_rule;
--changeset LuckyPuppy514:8
--comment: 创建剧集范例表
CREATE TABLE IF NOT EXISTS sonarr_example
(
   -- 哈希值
   hash TEXT NOT NULL PRIMARY KEY,
   -- 原始内容
   original_text TEXT NOT NULL,
   -- 格式化内容
   format_text TEXT DEFAULT NULL,
   -- 有效状态（有效：1，无效：0，默认：1）
   valid_status INTEGER DEFAULT 1 NOT NULL,
   -- 创建时间
   create_time DATETIME DEFAULT
   (
      DATETIME
      (
         CURRENT_TIMESTAMP,
         'localtime'
      )
   ),
   -- 更新时间
   update_time DATETIME DEFAULT
   (
      DATETIME
      (
         CURRENT_TIMESTAMP,
         'localtime'
      )
   )
);
--rollback DROP TABLE sonarr_example;
--changeset LuckyPuppy514:9
--comment: 创建电影 标题表
CREATE TABLE IF NOT EXISTS radarr_title
(
   -- 主键
   id INTEGER NOT NULL PRIMARY KEY,
   -- TMDB 编号
   tmdb_id INTEGER NOT NULL,
   -- 序号
   sno INTEGER DEFAULT 0 NOT NULL,
   -- 主标题
   main_title TEXT NOT NULL,
   -- 标题
   title TEXT NOT NULL,
   -- 净标题
   clean_title TEXT NOT NULL,
   -- 年份
   year INTEGER NOT NULL,
   -- 监控状态（监控中：1，未监控：0，默认：1）
   monitored INTEGER DEFAULT 1 NOT NULL,
   -- 有效状态（有效：1，无效：0，默认：1）
   valid_status INTEGER DEFAULT 1 NOT NULL,
   -- 创建时间
   create_time DATETIME DEFAULT
   (
      DATETIME
      (
         CURRENT_TIMESTAMP,
         'localtime'
      )
   ),
   -- 更新时间
   update_time DATETIME DEFAULT
   (
      DATETIME
      (
         CURRENT_TIMESTAMP,
         'localtime'
      )
   )
);
--rollback DROP TABLE radarr;
--changeset LuckyPuppy514:10
--comment: 创建电影规则表
CREATE TABLE IF NOT EXISTS radarr_rule
(
   -- 主键
   id TEXT NOT NULL PRIMARY KEY,
   -- 标记
   token TEXT NOT NULL,
   -- 优先级（同一标记越小优先级越高）
   priority INTEGER DEFAULT 1000 NOT NULL,
   -- 匹配正则
   regex TEXT NOT NULL,
   -- 替换内容
   replacement TEXT DEFAULT '' NOT NULL,
   -- 偏移量
   offset INTEGER DEFAULT 0 NOT NULL,
   -- 范例
   example TEXT DEFAULT '' NOT NULL,
   -- 备注
   remark TEXT DEFAULT NULL,
   -- 作者
   author TEXT DEFAULT NULL,
   -- 有效状态（有效：1，无效：0，默认：1）
   valid_status INTEGER DEFAULT 1 NOT NULL,
   -- 创建时间
   create_time DATETIME DEFAULT
   (
      DATETIME
      (
         CURRENT_TIMESTAMP,
         'localtime'
      )
   ),
   -- 更新时间
   update_time DATETIME DEFAULT
   (
      DATETIME
      (
         CURRENT_TIMESTAMP,
         'localtime'
      )
   )
);
--rollback DROP TABLE radarr_rule;
--changeset LuckyPuppy514:11
--comment: 创建电影范例表
CREATE TABLE IF NOT EXISTS radarr_example
(
   -- 哈希值
   hash TEXT NOT NULL PRIMARY KEY,
   -- 原始内容
   original_text TEXT NOT NULL,
   -- 格式化内容
   format_text TEXT DEFAULT NULL,
   -- 有效状态（有效：1，无效：0，默认：1）
   valid_status INTEGER DEFAULT 1 NOT NULL,
   -- 创建时间
   create_time DATETIME DEFAULT
   (
      DATETIME
      (
         CURRENT_TIMESTAMP,
         'localtime'
      )
   ),
   -- 更新时间
   update_time DATETIME DEFAULT
   (
      DATETIME
      (
         CURRENT_TIMESTAMP,
         'localtime'
      )
   )
);
--rollback DROP TABLE radarr_example;