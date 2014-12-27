//状态值
var yesOrNoJson = {"0":"否","1":"是"};

//发布状态值
var pubStateJson = {"defined":"未上线", "online":"上线", "offline":"下线","preonline":"预上线"};

//状态值
var statusJson = {
		"0":"初始化","1":"待存储","2":"存储中","3":"存储完成","-3":"存储失败",
		"-4":"存储提交失败","5":"预上线",
		"11":"待分发","12":"分发中","-12":"分发失败",
		"21":"草稿","22":"待审核","23":"审核被拒","24":"标记驳回",
		"25":"已审核","26":"默认审核","27":"下线","28":"更新",
		"99":"已废弃","-1":"已删除"};

var common = {};
(function(){
	
	//enter事件
	common.enterEvent = function(eventName){
		 document.onkeydown = function(e){
	    	    if(!e) e = window.event;//火狐中是 window.event
		        if((e.keyCode || e.which) == 13){
		        	eval(eventName)();
		        }
	        }
	};
	
	//*********************对话框页面控制函数**********************//
	//通用对话框
	common.tipMessager = function(type,msg,delay){
		$.messager.show({
			title:'Error',
			msg:'<center>'+msg+'</center>',
			timeout:undefined==delay?1000:delay,
			showType:type,
			style:{
				right:'',
				top:document.body.scrollTop+document.documentElement.scrollTop + 200,
				bottom:''
			}
		});
	}
	
	//通用对话框
	common.fadeMessager = function(datagridId,msg,delay,width,height,hShift){
		//根据反馈结果，弹出对话框，显示操作结果(2秒自动关闭或手动关闭，触发当前页面刷新)
		if(undefined==hShift){
			hShift = 200;
		}
		$.messager.show({
			showType:'fade',
			//showSpeed： 定义消息窗口完成显示所需的以毫秒为单位的时间。默认是 600。
			width:undefined==width?300:width,
			height:undefined==height?100:height,
			msg:"<center>"+msg+"</center>",
			title:'info',
			timeout:undefined==delay?2000:delay,
			style:{
				right:'',
				top:document.body.scrollTop+document.documentElement.scrollTop+hShift,
				bottom:''
			},
			onDestroy:function(){
				$("#"+datagridId).datagrid('reload');
			}
		});
	}
	
	common.fadeMessageShow=function(msg){
		$.messager.show({
			showType:'fade',
			//showSpeed： 定义消息窗口完成显示所需的以毫秒为单位的时间。默认是 600。
			width:300,
			height:100,
			msg:"<center>"+msg+"</center>",
			title:'info',
			timeout:undefined==delay?2000:delay,
			style:{
				right:'',
				top:document.body.scrollTop+document.documentElement.scrollTop+hShift,
				bottom:''
			},
			onDestroy:function(){
			}
		});
	}
	
	
	// 显示提示
	common.showTip = function(target, msg){
		 $(target).tooltip('destroy');
		 $(target).tooltip({
			    position: 'right',
			    content: '<span style="color:red">'+msg+'</span>',
			    showEvent:'mouseover',
			    onShow: function(){
			        $(this).tooltip('tip').css({
			            backgroundColor: '#FFFFCC',
			            borderColor: '#CC9933',
			            color:"#000"
			        });
			    }
		 }).tooltip('show');
	}
	
	// 隐藏提示
	common.hideTip = function(target) {
		$(target).tooltip('destroy');
	}
	
	//*********************tab页面控制函数**********************//
	//添加tab方法
	common.addTabs = function(subtitle, url, icon) {
			var jq = top.jQuery;
			var createFrame = function(url) {
				var s = '<iframe scrolling="auto" frameborder="0"  src="' + url + '" style="width:100%;height:100%;"></iframe>';
				return s;
			}
			if (!jq('#tabs').tabs('exists', subtitle)) {
				jq('#tabs').tabs('add', {
					title : subtitle,
					content : createFrame(url),
					closable : true,
					icon : icon
				});
			} else {
				$.messager.confirm('Confirm', subtitle+'页面已经存在，确认覆盖吗?', function(r){
					if (r){
						jq('#tabs').tabs('close', subtitle);
						jq('#tabs').tabs('add', {
							title : subtitle,
							content : createFrame(url),
							closable : true,
							icon : icon
						});
					}
				});
			
	    }
	};
	
	common.refreshTabByFunc = function(title, function_name){  
		var jq = top.jQuery;
	    var refresh_tab = title?jq('#tabs').tabs('getTab',title):jq('#tabs').tabs('getSelected');  
	    
	    if(refresh_tab && refresh_tab.find('iframe').length > 0){  
		    var _refresh_ifram = refresh_tab.find('iframe')[0];  
		    _refresh_ifram.contentWindow[function_name]();
	    }  
	};
	
	common.refreshTabByUrl = function(title, url){  
		var jq = top.jQuery;
	    var refresh_tab = title?jq('#tabs').tabs('getTab',title):jq('#tabs').tabs('getSelected');  
	    
	    if(refresh_tab && refresh_tab.find('iframe').length > 0){  
		    var _refresh_ifram = refresh_tab.find('iframe')[0];  
		 	var refresh_url = url?url:_refresh_ifram.src;  
		 	_refresh_ifram.contentWindow.location.href=refresh_url;  
	    }  
	};
	
   
	//*********************日期控制函数**********************//
	common.dateUtils = function(date){
		var now = date||new Date();                 //当前日期   
		this.nowDayOfWeek = now.getDay();         //今天本周的第几天   
		this.nowDay = now.getDate();              //当前日   
		this.nowMonth = now.getMonth();           //当前月   
		this.nowYear = now.getFullYear();             //当前年   
		//nowYear += (nowYear < 2000) ? 1900 : 0;  //
		
	}

			//获得本周的开始日期   
	common.dateUtils.prototype.getWeekStartDate =  function(){
				var dayofweek = this.nowDayOfWeek == 0 ? this.nowDay - this.nowDayOfWeek-6 : this.nowDay - this.nowDayOfWeek+1;
			    var weekStartDate = new Date(this.nowYear, this.nowMonth, dayofweek);    
			    return weekStartDate;   
	};
	//获得本周的结束日期 
	common.dateUtils.prototype.getWeekEndDate = function(){
		  var dayofweek = this.nowDayOfWeek == 0 ? this.nowDay  :  this.nowDay + (7 - this.nowDayOfWeek);
		  var weekEndDate = new Date(this.nowYear, this.nowMonth, this.nowDay + (7 - dayofweek));    
		  return weekEndDate; 
	},
	common.dateUtils.prototype.formatDate = function(date){
		    var myyear = date.getFullYear();   
		    var mymonth = date.getMonth()+1;   
		    var myweekday = date.getDate();    
		       
		    if(mymonth < 10){   
		        mymonth = "0" + mymonth;   
		    }    
		    if(myweekday < 10){   
		        myweekday = "0" + myweekday;   
		    }   
		    return (myyear+"-"+mymonth + "-" + myweekday); 
	},
	common.dateUtils.prototype.string2Date = function(dateStr){
		var date = dateStr.split("-");
		var result = new Date();
		result.setYear(date[0]);
		result.setMonth(parseInt(date[1])-1);
		result.setDate(date[2]);
		return result;
	};
	//获得本月的开始日期   
	common.dateUtils.prototype.getMonthStartDate = function(){   
	    var monthStartDate = new Date(this.nowYear, this.nowMonth, 1);    
	    return this.formatDate(monthStartDate);   
	};  
	  
	//获得本月的结束日期   
	common.dateUtils.prototype.getMonthEndDate = function(){   
	    var monthEndDate = new Date(this.nowYear, this.nowMonth, this.getMonthDays(nowMonth));    
	    return this.formatDate(monthEndDate);   
	};   
	  
	//获得本季度的开始日期   
	common.dateUtils.prototype.getQuarterStartDate = function(){   
	    var quarterStartDate = new Date(this.nowYear, this.getQuarterStartMonth(), 1);    
	    return this.formatDate(quarterStartDate);   
	};   
	  
	//或的本季度的结束日期   
	common.dateUtils.prototype.getQuarterEndDate = function(){   
	    var quarterEndMonth = getQuarterStartMonth() + 2;   
	    var quarterStartDate = new Date(this.nowYear, quarterEndMonth, this.getMonthDays(quarterEndMonth));    
	    return this.formatDate(quarterStartDate);   
	};
	//获得本周的数组
	common.dateUtils.prototype.getThisWeekArray = function(){
		var array = [];
		var startDate = this.getWeekStartDate();
		array.push(this.formatDate(startDate));
		for(var i = 1;i<7;i++){
				var d = new Date()
				d.setDate(startDate.getDate()+ i);
				array.push(this.formatDate(d));
		}
		return array;
	};
	//获取下一周的时间数组
	common.dateUtils.prototype.getNextWeekArray = function(date){
		var array = [];
		var time = this.string2Date(date);
		var firstdayofNextweek = time;
		firstdayofNextweek.setDate(time.getDate()+1);
		array.push(this.formatDate(firstdayofNextweek));
		for(var i = 1;i<7;i++){
				var d = new Date();
				d.setYear(firstdayofNextweek.getFullYear());
				d.setMonth(firstdayofNextweek.getMonth());
				d.setDate(firstdayofNextweek.getDate()+ i);
				array.push(this.formatDate(d));
		}
		return array;
		
		
	};
	common.dateUtils.prototype.getPreWeekArray = function(date){
		var array = [];
		var time = this.string2Date(date);
		var lastdayofPreWeek = time;
		lastdayofPreWeek.setDate(time.getDate()-1);
		this.nowDayOfWeek = lastdayofPreWeek.getDay();         //今天本周的第几天   
		this.nowDay = lastdayofPreWeek.getDate();              //当前日   
		this.nowMonth = lastdayofPreWeek.getMonth();           //当前月   
		this.nowYear = lastdayofPreWeek.getYear();
		for(var i = 6;i>0;i--){
				var d = new Date();
				d.setYear(lastdayofPreWeek.getFullYear());
				d.setMonth(lastdayofPreWeek.getMonth());
				d.setDate(lastdayofPreWeek.getDate()- i);
				array.push(this.formatDate(d));
		}
		array.push(this.formatDate(lastdayofPreWeek));
		return array;
	};
	
	
	//*********************???????**********************//
	//获取Json格式map中Key对应的值，例如用于数据库查处的状态值找到对应的状态页面显示
	common.getJsonMapsValue=function(jsonMaps,key){
		 var value = '';
		 $(jsonMaps).each(function(index,item){
			 if(item.key == key){
				 value = item.value;
				 return false;
			 }
		 });
		 return value;   
	};
	
	
	//*********************通用基础变量判断是否为空函数**********************//
	//判断是否非空
	String.prototype.isEmpty = function() {
		var source = this;
	 	if(source==undefined||source==''||source=='null'||/^\s*$/.test(source))
			return true;
	 	return false;
	}
	
	//*********************无状态批量提交控制函数**********************//
	//ajax批量提交多条选中记录到后台
	common.ajaxBatchSubmit=function(url,ids,tableId){
		//提交后台
	  	$.post(url,{ids:ids},function(data){
	  		//var data = eval('(' + data + ')');  // change the JSON string to javascript object
			//根据反馈结果，弹出对话框，显示操作结果(2秒自动关闭或手动关闭，触发当前页面刷新)
			$.messager.show({
				showType:'fade',
				//showSpeed： 定义消息窗口完成显示所需的以毫秒为单位的时间。默认是 600。
				width:300,
				height:100,
				msg:data.message,
				title:'info',
				timeout:2000,
				style:{
					right:'',
					top:document.body.scrollTop+document.documentElement.scrollTop+200,
					bottom:''
				},
				onDestroy:function(){
					$("#"+tableId).datagrid('reload');
				}
			});
	    });
	};
	
	
	// ajax批量删除操作
	common.ajaxBatchDelete = function (datagridId, actionUrl) {
		//删除时先获取选择行
		var rows = $("#"+datagridId).datagrid("getSelections");
		//选择要删除的行
		if (rows.length > 0) {
			$.messager.confirm("提示", "你确定要删除吗?", function (r) {
				if (r) {
					var ids = "";
			        for(var i=0,len=rows.length; i<len; i++){
						if(i>0){
							ids+=",";
						}
						ids += rows[i].id;
			        }
					$.post(actionUrl, {ids:ids}, function(data){
						if(data.success){
							$("#"+datagridId).datagrid('reload');
							$.messager.alert("提示", data.message, "info");
						} else{
							$.messager.alert("提示", "删除操作失败", "error");
						}
					});
				}
			});
        } else {
            $.messager.alert("提示", "请选择要删除的行", "warning");
        }
	}
	
	
	//*********************通用链接控制函数**********************//
	// 返回超链接
	common.htmlLink = function(text, func, showUnderline){
		if (text == "--") {
			return text;
		}
		else {
			if (!showUnderline) {
				return "<a href='#' onclick='" + func + "' style='color:blue; text-decoration:none;'>" + text + "</font></a>";
			}
			else {
				return "<a href='#' onclick='" + func + "'>" + text + "</a>";
			}
		}	
	}

	/**
	 * 将json对象转化为用于查询下拉列表初始化json数组对象
	 * 
	 * 如：{"1":"test1", "2":"test2"} 
	 * 可以转化为
	 * [{"id":"","text":"请选择","selected":true}, {"id":"1","text":"test1"},{"id":"2","text":"test2"}]
	 * 
	 * obj			为待转化的json对象
	 * isInitNull	是否初始化“请选择”
	 * id			下拉列表初始化对象属性ID值变量名，默认为“id”
	 * text			下拉列表初始化对象属性显示值变量名，默认为“text”
	 */
	common.queryList = function(obj, isInitNull, id, text) {
		var queryJson = [];
		if (id == undefined) {
			id = "id";
		}
		if (text == undefined) {
			text = "text";
		}
		if (isInitNull) {//初始化“请选择”
			var isNullSelect = '{"'+ id + '":"","'+ text + '":"请选择","selected":true}';
			var queryIsNullObj = eval('(' + isNullSelect + ')');
			queryJson.push(queryIsNullObj);
		}
		for ( var p in obj ){ // 方法
			if ( typeof ( obj[p]) != "function" ){
				// p 为属性名称，obj[p]为对应属性的值
				var select = '{"'+ id + '":"' + p + '","'+ text + '":"'+ obj[p] + '"}';
				var selectObj = eval('(' + select + ')');
				queryJson.push(selectObj);
			} 
		}
		return queryJson;
	}
	
	
	/**
	 * 新增/编辑保存方法
	 * 
	 * dlgId		弹出框Id
	 * formId 		保存的表单ID
	 * url			请求的URL
	 * datagridId	需要刷新datagrid的id
	 * 
	 * 
	 */
	common.doSave=function(dlgId,formId,url,datagridId){
	    $('#'+formId).form('submit',{
	    	url: url,
	        onSubmit: function(){
	            return $('#'+formId).form('validate');
	        },
	        success: function(result){
	            var result = eval('('+result+')');
	            if (result.success==true){
	            	$('#'+dlgId).dialog('close');
	                $('#'+datagridId).datagrid('reload');
                } else {
                    $.messager.show({
                        title: '错误',
                        msg: result.message
                    });
                }
	        }
	    });
	}
	
})();

  
