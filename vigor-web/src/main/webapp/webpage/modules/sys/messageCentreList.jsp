<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>邮件中心</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			var start = {
			    elem: '#startTime',
			    format: 'YYYY-MM-DD hh:mm:ss',
			    // min: laydate.now(), 
			    istime: true,
			    event: 'focus',
			    choose: function(datas){
			         end.min = datas; //开始日选好后，重置结束日的最小日期
			         end.start = datas //将结束日的初始值设定为开始日
			    }
			};
			var end = {
			    elem: '#endTime',
			    format: 'YYYY-MM-DD hh:mm:ss',
			    min: laydate.now(),
			    istime: true,
			    event: 'focus',
			    choose: function(datas){
			        start.max = datas; //结束日选好后，重置开始日的最大日期
			    }
			};
			laydate(start);
			laydate(end);
			

	        $("#contentTable tbody td:nth-child(2)").each(function(i){
                //给td设置title属性,并且设置td的完整值.给title属性.
                $(this).attr("title",$(this).text());

	      	});
		
		});
		
		function batchAgain(){
			var str="";
		    var ids="";
		    $("#contentTable tbody tr td input.i-checks:checkbox").each(function(){
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
		    
		   if(confirm('确认要审核选中作业吗？'))
		   {
			   $.ajax({
					type:"post",
			        url: "${ctx}/sys/messagecentre/status",
			        data:"ids="+ids,
			        success: function (data) {
			    	   	top.layer.alert(data);
			    	 	sortOrRefresh();
			        }
				});
		   }
		}
		
		function again(messageId){
		    
		   if(confirm('确认要重发该邮件吗？'))
		   {
			   $.ajax({
					type:"post",
			        url: "${ctx}/sys/messagecentre/status",
			        data:"ids="+messageId,
			        success: function (data) {
			    	   	top.layer.alert(data);
			    	 	sortOrRefresh();
			        }
				});
		   }
		}
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>邮件列表 </h5>
		<div class="ibox-tools">
		</div>
	</div>
    
    <div class="ibox-content">
	<sys:message content="${message}"/>
	
	<!--查询条件-->
	<form:form id="searchForm" modelAttribute="messageCentre" action="${ctx}/sys/messagecentre/list/" method="post" class="form-inline form-box">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<label>状态：</label>
			<form:select path="messageStatus" id="messageStatus" name="messageStatus" class="form-control input-sm required">
				<form:option value="-2">全部</form:option> 
				<form:option value="-1">未发送</form:option>
				<form:option value="0">发送失败</form:option>
				<form:option value="1">发送成功</form:option>
			</form:select>
		</div>
		<div class="form-group">
			<label>开始时间：</label>
			<input id="startTime" name="startTime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${messageCentre.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
		</div>
		<div class="form-group">
			<label>结束时间：</label>
			<input id="endTime" name="endTime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${messageCentre.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>	
		</div>
		<div class="btn-form-box">
			<button  class="btn btn-success btn-rounded btn-sm filter" onclick="search()" value="33"><i class="fa fa-search"></i> 查询</button>
			<a  class="btn btn-success btn-rounded btn-sm btn-outline filterReset" onclick="reset()" ><i class="fa fa-refresh"></i> 重置</a>
		</div>
	</form:form>
	
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="sys:messagecentre:status">
				<button class="btn btn-white btn-sm toolbar" data-toggle="tooltip" data-placement="left" onclick="batchAgain()" title="重发"><i class="fa fa-file-text-o"></i> 重发</button>
			</shiro:hasPermission>
		</div>
	</div>
	</div>
	
	<!-- 表格 -->
	<table id="contentTable" class="table table-bordered table-hover table-condensed dataTables-example dataTable table-fixed table-source table-store-etl table-checkbox">
		<colgroup>
			<col style="width:5%">
			<col style="width:30%">
			<col style="width:25%">
			<col style="width:10%">
			<col style="width:20%">
			<col style="width:10%">
		</colgroup>
		<thead>
			<tr>
				<th><input type="checkbox" class="i-checks ichecks-all"></th>
				<th>邮件标题</th>
				<th>邮件接收人</th>
				<th>邮件状态</th>
				<th>完成时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="messageCentre">
			<tr>
				<td class="text-center"><input type="checkbox" id="${messageCentre.messageId}" class="i-checks"></td>
				<td class="viewBig">
					${messageCentre.messageTitle}
				</td>
				<td>
					${messageCentre.receiveNum}
				</td>
				<td class="text-center">
					<c:choose>
					  <c:when test="${messageCentre.messageStatus==-1}">   
					   	 未发送
					  </c:when> 
					   <c:when test="${messageCentre.messageStatus==0}">   
					   	 发送失败
					  </c:when>
					  <c:otherwise>   
					    发送成功 
					  </c:otherwise> 
					</c:choose>
				</td>
				<td class="text-center">
					<fmt:formatDate value="${messageCentre.finishTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td class="text-center">
					<shiro:hasPermission name="sys:messagecentre:status">
						<a href="#" onclick="again(${messageCentre.messageId})" class="alink" >重发</a>
					</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
		<!-- 分页代码 -->
	<table:page page="${page}"></table:page>
	</div>
	</div>
</div>
</body>
</html>
