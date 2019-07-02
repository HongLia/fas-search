$(document).ready(function() {
	//tree ajax加载完成初始化table
	innttree();
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
			url:ctx+"/caseinfo/list",
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
				data : "CONF_CODE",
			},{
				data : "CONF_NAME",
			}, {
				data : "CONF_URL",
			}, {
				data : "DESCN"
			}, {
				data : "CREATOR",
			}, {
				data : "CREATE_TIME"
			}
		],
		aoColumnDefs : [
		{
			targets : 6,
			render : function(data, type, full) { //返回自定义内容
				return formatDateTime(data);
			}
		}
		],
		language : {
			url:ctx+"/resources/lang-zh_CN.json"
		},initComplete:function () {
			tableHeight();
		},
		createdRow:function (row,data,dataIndex) {
			utils.tdShowMaxLength(row,40);// td文字超出10字以...显示
		},
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
	var treeObj = $.fn.zTree.getZTreeObj("chargetree");
	treeObj.checkAllNodes(false);
	$("h4").text("新增类罪配置");
	$('#itemModal').modal('show');
}
//修改
function btn_edit_click() {
	var treeObj = $.fn.zTree.getZTreeObj("chargetree");
	treeObj.checkAllNodes(false);
	var row = $('#itemTable').DataTable().rows('.selected').data();
	if (row.length != 0) {
		var treeObj = $.fn.zTree.getZTreeObj("chargetree");
		var CHARGE_LIST = row[0].CHARGE_LIST;
		var data = CHARGE_LIST.split(",");
		$.each(data,function () {
			var node = treeObj.getNodeByParam("id", this, null);
			if(node){treeObj.checkNode(node, true, true,true);}
		});
		$("#itemForm input[name=oldcode]").val(row[0].CONF_CODE);
		$("#itemForm input[name=CONF_CODE]").val(row[0].CONF_CODE);
		$("#itemForm input[name=CONF_NAME]").val(row[0].CONF_NAME);
		$("#itemForm input[name=CONF_URL]").val(row[0].CONF_URL);
		$("#itemForm input[name=DESCN]").val(row[0].DESCN);
		$("h4").text("修改类罪配置");
		$('#itemModal').modal('show');
	}else{
		window.parent.toastr[MES_WARN]("请选中需要修改的记录！");
		return false;
	}
}
//删除
function btn_del_click() {
	var row = $('#itemTable').DataTable().rows('.selected').data();
	if (row.length == 0) {
		window.parent.toastr[MES_WARN]("请选中需要删除的记录！");
		return false;
	}
	bootbox.confirm("是否删除？", function(result) {
		if (result) {
			$.ajax({
				url:ctx+"/caseinfo/delet",
				data : {
					CONF_CODE : row[0].CONF_CODE
				},
				success : function(result) {
					if (result == "success") {
						$("#itemTable").dataTable().fnDraw(false);
					} else {
						window.parent.toastr[MES_ERROR]("删除失败");
					}
				},
				error : function(error) {
					window.parent.toastr[MES_ERROR]("系统错误");
				}
			});
		}
	});
}
//保存
function  btn_save_click() {
	var url = "caseinfo/add";
	if($("#itemForm input[name=oldcode]").val()!=''){
		url = "caseinfo/edit";
	}
	var form = $("#itemForm");
	var treeObj = $.fn.zTree.getZTreeObj("chargetree");
	var nodes = treeObj.getCheckedNodes(true);
	var charges=[];
	var charges_json=[];
	$.each(nodes,function () {
		if(!this.getCheckStatus().half){
			charges.push(this.id)
			charges_json.push({id:this.id,name:this.name});
		}
	});
	if (form.valid()) {
		$.ajax({
			url:ctx+"/"+ url,
			type : "POST",
			data : form.serialize()+"&CHARGE_LIST="+charges.join()+"&CHARGE_JSON="+JSON.stringify(charges_json),
			success : function(result) {
				if (result == "success") {
					btn_close_click();
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

//tree
function innttree(){
	var setting = {
		async:{
			enable:false,
			type:"post",
			url:ctx+"/charge/tree",
			datatype:"json",
			autoParam: ["id"]
		},
		view: {
			dblClickExpand: true,
			showLine: true,
			selectedMulti: false,
		},
		check:{
			chkboxType:{ "Y" : "ps", "N" : "ps" },
			enable: true
		},
		data: {
			simpleData: {
				enable:true
			}
		},callback: {
			onCheck: function(event,treeId, treeNode) {
				var treeObj = $.fn.zTree.getZTreeObj("chargetree");
				var nodes = treeObj.getCheckedNodes(true);
				var names=[];
				$.each(nodes,function () {
					if(!this.getCheckStatus().half){
						names.push(this.name)
					}
				});
				$("#CHARGE_NAMES").attr("title",names.join());
				$("#CHARGE_NAMES").val(names.join());
			}
		}
	};
	//查询用户树 登录用户所在部门树
	$.ajax({
		url:ctx+"/charge/Alltree",
		type:"POST",
		dataType:"json",
		success:function(data){
			$.fn.zTree.init($("#chargetree"), setting, data);
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
//from验证
//验证
$("#itemForm").validate({
	rules : {
		CONF_CODE : {
			required : true,
			maxlength : 2
		},
		CONF_NAME : {
			required : true,
			maxlength : 100
		},
		CONF_URL : {
			required : true,
			maxlength : 200
		},
		DESCN : {
			maxlength : 255
		}
	},
	messages : {
		CONF_CODE : {
			required : "请输入配置",
			maxlength : "内容过长"
		},
		CONF_NAME : {
			required : "请输入配置",
			maxlength : "内容过长"
		},
		CONF_URL : {
			required : "请输入配置",
			maxlength : "内容过长"
		},
		DESCN : {
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