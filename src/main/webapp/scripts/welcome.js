
$(document).ready(function() {
	//initNoticeTable();
});

//表格初始化
function initNoticeTable() {
    var table = $('#NoticeTable');
    var oTable = table.dataTable({	
		processing : true,//加载数据时候是否显示进度条
		serverSide : true,//是否从服务加载数据 
		bSort : false,//排序
		searching : false,//控件的搜索功能,
		deferRender: true,//定义在render时是否仅仅render显示的dom
		bLengthChange:false, //关闭每页显示多少条数据
		bPaginate: false, //翻页功能
		bFilter:false, //过滤功能
		bPaginate:false,//是否使用分页 	
		bInfo: false,//页脚信息
		bDestroy:true,
		ajax : {
			url : "notice/newlist",
			type : "POST",
			error: AjaxError
		},
		columns : [ 
		{
			data : "TITLE",
		}, {
			data : "CREATE_TIME",
			width : "140px",
		}, {
			width : "100px",
		} ],
		aoColumnDefs : [ 
		{
			targets : 1,
			render : function(data, type, full) {
				return formatDate(data);
			}
		}, {
			targets : 2,
			render : function(data, type, full) {
				return "<a class='look'><i class='fa fa-file-text-o'></i> 查看通告 </a>";
			}
		} ],
		language : {
			url : "resources/lang-zh_CN.json"		
		}
	});
	//查看通告
	table.on('click', '.look', function (e) {
		e.preventDefault()		
		var data = table.DataTable().row($(this).parents("tr")).data();
    	$("#lookForm input[name=TITLE_LOOK]").val(data.TITLE);
    	$("#lookForm textarea[name=CONTENT_LOOK]").val(data.CONTENT);
    	$('#lookModal').modal('show');      
	})	
}
//异常错误提示
function AjaxError( xhr, textStatus, error ) {  
    if ( textStatus === 'timeout' ) {  
    	bootbox.alert("服务器没有响应！");  
    }  
    else {  
    	//bootbox.alert("服务器出错,请重试！");
    }   
    $("#itemTable").dataTable().fnProcessingIndicator(false );  
}  
