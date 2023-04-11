--liquibase formatted sql logicalFilePath:/database/changelog/v3.1.1.sql
--changeset LuckyPuppy514:1
--comment: 删除剧集下载器格式配置
DELETE FROM system_config WHERE id=4;
--rollback INSERT INTO system_config (id, "key", value, valid_status) VALUES (4, 'sonarrDownloaderFormat', '{title} {season}{episode} {language}{resolution}{group}', 1 );
--changeset LuckyPuppy514:2
--comment: 删除电影下载器格式配置
DELETE FROM system_config WHERE id=20;
--rollback INSERT INTO system_config (id, "key", value, valid_status) VALUES (20, 'radarrDownloaderFormat', '{title} {year} {language}{resolution}{group}', 1 );
