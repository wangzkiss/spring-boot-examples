<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>任务管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
			var start = {
			    elem: '#startTime',
			    format: 'YYYY-MM-DD hh:mm:ss',
			    // min: laydate.now(), 
			    istime: true,
			    // event: 'focus',
			    choose: function(datas){
			         end.min = datas; //开始日选好后，重置结束日的最小日期
			         end.start = datas //将结束日的初始值设定为开始日
			    }
			};
			var end = {
			    elem: '#endTime',
			    format: 'YYYY-MM-DD hh:mm:ss',
			    // min: laydate.now(),
			    istime: true,
			    // event: 'focus',
			    choose: function(datas){
			        start.max = datas; //结束日选好后，重置开始日的最大日期
			    }
			};
			laydate(start);
			laydate(end);

            $("#contentTable tbody td:nth-child(2),td:nth-child(5)").each(function(i){
                //给td设置title属性,并且设置td的完整值.给title属性.
                $(this).attr("title",$(this).text());

	      	});

		});
		
		function batchGroup(){
			var str="";
		    var ids="";
		    $(".table-fixed__table tbody tr td input.i-checks:checkbox").each(function(){
			    if(true == $(this).is(':checked')){
			      str+=$(this).attr("id")+",";
			    }
		    });
		    
		    if(str.substr(str.length-1)== ','){
		    	ids = str.substr(0,str.length-1);
		    }
		    if(ids == ""){
				top.layer.alert('请至少选择一个任务!', {icon: 0, title:'警告'});
			    return;
		  	}
		    
		    var taskFlag = document.getElementById("taskFlag").value;

			openDialog('任务组', '${ctx}/tji/taskgroup/list-for-task?taskFlag='+taskFlag+'&taskIds='+ids,'800px', '500px');
			

		}
		
		function batchSch(){
			var str="";
		    var ids="";
		    $(".table-fixed__table tbody tr td input.i-checks:checkbox").each(function(){
			    if(true == $(this).is(':checked')){
			      str+=$(this).attr("id")+",";
			    }
		    });
		    
		    if(str.substr(str.length-1)== ','){
		    	ids = str.substr(0,str.length-1);
		    }
		    
		    if(ids == ""){
				top.layer.alert('请至少选择一个任务!', {icon: 0, title:'警告'});
			    return;
		  	}

		    var taskFlag = document.getElementById("taskFlag").value;
		    // console.log($("#jobIds").val())
			openDialog('设置调度', '${ctx}/tji/job/get?taskIds='+ids,'800px', '500px');
		}
		
		function delTask(taskId)
		{
			var index = top.layer.confirm('确认要删除该任务吗？', {icon: 3, title:'系统提示'}, function(){

				$.ajax({
					type:"post",
			        url: "${ctx}/tji/task/del",
			        data:"ids="+taskId,
			        success: function (data) {
			    	   	top.layer.alert(data);
			    	   	sortOrRefresh();
			        }
				});
			})
			
		}

		function showImport(){
			openDialog('ETL模板任务导入', '${ctx}/tji/task/show-import','500px', '350px');
		}

		function openDialogConn(title,url,width,height,val,target, typeName){
			var jobIds = []
			var data = $('input[name=jobIds]').each(function(i,n){
				if(n.id == val){
					jobIds.push(n.value)
				}
			})
			if(jobIds.length > 0){
				url = url+'&typeName='+typeName +'&jobIds='+jobIds.join(',')
			}else{
				url = url+'&typeName='+typeName
			}
			
		    top.layer.open({
		        type: 2,  
		        area: [width, height],
		        title: title,
		        maxmin: true, //开启最大化最小化按钮
		        content: url,
		        btn: ['确定','关闭'],
		        yes: function(index, layero){
		            var body = top.layer.getChildFrame('body', index);
					var iframeWin = layero.find('iframe')[0]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
					var inputForm = body.find('#inputForm');
					var top_iframe;
					if(target){
						top_iframe = target;//如果指定了iframe，则在改frame中跳转
					}else{
						top_iframe = top.getActiveTab().attr("name");//获取当前active的tab的iframe 
					}

					inputForm.attr("target",top_iframe);//表单提交成功后，从服务器返回的url在当前tab中展示

					if(iframeWin.contentWindow.doSubmit() ){
					// top.layer.close(index);//关闭对话框。
						setTimeout(function(){top.layer.close(index)}, 100);//延时0.1秒，对应360 7.1版本bug
					}
		        
		        },
		    });   
	    
	  	}
	  	var height = this.parent.window.document.body.clientHeight - 100,
			width = this.parent.window.document.body.clientWidth - 100;
	  	function openDialogTask (url, title) {
	  		var index = top.layer.open({
                type: 2,  
                area: ["100%", "100%"],
                title: title,
                maxmin: true, //开启最大化最小化按钮
                content: url,
                btn: ['确定', '关闭'],
                yes: function(index, layero){
                    var body = top.layer.getChildFrame('body', index);
                    var iframeWin = layero.find('iframe')[0]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
                    // var inputForm = body.find('#importForm');
                    var params = iframeWin.contentWindow.doSubmit()
                    // console.log(params)
                    var type = typeof params[0]
                    if(type != 'object'){
                    	top.layer.msg(params[0], {icon: 0})
                    	return;
                    }
                    $.post(params[1], params[0], function (json) {
                    	top.layer.closeAll();
                    	location.reload();
                    	top.layer.alert(json.msg);
                    })
                },
                cancel: function(index){
                    top.layer.close(index)
                }
            });
	  	}
	  	function openDialogEtl(url, title, target){
	  		var index = top.layer.open({
                type: 2,  
                area: ["900px", "600px"],
                title: title,
                maxmin: true, //开启最大化最小化按钮
                content: url,
                btn: ['确定', '关闭'],
                yes: function(index, layero){
                    var body = top.layer.getChildFrame('body', index);
			        var iframeWin = layero.find('iframe')[0]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
			        var inputForm = body.find('#inputForm');
			        var top_iframe;
			        if(target){
			        	top_iframe = target;//如果指定了iframe，则在改frame中跳转
			        }else{
			        	top_iframe = top.getActiveTab().attr("name");//获取当前active的tab的iframe 
			        }
			        inputForm.attr("target",top_iframe);//表单提交成功后，从服务器返回的url在当前tab中展示
			         
			        var iframeid="iframe"+Date.now();
		            var iframe=$('<iframe id="'+iframeid+'" style="display:none" ></iframe>');
		            iframe.attr('name',iframeid);
		            body.append(iframe);
		            inputForm.attr("target",iframeid);//表单提交成功后，从服务器返回的url在当前tab中展示
		                
		            iframe.on("load error",function(){
		                top.layer.close(index);
		                //iframeWin.contentWindow.closeTip&&iframeWin.contentWindow.closeTip();
		                iframe.remove();
		                setTimeout(function(){
		               		window.location.reload();
		                }, 1000)

		            });
		            iframeWin.contentWindow.doSubmit();
                },
                cancel: function(index){
                    top.layer.close(index)
                }
            });
	  	}
	  	function addActive(title){
	  		var index = top.layer.open({
	  			type: 2,
	  			title: title,
	  			btn: ["确定","取消"],
	  			area: ["700px","500px"],
	  			content: '${ctx}/tji/task/show-activiti',
	  			yes: function(index, layero){
	  				var body = top.layer.getChildFrame('body', index);
                    var iframeWin = layero.find('iframe')[0]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
                    var inputForm = body.find('#importForm');
                    var url = '${ctx}/activiti/model/create'
                    var params = iframeWin.contentWindow.doSubmit();
                    var typeData = typeof params
                    if(typeData != 'object'){
                    	top.layer.msg(params, {icon: 0})
                    	return;
                    }
                    $.get(url, params, function(json){
                    	var data = $.parseJSON(json)
                    	if(data.retStatus == '000'){
                    		location.reload();
							top.openTab('activiti/modeler.html?modelId='+data.retBody, '工作流设计');
							top.layer.close(index)
                    	}else{
                    		top.layer.alert(data.retMsg)
                    	}
                    })
	  			}
	  		})
	  	};

	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
    <div class="ibox-title">
		<h5>
		<c:if test="${task.taskFlag==1}">
			ETL任务
		</c:if>
		<c:if test="${task.taskFlag==2}">
			计算任务
		</c:if>
		<c:if test="${task.taskFlag==3}">
			工作流任务
		</c:if>
		</h5>
	</div>
    <div class="ibox-content">
	<sys:message content="${message}"/>
	<!--查询条件-->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="task" name="task" action="${ctx}/tji/task/" method="post" class="form-inline form-box">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="taskFlag" name="taskFlag" type="hidden" value="${task.taskFlag}"/>
		<input id="groupId" name="groupId" type="hidden" value="${task.groupId}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
			<c:if test="${task.taskFlag==1}">
			<div class="form-group">
				<label>任务类型：</label>
				<form:select path="taskType" id="taskType" name="taskType" class="form-control input-sm required"> 
					<form:option value="-1">全部</form:option>
					<form:option value="1">ETL自定义任务</form:option>
					<form:option value="2">ETL模板任务</form:option>
					<form:option value="13">FLUME任务</form:option>
				</form:select>
				</div>
			</c:if>
			<c:if test="${task.taskFlag==2}">
			<div class="form-group">
				<label>任务类型：</label>
				<form:select path="taskType" id="taskType" name="taskType" class="form-control input-sm required"> 
					<option value="-1">全部</option>
				</form:select>
			</div>
			</c:if>
		<div class="form-group">
			<label>任务名：</label>
			<form:input path="taskName" htmlEscape="false" maxlength="100"  class=" form-control input-sm"/>
		</div>
		<div class="form-group">
			<label>所属组：</label>
			<form:input path="groupName" htmlEscape="false" maxlength="100"  class=" form-control input-sm"/>
		</div>
		<div class="form-group">
			<label>开始时间：</label>
			<input id="startTime" name="startTime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${task.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
		</div>
		<div class="form-group">
			<label>结束时间：</label>
			<input id="endTime" name="endTime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${task.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
		 </div>
		 <div class="btn-form-box">
			<button  class="btn btn-success btn-rounded btn-sm filter" onclick="search()" value="33"><i class="fa fa-search"></i> 查询</button>
			<a class="btn btn-success btn-rounded btn-sm btn-outline filterReset" onclick="reset()" ><i class="fa fa-refresh"></i> 重置</a>
		 </div>
	</form:form>
	</div>
	</div>
	<!-- 工具栏 -->
	<div class="row c-mb">
	<div class="col-sm-12">
		<div class="pull-left">
			<c:if test="${task.taskFlag==3}">
			<shiro:hasPermission>
				<!-- <button class="btn btn-white btn-sm toolbar" onclick="top.openTab('${ctx}/tji/task/to-act','工作流设计')" data-toggle="tooltip" data-placement="left" title="添加工作流"><i class="fa fa-plus"></i> 添加</button> -->
				<button class="btn btn-white btn-sm toolbar" onclick="addActive(this.title)" data-toggle="tooltip" data-placement="left" title="添加工作流"><i class="fa fa-plus"></i> 添加</button>
			</shiro:hasPermission>
			
			</c:if>
			<c:if test="${task.taskFlag==1}">
				<shiro:hasPermission name="tji:job:add">
					<!-- <table:addRow url="${ctx}/tji/task/set-etl" width="900px" height="600px" title="ETL任务配置"></table:addRow> -->
					<div class="btn-group">
						<button type="button" class="btn btn-success dropdown-toggle btn-sm" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><i class="fa fa-plus"></i> 添加  <span class="caret"></span>
						</button>&nbsp;
						<ul class="dropdown-menu">
						    <li><a href="#" onclick="openDialogEtl('${ctx}/tji/task/set-etl', '新增ETL模板任务')">ETL模板任务</a></li>
						    <li><a href="#" onclick="openDialogTask('${ctx}/tji/taskconfigure/addFlumnTask', '新增Flume任务')">FLUME任务</a></li>
						</ul>
					</div>
				</shiro:hasPermission>
			</c:if>
			<c:if test="${task.taskFlag==1}">
				<shiro:hasPermission name="tji:task:import">
					<button id="btnImport" class="btn btn-white btn-sm toolbar" data-toggle="tooltip" data-placement="left" title="导入" onclick="showImport()"><i class="fa fa-folder-open-o "></i> 导入</button>
				</shiro:hasPermission>
			</c:if>
			<c:if test="${task.taskFlag==2}">
				<shiro:hasPermission name="tji:job:add">
					<div class="btn-group">
						<button type="button" class="btn btn-success dropdown-toggle btn-sm" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><i class="fa fa-plus"></i> 添加  <span class="caret"></span>
						</button>&nbsp;
						<ul class="dropdown-menu">
						    <li><a href="#" onclick="openDialogTask('${ctx}/tji/task/formKylin', '新增OLAP任务')">OLAP任务</a></li>
						    <li><a href="#" onclick="openDialogTask('${ctx}/compute/computetask/showHiveTask', '新增HIVE任务')">HIVE任务</a></li>
						    <li><a href="#" onclick="openDialogTask('${ctx}/compute/computeSparktask/showSparkTask', '新增SPARK任务')">SPARK任务</a></li>
						    <li><a href="#" onclick="openDialogTask('${ctx}/tji/taskconfigure/addCustomTask', '新增自定义任务')">自定义任务</a></li>
						    <li><a href="#" onclick="openDialogTask('${ctx}/tji/taskconfigure/addDataCheckTask', '新增数据质量任务')">数据质量任务</a></li>
						    <li><a href="#" onclick="openDialogTask('${ctx}/tji/taskconfigure/addScriptTask', '新增存储过程任务')">存储过程任务</a></li>
						    <li><a href="#" onclick="openDialogTask('${ctx}/compute/computeTratask/showTraTask', '新增OLTP任务')">OLTP任务</a></li>
						</ul>
					</div>
				</shiro:hasPermission>
			</c:if>
			<shiro:hasPermission name="tji:job:add">
				<button class="btn btn-white btn-sm toolbar" data-toggle="tooltip" data-placement="left" onclick="batchSch()" title="设置调度"><i class="fa fa-file-text-o"></i> 设置调度</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="tji:taskgroup:listForTask">
				<button class="btn btn-white btn-sm toolbar" data-toggle="tooltip" data-placement="left" onclick="batchGroup()" title="分组"><i class="fa fa-file-text-o"></i> 分组</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="tji:task:del">
				<table:delRowAjax url="${ctx}/tji/task/del?taskFlag=${task.taskFlag}" id="contentTable">
				</table:delRowAjax>
			</shiro:hasPermission>
			
			
		</div>
		<!-- <div class="pull-right">
			<c:if test="${task.taskFlag==2}">
			<shiro:hasPermission name="tji:job:add">
				<button class="btn btn-white btn-sm toolbar" onclick="openDialogTask('${ctx}/tji/task/formKylin', '新增Kylin任务')" data-toggle="tooltip" data-placement="left" title="Kylin任务"><i class="fa fa-plus white"></i> Kylin任务</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="tji:job:add">
				<button class="btn btn-white btn-sm toolbar" onclick="openDialogTask('${ctx}/compute/computetask/showHiveTask', '新增Hive任务')" data-toggle="tooltip" data-placement="left" title="Hive任务"><i class="fa fa-plus white"></i> Hive任务</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="tji:job:add">
				<button class="btn btn-white btn-sm toolbar" onclick="openDialogTask('${ctx}/compute/computeSparktask/showSparkTask', '新增Spark任务')" data-toggle="tooltip" data-placement="left" title="Spark任务"><i class="fa fa-plus white"></i> Spark任务</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="tji:job:add">
				<button class="btn btn-white btn-sm toolbar" onclick="openDialogTask('${ctx}/tji/taskconfigure/addCustomTask', '新增自定义MR任务')" data-toggle="tooltip" data-placement="left" title="自定义MR任务"><i class="fa fa-plus white"></i> 自定义MR任务</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="tji:job:add">
				<button class="btn btn-white btn-sm toolbar" onclick="openDialogTask('${ctx}/tji/taskconfigure/addDataCheckTask', '新增数据质量任务')" data-toggle="tooltip" data-placement="left" title="数据质量任务"><i class="fa fa-plus white"></i> 数据质量任务</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="tji:job:add">
				<button class="btn btn-white btn-sm toolbar" onclick="openDialogTask('${ctx}/tji/taskconfigure/addScriptTask', '新增脚本任务配置')" data-toggle="tooltip" data-placement="left" title="脚本任务"><i class="fa fa-plus white"></i> 脚本任务</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="tji:job:add">
				<button class="btn btn-white btn-sm toolbar" onclick="openDialogTask('${ctx}/compute/computeTratask/showTraTask', '新增X-OLTP任务')" data-toggle="tooltip" data-placement="left" title="X-OLTP任务"><i class="fa fa-plus white"></i> X-OLTP任务</button>
			</shiro:hasPermission>
			</c:if>
		</div> -->
	</div>
	</div>
	<!-- 表格 -->
	<div class="table-fixed" style="margin-top:0; ">
	<table id="contentTable" class="table table-bordered table-hover table-condensed table-store-msg table-fixed__table">
		<colgroup>
			<col style="width:3%">
			<col>
			<col style="width:15%">
			<col style="width:10%">
			<col style="width:15%">
			<col style="width:20%">
		</colgroup>
		<thead>
			<tr>
				<th> <input type="checkbox" class="i-checks ichecks-all"></th>
				<th class="sort-column taskName">任务名</th>
				<th class="groupName">所属组</th>
				<th class="typeName">任务类型</th>
				<th class="sort-column createTime">创建时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tasks">
			<tr>
				<td class="text-center"> <input type="checkbox" id="${tasks.taskId}" class="i-checks"></td>
				<td class="viewBig"><a class="alink" href="#" onclick="openDialogView('查看任务', '${ctx}/tji/task/view?taskId=${tasks.taskId}','800px', '500px')">
					${tasks.taskName}
				</a></td>
				<td >
					${tasks.groupName}
				</td>
				<td class="text-center">
					${tasks.typeName}
				</td>
				<td class="viewBig">
					<fmt:formatDate value="${tasks.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td class="tdImportant">
					<c:choose>
					<c:when test="${tasks.taskType=='12'}">
						<a href="#" onclick="openDialogView('查看', '${ctx}/tji/task/view?taskId=${tasks.taskId}','900px', '600px')" class="alink" > 查看</a>
					</c:when>
					<c:otherwise>
					<shiro:hasPermission name="tji:task:view">
						<a href="#" onclick="openDialogView('查看', '${ctx}/tji/task/view?taskId=${tasks.taskId}','800px', '500px')" class="alink" > 查看</a>
					</shiro:hasPermission>
					</c:otherwise>
					</c:choose>
					<span class="gap-line">|</span>
					<shiro:hasPermission name="tji:job:add">
					<c:forEach items="${tasks.jobList}" var="job">
						<input type="text" hidden="true" name="jobIds" class="jobIds" id="${tasks.taskId}" value="${job.jobId}">
					</c:forEach>
    					<a href="#" onclick="openDialogConn('调度设置', '${ctx}/tji/job/get?taskIds=${tasks.taskId}','800px', '500px','${tasks.taskId}','','${tasks.typeName}')"  class="alink"> 调度设置</a>
    				</shiro:hasPermission>
					<span class="gap-line">|</span>
					<c:if test="${tasks.taskType=='1'}">
						<a href="#" class="alink noPermissions" >修改 </a>
						<span class="gap-line">|</span>
					</c:if>
    				<c:if test="${tasks.taskType=='2'}">
	    				<shiro:hasPermission name="tji:job:add">
	    					<a href="#" onclick="openDialog('ETL任务配置修改', '${ctx}/tji/task/set-etl?taskId=${tasks.taskId}','900px', '600px')" class="alink" >修改 </a>
	    				</shiro:hasPermission>
	    				<span class="gap-line">|</span>
    				</c:if>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	</div>
	<!-- 分页代码 -->
	<table:page page="${page}"></table:page>
	</div>
	</div>
</div>

<div id="importId" class="hide">
	<form id="importForm" class="form-padding" action="${ctx}/tji/task/import" method="post" enctype="multipart/form-data" onsubmit="loading('正在导入，请稍等...');">
		<table class="table table-bordered  table-condensed dataTables-example no-footer table-inform" style="margin-bottom: 0;">
			<colgroup>
				<col style="width:30%">
				<col style="width:70%">
			</colgroup>
		   	<tbody>
				<tr>
					<td class="width-20 active"><label class="pull-right">任务分组：</label></td>
					<td class="width-35">
						<form:select path="task.groupId" class="form-control input-sm required" >
						</form:select>
					</td> 
				</tr>
				<tr>
					<td class="width-20 active"><label class="pull-right">任务文件：</label></td>
					<td class="width-35">
						<input id="uploadFile" name="file" type="file" style="width:330px"/>
						<p class="help-block">（导入文件不能超过2M，仅允许导入“xls”或“xlsx”格式文件！）</p>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form>
</div>

</body>
</html>
