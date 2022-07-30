package [basePackageName].controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import [basePackageName].[servicePath].[iservicePath].[interfacePrefix][name]Service;

import io.swagger.annotations.Api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
* @ClassName: [name]Controller
* @Description: [comment]Controller
* @author [author]
* @date [date]
*
*/
@RestController
@RequestMapping("/[lname]")
@Api(tags = "[comment]")
public class [name]Controller {
	private static final Logger LOGGER = LoggerFactory.getLogger([name]Controller.class);
	
	@Autowired
	private [interfacePrefix][name]Service [lname]Service;
	
}