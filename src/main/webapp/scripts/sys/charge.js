	$(document).ready(function() {
		$("#treediv").css("height",$(window).height()-50);
		//初始部门树
		inntlisttree();inntdepttree();
	});
	//tree
	function inntlisttree(){
		var setting = {
			async:{
				enable:true,
				type:"post",
				url:ctx+"/charge/tree",
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
					url:ctx+"/charge/set",
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
					url:ctx+"/charge/sortup",
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
					url : ctx+"charge/sortdown",
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
				$("#itemForm input[name=CHARGEID]").val("");
				$("#PARENT_ID").val(treeNode.name);
				$("#itemForm input[name=PARENT_ID]").val(treeNode.id);
				$("h4").text("新增");
				$('#itemModal').modal('show');
				$('#itemModal').on('shown.bs.modal', function() {
					$('input[name=CHARGENAME]').focus();
				});
			});
			//修改
			var btnedit = $("#editBtn_"+treeNode.tId);
			if (btnedit) btnedit.bind("click", function(){
				$("#itemForm select[name=ZRLY]").val(treeNode.ZRLY);
				$("#itemForm input[name=CHARGEID]").val(treeNode.id);
				$("#itemForm input[name=CHARGENAME]").val(treeNode.name);
				var pnode = treeNode.getParentNode();
				$("#PARENT_ID").val(pnode.name);
				$("#itemForm input[name=PARENT_ID]").val(pnode.id);
				if(treeNode.DEPTNAME){
					$("#itemForm input[name=DEPTNAME]").val(treeNode.DEPTNAME);
					$("#itemForm input[name=DEPTID]").val(treeNode.DEPTID);
				}
				$("h4").text("修改");
				$('#itemModal').modal('show');
				$('#itemModal').on('shown.bs.modal', function() {
					$('input[name=CHARGENAME]').focus();
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
			url:ctx+"/charge/tree",
			type:"POST",
			dataType:"json",
			success:function(data){
				data.push({id:0,name:'犯罪类型字典',pId:null,open:true});
				$.fn.zTree.init($("#typeIdIdtree"), setting, data);
				//强行展开一级
				//ChildNodes($.fn.zTree.getZTreeObj("typeIdIdtree"));
			}
		});
	}
	function btn_save_click(){
		var pId = $("#itemForm input[name=PARENT_ID]").val();
		var url = "charge/add";
		if ($("#itemForm input[name=CHARGEID]").val() != "") {
			url = "charge/edit";
		}
		var form = $("#itemForm");
		if (form.valid()) {
		 	var PINGYIN = pinyin.getFullChars($("#itemForm input[name=CHARGENAME]").val());
			var SHOUZIMU = pinyin.getCamelChars($("#itemForm input[name=CHARGENAME]").val());
			PINGYIN=  PINGYIN.toLocaleLowerCase()+PINGYIN.toLocaleUpperCase();
			SHOUZIMU= SHOUZIMU.toLocaleLowerCase();
			$.ajax({
				url:ctx+"/"+ url,
				type: "POST",
				data: $("#itemForm").serialize()+"&PINGYIN="+PINGYIN+"&SHOUZIMU="+SHOUZIMU,
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
	//tree
	function inntdepttree(){
		var setting = {
			async:{
				enable:true,
				type:"post",
				url:ctx+"/dept/tree",
				datatype:"json",
				autoParam: ["id"]
			},
			view: {
				dblClickExpand: true,
				showLine: true,
				selectedMulti: false,
			},
			data: {
				simpleData: {
					enable:true
				}
			},callback: {
				onClick: function(event,treeId, treeNode) {
					$("#itemForm input[name=DEPTNAME]").val(treeNode.name);
					$("#itemForm input[name=DEPTID]").val(treeNode.id);
					hideMenu();
				}
			}
		};
		//修改部门树加载全部部门
		$.ajax({
			url:ctx+"/dept/tree",
			type:"POST",
			data:{id:"0"},
			dataType:"json",
			success:function(data){
				$.fn.zTree.init($("#depttree"), setting, data);
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
	//验证
	$("#itemForm").validate({
		rules : {
			CHARGENAME:{
				required : true,
				maxlength:200
			},
			DEPTNAME : {
				required : true
			}
		},
		messages : {
			CHARGENAME:{
				required : "请输入犯罪名称",
				maxlength:"内容过长"
			},
			DEPTNAME : {
				required : "请选择部门",
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