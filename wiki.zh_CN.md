
[中文](https://github.com/LuckyPuppy514/jproxy/blob/main/wiki.zh_CN.md) | [English](https://github.com/LuckyPuppy514/jproxy/blob/main/wiki.md)

**目录**

- [进阶使用](#进阶使用)
  - [1. 代理路径配置](#1-代理路径配置)
  - [2. 新增规则](#2-新增规则)
    - [2.1. 基本属性](#21-基本属性)
    - [2.2. 匹配规则](#22-匹配规则)
    - [2.3. 常用正则](#23-常用正则)
  - [3. 规则管理](#3-规则管理)
    - [3.1. 查询](#31-查询)
    - [3.2. 启用，禁用，删除](#32-启用禁用删除)
    - [3.3. 分享](#33-分享)
    - [3.4. 导入，导出](#34-导入导出)
    - [3.5. 编辑](#35-编辑)
  - [4. 规则市场](#4-规则市场)
    - [4.1. 查询](#41-查询)
    - [4.2. 下载](#42-下载)
  - [5. 用例测试](#5-用例测试)
    - [5.1. 新增](#51-新增)
    - [5.2. 查询](#52-查询)
- [常见问题](#常见问题)
  - [1. 验证码无法显示](#1-验证码无法显示)
  - [2. 修改端口](#2-修改端口)
    - [2.1. Docker](#21-docker)
    - [2.2. Linux / Windows](#22-linux--windows)

# 进阶使用
## 1. 代理路径配置
如果 Jackett / Prowlarr 设置了路径，例如：/jackett，接口地址变为：
```
http://127.0.0.1:9117/jackett/api/...
```
则可以设置代理路径为：
```
/jackett/api/.*
```

> <font color="red">注意：代理路径匹配采用正则匹配</font>

![20220730123600](https://raw.githubusercontent.com/LuckyPuppy514/pic-bed/main/common/20220730123600.png)

## 2. 新增规则

### 2.1. 基本属性

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

| 属性 | 必填 | 影响执行效果 | 说明 |
| :---: | :---: | :------: | :------: |
| 规则名称 | <font color='red'>是</font> | 不影响 | 建议使用发布组，字幕组，剧集名称 |
| 规则语言 | <font color='red'>是</font> | 不影响 | 规则适用的语言 |
| 规则类型 | <font color='red'>是</font> | 不影响 | 规则分类（默认：发布组） |
| 正则类型 | <font color='red'>是</font> | 影响 | 搜索：用于替换搜索关键字；结果：用于格式化结果标题（默认：结果） |
| 执行规则 | <font color='red'>是</font> | 影响 | 一次：匹配成功一次，则不再执行其他一次规则；总是：即使有规则执行成功，还是会执行 |
| 执行优先级 | <font color='red'>是</font> | 影响 | 越小越优先执行 |
| 启用状态 | <font color='red'>是</font> | 影响 | 禁用则不会使用该规则 |
| 备注 | 否 | 不影响 | 可以填写规则说明，作者等信息 |

</div>

> <font color='red'>注意：执行规则为 “总是” 时，优先级数值建议设置为大一些，避免影响其他规则</font>

![20220730124030](https://raw.githubusercontent.com/LuckyPuppy514/pic-bed/main/common/20220730124030.png)


### 2.2. 匹配规则

<div class="center">

| 属性 | 必填 | 影响执行效果 | 说明 |
| :---: | :---: | :------: | :------: |
| 匹配正则 | <font color='red'>是</font> | 影响 | 用于匹配的正则表达式 |
| 替换正则 | <font color='red'>是</font> | 影响 | 用于替换的正则表达式 |
| 用例内容 | <font color='red'>是</font> | 不影响 | 用于测试的标题，每行一个 |

</div>

> <font color='red'>注意：这里的测试仅执行当前规则，项目内其他规则不会执行，要进行所有规则测试，请使用：测试用例</font>

![20220730130759](https://raw.githubusercontent.com/LuckyPuppy514/pic-bed/main/common/20220730130759.png)

### 2.3. 常用正则

Sonarr 动漫标题格式化建议
```
[发布组名称，非英文剧集名称，标题等信息] 英文名称 季 集 [分辨率，音频等信息][语言]
```
例如
```
[爱恋&漫猫字幕组][4月新番][测不准的阿波连同学] Aharen-san wa Hakarenai 09 [1080p][MP4][GB][简中]
[桜都字幕組][即使如此依舊步步進逼] Soredemo Ayumu wa Yosetekuru 01 [1080p][繁體內嵌]
[悠哈璃羽字幕社][RPG不动产] RPG Fudousan 06 [x264 1080p] [CHS]
[幻樱字幕组][间谍过家家 / 间谍家家酒] Spy x Family 09 [BIG5_MP4][1280X720]
```

<font color='red'>

+ 无关信息：使用 [ ] 包围，避免影响匹配
+ 重要信息：英文名称 季 集 [分辨率，音频等信息][语言]，Sonarr 匹配关键信息

</font>

<div class="center">

| 正则表达式 | 说明 | 匹配例子 |
| :---: | :---: | :---: |
| . | 匹配一个所有字符 | 'a', '1', '你' |
| .* | 匹配0个或多个所有字符 | '', 'abc', 'abc123' |
| .+ | 匹配1个或多个所有字符 | 'abc', 'abc123' |
| \d | 匹配一个数字 | '0', '1', '9' |
| \d+ | 匹配1个或多个数字 | '0', '123' |
| \s | 匹配一个空格 | ' ' |
| \s* | 匹配0个或多个空格 | '', '  ' |
| [a-zA-Z] | 匹配一个英文字母 | 'a', 'z', 'A' |
| [^a-zA-Z] | 匹配一个非英文字母的字符 | '1', '+', '-' |
| \ | 特殊字符需要加 \ 转译，例如：\\[, \\] | '[', ']' |

</div>

[更多正则表达式请参考菜鸟教程](https://www.runoob.com/java/java-regular-expressions.html)

匹配正则，例如：
```
\[(发布组)\] (.*) / (.*) \[(\d+)\](.*)
```
匹配类似以下格式的所有标题
```
[发布组] 所有字符A / 所有字符B [1个或多个数字]所有字符C
```
替换正则可以使用 $数字，取到对应()的内容
```
$1 => 第一个括号的内容：发布组
$2 => 第二个括号的内容：所有字符A
$3 => 第三个括号的内容：所有字符B
$4 => 第四个括号的内容：1个或多个数字
...
```
例如：
```
[$1][$3] $2 $4 $5
```
格式化后的内容
```
[发布组][所有字符B] 所有字符A 1个或多个数字 所有字符C
```

## 3. 规则管理
### 3.1. 查询
+ 规则名称：模糊查询
+ 其他选项：精确查询

![20220730141343](https://raw.githubusercontent.com/LuckyPuppy514/pic-bed/main/common/20220730141343.png)

### 3.2. 启用，禁用，删除
勾选需要处理的数据，点击对应按钮即可
> <font color='red'>注意：只有禁用的数据才能删除</font>

![20220730141418](https://raw.githubusercontent.com/LuckyPuppy514/pic-bed/main/common/20220730141418.png)

### 3.3. 分享
勾选需要分享的数据，点击右上角的分享按钮即可
> <font color='red'>注意：只有本地数据（可以根据小云朵判断，本地数据：-）才能分享；分享后的数据，同步原作者编辑和删除，但是启用状态不会同步</font>

![20220730141451](https://raw.githubusercontent.com/LuckyPuppy514/pic-bed/main/common/20220730141451.png)

### 3.4. 导入，导出
+ 导入：点击右上角导入图标，选择以往导出的数据，进行导入即可
+ 导出：勾选需要导出的数据，点击右上角导出图标即可  

![20220730141512](https://raw.githubusercontent.com/LuckyPuppy514/pic-bed/main/common/20220730141512.png)

### 3.5. 编辑
点击对应数据的编辑按钮即可
> <font color='red'>注意：下载的数据不建议编辑，因为每个小时会自动同步原作者的编辑，所以即使编辑后也会被覆盖，如果实在想要修改，建议自行新增对应规则</font>

![20220730141536](https://raw.githubusercontent.com/LuckyPuppy514/pic-bed/main/common/20220730141536.png)
![20220730141602](https://raw.githubusercontent.com/LuckyPuppy514/pic-bed/main/common/20220730141602.png)

## 4. 规则市场

### 4.1. 查询
+ 规则名称：模糊查询
+ 其他选项：精确匹配

![20220730141857](https://raw.githubusercontent.com/LuckyPuppy514/pic-bed/main/common/20220730141857.png)

### 4.2. 下载
找到想要下载的规则，勾选后，点击右上角下载按钮进行下载即可

![20220730141953](https://raw.githubusercontent.com/LuckyPuppy514/pic-bed/main/common/20220730141953.png)

## 5. 用例测试

> 主要是方便查看规则执行的效果

### 5.1. 新增
选择对应规则，输入需要测试的标题，每行一个，提交即可

![20220730142218](https://raw.githubusercontent.com/LuckyPuppy514/pic-bed/main/common/20220730142218.png)

### 5.2. 查询
可以通过规则名称，用例内容，进行模糊查询
> <font color='red'>注意：通过仅代表有规则匹配成功，不代表 Sonarr 就一定能识别，Sonarr 能否识别需要看格式化后的标题是否符合 Sonarr 识别的标准</font>

![20220730142357](https://raw.githubusercontent.com/LuckyPuppy514/pic-bed/main/common/20220730142357.png)

# 常见问题

## 1. 验证码无法显示
centos
```
yum install fontconfig
fc-cache --force
```
alpine
```
apk add --update font-adobe-100dpi ttf-dejavu fontconfig
```

## 2. 修改端口
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
      - 你要想的端口号:8117
    restart: unless-stopped
```

docker run
```
docker pull luckypuppy514/jproxy:latest
```
```
docker run --name jproxy \
-v /docker/jproxy/config:/app/config \
-p 你想要的端口:8117 \
-e TZ=Asia/Shanghai \
--restart unless-stopped \
-d luckypuppy514/jproxy
```

> <font color='red'>注意：docker 网络非 host 模式，Jackett / Prowlarr 的 IP 就不能使用：127.0.0.1，需要修改为宿主机的具体 IP</font>

### 2.2. Linux / Windows
1. [下载配置文件：application.yml](https://raw.githubusercontent.com/LuckyPuppy514/jproxy/main/release/jar/config/application.yml)

>
2. 修改端口号，并保存到：config/ 目录，重启即可
```
server:
   port: 8117
```

