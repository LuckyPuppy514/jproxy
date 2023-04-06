package ${package.Controller};

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

import ${package.Entity}.${entity};
import ${package.Parent}.model.request.PageRequest;
import ${package.Parent}.model.response.PageResponse;
import ${package.Service}.${table.serviceName};

<#if springdoc>
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
</#if>
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * <#if table.comment?? && table.comment != "">${table.comment}<#else>${table.entityName}</#if>
 * </p>
 *
 * @author ${author}
 * @date ${date}
 */
<#if springdoc>
@Tag(name = "<#if table.comment?? && table.comment != "">${table.comment}<#else>${table.entityName}</#if>")
</#if>
@RequestMapping("/api<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequiredArgsConstructor
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>

	private final ${table.serviceName} ${table.entityPath}Service;

	@Operation(summary = "保存")
	@PostMapping("/save")
	public ResponseEntity<Void> save(@ParameterObject ${entity} request) {
		${table.entityPath}Service.save(request);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "查询")
	@GetMapping("/query")
	public ResponseEntity<PageResponse<${entity}>> query(@ParameterObject PageRequest request) {
		return ResponseEntity.ok(new PageResponse<>(${table.entityPath}Service.page(request.mybatisPlusPage())));
	}

	@Operation(summary = "更新")
	@PostMapping("/update")
	public ResponseEntity<Void> update(@ParameterObject ${entity} request) {
		${table.entityPath}Service.saveOrUpdate(request);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "删除")
	@GetMapping("/remove")
	public ResponseEntity<Void> remove(@ParameterObject ${entity} request) {
		${table.entityPath}Service.removeById(request);
		return ResponseEntity.ok().build();
	}
}
</#if>