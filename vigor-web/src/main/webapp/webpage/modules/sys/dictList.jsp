<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>字典管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content">
<div class="ibox">
<div class="ibox-title">
		<h5>字典列表 </h5>
		<!-- <div class="ibox-tools">
			<a class="collapse-link">
				<i class="fa fa-chevron-up"></i>
			</a>
			<a class="dropdown-toggle" data-toggle="dropdown" href="form_basic.html#">
				<i class="fa fa-wrench"></i>
			</a>
			<ul class="dropdown-menu dropdown-user">
				<li><a href="#">选项1</a>
				</li>
				<li><a href="#">选项2</a>
				</li>
			</ul>
			<a class="close-link">
				<i class="fa fa-times"></i>
			</a>
		</div> -->
	</div>
    <div class="ibox-content">
	<sys:message content="${message}"/>
	<!-- 查询条件 -->
	<form:form id="searchForm" modelAttribute="dict" action="${ctx}/sys/dict/" method="post" class="form-inline form-box">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<label>类型：</label>
			<form:select id="type" path="type" class="form-control input-sm m-b">
			<form:option value="" label="全部"/><form:options items="${typeList}" htmlEscape="false"/>
			</form:select>
		</div>
		<div class="form-group">
			<label>描述：</label>
			<form:input path="description" htmlEscape="false" maxlength="50" class="form-control input-sm"/>
		</div>
		<div class="btn-form-box">
			<button  class="btn btn-success btn-rounded btn-sm filter" onclick="search()" ><i class="fa fa-search"></i> 查询</button>
			<a  class="btn btn-success btn-rounded btn-sm btn-outline filterReset" onclick="reset()" ><i class="fa fa-refresh"></i> 重置</a>
		</div>
	</form:form>
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="sys:dict:add">
				<table:addRow url="${ctx}/sys/dict/form" title="字典"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="sys:dict:edit">
			    <table:editRow url="${ctx}/sys/dict/form" id="contentTable"  title="字典"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="sys:dict:del">
				<table:delRow url="${ctx}/sys/dict/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
	       <!-- <button class="btn btn-white btn-sm toolbar" data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button> -->
			</div>
	</div>
	</div>
	<table id="contentTable" class="table table-bordered table-hover table-condensed dataTables-example dataTable table-fixed table-source table-store-cd table-checkbox">
		<colgroup>
			<col style="width:5%">
			<col style="width:8%">
			<col style="width:15%">
			<col style="width:20%">
			<col style="width:20%">
			<col style="width:7%">
			<col style="width:25%">
		</colgroup>
		<thead>
			<tr>
				<th> <input type="checkbox" class="i-checks"></th>
				<th  class="sort-column value">键值</th>
				<th >标签</th>
				<th  class="sort-column type">类型</th>
				<th  class="sort-column description">描述</th>
				<th  class="sort-column sort">排序</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="dict">
			<tr>
				<td class="text-center"><input type="checkbox" id="${dict.id}" class="i-checks"></td>
				<td class="text-center">${dict.value}</td>
				<td class="text-center"><a  href="#" onclick="openDialogView('查看字典', '${ctx}/sys/dict/form?id=${dict.id}','800px', '500px')">${dict.label}</a></td>
				<td class="text-center"><a href="javascript:" onclick="$('#type').val('${dict.type}');$('#searchForm').submit();return false;">${dict.type}</a></td>
				<td class="text-center">${dict.description}</td>
				<td class="text-center">${dict.sort}</td>
				<td>
					<shiro:hasPermission name="sys:dict:view">
						<a href="#" onclick="openDialogView('查看字典', '${ctx}/sys/dict/form?id=${dict.id}','800px', '500px')" class="alink" >查看</a>
					</shiro:hasPermission>
					<span class="gap-line">|</span>
					<shiro:hasPermission name="sys:dict:edit">
    					<a href="#" onclick="openDialog('修改字典', '${ctx}/sys/dict/form?id=${dict.id}','800px', '500px')" class="alink" >修改</a>
    				</shiro:hasPermission>
					<span class="gap-line">|</span>
    				<shiro:hasPermission name="sys:dict:del">
						<a href="${ctx}/sys/dict/delete?id=${dict.id}&type=${dict.type}" onclick="return confirmx('确认要删除该字典吗？', this.href)" class="alink">删除</a>
					</shiro:hasPermission>
					<span class="gap-line">|</span>
					<shiro:hasPermission name="sys:dict:add">
						<a href="#" onclick="openDialog('添加键值', '<c:url value='${fns:getAdminPath()}/sys/dict/form?type=${dict.type}&sort=${dict.sort+10}'><c:param name='description' value='${dict.description}'/></c:url>','800px', '500px')" class="alink" >添加键值</a>
					</shiro:hasPermission>
					<span class="gap-line">|</span>
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