--liquibase formatted sql

--changeset LuckyPuppy514:1
--comment: insert qBittorrent proxy config
INSERT OR IGNORE INTO PROXY_CONFIG (PROXY_ID, PROXY_TYPE, PROXY_IP, PROXY_PORT, PROXY_PATH, VALIDSTATUS) VALUES (3, 'qBittorrent', '127.0.0.1', '8080', '/qbittorrent/**', 1);
--rollback DELETE FROM PROXY_CONFIG WHERE PROXY_ID = 3;

--changeset LuckyPuppy514:2
--comment: update proxy path of Jackett
UPDATE PROXY_CONFIG SET PROXY_PATH = '/jackett/**' WHERE PROXY_ID = 1;
--rollback UPDATE PROXY_CONFIG SET PROXY_PATH = '/api/.*' WHERE PROXY_ID = 1;

--changeset LuckyPuppy514:3
--comment: update proxy path of Prowlarr
UPDATE PROXY_CONFIG SET PROXY_PATH = '/prowlarr/**' WHERE PROXY_ID = 2;
--rollback UPDATE PROXY_CONFIG SET PROXY_PATH = '/.*/api.*' WHERE PROXY_ID = 1;