<%@ page import="net.sf.json.JSONArray" %>
<%@ page import="org.springframework.beans.factory.annotation.Autowired" %>
<%@ page import="com.fas.base.service.BaseDicService" %>
<%@ page import="org.springframework.web.context.ContextLoader" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="js.jsp"%>
<%
	BaseDicService baseDicService = ContextLoader.getCurrentWebApplicationContext().getBean(BaseDicService.class);
	request.setAttribute("noticetype", JSONArray.fromObject(baseDicService.getDictionary("noticetype")));
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width,initial-scale=1" >
	<title></title>
</head>
<link href="${ctx}/resources/assets/global/plugins/ztree/metroStyle/metroStyle.css" rel="stylesheet" type="text/css"/>
<link href="${ctx}/resources/css/loadding.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/resources/assets/global/plugins/jquery-multi-select/css/multi-select.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/resources/assets/global/plugins/bootstrap-fileinput/bootstrap-fileinput.css" rel="stylesheet" type="text/css"/>
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
	.box{
		cursor: hand;
	}
	.ellipsisLine{
		-webkit-line-clamp:3;
		overflow: hidden;
		text-overflow:ellipsis ;
		-webkit-box-orient:vertical;
		overflow: hidden;
	}
</style>
<body class="page-content">
	<div class="my-col-md">
		<div class="portlet light bordered">
			<div class="portlet-title">
				<div class="caption"><i class="icon-envelope"></i>通告管理</div>
				<div class="actions">
					<button class="btn btn-sm btn-primary" id="btn_add" data-toggle="modal" onclick="btn_add_click();"><i class="fa fa-plus"></i> 增加</button>
					<button class="btn btn-sm btn-info" id="btn_edit" data-toggle="modal" onclick="btn_edit_click();"><i class="fa fa-pencil-square-o"></i> 修改</button>
					<button class="btn btn-sm btn-success" id="btn_set" onclick="btn_set_click();"><i class="fa fa-cog"></i> 状态</button>
					<button class="btn btn-sm btn-warning" id="btn_publish" onclick="btn_publish_click();"><i class="fa fa-cog"></i> 发布</button>
				</div>
			</div>
			<div class="portlet-body flip-scroll">
				<table class="table table-striped table-bordered table-hover" style="white-space: nowrap;width: 100%;" id="itemTable">
					<thead>
					<tr>
						<th ><input id="cb" name="cb" type="checkbox" class="checkchild" data-set="#itemTable.checkboxes" /> </th>
						<th>序号</th>
						<th>通告标题</th>
						<th>通告类型</th>
						<th>发布人</th>
						<th>发布时间</th>
					</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>

<div id="itemModal" class="modal fade" role="dialog" <%--tabindex="-1"--%> data-backdrop="static">
	<!-- 	<div class="modal-dialog"> -->
	<div class="modal-dialog modal-lg" style="width:900px;height: 500px;" >
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
				<h4 class="modal-title"></h4>
			</div>
			<div class="modal-body col-md-12">
				<div class="from-group">
					<form class="form-horizontal" role="form" id="itemForm" enctype="multipart/form-data">
						<div class="from-group">
							<div class="col-md-12 col-lg-12">
								<label class="control-label col-md-2 col-lg-2 align-label">
									分类：
								</label>
								<div class="col-md-10 col-lg-10 paddingRight0">
									<select name="TYPE" class="form-control">
										<c:forEach items="${noticetype}" var="temp">
											<option value="${temp.ITEM_CODE}">${temp.ITEM_NAME}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
						<div class="form-group">
							<div class="col-md-12 col-lg-12">
								<label class="control-label col-md-2 col-lg-2 align-label">
									通告标题：
								</label>
								<div class="col-md-10 col-lg-10 paddingRight0">
									<input type="text" name="TITLE" class="form-control" placeholder="请输入通告标题">
								</div>
							</div>
						</div>
						<div class="form-group">
							<div class="col-md-12 col-lg-12">
								<label class="control-label col-md-2 col-lg-2 align-label">
									选择签收部门：
								</label>
								<div class="col-md-10 col-lg-10">
									<input name="SIGN_DEPT_NAME" class="form-control" readonly="readonly" onclick="showMenu();">
									<input name="SIGN_DEPT"  class="form-control" style="display: none">
									<div id="menuContent" class="menuContent inputtree">
										<ul id="depttree" class="ztree" style="margin-top:0; width:100%"></ul>
									</div>
								</div>
							</div>
						</div>
						<div class="from-group">
							<div class="col-md-12 col-lg-12">
                            	<label class="control-label col-md-2 col-lg-2 align-label">
                                	附件上传：
                            	</label>
                            	<%--<div class="col-md-10 col-lg-10 paddingRight0">
                                  <span class="input-group-addon btn default btn-file" style="border-radius: 5px 5px 5px 5px!important;width: 50px;height: 20px;">
									  <input  type="file" name="file" >上传文件
								  </span>
                            	</div>--%>
								<div id="xzfile" class="fileinput fileinput-new" data-provides="fileinput">
									<div class="input-group input-large">
										<div class="form-control uneditable-input input-fixed input-medium" data-trigger="fileinput" style="border-radius: 5px 0 0 5px!important;">
											<i class="fa fa-file fileinput-exists"></i>&nbsp;
											<span class="fileinput-filename"> </span>
										</div>
										<span class="input-group-addon btn default btn-file" style="border-radius: 0 5px 5px 0!important;">
										<span class="fileinput-new">选择文件</span>
										<span class="fileinput-exists">重新选择</span>
										<input type="file" name="file" id="file" onchange="filechange()"> </span>
										<a id="delfile" href="javascript:;" class="input-group-addon btn red fileinput-exists" data-dismiss="fileinput" style="border-radius: 0 5px 5px 0!important;">删除</a>
									</div>
								</div>
							</div>
                        </div>
						<div class="form-group">
							<div class="col-md-12 col-lg-12">
								<label class="control-label col-md-2 col-lg-2 align-label">
									通告内容：
								</label>
								<div class="col-md-10 col-lg-10 paddingRight0">
									<textarea  id="editor" class="ckeditor form-control" rows="7" data-error-container="#editor_error" placeholder="请输入通告内容"></textarea>
									<div id="editor_error"></div>
									<label class="editor_error"></label>
								</div>

								<%--<div class="col-md-10 col-lg-10 paddingRight0">
                                    <textarea id="editor" name="CONTENT" type="text/plain" class="form-control" rows="10" placeholder="请输入通告内容"></textarea>
                                </div>--%>
							</div>
						</div>
						<input type="text" name="ID" hidden="hidden" />
					</form>
				</div>
				<%--<div id="menuContent" class="menuContent inputtree col-md-3">
					<ul id="depttree" class="ztree" style="margin-top:0; width:100%"></ul>
				</div>--%>
			</div>
			<div class="modal-footer ">
				<%--<button class="btn btn-warning" onclick="btn_publish_single_click();"><i class="fa fa-cog"></i> 发布</button> --%>
				<button class="btn btn-primary" onclick="btn_save_click();" id="sc"><i class="fa fa-save"></i> 保存</button>
				<button class="btn btn-default" data-dismiss="modal" onclick="btn_close_click();" id="btn_close"><i class="fa fa-remove"></i> 取消</button>
			</div>
		</div>
	</div>
</div>
<!-- //////////////////////////////////////////////////////// -->
	<div id="lookPublishParticulars" class="modal fade" role="dialog" tabindex="-1" data-backdrop="static">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"  aria-hidden="true"></button>
					<h4 class="modal-title">发布</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form" id="PublishForm">
						<div   style="width: 860px;margin: auto;text-align: center">
							<input type="text" name="TITLE_LOOK" style="border: 0px" readonly>
						</div>
						<div style="width: 450px;height: 50px;margin-left: 0px;text-align: center">
							发布单位：<input type="text" name="CREATOR_LOOK" style="border: 0px" readonly>
						</div>
						<div readonly style="width: 410px;height: 50px;margin-left: 500px;margin-top: -50px;text-align: left">
							发布时间：<input type="text" name="CREATE_TIME_LOOK" style="border: 0px" readonly>
						</div>
						<div style="width: 860px;margin-left: 0px;text-align: left">
							<div class="ellipsisLine" name="CONTENT_LOOK" style="border: 0px;height: 100px;" rows="20" cols="120" readonly ></div>
						</div>
						<div style="width: 860px;height: 20px;margin-left: 0px;text-align: left" class="box" onclick="downloadfile()" >
							附件：<i class="fa fa fa-download" style="cursor:pointer" ></i><input type="text" name="FILENAME_LOOK" style="border: 0px" readonly>
						</div>
					</form>

				<div>
					<label class="control-label col-md-2">签收记录</label>
					<div class="portlet-body flip-scroll">
						<table class="table table-striped table-bordered table-hover" style="white-space: nowrap;width: 100%;" id="PublishTable">
							<thead>
							<tr>
								<th>签收人</th>
								<th>所属单位</th>
								<th>签收时间</th>
								<th>签收状态</th>
							</tr>
							</thead>
						</table>
					</div>
				</div>
				</div>
				<div class="modal-footer">
					<button class="btn btn-default" data-dismiss="modal" onclick="btn_close_click();">
						<i class="fa fa-remove"></i> 关闭
					</button>
				</div>
			</div>
		</div>
	</div>

	<div id="lookSignParticulars" class="modal fade" role="dialog" tabindex="-1" data-backdrop="static">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"  aria-hidden="true"></button>
					<h4 class="modal-title">签收</h4>
				</div>
				<div class="modal-body" onclick="btn_signed_click()">
					<form class="form-horizontal" role="form" id="SignForm">
						<div style="width: 860px;height: 600px;margin: auto">
							<div  style="width: 860px;height: 50px;margin: auto;text-align: center">
								<input type="text" name="TITLE_LOOK" style="border: 0px" readonly>
							</div>
							<div style="width: 450px;height: 50px;margin-left: 0px;text-align: center">
										<input type="text" name="ID_LOOK" style="border: 0px" hidden readonly>
								发布单位：<input type="text" name="CREATOR_LOOK" style="border: 0px" readonly>
							</div>
							<div readonly style="width: 410px;height: 50px;margin-left: 500px;margin-top: -50px;text-align: left">
								发布时间：<input type="text" name="CREATE_TIME_LOOK" style="border: 0px;width: 200px" readonly>
							</div>
							<div style="width: 860px;height: 450px;margin-left: 0px;text-align: left">
								<div name="CONTENT_LOOK" style="border: 0px" rows="20" cols="120" readonly="readonly" ></div>
							</div>
							<div style="width: 860px;height: 20px;margin-left: 0px;text-align: left" class="box" onclick="downloadfile()">
								附件：<i class="fa fa fa-download" style="cursor:pointer" ></i><input type="text" name="FILENAME_LOOK" style="border: 0px" readonly>
							</div>
						</div>
					</form>
					<div style="margin-left: 800px">
						<button id="sign" class="btn btn-primary" onclick="btn_sign_click();"><i class="fa fa-save"></i>签收</button>
					</div>
				</div>
				<div class="modal-footer">
					<button class="btn btn-default" data-dismiss="modal" onclick="btn_close_click();">
						<i class="fa fa-remove"></i> 关闭
					</button>
				</div>
			</div>
		</div>
	</div>
</body>
<script src="${ctx}/resources/assets/global/plugins/ztree/jquery.ztree.all-3.5.min.js" type="text/javascript"></script>
<script src="${ctx}/scripts/sys/notice.js" type="text/javascript"></script>
<script src="${ctx}/resources/assets/global/plugins/ckeditor/ckeditor.js" type="text/javascript"></script>
<script src="${ctx}/resources/assets/global/plugins/bootstrap-fileinput/bootstrap-fileinput.js" type="text/javascript"></script>
</html>