<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>分配角色</title>
	<meta name="decorator" content="default"/>
</head>
<body class="form-padding">
	<table class="table table-bordered table-condensed table-inform">
		<colgroup>
			<col style="width:15%">
			<col style="width:35%">
			<col style="width:15%">
			<col style="width:35%">
		</colgroup>
		<tbody>
			<tr>
				<td>角色名称：</td>
				<td>${role.name}</td>
				<td>归属机构：</td>
				<td>${role.office.name}</td>
			</tr>
			<tr>
				<td>英文名称：</td>
				<td>${role.enname}</td>
				<td>角色类型：</td>
				<td>${role.roleType}</td>
			</tr>
			<tr>
				<td>数据范围：</td>
				<td>
					<c:set var="dictvalue" value="${role.dataScope}" scope="page" />
					${fns:getDictLabel(dictvalue, 'sys_data_scope', '')}
				</td>
				<td></td>
				<td></td>
			</tr>
		</tbody>
	</table>
	<sys:message content="${message}"/>
	<div class="breadcrumb">
		<form id="assignRoleForm" action="${ctx}/sys/role/assignrole" method="post" class="hide">
			<input type="hidden" name="id" value="${role.id}"/>
			<input id="idsArr" type="hidden" name="idsArr" value=""/>
		</form>
		<button id="assignButton" type="submit"  class="btn btn-white btn-sm toolbar" title="添加人员"><i class="fa fa-plus"></i> 添加人员</button>
		<script type="text/javascript">
			$("#assignButton").click(function(){
				top.layer.open({
				    type: 2, 
				    area: ['800px', '500px'],
				    title:"选择用户",
			        maxmin: true, //开启最大化最小化按钮
				    content: "${ctx}/sys/role/usertorole?id=${role.id}" ,
				    btn: ['确定', '关闭'],
				    yes: function(index, layero){
		    	       var pre_ids = layero.find("iframe")[0].contentWindow.pre_ids;
						var ids = layero.find("iframe")[0].contentWindow.ids;
						if(ids[0]==''){
								ids.shift();
								pre_ids.shift();
							}
							if(pre_ids.sort().toString() == ids.sort().toString()){
								top.$.jBox.tip("未给角色【${role.name}】分配新成员！", 'info');
								return false;
							};
					    	// 执行保存
					    	loading('正在提交，请稍等...');
					    	var idsArr = "";
					    	for (var i = 0; i<ids.length; i++) {
					    		idsArr = (idsArr + ids[i]) + (((i + 1)== ids.length) ? '':',');
					    	}
					    	$('#idsArr').val(idsArr);
					    	$('#assignRoleForm').submit();
						    top.layer.close(index);
					  },
					  cancel: function(index){ 
		    	       }
				}); 
			});
		</script>
	</div>
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTable">
		<colgroup>
			<col>
			<col>
			<col>
			<col>
			<col style="width:15%">
			<col style="width:15%">
			<col style="width:8%">
		</colgroup>
		<thead>
			<tr>
				<th>归属公司</th>
				<th>归属部门</th>
				<th>登录名</th>
				<th>姓名</th>
				<th>电话</th>
				<th>手机</th>
				<shiro:hasPermission name="sys:user:edit">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${userList}" var="user">
			<tr>
				<td>${user.company.name}</td>
				<td>${user.office.name}</td>
				<td class="text-center"><a href="${ctx}/sys/user/form?id=${user.id}">${user.loginName}</a></td>
				<td class="text-center">${user.name}</td>
				<td class="text-center">${user.phone}</td>
				<td class="text-center">${user.mobile}</td>
				<shiro:hasPermission name="sys:role:edit">
				<td class="text-center">
					<a href="${ctx}/sys/role/outrole?userId=${user.id}&roleId=${role.id}" onclick="return confirmx('确认要将用户<b>[${user.name}]</b>从<b>[${role.name}]</b>角色中移除吗？', this.href)">移除</a>
				</td>
				</shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</body>
</html>
