[中文](https://github.com/LuckyPuppy514/jproxy/blob/main/wiki.zh_CN.md) | [English](https://github.com/LuckyPuppy514/jproxy/blob/main/wiki.md)

- [🐳 Advanced Usage](#-advanced-usage)
  - [1. Add Rule](#1-add-rule)
    - [1.1. Basic Attributes](#11-basic-attributes)
    - [1.2. Match Regular](#12-match-regular)
    - [1.3. Regular Expressions](#13-regular-expressions)
  - [2. Rule Manage](#2-rule-manage)
    - [2.1. Search](#21-search)
    - [2.2. Enable, Disable, Delete](#22-enable-disable-delete)
    - [2.3. Share](#23-share)
    - [2.4. Import, Export](#24-import-export)
    - [2.5. Edit](#25-edit)
  - [3. Rule Market](#3-rule-market)
    - [3.1. Search](#31-search)
    - [3.2. Dowanload](#32-dowanload)
  - [4. Test Example](#4-test-example)
    - [4.1. Add](#41-add)
    - [4.2. Search](#42-search)
- [😰 Common Problem](#-common-problem)
  - [1. Captcha Error](#1-captcha-error)
  - [2. Modify Running Port](#2-modify-running-port)
    - [2.1. Docker](#21-docker)
    - [2.2. Linux / Windows](#22-linux--windows)

# 🐳 Advanced Usage

## 1. Add Rule

### 1.1. Basic Attributes

| Attribute | Required | Affect the execution effect | explanation |
| :---: | :---: | :------: | :------: |
| RuleName | ⭕ | ❌ | release group name, anime or serial title |
| RuleLanguage | ⭕ | ❌ | the language which the rule match |
| RuleType | ⭕ | ❌ | rule type (default: Release Group） |
| RegularType | ⭕ | ⭕ | Search: used to replace search key; result: used to format the result title (default: result) |
| ExecuteRule | ⭕ | ⭕ | Once: match success once and do not execute other Once rule; Always: still execute while other rules match |
| ExecutePriority | ⭕ | ⭕ | smaller execute first |
| Validstatus | ⭕ | ⭕ | disable and do not execute it |
| Remark | ❌ | ❌ | rule explanation, author ... |

> 🔥warning🔥: when ExecuteRule is Always, ExecutePriority value should be setted to bigger, so that it will not affect other rules

![20220730151458](https://raw.githubusercontent.com/LuckyPuppy514/pic-bed/main/common/20220730151458.png)

### 1.2. Match Regular

| Attribute | Required | Affect the execution effect | explanation |
| :---: | :---: | :------: | :------: |
| RegularMatch | ⭕ | ⭕ | regular expressions used to match |
| RegularReplace | ⭕ | ⭕ | regular expressions used to replace |
| ExampleContent | ⭕ | ❌ | titles used to test, one for per line |

> 🔥warning🔥: it only test the rule input, other rules will not execute, if you want to test all, please use Test Example

![20220730151642](https://raw.githubusercontent.com/LuckyPuppy514/pic-bed/main/common/20220730151642.png)

### 1.3. Regular Expressions

Sonarr Anime Title Format Suggestion

```text
[Release Group, not english info] name-in-english season episode [resolution, audio info][language][other info]
```

example

```text
[爱恋&漫猫字幕组][4月新番][测不准的阿波连同学] Aharen-san wa Hakarenai 09 [1080p][MP4][GB][简中]
[桜都字幕組][即使如此依舊步步進逼] Soredemo Ayumu wa Yosetekuru 01 [1080p][繁體內嵌]
[悠哈璃羽字幕社][RPG不动产] RPG Fudousan 06 [x264 1080p] [CHS]
[幻樱字幕组][间谍过家家 / 间谍家家酒] Spy x Family 09 [BIG5_MP4][1280X720]
```

- other info: using [ ] include
- important info: name-in-english season episode [resolution, audio info][language], it is the key sonarr match or not

[Regular Expressions In Java](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html)

RegularMatch Example:

```text
\[(发布组)\] (.*) / (.*) \[(\d+)\](.*)
```

match the title like:

```text
[发布组] 所有字符A / 所有字符B [1个或多个数字]所有字符C
```

use $number can get the content in()

```text
$1 => first ()：发布组
$2 => second ()：所有字符A
$3 => third ()：所有字符B
$4 => fourth ()：1个或多个数字
...
```

example:

```text
[$1][$3] $2 $4 $5
```

format result:

```text
[发布组][所有字符B] 所有字符A 1个或多个数字 所有字符C
```

## 2. Rule Manage

### 2.1. Search

- RuleName：Fuzzy

- Others：Exact

![20220730153625](https://raw.githubusercontent.com/LuckyPuppy514/pic-bed/main/common/20220730153625.png)

### 2.2. Enable, Disable, Delete

choose and click the button
> 🔥warning🔥: only disable rule can be deleted

![20220730153808](https://raw.githubusercontent.com/LuckyPuppy514/pic-bed/main/common/20220730153808.png)

### 2.3. Share

choose and click share button at top right

> 🔥warning🔥: only local rule can be shared (local rule: -), rules shared will sync edit by author, but delete or validstatus will not sync

![20220730154233](https://raw.githubusercontent.com/LuckyPuppy514/pic-bed/main/common/20220730154233.png)

### 2.4. Import, Export

![20220730154320](https://raw.githubusercontent.com/LuckyPuppy514/pic-bed/main/common/20220730154320.png)

### 2.5. Edit

> 🔥warning🔥: rule from market will sync author edit every hour, so it is suggested that if you want to edit it, please add it by yourself

![20220730154639](https://raw.githubusercontent.com/LuckyPuppy514/pic-bed/main/common/20220730154639.png)

## 3. Rule Market

### 3.1. Search

- RuleName：Fuzzy
- Others：Exact

![20220730154717](https://raw.githubusercontent.com/LuckyPuppy514/pic-bed/main/common/20220730154717.png)

### 3.2. Dowanload

search and choose, click download button at top right

![20220730154147](https://raw.githubusercontent.com/LuckyPuppy514/pic-bed/main/common/20220730154147.png)

## 4. Test Example

> test rule more easily

### 4.1. Add

choose rule, and input title you want to test, one for per line

![20220730154947](https://raw.githubusercontent.com/LuckyPuppy514/pic-bed/main/common/20220730154947.png)

### 4.2. Search

> 🔥warning🔥: pass only mean that match success, sonarr will not accept it 100 percent

![20220730155019](https://raw.githubusercontent.com/LuckyPuppy514/pic-bed/main/common/20220730155019.png)

# 😰 Common Problem

## 1. Captcha Error

centos

```text
yum install fontconfig
fc-cache --force
```

alpine

```text
apk add --update font-adobe-100dpi ttf-dejavu fontconfig
```

## 2. Modify Running Port

### 2.1. Docker

docker-compose

```text
version: "3"
services:
  jproxy:
    image: luckypuppy514/jproxy:latest
    container_name: jproxy
    environment:
      - TZ=Asia/Shanghai
      - "JAVA_OPTS=-Xms256m -Xmx256m"
    volumes:
      - /docker/jproxy/config:/app/config
    ports:
      - port you want:8117
    restart: unless-stopped
```

docker run

```text
docker pull luckypuppy514/jproxy:latest
```

```text
docker run --name jproxy \
-v /docker/jproxy/config:/app/config \
-e TZ=Asia/Shanghai \
-e "JAVA_OPTS=-Xms256m -Xmx256m" \
-p port you want:8117 \
--restart unless-stopped \
-d luckypuppy514/jproxy:latest
```

> 🔥warning🔥：when network mode is not host，Jackett / Prowlarr can not use ip: 127.0.0.1, you should use your server ip

### 2.2. Linux / Windows

1. donwload if not exist: [config/application.yml](https://raw.githubusercontent.com/LuckyPuppy514/jproxy/main/release/jar/config/application.yml)
   >
2. modify server.port and restart

```text
server:
   port: 8117
```
