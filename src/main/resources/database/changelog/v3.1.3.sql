--liquibase formatted sql logicalFilePath:/database/changelog/v3.1.3.sql
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
   21,
   'transmissionUsername',
   '',
   0
),

(
   22,
   'transmissionPassword',
   '',
   0
);
--rollback DELETE FROM system_config WHERE id IN (21, 22);