package com.huwb.autocode.service;

import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huwb.autocode.mapper.GeneratorMapper;
import com.huwb.autocode.model.autocode.BeanColumn;
import com.huwb.autocode.model.autocode.Tablepar;
import com.huwb.autocode.model.autocode.TsysTables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GeneratorService {
	@Autowired
	private GeneratorMapper generatorMapper;
	/**
	 * 分页查询
	 * @return
	 */
	 public PageInfo<TsysTables> list(Tablepar tablepar, String searchText){
		 	PageHelper.startPage(tablepar.getPage(), tablepar.getLimit());
		 	List<TsysTables> list=  generatorMapper.queryList(searchText);
		    PageInfo<TsysTables> pageInfo = new PageInfo<TsysTables>(list);
		    return pageInfo;
	 }
	 

	 /**
	  * 查询具体某表信息
	  * @param tableName
	  * @return
	  */
	 public List<TsysTables> queryList(String tableName){
		return generatorMapper.queryList(tableName);
	 }
	
	/**
	  * 查询表详情
	  * @param tableName
	  * @return
	  */
	public List<BeanColumn> queryColumns2(String tableName){
		System.out.println("queryColumns2>>>"+JSONUtil.toJsonPrettyStr(generatorMapper.queryColumns3(tableName)));
		return generatorMapper.queryColumns2(tableName);
	 }
	
}
