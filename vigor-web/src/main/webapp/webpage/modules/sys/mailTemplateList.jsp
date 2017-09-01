<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>邮件模板管理</title>
	<meta name="decorator" content="default"/>
	<style>
		fieldset{padding:.35em .625em .75em;margin:0 2px;border:1px solid silver;margin-bottom:0;}
		legend{padding:.5em;border:0;width:auto;font-size: 14px;margin-bottom:0;}
	</style>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>邮件模板列表 </h5>		
			</div>
		    <div class="ibox-content">
			<sys:message content="${message}"/>
				<!-- <div class="panel panel-info"> -->
					<!-- <div class="panel-body"> -->
						<c:forEach items="${page.list}" var="mailTemplate">
							<p>${mailTemplate.mtContent}</p>
							<input type="radio" id="mtId" hidden="true" value="${mailTemplate.mtId}">
						</c:forEach>
					<!-- <button class="floatRight confirm" id="confirm">确认</button> -->
					<!-- </div> -->
				<!-- </div> -->
			</div>
		</div>
	</div>
<script type="text/javascript">
		function submit(){
			var confirm = $("#confirm");
			var mtId = $("#mtId").val();
			var radio =  $('input[name="checks"]:checked').val()
			confirm.on('click', function(){
				$.ajax({
					type: 'post',
					data: {
						mtId: mtId,
						mtType: 1,
					},
					url: '${ctx}/sys/mailtemplate/pitchon',
					success: function(msg){
						// console.log(msg)
						top.layer.alert(msg);
					}
				})
			})
		}
		submit();
</script>
</body>
</html>