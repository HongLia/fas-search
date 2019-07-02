<%@ page import="com.fas.base.service.BaseDicService" %>
<%@ page import="net.sf.json.JSONArray" %>
<%@ page import="org.springframework.web.context.ContextLoader" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="js.jsp"%>
<%
	BaseDicService baseDicService = ContextLoader.getCurrentWebApplicationContext().getBean(BaseDicService.class);
	request.setAttribute("filetype", JSONArray.fromObject(baseDicService.getDictionary("FILETYPE")));
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title></title>
</head>
<link href="${ctx}/resources/assets/global/plugins/bootstrap-fileinput/bootstrap-fileinput.css" rel="stylesheet" type="text/css"/>
<body class="page-content">
	<div class="my-col-md">
		<div class="portlet light">
			<div class="portlet-title">
				<div class="actions">
						<button class="btn btn-sm btn-primary" id="btn_add" data-toggle="modal" onclick="btn_add_click();"><i class="fa fa-upload"></i>上传</button>
						<button class="btn btn-sm btn-info" id="btn_edit" data-toggle="modal" onclick="btn_edit_click();"><i class="fa fa-pencil-square-o"></i> 修改</button>
						<button class="btn btn-sm btn-success" id="btn_set" onclick="btn_set_click();"><i class="fa fa-cog"></i> 状态</button>
				</div>
			</div>
			<div class="portlet-body flip-scroll">
				<table class="table table-striped table-bordered table-hover" id="itemTable">
					<thead>
					<tr>
						<th>序号</th>
						<th>文件类型</th>
						<th>文件路径</th>
						<th>文件名称</th>
						<th>状态</th>
						<th>创建人</th>
						<th>创建时间</th>
					</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
	<div id="itemModal" class="modal fade" role="dialog" tabindex="-1" data-backdrop="static">
		<!-- 	<div class="modal-dialog"> -->
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
					<h4 class="modal-title"></h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form" id="itemForm" enctype="multipart/form-data">
						<div class="form-group">
							<div class="col-md-5 col-lg-5">
							<label class="control-label col-md-4 col-lg-4">文件类型：</label>
							<div class="col-md-8 col-lg-8">
								<select  name="typeCode"  class="form-control">
									<c:forEach items="${filetype}" var="type">
										<option value="${type.ITEM_CODE}">${type.ITEM_NAME}</option>
									</c:forEach>
								</select>
							</div>
							</div>
							<div class="col-md-7 col-lg-7">
								<div id="xzfile" class="fileinput fileinput-new" data-provides="fileinput">
									<div class="input-group input-large">
										<div class="form-control uneditable-input input-fixed input-medium" data-trigger="fileinput" style="border-radius: 5px 0 0 5px!important;">
											<i class="fa fa-file fileinput-exists"></i>&nbsp;
											<span class="fileinput-filename"> </span>
										</div>
										<span class="input-group-addon btn default btn-file" style="border-radius: 0 5px 5px 0!important;">
										<span class="fileinput-new">选择文件</span>
										<span class="fileinput-exists">重新选择</span>
										<input type="file" name="file"> </span>
										<a id="delfile" href="javascript:;" class="input-group-addon btn red fileinput-exists" data-dismiss="fileinput" style="border-radius: 0 5px 5px 0!important;">删除</a>
									</div>
								</div>
								<div id="filejd" class="progress" style="width: 375px;height: 24px;margin: 5px 0;">
									<div class="progress-bar progress-bar-info progress-bar-striped active" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width:100%;">
										<center><span>正在上传...</span></center>
									</div>
								</div>
							</div>
						</div>
						<input type="text" name="id" hidden="hidden" />
					</form>
				</div>
				<div class="modal-footer">
					<button id="sc" class="btn btn-primary" onclick="btn_save_click();"><i class="fa fa-save"></i> 保存</button>
					<button class="btn btn-default" data-dismiss="modal"><i class="fa fa-remove"></i>关闭</button>
				</div>
			</div>
		</div>
	</div>
</body>
<script src="${ctx}/resources/assets/global/plugins/bootstrap-fileinput/bootstrap-fileinput.js" type="text/javascript"></script>
<script src="${ctx}/scripts/sys/dwload.js" type="text/javascript"></script>
</html>