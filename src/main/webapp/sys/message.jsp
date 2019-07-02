<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="js.jsp"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title></title>
</head>
<body class="page-content">
	<div class="my-col-md">
		<div class="portlet light bordered">
			<div class="portlet-title">
				<div class="caption"><i class="icon-envelope"></i>消息列表</div>
			</div>
			<div class="portlet-body flip-scroll">
				<table class="table table-striped table-bordered table-hover" style="white-space: nowrap;width: 100%;" id="itemTable">
					<thead>
					<tr>
						<th>序号</th>
						<th>标题</th>
						<th>内容</th>
						<th>创建时间</th>
						<th>创建者</th>
						<th>所有者</th>
						<th>消息状态</th>
					</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
</body>
<script src="${ctx}/scripts/sys/message.js" type="text/javascript"></script>
</html>