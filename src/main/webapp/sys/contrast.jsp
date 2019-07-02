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
	.pageRow{
		width: 50%;
	}
</style>
<body class="page-content">

	<div class="my-col-md">
		<div class="portlet light">
			<div class="portlet-title">
				<div class="actions">
						<button class="btn btn-sm btn-primary" id="user_add" data-toggle="modal" onclick="btn_add_click();"><i class="fa fa-plus"></i> 增加</button>
				</div>
			</div>
			<div class="portlet-body flip-scroll">
				<div class="row">
					<div class="col-md-6">
						<table class="table table-striped table-bordered table-hover" id="pTable" style="width: 100%;white-space: nowrap">
							<thead>
							<tr>
								<th>序号</th>
								<th>操作</th>
								<th>源字典项</th>
								<th>目标字典项</th>
								<th>备注</th>
							</tr>
							</thead>
						</table>
					</div>
					<div class="col-md-6">
						<table class="table table-striped table-bordered table-hover" id="Table" hidden="hidden" style="width: 100%;white-space: nowrap">
							<thead>
							<tr>
								<th>序号</th>
								<th>操作</th>
								<th>源字典项</th>
								<th>目标字典项</th>
								<th>备注</th>
							</tr>
							</thead>
						</table>
					</div>
				</div>
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
							<label class="control-label col-md-4 col-lg-4">源字典项：</label>
							<div class="col-md-8 col-lg-8">
								<select name="SOURCE_CODE"  class="form-control" onchange="dicvila();">
								</select>
							</div>
						</div>
						<div class="col-md-6 col-lg-6">
							<label class="control-label col-md-4 col-lg-4">目标字典项：</label>
							<div class="col-md-8 col-lg-8">
								<select name="TARGET_CODE"  class="form-control" onchange="dicvila();">
								</select>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-md-12 col-lg-12">
							<label class="control-label col-md-2 col-lg-2 align-label">
								备注：
							</label>
							<div class="col-md-10 col-lg-10 paddingRight0">
								<input type="text" name="DESCN" class="form-control">
							</div>
						</div>
					</div>
					<input type="text" hidden="hidden" name="PARENT_ID">
					<input type="text" name="ID" hidden="hidden" />
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
<script src="${ctx}/scripts/sys/contrast.js" type="text/javascript"></script>

</html>