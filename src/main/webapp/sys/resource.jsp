<%@ page import="com.fas.base.service.ResouRceService" %>
<%@ page import="org.springframework.web.context.ContextLoader" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="js.jsp"%>
<%
	ResouRceService resouRceService = ContextLoader.getCurrentWebApplicationContext().getBean(ResouRceService.class);
	request.setAttribute("applys",resouRceService.getapply());
	request.setAttribute("dict",resouRceService.getauthority("DATA_AUTHORITY"));
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title></title>
</head>
<link href="${ctx}/resources/assets/global/plugins/ztree/metroStyle/metroStyle.css" rel="stylesheet" type="text/css"/>
<style>
	.ztree * {
		font-size: 14px;
		margin: 0;
		padding: 0;
	}
</style>
<body class="page-content">

	<div class="my-col-md">
		<div class="portlet light">
			<div class="portlet-title">
				<select id="APP" onchange="innttree();" class="bs-select form-control" data-live-search="true">
					<optgroup label="应用">
						<c:forEach items="${applys}" var="temp">
							<c:if test="${fn:indexOf(temp.APP_ID,'portal')==-1}">
								<option value="${temp.APP_ID}">${temp.APP_NAME}</option>
							</c:if>
						</c:forEach>
					</optgroup>
					<optgroup label="平台">
						<c:forEach items="${applys}" var="temp">
							<c:if test="${fn:indexOf(temp.APP_ID,'portal')>0}">
								<option value="${temp.APP_ID}">${temp.APP_NAME}</option>
							</c:if>
						</c:forEach>
					</optgroup>
				</select>
			</div>
			<div class="portlet-body flip-scroll">
				<ul id="typeIdIdtree" class="ztree"></ul>
			</div>
		</div>
	</div>
<!-- 弹窗-->
<div id="itemModal" class="modal fade" role="dialog" tabindex="-1" data-backdrop="static">
	<!-- 	<div class="modal-dialog"> -->
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
				<h4 class="modal-title"></h4>
			</div>
			<div class="modal-body">
				<form class="form-horizontal" role="form" id="itemForm">
					<div class="form-group">
						<div class="col-md-6 col-lg-6">
							<label class="control-label col-md-4 col-lg-4">所属应用：</label>
							<div class="col-md-8 col-lg-8">
								<input type="text" readonly="readonly" id="APP_ID" class="form-control">
								<input type="text" hidden="hidden" name="APP_ID">
							</div>
						</div>
						<div class="col-md-6 col-lg-6">
							<label class="control-label col-md-4 col-lg-4">资源名称：</label>
							<div class="col-md-8 col-lg-8">
								<input type="text" name="NAME" class="form-control" placeholder="请输入资源名称">
								<%--<select id="NAME"  class="form-control" placeholder="请选择资源名称" onchange="seldataauthority()">--%>
									<%--<option hidden="hidden"></option>--%>
									<%--<c:forEach items="${dict}" var="temp">--%>
										<%--<option value="${temp.ITEM_CODE}">${temp.ITEM_NAME}</option>--%>
									<%--</c:forEach>--%>
								<%--</select>--%>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-md-6 col-lg-6">
							<label class="control-label col-md-4 col-lg-4">KEY：</label>
							<div class="col-md-8 col-lg-8">
								<input type="text" name="RES_KEY" class="form-control" >
							</div>
						</div>
						<div class="col-md-6 col-lg-6">
							<label class="control-label col-md-4 col-lg-4">类型：</label>
							<div class="col-md-8 col-lg-8">
								<select  name="TYPE"  class="form-control">
									<option value="0">菜单</option>
									<option value="1">按钮</option>
									<option value="2">其他</option>
								</select>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-md-6 col-lg-6">
							<label class="control-label col-md-4 col-lg-4">图标：</label>
							<div class="col-md-8 col-lg-8">
								<input type="text" name="ICONCLS" class="form-control" >
							</div>
						</div>
						<div class="col-md-6 col-lg-6">
							<label class="control-label col-md-4 col-lg-4">上级：</label>
							<div class="col-md-8 col-lg-8">
								<input type="text" readonly="readonly" id="PARENTID" class="form-control">
								<input type="text" hidden="hidden" name="PARENTID">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-md-12 col-lg-12">
							<label class="control-label col-md-2 col-lg-2 align-label">
								动作：
							</label>
							<div class="col-md-10 col-lg-10 paddingRight0">
								<input type="text" name="ACTION" class="form-control" >
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-md-12 col-lg-12">
							<label class="control-label col-md-2 col-lg-2 align-label">
								描述：
							</label>
							<div class="col-md-10 col-lg-10 paddingRight0">
								<input type="text" name="DESCN" class="form-control">
							</div>
						</div>
					</div>
					<input type="text" name="RESOURCEID" hidden="hidden" />
				</form>
			</div>
			<div class="modal-footer">
				<button class="btn btn-primary" onclick="btn_save_click();"><i class="fa fa-save"></i> 保存</button>
				<button class="btn btn-default" data-dismiss="modal" onclick="btn_close_click();" id="btn_close"><i class="fa fa-remove"></i> 取消</button>
			</div>
		</div>
	</div>
</div>
</body>
<script src="${ctx}/resources/assets/global/plugins/ztree/jquery.ztree.all-3.5.min.js" type="text/javascript"></script>
<script src="${ctx}/scripts/sys/resource.js" type="text/javascript"></script>
</html>