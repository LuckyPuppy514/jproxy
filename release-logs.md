
[中文](https://github.com/LuckyPuppy514/jproxy/blob/main/release-logs.zh_CN.md) | [English](https://github.com/LuckyPuppy514/jproxy/blob/main/release-logs.md)

# Release Logs

## v2.6.3 2022-09-02
1. Fixed: replace WebClient with RestTemplate to solve request error sometime

## v2.6.2 2022-08-10
1. Fixed: can not format season and ep or date in search key while series type is Standard or Daily

## v2.6.1 2022-08-07
1. Fixed: part of BT/PT indexers can not download while use qBittorrent proxy

## v2.6.0 2022-08-05
1. New Function: qBittorrent Proxy
2. New Function: add search condition: remark
3. Fixed: import wrong season while sonarr unrecognize the title of torrent

## v2.5.2 2022-08-01

1. New Function: Reachalbe test first when save proxy config

## v2.5.1 2022-07-31

1. Update README.md
2. Fixed: docker build error at aarch64 by changing sqlite-jdbc version to: 3.39.2-SNAPSHOT
3. Fixed: sync error
4. Fixed: prowlarr error

## v2.5.0 2022-07-30

1. Web UI: Chinese or English
2. Proxy Config: Jackett / Prowlarr ip, port setting
3. Add Rule: Include search and result rule
4. Rule Manage: Search, edit, delete, share and import or export
5. Rule Market: Search rules shared by others and download
6. Rule Test: Add title list and check the result after format

