<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ attribute name="url" type="java.lang.String" required="true"%>
<%@ attribute name="title" type="java.lang.String" required="true"%>
<%@ attribute name="width" type="java.lang.String" required="false"%>
<%@ attribute name="height" type="java.lang.String" required="false"%>
<%@ attribute name="target" type="java.lang.String" required="false"%>
<%@ attribute name="label" type="java.lang.String" required="false"%>
<button class="btn btn-white btn-sm toolbar" data-toggle="tooltip" data-placement="left" onclick="add()" title="添加"><i class="fa fa-plus"></i> ${label==null?'添加':label}</button>
<%-- 使用方法： 1.将本tag写在查询的form之前；2.传入table的id和controller的url --%>
<script type="text/javascript">
	
	//打开对话框(添加修改)
	function openDialogCon(title,url,width,height,target){
		top.layer.open({
		    type: 2,  
		    area: [width, height],
		    title: title,
	        maxmin: true, //开启最大化最小化按钮
		    content: url ,
		    btn: ['确定','关闭', '测试连接'],
		    yes: function(index, layero){
		    	 var body = top.layer.getChildFrame('body', index);
             var iframeWin = layero.find('iframe')[0]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
             var inputForm = body.find('#inputForm');
             var top_iframe;
             if(target){
               top_iframe = target;//如果指定了iframe，则在改frame中跳转
             }else{
               top_iframe = top.getActiveTab().attr("name");//获取当前active的tab的iframe 
             }
             var iframeid="iframe"+Date.now();
             var iframe=$('<iframe id="'+iframeid+'" style="display:none" ></iframe>');
             iframe.attr('name',iframeid);
             body.append(iframe);
             inputForm.attr("target",iframeid);//表单提交成功后，从服务器返回的url在当前tab中展示
            iframe.on("load error",function(){
                top.layer.close(index);
                //iframeWin.contentWindow.closeTip&&iframeWin.contentWindow.closeTip();
                iframe.remove();
                setTimeout(function(){
                  window.location.reload();
                }, 1000)

            });
            iframeWin.contentWindow.doSubmit();
				
			  },
			  btn3: function(index, layero){ 
			    	 var body = top.layer.getChildFrame('body', index);
			         var ip = body.find("input[name='ip']").val();
			         var userName = body.find("input[name='userName']").val();
			         var userPwd = body.find("input[name='userPwd']").val();
			         var port = body.find("input[name='port']").val();
			         var repoName = body.find("input[name='repoName']").val();
			         var repoType = body.find("select[name='repoType']").val(); 
			         if(ip&&userName&&userPwd&&port&&repoName&&repoType)
			         {
			         	
			         	  var url="${ctx}/repo/RepoSource/testConData";
			         	  var loadi = top.layer.load('连接中…'); 
				         $.ajax({ type : "POST", url : url, data : {
				        	 "ip":ip,"userName":userName,"userPwd":userPwd,
				        	 "port":port,"repoName":repoName,"repoType":repoType
				         }, 
				        	 dataType:"text",
				             success : function(msg) {
					             var data = $.parseJSON(msg);
					             top.layer.close(loadi);
					             if(data==null)
					             {
					                 top.layer.alert("请求未响应！无数据");
					                 return;
					             }   
					             if (data.msg) {
					             	//messageBox.html(data.msg);
					                top.layer.alert(data.msg);
					             }
				        	 }
				    	 });  
			         }else{
			         	 top.layer.alert("必填字段不能为空！");
			         }
			        
			    }
		}); 	
		
	}
	function add(){
		openDialogCon("新增"+'${title}',"${url}","${width==null?'800px':width}", "${height==null?'500px':height}","${target}");
	}
	function validateData()
	{

	}
</script>