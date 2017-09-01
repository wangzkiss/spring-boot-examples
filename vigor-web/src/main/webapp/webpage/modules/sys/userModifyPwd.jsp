<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>修改密码</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#oldPassword").focus();
			// $("#btnSubmit").click(function(){
			// 	var oldPassword = $("#oldPassword").val();
			// 	var newPassword = $("#newPassword").val();
			// 	var confirmNewPassword = $("#confirmNewPassword").val();
			// 	console.log(oldPassword)

			// 	$("#inputForm").ajaxSubmit({
			// 		type: 'post', // 提交方式 get/post
		 //            url: '${ctx}/sys/user/modifyPwd', // 需要提交的 url
		 //            async: true,  
		 //          	cache: false,  
		 //          	contentType: false,  
		 //          	processData: false,
		 //          	dataType:"text",
		 //          	success: function(json){
		 //          		console.log(json)
		 //          	}
			// 	})

			// })
		});
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="user" action="${ctx}/sys/user/modifyPwd"  method="post" class="form-horizontal form-group">
		<form:hidden path="id"/>
		<sys:message hideType="1" content="${message}"/>
		<div class="control-group">
		</div>
		
		<div class="control-group">
			<label class="col-sm-3 control-label"><font color="red">*</font>旧密码:</label>
			<div class="controls">
				<input id="oldPassword" onkeydown="return(event.keyCode!=13)" name="oldPassword" type="password" value="" maxlength="50" minlength="3"  class="form-control  max-width-250 required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="col-sm-3 control-label"><font color="red">*</font>新密码:</label>
			<div class="controls">
				<input id="newPassword" onkeydown="return(event.keyCode!=13)" name="newPassword" type="password" value="" maxlength="50" minlength="3" class="form-control  max-width-250 required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="col-sm-3 control-label"><font color="red">*</font>确认新密码:</label>
			<div class="controls">
				<input id="confirmNewPassword" onkeydown="return(event.keyCode!=13)" name="confirmNewPassword" type="password" value="" maxlength="50" minlength="3" class="form-control  max-width-250 required" equalTo="#newPassword"></input>
			</div>
		</div>
		<div class="control-group">
			<label class="col-sm-3 control-label"></label>
			<div class="form-actions">
				<input id="btnSubmit" class="btn btn-primary" style="display: none;" type="submit" value="保 存"/>
			</div>
		</div>
	</form:form>
</body>
</html>