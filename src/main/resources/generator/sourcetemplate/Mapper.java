package [basePackageName].[mapperPath];

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import [basePackageName].param.TestPostParam;
import [basePackageName].resp.TestPostResp;


/**
* @ClassName: [interfacePrefix][name]Mapper
* @Description: [comment]mapper
* @author [author]
* @date [date]
*
*/
@DS("master")
public interface [interfacePrefix][name]Mapper {
	@Select("<script>"
		+ "select #{param} param from dual"
		+ "</script>")
	String selectTest1(@Param("param")String param);
	
	IPage<TestPostResp> selectTest2(Page<?> page, @Param("param")TestPostParam param);
}