$(document).ready(function() {
	initTable();
});

//表格初始化
function initTable() {
    var table = $('#itemTable');
    var oTable = table.DataTable({
		dom:"<'row'<'search text-left'f>r>t<'row pageRow'<'col-sm-3 col-md-3 col-lg-3 data_len'l><'col-sm-3 col-md-3 col-lg-3'i><'col-sm-6 col-md-6 col-lg-6'p>>",
		scrollY:true,
		processing : true,
		serverSide : true,
		bSort : false,
		searching : false,
		pagingType : "full_numbers",
		lengthChange : true,
		deferRender: true,
		bDestroy:true,
		lengthMenu : [ 10, 25, 50 ],
		ajax : {
			url:ctx+"/application/list",
			type : "POST",
			error: AjaxError,
			xhrFields:{
				withCredentials:true
			}
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
				data : "APP_ID",
				width : "100px"
			}, {
				data : "APP_NAME",
				width : "260px"
			}, {
				data : "REMARKS"
			}, {
				data : "ENABLED",
				width : "40px"
			}
		],
		aoColumnDefs : [
		{		
			targets : 4,
		 	data : "ENABLED",
			render : function(data, type, full) { //返回自定义内容
			 	if (data == "0") {
			 		return "<span class='label label-sm label-primary'>正常</span>";
			   	} else {
			   		return "<span class='label label-sm label-danger'>注销</span>";
			 	}
			}
		}
		],		
		language : {
			url:ctx+"/resources/lang-zh_CN.json"
		},initComplete:function () {
			tableHeight();
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
	$("h4").text("新增");
	//$("#itemForm input[name=APP_ID]").attr("readonly",false);
	$("#itemForm input[name=APP_ID]").removeAttr("readonly");
	$('#itemModal').modal('show');
    $('#itemModal').on('shown.bs.modal', function() {
    	$('input[name=APP_ID]').focus();
    });	  
}

//修改
function btn_edit_click() {
	var row = $('#itemTable').DataTable().rows('.selected').data();
	if (row.length == 0) {
		window.parent.toastr[MES_WARN]("请选中需要修改的记录！");
		return false;
	}
	$("#itemForm input[name=APP_ID]").val(row[0].APP_ID);
	//$("#itemForm input[name=APP_ID]").attr("readonly",true);
	$("#itemForm input[name=APP_ID]").attr("readonly","readonly");
	$("#itemForm input[name=APP_NAME]").val(row[0].APP_NAME);
	$("#itemForm textarea[name=REMARKS]").val(row[0].REMARKS);

	$("h4").text("修改");
	$('#itemModal').modal('show');
    $('#itemModal').on('shown.bs.modal', function() {
        $('input[name=APP_NAME]').focus();
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
		msg = "确定要注销系统‘" + row[0].APP_NAME + "’吗？";
	}else{
		msg = "确定要恢复系统‘" + row[0].APP_NAME + "’吗？";
	}
	
	bootbox.confirm(msg, function(result) {
		if (result) {
			$.ajax({
				url:ctx+"/application/set",
				cache : false,
				type : "POST",
				data : {
					APP_ID : row[0].APP_ID
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

// 保存按钮单击事件
function btn_save_click() {	
	var url = "application/add";
	if($("#itemForm input[name=APP_ID]").attr("readonly")=="readonly"){
		url = "application/edit";
	}
	var form = $("#itemForm");
	if (form.valid()) {
		$.ajax({
			url:ctx+"/"+url,
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
		APP_ID : {
			required : true,
			maxlength : 20
		},
		APP_NAME : {
			required : true,
			maxlength : 60
		}
	},
	messages : {
		APP_ID : {
			required : "请输入系统代码.",
			maxlength : "最大长度为60."
		},
		APP_NAME : {
			required : "请输入系统名称.",
			maxlength : "最大长度为60."		
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