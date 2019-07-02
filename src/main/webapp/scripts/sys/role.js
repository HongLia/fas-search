$(document).ready(function() {
	$('.bs-select').selectpicker({
		iconBase: 'fa',
		tickIcon: 'fa-check'
	});
	initTable();
});
var serchvalue;
//表格初始化
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
		bDestroy:true,
		lengthMenu : [ 10, 25, 50 ],
		ajax : {
			url:ctx+"/role/list",
			type : "POST",
			data:{
				searchValue:$("#itemTable_filter input").val(),
			},
			error: AjaxError
		},
		select : {
			style : 'single'
		},
		columns : [
			{
				data:null,
				sWidth:'30px',
				sClass:"text-center",
				fnCreatedCell:function (nTd,sData,oData,iRow,iCol) {
					var startnum=this.api().page()*(this.api().page.info().length);
					$(nTd).html(iRow+1+startnum);
				}
			},{
				data : "NAME",
			}, {
				data : "DESCN",
			}, {
				data : "ENABLED"
			}, {
				data : "LEVEL_",
			}
		],
		aoColumnDefs : [
		{		
			targets : 3,
		 	data : "ENABLED",
			render : function(data, type, full) { //返回自定义内容
			 	if (data == 0) {
			 		return "<span class='label label-sm label-primary'>正常</span>";
			   	} else {
			   		return "<span class='label label-sm label-danger'>注销</span>";
			 	}
			}
		},
		{
			targets : 4,
			data : "LEVEL_",
			render : function(data, type, full) { //返回自定义内容
				switch (data){
					case 0: return "管理员级"; break;
					case 1:return "部级";break;
					case 2:return "省级";break;
					case 3:return "市级";break;
					default:return "";break;
				}
			}
		}
		],		
		language : {
			url:ctx+"/resources/lang-zh_CN.json"
		},initComplete:function () {
			tableHeight();
			$("#itemTable_filter input").attr("placeholder","角色名称");
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
	hidecloum("#itemTable thead",oTable);
}

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
	$("#itemForm input[name=ROLEID]").val("");
	$("h4").text("新增");
	$('#itemModal').modal('show');
    $('#itemModal').on('shown.bs.modal', function() {
    	$('input[name=NAME]').focus();
    });	  
}

//修改
function btn_edit_click() {
	var row = $('#itemTable').DataTable().rows('.selected').data();
	if (row.length == 0) {
		window.parent.toastr[MES_WARN]("请选中需要修改的记录！");
		return false;
	}
	$("#itemForm input[name=ROLEID]").val(row[0].ROLEID);
	$("#itemForm select[name=LEVEL_]").val(row[0].LEVEL_);
	$("#itemForm input[name=NAME]").val(row[0].NAME);
	$("#itemForm textarea[name=DESCN]").val(row[0].DESCN);

	$("h4").text("修改");
	$('#itemModal').modal('show');
    $('#itemModal').on('shown.bs.modal', function() {
        $('input[name=NAME]').focus();
    });
}

//设置状态
function btn_set_click() {
	var row = $('#itemTable').DataTable().rows('.selected').data();
	if (row.length == 0) {
		window.parent.toastr[MES_WARN]("请选中需要设置状态的记录！");
		return false;
	}
	if (row[0].ENABLED=="0"){
		msg = "确定要注销系统' " + row[0].NAME + " '吗？";
	}else{
		msg = "确定要恢复系统' " + row[0].NAME + " '吗？";
	}
	
	bootbox.confirm(msg, function(result) {
		if (result) {
			$.ajax({
				url:ctx+"/role/set",
				cache : false,
				type : "POST",
				data : {
					ROLEID : row[0].ROLEID
				},
				success : function(result) {
					if (result == "success") {
						window.parent.toastr[MES_SUCCESS]("状态设置成功");
						$("#itemTable").dataTable().fnDraw(false);
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
function btn_setresource_click() {
	var row = $('#itemTable').DataTable().rows('.selected').data();
	if (row.length == 0) {
		window.parent.toastr[MES_WARN]("请选中需要设置的角色！");
		return false;
	}
	$("#applyForm input[name=ROLEID]").val(row[0].ROLEID);
	ajaxmultiselect();
	$("h4").text("设置资源权限");
	$('#resourceModal').modal('show');
}
function btn_saveresource_click() {
	var treeObj = $.fn.zTree.getZTreeObj("typeIdIdtree");
	var nodes = treeObj.getCheckedNodes(true);
	var roles=[];
	$.each(nodes,function () {
		if(this.id!=0) {
			roles.push(
				{
					ROLEID: $("#applyForm input[name=ROLEID]").val(),
					RESOURCEID: this.id
				}
			);
		}
	});
	$.ajax({
		url:ctx+"/role/saveresource",
		cache : false,
		type:"POST",
		data:{
			StRoleResources:JSON.stringify(roles),
			roleid:$("#applyForm input[name=ROLEID]").val(),
			appid:$("#APP").val()
		},
		success : function(result) {
			window.parent.toastr[MES_SUCCESS]("资源权限设置成功");
		},
		error : function(error) {
			window.parent.toastr[MES_ERROR]("资源权限设置失败");
		}
	});
}
function ajaxmultiselect(){
	var appid =$("#APP").val();
	var setting = {
		check: {
			enable: true
		},
		view: {
			dblClickExpand: true,
			showLine: true,
			selectedMulti: false
		},
		data: {
			simpleData: {
				enable:true
			}
		}
	};
	$.ajax({
		url:ctx+"/resource/tree",
		type:"POST",
		async:false,
		data:{
			appid:appid,
			type:"-1",
			enabled:"0"
		},
		dataType:"json",
		success:function(data){
			$.fn.zTree.init($("#typeIdIdtree"), setting, data);
			var treeObj = $.fn.zTree.getZTreeObj("typeIdIdtree");
			//添加菜单按钮
			$.ajax({
				url:ctx+"/resource/tree",
				type:"POST",
				async:false,
				data:{
					appid:appid,
					type:"0",
					enabled:"0"
				},
				dataType:"json",
				success:function(datacd){
					treeObj.addNodes(treeObj.getNodeByParam("type", 0, null), datacd,false);
				}
			});
			//添加其他
			$.ajax({
				url:ctx+"/resource/tree",
				type:"POST",
				async:false,
				data:{
					appid:appid,
					type:"2",
					enabled:"0"
				},
				dataType:"json",
				success:function(dataan){
					treeObj.addNodes(treeObj.getNodeByParam("type",2, null), dataan, false);
				}
			});
			//初始化选中
			$.ajax({
				url:ctx+"/role/getresource",
				type : "POST",
				data : "roleid="+$("#applyForm input[name=ROLEID]").val(),
				async:false,
				success : function(result) {
					$.each(result,function () {
						if (this.resourceid==this.roleresource) {
							var node = treeObj.getNodeByParam("id",this.roleresource, null);
							//无子节点的进行勾选设置
							if(node){
								if(node.check_Child_State==-1){
									treeObj.checkNode(node, true, true);
								}
							}
						}
					});
				}
			});
		}
	});
}
// 保存按钮单击事件
function btn_save_click() {	
	var url = "role/add";
	if($("#itemForm input[name=ROLEID]").val()!= ""){
		url = "role/edit";
	}
	var form = $("#itemForm");
	if (form.valid()) {
		$.ajax({
			url:ctx+"/"+ url,
			type : "POST",
			data : form.serialize(),
			success : function(result) {
				if (result == "success") {
					$("#itemModal").modal("hide");
					window.parent.toastr[MES_SUCCESS]("保存成功");
					$("#itemTable").dataTable().fnDraw(false);
				} else {
					window.parent.toastr[MES_ERROR]("保存失败");
				}
				btn_close_click();
			},
			error : function() {
				window.parent.toastr[MES_ERROR]("数据保存失败！");
			}
		});
	}
}

//验证
$("#itemForm").validate({
	rules : {
		NAME : {
			required : true,
			maxlength : 60
		},
		DESCN:{
			maxlength : 255
		}
	},
	messages : {
		NAME : {
			required : "请输入角色名称.",
			maxlength : "内容过长."
		},
		DESCN:{
			maxlength : "内容过长."
		}
	},
	highlight : function(element) {
		$(element).closest('div').addClass('has-error');
	},
	success : function(label) {
		$(label).closest('div').removeClass('has-error');
		$(label).remove();
	}
});