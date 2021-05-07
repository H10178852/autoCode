package com.huwb.autocode.controller;

import com.huwb.autocode.common.domain.AjaxResult;
import com.huwb.autocode.common.domain.ResuTree;
import com.huwb.autocode.common.domain.ResultTable;
import com.huwb.autocode.common.domain.base.BaseController;
import com.huwb.autocode.model.autocode.*;
import com.huwb.autocode.service.GeneratorService;
import com.huwb.autocode.util.AutoCode.AutoCodeUtil;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;


@Controller
@RequestMapping("/autoCode")
public class AutoCodeController extends BaseController {

	private String prefix = "autoCode";

	@Autowired
	private GeneratorService generatorService;


	/**
	 * 查询所有表(树结构)
	 * @return
	 */
	@GetMapping("/selectTables")
	@ResponseBody
	public ResuTree selectTables() {
		List<TsysTables> list = generatorService.queryList(null);
		List<TsysTablesVo> TreeList = new ArrayList<TsysTablesVo>();
		for (int i = 0; i < list.size(); i++) {
			TsysTablesVo tablesVo = new TsysTablesVo(i + 1, -1, list.get(i).getTableName(), list.get(i).getEngine(),
					list.get(i).getTableComment(), list.get(i).getTableModel(), list.get(i).getCreateTime(),
					list.get(i).getTableName() + " > " + list.get(i).getTableComment());
			TreeList.add(tablesVo);
		}

		TsysTablesVo tables = new TsysTablesVo();
		tables.setTableModel("all");
		tables.setTableAndName("所有表");
		tables.setParentId(0);
		tables.setId(-1);
		TreeList.add(tables);
		return dataTree(TreeList);
	}

	/**
	 * 查询单个表信息
	 * @param tableName
	 * @return
	 */
	@GetMapping("/queryTableInfo")
	@ResponseBody
	public ResultTable queryTableInfo(String tableName) {
		List<BeanColumn> list = generatorService.queryColumns2(tableName);
		return pageTable(list, list.size());
	}

	/**
	 * 根据路径生成代码 (不建议使用，万一覆盖后果自负!!  建议使用下面方法，生成zip包，代码手动拷贝到项目里，代码无价，谨慎。)
	 * @param autoConfigModel
	 * @return
	 */
//	@PostMapping("/createAuto")
	@ResponseBody
	public AjaxResult createAuto(AutoConfigModel autoConfigModel) {
		// 根据表名查询表字段集合
		List<BeanColumn> list = generatorService.queryColumns2(autoConfigModel.getTableName());
		// 初始化表信息
		TableInfo tableInfo = new TableInfo(autoConfigModel.getTableName(), list, autoConfigModel.getTableComment());

		AutoCodeUtil.autoCodeOneModel(tableInfo, autoConfigModel);
		return AjaxResult.success();
	}


	/**
	 * 生成代码(ZIP包)(建议使用)
	 * @param tableName 表名
	 * @throws IOException
	 */
	@GetMapping("/createAutoZip")
	@ResponseBody
	public void createAutoZip(String tableName,HttpServletResponse response) throws IOException {
		List<TsysTables> tables = generatorService.queryList(tableName);

		if(tables.size() == 0 || tables.size() > 1){
			return;
		}

		AutoConfigModel autoConfigModel = new AutoConfigModel();
		autoConfigModel.setTableName(tables.get(0).getTableName());
		autoConfigModel.setTableComment(tables.get(0).getTableComment());
		autoConfigModel.setPid("0");

		byte[] b;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);
		// 根据表名查询表字段集合
		List<BeanColumn> list = generatorService.queryColumns2(autoConfigModel.getTableName());
		// 初始化表信息
		TableInfo tableInfo = new TableInfo(autoConfigModel.getTableName(), list, autoConfigModel.getTableComment());
		// 自动生成
		AutoCodeUtil.autoCodeOneModel(tableInfo, autoConfigModel, zip);
		IOUtils.closeQuietly(zip);
		b = outputStream.toByteArray();
		response.reset();
		response.setHeader("Content-Disposition", "attachment; filename=\"autoCode.zip\"");
		response.addHeader("Content-Length", "" + b.length);
		response.setContentType("application/octet-stream; charset=UTF-8");
		IOUtils.write(b, response.getOutputStream());
	}


	@GetMapping("/viewAuto")
	public String viewAuto(AutoConfigModel autoConfigModel, ModelMap model) {
		List<BeanColumn> list = generatorService.queryColumns2(autoConfigModel.getTableName());
		TableInfo tableInfo = new TableInfo(autoConfigModel.getTableName(), list, autoConfigModel.getTableComment());
		Map<String, String> map = AutoCodeUtil.viewAuto(tableInfo,autoConfigModel);
		model.put("viewmap", map);
		return prefix + "/view";
	}

}