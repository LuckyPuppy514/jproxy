
[中文](https://github.com/LuckyPuppy514/jproxy/blob/main/changelog.md) | English

# Change Logs

## v3.4.2 2024-02-19

👻 Fix bug of rename

## v3.4.1 2023-10-13

🚀 Append other info while rename file

## v3.4.0 2023-08-09

👻 Fix the problem that appending the main language title actually appends the alternate language title

## v3.3.9 2023-08-08

🆕 Caching indexer query result

## v3.3.8 2023-08-06

👻 Fix bug of pagination

## v3.3.7 2023-08-06

🚀 Optimize search logic

## v3.3.6 2023-08-06

🚀 Optimize matching logic

## v3.3.5 2023-08-06

🚀 Optimize language matching logic

## v3.3.4 2023-07-27

🆕 New parameter `min-count`：append primary title (without season and episode number) to search while current result count less than this value

## v3.3.3 2023-07-17

🚀 Optimize title matching logic

## v3.3.2 2023-07-14

🚀 Optimize search logic

## v3.3.1 2023-07-08

🚀 Optimize import logic

## v3.3.0 2023-06-27

🚀 Optimize file rename logic

## v3.2.9 2023-06-22

🚀 Optimize file rename logic

## v3.2.8 2023-06-13

🚀 Optimize clean title logic

## v3.2.7 2023-06-04

👻 Fix bug of clean title
👻 Fix bug about TMDB sync

## v3.2.6 2023-06-03

🚀 Optimize title matching logic

## v3.2.5 2023-06-03

🚀 Optimize title matching logic

## v3.2.4 2023-05-29

👏 Merge pull request [#41](https://github.com/LuckyPuppy514/jproxy/pull/41) from DDS-Derek/main

## v3.2.3 2023-05-28

🚀 Optimize downloader file rename logic

## v3.2.2 2023-05-23

🚀 Optimize search logic

## v3.2.1 2023-04-30

🆕 Add title at TMDB Menu
🚀 Optimize search logic

## v3.2.0 2023-04-30

🚀 Optimize clean title logic

## v3.1.9 2023-04-29

👻 Fix downloader rename bug

## v3.1.8 2023-04-17

🆕 Added version display and upgrade prompt

## v3.1.7 2023-04-16

🆕 Add file rename switch

## v3.1.6 2023-04-14

👻 Fix downloader rename bug

## v3.1.4 2023-04-14

🚀 Optimize downloader rename

## v3.1.3 2023-04-14

🚀 Optimize qBittorrent rename
🆕 Added Transmission rename
🚀 Optimize search of series without absolute number

## v3.1.2 2023-04-12

👏 Merge pull request [#27](https://github.com/LuckyPuppy514/jproxy/pull/27)

## v3.1.1 2023-04-12

🚀 Optimize qBittorrent rename

## v3.1.0 2023-04-11

🚮 Remove the proxy of qBittorrent (if you set the proxy in Sonarr according to the old version, please restore the settings)
🆕 Add qBittorrent rename function, support Sonarr and Radarr

## v3.0.9 2023-04-10

🆕 Switch cache database: Redis => Caffeine
🚀 Optimize title matching logic

## v3.0.8 2023-04-09

🆕 Add liquibase config

## v3.0.7 2023-04-09

🆕 Add package way

## v3.0.6 2023-04-08

🆕 Added the function to modify the TMDB title

## v3.0.5 2023-04-08

👻 Fix the downloaded bug

## v3.0.4 2023-04-08

👻 Fix BUG of append TMDB title to search

## v3.0.3 2023-04-07

🚀 Optimize title matching logic

## v3.0.2 2023-04-07

🚀 Optimize Radarr title matching logic

## v3.0.1 2023-04-07

🆕 Add a backup address for the rule to avoid the inability to synchronize the rules due to the inability to access github
🚀 Docker basic image changes to support ARM architecture

## v3.0.0 2023-04-06

🚨 Code refactoring, not compatible with v2 version

🆕 Separation of front and back ends, new WebUI
🆕 Logical reconstruction, the matching method is separated from the original single rule into multiple marking rules, which is more free and has a higher recognition rate
🆕 Automatically append the selected language title to optimize Sonarr search via TMDB
🆕 Add support for Radarr, automatically add the main language title to optimize the search, adapt to the new logic, format the results, and improve the recognition rate
🆕 The rule sharing is changed to Pull Request, and the rule download is changed to directly synchronize the rules under the `src/main/resources/rule` directory of this project

## v2.6.5 2023-01-17

🚀 Improve: torrent name format(reduce import error)

## v2.6.4 2022-09-21

👻 Fixed: Market-Search, sort bug of download count
🆕 New Function: enable modify username while change password

## v2.6.3 2022-09-02

👻 Fixed: replace WebClient with RestTemplate to solve request error sometime

## v2.6.2 2022-08-10

👻 Fixed: can not format season and ep or date in search key while series type is Standard or Daily

## v2.6.1 2022-08-07

👻 Fixed: part of BT/PT indexers can not download while use qBittorrent proxy

## v2.6.0 2022-08-05

🆕 New Function: qBittorrent Proxy
🆕 New Function: add search condition: remark
👻 Fixed: import wrong season while sonarr unrecognize the title of torrent

## v2.5.2 2022-08-01

🆕 New Function: Reachalbe test first when save proxy config

## v2.5.1 2022-07-31

🆕 Add README.md
👻 Fixed: docker build error at aarch64 by changing sqlite-jdbc version to: 3.39.2-SNAPSHOT
👻 Fixed: sync error
👻 Fixed: prowlarr error

## v2.5.0 2022-07-30

🆕 Web UI: Chinese or English
🆕 Proxy Config: Jackett / Prowlarr ip, port setting
🆕 Add Rule: Include search and result rule
🆕 Rule Manage: Search, edit, delete, share and import or export
🆕 Rule Market: Search rules shared by others and download
🆕 Rule Test: Add title list and check the result after format
