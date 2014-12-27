<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<% 
  String path =request.getContextPath();
%>
<html>
<head>
	<meta charset="UTF-8">
	<title>apk管理</title>
	<jsp:include page="/common.jsp"></jsp:include>
    <script type="text/javascript" src="<c:url value='/resources/js/mygrid.js' />"></script>	
    <script type="text/javascript" src="<c:url value='/resources/js/common.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/js/JSON.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/js/ajaxloading.js' />"></script>
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
		<div region="center">
		<!-- 列表数据定义的存放表格 -->
		<table id="list_data"  title="任务列表"  fit="true" data-options="toolbar:'#tb'">
		</table>
		</div>
		<div id="tb" style="padding:5px;height:auto">
		    <div>
				apk名称:<input class="easyui-text" style="width: 100px"
						id="nameQuery" name="nameQuery" > 		
				来源名称:
				<select id="sourceName" name="sourceName" style="width: 80px" class="easyui-combobox">
				   <option value="">请选择</option>
				   <option value="shafa">沙发</option>
				   <option value="dangbei">当贝</option>
				   <option value="7po">奇珀</option>
				</select>
				日期:<input type="text" id="createTime" name="createTime" class="easyui-datebox" style="width:200px">
				<a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-search"
					onclick="javascript:doSearch()">查询</a>
				<a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-search"
					onclick="javascript:clearSearch()">清除</a>
				<a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-download"
					onclick="javascript:zip()">下载</a>
			</div>
		</div>
		
	  <div id="dlg_detail" data-options="title:'详情页'" class="easyui-dialog" style="width:800px;height:650px;padding:10px 10px;"
		 closed="true" modal="true" buttons="#dlg-buttons">
		     <form id="editform" method="post">
		               <input type="hidden" name="id"/>
				       <table class="mytable" style="border:solid 1px #ccc;">
					       <tr><td class="alignright grayColor">apk名称:</td><td><input class="easyui-validatebox" name="name"/></td></tr>
					       <tr><td class="alignright grayColor">操作方式:</td><td><input class="easyui-validatebox" name="operationWay"/></td></tr>
					       <tr><td class="alignright grayColor">类型:</td><td><input class="easyui-validatebox" name="type"/></td></tr>
					       <tr><td class="alignright grayColor">来源:</td><td><input class="easyui-validatebox" name="sourceName"/></td></tr>
					       <tr><td class="alignright grayColor">简介</td><td><textarea  class="easyui-validatebox" name="introduce"></textarea></td></tr>
					       <tr><td class="alignright grayColor">大小</td><td><input class="easyui-validatebox" name="size"/></td></tr>
					       <tr><td class="alignright grayColor">下载</td><td><input class="easyui-validatebox" name="downloadTimes"/></td></tr>
					       <tr><td class="alignright grayColor">语言</td><td><input class="easyui-validatebox" name="language"/></td></tr>
					       <tr><td class="alignright grayColor">版本</td><td><input class="easyui-validatebox" name="version"/></td></tr>
					       <tr><td class="alignright grayColor">更新日期</td><td><input class="easyui-validatebox" name="updateDate"/></td></tr>
					       <tr><td class="alignright grayColor">安卓系统</td><td><input class="easyui-validatebox" name="androidSystemVersion"/></td></tr>
				      </table>
			   </form>
			    
			    <div class="pictureInfo" style="width:auto;height:270px;margin-top:10px;">
			       <table id="pictureDatagrid" fit=true data-options="toolbar:'#pictureDatagridtb'"></table>
			       <div id="pictureDatagridtb">
			         <a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" 
						id="deleteButton" onclick="javascript:">删除</a>
			       </div>
			    </div>
		     
	          <div id="dlg-buttons" >
					<a href="#" class="easyui-linkbutton" iconCls="icon-save" plain="false" 
						id="assetSave" onclick="javascript:saveBaseInfo()">保存</a>
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
	$(".clo_x").on('click',function(){$("#zip_cart_area").hide();});
	$("#zip_tool_int").on('click',function(){$("#zip_cart_area").show();});
	 var options = {
	            columns:[[
					{field:'id',title:'id',align:'center',width:60,hidden:true},
					{field:'name',title:'apk名称',align:'center',width:120,sortable:true,
						formatter:function(value,rec)
						{
							return "<a href=\"#\">"+value+"</a>";
						}
					},
					{field:'operationWay',title:'操控方式',align:'center',width:100,sortable:true},
					{field:'sourceName',title:'来源',align:'center',width:80,sortable:true,formatter:function(value,rec){
						  if(value=='shafa')
							  return "沙发管家";
						  else if(value=='7po')
							  return "奇珀网";
							  else if(value=="dangbei")
								  return "当贝网";
					}
					},
					{field:'updateDate',title:'apk发布日期',align:'center',width:100,sortable:true,formatter:function(value,rec){
						return formatDate(value,'yyyy-MM-dd');
					}},
					{field:'status',title:'状态',align:'center',width:100,formatter:function(value,rec){
						 if(value==-1){
							 return "下载失败";
						 }else if(value==1){
							 return "下载成功"
						 }else if(value==0){
							 return "初始化";
						 }
					}},
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
			 	url:path+'/apk/list',
			 	pageSize:100,
			 	fitColumn:true,
			    onClickCell: function(index,field,value){
			    	if(field=='name')
				    showDetail(index);
				}
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
        var name = $("#nameQuery").val();
	    var sourceName = $("#sourceName").combobox('getValue');
	    var createTime = $("#createTime").datebox('getValue');
	    createTime = createTime?createTime+" 00:00:00":createTime;
	   	var query = {
   			'name':name,
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
		var data = $('#list_data').datagrid('getData');
		var row = data.rows[index];
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
	
	function saveBaseInfo(){
		  $('#editform').form('submit',{
			    url:'<%=path%>/apk/save',
		        onSubmit: function(){
		        },
		        success: function(data){
		        	common.fadeMessager("list_data",data.message);
		        	$("#dlg_detail").dialog('close');
		        }
		 });
	}
	
	function zip(){
		var rows = $("#list_data").datagrid('getSelections');
		if(rows.length==0){
			common.fadeMessageShow("请选择记录");
			return;
		}
		var objectArray = [];
		for(var i=0,len=rows.length;i<len;i++){
			var obj = {};
			obj.sourceName=rows[i].sourceName;
			obj.apkName=rows[i].name;
			obj.sourceId=rows[i].sourceId;
			objectArray.push(obj);
		}
		window.location.href = '<%=path%>/apk/zip?objStr='+JSON.stringify(objectArray);
	}
	
        
	</script>
	
	
</html>