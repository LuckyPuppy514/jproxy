package [basePackageName].[servicePath].[iservicePath];

import com.baomidou.mybatisplus.core.metadata.IPage;

import [basePackageName].param.TestPostParam;
import [basePackageName].resp.TestPostResp;


/**
* @ClassName: [interfacePrefix][name]Service
* @Description: [comment]service
* @author [author]
* @date [date]
*
*/
public interface [interfacePrefix][name]Service {
	String selectTest1(String param);
	IPage<TestPostResp> selectTest2(TestPostParam param);
}