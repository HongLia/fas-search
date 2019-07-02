var validator = "";
$(document).ready(function() {
	initTable();
	innttree();
});
//表格初始化
var serchvalue;
function initTable() {
	var table = $('#itemTable');
	var oTable = table.DataTable({
		dom:"<'row'<'search text-left'f>r>t<'row pageRow'<'col-sm-3 col-md-3 col-lg-3 data_len'l><'col-sm-3 col-md-3 col-lg-3'i><'col-sm-6 col-md-6 col-lg-6'p>>",
		scrollY:true,
		processing : true,
		serverSide : true,
		bSort : false,
		searching : true,
		pagingType : "full_numbers",
		lengthChange : true,
		deferRender: true,
		bDestroy: true,
		lengthMenu : [ 10, 25, 50 ],
		ajax : {
			url : ctx+"notice/list",
			type : "POST",
			data:{
				appid:$("#TYPE").val(),
				searchValue:$("#itemTable_filter input").val(),
			},
			error: AjaxError
		},
	/*	select : {
			style : 'single'
		},*/
		columns : [
			{
				data:"ID",
				sClass:"text-center",
				render: function (data,type,full) {

					sReturn = "<input  class='checkchild' id='id' type='checkbox' name='cb' value='"+data+"'>";
					return sReturn;
				},
				width : "30px",
				"bSortable":false
			}, {
				data:null,
				width:'30px',
				sClass:"text-center",
				"bSortable":false,
				title:'序号',
				fnCreatedCell:function (nTd,sData,oData,iRow,iCol) {
					var startnum=this.api().page()*(this.api().page.info().length);
					$(nTd).html(iRow+1+startnum);
				}
			},
			{
				data : "TITLE",
				sClass:"text-center",
            }, {
                data : "TYPE",
                sClass:"text-center",
			}, {
				data : "CREATOR",
				sClass:"text-center",
			}, {
				data : "CREATE_TIME",
				sClass:"text-center",
            } ],
		aoColumnDefs : [
			{
				targets : 2,
				data : "TITLE",
				render : function(data, type, full) { //返回自定义内容
					if(full.fbqs==0){
                        if(full.RELEASE_STATE==0){
                            return "<span class='label label-sm label-danger'>待发布</span> <a class='lookpublishparticulars'><i class='fa fa-file-text-o'></i> "+data+" </a>";
						}else{
                            return "<span class='label label-sm label-primary'>已发布</span> <a class='lookpublishparticulars'><i class='fa fa-file-text-o'></i> "+data+" </a>";
						}
					}
					if(full.fbqs==1){
						return "<span class='label label-sm label-danger'>待签收</span> <a class='looksignparticulars' ><i class='fa fa-file-text-o'></i> "+data+" </a>";
					}
					if(full.fbqs==2){
						return "<span class='label label-sm label-primary'>已签收</span> <a class='looksignparticulars'><i class='fa fa-file-text-o'></i> "+data+" </a>";
					}
				}
			}, {
				targets : 5,
				render : function(data, type, full) {
					return formatDateTime(data);
				}
			}],
		language : {
			url : ctx+"resources/lang-zh_CN.json"
		},
		initComplete:function () {
			tableHeight();
			$("#itemTable_filter input").attr("placeholder","通告标题");
			$("#itemTable_filter input").val(serchvalue);
			$("#itemTable_filter input").focus();
			$("#itemTable_filter").append('<i id="btn-search-i" class="fa fa-search" style="cursor:pointer;margin:6px 0px 1px 1px;"></i>');
			$("#itemTable_filter").addClass("input-icon right");
			$("#itemTable_filter input").unbind();
			$("#itemTable_filter input").on("keypress",function(e){
				if(e.keyCode!=13)return;
				serchvalue=$(this).val();
				initTable();
			});
			$("#itemTable_filter i").on("click",function(e){
				serchvalue=$("#itemTable_filter input").val();
				initTable();
			});
		}
	});

	//签收者详情
	table.on('click', '.looksignparticulars', function (e) {
		$("h4").text("公告详情");
		e.preventDefault();
		var data = table.DataTable().row($(this).parents("tr")).data();
		$("#SignForm input[name=ID_LOOK]").val(data.ID);
		$("#SignForm input[name=TITLE_LOOK]").val(data.TITLE);
		$("#SignForm input[name=CREATOR_LOOK]").val(data.CREATOR);
		$("#SignForm input[name=CREATE_TIME_LOOK]").val(formatDateTime(data.CREATE_TIME));
		$("#SignForm div[name=CONTENT_LOOK]").html(data.CONTENT);
		$("#SignForm input[name=FILENAME_LOOK]").val(data.FILENAME);
		btn_signed_click(data.ID);
		if(data.fbqs==2){
			$("#sign").hide();
		}
		$('#lookSignParticulars').modal('show');
	})
	//发布者详情
	table.on('click', '.lookpublishparticulars', function (e) {
		$("h4").text("公告详情");
		e.preventDefault();
		var data = table.DataTable().row($(this).parents("tr")).data();
		btn_signed_click(data.ID);
		initPublishTable(data.ID);
		$("#PublishForm input[name=TITLE_LOOK]").val(data.TITLE);
		$("#PublishForm input[name=CREATOR_LOOK]").val(data.CREATOR);
		$("#PublishForm input[name=CREATE_TIME_LOOK]").val(formatDateTime(data.CREATE_TIME));
		$("#PublishForm div[name=CONTENT_LOOK]").html(data.CONTENT);
		$("#PublishForm input[name=FILENAME_LOOK]").val(data.FILENAME);
		$('#lookPublishParticulars').modal('show');
	})
	/*//查看通告
	table.on('click', '.look', function (e) {
		$("h4").text("查看通告");
		e.preventDefault();
		var data = table.DataTable().row($(this).parents("tr")).data();
		$("#lookForm input[name=TITLE_LOOK]").val(data.TITLE);
		$("#lookForm textarea[name=CONTENT_LOOK]").val(data.CONTENT);
		$('#lookModal').modal('show');
	})
	hidecloum("#itemTable thead",oTable);*/
}
////////////////////////////////////////////////////////////////////////////////////////////////
function downloadfile() {
	window.location.href=ctx+"/notice/downloadbyid?id="+ids;
}
//表格初始化
function initPublishTable(id) {
	var table = $('#PublishTable');
	var oTable = table.DataTable({
		searching : false,
		bSort : false,
		paging:false,
		processing : true,
		serverSide : true,
		lengthChange : true,
		deferRender: true,
		bDestroy: true,
		lengthMenu : [ 10, 25, 50 ],
		ajax : {
			url : ctx+"notice/publishlist",
			type : "POST",
			data:{
				id:id,
			},
			error: AjaxError
		},
		columns : [

			{
				data : "USERNAME",
				sClass:"text-center",
			},{
				data : "DEPTNAME",
				sClass:"text-center",
			},{
				data : "SIGNTIME",
				sClass:"text-center",
			},{
				data : "SIGNSTATE",
				sClass:"text-center",
			} ],
		aoColumnDefs : [
			 /*{
				targets : 1,
 				data:"USERID",
				render : function(data, type, full) {

					return formatDateTime(data);
				}
			}*/  {
				targets : 2,
				render : function(data, type, full) {

					return formatDateTime(data);
				}
			}, {
 				targets : 3,
				data :"SIGNSTATE",
 				render : function(data, type, full) {
				if(data == "0"){
					return "<span class='label label-sm label-danger'>未签收</span>";
				}
				if(data == "1"){
					return "<span class='label label-sm label-success'>已签收</span>";
				}
 				}
 			}],
		language : {
			url : ctx+"resources/lang-zh_CN.json"
		}
	})
}

////////////////////////////////////////////////////////////////////////////////////////////////
//为class为checkchild的span添加单击事件
$('#itemTable').on('click', '.checkchild', function(e) {
	if($(this).attr("id") == "cb"){
		var check = $(this).prop("checked"); //全选
		$(".checkchild").prop("checked",check);
		if($(this).hasClass('check_boxon')){
			$(".checkchild").removeClass('check_boxon');
			$("#itemTable tbody tr").removeClass('selected');
		}else{
			$(".checkchild").addClass('check_boxon');
			$("#itemTable tbody tr").addClass('selected');
		}
	}else{
		var tr = $(this).parents('tr')[0];
		if($(this).hasClass('check_boxon')){
			$(this).toggleClass('check_boxon');
			$(tr).toggleClass('selected');
		}else{
			$(this).toggleClass('check_boxon');
			$(tr).toggleClass('selected');
		}
	}
	//e.stopPropagation();//阻止冒泡事件
});
// 全选全不选
/*$(".checkchild").click(function () {
 var check = $(this).prop("checked");
 $(".checkchild").prop("checked",check);
 });*/

//行点击
$('#itemTable tbody').on('click', 'tr', function (e) {
	if ($(e.target).attr("type") != "checkbox") {
		var p = this;
		$($(p).children()[0]).children().each(function () {
			if(this.type=="checkbox"){
				if(this.checked){
					this.checked = false;
				}else{
					this.checked = true;
				}
			}
		}) ;
		$(this).toggleClass('selected');
	}
} );

//异常错误提示
function AjaxError( xhr, textStatus, error ) {
	if ( textStatus === 'timeout' ) {
		window.parent.toastr[MES_ERROR]("服务器没有响应！");
	}
	else {
		window.parent.toastr[MES_ERROR]("服务器出错,请重试！");
	}
	$("#itemTable").dataTable().fnProcessingIndicator(false );
}

//新增
function btn_add_click() {

	/*$.ajax({
		url : "notice/hdfs",
		type : "POST"
	});*/
	$("xzfile").show();
	$("#itemForm input[name=ID]").val("");
	$("h4").text("新增通知通告");
	$('#itemModal').modal('show');
	$('#itemModal').on('shown.bs.modal', function() {
		$('input[name=TITLE]').focus();
	});
}

//修改
function btn_edit_click() {
	var row = $('#itemTable').DataTable().rows('.selected').data();
	if (row.length == 0) {
		window.parent.toastr[MES_WARN]("请选中需要修改的记录！");
		return false;
	}
	if (row.length > 1) {
		window.parent.toastr[MES_WARN]("请选择一条需要修改的记录！");
		return false;
	}
	if (row[0].RELEASE_STATE == 1) {
		window.parent.toastr[MES_WARN]("通告已发布，不可修改！");
		return false;
	}
	SetContents(row[0].CONTENT);
	$("#itemForm select[name=TYPE]").val(row[0].TYPE);
	$("#itemForm input[name=TITLE]").val(row[0].TITLE);
	$("#itemForm input[name=SIGN_DEPT]").val(row[0].SIGN_DEPT);
	$("#itemForm textarea[name=CONTENT]").val(row[0].CONTENT);
	$("#itemForm input[name=ID]").val(row[0].ID);

	$("h4").text("修改");
	$('#itemModal').modal('show');
	$('#itemModal').on('shown.bs.modal', function() {
		$('input[name=TITLE]').focus();
	});
	$('#itemModal').modal().on('hidden', function () {
		$('form').each(function (index) {
			$('form')[index].reset();
		});
	});
}

//设置状态
function btn_set_click() {
	var row = $('#itemTable').DataTable().rows('.selected').data();
	var ids = "";
	var states = "";
	for(var i = 0;i<row.length;i++) {
		if (i != row.length - 1) {
			ids += row[i].ID + ",";
			states += row[i].STATE + ",";
		} else {
			ids += row[i].ID;
			states += row[i].STATE;
		}
	}
	if (row.length == 0) {
		window.parent.toastr[MES_WARN]("请选中需要设置状态的记录！");
		return false;
	}
	if (row[0].STATE=="0"){
		msg = "确定要注销记录或恢复记录吗？";
	}else{
		msg = "确定要注销记录或恢复记录吗？";
	}

	bootbox.confirm(msg, function(result) {
		if (result) {
			$.ajax({
				url : ctx+"notice/set",
				cache : false,
				type : "POST",
				data : {
					ids :ids,
					states:states
				},
				success : function(result) {
					if (result == "success") {
						window.parent.toastr[MES_SUCCESS]("状态设置成功");
						$("#itemTable").dataTable().fnDraw(false);
						$("#cb").attr("checked",false);
						$(".checkchild").removeClass('check_boxon');
						$("#itemTable tbody tr").removeClass('selected');
					} else {
						window.parent.toastr[MES_ERROR]("状态设置失败");
					}
				},
				error : function(error) {
					window.parent.toastr[MES_ERROR]("状态设置失败");
				}
			});
		}
	});
}
//获取id
function btn_signed_click(id) {
 ids = id;
}
//签收
function btn_sign_click() {
	$.ajax({
		url : ctx+"notice/sign",
		cache : false,
		type : "POST",
		data : {
			id :ids,
		},
		success : function(result) {
			if (result == "success") {
				window.parent.toastr[MES_SUCCESS]("签收成功");
				$("#sign").hide();
			} else {
				window.parent.toastr[MES_ERROR]("签收失败");
			}
		},
		error : function(error) {
			window.parent.toastr[MES_ERROR]("签收失败...");
		}

	});
}
//发布
function btn_publish_click(){
	var row = $('#itemTable').DataTable().rows('.selected').data();
	if (row.length == 0) {
		window.parent.toastr[MES_WARN]("请选中需要发布的记录！");
		return false;
	}
	if (row.length >1) {
		window.parent.toastr[MES_WARN]("请选择单个通告发布！");
		return false;
	}
	if (row[0].RELEASE_STATE=="1"){
		window.parent.toastr[MES_WARN]("通告已发布！");
		return false;
	}else{
		msg = "确定要发布记录吗？";
	}
	bootbox.confirm(msg, function(result) {
		if (result) {
			utils.showLoadding("发布中，请稍等！！！");
			$.ajax({
				url : ctx+"notice/release",
				cache : false,
				type : "POST",
				data : {
					ID :row[0].ID,
					SIGN_DEPT:row[0].SIGN_DEPT,
				},
				success : function(result) {
					utils.hideLoadding();
					if (result == "success") {
						window.parent.toastr[MES_SUCCESS]("发布成功");
						$("#itemTable").dataTable().fnDraw(false);
						$("#cb").attr("checked",false);
						$(".checkchild").removeClass('check_boxon');
						$("#itemTable tbody tr").removeClass('selected');
					}
				},
				error : function(error) {
					utils.hideLoadding();
					window.parent.toastr[MES_ERROR]("发布失败");
				}
			});
		}
	});
}

//ckeditor 获取值
function GetContents()
{
	var editor = CKEDITOR.instances.editor;
return editor.getData().replace(/%/g,"%25").replace(/\&/g,"%26").replace(/\+/g,"%2B");
}
//ckeditor 赋值
function SetContents(value)
{
	var editor = CKEDITOR.instances.editor;
	editor.setData(value);
}

function initjdt() {
	$("#xzfile").show();
	$("#sc").show();
}
// 保存按钮单击事件
function showhide(show) {
	if(show){
		//隐藏内容div
		$("#showeditor").hide();
		$(".looklook").show();
		//显示编辑div
		$("#editor").show();
		$("#itemForm").find("input").prop("readonly",false);
		$("#itemForm").find("select").prop("disabled",false);
		$(".bc").show();
	}else {
		//显示内容div
		$("#showeditor").show();
		$(".looklook").hide();
		//隐藏编辑div
		$("#editor").hide();
		$(".bc").hide();
		$("#itemForm").find("input").prop("readonly",true);
		$("#itemForm").find("select").prop("disabled",true);
	}
}

$('#itemModal').on('hidden.bs.modal', function() {
	$(".editor_error").hide();
	//获取所有的label并去除它们验证不通过的样式
	$("label").each(function () {
		$(this).closest('div').removeClass('has-error');
	});
	$('form').data('validator').resetForm();
});
function btn_save_click() {
	var file = document.getElementById('file').files[0];
	var filename = file.name;
	var url = ctx+"notice/add";
	if ($("#itemForm input[name=ID]").val() != "") {
		url = ctx+"notice/edit";
	}
	var form = $("#itemForm");
	if (validator.form()&&$("input[name=file]").val()!="") {
		$("#filejd").show();
		var formData=new FormData($("#itemForm")[0]);
		formData.append("CONTENT",GetContents());
		formData.append("FILE",GetContents());
		$.ajax({
			url : url,
			type : "POST",
			data : formData,
			async: false,
			cache: false,
			contentType: false,
			processData: false,
			success : function(result) {
				if (result == "success") {
					$("#itemModal").modal("hide");
					window.parent.toastr[MES_SUCCESS]("保存成功");
					$("#itemTable").dataTable().fnDraw(false);
					btn_close_click();
				} else {
					window.parent.toastr[MES_ERROR]("保存失败");
				}
			},
			error : function() {
				window.parent.toastr[MES_ERROR]("数据保存失败！");
			}
		});
	}

}
//验证
validator =  $("#itemForm").validate({
	rules : {
		/*TYPE : {
			required : true,
		},*/
		TITLE : {
			required : true,
			maxlength : 25
		},
		SIGN_DEPT : {
			required : true,
		},
		CONTENT : {
			required : true
		}

	},
	messages : {

		TITLE : {
			required : "请输入通告名称.",
			maxlength : "最大长度为25."
		},
		SIGN_DEPT : {
			required : "请输入签收人.",
		},
		CONTENT : {
			required : "请输入通告内容."		}
	},
	highlight : function(element) {
		$(element).closest('div').addClass('has-error');
	},
	success : function(label) {
		$(label).closest('div').removeClass('has-error');
		$(label).remove();
	}
});
//tree
function innttree(){
	var setting = {
		async:{
			enable:true,
			type:"post",
			url:ctx+"/notice/tree",
			datatype:"json",
			autoParam: ["id"]
		},
		view: {
			dblClickExpand: true,
			showLine: true,
			selectedMulti: true,
		},
		data: {
			simpleData: {
				enable:true,
				idKey:"id"
			}
		},
		check:{
			enable:true,
		},callback: {
			onCheck: function(event,treeId, treeNode) {
				var treeObj = $.fn.zTree.getZTreeObj("depttree");
				var nodes = treeObj.getCheckedNodes(true);
				var names=[];
				var ids=[];
				$.each(nodes,function () {
					if(!this.getCheckStatus().half){
						names.push(this.name)
						ids.push(this.id);
					}
				});
				$("#itemForm input[name=SIGN_DEPT_NAME]").val(names.join());
				$("#itemForm input[name=SIGN_DEPT]").val(ids.join());
			}
		}

	};
	//修改部门树加载全部部门
	$.ajax({
		url:ctx+"/notice/tree",
		type:"POST",
		dataType:"json",
		success:function(data){
			$.fn.zTree.init($("#depttree"), setting, data);
		}
	});
	//查询用户树 登录用户所在部门树
	$.ajax({
		url:ctx+"/notice/tree",
		type:"POST",
		dataType:"json",
		success:function(data){
			$.fn.zTree.init($("#depttree"), setting, data);
			//强行展开一级
			ChildNodes($.fn.zTree.getZTreeObj("depttree"));
		}
	});
}
function showMenu() {
	$("#menuContent").slideDown("fast");
	$("body").bind("mousedown", onBodyDown);
}
function hideMenu() {
	$("#menuContent").fadeOut("fast");
	$("body").unbind("mousedown", onBodyDown);
}
function onBodyDown(event) {
	if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length>0)) {
		hideMenu();
	}
}
