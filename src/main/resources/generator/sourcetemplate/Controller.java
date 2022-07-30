package [basePackageName].controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.DynamicParameter;
import com.github.xiaoymin.knife4j.annotations.DynamicResponseParameters;
import [basePackageName].resp.ResVo;
import [basePackageName].param.TestPostParam;
import [basePackageName].resp.Pagination;
import [basePackageName].resp.TestPostResp;
import [basePackageName].[servicePath].[iservicePath].[interfacePrefix][name]Service;
import [basePackageName].util.JacksonUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
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
	
	@ApiOperation("get接口")
	//请求参数
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "认证信息", paramType = "header", dataType = "String", required = true),
		@ApiImplicitParam(name = "param", value = "参数", paramType = "query", dataType = "String", required = false)
	})
	//响应码
	@ApiResponses({
		@ApiResponse(code = 200, message = "success"),
		@ApiResponse(code = 500, message = "error")
	})
	//响应参数
	@ApiOperationSupport(
		    responses = @DynamicResponseParameters(properties = {
		        @DynamicParameter(value = "状态码（200：成功，其他：失败）",name = "code"),
		        @DynamicParameter(value = "数据",name = "data"),
		        @DynamicParameter(value = "状态信息",name = "message")
		    })
		)
	@GetMapping("/get")
	public ResVo<String> get(String param, HttpServletRequest request) {
		LOGGER.info("请求[comment]get接口，入参：{}", param);
		try {
			String token = request.getHeader("token");
			//校验
			if (StringUtils.isBlank(token)) {
				return ResVo.fail("token不能为空");
			}

			String result = [lname]Service.selectTest1(param);
			return ResVo.success(result);
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return ResVo.error(e.getMessage());
		}
	}
	
	@ApiOperation("post接口")
	@PostMapping("/post")
	public ResVo<Pagination<TestPostResp>> post(@Valid TestPostParam param, BindingResult bindingResult) {
		try {
			LOGGER.info("请求测试post接口，入参：{}", JacksonUtil.toJSONString(param));
			
			if (bindingResult.hasErrors()){
	            return ResVo.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
	        }
			
			Pagination<TestPostResp> result = new Pagination<>();
			
			IPage<TestPostResp> page = [lname]Service.selectTest2(param);
			
			result.setPageIndex(page.getCurrent());
			result.setPageSize(page.getSize());
			result.setTotal(page.getTotal());
			result.setPageCount(page.getPages());
			result.setList(page.getRecords());
			return ResVo.success(result);
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return ResVo.error(e.getMessage());
		}
	}
}