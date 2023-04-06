package com.lckp.jproxy;

import java.util.Collections;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

/**
 * 
 * <p>
 * 代码生成器
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-04
 */
public class CodeGenerator {

	public static void main(String[] args) {
		// 数据库
		String tableName = "system_user";
		String dbUsername = "";
		String dbPassword = "";
		String dbUrl = "jdbc:sqlite:target\\classes\\database\\jproxy.db";
		
		// 作者
		String author = "LuckyPuppy514";
		// 包名
		String packageName = CodeGenerator.class.getPackageName();
		// 输出目录
		String outputDir = System.getProperty("user.dir") + "/src/main/java/";
		String xmlOutputDir = System.getProperty("user.dir") + "/src/main/resources/mapper";
		
		FastAutoGenerator.create(dbUrl, dbUsername, dbPassword)
			.globalConfig(builder -> builder.author(author)
					.disableOpenDir()
					.enableSpringdoc()
					.outputDir(outputDir)
			)
			.packageConfig(builder -> builder.parent(packageName)
					.pathInfo(Collections.singletonMap(OutputFile.xml, xmlOutputDir))
			)
			.strategyConfig(builder -> builder.addInclude(tableName)
					.controllerBuilder().enableRestStyle()
					.entityBuilder().enableLombok()
			)
			.templateEngine(new FreemarkerTemplateEngine())
			.templateConfig(builder -> builder.disable()
					.entity("/templates/entity.java")
				    .service("/templates/service.java")
				    .serviceImpl("/templates/serviceImpl.java")
				    .mapper("/templates/mapper.java")
				    .xml("/templates/mapper.xml")
				    .controller("/templates/controller.java")
			)
			.execute();
	}
}
