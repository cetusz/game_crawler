/**
 * 媒资管理前端代码逻辑封装
 * @author 904032
 */

var task = {};

var triggerTypeJson = {"1":"simple", "2":"cron"};
var jobStateJson = {"0":"normal", "1":"paused", "2":"complete", "3":"error", "4":"blocked", "-1":"none", "-2":"SysError"};

(function(){

	//媒资列表加载页面
	task.initPage = function(){

		 var options = {
	            columns:[[
					{field:'id',title:'任务编号',align:'center',width:60,sortable:true},
					{field:'title',title:'任务名称',align:'center',width:120,sortable:true,
						formatter:function(value,row,index){
							return htmlLink(row.title);
						}
					},
					{field:'jobDetailName',title:'任务对象',align:'center',width:150,sortable:true},
					{field:'jobTriggerName',title:'任务触发器',align:'center',width:150,sortable:true},
					{field:'triggerType',title:'触发器类型',align:'center',width:100,sortable:true,
						formatter:function(value,row,index){
							return triggerTypeJson[row.triggerType];
						}
					},
					{field:'targetMethod',title:'任务方法',align:'center',width:100,sortable:true,hidden:true},
					{field:'cronExpression',title:'Quartz时间',align:'center',width:100,sortable:true,hidden:true},
					{field:'jobStartTime',title:'开始时间',align:'center',width:150,sortable:true,hidden:true},
					{field:'startDelay',title:'延迟时间(毫秒)',align:'center',width:120,sortable:true,hidden:true},
					{field:'repeatInterval',title:'间隔时间(毫秒)',align:'center',width:120,sortable:true},
					{field:'state',title:'运行状态',align:'center',width:100 ,
						formatter:function(value,row,index){
							return jobStateJson[row.state];
						}
					},
					{field:'action',title:'操作',width:150,align:'center',
						formatter:function(value,row,index){
							var start = '<a href="#" onclick="task.start('+row.id+')">启动</a>';
							var pause = '<a href="#" onclick="task.pause('+row.id+')">暂停</a>';
							var state = row.state;
							
							var show ="";
							if(-1==state||1==state||2==state){
								show = start;
							}
							
							if(0==state){//可暂停的任务状态
								show = show + pause;
							}
							
							if(4==state){
								show = "运行中";
							}
	
							return show;
						}
					}
			 	]],
			 	url:globalWebAppURL+'/manager/task/list',
			    onClickCell: function(index,field,value){
				 	   
				      if(field == "title")
				      {		
				    	  task.showRawDetail('list_data',index,field,value,'modify');
				      }
				      else if(field == "detail")
				      {
				    	  task.showRawDetail('list_data',index,field,value,'detail');
				      }
				}
		}; 
		$('#list_data').mygrid(options);	
	}
	

	//link 页面超链接 
	function htmlLink(text,showColorRed) {
		if(text == "--"){
			return text;
		}
		else{
			if(showColorRed){
				return "<a href='#'><font color=red><strong>" + text + "</strong></font></a>"
			}
			else{
				return "<a href='#'>" + text + "</a>"
			}
		}	
	}

	
	//进入媒资修改|新增页面
	task.showRawDetail = function(datagridId,index,field,value,pageType){
		var data = $('#'+datagridId).datagrid('getData');
		var selectRec = data.rows[index];
		var id = selectRec.id;
		var url = globalWebAppURL+'/manager/task/id/'+id+'/page?pageType='+pageType;
		var title = selectRec.title;
		if(pageType=='detail') {
			common.addTabs(title + " 详情", url, 'icon icon-nav');
		}
		else if(pageType='modify') 
		{
			common.addTabs( title + " 编辑", url,'icon icon-nav');
		}
		else if(pageType='add') 
		{
			common.addTabs( title + "任务录入", url,'icon icon-nav');
		}
	}

	//列表页面
	task.start = function(id){
		var idsArray = new Array();
		var checkPass = true;
		if(id){
			idsArray[0] = id;
		}
		else{
			var rows = $('#list_data').datagrid('getSelections'); 
			var len = rows.length;
			for(var i=0;i<len;i++){
				idsArray[i] = rows[i].id;	
			}
		}

		//弹出对话框
		if(undefined==idsArray||idsArray.length<1){
			common.tipMessager('show','没有选择记录',1000);
			return;
		}
		
		//提交后台
		var submitUrl = globalWebAppURL+'/manager/task/start';
	  	$.post(submitUrl,{ids:idsArray.toString()},function(data){
	  		var data = eval('(' + data + ')');  // change the JSON string to javascript object
	  		//操作结果显示
	  		common.fadeMessager("list_data",data.message);
	    });
	}
	
	
	//列表页面
	task.pause = function(id){
		var idsArray = new Array();

		if(id){
			idsArray[0] = id;
		}
		else{
			var rows = $('#list_data').datagrid('getSelections'); 
			var len = rows.length;
			for(var i=0;i<len;i++){
				idsArray[i] = rows[i].id;	
			}
		}

		//弹出对话框
		if(undefined==idsArray||idsArray.length<1){
			common.tipMessager('show','没有选择记录',1000);
			return;
		}
		
		//提交后台
		var submitUrl = globalWebAppURL+'/manager/task/pause';
	  	$.post(submitUrl,{ids:idsArray.toString()},function(data){
	  		var data = eval('(' + data + ')');  // change the JSON string to javascript object
	  		//操作结果显示
	  		common.fadeMessager("list_data",data.message);
	    });
	}
	
	
	
	
	//************************以下为编排页面js代码**************************//
	//编辑页面初始加载
	task.formLoad = function(formId,assetId){
	     $('#'+formId).form({
		        url:globalWebAppURL+"/manager/task/save",
		        onSubmit: function(){
		    		$.messager.progress();
		        	var isValid = $(this).form('validate');
		    		if (!isValid){
		    			$.messager.progress('close');
		    		}
		    		return isValid;	// return false will stop the form submission
		        },
		        success:function(data){
	    			$.messager.progress('close');
		        	var data = eval('(' + data + ')');  // change the JSON string to javascript object
					if(data.success)
					{
						$.messager.alert('Success','<center>操作成功</center>');
						common.refreshTabByFunc('源扩展配置','doSearch');
						var jq = top.jQuery;
						var currTabPannel = jq('#tabs').tabs('getSelected');
						var currTab = currTabPannel.panel('options').title;
						jq('#tabs').tabs('close', currTab);
					}
					else
					{
						$.messager.alert('Warning','<center>操作失败!'+data.message+"</center>");
					}
		        },
		        onLoadSuccess:function(data){
					tiggerTr($("#triggerType").combobox('getText'));
				}
			});
	     
	    //根据id 是否向后台发送数据获取请求
		if(assetId>0)
		{
			$('#list_data').datagrid('unselectAll');
			$('#'+formId).form('load',globalWebAppURL+'/manager/task/id/'+assetId);
		}
	}
	
	//编排页面保存
	task.save = function(formId){
		$('#'+formId).submit();
	}
	
	//编排页面取消编辑
	task.cancel = function(){
		var jq = top.jQuery;
		var currTabPannel = jq('#tabs').tabs('getSelected');
		var currTab = currTabPannel.panel('options').title;
		jq('#tabs').tabs('close', currTab);
	}
	task.add = function(){
		common.addTabs("任务录入", globalWebAppURL+"/manager/task/id/-1/page?pageType=add",'icon icon-nav');
	}
})();
