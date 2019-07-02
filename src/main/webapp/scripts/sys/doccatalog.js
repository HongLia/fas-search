$(document).ready(function() {
	$("#treediv").css("height",$(window).height()-50);
	//初始部门树
	inntlisttree();
});
//tree
function inntlisttree(){
	var setting = {
		async:{
			enable:true,
			type:"post",
			url:ctx+"/doccatalog/tree",
			datatype:"json",
			autoParam: ["id"]
		},
		view: {
			dblClickExpand: true,
			showLine: true,
			selectedMulti: false,
			addHoverDom: addHoverDom,
			removeHoverDom: removeHoverDom,
			fontCss: setFontCss
		},
		data: {
			simpleData: {
				enable:true
			}
		}
	};
	function setFontCss(treeId, treeNode) {
		return treeNode.ENABLED == 1 ? {color:"red"} : {};
	};
	function addHoverDom(treeId, treeNode) {
		var sObj = $("#" + treeNode.tId + "_span");
		if (
			$("#editBtn_"+treeNode.tId).length>0 ||
			$("#addBtn_"+treeNode.tId).length>0  ||
			$("#upBtn_"+treeNode.tId).length>0  ||
			$("#setBtn_"+treeNode.tId).length>0  ||
			$("#downBtn_"+treeNode.tId).length>0
		) return;
		var addStr = "<span style='margin: 0px 5px;' class='font-blue icon-plus' id='addBtn_" + treeNode.tId + "' title='添加' onfocus='this.blur();'></span>";
		var editStr = "<span style='margin: 0px 5px;' class='font-blue-steel icon-note' id='editBtn_" + treeNode.tId + "' title='修改' onfocus='this.blur();'></span>";
		var upStr = "<span style='margin: 0px 5px;' class='font-red-thunderbird icon-arrow-up' id='upBtn_" + treeNode.tId + "' title='上移' onfocus='this.blur();'></span>";
		var downStr = "<span style='margin: 0px 5px;' class='font-red-sunglo icon-arrow-down' id='downBtn_" + treeNode.tId + "' title='下移' onfocus='this.blur();'></span>";
		var setStr = "<span style='margin: 0px 5px;' class='font-green icon-reload' id='setBtn_" + treeNode.tId + "' title='状态' onfocus='this.blur();'></span>";
		sObj.append(addStr);
		if(treeNode.pId!=null){
			sObj.append(editStr);
			sObj.append(upStr);
			sObj.append(downStr);
			sObj.append(setStr);
		}
		//绑定事件
		//状态设置
		var setBtn = $("#setBtn_"+treeNode.tId);
		if (setBtn) setBtn.bind("click", function(){
			var pId = "0";
			if(treeNode.pId!=null){
				pId = treeNode.pId
			}
			$.ajax({
				url:ctx+"/doccatalog/set",
				type : "POST",
				data : "id="+treeNode.id+"&enabled="+treeNode.ENABLED,
				success : function(result) {
					var treeObj = $.fn.zTree.getZTreeObj("typeIdIdtree");
					var node = treeObj.getNodeByParam("id", pId, null);
					treeObj.reAsyncChildNodes(node, "refresh");
				}
			});
		});
		//上
		var upBtn = $("#upBtn_"+treeNode.tId);
		if (upBtn) upBtn.bind("click", function(){
			var pId = "0";
			if(treeNode.pId!=null){
				pId = treeNode.pId
			}
			$.ajax({
				url:ctx+"/doccatalog/sortup",
				type : "POST",
				data : "id="+treeNode.id+"&pId="+pId,
				success : function(result) {
					if(result=="nomove"){
						window.parent.toastr[MES_INFO]("已在第一位！");
					}else{
						var treeObj = $.fn.zTree.getZTreeObj("typeIdIdtree");
						var node = treeObj.getNodeByParam("id", pId, null);
						treeObj.reAsyncChildNodes(node, "refresh");
					}
				},
				error : function() {
					window.parent.toastr[MES_ERROR]("数据保存失败！");
				}
			});
		});
		//下
		var downBtn = $("#downBtn_"+treeNode.tId);
		if (downBtn) downBtn.bind("click", function(){
			var pId = "0";
			if(treeNode.pId!=null){
				pId = treeNode.pId
			}
			$.ajax({
				url:ctx+"/doccatalog/sortdown",
				type : "POST",
				data : "id="+treeNode.id+"&pId="+pId,
				success : function(result) {
					if(result=="nomove"){
						window.parent.toastr[MES_INFO]("已在末位！");
					}else{
						var treeObj = $.fn.zTree.getZTreeObj("typeIdIdtree");
						var node = treeObj.getNodeByParam("id", pId, null);
						treeObj.reAsyncChildNodes(node, "refresh");
					}
				},
				error : function() {
					window.parent.toastr[MES_ERROR]("数据保存失败！");
				}
			});
		});
		//添加
		var btnadd = $("#addBtn_"+treeNode.tId);
		if (btnadd) btnadd.bind("click", function(){
			$("#itemForm input[name=ID]").val("");
			$("#PARENT_ID").val(treeNode.name);
			$("#itemForm input[name=PARENT_ID]").val(treeNode.id);
			$("h4").text("新增");
			$('#itemModal').modal('show');
			$('#itemModal').on('shown.bs.modal', function() {
				$('input[name=NAME]').focus();
			});
		});
		//修改
		var btnedit = $("#editBtn_"+treeNode.tId);
		if (btnedit) btnedit.bind("click", function(){
			$("#itemForm input[name=ID]").val(treeNode.id);
			$("#itemForm input[name=NAME]").val(treeNode.name);
			var pnode = treeNode.getParentNode();
			$("#PARENT_ID").val(pnode.name);
			$("#itemForm input[name=PARENT_ID]").val(pnode.id);
			$("h4").text("修改");
			$('#itemModal').modal('show');
			$('#itemModal').on('shown.bs.modal', function() {
				$('input[name=NAME]').focus();
			});
		});
	};
	function removeHoverDom(treeId, treeNode) {
		$("#addBtn_"+treeNode.tId).unbind().remove();
		$("#editBtn_"+treeNode.tId).unbind().remove();
		$("#upBtn_"+treeNode.tId).unbind().remove();
		$("#downBtn_"+treeNode.tId).unbind().remove();
		$("#setBtn_"+treeNode.tId).unbind().remove();
	};
	$.ajax({
		url:ctx+"/doccatalog/tree",
		type:"POST",
		dataType:"json",
		success:function(data){
			data.push({id:0,name:"知识库目录",open:true});
			$.fn.zTree.init($("#typeIdIdtree"), setting, data);
		}
	});
}
function btn_save_click(){
	var pId = $("#itemForm input[name=PARENT_ID]").val();
	var url = "doccatalog/add";
	if ($("#itemForm input[name=ID]").val()!="") {
		url = "doccatalog/edit";
	}
	var form = $("#itemForm");
	if (form.valid()) {
		$.ajax({
			url:ctx+"/"+ url,
			type: "POST",
			data: $("#itemForm").serialize(),
			success: function (result) {
				if (result == "success") {
					$("#itemModal").modal("hide");
					window.parent.toastr[MES_SUCCESS]("保存成功");
					var treeObj = $.fn.zTree.getZTreeObj("typeIdIdtree");
					var node = treeObj.getNodeByParam("id", pId, null);
					treeObj.reAsyncChildNodes(node, "refresh");
					btn_close_click();
				} else {
					window.parent.toastr[MES_ERROR]("保存失败");
				}
			},
			error: function () {
				window.parent.toastr[MES_ERROR]("数据保存失败！");
			}
		});
	}
}
//验证
$("#itemForm").validate({
	rules : {
		ID:{
			required : true,
			number:true,
			maxlength : 6
		},
		NAME:{
			required : true,
			maxlength:100
		}
	},
	messages : {
		ID:{
			required : "请输入地区编码",
			number:"请输入6位数字",
			maxlength : "请输入6位数字"
		},
		NAME:{
			required : "请输入地区名称",
			maxlength:"内容过长"
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