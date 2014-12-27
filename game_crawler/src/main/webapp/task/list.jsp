<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<% 
  String path =request.getContextPath();
%>
<html>
<head>
	<meta charset="UTF-8">
	<title>系统管理-任务管理</title>
<jsp:include page="/common.jsp"></jsp:include>
    <script type="text/javascript" src="<c:url value='/resources/js/mygrid.js' />"></script>	
    <script type="text/javascript" src="<c:url value='/resources/js/common.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/js/task.js' />"></script>	
   
</head>
<body>
	<div class="easyui-layout" fit="true">
		
		<!-- 列表数据定义的存放表格 -->
		<table id="list_data"  title="任务列表"  fit="true" data-options="toolbar:'#tb'">
		</table>
		
		<div id="tb" style="padding:5px;height:auto">
		    <div style="margin-bottom:5px">
		        <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="add()">新增</a>
		        <a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="del()">删除</a>	 
		        <a href="#" class="easyui-linkbutton" iconCls="icon-refresh" plain="true" onclick="refresh()">刷新</a>
				<a href="#" class="easyui-linkbutton" iconCls="icon-start" plain="true" onclick="task.start()">启动</a>
				<a href="#" class="easyui-linkbutton" iconCls="icon-stop" plain="true" onclick="task.pause()">暂停</a>	        
			</div>
		    <div>
				任务名称:<input class="easyui-text" style="width: 100px"
						id="title" name="title" > 		
				任务类型:
				<select id="triggerType" name="triggerType" style="width: 80px"class="easyui-combobox" 
					data-options=" valueField: 'key',
								   textField: 'value',
								   method:'get'">
				</select>
				<a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true"
					onclick="javascript:doSearch()">查询</a>
				<a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true"
					onclick="javascript:clearSearch()">清除</a>
			</div>
		</div>
	</div>
	  
</body>
<script type="text/javascript">
 path = '<%=path%>';
//页面入口处
$(function(){
	//类型
	$("#triggerType").combobox('loadData',common.queryList(triggerTypeJson, true,"key","value"));
	$("#triggerType").combobox('loadData',common.queryList(triggerTypeJson, true,"key","value"));
	//页面初始化
	task.initPage();
	
	//回车键事件
    common.enterEvent('doSearch');
});

	//查询
	function doSearch(){
        var title = $("#title").val();
	    var triggerType = $("#triggerType").combobox('getValue');

	   	var query = {
   			'title':title,
   			'triggerType':triggerType
	   	};
	    $('#list_data').datagrid('unselectAll');
	    $('#list_data').datagrid('load',query);
	} 
     
	function refresh(){
		clearSearch();
		doSearch();
	}
	
	function clearSearch(){
		$("#title").val("");
		$("#triggerType").combobox('clear');
		$("#showType").combobox('clear');
		$("#status").combobox('clear');
	}
	
	function add(){
		common.addTabs("任务录入", "<%=path%>/manager/task/id/-1/page?pageType=add",'icon icon-nav');
	}
	
	function del(){
		var rows = $("#list_data").datagrid('getSelections');
		if(rows.length>1){
			$.messager.alert('提示','只能选择一条记录','info');
			return;
	    } 
		$.post('<%=path%>/manager/task/del',{id:rows[0].id},
				function(data){
			         $.messager.alert('提示',data.Message,'info');
			         $("#list_data").datagrid('load');
		});
	  }
        
	</script>
	
	
</html>