--liquibase formatted sql logicalFilePath:/database/changelog/v3.0.2.sql
--changeset LuckyPuppy514:1
--comment: 更新净标题正则表达式
UPDATE system_config SET value='(\||@|"|!|\?|`|_|:|\s|\[|\]|\-|\.|''|，|、|。|！|？|（|）|—|♀|20\d{2})' WHERE id=16;
--rollback DELETE FROM system_config WHERE id=16;