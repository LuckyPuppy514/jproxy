
[中文](https://github.com/LuckyPuppy514/jproxy/blob/main/release-logs.zh_CN.md) | [English](https://github.com/LuckyPuppy514/jproxy/blob/main/release-logs.md)

# 发布日志

## v2.6.3 2022-09-02
1. 修复：使用 RestTemplate 代替 WebClient 以解决偶尔请求异常的问题

## v2.6.2 2022-08-10
1. 修复：当系列类型为：Standard, Daily 时，查询关键字季集，日期等参数无法格式化的问题

## v2.6.1 2022-08-07
1. 修复：部分 BT/PT 站当使用 qBittorrent 代理时无法下载的问题

## v2.6.0 2022-08-05
1. 新功能：qBittorrent 代理
2. 新功能：新增搜索条件：备注
3. 修复：当 sonarr 无法识别种子标题时，导入错误季的问题

## v2.5.2 2022-08-01

1. 新功能：保存代理配置时，先进行连通性测试

## v2.5.1 2022-07-30

1. 更新 README.md
2. 变更：sqlite-jdbc 版本到：3.39.2-SNAPSHOT，用于修复在 aarch64 机器上 docker build 出错的问题
3. 修复：同步出错的问题
4. 修复：prowlarr 错误

## v2.5.0 2022-07-30

1. 简单界面：支持中文和英文
2. 代理配置：配置 Jackett / Prowlarr 的地址，端口等信息
3. 新增规则：包括查询规则和结果规则
4. 规则管理：查询，编辑，删除，分享，以及导入导出等
5. 规则市场：可以查询大家分享的规则，并下载
6. 用例测试：可以批量添加标题进行测试，查看格式化后的效果

