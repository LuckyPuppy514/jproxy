package [basePackageName].[modelPath];

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

[imports]
		
/**
* @ClassName: [name]
* @Description: [comment]实体
* @author [author]
* @date [date]
*
*/
@ApiModel(value = "[comment]")
public class [name] implements Serializable {
	private static final long serialVersionUID = 1L;
	
	[column]
	[getset]
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}