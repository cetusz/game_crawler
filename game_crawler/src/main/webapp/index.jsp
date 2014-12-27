<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
	<head>
		<meta charset="UTF-8">
		<title>椰子游戏爬虫管理系统</title>
		<link rel="stylesheet" type="text/css" href="<%=path %>/resources/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=path %>/resources/easyui/themes/icon.css">
		<script type="text/javascript" src="<%=path %>/resources/easyui/jquery-1.8.3.min.js"></script>
		<script type="text/javascript" src="<%=path %>/resources/easyui/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="<%=path %>/resources/easyui/locale/easyui-lang-zh_CN.js"></script>
		<link rel="stylesheet" type="text/css" href="<%=path %>/resources/css/default.css">
		<script type="text/javascript" src='<%=path %>/resources/js/outlook.js'> </script>
		<script type="text/javascript">
		    var _menus = {
				basic : [
				    {
				    	"menuid": 1,
				        "menuname": "抓源管理",
						"icon": "icon-sys",
				        "menus": [
				             {
				                "menuid": 11,
				                "menuname": "任务配置",
								"icon": "icon-nav",
								"url": "manager/task/listpage"
				            },
							{
				                "menuid": 12,
				                "menuname": "apk管理",
								"icon": "icon-nav",
								"url": "apk/pageIndex"
				            },
				            {
				                "menuid": 13,
				                "menuname": "视频管理",
								"icon": "icon-nav",
								"url": "video/pageIndex"
				            }
				        ]
				    }
				]
			};
		    $(window).bind('beforeunload', function(){
		    	if(event.clientX>document.body.clientWidth && event.clientY < 0 || event.altKey){ 
		    	  return "你正在关闭浏览器，确定关闭?"; 
		    	}else{ 
		    	  return "你正在刷新页面,确定刷新?"; 
		    	} 
		    });
	
		</script>
		
		<style>
			#css3menu li {
				float: left;
				list-style-type: none;
			}
			
			#css3menu li a {
				color: #fff;
				padding-right: 20px;
			}
			
			#css3menu li a.active {
				color: yellow;
			}
		</style>
	</head>
	<body class="easyui-layout" style="overflow-y: hidden" scroll="no">
	
		<div region="west" hide="true" split="true" title="导航菜单"
			style="width:180px;" id="west">
			<div id='wnav' class="easyui-accordion" fit="true" border="false">
			</div>
	
		</div>
		
		<div region="south" border="true" style="height:50px;padding:10px;text-align:center">
			&copy;深圳市同洲电子股份有限公司版权所有
		</div>
		
		<div region="north" split="true" border="false"
			style="overflow: hidden; height: 50px;
	        background: url(<%=path%>/resources/images/layout-browser-hd-bg.gif) #7f99be repeat-x center 50%;
	        line-height: 40px;color: #fff; font-family: Verdana, 微软雅黑,黑体">
			<span style="float:right; padding-right:20px;" class="head">
			</span>
			<span style="padding-left:10px; font-size: 16px; float:left;"><img src="<%=path %>/resources/images/spider.png" width="40" height="40" align="absmiddle" />椰子游戏爬虫系统</span>
			<span style=" font-size: 16px; font-family:'华文楷体'; float:center;"> </span>
	
			<ul id="css3menu"
				style="padding:0px; margin:0px;list-type:none; float:left; margin-left:40px;">
				<li><a class="active" name="basic" href="javascript:;" title=""></a>
				</li>
			</ul>
		</div>
		
		<div region="center" id="mainPanle"
			style="background: #eee; overflow-y:hidden">
			<div id="tabs" class="easyui-tabs" fit="true" border="false">
				<div title="首页" style="padding:20px;overflow:hidden;" id="home">
					<h3>欢迎使用游戏爬虫系统</h3>
				</div>
			</div>
		</div>
	
		<div id="password" class="easyui-window" title="修改密码"
			data-options="modal:true,closed:true,iconCls:'icon-save',tools:'#tt'"
			style="width:300px;height:200px;">
			<form id="ff" method="post" action="../manage/system!login.do">
				<table align="center">
					<tr>
						<td>旧密码:</td>
						<td><input class="easyui-validatebox" type="text"
							id="oldPassword" name="oldPassword" data-options="required:true"></input>
						</td>
					</tr>
					<tr>
						<td>新密码:</td>
						<td><input class="easyui-validatebox" type="text"
							id="newPassword" name="newPassword" data-options="required:true"></input>
						</td>
					</tr>
				</table>
				<input type="hidden" name="userId" id="userId"
					value='<s:property value="#session.user.userId"/>' />
			</form>
			<div style="text-align:center;padding:5px">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="submitForm()">修改</a> <a href="javascript:void(0)"
					class="easyui-linkbutton" onclick="clearForm()">清除</a>
			</div>
		</div>
		<script>
		</script>
	</body>
</html>
