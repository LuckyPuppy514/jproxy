-- 后台用户表
CREATE TABLE IF NOT EXISTS ADMIN_USER(
        -- 后台用户 ID（主键）
        USER_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
        -- 用户名
        USERNAME VARCHAR(64) NOT NULL,
        -- 密码
        PASSWORD VARCHAR(128) NOT NULL,
        -- 有效状态（有效：1，无效：0，默认：1）
        VALIDSTATUS INTEGER(1) DEFAULT 1 NOT NULL,
        -- 创建时间
        CREATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP,
        -- 更新时间
        UPDATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP
);
-- 初始化后台用户
INSERT OR IGNORE INTO "ADMIN_USER" ("USER_ID", "USERNAME", "PASSWORD", "VALIDSTATUS") VALUES (1, 'jproxy', 'c6f4ebbbb01a5c15ebee874504271637', 1);


-- 代理配置表：PROXY_CONFIG
CREATE TABLE IF NOT EXISTS PROXY_CONFIG(
        -- 代理配置 ID（主键）
        PROXY_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
        -- 代理类型（Jackett/Prowlarr）
        PROXY_TYPE VARCHAR(16) NOT NULL,
        -- 代理地址
        PROXY_IP VARCHAR(32) NOT NULL,
        -- 代理端口
        PROXY_PORT VARCHAR(8) NOT NULL,
        -- 代理路径
        PROXY_PATH VARCHAR(64) NOT NULL,
        -- 有效状态（有效：1，无效：0，默认：1）
        VALIDSTATUS INTEGER(1) DEFAULT 1 NOT NULL,
        -- 创建时间
        CREATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP,
        -- 更新时间
        UPDATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP
);
-- 初始化代理配置
INSERT OR IGNORE INTO "PROXY_CONFIG" ("PROXY_ID", "PROXY_TYPE", "PROXY_IP", "PROXY_PORT", "PROXY_PATH", "VALIDSTATUS") VALUES (1, 'Jackett', '127.0.0.1', '9117', '/api/.*', 1);
INSERT OR IGNORE INTO "PROXY_CONFIG" ("PROXY_ID", "PROXY_TYPE", "PROXY_IP", "PROXY_PORT", "PROXY_PATH", "VALIDSTATUS") VALUES (2, 'Prowlarr', '127.0.0.1', '9696', '/.*/api.*', 1);


-- 规则配置表：RULE_CONFIG
CREATE TABLE IF NOT EXISTS RULE_CONFIG(
        -- 规则配置 ID（主键）
        RULE_ID VARCHAR(32) PRIMARY KEY NOT NULL,
        -- 规则名称
        RULE_NAME VARCHAR(256) NOT NULL,
        -- 规则类型
        RULE_TYPE VARCHAR(16) DEFAULT 'Release_Group' NOT NULL,
        -- 规则语言
        RULE_LANGUAGE VARCHAR(64) NOT NULL,
        -- 正则类型（查询：Search，结果：Result）
        REGULAR_TYPE VARCHAR(16) NOT NULL,
        -- 匹配正则
        REGULAR_MATCH VARCHAR(512) NOT NULL,
        -- 替换正则
        REGULAR_REPLACE VARCHAR(512) NOT NULL,
        -- 执行规则（总是执行：Always，执行一次：Once，默认：Once）
        EXECUTE_RULE VARCHAR(16) DEFAULT 'Once' NOT NULL,
        -- 执行优先级（数字越小优先级越高，默认：1000）
        EXECUTE_PRIORITY INTEGER DEFAULT 1000 NOT NULL,
        -- 命中次数
        MATCH_COUNT INTEGER DEFAULT 0,
        -- 规则标签
        RULE_TAG VARCHAR(128),
        -- 备注
        REMARK VARCHAR(1024),
        -- 有效状态（有效：1，无效：0，默认：1）
        VALIDSTATUS INTEGER(1) DEFAULT 1 NOT NULL,
        -- 分享 KEY
        SHARE_KEY VARCHAR(64) DEFAULT NULL,
        -- 下载次数
        DOWNLOAD_COUNT INTEGER DEFAULT 0,
        -- 创建时间
        CREATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP,
        -- 更新时间
        UPDATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 规则测试用例表：RULE_TEST_EXAMPLE
CREATE TABLE IF NOT EXISTS RULE_TEST_EXAMPLE(
        -- 规则测试用例 ID（主键）
        EXAMPLE_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
        -- 用例内容
        EXAMPLE_CONTENT VARCHAR(1024) NOT NULL,
        -- 规则配置 ID（关联 RULE_CONFIG.RULE_ID）
        RULE_ID VARCHAR(32) NOT NULL,
        -- 有效状态（有效：1，无效：0，默认：1）
        VALIDSTATUS INTEGER(1) DEFAULT 1 NOT NULL,
        -- 创建时间
        CREATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP,
        -- 更新时间
        UPDATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP
);
