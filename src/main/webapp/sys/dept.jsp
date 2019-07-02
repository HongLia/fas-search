<%@ page import="com.fas.base.service.BaseDicService" %>
<%@ page import="com.fas.base.service.LocationService" %>
<%@ page import="net.sf.json.JSONArray" %>
<%@ page import="org.springframework.web.context.ContextLoader" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="js.jsp"%>
<%
	BaseDicService baseDicService = ContextLoader.getCurrentWebApplicationContext().getBean(BaseDicService.class);
	LocationService locationservice = ContextLoader.getCurrentWebApplicationContext().getBean(LocationService.class);
	request.setAttribute("JGXZ", JSONArray.fromObject(baseDicService.getDictionary("JGXZ")));
	request.setAttribute("JGLX", JSONArray.fromObject(baseDicService.getDictionary("JGLX")));
	request.setAttribute("SJ", locationservice.selecttree("0"));
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
	input.search-input{
		box-sizing: border-box;
		-moz-box-sizing:border-box;
		width: 100%;
		margin-bottom: 5px;
		height: auto;
	}
</style>
<body class="page-content">
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
							<label class="control-label col-md-4 col-lg-4">部门ID：</label>
							<div class="col-md-8 col-lg-8">
								<input type="text" name="DEPTID" class="form-control" placeholder="请输入部门ID">
							</div>
						</div>
						<div class="col-md-6 col-lg-6">
							<label class="control-label col-md-4 col-lg-4">部门名称：</label>
							<div class="col-md-8 col-lg-8">
								<input type="text" name="NAME" class="form-control" placeholder="请输入部门名称">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-md-6 col-lg-6">
							<label class="control-label col-md-4 col-lg-4">机构类型：</label>
							<div class="col-md-8 col-lg-8">
								<select name="DEGREE"  class="form-control">
									<c:forEach items="${JGLX}" var="temp">
										<option value="${temp.ITEM_CODE}">${temp.ITEM_NAME}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="col-md-6 col-lg-6">
							<label class="control-label col-md-4 col-lg-4">省级区域：</label>
							<div class="col-md-8 col-lg-8">
								<select  name="SJJGDM"  class="form-control" onchange="initselect('SJJGMC',this,'SHIJJGDM')">
									<option value="" hidden></option>
									<c:forEach items="${SJ}" var="temp">
										<c:if test="${temp.ENABLED==0}">
											<option value="${temp.id}">${temp.name}</option>
										</c:if>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-md-6 col-lg-6">
							<label class="control-label col-md-4 col-lg-4">市级区域：</label>
							<div class="col-md-8 col-lg-8">
								<select  name="SHIJJGDM"  class="form-control" onchange="initselect('SHIJJGMC',this,'DSJGDM')">
									<option value="" hidden></option>
								</select>
							</div>
						</div>
						<div class="col-md-6 col-lg-6">
							<label class="control-label col-md-4 col-lg-4">县区区域：</label>
							<div class="col-md-8 col-lg-8">
								<select  name="DSJGDM"  class="form-control" onchange="initselect('DSJGMC',this)">
									<option value="" hidden></option>
								</select>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-md-6 col-lg-6">
							<label class="control-label col-md-4 col-lg-4">机构性质：</label>
							<div class="col-md-8 col-lg-8">
								<select name="JGLX"  class="form-control">
									<c:forEach items="${JGXZ}" var="temp">
										<option value="${temp.ITEM_CODE}">${temp.ITEM_NAME}</option>
									</c:forEach>
								</select>
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
						<div class="col-md-6 col-lg-6">
							<label class="control-label col-md-4 col-lg-4">实际业务上级：</label>
							<div class="col-md-8 col-lg-8">
								<input name="DEPT_FUN" class="form-control" readonly="readonly" onclick="showMenu();">
								<input name="DEPT_CATE"  class="form-control" style="display: none">
								<div id="menuContent" class="menuContent inputtree">
									<ul id="depttree" class="ztree" style="margin-top:0; width:100%"></ul>
								</div>
							</div>
						</div>
						<div class="col-md-6 col-lg-6">
							<label class="control-label col-md-4 col-lg-4">描述：</label>
							<div class="col-md-8 col-lg-8">
								<input type="text" name="DESCN" class="form-control">
							</div>
						</div>
					</div>
					<input type="text" name="id" hidden="hidden" />
					<input type="text" name="SJJGMC" hidden="hidden" />
					<input type="text" name="SHIJJGMC" hidden="hidden" />
					<input type="text" name="DSJGMC" hidden="hidden" />
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
<script src="${ctx}/scripts/sys/dept.js" type="text/javascript"></script>

</html>