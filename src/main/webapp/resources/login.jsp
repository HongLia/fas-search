<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<%
		String path = "/fas-os";
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	%>
	<c:set var="ctx" value="<%= basePath%>"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="${ctx}/resources/assets/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/resources/assets/global/css/components.min.css" rel="stylesheet" id="style_components" type="text/css" />
<link href="${ctx}/resources/css/login.min.css" rel="stylesheet" type="text/css" />
<title>用户权限统一管理系统</title>
</head>
<body class=" login">
	<div class="menu-toggler sidebar-toggler"></div>
	<div class="logo"></div>
	<div class="content">
		<form class="login-form" id="loginform" name="loginform">
			<h3 class="form-title font-green">用户权限统一管理系统</h3>
			<div class="alert alert-danger display-hide">
				<button class="close" data-close="alert"></button>
				<span> 请输入用户名及密码！ </span>
			</div>
			<div class="form-group">
				<label class="control-label visible-ie8 visible-ie9">用户名</label> <input
					class="form-control form-control-solid placeholder-no-fix"
					type="text" autocomplete="off" placeholder="用户名" name="username" />
			</div>
			<div class="form-group">
				<label class="control-label visible-ie8 visible-ie9">密码</label> <input
					class="form-control form-control-solid placeholder-no-fix"
					type="password" autocomplete="off" placeholder="密码" name="password" />
			</div>
			<div class="form-actions">
				<button type="button" onclick="fas_login();" class="btn green uppercase">登录</button>
			</div>
		</form>
	</div>
	<div class="copyright">技术支持 : 中科软科技股份有限公司</div>

	<script src="${ctx}/resources/assets/global/plugins/jquery.min.js" type="text/javascript"></script>
	<script src="${ctx}/resources/assets/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="${ctx}/resources/assets/global/plugins/bootbox/bootbox.min.js" type="text/javascript"></script>
	<script src="${ctx}/resources/assets/global/plugins/jquery-validation/js/jquery.validate.min.js" type="text/javascript"></script>
	<script src="${ctx}/resources/assets/global/scripts/app.min.js" type="text/javascript"></script>
	<script src="${ctx}/scripts/login.js" type="text/javascript"></script>
</body>
<script>
	function fas_login() {
		$.ajax({
			url:"${ctx}/login",
			type : "POST",
			data : $("form").serialize(),
			dataType: 'text',
			success : function(result) {
				var json = JSON.parse(result);
				if(json.code=="200"){
					window.location.href = "${ctx}sys/index.jsp";
				}else{
					alert(json.msg);
				}
			},
			error : function(result) {
				alert("失败")
			}
		});
	}
</script>
</html>