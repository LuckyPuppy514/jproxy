[中文](https://github.com/LuckyPuppy514/jproxy/blob/main/wiki.md) | [English](https://github.com/LuckyPuppy514/jproxy/blob/main/wiki.en_US.md)

- [进阶配置](#进阶配置)
  - [Sonarr](#sonarr)
    - [qBittorrent](#qbittorrent)
    - [TMDB](#tmdb)
  - [Radarr](#radarr)
  - [净标题正则](#净标题正则)
- [使用说明](#使用说明)
  - [缓存清理](#缓存清理)
  - [规则新增](#规则新增)
  - [规则同步](#规则同步)
  - [规则分享](#规则分享)
  - [剧集范例](#剧集范例)
  - [电影范例](#电影范例)

# 进阶配置

## Sonarr

### qBittorrent

> 🌟 定时重命名 Sonarr / Radarr 下载队列对应的种子名和文件名，减少导入错误的情况

填写 qBittorrent 的 `服务地址`，`用户名` 和 `密码`，保存后应该如图所示 ✅，否则请检查输入和网络连通性

![20230411121451](https://github.com/LuckyPuppy514/image/raw/main/2023/2023-04-11/20230411121451.webp)

### TMDB

> 🌟 同步 TMDB 标题，可用于搜索时追加主语言和备语言标题进行搜索，同时也能用于结果标题匹配，提升识别率，并且支持修改以便用户手动校准

① 如果没有 TMDB 账号，请先 [注册账号](https://www.themoviedb.org/signup)，然后 [获取 API 密钥](https://www.themoviedb.org/settings/api)，保存后正常应如图所示 ✅ ，否则请检查输入和网络连通性

![20230405203809](https://github.com/LuckyPuppy514/image/raw/main/2023/2023-04-05/20230405203809.webp)
![20230405203920](https://github.com/LuckyPuppy514/image/raw/main/2023/2023-04-05/20230405203920.webp)

② 首次使用，建议手动同步一次数据（后续会自动同步）

![20230405203944](https://github.com/LuckyPuppy514/image/raw/main/2023/2023-04-05/20230405203944.webp)

## Radarr

① 在 `系统配置 - 基础配置` 中填写 Radarr `服务地址` 和 `API 密钥`，保存后，正常应如图所示 ✅，否则请检查输入和网络连通性

![20230405202739](https://github.com/LuckyPuppy514/image/raw/main/2023/2023-04-05/20230405202739.webp)
![20230405202845](https://github.com/LuckyPuppy514/image/raw/main/2023/2023-04-05/20230405202845.webp)

② 首次使用，建议手动同步一次 `电影标题` 和 `电影规则`（后续会自动同步）

![20230405202924](https://github.com/LuckyPuppy514/image/raw/main/2023/2023-04-05/20230405202924.webp)
![20230405202947](https://github.com/LuckyPuppy514/image/raw/main/2023/2023-04-05/20230405202947.webp)

③ 在 `Radarr - 设置 - 索引器` 中，修改索引器 `URL` 的 `IP` 和 `端口号` 为 JProxy 的 `IP` 和 `端口号`，并追加路径 `/radarr/jackett`（Prowlarr 则追加 `/radarr/prowlarr`）

`http://192.168.6.15:9117/api/v2.0/......` ➡️ `http://192.168.6.14:8117/radarr/jackett/api/v2.0/......`

![20230405203051](https://github.com/LuckyPuppy514/image/raw/main/2023/2023-04-05/20230405203051.webp)

💡 建议先修改一个，确认正常后再修改其他的索引器配置

![20230406131806](https://github.com/LuckyPuppy514/image/raw/main/2023/2023-04-06/20230406131806.webp)

💡 Radarr 质量语言配置为主语言时，Radarr 本身会自动追加该语言标题进行查询，但同时搜索结果不符合该语言时会被拒绝，所以建议

- `动漫类型` 质量语言配置为主语言，因为 Radarr 本身追加主语言查询更完善，且 `动漫类型` 一般要限制结果语言为主语言
- `其他类型` 质量语言配置为 Any，由 JProxy 自动追加主语言标题进行查询，避免因为语言被 Radarr 拒绝

![20230406135619](https://github.com/LuckyPuppy514/image/raw/main/2023/2023-04-06/20230406135619.webp)

## 净标题正则

用于去除标题中的特殊字符，提升标题匹配成功率（更新后需手动同步标题或等待定时自动更新）

![20230406155417](https://github.com/LuckyPuppy514/image/raw/main/2023/2023-04-06/20230406155417.webp)

![20230406155525](https://github.com/LuckyPuppy514/image/raw/main/2023/2023-04-06/20230406155525.webp)

# 使用说明

## 缓存清理

用于清理缓存

- 当剧集标题，TMDB 标题或电影标题同步提示过于频繁时，可尝试清理对应缓存后重试
- 当基础配置，剧集规则或电影规则更新，但是未生效时，可尝试清理对应缓存或所有缓存后重试

![20230405114239](https://github.com/LuckyPuppy514/image/raw/main/2023/2023-04-05/20230405114239.webp)

## 规则新增

![20230406143727](https://github.com/LuckyPuppy514/image/raw/main/2023/2023-04-06/20230406143727.webp)

|   属性   |                说明                 |                                                  备注                                                   |
| :------: | :---------------------------------: | :-----------------------------------------------------------------------------------------------------: |
|   标记   | 同类标记匹配成功一个则不会继续匹配  |                                 可在 `索引器格式` 中利用 `{标记}` 使用                                  |
|  优先级  |   同类标记优先级从小到大进行匹配    |                                                默认 1000                                                |
| 匹配正则 |          匹配的正则表达式           | 可参考：[菜鸟教程](https://www.runoob.com/java/java-regular-expressions.html)，建议单个规则不要过于复杂 |
| 替换内容 |        匹配成功后的替换内容         |                                             可参考已有规则                                              |
|  偏移量  | 仅限标记 `season` 和 `episode` 使用 |                               请确保匹配正则的适用范围，避免影响其他规则                                |
| 测试范例 |          用于测试当前规则           |                                             可参考已有规则                                              |
| 测试结果 | 当规则或测试范例有变动时，自动更新  |                                                    -                                                    |
|   备注   |       建议填写规则适用的范例        |                                             可参考已有规则                                              |
|   作者   |           规则的作者名称            |                                              主要用于分享                                               |

![20230406151216](https://github.com/LuckyPuppy514/image/raw/main/2023/2023-04-06/20230406151216.webp)

## 规则同步

可通过 `系统设置 - 基础配置 - 规则同步作者列表` 限制同步指定作者的规则

![20230406145822](https://github.com/LuckyPuppy514/image/raw/main/2023/2023-04-06/20230406145822.webp)

[💡 可同步的规则及作者列表可在这里查看](https://github.com/LuckyPuppy514/jproxy/tree/main/src/main/resources/rule)

## 规则分享

① 选择需要分享的规则导出

![20230406145855](https://github.com/LuckyPuppy514/image/raw/main/2023/2023-04-06/20230406145855.webp)

② `Fork` 本项目，提交导出的文件到 `src/main/resources/rule` 目录，并在 `src/main/resources/rule/author.json` 添加作者名称，然后提交一个 `Pull Request`

```text
[
  "LuckyPuppy514",
  "作者名称"
]
```

## 剧集范例

用于批量测试（请注意 ✅ 并不代表匹配准确性，只代表进行了格式化）

![20230405114416](https://github.com/LuckyPuppy514/image/raw/main/2023/2023-04-05/20230405114416.webp)

## 电影范例

用于批量测试（请注意 ✅ 并不代表匹配准确性，只代表进行了格式化）

![20230405115146](https://github.com/LuckyPuppy514/image/raw/main/2023/2023-04-05/20230405115146.webp)
