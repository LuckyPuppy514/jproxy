/**  
* @Title: CodeGenerator.java
* @version V1.0  
*/
package com.lckp.generator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * @ClassName: CodeGenerator
 * @Description: 代码生成器（支持controller,service,model,mapper）
 * @author Liujiawang
 * @date 2020年7月10日 下午3:20:04
 *
 */
public class CodeGenerator {
	// 配置文件路径
	private static String propertiesPath = "/generator";
	// 数据库配置
	private static Properties jdbcProperties = new Properties();
	// 模板参数值配置
	private static Properties valueProperties = new Properties();

	// 模板
	private static String controllerTemplate = "";
	private static String serviceTemplate = "";
	private static String iserviceTemplate = "";
	private static String mapperTemplate = "";
	private static String mapperXmlTemplate = "";
	private static String modelTemplate = "";

	private static Connection dbConnection;
	private static Statement statement;

	private static final Logger LOGGER = LoggerFactory.getLogger(CodeGenerator.class);

	public static void main(String args[]) {
		try {
			readProperties();
			loadTeamlate();

			Scanner scanner = new Scanner(System.in);
			if (StringUtils.isBlank(valueProperties.getProperty("name"))) {
				System.out.println("请输入模块名：");
				String name = scanner.nextLine();
				System.out.println("请输入模块中文含义：");
				String comment = scanner.nextLine();
				valueProperties.setProperty("name", name);
				valueProperties.setProperty("lname", name.substring(0, 1).toLowerCase() + name.substring(1));
				valueProperties.setProperty("comment", comment);
			}

			System.out.println("请输入需要创建的代码块（C：controller，S：service，M：mapper,E:model）");
			String code = scanner.nextLine();
			code = code.toUpperCase();

			if (code.contains("C")) {
				generateController();
			}
			if (code.contains("S")) {
				generateService();
			}
			if (code.contains("M")) {
				generateMapper();
			}
			if (code.contains("E")) {
				if (StringUtils.isBlank(valueProperties.getProperty("tableName"))) {
					System.out.println("请输入表名：");
					String tableName = scanner.nextLine();
					jdbcProperties.setProperty("tableName", tableName);
				}
				generateModel();
			}
			scanner.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 读取配置文件
	public static void readProperties() throws Exception {
		// 读取数据库配置
		jdbcProperties.load(new InputStreamReader(
				new ClassPathResource(propertiesPath + "/jdbc.properties").getInputStream(), "utf8"));

		// 读取模板参数值配置
		valueProperties.load(new InputStreamReader(
				new ClassPathResource(propertiesPath + "/value.properties").getInputStream(), "utf8"));
		// 设置生成时间
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		valueProperties.setProperty("date", dateFormat.format(new Date()));
		String basePackageName = valueProperties.getProperty("basePackageName");
		// 设置基础目录
		valueProperties.setProperty("basePath",
				System.getProperty("user.dir") + "/src/main/java/" + basePackageName.replace(".", "/"));
	}

	// 加载模板
	public static void loadTeamlate() throws IOException {
		String templatePath = valueProperties.get("templatePath").toString();
		// 读取模板
		InputStream controllerStream = new ClassPathResource(templatePath + "/Controller.java").getInputStream();
		controllerTemplate = readStream(controllerStream);
		InputStream iserviceStream = new ClassPathResource(templatePath + "/IService.java").getInputStream();
		iserviceTemplate = readStream(iserviceStream);
		InputStream serviceStream = new ClassPathResource(templatePath + "/Service.java").getInputStream();
		serviceTemplate = readStream(serviceStream);
		InputStream mapperStream = new ClassPathResource(templatePath + "/Mapper.java").getInputStream();
		mapperTemplate = readStream(mapperStream);
		InputStream mapperXmlStream = new ClassPathResource(templatePath + "/Mapper.xml").getInputStream();
		mapperXmlTemplate = readStream(mapperXmlStream);
		InputStream modelStream = new ClassPathResource(templatePath + "/Model.java").getInputStream();
		modelTemplate = readStream(modelStream);
	}

	public static String readStream(InputStream inputStream) throws IOException {
		StringBuilder builder = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String str = null;
		while ((str = reader.readLine()) != null) {
			builder.append(str + "\r\n");
		}
		return builder.toString();
	}

	// 写入模板参数
	public static String writeValue(String template) {
		// valueProperties.setProperty("author", System.getenv().get("USERNAME"));
		for (Object key : valueProperties.keySet()) {
			template = template.replace("[" + key + "]", valueProperties.getProperty(key.toString()));
		}
		return template;
	}

	public static void generateController() throws IOException {
		// 创建controller目录
		File controllerDir = new File(
				valueProperties.getProperty("basePath") + "/" + valueProperties.getProperty("controllerPath"));
		if (!controllerDir.exists()) {
			controllerDir.mkdir();
		}

		// 创建controller
		File controller = new File(
				controllerDir.getPath() + "/" + valueProperties.getProperty("name") + "Controller.java");
		if (!controller.exists()) {
			controller.createNewFile();
		} else {
			System.err.println("Controller已存在");
			return;
		}
		// 替换缺省代码中的参数
		String defaultCode = writeValue(controllerTemplate);

		// 写入缺省代码
		write(defaultCode, controller);
		System.out.println("创建Controller成功：" + controller.getAbsolutePath());
	}

	public static void generateService() throws IOException {
		// 创建iservice目录
		File iserviceDir = new File(
				valueProperties.getProperty("basePath") + "/" + valueProperties.getProperty("servicePath"));
		if (!iserviceDir.exists()) {
			iserviceDir.mkdir();
		}
		// 创建iservice
		File iservice = new File(iserviceDir.getAbsoluteFile() + "/" + valueProperties.getProperty("iservicePath") + "/"
				+ valueProperties.getProperty("interfacePrefix") + valueProperties.getProperty("name")
				+ "Service.java");
		if (!iservice.exists()) {
			iservice.createNewFile();
		}  else {
			System.err.println("IService已存在");
			return;
		}

		// 替换缺省代码中的参数
		String defaultCode = writeValue(iserviceTemplate);

		// 写入缺省代码
		write(defaultCode, iservice);
		System.out.println("创建IService成功：" + iservice.getAbsolutePath());

		// 创建service目录
		File serviceDir = new File(
				valueProperties.getProperty("basePath") + "/" + valueProperties.getProperty("servicePath"));
		if (!serviceDir.exists()) {
			serviceDir.mkdir();
		}
		// 创建serviceImpl目录
		File serviceImplDir = new File(serviceDir.getPath() + "/" + valueProperties.getProperty("serviceImplPath"));
		if (!serviceImplDir.exists()) {
			serviceImplDir.mkdir();
		}
		// 创建serviceImpl
		File serviceImpl = new File(
				serviceImplDir.getPath() + "/" + valueProperties.getProperty("name") + "ServiceImpl.java");
		if (!serviceImpl.exists()) {
			serviceImpl.createNewFile();
		} else {
			System.err.println("ServiceImpl已存在");
			return;
		}

		// 替换缺省代码中的参数
		defaultCode = writeValue(serviceTemplate);

		// 写入缺省代码
		write(defaultCode, serviceImpl);
		System.out.println("创建ServiceImpl成功：" + serviceImpl.getAbsolutePath());
	}

	public static void generateMapper() throws IOException {
		// 创建mapper目录
		File mapperDir = new File(
				valueProperties.getProperty("basePath") + "/" + valueProperties.getProperty("mapperPath"));
		if (!mapperDir.exists()) {
			mapperDir.mkdir();
		}
		// 创建mapper
		File mapper = new File(mapperDir.getPath() + "/" + valueProperties.getProperty("interfacePrefix")
				+ valueProperties.getProperty("name") + "Mapper.java");
		if (!mapper.exists()) {
			mapper.createNewFile();
		} else {
			System.err.println("Mapper已存在");
			return;
		}

		// 替换缺省代码中的参数
		String defaultCode = writeValue(mapperTemplate);

		// 写入缺省代码
		write(defaultCode, mapper);
		System.out.println("创建Mapper成功：" + mapper.getAbsolutePath());

		// 创建mapper目录
		File mapperXmlDir = new File(
				valueProperties.getProperty("basePath").replace("src/main/java", "src/main/resources") + "/"
						+ valueProperties.getProperty("mapperPath"));
		if (!mapperDir.exists()) {
			mapperDir.mkdir();
		}
		// 创建mapper
		File mapperXml = new File(mapperXmlDir.getPath() + "/" + valueProperties.getProperty("interfacePrefix")
				+ valueProperties.getProperty("name") + "Mapper.xml");
		if (!mapperXml.exists()) {
			mapperXml.createNewFile();
		}  else {
			System.err.println("MapperXml已存在");
			return;
		}

		// 替换缺省代码中的参数
		defaultCode = writeValue(mapperXmlTemplate);

		// 写入缺省代码
		write(defaultCode, mapperXml);
		System.out.println("创建MapperXml成功：" + mapperXml.getAbsolutePath());

	}

	public static void generateModel() throws Exception {
		// 创建model目录
		File modelDir = new File(
				valueProperties.getProperty("basePath") + "/" + valueProperties.getProperty("modelPath"));
		if (!modelDir.exists()) {
			modelDir.mkdir();
		}
		// 创建model
		File model = new File(modelDir.getPath() + "/" + valueProperties.getProperty("name") + ".java");
		if (!model.exists()) {
			model.createNewFile();
		}

		StringBuilder builder = new StringBuilder();
		builder.append("package " + valueProperties.getProperty("basePackageName") + "."
				+ valueProperties.getProperty("modelPath") + ";");

		connectDB();
		getCode();

		// 替换缺省代码中的参数
		String defaultCode = writeValue(modelTemplate);

		// 写入缺省代码
		write(defaultCode, model);
		System.out.println("创建model成功：" + model.getAbsolutePath());
	}

	// 连接数据库
	public static void connectDB() throws Exception {
		// 注册 JDBC 驱动
		Class.forName(jdbcProperties.getProperty("drvierName"));
		// 打开链接
		LOGGER.info("连接数据库...");
		dbConnection = DriverManager.getConnection(jdbcProperties.getProperty("url"),
				jdbcProperties.getProperty("username"), jdbcProperties.getProperty("password"));
		statement = dbConnection.createStatement();
	}

	// 构造column,getter,setter
	public static void getCode() throws SQLException {
		StringBuilder column = new StringBuilder();
		StringBuilder getset = new StringBuilder();
		StringBuilder imports = new StringBuilder();

		ResultSet rs = null;
		try {
			rs = statement.executeQuery(getSql());
			while (rs.next()) {
				String name = rs.getObject("columnName").toString();
				name = toCamelCase(name);
				String Name = name.substring(0, 1).toUpperCase() + name.substring(1);
				String comment = rs.getObject("columnComment").toString();
				String jdbcType = rs.getObject("jdbcType").toString();
				String javaType = toJavaType(jdbcType);

				// column
				column.append("@ApiModelProperty(value = \"" + comment + "\")\r\n\t");
				column.append("private " + javaType + " " + name + ";\r\n\r\n\t");
				// setter
				getset.append("public void set" + Name + "(" + javaType + " " + name + ")");
				getset.append("{this." + name + "=" + name + ";}\r\n");

				// getter
				getset.append("\tpublic " + javaType + " get" + Name + "()");
				getset.append("{return " + name + ";}\r\n\r\n\t");

				// imports
				switch (javaType) {
				case "LocalDateTime":
					if (!imports.toString().contains("import java.time.LocalDateTime;")) {
						imports.append("import java.time.LocalDateTime;\r\n");
					}
					break;
				case "LocalDate":
					if (!imports.toString().contains("import java.time.LocalDate;")) {
						imports.append("import java.time.LocalDate;\r\n");
					}
					break;
				default:
					break;
				}

			}
			
			valueProperties.setProperty("imports", imports.toString());
			valueProperties.setProperty("column", column.toString());
			valueProperties.setProperty("getset", getset.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

	// 根据数据库类型，选择查询字段类型
	public static String getSql() {
		String sql = "";
		switch (jdbcProperties.getProperty("dbType")) {
		case "mysql":
			sql = jdbcProperties.getProperty("mysqlSql");
			break;
		case "oracle":
			sql = jdbcProperties.getProperty("oracleSql");
			break;
		default:
			sql = jdbcProperties.getProperty("mysqlSql");
			break;
		}
		sql = sql.replace("[tableName]", jdbcProperties.getProperty("tableName"));
		return sql;
	}

	// jdbc类型转java类型，默认转String
	public static String toJavaType(String jdbcType) {
		jdbcType = jdbcType.toLowerCase();
		/*if (jdbcType.startsWith("bigint")) {
			return "Long";
		}
		if (jdbcType.contains("datetime")) {
			return "LocalDateTime";
		}
		if (jdbcType.contains("date")) {
			return "LocalDate";
		}*/
		return "String";
	}

	// 下划线转驼峰
	public static String toCamelCase(String value) {
		value = value.toLowerCase();
		StringBuilder builder = new StringBuilder();
		String[] strings = value.split("_");
		builder.append(strings[0]);
		for (int i = 1; i < strings.length; i++) {
			builder.append(strings[i].substring(0, 1).toUpperCase() + strings[i].substring(1));
		}
		return builder.toString();
	}
	
	private static void write(String content, File file) throws IOException {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(content);
			writer.flush();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

}
