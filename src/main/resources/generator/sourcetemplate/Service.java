package [basePackageName].[servicePath].[serviceImplPath];


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import [basePackageName].param.TestPostParam;
import [basePackageName].resp.TestPostResp;
import [basePackageName].[servicePath].[iservicePath].[interfacePrefix][name]Service;
import [basePackageName].[mapperPath].[interfacePrefix][name]Mapper;

/**
* @ClassName: [name]ServiceImpl
* @Description: [comment]serviceImpl
* @author [author]
* @date [date]
*
*/
@Service("[lname]Service")
public class [name]ServiceImpl implements [interfacePrefix][name]Service {
	
	@Autowired
	private [interfacePrefix][name]Mapper [lname]Mapper;
	
	/**
	 * @Description: description
	 * @param eid
	 * @return
	 */
	@Override
	public String selectTest1(String param) {
		return [lname]Mapper.selectTest1(param);
	}
	
	/**
	 * @Description: description
	 * @param eid
	 * @return
	 */
	@Override
	public IPage<TestPostResp> selectTest2(TestPostParam param) {
		Page<?>page = new Page<>();
		page.setCurrent(param.getPageIndex());
		page.setSize(param.getPageSize());
		return [lname]Mapper.selectTest2(page, param);
	}
}
