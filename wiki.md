
[中文](https://github.com/LuckyPuppy514/jproxy/blob/main/wiki.zh_CN.md) | [English](https://github.com/LuckyPuppy514/jproxy/blob/main/wiki.md)

**目录**

- [Advanced Usage](#advanced-usage)
  - [1. Proxy Path Setting](#1-proxy-path-setting)
  - [2. Add Rule](#2-add-rule)
    - [2.1. Basic Attributes](#21-basic-attributes)
    - [2.2. Match Regular](#22-match-regular)
    - [2.3. Regular Expressions](#23-regular-expressions)
  - [3. Rule Manage](#3-rule-manage)
    - [3.1. Search](#31-search)
    - [3.2. Enable, Disable, Delete](#32-enable-disable-delete)
    - [3.3. Share](#33-share)
    - [3.4. Import, Export](#34-import-export)
    - [3.5. Edit](#35-edit)
  - [4. Rule Market](#4-rule-market)
    - [4.1. Search](#41-search)
    - [4.2. Dowanload](#42-dowanload)
  - [5. Test Example](#5-test-example)
    - [5.1. Add](#51-add)
    - [5.2. Search](#52-search)
- [Common Problem](#common-problem)
  - [1. Captcha Error](#1-captcha-error)
  - [2. Modify Running Port](#2-modify-running-port)
    - [2.1. Docker](#21-docker)
    - [2.2. Linux / Windows](#22-linux--windows)

# Advanced Usage
## 1. Proxy Path Setting
If Jackett / Prowlarr have setted a special base path，like：/jackett, and api path like: 
```
http://127.0.0.1:9117/jackett/api/...
```
and then proxy path should be like: 
```
/jackett/api/.*
```

> <font color="red">warning: proxy path match with regular expressions</font>

![20220730145817](https://raw.githubusercontent.com/LuckyPuppy514/pic-bed/main/common/20220730145817.png)

## 2. Add Rule

### 2.1. Basic Attributes

<style>
.center 
{
  width: auto;
  display: table;
  margin-left: auto;
  margin-right: auto;
}
</style>

<div class="center">

| Attribute | Required | Affect the execution effect | explanation |
| :---: | :---: | :------: | :------: |
| RuleName | <font color='red'>yes</font> | no | release group name, anime or serial title |
| RuleLanguage | <font color='red'>yes</font> | no | the language which the rule match |
| RuleType | <font color='red'>yes</font> | no | rule type (default: Release Group） |
| RegularType | <font color='red'>yes</font> | yes | Search: used to replace search key; result: used to format the result title (default: result) |
| ExecuteRule | <font color='red'>yes</font> | yes | Once: match success once and do not execute other Once rule; Always: still execute while other rules match |
| ExecutePriority | <font color='red'>yes</font> | yes | smaller execute first |
| Validstatus | <font color='red'>yes</font> | yes | disable and do not execute it |
| Remark | <font color='red'>no</font> | no | rule explanation, author ... |

</div>

> <font color='red'>warning: when ExecuteRule is Always, ExecutePriority value should be setted to bigger, so that it will not affect other rules</font>

![20220730151458](https://raw.githubusercontent.com/LuckyPuppy514/pic-bed/main/common/20220730151458.png)

### 2.2. Match Regular

<div class="center">

| Attribute | Required | Affect the execution effect | explanation |
| :---: | :---: | :------: | :------: |
| RegularMatch | <font color='red'>yes</font> | yes | regular expressions used to match |
| RegularReplace | <font color='red'>yes</font> | yes | regular expressions used to replace |
| ExampleContent | <font color='red'>yes</font> | no | titles used to test, one for per line |

</div>

> <font color='red'>warning: it only test the rule input, other rules will not execute, if you want to test all, please use Test Example</font>

![20220730151642](https://raw.githubusercontent.com/LuckyPuppy514/pic-bed/main/common/20220730151642.png)

### 2.3. Regular Expressions

Sonarr Anime Title Format Suggestion
```
[Release Group, not english info] name-in-english season episode [resolution, audio info][language][other info]
```
example
```
[爱恋&漫猫字幕组][4月新番][测不准的阿波连同学] Aharen-san wa Hakarenai 09 [1080p][MP4][GB][简中]
[桜都字幕組][即使如此依舊步步進逼] Soredemo Ayumu wa Yosetekuru 01 [1080p][繁體內嵌]
[悠哈璃羽字幕社][RPG不动产] RPG Fudousan 06 [x264 1080p] [CHS]
[幻樱字幕组][间谍过家家 / 间谍家家酒] Spy x Family 09 [BIG5_MP4][1280X720]
```

<font color='red'>

+ other info: using [ ] include
+ important info: name-in-english season episode [resolution, audio info][language], it is the key sonarr match or not

</font>

[Regular Expressions In Java](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html)

RegularMatch Example:
```
\[(发布组)\] (.*) / (.*) \[(\d+)\](.*)
```
match the title like: 
```
[发布组] 所有字符A / 所有字符B [1个或多个数字]所有字符C
```
use $number can get the content in()

```
$1 => first ()：发布组
$2 => second ()：所有字符A
$3 => third ()：所有字符B
$4 => fourth ()：1个或多个数字
...
```
example: 
```
[$1][$3] $2 $4 $5
```
format result: 
```
[发布组][所有字符B] 所有字符A 1个或多个数字 所有字符C
```

## 3. Rule Manage
### 3.1. Search
+ RuleName：Fuzzy
+ Others：Exact

![20220730153625](https://raw.githubusercontent.com/LuckyPuppy514/pic-bed/main/common/20220730153625.png)

### 3.2. Enable, Disable, Delete
choose and click the button
> <font color='red'>warning: only disable rule can be deleted</font>

![20220730153808](https://raw.githubusercontent.com/LuckyPuppy514/pic-bed/main/common/20220730153808.png)

### 3.3. Share
choose and click share button at top right

> <font color='red'>warning: only local rule can be shared (local rule: -), rules shared will sync edit by author, but validstatus will not sync</font>

![20220730154233](https://raw.githubusercontent.com/LuckyPuppy514/pic-bed/main/common/20220730154233.png)

### 3.4. Import, Export

![20220730154320](https://raw.githubusercontent.com/LuckyPuppy514/pic-bed/main/common/20220730154320.png)

### 3.5. Edit
> <font color='red'>warning: rule from market will sync author edit every hour, so it is suggested that if you want to edit it, please add it by yourself</font>

![20220730154639](https://raw.githubusercontent.com/LuckyPuppy514/pic-bed/main/common/20220730154639.png)

## 4. Rule Market

### 4.1. Search
+ RuleName：Fuzzy
+ Others：Exact

![20220730154717](https://raw.githubusercontent.com/LuckyPuppy514/pic-bed/main/common/20220730154717.png)

### 4.2. Dowanload
search and choose, click download button at top right

![20220730154147](https://raw.githubusercontent.com/LuckyPuppy514/pic-bed/main/common/20220730154147.png)

## 5. Test Example

> test rule more easily

### 5.1. Add
choose rule, and input title you want to test, one for per line

![20220730154947](https://raw.githubusercontent.com/LuckyPuppy514/pic-bed/main/common/20220730154947.png)

### 5.2. Search
> <font color='red'>warning: pass only mean that match success, sonarr will not accept it 100 percent</font>

![20220730155019](https://raw.githubusercontent.com/LuckyPuppy514/pic-bed/main/common/20220730155019.png)

# Common Problem

## 1. Captcha Error
centos
```
yum install fontconfig
fc-cache --force
```
alpine
```
apk add --update font-adobe-100dpi ttf-dejavu fontconfig
```

## 2. Modify Running Port
### 2.1. Docker
docker-compose
```
version: "3"
services:
  jproxy:
    image: luckypuppy514/jproxy:latest
    container_name: jproxy
    environment:
      - TZ=Asia/Shanghai
    volumes:
      - /docker/jproxy/config:/app/config
    ports:
      - port you want:8117
    restart: unless-stopped
```

docker run
```
docker pull luckypuppy514/jproxy:latest
```
```
docker run --name jproxy \
-v /docker/jproxy/config:/app/config \
-p port you want:8117 \
-e TZ=Asia/Shanghai \
--restart unless-stopped \
-d luckypuppy514/jproxy
```

> <font color='red'>warning：when network mode is not host，Jackett / Prowlarr can not use ip: 127.0.0.1, you should use your server ip</font>

### 2.2. Linux / Windows
1. [donwload application.yml](https://raw.githubusercontent.com/LuckyPuppy514/jproxy/main/release/jar/config/application.yml)

>
1. modify server.port, save into the folder: config/, and restart
```
server:
   port: 8117
```

