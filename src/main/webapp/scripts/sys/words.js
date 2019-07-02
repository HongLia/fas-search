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
			url:ctx+"/words/list",
			type : "POST",
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
				data : "WORD"
			}, {
				data : "CREATOR"
			}, {
				data : "CREATETIME"
			}, {
				data : "UPDATOR"
			}, {
				data : "UPDATETIME"
			}
		],
		aoColumnDefs : [
		{		
			targets : 3,
			render : function(data, type, full) { //返回自定义内容
				return formatDateTime(data);
			}
		},{
				targets : 5,
				render : function(data, type, full) { //返回自定义内容
					return formatDateTime(data);
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
	$("#itemForm input[name=ID]").val("");
	$("h4").text("新增");
	$('#itemModal').modal('show');
    $('#itemModal').on('shown.bs.modal', function() {
    	$('input[name=WORD]').focus();
    });	  
}

//修改
function btn_edit_click() {
	var row = $('#itemTable').DataTable().rows('.selected').data();
	if (row.length == 0) {
		window.parent.toastr[MES_WARN]("请选中需要修改的记录！");
		return false;
	}
	$("#itemForm input[name=ID]").val(row[0].ID);
	$("#itemForm input[name=WORD]").val(row[0].WORD);

	$("h4").text("修改");
	$('#itemModal').modal('show');
    $('#itemModal').on('shown.bs.modal', function() {
        $('input[name=WORD]').focus();
    });
}

// 保存按钮单击事件
function btn_save_click() {	
	var url = "words/add";
	if($("#itemForm input[name=ID]").val()!=""){
		url = "words/edit";
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
		WORD : {
			required : true,
			maxlength : 20
		}
	},
	messages : {
		WORD : {
			required : "请输入敏感词.",
			maxlength : "最大长度为20."
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