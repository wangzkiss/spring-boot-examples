<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>用户管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/webpage/include/treeview.jsp" %>
	<style type="text/css">
		.ibox {margin-bottom: 0;}
	</style>
	<script type="text/javascript">
		function refresh(){//刷新
			
			window.location="${ctx}/sys/user/index";
		}
	</script>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="ibox">
		<div class="ibox-title">
			<h5>用户列表</h5>
		</div>
		<div class="ibox-content">
			<sys:message content="${message}"/>
			<div id="content">
				<div class="row">
					<div class="col-sm-4 col-md-3">
						<div class="ibox">
							<div class="ibox-title">
								<h5>组织架构</h5>
								<div class="ibox-tools">
									<a onclick="refresh()" title="刷新"><i class="fa fa-refresh"></i></a>
								</div>
								
							</div>
							<div class="ibox-content">
								<ul id="ztree" class="ztree" style="height:calc(100% - 200px); overflow:auto;"></ul>
							</div>
						</div>
					</div>
					<!-- <div id="left" style="background-color:#fff; margin-top: 10px;margin-left: 10px" class="col-sm-1"></div> -->
					<div class="col-sm-8 col-md-9">
						<iframe id="officeContent" name="officeContent" src="${ctx}/sys/user/list" width="100%" style="height:calc(100% - 130px);" frameborder="0"></iframe>
					</div>
				</div>
				<!-- <div id="left"  style="background-color:#fff; margin-top: 30px;margin-left: 10px" class="leftBox col-sm-1">
					<a onclick="refresh()" class="pull-right">
						<i class="fa fa-refresh"></i>
					</a>
					<div id="ztree" class="ztree leftBox-content"></div>
				</div> -->
				<!-- <div id="right"  class="col-sm-11  animated fadeInRight" style="margin-left: -10px">
					<iframe id="officeContent" name="officeContent" src="${ctx}/sys/user/list" width="100%" height="91%" frameborder="0"></iframe>
				</div> -->
			</div>
		</div>
	</div>
	<script type="text/javascript">
		var setting = {data:{simpleData:{enable:true,idKey:"id",pIdKey:"pId",rootPId:'0'}},
			callback:{
				onClick:function(event, treeId, treeNode){
					var id = treeNode.id == '0' ? '' :treeNode.id;
					$('#officeContent').attr("src","${ctx}/sys/user/list?office.id="+id+"&office.name="+treeNode.name);
				},
			}
		};
		
		function refreshTree(){
			$.getJSON("${ctx}/sys/office/treeData",function(data){
				$.each(data, function(i, n){
					if(n.pId == '0'){
						n['open'] = true
					}
				})
				$.fn.zTree.init($("#ztree"), setting, data);
			});
		}
		refreshTree();
		// var leftWidth = 320; // 左侧窗口大小
		// var htmlObj = $("html"), mainObj = $("#main");
		// var frameObj = $("#left, #openClose, #right, #right iframe");
		// function wSize(){
		// 	var strs = getWindowSize().toString().split(",");
		// 	htmlObj.css({"overflow-x":"hidden", "overflow-y":"hidden"});
		// 	mainObj.css("width","auto");
		// 	frameObj.height(strs[0] - 120);
		// 	var leftWidth = ($("#left").width() < 0 ? 0 : $("#left").width());
		// 	$("#right").width($("#content").width()- leftWidth - $("#openClose").width() -60);
		// 	$(".ztree").width(leftWidth - 10).height(frameObj.height() - 46);
		// }
	</script>
	<script src="${ctxStatic}/common/wsize.min.js" type="text/javascript"></script>
</body>
</html>