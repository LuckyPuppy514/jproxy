--liquibase formatted sql logicalFilePath:/database/changelog/v3.1.0.sql
--changeset LuckyPuppy514:1
--comment: 插入新的配置
INSERT INTO system_config
(
   id,
   "key",
   value,
   valid_status
)
VALUES
(
   18,
   'qbittorrentUsername',
   '',
   0
),

(
   19,
   'qbittorrentPassword',
   '',
   0
),

(
   20,
   'radarrDownloaderFormat',
   '{title} {year} {language}{resolution}{group}',
   1
);
--rollback DELETE FROM system_config WHERE id IN (18, 19, 20);
--changeset LuckyPuppy514:2
--comment: 更新净标题正则表达式
UPDATE system_config SET value= '{title} {season}{episode} {language}{resolution}{group}' WHERE id=4;
--rollback UPDATE system_config SET value= '{season}{episode} {language}{group}' WHERE id=4;
--changeset LuckyPuppy514:3
--comment: 剧集标题表新增 series_id 列
ALTER TABLE sonarr_title ADD COLUMN series_id INTEGER;
--rollback ALTER TABLE sonarr_title DROP COLUMN series_id;
--changeset LuckyPuppy514:4
--comment: 电影标题表新增 movie_id 列
ALTER TABLE radarr_title ADD COLUMN movie_id INTEGER;
--rollback ALTER TABLE radarr_title DROP COLUMN movie_id;