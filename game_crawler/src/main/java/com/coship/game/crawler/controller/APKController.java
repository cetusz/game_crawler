package com.coship.game.crawler.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.net.ftp.FTPClient;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.coship.game.crawler.domain.APKEntity;
import com.coship.game.crawler.domain.ZipVo;
import com.coship.game.crawler.service.IAPKEntityService;
import com.coship.game.crawler.utils.ConfigFactory;
import com.coship.game.crawler.utils.Constants;
import com.coship.game.crawler.utils.FtpUtil;
import com.coship.game.crawler.utils.ZipUtil;
import com.my.mybatis.support.Page;

@Controller
@RequestMapping("apk")
public class APKController extends BaseController{

	@Autowired IAPKEntityService apkService;
	
	@RequestMapping("pageIndex")
	public ModelAndView pageIndex(){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("apk/list");
		mv.addObject("serverPath", ConfigFactory.getString(Constants.FTPHOST));
		return mv;
	}
	@RequestMapping("list")
	public ResponseEntity<Map<String,Object>> list(APKEntity entity,int page,int rows){
		Page<APKEntity> pager = apkService.selectPageList(entity,null,page,rows);
		return createPageResponseEntity(pager);
	}
	
	@RequestMapping("save")
	public @ResponseBody Map<String,Object> save(APKEntity entity){
		apkService.saveOrUpdate(entity);
		return result(true,"保存成功!");
	}
	
	@RequestMapping("del")
	public  Map<String,Object> del(List<Long> ids){
		apkService.deleteMulti(ids);
		return result(true,"保存成功!");
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("zip")
	public void zip(HttpServletResponse response,String objStr){
		String zipFileName = System.currentTimeMillis()+"";
		String downloadTmpDir = ConfigFactory.getString(Constants.FTPDOWNLOADTMPDIR);
		String ftpBasePath = ConfigFactory.getString(Constants.FTPPATH)+"/apk";
		String ftpHost = ConfigFactory.getString(Constants.FTPHOST);
		int ftpPort = ConfigFactory.getInt(Constants.FTPPORT);
		String user = ConfigFactory.getString(Constants.FTPUSER);
		String pwd = ConfigFactory.getString(Constants.FTPPWD);
		FTPClient client = FtpUtil.connectFTPServer(ftpHost, ftpPort, user, pwd);
		List<ZipVo> list = new ArrayList<ZipVo>();
		ObjectMapper om = new ObjectMapper();
		om.configure(
		     DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		DateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		om.setDateFormat(dataFormat);
        try {  
            JsonNode node=om.readTree(objStr);  
            list = (List<ZipVo>) om.readValue(node.toString(), new TypeReference<List<ZipVo>>(){});  
            for(ZipVo vo:list){
            	String remoteDirectory = ftpBasePath+"/"+vo.getSourceName()+"/"+vo.getApkName();
            	remoteDirectory = new String(remoteDirectory.getBytes("utf-8"),"iso-8859-1");
            	FtpUtil.downLoadDirectory(client,downloadTmpDir+"/"+zipFileName+"/", remoteDirectory);
            }
            response.setContentType("APPLICATION/OCTET-STREAM");  
            response.setHeader("Content-Disposition","attachment; filename="+zipFileName+".zip");
            ZipUtil.compress(downloadTmpDir+"/"+zipFileName,response.getOutputStream());
           
        } catch (JsonParseException e) {  
        	
        } catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
