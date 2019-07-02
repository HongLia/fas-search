	$(document).ready(function() {
		$('.bs-select').selectpicker({
			iconBase: 'fa',
			tickIcon: 'fa-check'
		});
		$.cookie("z_tree",null);
		$("#treediv").css("height",$(window).height()-110);
		//初始部门树
		innttree();
	});
	//tree
	function innttree(){
		var setting = {
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
			},callback: {
				onExpand: onExpand,
				onCollapse: onCollapse
			}
		};
		$.ajax({
			url:ctx+"/resource/tree",
			type:"POST",
			data:{
				appid:$("#APP").val(),
				type:"-1"
			},
			dataType:"json",
			success:function(data){
				$.fn.zTree.init($("#typeIdIdtree"), setting, data);
				var treeObj = $.fn.zTree.getZTreeObj("typeIdIdtree");
				//添加菜单按钮
				$.ajax({
					url:ctx+"/resource/tree",
					type:"POST",
					data:{
						appid:$("#APP").val(),
						type:"0"
					},
					dataType:"json",
					success:function(datacd){
						treeObj.addNodes(treeObj.getNodeByParam("type", 0, null), datacd,false);
						cookieztree(treeObj);
					}
				});
				//添加其他
				$.ajax({
					url:ctx+"/resource/tree",
					type:"POST",
					data:{
						appid:$("#APP").val(),
						type:"2"
					},
					dataType:"json",
					success:function(dataan){
						treeObj.addNodes(treeObj.getNodeByParam("type",2, null), dataan, false);
						cookieztree(treeObj);
					}
				});
			}
		});
	}
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
			//非按钮 显示 添加修改功能 按钮显示修改
			if(treeNode.type!=1){
				sObj.append(addStr);
				//一级不显示修改按钮
				if(treeNode.id!=0){
					sObj.append(editStr);
					sObj.append(upStr);
					sObj.append(downStr);
					sObj.append(setStr);
				}
			}else{
				sObj.append(editStr);
				sObj.append(upStr);
				sObj.append(downStr);
				sObj.append(setStr);
			}
			//绑定事件
			//状态设置
			var setBtn = $("#setBtn_"+treeNode.tId);
			if (setBtn) setBtn.bind("click", function(){
				$.ajax({
					url:ctx+"/resource/set",
					type : "POST",
					data : "id="+treeNode.id+"&enabled="+treeNode.ENABLED
				});
				if(treeNode.children){
					var result=[];
					result = getAllChildrenNodes(treeNode,result);
					$.each(result,function () {
						$.ajax({
							url:ctx+"/resource/set",
							type : "POST",
							data : "id="+this+"&enabled="+treeNode.ENABLED
						});
					});
				}
				innttree();
			});
			//添加
			var btnadd = $("#addBtn_"+treeNode.tId);
			if (btnadd) btnadd.bind("click", function(){
				//adddataauthority(treeNode);
				$("#APP_ID").val($("#APP option:selected").text());
				$("#itemForm input[name=APP_ID]").val($("#APP").val());
				$("#PARENTID").val(treeNode.name);
				$("#itemForm input[name=PARENTID]").val(treeNode.id);

				if(treeNode.type!=2){
					//根菜单只能添加菜单
					if(treeNode.id==0){
						$("#itemForm select[name=TYPE]").html(
							'<option value="0">菜单</option>'
						);
					}else{
						$("#itemForm select[name=TYPE]").html(
							'<option value="0">菜单</option> <option value="1">按钮</option>'
						);
					}
				}else{
					$("#itemForm select[name=TYPE]").html(
						'<option value="2">其他</option>'
					);
				}

				$("h4").text("新增");
				$('#itemModal').modal('show');
				$('#itemModal').on('shown.bs.modal', function() {
					$('input[name=DEPTID]').focus();
				});
			});
			//修改
			var btnedit = $("#editBtn_"+treeNode.tId);
			if (btnedit) btnedit.bind("click", function(){
				//adddataauthority(treeNode);
				$("#APP_ID").val($("#APP option:selected").text());
				$("#itemForm input[name=APP_ID]").val($("#APP").val());
				$("#PARENTID").val(treeNode.getParentNode().name);
				$("#itemForm input[name=PARENTID]").val(treeNode.getParentNode().id);

				if(treeNode.type!=2){
					//父级为根目录的只能修改为菜单
					if(treeNode.pId==0){
						$("#itemForm select[name=TYPE]").html(
							'<option value="0">菜单</option>'
						);
					}else{
						$("#itemForm select[name=TYPE]").html(
							'<option value="0">菜单</option> <option value="1">按钮</option>'
						);
					}
					$("#itemForm input[name=NAME]").val(treeNode.NAME);
					$("#itemForm input[name=RES_KEY]").val(treeNode.RES_KEY);
				}else{
					$("#itemForm select[name=TYPE]").html(
						'<option value="2">其他</option>'
					);
					//$("#NAME").val(treeNode.RES_KEY);
					// $("#itemForm input[name=NAME]").val($("#NAME option:selected").text());
					// $("#itemForm input[name=RES_KEY]").val($("#NAME").val());
					$("#itemForm input[name=NAME]").val(treeNode.NAME);
					$("#itemForm input[name=RES_KEY]").val(treeNode.RES_KEY);
				}
				$("#itemForm input[name=ACTION]").val(treeNode.ACTION);
				$("#itemForm input[name=ICONCLS]").val(treeNode.ICONCLS);
				$("#itemForm input[name=DESCN]").val(treeNode.DESCN);
				$("#itemForm select[name=TYPE]").val(treeNode.type);
				$("#itemForm input[name=RESOURCEID]").val(treeNode.id);

				$("h4").text("修改");
				$('#itemModal').modal('show');
				$('#itemModal').on('shown.bs.modal', function() {
					$('input[name=DEPTID]').focus();
				});
			});
			//上
			var upBtn = $("#upBtn_"+treeNode.tId);
			if (upBtn) upBtn.bind("click", function(){
				$.ajax({
					url:ctx+"/resource/sortup",
					type : "POST",
					data : "id="+treeNode.id+"&pId="+treeNode.pId+"&appid="+$("#APP").val(),
					success : function(result) {
						if(result=="nomove"){
							window.parent.toastr[MES_INFO]("已在第一位！");
						}else{
							innttree();
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
				$.ajax({
					url:ctx+"/resource/sortdown",
					type : "POST",
					data : "id="+treeNode.id+"&pId="+treeNode.pId+"&appid="+$("#APP").val(),
					success : function(result) {
						if(result=="nomove"){
							window.parent.toastr[MES_INFO]("已在末位！");
						}else{
							innttree();
						}
					},
					error : function() {
						window.parent.toastr[MES_ERROR]("数据保存失败！");
					}
				});
			});
	};
	function removeHoverDom(treeId, treeNode) {
		$("#addBtn_" + treeNode.tId).unbind().remove();
		$("#editBtn_" + treeNode.tId).unbind().remove();
		$("#upBtn_"+treeNode.tId).unbind().remove();
		$("#downBtn_"+treeNode.tId).unbind().remove();
		$("#setBtn_"+treeNode.tId).unbind().remove();
	};
	function btn_save_click(){
		var url = "resource/add";
		if ($("#itemForm input[name=RESOURCEID]").val() != "") {
			url = "resource/edit";
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
						innttree();
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
	function seldataauthority() {
		$("#itemForm input[name=NAME]").val($("#NAME option:selected").text());
		$("#itemForm input[name=RES_KEY]").val($("#NAME").val());
	}
	//新增数据权限页面显示调整
	function adddataauthority(treeNode) {
		if(treeNode.type==2){
			$("#NAME").show();
			$("#itemForm input[name=RES_KEY]").attr("readOnly","readOnly");
			$("#itemForm input[name=NAME]").hide();
		}else {
			$("#NAME").hide();
			$("#itemForm input[name=RES_KEY]").removeAttr("readOnly");
			$("#itemForm input[name=NAME]").show();
		}
	}
	//验证
	$("#itemForm").validate({
		rules : {
			NAME : {
				required : true,
				maxlength : 100
			},
			RES_KEY:{
				maxlength : 30
			},
			ACTION:{
				maxlength : 500
			},
			DESCN:{
				maxlength : 255
			}
		},
		messages : {
			NAME : {
				required : "请输入资源名称",
				maxlength : "内容过长."
			},
			RES_KEY:{
				maxlength : "内容过长."
			},
			ACTION:{
				maxlength : "内容过长."
			},
			DESCN:{
				maxlength : "内容过长."
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
	//获取全部叶子节点
	function getAllChildrenNodes(treeNode,result){
		if (treeNode.isParent) {
			var childrenNodes = treeNode.children;
			if (childrenNodes) {
				for (var i = 0; i < childrenNodes.length; i++) {
					result.push(childrenNodes[i].id);
					result = getAllChildrenNodes(childrenNodes[i], result);
				}
			}
		}
		return result;
	}