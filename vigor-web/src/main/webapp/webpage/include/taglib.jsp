<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fns" uri="/WEB-INF/tlds/fns.tld" %>
<%@ taglib prefix="sys" tagdir="/WEB-INF/tags/sys" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="t" uri="/menu-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}${fns:getAdminPath()}"/>
<c:set var="ctxStatic" value="${pageContext.request.contextPath}/static"/>
<c:set var="ctxWeb" value="${pageContext.request.contextPath}/webpage"/>

<script type="text/javascript">
    
function testCon(dataId)
{
    var url="${ctx}/repo/RepoSource/testCon?id="+dataId;
    var loadi = layer.load('连接中…'); //需关闭加载层时，执行layer.close(loadi)即可
    $.ajax({ type : "POST", url : url, data : "", dataType:"text",
        success : function(msg) {
        layer.close(loadi);
        var data = $.parseJSON(msg);
        if(data==null)
        {
            top.layer.alert("请求未响应！无数据");
            return;
        }   
        if (data.msg) {
           top.layer.alert(data.msg);
        }
    }
});         
}

/*   op 1 启动  2 停止  type  1 集群  2 节点*/
function clusterOp(dataId,op,type)
{
    var opUrl=op==1?"etlstart":"etlstop";
    var url="${ctx}/server/platform/"+opUrl+"?id="+dataId+"&type="+type;
    var loadi = layer.load('启动中…'); //需关闭加载层时，执行layer.close(loadi)即可
    $.ajax({ type : "POST", url : url, data : "", dataType:"text",
        success : function(msg) {
        layer.close(loadi);
          var data = $.parseJSON(msg);
          if(data==null)
          {
              top.layer.alert("请求未响应！");
              return;
          }   
          if (data.msg) {
             top.layer.alert(data.msg);
              window.location = "${ctx}/server/platform/etl";
          }
      }
  });         
}
function dataEnable(url,dataId)
{
    var pageNo = $("#pageNo").val();
    var redicturl="${ctx}"+url;
    var url="${ctx}"+url+"enable?id="+dataId;
    var loadi = layer.load('启动中…'); 
    $.ajax({ type : "POST", url : url, data : "", dataType:"text",
     success : function(msg) {
         layer.close(loadi);
        var data = $.parseJSON(msg);
        if(data==null)
        {
            top.layer.alert("请求未响应!");
            return;
        }   
        if (data.msg) {
           top.layer.alert(data.msg);
         // window.location = redicturl;
          // search();
          var newUrl = redicturl.substring(0, redicturl.length-1 )
          //window.location = ""+newUrl+"?pageNo=" + pageNo;
          $("#searchForm").submit();
        }
    }
    });
}
function dataDisable(url_1,dataId)
{
	var pageNo = $("#pageNo").val();
    var loadi = layer.load('取消启动中…');
    var redicturl="${ctx}"+url_1;
    var url="${ctx}"+url_1+"disable?id="+dataId;
   
    $.ajax({ type : "POST", url : url, data : "", dataType:"text",
     success : function(msg) {
         layer.close(loadi);
        var data = $.parseJSON(msg);
        if(data==null)
        {
            top.layer.alert("请求未响应!");
            return;
        }   
        if (data.msg) {
           top.layer.alert(data.msg);
          // window.location = redicturl;
          // window.location = "${ctx}/store/metaStore?pageNo=" + pageNo;
           // search();
          var newUrl = redicturl.substring(0, redicturl.length-1 )
          //window.location = ""+newUrl+"?pageNo=" + pageNo;
          $("#searchForm").submit();
        }
    }
    });
	
} 
</script>
