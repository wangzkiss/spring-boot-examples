<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>机构管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  // if(validateForm.form()){
			 //  $("#inputForm").submit();
			 //  // return true;
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
<body class="form-padding">
	<form:form id="inputForm" modelAttribute="office" action="${ctx}/sys/office/save" method="post">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<table class="table table-bordered  table-condensed dataTables-example dataTable table-inform">
			<colgroup>
				<col style="width:15%">
				<col style="width:35%">
				<col style="width:15%">
				<col style="width:35%">
			</colgroup>
		   <tbody>
		      <tr>
		         <td>上级机构：</td>
		         <td><sys:treeselect id="office" name="parent.id" value="${office.parent.id}" labelName="parent.name" labelValue="${office.parent.name}"
					title="机构" url="/sys/office/treeData" extId="${office.id}"  cssClass="form-control input-sm" allowClear="${office.currentUser.admin}"/></td>
		         <td><i class="star">*</i>归属区域：</td>
		         <td><sys:treeselect id="area" name="area.id" value="${office.area.id}" labelName="area.name" labelValue="${office.area.name}"
					title="区域" url="/sys/area/treeData" cssClass="form-control input-sm required"/></td>
		      </tr>
		       <tr>
		         <td><i class="star">*</i>机构名称：</td>
		         <td><form:input path="name" htmlEscape="false" maxlength="50" class="form-control input-sm required"/></td>
		         <td>机构编码：</td>
		         <td><form:input path="code" htmlEscape="false" maxlength="50" class="form-control input-sm"/></td>
		      </tr>
		       <tr>
		         <td>机构类型：</td>
		         <td><form:select path="type" class="form-control input-sm">
					<form:options items="${fns:getDictList('sys_office_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select></td>
		         <td>机构级别：</td>
		         <td><form:select path="grade" class="form-control input-sm">
					<form:options items="${fns:getDictList('sys_office_grade')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select></td>
		      </tr>
		      <tr>
		         <td>是否可用：</td>
		         <td><form:select path="useable" class="form-control input-sm">
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
					<span class="help-inline">“是”代表此账号允许登陆，“否”则表示此账号不允许登陆</span></td>
		         <td>主负责人：</td>
		         <td><sys:treeselect id="primaryPerson" name="primaryPerson.id" value="${office.primaryPerson.id}" labelName="office.primaryPerson.name" labelValue="${office.primaryPerson.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="form-control input-sm" allowClear="true" notAllowSelectParent="true"/></td>
		      </tr>
		      <tr>
		         <td>副负责人：</td>
		         <td><sys:treeselect id="deputyPerson" name="deputyPerson.id" value="${office.deputyPerson.id}" labelName="office.deputyPerson.name" labelValue="${office.deputyPerson.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="form-control input-sm" allowClear="true" notAllowSelectParent="true"/></td>
		         <td>联系地址：</td>
		         <td><form:input path="address" htmlEscape="false" maxlength="50" cssClass="form-control input-sm" /></td>
		      </tr>
		      <tr>
		         <td>邮政编码：</td>
		         <td><form:input path="zipCode" htmlEscape="false" maxlength="50" cssClass="form-control input-sm" /></td>
		         <td>负责人：</td>
		         <td><form:input path="master" htmlEscape="false" maxlength="50" cssClass="form-control input-sm" /></td>
		      </tr>
		      <tr>
		         <td>电话：</td>
		         <td><form:input path="phone" htmlEscape="false" maxlength="50" cssClass="form-control input-sm" /></td>
		         <td>传真：</td>
		         <td><form:input path="fax" htmlEscape="false" maxlength="50" cssClass="form-control input-sm" /></td>
		      </tr>
		      <tr>
		         <td>邮箱：</td>
		         <td><form:input path="email" htmlEscape="false" maxlength="50" cssClass="form-control input-sm" /></td>
		         <td>备注：</td>
		         <td><form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="form-control input-sm"/></td>
		      </tr>
	      </tbody>
	      </table>
	</form:form>
</body>
</html>