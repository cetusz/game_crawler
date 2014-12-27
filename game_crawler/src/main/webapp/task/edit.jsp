<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title>任务录入</title>
	<jsp:include page="/common.jsp"></jsp:include>
    <script type="text/javascript" src="<c:url value='/resources/js/common.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/js/task.js' />"></script>	
</head>
<body>
<div id="addPannel" class="easyui-panel" title="任务编排页面" style="fit:true;background:#fafafa" >
	<div style="padding:10px 60px 20px 60px">
	
  	<form id="editform" method="post" >
		<table  class="mytable">
			<tr>
				<td class="alignright">任务名称:</td>	
				<td width="600px"> 
					<input type="hidden" id="id" name="id" value="${id }"/>
					<input id="title" name="title"
					  	class="easyui-validatebox"  type="text" data-options="required:true" />
					<span class="span_require_tip">*</span> 
				</td>
			</tr>
		    <tr>
					<td class="alignright">任务类名:</td>	
					<td> 
						<input id="jobClass" name="jobClass" data-options="required:true"
							class="easyui-validatebox"  type="text" /> 
					<span class="span_require_tip">*</span> 
					</td>
				</tr>
				<tr>
					<td class="alignright">任务Bean:</td>	
					<td> 
						<input id="jobBeanName" name="jobBeanName" data-options="required:true"
							class="easyui-validatebox"  type="text" /> 
					<span class="span_require_tip">*</span> 
					</td>
				</tr>
				<tr>
					<td class="alignright">生命状态:</td>	
					<td> 
						<select id="usable" name="usable" class="easyui-combobox" 
							data-options="
										valueField: 'key',
										textField: 'value',
										method:'get'">
						</select>		
						<span class="span_require_tip">*</span> 
						<span> 是：可运行|否：不允许运行</span>
					</td>
				</tr>
				<tr>
					<td class="alignright">jobDetail:</td>	
					<td> 
						<input id="jobDetailName" name="jobDetailName" data-options="required:true"
							class="easyui-validatebox"  type="text" /> 
					<span class="span_require_tip">*</span> 
					</td>
				</tr>
				<tr>
					<td class="alignright">jobTrigger:</td>	
					<td> 
						<input id="jobTriggerName" name="jobTriggerName" data-options="required:true"
							class="easyui-validatebox"  type="text" />
					<span class="span_require_tip">*</span> 
					</td>
				</tr>
			<tr>
				<td class="alignright">任务类型:</td>	
				<td>
					<select id="triggerType" name="triggerType" class="easyui-combobox" 
						data-options="
									required:true,
									valueField: 'key',
									textField: 'value',
									method:'get'">
					</select>		
					<span class="span_require_tip">*</span> 
				</td>
			</tr>
			<tr>
				<td class="alignright">targetMethod:</td>	
				<td> 
					<input id="targetMethod" name="targetMethod" data-options="required:true"
						class="easyui-validatebox"  type="text" /> 
					<span class="span_require_tip">*</span> 
				</td>
			</tr>
			<tr jobtype='cron'>
				<td class="alignright">cronExpression:</td>	
				<td> 
					<input id="cronExpression" name="cronExpression"  data-options="required:true"
						class="easyui-validatebox"  type="text" /> 
					<span class="span_require_tip">*</span> 
					<span><br>示例：<br>
							每隔12小时执行一次            0 0 0/12 * * ? * <br/>
	                     	每隔01分钟执行一次            0 0/1 * * * ? *  <br/></span>
					
				</td>
			</tr>			
			<tr jobtype='simple'>
				<td class="alignright">jobStartTime:</td>	
				<td> 
					<input id="jobStartTime" name="jobStartTime"  class="easyui-datetimebox"
						data-options="showSeconds:true,required:true" style="width: 150px"/> 

				</td>
			</tr>
			<tr  jobtype='simple'>
				<td class="alignright">startDelay:</td>	
				<td> 
					<input id="startDelay" name="startDelay" class="easyui-numberbox" 
					  type="text"	data-options="required:true,min:1000"/> 
					<span class="span_require_tip">* 单位:毫秒 1秒=1000毫秒</span>
				</td>
			</tr>			
			<tr jobtype='simple'>
				<td class="alignright">repeatInterval:</td>	
				<td> 
					<input id="repeatInterval" name="repeatInterval" class="easyui-numberbox" 
					  type="text"	data-options="required:true,min:1000" /> 
					<span class="span_require_tip">* 单位:毫秒</span>
				</td>
			</tr>			
		
			<tr><td>&nbsp;</td></tr>
			<tr>
				<td style="text-align:right;"></td>
				<td>
					<c:if test="${pageType ne 'detail'}">
						<a href="#" class="easyui-linkbutton" iconCls="icon-save" plain="false" onclick="javascript:task.save('editform')">保存</a>
					</c:if>
					<a href="#" class="easyui-linkbutton" iconCls="icon-no" plain="false" onclick="javascript:task.cancel()">取消</a>
				</td>
			</tr>
		</table>

	</form>
	</div>
	
</div>

<script type="text/javascript">

	//页面入口处
	$(function(){
		//类型
		$("#triggerType").combobox('loadData',common.queryList(triggerTypeJson, true,"key","value"));
		
		//类型
		$("#usable").combobox('loadData',common.queryList(yesOrNoJson, true,"key","value"));
		
		$("#triggerType").combobox({'onSelect':function(rec){
			tiggerTr(rec.value);
		}});
		//表单加载
		var _id = '${id}';
		task.formLoad("editform",_id);
		if(_id>0){
			$('#triggerType').combobox('disable');
		}
	});
	
	function tiggerTr(value){
		if(value=='simple'){
			$("tr[jobtype='cron']").each(function(index,item){
				$(item).hide();
			});
			$("tr[jobtype='simple']").each(function(index,item){
				$(item).show();
			});
			$('#cronExpression').val('');
            
			$("#jobStartTime").validatebox("reuse");
			$("#startDelay").validatebox("reuse");
			$("#repeatInterval").validatebox("reuse");
			$('#cronExpression').validatebox("remove");
		}else if(value=='cron'){
			$("tr[jobtype='cron']").each(function(index,item){
				$(item).show();
			});
			$("tr[jobtype='simple']").each(function(index,item){
				$(item).hide();
			});
			$("#jobStartTime").datetimebox('reset');
			$("#startDelay").val('');
			$("#repeatInterval").val('');
		                 
			$("#jobStartTime").validatebox("remove");
			$("#startDelay").validatebox("remove");
			$("#repeatInterval").validatebox("remove");
			$('#cronExpression').validatebox("reuse");
		}
		$("#title").focus();
	}
	
</script>
</body>
</html>