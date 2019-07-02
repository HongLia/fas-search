$(document).ready(function() {
	$("#treediv").css("height",$(window).height()-110);
	//tree ajax加载完成初始化table
	innttree();
});
//表格初始化
function initTable(appcodes) {
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
			url:ctx+"/ckinfo/list",
			type : "POST",
			data:{
				appcodes:appcodes
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
				data : "APPLY_TYPE_COED",
			},{
				data : "CONF_TYPE",
			}, {
				data : "APPLY_TYPE_VALUE",
			}, {
				data : "DESCN"
			}
		],
		aoColumnDefs : [
		{
			targets : 2,
		 	data : "CONF_TYPE",
			render : function(data, type, full) { //返回自定义内容
				var text = "";
			 	switch (data){
					case "0": text="工作流"; break;
					case "1": text="登记URL"; break;
					case "2": text="展示URL"; break;
					case "3": text="审核URL"; break;
					case "4": text="调整URL"; break;
					case "5": text="跟踪URL"; break;
					case "6": text="反馈URL"; break;
				}
				return text;
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
	var treeObj = $.fn.zTree.getZTreeObj("deptquerytree");
	var treenode = treeObj.getSelectedNodes()[0];
	if(treenode){
		if(treenode.children){
			window.parent.toastr[MES_WARN]("请选择末级资源");
		}else{
			$("#itemForm input[name=COED]").val(treenode.RES_KEY);
			$("h4").text("查控配置");
			var rows = $('#itemTable').DataTable().rows().data();
			$.each(rows,function () {
				var text = "";
				switch (this.CONF_TYPE){
					case "0": text="gzl"; break;
					case "1": text="dj"; break;
					case "2": text="zs"; break;
					case "3": text="sh"; break;
					case "4": text="tz"; break;
					case "5": text="gz"; break;
					case "6": text="fk"; break;
				}
				$("#itemForm input[name="+text+"url]").val(this.APPLY_TYPE_VALUE);
				$("#itemForm input[name="+text+"bz]").val(this.DESCN);
			});
			$('#itemModal').modal('show');
		}
	}else{
		window.parent.toastr[MES_WARN]("请在左侧树选择查控资源");
	}
}
function  btn_save_click() {
	var url = "ckinfo/add";
	var form = $("#itemForm");
	if (form.valid()) {
		$.ajax({
			url:ctx+"/"+ url,
			type : "POST",
			data : form.serialize(),
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
		view: {
			dblClickExpand: true,
			showLine: true,
			selectedMulti: false
		},
		data: {
			simpleData: {
				enable:true
			}
		},callback: {
			onClick: function(event,treeId, treeNode) {
				var ids=[];
				ids = getChildren(ids,treeNode);
				initTable(ids.join());
			}
		}
	};
	$.ajax({
		url:ctx+"/resource/tree",
		type:"POST",
		data:{
			appid:appid,
			type:"-1"
		},
		dataType:"json",
		success:function(data){
			data[1].name="查控配置";
			$.fn.zTree.init($("#deptquerytree"), setting, data[1]);
			var treeObj = $.fn.zTree.getZTreeObj("deptquerytree");
			//添加其他
			$.ajax({
				url:ctx+"/resource/tree",
				type:"POST",
				data:{
					appid:appid,
					type:"2"
				},
				dataType:"json",
				success:function(dataan){
					treeObj.addNodes(treeObj.getNodeByParam("id",0, null),dataan, false);
					//var nodes = treeObj.getNodesByFilter(Nfilter); // 查找节点集合
					//treeObj.hideNodes(nodes);
					//var ids= [];
					//ids = getChildren(ids,treeObj.getNodesByFilter(Yfilter)[0]);
					//树全部加载成功后 初始化表格数据
					initTable(-1);
				}
			});
		}
	});
}
//过滤 RES_KEY=='CK100' 和pId==0 的节点 用于初始化查询
function Yfilter(node) {
	return (node.RES_KEY=='CK100' && node.pId==0);
}
//过滤 RES_KEY！='CK100' 和pId==0 的节点集合 用于隐藏
function Nfilter(node) {
	return (node.RES_KEY!='CK100' && node.pId==0);
}
//首次init table 获取所有显示节点的集合
function getChildren(ids,treeNode){
	if(!treeNode.isHidden){
		if (treeNode.isParent){
			for(var obj in treeNode.children){
				getChildren(ids,treeNode.children[obj]);
			}
		}else{
			if(treeNode.RES_KEY){
				ids.push("'"+treeNode.RES_KEY+"'");
			}
		}
	}
	return ids;
}
//from验证
//验证
$("#itemForm").validate({
	rules : {
		gzlurl : {
			// required : true,
			maxlength : 100
		},
		djurl : {
			required : true,
			maxlength : 100
		},
		zsurl : {
			required : true,
			maxlength : 100
		},
		shurl : {
			required : true,
			maxlength : 100
		},
		tzurl : {
			required : true,
			maxlength : 100
		},
		gzurl : {
			required : true,
			maxlength : 100
		},
		fkurl : {
			required : true,
			maxlength : 100
		}
	},
	messages : {
		gzlurl : {
			// required : "请输入配置",
			maxlength : "内容过长"
		},
		djurl : {
			required : "请输入配置",
			maxlength : "内容过长"
		},
		zsurl : {
			required : "请输入配置",
			maxlength : "内容过长"
		},
		shurl : {
			required : "请输入配置",
			maxlength : "内容过长"
		},
		tzurl : {
			required : "请输入配置",
			maxlength : "内容过长"
		},
		gzurl : {
			required : "请输入配置",
			maxlength : "内容过长"
		},
		fkurl : {
			required : "请输入配置",
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