[中文](https://github.com/LuckyPuppy514/jproxy/blob/main/wiki.md) | [English](https://github.com/LuckyPuppy514/jproxy/blob/main/wiki.en_US.md)

- [Advanced Configuration](#advanced-configuration)
  - [Sonarr](#sonarr)
    - [qBittorrent](#qbittorrent)
    - [TMDB](#tmdb)
  - [Radarr](#radarr)
  - [Clean Title Regex](#clean-title-regex)
- [Usage](#usage)
  - [Cache](#cache)
  - [Add Rule](#add-rule)
  - [Sync Rule](#sync-rule)
  - [Share Rule](#share-rule)
  - [Series Example](#series-example)
  - [Movie Example](#movie-example)

# Advanced Configuration

## Sonarr

### qBittorrent

> 🌟 Proxy qBittorrent is able to format the titles that qBittorrent transmits to Sonarr when the download is complete, reducing the chance of importing the wrong season

① Fill in the `Server Url` of qBittorrent, after saving, it should be as shown in the picture ✅ , otherwise please check the input and network connectivity

![20230406182922](https://github.com/LuckyPuppy514/image/raw/main/2023/2023-04-06/20230406182922.webp)

② Modify the `Host` and `Port` of the downloader in Sonarr to the `IP` and `Port` of JProxy, and append `/sonarr/qbittorrent` to `Url Base`

`Empty` ➡️ `/sonarr/qbittorrent`

`/qbittorrent` ➡️ `/sonarr/qbittorrent/qbittorrent`

![20230405203518](https://github.com/LuckyPuppy514/image/raw/main/2023/2023-04-05/20230405203518.webp)

🚨 If you have `Remote Path Mappings`，do not forget modify the `Host` to the `IP` of JProxy

![20230405203612](https://github.com/LuckyPuppy514/image/raw/main/2023/2023-04-05/20230405203612.webp)

### TMDB

> 🌟 Synchronize the TMDB title, which can be used to add the title of the primary language and the secondary language when searching, and can also be used to match the title of the result to improve the recognition rate

① If you do not have a TMDB account, please [register an account](https://www.themoviedb.org/signup) first, then [obtain an API key](https://www.themoviedb.org/settings/api), after saving, it should normally be as shown in the picture ✅, otherwise please check the input and network connectivity

![20230405203809](https://github.com/LuckyPuppy514/image/raw/main/2023/2023-04-05/20230405203809.webp)
![20230406184917](https://github.com/LuckyPuppy514/image/raw/main/2023/2023-04-06/20230406184917.webp)

② For the first use, it is recommended to manually synchronize the data once (it will be automatically synchronized later)

![20230406184943](https://github.com/LuckyPuppy514/image/raw/main/2023/2023-04-06/20230406184943.webp)

## Radarr

① Fill in the Radarr `Server Url` and `API KEY` in `System - Configure`, after saving, it should normally be as shown in the picture ✅, otherwise please check the input and network connectivity

![20230406185218](https://github.com/LuckyPuppy514/image/raw/main/2023/2023-04-06/20230406185218.webp)
![20230406185313](https://github.com/LuckyPuppy514/image/raw/main/2023/2023-04-06/20230406185313.webp)

② For the first use, it is recommended to manually synchronize `Movie Title` and `Movie Rule` once (it will be automatically synchronized later)

![20230406185347](https://github.com/LuckyPuppy514/image/raw/main/2023/2023-04-06/20230406185347.webp)
![20230406185409](https://github.com/LuckyPuppy514/image/raw/main/2023/2023-04-06/20230406185409.webp)

③ In `Radarr - Settings - Indexer`, modify the `IP` and `Port` of `URL` of the indexer to the `IP` and `Port` of JProxy, and append the path `/radarr/jackett` (Prowlarr should append with `/radarr/prowlarr`)

`http://192.168.6.15:9117/api/v2.0/......` ➡️ `http://192.168.6.14:8117/radarr/jackett/api/v2.0/......`

![20230406185508](https://github.com/LuckyPuppy514/image/raw/main/2023/2023-04-06/20230406185508.webp)

💡 It is recommended to modify one first, and then modify other indexer configurations after confirming that it is normal

![20230406185100](https://github.com/LuckyPuppy514/image/raw/main/2023/2023-04-06/20230406185100.webp)

💡 When the Radarr quality language is configured as the main language, Radarr itself will automatically append the language title to the search, but at the same time, if the search result does not match the language, it will be rejected, so it is recommended

- `Anime` quality language is configured as the main language, because Radarr itself adds the main language query is more perfect, and `Anime` generally restricts the result language to the main language
- `Other` quality language is configured as Any, and JProxy will automatically append the main language title to query to avoid being rejected by Radarr because of the language

![20230406185555](https://github.com/LuckyPuppy514/image/raw/main/2023/2023-04-06/20230406185555.webp)

## Clean Title Regex

It is used to remove special characters in the title and improve the success rate of title matching (after updating, you need to manually synchronize the title or wait for the automatic update at regular intervals)

![20230406191734](https://github.com/LuckyPuppy514/image/raw/main/2023/2023-04-06/20230406191734.webp)
![20230406191818](https://github.com/LuckyPuppy514/image/raw/main/2023/2023-04-06/20230406191818.webp)

# Usage

## Cache

Used to clear the redis cache

- When the Series Title, TMDB Title or Movie Title synchronization prompt is too frequent, you can try to clear their cache and try again
- When the Configuration, Series Rule or Movie Rule are updated, but it does not take effect, you can try to clear their cache or all caches and try again

![20230406191621](https://github.com/LuckyPuppy514/image/raw/main/2023/2023-04-06/20230406191621.webp)

## Add Rule

![20230406191845](https://github.com/LuckyPuppy514/image/raw/main/2023/2023-04-06/20230406191845.webp)

| Properties | Description | Remarks |
| :-: | :-: | :-: |
| Token | If one of the same token matches successfully, it will not continue to match | can be used in `Indexer Format` using `{token}` |
| Priority | The priority of similar token is matched from small to large | Default 1000 |
| Matching regular expressions | Matching regular expressions | Refer to: [Rookie Tutorial](https://www.runoob.com/java/java-regular-expressions.html), it is recommended that a single rule should not be too complicated |
| Replacement content | Replacement content after successful matching | You can refer to existing rules |
| Offset | Only used by token `season` and `episode` | Please make sure to match the applicable scope of the regex to avoid affecting other rules |
| Test example | Used to test current rules | Can refer to existing rules |
| Test Results | Automatic Updates When Rules or Test Cases Change | - |
| Remarks | It is recommended to fill in the applicable example of the rules | You can refer to the existing rules |
| Author | The name of the author of the rule | Mainly for sharing |

![20230406191925](https://github.com/LuckyPuppy514/image/raw/main/2023/2023-04-06/20230406191925.webp)

## Sync Rule

You can limit the rules for synchronizing specified authors through `System - Configure - Rule Sync Author List`

![20230406192047](https://github.com/LuckyPuppy514/image/raw/main/2023/2023-04-06/20230406192047.webp)

[💡 The list of rules and authors that can be synchronized can be viewed here](https://github.com/LuckyPuppy514/jproxy/tree/main/src/main/resources/rule)

## Share Rule

① Select the rules that need to be shared and export

![20230406192128](https://github.com/LuckyPuppy514/image/raw/main/2023/2023-04-06/20230406192128.webp)

② `Fork` this project, submit the exported files to the `src/main/resources/rule` directory, and add the author name in `src/main/resources/rule/author.json`, and then submit a `Pull Request`

```text
[
  "LuckyPuppy514",
  "Author"
]
```

## Series Example

For batch testing (note that ✅ does not represent match accuracy, only formatting)

![20230406185640](https://github.com/LuckyPuppy514/image/raw/main/2023/2023-04-06/20230406185640.webp)

## Movie Example

For batch testing (note that ✅ does not represent match accuracy, only formatting)

![20230406190448](https://github.com/LuckyPuppy514/image/raw/main/2023/2023-04-06/20230406190448.webp)
