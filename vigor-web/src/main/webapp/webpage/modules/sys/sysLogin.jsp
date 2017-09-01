<%@ page contentType="text/html;charset=UTF-8" %>
  <%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
    <%@ include file="/webpage/include/taglib.jsp"%>
      <!DOCTYPE html>
      <html>

      <head>
        <meta name="description" content="User login page" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <script src="${ctxStatic}/jquery/jquery-2.1.1.min.js" type="text/javascript"></script>
        <script src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js" type="text/javascript"></script>
        <script src="${ctxStatic}/jquery-validation/1.14.0/jquery.validate.js" type="text/javascript"></script>
        <script src="${ctxStatic}/jquery-validation/1.14.0/localization/messages_zh.min.js" type="text/javascript"></script>
        <link href="${ctxStatic}/bootstrap/3.3.4/css_default/bootstrap.min.css" type="text/css" rel="stylesheet" />
        <script src="${ctxStatic}/bootstrap/3.3.4/js/bootstrap.min.js" type="text/javascript"></script>
        <link href="${ctxStatic}/awesome/4.4/css/font-awesome.min.css" rel="stylesheet" />
        <!-- tospur -->
        <link href="${ctxStatic}/common/jeeplus.css" type="text/css" rel="stylesheet" />
        <script src="${ctxStatic}/common/jeeplus.js" type="text/javascript"></script>
        <link  rel="shortcut icon" href="${ctxStatic}/images/favicon.ico"
        <!-- text fonts -->
        <link rel="stylesheet" href="${ctxStatic }/common/login/ace-fonts.css" />
        <!-- ace styles -->
        <link rel="stylesheet" href="${ctxStatic }/common/login/ace.css" />
        <!-- 引入layer插件 -->
        <script src="${ctxStatic}/layer-v2.0/layer/layer.js"></script>
        <script src="${ctxStatic}/layer-v2.0/layer/laydate/laydate.js"></script>
        <!--[if lte IE 9]>
			<link rel="stylesheet" href="../assets/css/ace-part2.css" />
		<![endif]-->
        <link rel="stylesheet" href="${ctxStatic }/common/login/ace-rtl.css" />
        <style type="text/css">
        .light-login{
          /*background: -webkit-linear-gradient(top, #cce7f5, #ccf3fe);
          background: -moz-linear-gradient(top, #cce7f5, #ccf3fe);
          background: linear-gradient(top, #cce7f5, #ccf3fe);*/
          background: url(${ctxStatic}/common/login/images/login.png) no-repeat;
          background-size:  cover;
        }
        .bound {
          border-radius: 12px;
        }
        
        html, body {
          font-family: "微软雅黑";
          height: 100%;
        }
        
        .headLogin {
          height: 54px;
          background: rgba(64, 192, 239, 0.51);
          overflow: hidden;
        }
        
        #loginForm>fieldset>label {
          display: block;
          padding-left: 28px;
          margin-bottom: 24px;
          /*border-bottom: 1px solid #7e7e7e;*/
          background: url(${ctxStatic}/common/login/images/login-icon.png) no-repeat 2px 6px/16px;
        }
        
        #loginForm>fieldset>label:nth-child(2){
          background: url(${ctxStatic}/common/login/images/login-icon.png) no-repeat 2px -24px/16px;
          margin-bottom: 20px;
        }
        #loginForm input {
          border: none;
          height: 26px;
          color: #878686;
          background: #f2f2f2; border-radius: 2px;
          font-size: 14px;
        }
        
        .validateCode #validateCode {
          height: 26px !important;
          margin-bottom: 0 !important;
          margin-right: 10px;
          width: 113px !important;
          font-weight: 400 !important;
          background: #f2f2f2 !important; border-radius: 2px !important;
          vertical-align: middle;
        }
        .validateCode a{display: none;}
        
        .headLogin img {
          display: block;
          margin: 0 auto;
          /*margin-top: 18px;*/
        }
        
        .widget-body {
          background: none;
          overflow: hidden;
        }
        
        .widget-main {
          background: rgba(255, 255, 255, .3);
        }
        
        .lbl {
          color: #005598;
          padding-left: 4px;
        }
        
        #loginForm input:-webkit-autofill {
          -webkit-box-shadow: 0 0 0px 1000px #f2f2f2 inset !important;
          background: #fff url(/tospur-admin/static/common/login/images/login-icon.png) no-repeat 8px 8px !important;
        }
        
        input[type=checkbox].ace:hover + .lbl::before,
        input[type=radio].ace:hover + .lbl::before,
        input[type=checkbox].ace + .lbl:hover::before,
        input[type=radio].ace + .lbl:hover::before {
          border-color: #5dc309;
        }
        
        .boxInner {
          padding: 40px 20px;
          border-radius: 0 0 4px 4px;
          background: #fff;
        }
        
        #logining {
          background-color: #005598 !important;
          border-color: #005598;
          height: 30px;
          border:none;
          border-radius: 4px;
          color: #fff;
          margin-top: 14px;
          transition: all 1s;
          -moz-transition: all 1s;
          -webkit-transition: all 1s;
          -o-transition: all 1s;
        }
        #logining:focus {
          outline: none;
        }
        
        #logining:hover {
          background-color: #306894 !important;
          border-color: #306894;
        }
        
        #logining:hover span {
          background-color: #306894 !important;
        }
        
        #logining span {
          background: #40c0ef;
          transition: all 1s;
          -moz-transition: all 1s;
          -webkit-transition: all 1s;
          -o-transition: all 1s;
        }

        .top-logo-box{background: #033360; height: 70px;}
        .top-logo-box img{ height: 70px; margin-left: 20px;}
        .login-wrapper{
          /*padding: 30px;
          position: absolute;
          top: 50%;
          left: 57%;
          -webkit-transform: translate(-50%, -50%);
          -moz-transform: translate(-50%, -50%);
          transform: translate(-50%, -50%);
          overflow: hidden;*/
          /*background: url(${ctxStatic}/common/login/images/login.png) no-repeat;*/
          /*background: url(${ctxStatic}/common/login/images/login-img.png) no-repeat 30px center;*/

        }
        
        #login-box {
          /*float: right;*/
          width: 290px;
          box-shadow: 0 0 40px rgba(68, 71, 72, .3);
          position: fixed;
          top: 33%;
          right: 18%;
        }
        .login-img{float: left;}

        </style>
        <title>${fns:getConfig('productName')} 欢迎登录</title>
        <script>
        if (window.top !== window.self) {
          window.top.location = window.location;
        }
        </script>
<!--         <script type="text/javascript">
        $(document).ready(function() {
				$("#loginForm").submit(function(e){
					console.log(e)
				 // $("#messageBox").text("密码错误！");
				});
          $("#loginForm").validate({
            rules: {
              validateCode: {
                remote: "${pageContext.request.contextPath}/servlet/validateCodeServlet"
              }
            },
            messages: {
              username: {
                required: "请填写用户名."
              },
              password: {
                required: "请填写密码."
              },
              validateCode: {
                remote: "验证码不正确.",
                required: "请填写验证码."
              }
            },
            errorLabelContainer: "#messageBox",
            errorPlacement: function(error, element) {
              error.appendTo($("#loginError").parent());
            }
          });


        });
        // 如果在框架或在对话框中，则弹出提示并跳转到首页
        if (self.frameElement && self.frameElement.tagName == "IFRAME" || $('#left').length > 0 || $('.jbox').length > 0) {
          alert('未登录或登录超时。请重新登录，谢谢！');
          top.location = "${ctx}";
        }
        </script> -->
       
      </head>

      <body class="login-layout light-login">
        <div class="top-logo-box"><img src="${ctxStatic}/common/login/images/logo.png" alt=""></div>
        <div class="login-wrapper">
          <!-- <div class="login-img"><img src="${ctxStatic}/common/login/images/login.png" style="width: 100%;height:calc(100% - 60px)" alt=""></div> -->
          <div id="login-box" class="bound">
            <div class="widget-body bound">
                <div class="headLogin">
                  <img style="height: 54px" src="${ctxStatic}/common/login/images/logo-blue1.png" alt="">
                </div>
                <div class="boxInner">
                  <form id="loginForm" class="form-signin" action="${ctx}/login" method="post">
                    <fieldset>
                      <label class="block clearfix">
                        <span class="block input-icon input-icon-right">
  															<input type="text"  id="username" name="username" class="form-control required"  value="${username}" placeholder="请输入帐号" autocomplete="off"/>
  														</span>
                      </label>
                      <label class="block clearfix">
                        <span class="block input-icon input-icon-right">
  															<input type="password" id="password" name="password" class="form-control required" placeholder="请输入密码" />
  														</span>
                      </label>
                      <c:if test="${isValidateCodeLogin}">
                        <div class="input-group m-b text-muted validateCode">
                          <label class="input-label mid" for="validateCode" style="vertical-aglin:middle;">验证码：</label>
                          <sys:validateCode name="validateCode" inputCssStyle="margin-bottom:5px; vertical-aglin:middle;" />
                        </div>
                      </c:if>
                      <div class="clearfix" style="position:relative">
                        <label class="inline">
                          <input type="checkbox" id="rememberMe" name="rememberMe" ${rememberMe ? 'checked' : ''} class="ace" />
                          <span class="lbl"> 下次自动登录</span>
                        </label>
                        <div id="messageBox" class="alert alert-success" style="background:#fff;border:none; color:red;top: 20px;position: absolute;left: 4px; padding:0">${message}</div>
                      </div>
                
                      <button type="submit" id="logining" class="col-xs-12 btn btn-sm ">
                        登&nbsp;录
                      </button>
                      <div class="space-4"></div>
                      <div id="themeSwitch" class="dropdown">
                      </div>
                    </fieldset>
                  </form>
                </div>
              <!-- /.widget-main -->
            </div>
            <!-- /.widget-body -->
          </div>
        </div>
        <!-- /.login-box -->
        <!-- /.main-content -->
        <script type="text/javascript">
        window.jQuery || document.write("<script src='../assets/js/jquery.js'>" + "<" + "/script>");
        </script>
        <!-- <![endif]-->
        <!--[if IE]>
<script type="text/javascript">
 window.jQuery || document.write("<script src='../assets/js/jquery1x.js'>"+"<"+"/script>");
</script>
<![endif]-->
        <script type="text/javascript">
        if ('ontouchstart' in document.documentElement) document.write("<script src='../assets/js/jquery.mobile.custom.js'>" + "<" + "/script>");
        </script>
        <style>
        /* Validation */
        
        label.error {
          color: #cc5965;
          display: inline-block;
          margin-left: 5px;
        }
        
        .form-control.error {
          border: 1px dotted #cc5965;
        }
        </style>
        <!-- inline scripts related to this page -->
        <script type="text/javascript">
        $(document).ready(function() {
          $(document).on('click', '.form-options a[data-target]', function(e) {
            e.preventDefault();
            var target = $(this).data('target');
            $('.widget-box.visible').removeClass('visible'); //hide others
            $(target).addClass('visible'); //show target
          });
        });
        </script>
      </body>

      </html>
