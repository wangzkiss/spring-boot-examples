]<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>角色管理</title>
	<meta name="decorator" content="default"/>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
			<h5>角色列表 </h5>
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
	<form:form id="searchForm" modelAttribute="role" action="${ctx}/sys/role/" method="post" class="form-inline form-box">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<label>角色名称：</label>
			<form:input path="name" value="${role.name}"  htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
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
			<shiro:hasPermission name="sys:role:add">
				<table:addRow url="${ctx}/sys/role/form" title="角色"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="sys:role:edit">
			    <table:editRow url="${ctx}/sys/role/form" id="contentTable"  title="角色"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="sys:role:del">
				<table:delRow url="${ctx}/sys/role/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
	       <!-- <button class="btn btn-white btn-sm toolbar" data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button> -->
			</div>
	</div>
	</div>
	<table id="contentTable" class="table table-bordered table-hover table-condensed dataTables-example dataTable table-fixed table-source table-store-cd table-checkbox">
		<thead>
			<tr>
				<th><input type="checkbox" class="i-checks"></th>
				<th>角色名称</th>
				<th>英文名称</th>
				<th>归属机构</th>
				<th>数据范围</th>
				<shiro:hasPermission name="sys:role:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="role">
				<tr>
					<td class="text-center"><input type="checkbox" id="${role.id}" class="i-checks"></td>
					<td class="text-center"><a href="#" onclick="openDialogView('查看角色', '${ctx}/sys/role/form?id=${role.id}','800px', '500px')">${role.name}</a></td>
					<td class="text-center"><a href="#" onclick="openDialogView('查看角色', '${ctx}/sys/role/form?id=${role.id}','800px', '500px')">${role.enname}</a></td>
					<td class="text-center">${role.office.name}</td>
					<td class="text-center">${fns:getDictLabel(role.dataScope, 'sys_data_scope', '无')}</td>
					<td>
						<shiro:hasPermission name="sys:role:view">
						<a href="#" onclick="openDialogView('查看角色', '${ctx}/sys/role/form?id=${role.id}','800px', '500px')" class="alink" > 查看</a>
						</shiro:hasPermission>
						<span class="gap-line">|</span>
						<shiro:hasPermission name="sys:role:edit">
						<c:if test="${(role.sysData eq fns:getDictValue('是', 'yes_no', '1') && fns:getUser().admin)||!(role.sysData eq fns:getDictValue('是', 'yes_no', '1'))}">
							<a href="#" onclick="openDialog('修改角色', '${ctx}/sys/role/form?id=${role.id}','800px', '500px')" class="alink" >修改</a>
						</c:if>
						</shiro:hasPermission>
						<span class="gap-line">|</span>
						<shiro:hasPermission name="sys:role:del">
						<a href="${ctx}/sys/role/delete?id=${role.id}" onclick="return confirmx('确认要删除该角色吗？', this.href)" class="alink">删除</a>
						</shiro:hasPermission>
						<span class="gap-line">|</span>
						<shiro:hasPermission name="sys:role:assign">
						<a href="#" onclick="openDialog('权限设置', '${ctx}/sys/role/auth?id=${role.id}','350px', '500px')" class="alink" >权限设置</a>
						</shiro:hasPermission>
						<span class="gap-line">|</span>
						<shiro:hasPermission name="sys:role:assign">
						<a href="#" onclick="openDialogView('分配用户', '${ctx}/sys/role/assign?id=${role.id}','800px', '500px')"  class="alink" >分配用户</a>
						</shiro:hasPermission>
						<span class="gap-line">|</span>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	</div>
	</div>
	</div>
</body>
</html>