<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="js.jsp"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title></title>
</head>
<body class="page-content">

	<div class="my-col-md">
		<div class="portlet light">
			<div class="portlet-title">
		       	<div class="actions">
					<button class="btn btn-sm btn-primary" id="btn_add" data-toggle="modal" onclick="btn_add_click();"><i class="fa fa-plus"></i> 增加</button>
					<button class="btn btn-sm btn-info" id="btn_edit" data-toggle="modal" onclick="btn_edit_click();"><i class="fa fa-pencil-square-o"></i> 修改</button>
					<button class="btn btn-sm btn-success" id="btn_set" onclick="btn_set_click();"><i class="fa fa-cog"></i> 状态</button>
		       	</div>
       		</div>
       		<div class="portlet-body flip-scroll">
		 		<table class="table table-striped table-bordered table-hover" id="itemTable">
					<thead>
				  		<tr>
							<th>序号</th>
		             		<th>系统代码</th>
 		             		<th>系统名称</th>
							<th>系统说明</th>
							<th>状态</th>
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
			   		<form class="form-horizontal" role="form" id="itemForm">
				    	<div class="form-group">
							<label class="control-label col-md-1 col-lg-1 align-label">系统代码：</label>
							<div class="col-md-11 col-lg-11 paddingRight0">
								<input type="text" name="APP_ID" class="form-control" placeholder="请输入系统代码">
							</div>
				       	</div>
				    	<div class="form-group">
							<label class="control-label col-md-1 col-lg-1 align-label">系统名称：</label>
							<div class="col-md-11 col-lg-11 paddingRight0">
								<input type="text" name="APP_NAME" class="form-control" placeholder="请输入系统名称">
							</div>
				       	</div>
					 	<div class="form-group">
							<label class="control-label col-md-1 col-lg-1 align-label">系统说明：</label>
							<div class="col-md-11 col-lg-11 paddingRight0">
								<textarea name="REMARKS" class="form-control" rows="10" ></textarea>
							</div>
					   	</div>
			    	</form>
			 	</div>
				<div class="modal-footer">
					<button class="btn btn-primary" onclick="btn_save_click();"><i class="fa fa-save"></i> 保存</button>
			       	<button class="btn btn-default" data-dismiss="modal" onclick="btn_close_click();"><i class="fa fa-remove"></i> 取消</button>
			  	</div>
			</div>
		</div>
	</div>

</body>
<script src="${ctx}/scripts/sys/application.js" type="text/javascript"></script>

</html>