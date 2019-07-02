<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="js.jsp"%>
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
<ul id="typeIdIdtree" class="ztree"></ul>
<!-- 弹窗-->
<div id="itemModal" class="modal fade" role="dialog" tabindex="-1" data-backdrop="static">
	<!-- 	<div class="modal-dialog"> -->
	<div class="modal-dialog modal-md">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
				<h4 class="modal-title"></h4>
			</div>
			<div class="modal-body">
				<form class="form-horizontal" role="form" id="itemForm">
					<div class="form-group">
						<div class="col-md-6 col-lg-6">
							<label class="control-label col-md-4 col-lg-4">地区编码：</label>
							<div class="col-md-8 col-lg-8">
								<input type="text" name="ID" class="form-control">
							</div>
						</div>
						<div class="col-md-6 col-lg-6">
							<label class="control-label col-md-4 col-lg-4">地区名称：</label>
							<div class="col-md-8 col-lg-8">
								<input type="text" name="NAME" class="form-control">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-md-12 col-lg-12">
							<label class="control-label col-md-2 col-lg-2 align-label">
								上级：
							</label>
							<div class="col-md-10 col-lg-10 paddingRight0">
								<input type="text" readonly="readonly" id="PARENT_ID" class="form-control">
								<input type="text" hidden="hidden" name="PARENT_ID">
							</div>
						</div>
					</div>
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
<script src="${ctx}/scripts/sys/location.js" type="text/javascript"></script>

</html>