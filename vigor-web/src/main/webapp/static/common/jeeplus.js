/*
 * 通用公共方法
 * @author kiss
 * @version 2014-4-29
 */
$(document).ready(function() {
  try {
    // 链接去掉虚框
    $("a").bind("focus", function() {
      if (this.blur) { this.blur() };
    });
    //所有下拉框使用select2
    $("select").select2();
  } catch (e) {
    // blank
  }
});

// 引入js和css文件
function include(id, path, file) {
  if (document.getElementById(id) == null) {
    var files = typeof file == "string" ? [file] : file;
    for (var i = 0; i < files.length; i++) {
      var name = files[i].replace(/^\s|\s$/g, "");
      var att = name.split('.');
      var ext = att[att.length - 1].toLowerCase();
      var isCSS = ext == "css";
      var tag = isCSS ? "link" : "script";
      var attr = isCSS ? " type='text/css' rel='stylesheet' " : " type='text/javascript' ";
      var link = (isCSS ? "href" : "src") + "='" + path + name + "'";
      document.write("<" + tag + (i == 0 ? " id=" + id : "") + attr + link + "></" + tag + ">");
    }
  }
}

// 获取URL地址参数
function getQueryString(name, url) {
  var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
  if (!url || url == "") {
    url = window.location.search;
  } else {
    url = url.substring(url.indexOf("?"));
  }
  r = url.substr(1).match(reg)
  if (r != null) return unescape(r[2]);
  return null;
}

//获取字典标签
function getDictLabel(data, value, defaultValue) {
  for (var i = 0; i < data.length; i++) {
    var row = data[i];
    if (row.value == value) {
      return row.label;
    }
  }
  return defaultValue;
}

// 打开一个窗体
function windowOpen(url, name, width, height) {
  var top = parseInt((window.screen.height - height) / 2, 10),
    left = parseInt((window.screen.width - width) / 2, 10),
    options = "location=no,menubar=no,toolbar=no,dependent=yes,minimizable=no,modal=yes,alwaysRaised=yes," +
    "resizable=yes,scrollbars=yes," + "width=" + width + ",height=" + height + ",top=" + top + ",left=" + left;
  window.open(url, name, options);
}

// 恢复提示框显示
function resetTip() {
  top.$.jBox.tip.mess = null;
}

// 关闭提示框
function closeTip() {
  top.$.jBox.closeTip();
}

//显示提示框
function showTip(mess, type, timeout, lazytime) {
  resetTip();
  setTimeout(function() {
    top.$.jBox.tip(mess, (type == undefined || type == '' ? 'info' : type), {
      opacity: 0,
      timeout: timeout == undefined ? 2000 : timeout
    });
  }, lazytime == undefined ? 500 : lazytime);
}

// 显示加载框
function loading(mess) {
  if (mess == undefined || mess == "") {
    mess = "正在提交，请稍等...";
  }
  resetTip();
  top.$.jBox.tip(mess, 'loading', { opacity: 0 });
}

// 警告对话框
function alertx(mess, closed) {
  top.$.jBox.info(mess, '提示', {
    closed: function() {
      if (typeof closed == 'function') {
        closed();
      }
    }
  });
  top.$('.jbox-body .jbox-icon').css('top', '55px');
}

// 确认对话框
function confirmx(mess, href, closed) {

  top.layer.confirm(mess, { icon: 3, title: '系统提示' }, function(index) {
    //do something
    if (typeof href == 'function') {
      href();
    } else {
      resetTip(); //loading();
      location = href;
    }
    top.layer.close(index);
  });

  //	top.$.jBox.confirm(mess,'系统提示',function(v,h,f){
  //		if(v=='ok'){
  //			if (typeof href == 'function') {
  //				href();
  //			}else{
  //				resetTip(); //loading();
  //				location = href;
  //			}
  //		}
  //	},{buttonsFocus:1, closed:function(){
  //		if (typeof closed == 'function') {
  //			closed();
  //		}
  //	}});
  //	top.$('.jbox-body .jbox-icon').css('top','55px');
  return false;
}

// 提示输入对话框
function promptx(title, lable, href, closed) {
  top.$.jBox("<div class='form-search' style='padding:20px;text-align:center;'>" + lable + "：<input type='text' id='txt' name='txt'/></div>", {
    title: title,
    submit: function(v, h, f) {
      if (f.txt == '') {
        top.$.jBox.tip("请输入" + lable + "。", 'error');
        return false;
      }
      if (typeof href == 'function') {
        href();
      } else {
        resetTip(); //loading();
        location = href + encodeURIComponent(f.txt);
      }
    },
    closed: function() {
      if (typeof closed == 'function') {
        closed();
      }
    }
  });
  return false;
}

// 添加TAB页面
function addTabPage(title, url, closeable, $this, refresh) {
  top.$.fn.jerichoTab.addTab({
    tabFirer: $this,
    title: title,
    closeable: closeable == undefined,
    data: {
      dataType: 'iframe',
      dataLink: url
    }
  }).loadData(refresh != undefined);
}

// cookie操作
function cookie(name, value, options) {
  if (typeof value != 'undefined') { // name and value given, set cookie
    options = options || {};
    if (value === null) {
      value = '';
      options.expires = -1;
    }
    var expires = '';
    if (options.expires && (typeof options.expires == 'number' || options.expires.toUTCString)) {
      var date;
      if (typeof options.expires == 'number') {
        date = new Date();
        date.setTime(date.getTime() + (options.expires * 24 * 60 * 60 * 1000));
      } else {
        date = options.expires;
      }
      expires = '; expires=' + date.toUTCString(); // use expires attribute, max-age is not supported by IE
    }
    var path = options.path ? '; path=' + options.path : '';
    var domain = options.domain ? '; domain=' + options.domain : '';
    var secure = options.secure ? '; secure' : '';
    document.cookie = [name, '=', encodeURIComponent(value), expires, path, domain, secure].join('');
  } else { // only name given, get cookie
    var cookieValue = null;
    if (document.cookie && document.cookie != '') {
      var cookies = document.cookie.split(';');
      for (var i = 0; i < cookies.length; i++) {
        var cookie = jQuery.trim(cookies[i]);
        // Does this cookie string begin with the name we want?
        if (cookie.substring(0, name.length + 1) == (name + '=')) {
          cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
          break;
        }
      }
    }
    return cookieValue;
  }
}

// 数值前补零
function pad(num, n) {
  var len = num.toString().length;
  while (len < n) {
    num = "0" + num;
    len++;
  }
  return num;
}

// 转换为日期
function strToDate(date) {
  return new Date(date.replace(/-/g, "/"));
}

// 日期加减
function addDate(date, dadd) {
  date = date.valueOf();
  date = date + dadd * 24 * 60 * 60 * 1000;
  return new Date(date);
}

//截取字符串，区别汉字和英文
function abbr(name, maxLength) {
  if (!maxLength) {
    maxLength = 20;
  }
  if (name == null || name.length < 1) {
    return "";
  }
  var w = 0; //字符串长度，一个汉字长度为2   
  var s = 0; //汉字个数   
  var p = false; //判断字符串当前循环的前一个字符是否为汉字   
  var b = false; //判断字符串当前循环的字符是否为汉字   
  var nameSub;
  for (var i = 0; i < name.length; i++) {
    if (i > 1 && b == false) {
      p = false;
    }
    if (i > 1 && b == true) {
      p = true;
    }
    var c = name.charCodeAt(i);
    //单字节加1   
    if ((c >= 0x0001 && c <= 0x007e) || (0xff60 <= c && c <= 0xff9f)) {
      w++;
      b = false;
    } else {
      w += 2;
      s++;
      b = true;
    }
    if (w > maxLength && i <= name.length - 1) {
      if (b == true && p == true) {
        nameSub = name.substring(0, i - 2) + "...";
      }
      if (b == false && p == false) {
        nameSub = name.substring(0, i - 3) + "...";
      }
      if (b == true && p == false) {
        nameSub = name.substring(0, i - 2) + "...";
      }
      if (p == true) {
        nameSub = name.substring(0, i - 2) + "...";
      }
      break;
    }
  }
  if (w <= maxLength) {
    return name;
  }
  return nameSub;
}


//打开对话框(添加修改)
function openDialog(title, url, width, height, target) {
  top.layer.open({
        type: 2,  
        area: [width, height],
        title: title,
        maxmin: true, //开启最大化最小化按钮
        content: url ,
        btn: ['确定','关闭'],
        yes: function(index, layero){
           // var body = top.layer.getChildFrame('body', index);
           //   var iframeWin = layero.find('iframe')[0]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
           //   var inputForm = body.find('#inputForm');
           //   var top_iframe;
           //   if(target){
           //     top_iframe = target;//如果指定了iframe，则在改frame中跳转
           //   }else{
           //     top_iframe = top.getActiveTab().attr("name");//获取当前active的tab的iframe 
           //   }
           //   inputForm.attr("target",top_iframe);//表单提交成功后，从服务器返回的url在当前tab中展示
           //   // location.reload();
           //  if(iframeWin.contentWindow.doSubmit() ){
           //    // top.layer.close(index);//关闭对话框。
           //      setTimeout(function(){
           //        top.layer.close(index)
           //      }, 100);//延时0.1秒，对应360 7.1版本bug
           //   }
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
    });   

}

//打开对话框(查看)
function openDialogView(title, url, width, height, str, fn) {
  var numberHeight = height.split('px')[0], numberWidth = width.split('px')[0];
  var cHeight = $(window).height() > numberHeight ? numberHeight : $(window).height();
  var cWidth = $(window).width() > numberWidth ? numberWidth : $(window).width();
  var name = str || '关闭';
  top.layer.open({
    type: 2,
    area: [cWidth, cHeight],
    title: title,
    maxmin: true, //开启最大化最小化按钮
    content: url,
    btn: [name],
    end: function() {
      if(fn && typeof fn == 'function'){fn()}
    }
  });

}

function search() { //查询，页码清零
  $("#pageNo").val(0);
  $("#searchForm").submit();
  return false;
}

function reset() { //重置，页码清零
  $("#pageNo").val(0);
  $("#searchForm div.form-group input").val("");
  $("#searchForm div.form-group select").val("");
  $("#searchForm").submit();
  return false;
}

function sortOrRefresh() { //刷新或者排序，页码不清零

  $("#searchForm").submit();
  return false;
}

function page(n, s) { //翻页
  $("#pageNo").val(n);
  $("#pageSize").val(s);
  $("#searchForm").submit();
  $("span.page-size").text(s);
  return false;
}



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
         //window.location = redicturl;
          search();
        }
    }
    });
}
function dataDisable(url,dataId)
{
     var loadi = layer.load('取消启动中…'); 
     var redicturl="${ctx}"+url;
    var url="${ctx}"+url+"disable?id="+dataId;
   
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
           search();
        }
    }
    });
}
function DataPro(index,name,type,dataFormat,desc)
 {
     this.proName=name.replace(/(^\s*)|(\s*$)/g, "");//q去掉空格
     this.proIndex=index.replace(/(^\s*)|(\s*$)/g, "");
     this.proType=type.replace(/(^\s*)|(\s*$)/g, "");
     this.remarks=desc.replace(/(^\s*)|(\s*$)/g, "");
     this.proDesc=desc.replace(/(^\s*)|(\s*$)/g, "");
     this.type=2;//默认普通字段
     this.dataFormat=dataFormat.replace(/(^\s*)|(\s*$)/g, "");
 }

 function jarChange(obj){
         if (!(window.File || window.FileReader || window.FileList || window.Blob)) {
             layer.alert('请使用 Chrome 浏览器！');
             return;
         }
        var  file=$(obj);
        var files = file.prop('files');//获取到文件列表
        if(files.length > 0){
          var filePath=file.val();//str.charAt(str.length – 1)
          if(filePath.substr(filePath.length-3,filePath.length)=="jar")
          {
          // $('#' + eleId)[0].files[0].size;
          var size=(obj.files[0].size / (1024 * 1024)).toFixed(2);
           // 
           /* layer.alert(size);*/
           if(size>200)
           {
            layer.alert("文件大小不能超过 200M !"); 
            //清除选择的文件
            file.after(file.clone().val("")); 
            file.remove(); 
            return;
           }
          }else{

            layer.alert("文件格式错误,仅支持jar文件！");  
            //清除选择的文件
            file.after(file.clone().val("")); 
            file.remove(); 
            return;
        }
    }
} 

 function excelChange(obj){
         if (!(window.File || window.FileReader || window.FileList || window.Blob)) {
             layer.alert('请使用 Chrome 浏览器！');
             return;
         }
        var  file=$(obj);
        var files = file.prop('files');//获取到文件列表
        if(files.length > 0){
          var filePath=file.val();//str.charAt(str.length – 1)
          if(filePath.substr(filePath.length-3,filePath.length)=="xls" || filePath.substr(filePath.length-4,filePath.length)=="xlsx")
          {
          // $('#' + eleId)[0].files[0].size;
          var size=(obj.files[0].size / (1024 * 1024)).toFixed(2);
           // 
           /* layer.alert(size);*/
           if(size>5)
           {
            layer.alert("文件大小不能超过 5M !"); 
            //清除选择的文件
            file.after(file.clone().val("")); 
            file.remove(); 
            return;
           }
          }else{

            layer.alert("文件格式错误,仅支持xls、xlsx文件！");  
            //清除选择的文件
            file.after(file.clone().val("")); 
            file.remove(); 
            return;
        }
    }
} 

