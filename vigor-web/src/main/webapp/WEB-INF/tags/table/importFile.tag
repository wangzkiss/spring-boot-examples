<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ attribute name="url" type="java.lang.String" required="true"%>
<%-- 使用方法： 1.将本tag写在查询的form之前；2.传入controller的url --%>
<button id="btnImport" class="btn btn-white btn-sm toolbar"
	data-toggle="tooltip" data-placement="left" title="上传">
	<i class="fa fa-folder-open-o"></i> 上传
</button>
<div id="importBox" class="hide">
	<form id="importForm" action="${url}" method="post" class="form-padding"
		enctype="multipart/form-data"
		onsubmit="loading('正在上传，请稍等...');">

		<table
			class="table table-bordered  table-condensed table-inform">
			<colgroup>
				<col style="width:20%;">
				<col style="width:80%;">
			</colgroup>
			<tbody>
				<tr>
					<td><i class="star">*</i>包文件：</td>
					<td>
						<input id="uploadFile" name="file" onchange="jarChange(this)" type="file" style="width: 100%" />
						<span class="help-block">上传文件不能超过200M，仅允许上传“jar”格式文件</span>
					</td>
				</tr>
				<tr>
					<td>描述：</td>
					<td>
						<!-- <input name="remarks" class="form-control"/> -->
						<textarea maxlength="644" name="remarks" rows="2" class="form-control"></textarea>
					</td>
				</tr>
				<!-- <tr>
					<td class="width-15 active"><label class="pull-right"><font
							color="red">*</font>包路径</label></td>
					<td class="width-35"><input name="path"></td>
				</tr>
				<tr>
				<td class="width-15 active"><label class="pull-right">jar类型</label></td>
					<td class="width-35">
					<select name="jarType" class="form-control ">
							<option value="1" >系统自带</option>
							<option value="2" >自定义</option>				
					</select>
				</td>
				</tr> -->
			</tbody>
		</table>
	</form>
</div>
<script type="text/javascript">
	$(document).ready(function() {
		$("#btnImport").click(function() {
			top.layer.open({
				type : 1,
				area : [ 500, 340 ],
				title : "上传数据",
				content : $("#importBox").html(),
				btn : [ '确定', '关闭' ],
				btn1 : function(index, layero) {
					var inputForm = top.$("#importForm");
					var top_iframe = top.getActiveTab().attr("name");//获取当前active的tab的iframe 
					inputForm.attr("target", top_iframe);//表单提交成功后，从服务器返回的url在当前tab中展示

					top.$("#importForm").submit();
					top.layer.close(index);
				},
				btn2 : function(index) {
					top.layer.close(index);
				}
			});
		});
	});
</script>