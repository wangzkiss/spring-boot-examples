<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>个人信息</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		// var validateForm;
		// function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		//   if(validateForm.form()){
		// 	  $("#inputForm").submit();
		// 	  return true;
		//   }
		//   return false;
		// }
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="user" action="${ctx}/sys/user/infoEdit"  method="post" class="form-horizontal form-group">
		<div class="control-group">
		</div>
		<div class="control-group">
			<label class="col-sm-3 control-label">姓名:</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="50"  class="form-control  max-width-250 required" />
			</div>
		</div>
		<div class="control-group">
			<label class="col-sm-3 control-label">邮箱:</label>
			<div class="controls">
				<form:input path="email" htmlEscape="false" maxlength="50" class="form-control  max-width-250 email"/>
			</div>
		</div>
		<div class="control-group">
			<label class="col-sm-3 control-label">电话:</label>
			<div class="controls">
				<form:input path="phone" htmlEscape="false" class="form-control  max-width-250 phone_var" maxlength="50"/>
			</div>
		</div>
		<div class="control-group">
			<label class="col-sm-3 control-label">手机:</label>
			<div class="controls">
				<form:input path="mobile" class="form-control  max-width-250 required mobile_var" htmlEscape="false" maxlength="50"/>
			</div>
		</div>
		<div class="control-group">
			<label class="col-sm-3 control-label">备注:</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="form-control  max-width-250 "/>
			</div>
		</div>
	</form:form>
</body>
</html>