package com.huwb.autocode.util.AutoCode;

import cn.hutool.core.date.DateTime;
import com.huwb.autocode.model.autocode.AutoCodeConfig;
import com.huwb.autocode.model.autocode.AutoConfigModel;
import com.huwb.autocode.model.autocode.TableInfo;
import com.huwb.autocode.util.SnowflakeIdWorker;
import com.huwb.autocode.util.StringUtils;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class AutoCodeUtil {
	public static List<String> getTemplates(){
        List<String> templates = new ArrayList<String>();

        //java代码模板
        templates.add("auto_code/model/Entity.java.vm");
        templates.add("auto_code/model/EntityExample.java.vm");
        templates.add("auto_code/mapperxml/EntityMapper.xml.vm");
        templates.add("auto_code/service/EntityService.java.vm");
        templates.add("auto_code/mapper/EntityMapper.java.vm");

        return templates;
    }
	
	

	public static void autoCodeOneModel(TableInfo tableInfo, AutoConfigModel autoConfigModel){
		AutoCodeConfig autoCodeConfig=new AutoCodeConfig();
		//设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader" );
        Velocity.init(prop);
		Map<String, Object> map = new HashMap<>();
        //数据库表数据
		map.put("tableInfo",tableInfo);
        //字段集合
        map.put("beanColumns",tableInfo.getBeanColumns());
        //配置文件
        map.put("SnowflakeIdWorker", SnowflakeIdWorker.class);
        //class类路径
        map.put("parentPack", autoCodeConfig.getConfigkey("parentPack"));
        //作者
        map.put("author", autoConfigModel.getAuthor());
        //时间
        map.put("datetime",new DateTime());
        //sql需要的权限父级pid
        map.put("pid",autoConfigModel.getPid());
        
        VelocityContext context = new VelocityContext(map);
        
        //获取模板列表
        List<String> templates = getTemplates();

        for (String template : templates) {
        	try {
        			String targetPath = autoConfigModel.getParentPath();
        			String filepath=getCoverFileName(template,tableInfo ,autoCodeConfig.getConfigkey("parentPack"),targetPath);
    		        Template tpl = Velocity.getTemplate(template, "UTF-8" );
    				File file = new File(filepath);
    				if (!file.getParentFile().exists())
    		            file.getParentFile().mkdirs();
    		        if (!file.exists())
    		            file.createNewFile();
					try (FileOutputStream outStream = new FileOutputStream(file);
						 OutputStreamWriter writer = new OutputStreamWriter(outStream, "UTF-8");
						 BufferedWriter sw = new BufferedWriter(writer)) {
						 tpl.merge(context, sw);
						 sw.flush();
						 System.out.println("成功生成Java文件:" + filepath);
					}
        	} catch (IOException e) {
                try {
					throw new Exception("渲染模板失败，表名：" +"c"+"\n"+e.getMessage());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
            }
        }
	}
	

	public static Map<String,String> viewAuto(TableInfo tableInfo,AutoConfigModel autoConfigModel){
		Map<String, String> velocityMap=new HashMap<String, String>();
		
		AutoCodeConfig autoCodeConfig=new AutoCodeConfig();
		//设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);
		Map<String, Object> map = new HashMap<>();
		//数据库表数据
		map.put("tableInfo",tableInfo);
        //字段集合
        map.put("beanColumns",tableInfo.getBeanColumns());
        //配置文件
        map.put("SnowflakeIdWorker", SnowflakeIdWorker.class);
        //class类路径
        map.put("parentPack", autoCodeConfig.getConfigkey("parentPack"));
        //作者
        map.put("author", autoConfigModel.getAuthor());
        //时间
        map.put("datetime",new DateTime());
        //sql需要的权限父级pid
        map.put("pid",autoConfigModel.getPid());
        
        VelocityContext velocityContext = new VelocityContext(map);
        //获取模板列表
        List<String> templates = getTemplates();
        for (String template : templates) {
			Template tpl = Velocity.getTemplate(template, "UTF-8" );
			StringWriter sw = new StringWriter(); 
			tpl.merge(velocityContext, sw);
			System.out.println("输出模板");
			System.out.println(sw);
			System.out.println("输出模板 end");
			velocityMap.put(template.substring(template.lastIndexOf("/")+1, template.lastIndexOf(".vm")), sw.toString());
        }
        return velocityMap;
	}

	public static void autoCodeOneModel(TableInfo tableInfo,AutoConfigModel autoConfigModel,ZipOutputStream zip){
		AutoCodeConfig autoCodeConfig=new AutoCodeConfig();
		//设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader" );
        Velocity.init(prop);
		Map<String, Object> map = new HashMap<>();
        //数据库表数据
		map.put("tableInfo",tableInfo);
        //字段集合
        map.put("beanColumns",tableInfo.getBeanColumns());
        //配置文件
        map.put("SnowflakeIdWorker", SnowflakeIdWorker.class);
        //class类路径
        map.put("parentPack", autoCodeConfig.getConfigkey("parentPack"));
        //作者
        map.put("author", AutoCodeConfig.getConfig().getString("author"));
        //时间
        map.put("datetime",new DateTime());
        //sql需要的权限父级pid
        map.put("pid",autoConfigModel.getPid());
        VelocityContext velocityContext = new VelocityContext(map);
        //获取模板列表
        List<String> templates = getTemplates();
        for (String template : templates) {
        	try {
					String filepath=getCoverFileName(template,tableInfo ,autoCodeConfig.getConfigkey("parentPack"),"");
					Template tpl = Velocity.getTemplate(template, "UTF-8" );
					StringWriter sw = new StringWriter();
					tpl.merge(velocityContext, sw);
					zip.putNextEntry(new ZipEntry(filepath));
					IOUtils.write(sw.toString(), zip, "UTF-8");
					IOUtils.closeQuietly(sw);
					zip.closeEntry();
        	} catch (IOException e) {
                try {
					throw new Exception("渲染模板失败，表名：" +"c"+"\n"+e.getMessage());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
            }
        }
	}
	
	


    public static String getCoverFileName(String template,TableInfo tableInfo, String packageName,String targetPath) {
        
    	String separator=File.separator;

        if (template.contains("Entity.java.vm")) {//model.java
            return "model" + separator + tableInfo.getJavaTableName() + ".java";
        }
        if(template.contains("EntityExample.java.vm")) {//modelExample.java
        	return "model" + separator + tableInfo.getJavaTableName() + "Example.java";
        }
        
        if (template.contains("EntityMapper.java.vm")) {//daomapper.java
            return "mapper"  + separator + tableInfo.getJavaTableName() + "Mapper.java";
        }
        if (template.contains("EntityMapper.xml.vm")) {//daomapper.xml
            return "mybatis" + separator + tableInfo.getJavaTableName() + "Mapper.xml";
        }
        
        if (template.contains("EntityService.java.vm")) {
            return "service" + separator + tableInfo.getJavaTableName() + "Service.java";
        }


        return "";
    }
}
