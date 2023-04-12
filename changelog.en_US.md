
[中文](https://github.com/LuckyPuppy514/jproxy/blob/main/changelog.md) | [English](https://github.com/LuckyPuppy514/jproxy/blob/main/changelog.en_US.md)

# Change Logs

## v3.1.2 2023-04-12

1. Merge pull request [#27](https://github.com/LuckyPuppy514/jproxy/pull/27)

## v3.1.1 2023-04-12

1. Optimize qBittorrent rename

## v3.1.0 2023-04-11

1. Remove the proxy of qBittorrent (if you set the proxy in Sonarr according to the old version, please restore the settings)
2. Add qBittorrent rename function, support Sonarr and Radarr

## v3.0.9 2023-04-10

1. Switch cache database: Redis => Caffeine
2. Optimize title matching logic

## v3.0.8 2023-04-09

1. Update liquibase config

## v3.0.7 2023-04-09

1. Update package way

## v3.0.6 2023-04-08

1. Added the function to modify the TMDB title

## v3.0.5 2023-04-08

1. Fix the downloaded bug

## v3.0.4 2023-04-08

1. Fix BUG of append TMDB title to search

## v3.0.3 2023-04-07

1. Optimize title matching logic

## v3.0.2 2023-04-07

1. Optimize Radarr title matching logic

## v3.0.1 2023-04-07

1. Add a backup address for the rule to avoid the inability to synchronize the rules due to the inability to access github
2. Docker basic image changes to support ARM architecture

## v3.0.0 2023-04-06

🚨 Code refactoring, not compatible with v2 version

1. Separation of front and back ends, new WebUI
2. Logical reconstruction, the matching method is separated from the original single rule into multiple marking rules, which is more free and has a higher recognition rate
3. Automatically append the selected language title to optimize Sonarr search via TMDB
4. Add support for Radarr, automatically add the main language title to optimize the search, adapt to the new logic, format the results, and improve the recognition rate
5. The rule sharing is changed to Pull Request, and the rule download is changed to directly synchronize the rules under the `src/main/resources/rule` directory of this project

## v2.6.5 2023-01-17

1. Improve: torrent name format(reduce import error)

## v2.6.4 2022-09-21

1. Fixed: Market-Search, sort bug of download count
2. New Function: enable modify username while change password

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
