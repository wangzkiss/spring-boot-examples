<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>个人信息</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {

			$("#userPassWordBtn").click(function(){
				top.layer.open({
				    type: 2, 
				    area: ['600px', '350px'],
				    title:"修改密码",
				    content: "${ctx}/sys/user/modifyPwd" ,
				    btn: ['确定', '关闭'],
				    yes: function(index, layero){
				    	 var body = top.layer.getChildFrame('body', index);
				         var inputForm = body.find('#inputForm');
				         var btn = body.find('#btnSubmit');
				         var top_iframe = top.getActiveTab().attr("name");//获取当前active的tab的iframe 
				         inputForm.attr("target",top_iframe);//表单提交成功后，从服务器返回的url在当前tab中展示
				         inputForm.validate({
								rules: {
								},
								messages: {
									confirmNewPassword: {equalTo: "输入与上面相同的密码"}
								},
								submitHandler: function(form){
									loading('正在提交，请稍等...');
									form.submit();
									
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
					     if(inputForm.valid()){
				        	  loading("正在提交，请稍等...");
				        	  inputForm.submit();
				        	  top.layer.close(index);//关闭对话框。
				          }else{
					          return;
				          }
						
						
					  },
					  cancel: function(index){ 
		    	       }
				}); 
			});
			
			$("#userInfoBtn").click(function(){
				top.layer.open({
				    type: 2,  
				    area: ['600px', '520px'],
				    title:"个人信息编辑",
				    content: "${ctx}/sys/user/infoEdit" ,
				    btn: ['确定', '关闭'],
				    yes: function(index, layero){
				    	 var body = top.layer.getChildFrame('body', index);
				         var inputForm = body.find('#inputForm');
				         var top_iframe = top.getActiveTab().attr("name");//获取当前active的tab的iframe 
				         inputForm.attr("target",top_iframe);//表单提交成功后，从服务器返回的url在当前tab中展示
				         inputForm.validate();
				         if(inputForm.valid()){
				        	  loading("正在提交，请稍等...");
				        	  inputForm.submit();
				          }else{
					          return;
				          }
				        
						 top.layer.close(index);//关闭对话框。
						
					  },
					  cancel: function(index){ 
		    	       }
				}); 
			});

			$("#userImageBtn").click(function(){
				top.layer.open({
				    type: 2,  
				    area: ['700px', '500px'],
				    title:"上传头像",
				    content: "${ctx}/sys/user/imageEdit" ,
				  //  btn: ['确定', '关闭'],
				    yes: function(index, layero){
				    	 var body = top.layer.getChildFrame('body', index);
				         var inputForm = body.find('#inputForm');
				         var top_iframe = top.getActiveTab().attr("name");//获取当前active的tab的iframe 
				         inputForm.attr("target",top_iframe);//表单提交成功后，从服务器返回的url在当前tab中展示
				         inputForm.validate();
				         if(inputForm.valid()){
				        	  loading("正在提交，请稍等...");
				        	  inputForm.submit();
				          }else{
					          return;
				          }
				        
						 top.layer.close(index);//关闭对话框。
						
					  },
					  cancel: function(index){ 
		    	       }
				}); 
			});
			
		});
	</script>
</head>
<body>
	<div class="wrapper wrapper-content animated fadeInRight">
		<sys:message hideType="1" content="${message}"/>
		<div class="ibox float-e-margins">
			<div class="ibox-title">
				<h5>个人资料</h5>
				<div class="ibox-tools">
					<a class="dropdown-toggle" data-toggle="dropdown" href="#"><i class="fa fa-wrench"></i></a>
					<ul class="dropdown-menu dropdown-user">
						<li><a id="userImageBtn" data-toggle="modal" data-target="#register">更换头像</a></li>
						<li><a id="userInfoBtn" data-toggle="modal" data-target="#register">编辑资料</a></li>
						<!-- <li><a id="userPassWordBtn" data-toggle="modal" data-target="#register">更换密码</a></li> -->
						<!-- <li><a href="#" data-toggle="modal" data-target="#register">更换手机号</a></li> -->
					</ul>
				</div>
			</div>
			<div class="ibox-content">
				<img alt="image" class="img-responsive" src="${user.photo }" />
				<table class="table table-bordered table-inform">
					<colgroup>
						<col style="width:15%">
						<col style="width:35%">
						<col style="width:15%">
						<col style="width:35%">
					</colgroup>
					<tbody>
						<tr>
							<td>姓名：</td>
							<td>${user.name}</td>
							<td>邮箱：</td>
							<td>${user.email}</td>
						</tr>
						<tr>
							<td>手机：</td>
							<td>${user.mobile}</td>
							<td>电话：</td>
							<td>${user.phone}</td>
						</tr>
						<tr>
							<td>公司：</td>
							<td>${user.company.name}</td>
							<td>部门：</td>
							<td>${user.office.name}</td>
						</tr>
						<tr>
							<td>用户角色：</td>
							<td>${user.roleNames}</td>
							<td>用户类型：</td>
							<td>${fns:getDictLabel(user.userType, 'sys_user_type', '无')}</td>
						</tr>
						<tr>
							<td>用户名：</td>
							<td>${user.loginName}</td>
							<!-- <td>注册手机号码：</td>
							<td>${user.mobile}</td> -->
							<td>备注：</td>
							<td colspan="3">${user.remarks}</td>
						</tr>
						<!-- <tr>
							
						</tr> -->
					</tbody>
				</table>
				<p>上次登录IP: ${user.oldLoginIp}&nbsp;&nbsp;&nbsp;&nbsp;时间：<fmt:formatDate value="${user.oldLoginDate}" type="both" dateStyle="full"/></p>
			</div>
		</div>
	</div>
</body>
</html>