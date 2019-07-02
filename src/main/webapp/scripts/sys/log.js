$(document).ready(function() {
	// 初始化日期选择控件
	if (jQuery().datepicker) {
		$('.date-picker').datepicker({
			rtl: App.isRTL(),
			orientation: "left",
			autoclose: true
		});
	}
	initTable();
});
function cleansearch() {
	$('#searchForm')[0].reset();
	initTable();
}

//表格初始化
function initTable() {
	var table = $('#itemTable');
	var oTable = table.DataTable({
		dom:"<'row'<'search text-left'f>r>t<'row pageRow'<'col-sm-3 col-md-3 col-lg-3 data_len'l><'col-sm-3 col-md-3 col-lg-3'i><'col-sm-6 col-md-6 col-lg-6'p>>",
		scrollX:true,
		scrollY:true,
		processing : true,
		serverSide : true,
		bSort : true,
		searching : false,
		pagingType : "full_numbers",
		lengthChange : true,
		deferRender: true,
		bDestroy:true,
		lengthMenu : [ 10, 25, 50 ],
		order: [[9,"desc"]],
		ajax : {
			url:ctx+"/log/list",
			type : "POST",
			data: {
				userid:$("#searchForm input[name=userid]").val(),
				APP:$("#searchForm select[name=APP]").val(),
				search_begin: $("#searchForm input[name=search_begin]").val(),
				search_end: $("#searchForm input[name=search_end]").val(),
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
				data : "userid",
				width:'50px',
			}, {
				data : "username",
				width:'50px',
			}, {
				data : "deptname",
				width:'150px',
			}, {
				data : "appname",
				width:'150px',
			}, {
				data : "funname",
				width:'150px',
			}, {
				data : "mac",
				width:'150px',
			}, {
				data : "result",
				width:'150px',
			}, {
				data : "remarks",
				width:'150px',
			}, {
				data : "ip",
				width:'50px',
			}, {
				data : "crrq",
				sWidth:'150px',
			}
		],
		createdRow:function (row,data,dataIndex) {
			utils.tdShowMaxLength(row,20);// td文字超出10字以...显示
		},
		aoColumnDefs : [
			{
				targets : 7,
				render : function(data, type, full) { //返回自定义内容
					if (data == "0") {
						return "<span class='label label-sm label-primary'>成功</span>";
					} else {
						return "<span class='label label-sm label-danger'>失败</span>";
					}
				}
			},
		],
		language : {
			url:ctx+"/resources/lang-zh_CN.json"
		},initComplete:function () {
			tableHeight(hideheight);
		}
	});
	hidecloum("#itemTable thead",oTable);
}
var hideheight=65;
function logtableHeight() {
	hideheight=hideheight==65?135:65;
	tableHeight(hideheight);
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
function outExcel() {
	window.location.href=ctx+"log/outexcel";
}