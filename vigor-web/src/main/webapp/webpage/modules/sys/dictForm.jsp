<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>字典管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
  			$("#inputForm").submit();		
		}
		$(document).ready(function() {
			$("#value").focus();
			 validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					//form.submit();
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
<body class="form-padding">
	<form:form id="inputForm" modelAttribute="dict" action="${ctx}/sys/dict/save" method="post">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer table-inform">
			<colgroup>
				<col style="width:15%">
				<col style="width:35%">
				<col style="width:15%">
				<col style="width:35%">
			</colgroup>
		   	<tbody>
				<tr>
					<td>键值：</td>
					<td><form:input path="value" htmlEscape="false" maxlength="50" class="form-control input-sm required"/></td>
					<td>标签：</td>
					<td><form:input path="label" htmlEscape="false" maxlength="50" class="form-control input-sm required"/></td>
				</tr>
				<tr>
					<td>类型：</td>
					<td><form:input path="type" htmlEscape="false" maxlength="50" class="form-control input-sm required abc"/></td>
					<td>描述：</td>
					<td><form:input path="description" htmlEscape="false" maxlength="50" class="form-control input-sm required"/></td>
				</tr>
				<tr>
					<td>排序：</td>
					<td><form:input path="sort" htmlEscape="false" maxlength="11" class="form-control input-sm required digits"/></td>
					<td>备注：</td>
					<td><form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="form-control input-sm "/></td>
				</tr>
		   </tbody>
		</table>   
	</form:form>
</body>
</html>