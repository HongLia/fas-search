$(document).ready(function() {
	$('.bs-select').selectpicker({
		iconBase: 'fa',
		tickIcon: 'fa-check'
	});
	initTable();
});
var serchvalue;
//表格初始化
function initTable(appid) {
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
			url:ctx+"/params/list",
			type : "POST",
			data:{
				appid:$("#APP").val(),
				searchValue:$("#itemTable_filter input").val()==null?"":$("#itemTable_filter input").val(),
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
				data : "APP_ID",
				sWidth:'200px',
			},{
				data : "PARM_KEY"
			}, {
				data : "PARM_VALUE"
			}, {
				data : "REMARKS"
			}
		],
		aoColumnDefs : [
		{		

		}
		],		
		language : {
			url:ctx+"/resources/lang-zh_CN.json"
		},initComplete:function () {
			tableHeight();
			$("#itemTable_filter input").attr("title","选中");
			$("#itemTable_filter input").attr("placeholder","输入所属应用,参数编号,值,描述,回车搜索");
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
	$("#itemForm input[name=PARM_KEY]").removeAttr("readonly","readonly");
	$("#itemForm input[name=ID]").val("");
	$("h4").text("新增");
	$('#itemModal').modal('show');
    $('#itemModal').on('shown.bs.modal', function() {
    	$('input[name=PARM_KEY]').focus();
    });	  
}

//修改
function btn_edit_click() {
	$("#itemForm input[name=PARM_KEY]").attr("readonly","readonly");
	var row = $('#itemTable').DataTable().rows('.selected').data();
	if (row.length == 0) {
		window.parent.toastr[MES_WARN]("请选中需要修改的记录！");
		return false;
	}
	$("#itemForm select[name=APP_ID]").val(row[0].APP_ID.substring(row[0].APP_ID.indexOf("(")+1,row[0].APP_ID.indexOf(")")));
	$("#itemForm input[name=ID]").val("edit");
	$("#itemForm input[name=PARM_KEY]").val(row[0].PARM_KEY);
	$("#itemForm input[name=PARM_VALUE]").val(row[0].PARM_VALUE);
	$("#itemForm textarea[name=REMARKS]").val(row[0].REMARKS);

	$("h4").text("修改");
	$('#itemModal').modal('show');
    $('#itemModal').on('shown.bs.modal', function() {
        $('input[name=PARM_KEY]').focus();
    });
}
// 保存按钮单击事件
function btn_save_click() {
	function sava() {
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
			},
			error : function() {
				window.parent.toastr[MES_ERROR]("数据保存失败！");
			}
		});
	}
	var url = "params/add";
	var form = $("#itemForm");
	if (form.valid()) {
		if($("#itemForm input[name=ID]").val()!= ""){
			url = "params/edit";
			sava();
		}else{
			$.ajax({
				url:ctx+"/params/isone",
				type : "POST",
				data : "key="+$("#itemForm input[name=PARM_KEY]").val(),
				success : function(result) {
					if (result == "success") {
						sava();
					}else{
						$("#itemForm input[name=PARM_KEY]").val(null);
						window.parent.toastr[MES_WARN]("参数编码已存在！");
					}
					btn_close_click();
				}
			});
		}
	}
}
//验证
$("#itemForm").validate({
	rules : {
		PARM_KEY : {
			required : true,
			maxlength : 20
		},
		PARM_VALUE : {
			required : true,
			maxlength : 200
		},
		REMARKS:{
			maxlength : 500
		}
	},
	messages : {
		PARM_KEY : {
			required : "请输入参数编码.",
			maxlength : "内容过长"
		},
		PARM_VALUE : {
			required : "请输入参数值.",
			maxlength : "内容过长"
		},
		REMARKS:{
			maxlength : "内容过长"
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