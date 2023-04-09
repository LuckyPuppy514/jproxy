--liquibase formatted sql logicalFilePath:/database/changelog/v3.0.4.sql
--changeset LuckyPuppy514:1
--comment: 更新净标题正则表达式
UPDATE system_config SET value='(`|,|~|!|@|#|%|&|_|/|=|''|"|:|<|>|-|—|·|，|、|。|‘|’|“|”|？|！|：|（|）|【|】|《|》|♀)' WHERE id=16;
--rollback DELETE FROM system_config WHERE id=16;