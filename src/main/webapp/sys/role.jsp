<%@ page import="com.fas.base.service.ResouRceService" %>
<%@ page import="org.springframework.web.context.ContextLoader" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="js.jsp"%>
<%
	ResouRceService resourceservice = ContextLoader.getCurrentWebApplicationContext().getBean(ResouRceService.class);
	request.setAttribute("maxrole","-1");
	request.setAttribute("applys",resourceservice.getapply());
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
		       	<div class="actions">
						<button class="btn btn-sm btn-primary" id="btn_add" data-toggle="modal" onclick="btn_add_click();"><i class="fa fa-plus"></i> 增加</button>
						<button class="btn btn-sm btn-info" id="btn_edit" data-toggle="modal" onclick="btn_edit_click();"><i class="fa fa-pencil-square-o"></i> 修改</button>
						<button class="btn btn-sm btn-success" id="btn_set" onclick="btn_set_click();"><i class="fa fa-cog"></i> 状态</button>
						<button class="btn btn-sm btn-info" id="btn_setresource" data-toggle="modal" onclick="btn_setresource_click();"><i class="fa fa-pencil-square-o"></i> 设置角色资源</button>
		       	</div>
       		</div>
       		<div class="portlet-body flip-scroll">		                              
		 		<table class="table table-striped table-bordered table-hover" id="itemTable">
					<thead>
				  		<tr>
							<th>序号</th>
		             		<th>角色名称</th>
 		             		<th>描述</th>
							<th>是否可用</th>
							<th>角色等级</th>
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
							<div class="col-md-6 col-lg-6">
								<label class="control-label col-md-4 col-lg-4">角色名称：</label>
								<div class="col-md-8 col-lg-8">
									<input type="text" name="NAME" class="form-control" placeholder="请输入角色名称">
								</div>
							</div>
							<div class="col-md-6 col-lg-6">
								<label class="control-label col-md-4 col-lg-4">角色等级：</label>
								<div class="col-md-8 col-lg-8">
									<select  name="LEVEL_"  class="form-control">
										<c:if test="${maxrole<0}"><option value="0">管理员</option></c:if>
										<c:if test="${maxrole<1}"><option value="1">部级</option></c:if>
										<c:if test="${maxrole<2}"><option value="2">省级</option></c:if>
										<c:if test="${maxrole<3}"><option value="3">市级</option></c:if>
									</select>
								</div>
							</div>
				       	</div>
					 	<div class="form-group">
							<div class="col-md-12 col-lg-12">
								<label class="control-label col-md-2 col-lg-2 align-label">
									角色说明：
								</label>
								<div class="col-md-10 col-lg-10 paddingRight0">
									<textarea name="DESCN" class="form-control" rows="10" ></textarea>
								</div>
							</div>
					   	</div>
						<input type="text" name="ROLEID" hidden="hidden" />
			    	</form>
			 	</div>
				<div class="modal-footer">
					<button class="btn btn-primary" onclick="btn_save_click();"><i class="fa fa-save"></i> 保存</button>
			       	<button class="btn btn-default" data-dismiss="modal" onclick="btn_close_click();"><i class="fa fa-remove"></i> 取消</button>
			  	</div>
			</div>
		</div>
	</div>

	<div id="resourceModal" class="modal fade" tabindex="-1" data-backdrop="static" data-focus-on="input:first">
		<div class="modal-dialog modal-lg" class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
					<h4 class="modal-title">角色资源设置</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form" id="applyForm">
						<div class="form-group">
							<label class="control-label col-md-2">所属应用</label>
							<div class="col-md-9" >
								<select id="APP" onchange="ajaxmultiselect();" class="bs-select form-control" data-live-search="true">
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
						</div>
						<div class="form-group">
							<label class="control-label col-md-2">开通资源</label>
							<div class="col-md-9" style="max-height: 400px;overflow: auto;">
								<ul id="typeIdIdtree" class="ztree" style="margin-top:0;"></ul>
							</div>
						</div>
						<input type="text" name="ROLEID" hidden="hidden" />
					</form>
				</div>
				<div class="modal-footer">
					<button class="btn btn-primary" onclick="btn_saveresource_click();"><i class="fa fa-save"></i> 授予权限</button>
					<button class="btn btn-default" data-dismiss="modal"><i class="fa fa-remove"></i> 关闭</button>
				</div>
			</div>
		</div>
	</div>

</body>
<script src="${ctx}/resources/assets/global/plugins/ztree/jquery.ztree.all-3.5.min.js" type="text/javascript"></script>
<script src="${ctx}/scripts/sys/role.js" type="text/javascript"></script>
               
</html>