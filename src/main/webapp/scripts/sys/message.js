$(document).ready(function() {
	initTable();
});
function initTable() {
	var table = $('#itemTable');
	var oTable = table.DataTable({
		dom:"<'row'<'search text-left'f>r>t<'row pageRow'<'col-sm-3 col-md-3 col-lg-3 data_len'l><'col-sm-3 col-md-3 col-lg-3'i><'col-sm-6 col-md-6 col-lg-6'p>>",
		scrollY:true,
		processing : true,
		serverSide : false,
		bSort : false,
		searching : false,
		pagingType : "full_numbers",
		lengthChange : true,
		deferRender: true,
		bDestroy: true,
		lengthMenu : [ 10, 25, 50 ],
		ajax : {
			url : ctx+"message/list",
			type : "POST",
			error: AjaxError
		},
		columns : [
			{
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
				data : "mtitle",
				sClass:"text-center",
			}, {
				data : "mcontent",
				sClass:"text-center",
			}, {
				data : "createtime",
				sClass:"text-center",
			}, {
				data : "creator",
				sClass:"text-center",
			}, {
				data : "mbelong",
				sClass:"text-center",
			}, {
				data : "mzt",
				sClass:"text-center",
			}],
		aoColumnDefs : [
			{
				targets : 6,
				data : "STATE",
				render : function(data, type, full) { //返回自定义内容
					if (data == "0") {
						return "<span class='label label-sm label-primary'>未读</span>";
					}
					if (data == "1") {
						return "<span class='label label-sm label-success'>已读</span>";
					}
					if (data == "2") {
						return "<span class='label label-sm label-danger'>删除</span>";
					}
				}
			}, {
				targets : 3,
				render : function(data, type, full) {
					return formatDateTime(data);
				}
			}],
		language : {
			url : ctx+"resources/lang-zh_CN.json"
		},
		initComplete:function () {
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