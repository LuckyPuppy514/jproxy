package [basePackageName].[servicePath].[serviceImplPath];


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
