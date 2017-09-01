<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>区域管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  // if(validateForm.form()){
			 //  $("#inputForm").submit();
			 //  return true;
		  // }
			$("#inputForm").submit();
		  // return false;
		}
		$(document).ready(function() {
			$("#name").focus();
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					// form.submit();
					return true;
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
	</script>
</head>
<body class="form-padding  pace-done">
	<form:form id="inputForm" modelAttribute="area" action="${ctx}/sys/area/save" method="post">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<table class="table table-bordered table-condensed table-inform">
			<colgroup>
				<col style="width:15%">
				<col style="width:35%">
				<col style="width:15%">
				<col style="width:35%">
			</colgroup>
			<tbody>
				<tr>
					<td>上级区域：</td>
					<td><sys:treeselect id="area" name="parent.id" value="${area.parent.id}" labelName="parent.name" labelValue="${area.parent.name}"
					title="区域" url="/sys/area/treeData" extId="${area.id}" cssClass="form-control input-sm m-s" allowClear="true"/></td>
					<td>区域名称：</td>
					<td><form:input path="name" htmlEscape="false" maxlength="50" class="form-control input-sm required"/></td>
				</tr>
				<tr>
					<td>区域编码：</td>
					<td><form:input path="code" htmlEscape="false" maxlength="50" class="form-control input-sm"/></td>
					<td>区域类型：</td>
					<td>
						<form:select path="type" class="form-control input-sm">
							<form:options items="${fns:getDictList('sys_area_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td>备注：</td>
					<td colspan="3"><form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="form-control input-sm"/></td>
				</tr>
			</tbody>
		</table>
	</form:form>
</body>
</html>