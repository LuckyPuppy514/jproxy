/**
 * @Title: FormatTest.java
 * @version V1.0
 */
package com.lckp.jproxy.test;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;

import com.lckp.jproxy.util.FormatUtil;

/**
 * @className: FormatTest
 * @description: 
 * @date 2022年6月11日
 * @author LuckyPuppy514
 */
public class FormatTest {
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) {	
		String[] titles = {	
				"[愛戀&漫貓字幕組][4月新番][不會拿捏距離的阿波連同學][Aharen-san wa Hakarenai][09][1080p][MP4][BIG5][繁中]",
				"[爱恋&漫猫字幕组][4月新番][测不准的阿波连同学][Aharen-san wa Hakarenai][09][1080p][MP4][GB][简中]",
				"【喵萌奶茶屋】★04月新番★[阿波连摸不清/测不准的阿波连同学/Aharen-san wa Hakarenai][11][1080p][繁体][招募翻译校对]",
				"[悠哈璃羽字幕社] [RPG不动产_RPG Fudousan] [06] [x264 1080p] [CHS]",
				"[悠哈璃羽字幕社] [RPG不动产_RPG Fudousan] [05] [x264 1080p] [CHS]",
				"【千夏字幕组】【测不准的阿波连同学 / 不会拿捏距离的阿波连同学_Aharen-san wa Hakarenai】[第09话][1080p_AVC][繁体]",
				"【千夏字幕組】【測不準的阿波連同學 / 不會拿捏距離的阿波連同學_Aharen-san wa Hakarenai】[第09話][1080p_AVC][繁體]",
				"[动漫国字幕组&LoliHouse] Spy x Family / 间谍过家家 - 07 [WebRip 1080p HEVC-10bit AAC][简繁内封字幕]",
				"【幻樱字幕组】【4月新番】【古见同学有交流障碍症 Komi-san wa, Komyushou Desu.】【14】【GB_MP4】【1280X720】",
				"【幻樱字幕组】【4月新番】【间谍过家家 / 间谍家家酒 SPY×FAMILY】【09】【BIG5_MP4】【1280X720】",
				"【幻櫻字幕組】【4月新番】【古見同學有交流障礙症 Komi-san wa, Komyushou Desu.】【18】【BIG5_MP4】【1280X720】",
				"[猎户不鸽发布组] 社畜小姐想被幽灵幼女治愈。 Shachiku-san wa Youjo Yuurei ni Iyasaretai [10] [1080p+] [简中] [网盘] [2022年4月番]",
				"[猎户不鸽发布组] 骸骨骑士大人异世界冒险中 Gaikotsu Kishi-sama, Tadaima Isekai e Odekakechuu [10] [1080p+] [简中] [网盘] [2022年4月番]",
				"[ANi] RPG Real Estate - RPG 不動產（僅限港澳台地區） - 08 [1080P][Bilibili][WEB-DL][AAC AVC][CHT CHS][MP4]",
				"[ANi] The Rising of the Shield Hero S2 - 盾之勇者成名录 第二季 - 09 [1080P][Baha][WEB-DL][AAC AVC][CHT][MP4]",
				"[ANi] Skeleton Knight in Another World - 骸骨騎士大人異世界冒險中[01][1080P][Baha][WEB-DL][AAC AVC][MP4]",
				"[NC-Raws] 测不准的阿波连同学 / Aharen-san wa Hakarenai - 11 (B-Global 3840x2160 HEVC AAC MKV)",
				"[NC-Raws] RPG不动产 / RPG Fudousan - 11 (B-Global 1920x1080 HEVC AAC MKV)",
				"[NC-Raws] 盾之勇者成名录 S2 / Tate no Yuusha no Nariagari S2 - 11 (B-Global 1920x1080 HEVC AAC MKV)",
				"[猎户随缘发布组] 女忍者椿的心事 Kunoichi Tsubaki no Mune no Uchi [09] [1080p] [简中内封] [2022年4月番]",
				"[猎户随缘发布组] 约会大作战S4 Date A Live IV [10] [1080p] [简中内封] [2022年4月番]",
				"[Lilith-Raws] 史上最強大魔王轉生為村民 A / Shijou Saikyou no Daimaou - 11 [Baha][WEB-DL][1080p][AVC AAC][CHT][MP4]"
		};
		
		String[] rules = {
				"{\\[(爱恋&漫猫字幕组|愛戀&漫貓字幕組)\\](.*)\\[([a-zA-Z].*)\\]\\[(\\d+)\\](.*)}:{[$1]$2 $3 $4 $5}",
				"{【喵萌奶茶屋】.*\\[(.*)/([a-zA-Z].*)\\]\\[(\\d+)\\](.*)}:{[喵萌奶茶屋] $1 / $2 $3 $4}",
				"{\\[悠哈璃羽字幕社\\] \\[(.*)_(.*)\\] \\[(\\d+)\\](.*)}:{[悠哈璃羽字幕社] $2 $3$4}",
				"{【(千夏字幕组|千夏字幕組)】【(.*)_(.*)】\\[第(\\d+)(话|話)\\](.*)}:{[$1] $3 $4 $6}",
				"{\\[(.*)&LoliHouse\\] (.*) / (.*) - (.*) }:{[$1&LoliHouse] $3 / $2 $4}",
				"{【(幻樱字幕组|幻櫻字幕組)】【(.*)】【([^a-zA-Z]*)([a-zA-Z].*)】【(\\d+)】【([^【】]*)】【([^【】]*)】.*}:{[$1] $3 / $4 $5 [$6][$7]}",
				"{\\[猎户不鸽发布组\\] ([^ ]*) (.*) \\[(\\d+)\\](.*)}:{[猎户不鸽发布组] $1 / $2 $3$4}",
				"{\\[ANi\\] (.*) - (.*)\\s*[-|\\[]\\s*(\\d+)[ \\]](.+)}:{$1 $3 $4}",
				"{\\[NC-Raws\\] (.*) \\/ (.*) - (\\d+)(.*)}:{[NC-Raws] $1 / $2 $3$4 [简中\\]}",
				"{\\[猎户随缘发布组\\] ([^ ]*) ([a-zA-Z].*) \\[(\\d+)\\](.*)}:{[猎户随缘发布组] $1 / $2 $3$4}",
		        "{\\[Lilith-Raws\\](.*)Shijou Saikyou no Daimaou}:{[Lilith-Raws]$1Shijou Saikyou no Daimaou, Murabito A ni Tensei suru}"
		};
		
		List<String> ruleList = new ArrayList(Arrays.asList(rules));
		
		for(String title: titles) {
			System.out.println("原标题：" + title);
			System.out.println("格式化：" + FormatUtil.accurateFormat(title, ruleList));
			System.out.println();
		}
	}
}
