<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<% 
  String path =request.getContextPath();
%>
<html>
<head>
	<meta charset="UTF-8">
	<title>视频管理</title>
	<jsp:include page="/common.jsp"></jsp:include>
    <script type="text/javascript" src="<c:url value='/resources/js/mygrid.js' />"></script>	
    <script type="text/javascript" src="<c:url value='/resources/js/common.js' />"></script>
    <script type="text/javascript" src="<%=path %>/resources/easyui/extends/easyui-dateformatter.js"></script>
    <style type="text/css">
     .mytable tr{
       border:1px solid #ccc;
     }
     .grayColor{
        background:#F4F4F4;
     }
    </style>
</head>
<body>

	<div class="easyui-layout" fit="true">
		
		<!-- 列表数据定义的存放表格 -->
		<table id="list_data"  title="任务列表"  fit="true" data-options="toolbar:'#tb'">
		</table>
		
		<div id="tb" style="padding:5px;height:auto">
		    <div>
				视频名称:<input class="easyui-text" style="width: 100px"
						id="videoName" name="videoName" > 		
				来源名称:
				<select id="sourceName" name="sourceName" style="width: 80px" class="easyui-combobox">
				   <option value="">请选择</option>
				   <option value="tgbus">电玩巴士</option>
				</select>
				抓取日期:<input type="text" id="createTime" name="createTime" class="easyui-datebox" style="width:200px">
				<a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-search"
					onclick="javascript:doSearch()">查询</a>
				<a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-search"
					onclick="javascript:clearSearch()">清除</a>
			</div>
		</div>
		
	  <div id="dlg_detail" data-options="title:'详情页'" class="easyui-dialog" style="width:800px;height:650px;padding:10px 10px;"
		 closed="true" modal="true" buttons="#dlg-buttons">
		     <form id="editform" method="post">
		               <input type="hidden" name="id"/>
				       <table class="mytable" style="border:solid 1px #ccc;">
					       <tr><td class="alignright grayColor">视频名称:</td><td><input class="easyui-validatebox" name="videoName"/></td></tr>
					       <tr><td class="alignright grayColor">视频url</td><td><input class="easyui-validatebox" name="videoUrl"/></td></tr>
					       <tr><td class="alignright grayColor">视频发布日期</td><td><input class="easyui-validatebox" name="updateDate"/></td></tr>
					      <!--  <tr><td class="alignright grayColor">抓取时间</td><td><input class="easyui-validatebox" name="createTime"/></td></tr>
					       <tr><td class="alignright grayColor">更新时间</td><td><input class="easyui-validatebox" name="lastUpdateTime"/></td></tr> -->
				      </table>
			   </form>
			    
			    <div class="pictureInfo" style="width:auto;height:270px;margin-top:10px;">
			       <table id="pictureDatagrid" fit=true data-options="toolbar:'#pictureDatagridtb'"></table>
			       <div id="pictureDatagridtb">
				 	<a id="pictureDel" href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" 
	    				onclick="javascript:common.ajaxBatchDelete('pictureDatagrid','<%=path %>/picture/del')">删除</a>
			       </div>
			    </div>
		     
	          <div id="dlg-buttons" >
					<a href="#" class="easyui-linkbutton" iconCls="icon-save" plain="false" 
						id="videoSave" onclick="javascript:common.doSave('dlg_detail','editform','<%=path %>/video/save','list_data')">保存</a>
				   <a href="#" class="easyui-linkbutton" iconCls="icon-no" plain="false" 
						onclick="javascript:$('#dlg_detail').dialog('close')">取消</a>
		    </div>
	    </div>
	</div>
</body>
<script type="text/javascript">
var path = '<%=path%>';
//页面入口处
$(function(){
	 var options = {
	            columns:[[
					{field:'id',title:'id',align:'center',width:60,hidden:true},
					{field:'videoName',title:'视频名称',align:'center',width:120,sortable:true,
						formatter:function(value,row,index){
							return '<a href="javascript:showDetail('+index+')">'+value+'</a>';
						}
					},
					{field:'videoUrl',title:'视频url',align:'center',width:400,sortable:true},
					{field:'sourceName',title:'来源',align:'center',width:80,sortable:true,formatter:function(value,rec){
					  	if(value=='tgbus'){
							return "电玩巴士";
					  	}
					  	else if(value=='aipai'){
							return "爱拍";
						}
					}
					},
					{field:'updateDate',title:'视频发布日期',align:'center',width:100,sortable:true,formatter:function(value,rec){
						return formatDate(value,'yyyy-MM-dd');
					}},
					{field:'createTime',title:'抓取时间',align:'center',width:150,sortable:true,
						formatter:function(value,rec){
							return formatDate(value);
					    }
					},
					{field:'lastUpdateTime',title:'更新时间',align:'center',width:150,sortable:true,
						formatter:function(value,rec){
							return formatDate(value);
					    }
					},
					/* {field:'operator',title:'操作',align:'left',align:'center',width:100,sortable:true,formatter:function(value,rec){
						if(rec.status=="-1"){
						    	 return "<a href=\"#\">重新下载</a>";
						     }else{
						    	 return "--";
						     }
					}} */
			 	]],
			 	url:path+'/video/list',
			 	pageSize:100,
			 	fitColumn:true
		}; 
		$('#list_data').mygrid(options);
	    //回车键事件
        common.enterEvent('doSearch');
        $("#dlg_detail").dialog({'onClose':function(){$("#list_data").datagrid('unselectAll');}});
});

$.fn.datebox.defaults.formatter = function(date){
		var y = date.getFullYear();
		var m = date.getMonth()+1;
		var d = date.getDate();
		//return m+'/'+d+'/'+y;
		return y+"-"+m+"-"+d;
}
	//查询
	function doSearch(){
        var videoName = $("#videoName").val();
	    var sourceName = $("#sourceName").combobox('getValue');
	    var createTime = $("#createTime").datebox('getValue');
	    createTime = createTime?createTime+" 00:00:00":createTime;
	   	var query = {
   			'videoName':videoName,
   			'sourceName':sourceName,
   			'createTime':createTime
	   	};
	   
	   $('#list_data').datagrid('load',query);
	} 
     
	function refresh(){
		clearSearch();
		doSearch();
	}
	
	function clearSearch(){
		$("#nameQuery").val("");
		$("#sourceName").combobox('clear');
		$("#createTime").datebox('clear');
	}
	
	function showDetail(index){
		$("#dlg_detail").dialog('open');
		$('#list_data').datagrid('selectRow', index);
		var row = $('#list_data').datagrid("getSelected");
		if(row){
	    	$("#editform").form("load",row);
	    	initPictureDatagrid(row.id);
		}
		
	    
	}
	
	function initPictureDatagrid(resourceId){
		 var options = {
		            columns:[[
						{field:'id',title:'id',align:'center',width:60,hidden:true},
						{field:'pictureType',title:'图片类型',align:'center',width:120,sortable:true,formatter:function(value,rec){
							  if(value=='snapshot')
								  return "截图";
							  else
								  return "图标";
						}},
						{field:'serverUrl',title:'预览',align:'center',width:100,sortable:true,formatter:function(value,rec){
							             return "<img border=0 width=60 height=80 src=http://${serverPath}/"+value+" />";
						}},
						{field:'status',title:'来源',align:'center',width:80,sortable:true,formatter:function(value,rec){
							  if(value==-1)
								  return "下载失败";
							  else if(value==1)
								  return "下载成功";
						}
						},
						{field:'createTime',title:'抓取时间',align:'center',width:150,sortable:true,
							formatter:function(value,rec){
								return formatDate(value);
						    }
						},
						{field:'operator',title:'操作',align:'left',align:'center',width:100,sortable:true,formatter:function(value,rec){
							     if(rec.status=="-1"){
							    	 return "<a href=\"#\">重新下载</a>";
							     }else{
							    	 return "--";
							     }
						}}
				 	]],
				 	url:path+'/picture/list?resourceId='+resourceId,
				 	pageSize:20,
				 	fitColumn:true
			}; 
			$('#pictureDatagrid').mygrid(options);
	}

	
	</script>
	
	
</html>