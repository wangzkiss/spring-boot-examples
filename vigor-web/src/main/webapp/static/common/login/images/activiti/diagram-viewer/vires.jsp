<%@ page language="java" contentType="text/html; charset=UTF-8" isErrorPage="true" pageEncoding="UTF-8"%>
<%response.setStatus(HttpServletResponse.SC_OK);%>
<title>错误提示</title>
</head>
<body >
		<p onclick="javascript:history.go(-1);">信息提示：非法操作，不允许越权访问</p>
     <!-- <img src="app/styles/images/error.jpg" onclick="javascript:history.go(-1);"> -->
</body>