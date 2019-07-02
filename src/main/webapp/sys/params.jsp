<%@ page import="com.fas.base.service.ResouRceService" %>
<%@ page import="org.springframework.web.context.ContextLoader" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="js.jsp"%>
<%
	ResouRceService resouRceService = ContextLoader.getCurrentWebApplicationContext().getBean(ResouRceService.class);;
	request.setAttribute("applys",resouRceService.getapply());
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title></title>
</head>
<body class="page-content">

	<div class="my-col-md">
		<div class="portlet light">
			<div class="portlet-title">
				<div style="width: 60%;float: left">
					<select id="APP" onchange="initTable();" class="bs-select form-control" data-live-search="true">
						<option value="all">全部参数</option>
						<option value="gas">通用参数</option>
						<optgroup label="应用">
							<c:forEach items="${applys}" var="temp">
								<c:if test="${fn:indexOf(temp.APP_ID,'portal')==-1}">
									<option value="${temp.APP_ID}">${temp.APP_NAME}</option>
								</c:if>
							</c:forEach>
						</optgroup>
					</select>
				</div>
		       	<div class="actions" style="float: right">
						<button class="btn btn-sm btn-primary" id="btn_add" data-toggle="modal" onclick="btn_add_click();"><i class="fa fa-plus"></i> 增加</button>
						<button class="btn btn-sm btn-info" id="btn_edit" data-toggle="modal" onclick="btn_edit_click();"><i class="fa fa-pencil-square-o"></i> 修改</button>
					<button class="btn btn-sm btn-success" data-toggle="modal" onclick="cleanredis('PARAM');"><i class="fa fa-refresh"></i>重置参数</button>
		       	</div>
       		</div>
       		<div class="portlet-body flip-scroll">		                              
		 		<table class="table table-striped table-bordered table-hover" id="itemTable">
					<thead>
				  		<tr>
							<th>序号</th>
							<th>所属应用</th>
		             		<th>参数编码</th>
 		             		<th>参数值</th>
							<th>参数说明</th>
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
								<label class="control-label col-md-4 col-lg-4">所属应用：</label>
								<div class="col-md-8 col-lg-8">
									<select name="APP_ID" class="bs-select form-control" data-live-search="true">
										<option value="gas">通用参数</option>
										<optgroup label="应用">
											<c:forEach items="${applys}" var="temp">
												<c:if test="${fn:indexOf(temp.APP_ID,'portal')==-1}">
													<option value="${temp.APP_ID}">${temp.APP_NAME}</option>
												</c:if>
											</c:forEach>
										</optgroup>
									</select>
								</div>
							</div>
							<div class="col-md-6 col-lg-6">
								<label class="control-label col-md-4 col-lg-4">参数编码：</label>
								<div class="col-md-8 col-lg-8">
									<input type="text" name="PARM_KEY" class="form-control" placeholder="请输入参数编码">
								</div>
							</div>
				       	</div>
				    	<div class="form-group">
							<div class="col-md-12 col-lg-12">
								<label class="control-label col-md-2 col-lg-2 align-label">
									参数值：
								</label>
								<div class="col-md-10 col-lg-10 paddingRight0">
									<input type="text" name="PARM_VALUE" class="form-control" placeholder="请输入参数值">
								</div>
							</div>
				       	</div>					       	       	
					 	<div class="form-group">
							<div class="col-md-12 col-lg-12">
								<label class="control-label col-md-2 col-lg-2 align-label">
									参数说明：
								</label>
								<div class="col-md-10 col-lg-10 paddingRight0">
									<textarea name="REMARKS" class="form-control" rows="10" ></textarea>
								</div>
							</div>
					   	</div>
						<input type="text" name="ID" hidden="hidden"/>
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
<script src="${ctx}/scripts/sys/params.js" type="text/javascript"></script>
               
</html>