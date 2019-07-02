<%@ page import="com.fas.base.service.BaseDicService" %>
<%@ page import="net.sf.json.JSONArray" %>
<%@ page import="org.springframework.web.context.ContextLoader" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="js.jsp"%>
<%
	BaseDicService baseDicService = ContextLoader.getCurrentWebApplicationContext().getBean(BaseDicService.class);
	request.setAttribute("ZRLY", JSONArray.fromObject(baseDicService.getDictionary("ZRLY")));
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
	.inputtree{
		display:none;
		position: absolute;
		background-color: white;
		z-index: 9999;
		max-height: 300px;
		overflow: auto;
	}
</style>
<body class="page-content">
<button class="btn btn-sm btn-success" data-toggle="modal" onclick="cleanredis('CHARGE');" style="position: absolute;top: 10px;right: 10px;"><i class="fa fa-refresh"></i>重置犯罪类型</button>
<ul id="typeIdIdtree" class="ztree"></ul>
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
							<label class="control-label col-md-4 col-lg-4 align-label">
								犯罪名称：
							</label>
							<div class="col-md-8 col-lg-8">
								<input type="text" name="CHARGENAME" class="form-control" placeholder="请输入犯罪名称">
							</div>
						</div>
						<div class="col-md-6 col-lg-6">
							<label class="control-label col-md-4 col-lg-4">责任领域</label>
							<div class="col-md-8 col-lg-8">
								<select name="ZRLY"  class="form-control">
									<option value="">无</option>
									<c:forEach items="${ZRLY}" var="temp">
										<option value="${temp.ITEM_CODE}">${temp.ITEM_NAME}</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-md-6 col-lg-6">
							<label class="control-label col-md-4 col-lg-4">部&nbsp;&nbsp;&nbsp;&nbsp;门：</label>
							<div class="col-md-8 col-lg-8">
								<input name="DEPTNAME" class="form-control" readonly="readonly" onclick="showMenu();">
								<input name="DEPTID"  class="form-control" style="display: none">
								<div id="menuContent" class="menuContent inputtree">
									<ul id="depttree" class="ztree" style="margin-top:0; width:100%"></ul>
								</div>
							</div>
						</div>
						<div class="col-md-6 col-lg-6">
							<label class="control-label col-md-4 col-lg-4">上&nbsp;&nbsp;&nbsp;&nbsp;级：</label>
							<div class="col-md-8 col-lg-8">
								<input type="text" readonly="readonly" id="PARENT_ID" class="form-control">
								<input type="text" hidden="hidden" name="PARENT_ID">
							</div>
						</div>
					</div>
					<input type="text" name="CHARGEID" hidden="hidden" />
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
<script src="${ctx}/resources/assets/global/plugins/pinyin.js" type="text/javascript"></script>
<script src="${ctx}/scripts/sys/charge.js" type="text/javascript"></script>

</html>