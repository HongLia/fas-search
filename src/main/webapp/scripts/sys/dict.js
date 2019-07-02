	$(document).ready(function() {
		initpTable();
	});
	var editid="";
	var pData=null;
	//表格初始化
	var pserchvalue;
	var serchvalue;
	function initpTable() {
		var table = $('#pTable');
		var oTable = table.dataTable({
			dom:"<'row'<'search text-left'f>r>t<'row pageRow'<'col-sm-3 col-md-3 col-lg-3 data_len'l><'col-sm-3 col-md-3 col-lg-3'i><'col-sm-6 col-md-6 col-lg-6'p>>",
			scrollX:true,
			scrollY:true,
			processing : true,
			serverSide : true,
			bSort : false,
			searching : true,
			pagingType : "simple",
			lengthChange : true,
			deferRender: true,
			bDestroy:true,
			lengthMenu : [ 10, 25, 50 ],
			ajax : {
				url:ctx+"/dict/list",
				type : "POST",
				data:{
					searchValue:$("#pTable_filter input").val(),
				},
				error: AjaxError
			},
			select : {
				style : 'single'
			},
			columns : [
				{
					data:null,
					width:'30px',
					sClass:"text-center",
					fnCreatedCell:function (nTd,sData,oData,iRow,iCol) {
						var startnum=this.api().page()*(this.api().page.info().length);
						$(nTd).html(iRow+1+startnum);
					}
				}, {
					sClass:"text-center",
					width:'150px',
					data : "ID",
				},{
					data : "ITEM_CODE",
				}, {
					data : "ITEM_NAME",
				}, {
					data : "DESCN",
				}, {
					data : "ENABLED",
				}
			],
			aoColumnDefs : [
				{
					targets : 5,
					data : "ENABLED",
					render : function(data, type, full) { //返回自定义内容
						if (data == "0") {
							return "<span class='label label-sm label-primary'>正常</span>";
						} else {
							return "<span class='label label-sm label-danger'>注销</span>";
						}
					}
				},
				{
					targets : 1,
					data : "ID",
					render : function(data, type, full) { //返回自定义内容
						var addStr = "<span style='margin: 0px 10px;cursor: pointer;' onclick='Bindevent(this,event)' class='font-blue icon-plus' opt='addBtn' title='添加子节点' onfocus='this.blur();'></span>";
						var editStr = "<span style='margin: 0px 10px;cursor: pointer;' onclick='Bindevent(this,event)' class='font-blue-steel icon-note' opt='editBtn' title='修改' onfocus='this.blur();'></span>";
						var setStr = "<span style='margin: 0px 10px;cursor: pointer;' onclick='Bindevent(this,event)' class='font-green icon-reload' opt='setBtn' title='状态' onfocus='this.blur();'></span>";
						return addStr+=editStr+=setStr;
					}
				}
			],
			language : {
				url:ctx+"/resources/lang-zh_CN.json"
			},
			initComplete:function () {
				tableHeight();
				$("#pTable_filter input").attr("placeholder","字典编码");
				$("#pTable_filter input").val(pserchvalue);
				$("#pTable_filter input").focus();
				$("#pTable_filter").append('<i id="btn-search-i" class="fa fa-search" style="cursor:pointer;margin:6px 0px 1px 1px;"></i>');
				$("#pTable_filter").addClass("input-icon right");
				$("#pTable_filter input").unbind();
				$("#pTable_filter input").on("keypress",function(e){
					if(e.keyCode!=13)return;
					pserchvalue=$(this).val();
					initpTable();
				});
				$("#pTable_filter i").on("click",function(e){
					pserchvalue=$("#pTable_filter input").val();
					initpTable();
				});
			}
		});
		//行点击
		$('#pTable tbody').on('click', 'tr', function (e) {
			e.preventDefault();
			pData = table.DataTable().row($(this)).data();
			initTable({pId:pData.ID,FLAG:"1"});
		} );
	}
	function initTable(data) {
		if(data){
			data.searchValue=$("#Table_filter input").val();
		}else{
			data={pId:pData.ID,FLAG:"1",searchValue:$("#Table_filter input").val()}
		}
		var table = $('#Table');
		var oTable = table.dataTable({
			dom:"<'row'<'search text-left'f>r>t<'row pageRow'<'col-sm-3 col-md-3 col-lg-3 data_len'l><'col-sm-3 col-md-3 col-lg-3'i><'col-sm-6 col-md-6 col-lg-6'p>>",
			scrollX:true,
			scrollY:true,
			processing : true,
			serverSide : true,
			bSort : false,
			searching : true,
			pagingType : "simple",
			lengthChange : true,
			deferRender: true,
			bDestroy:true,
			lengthMenu : [ 10, 25, 50 ],
			ajax : {
				url:ctx+"/dict/list",
				type : "POST",
				data:data,
				error: AjaxError
			},
			select : {
				style : 'single'
			},
			columns : [
				{
					data:null,
					width:'31px',
					sClass:"text-center",
					fnCreatedCell:function (nTd,sData,oData,iRow,iCol) {
						var startnum=this.api().page()*(this.api().page.info().length);
						$(nTd).html(iRow+1+startnum);
					}
				}, {
					width:'150px',
					sClass:"text-center",
					data : "ID",
				},{
					data : "ITEM_CODE",
				}, {
					data : "ITEM_NAME",
				}, {
					data : "ENABLED",
				}
			],
			aoColumnDefs : [
				{
					targets : 4,
					data : "ENABLED",
					render : function(data, type, full) { //返回自定义内容
						if (data == "0") {
							return "<span class='label label-sm label-primary'>正常</span>";
						} else {
							return "<span class='label label-sm label-danger'>注销</span>";
						}
					}
				},
				{
					targets : 1,
					data : "ID",
					render : function(data, type, full) { //返回自定义内容
						var editStr = "<span style='margin: 0px 10px;cursor: pointer;' onclick='Bindevent(this,event)' class='font-blue-steel icon-note' opt='editBtn' title='修改' onfocus='this.blur();'></span>";
						var upStr = "<span style='margin: 0px 10px;cursor: pointer;' onclick='Bindevent(this,event)' class='font-red-thunderbird icon-arrow-up' opt='upBtn' title='上移' onfocus='this.blur();'></span>";
						var downStr = "<span style='margin: 0px 10px;cursor: pointer;' onclick='Bindevent(this,event)' class='font-red-sunglo icon-arrow-down' opt='downBtn' title='下移' onfocus='this.blur();'></span>";
						var setStr = "<span style='margin: 0px 10px;cursor: pointer;' onclick='Bindevent(this,event)' class='font-green icon-reload' opt='setBtn' title='状态' onfocus='this.blur();'></span>";
						return editStr+=upStr+=downStr+=setStr;
					}
				}
			],
			language : {
				url:ctx+"/resources/lang-zh_CN.json"
			},
			initComplete:function () {
				tableHeight();
				$("#Table_filter input").attr("placeholder","字典编码");
				$("#Table_filter input").val(serchvalue);
				$("#Table_filter input").focus();
				$("#Table_filter").append('<i id="btn-search-i" class="fa fa-search" style="cursor:pointer;margin:6px 0px 1px 1px;"></i>');
				$("#Table_filter").addClass("input-icon right");
				$("#Table_filter input").unbind();
				$("#Table_filter input").on("keypress",function(e){
					if(e.keyCode!=13)return;
					serchvalue=$(this).val();
					initTable();
				});
				$("#Table_filter i").on("click",function(e){
					serchvalue=$("#Table_filter input").val();
					initTable();
				});
			}
		});
		table.show();
	}

	//异常错误提示
	function AjaxError( xhr, textStatus, error ) {
		if ( textStatus === 'timeout' ) {
			window.parent.toastr[MES_ERROR]("服务器没有响应！");
		}
		else {
			window.parent.toastr[MES_ERROR]("服务器出错,请重试！");
		}
		$("#pTable").dataTable().fnProcessingIndicator(false );
	}
	function Bindevent(obj,e) {
		e.stopPropagation();
		var full = $(obj).parents("table").DataTable().row($(obj).parents("tr")).data();
		//绑定事件
		switch ($(obj).attr("opt")){
			case "setBtn":
				$.ajax({
					url: ctx + "/dict/set",
					type: "POST",
					data: "id=" + full.ID + "&enabled=" + full.ENABLED,
					success: function (result) {
						if (full.PARENT_ID == 0) {
							initpTable();
						} else {
							initTable({pId: pData.ID, FLAG: "1"});
						}
					}
				});
				break;
			case "upBtn":
				var pId = "0";
				if(full.PARENT_ID!=null){
					pId = full.PARENT_ID
				}
				$.ajax({
					url:ctx+"/dict/sortup",
					type : "POST",
					data : "id="+full.ID+"&pId="+pId,
					success : function(result) {
						if(result=="nomove"){
							window.parent.toastr[MES_INFO]("已在第一位！");
						}else{
							initTable({pId:pData.ID,FLAG:"1"});
						}
					},
					error : function() {
						window.parent.toastr[MES_ERROR]("数据保存失败！");
					}
				});
				break;
			case "downBtn":
				var pId = "0";
				if(full.PARENT_ID!=null){
					pId = full.PARENT_ID
				}
				$.ajax({
					url:ctx+"/dict/sortdown",
					type : "POST",
					data : "id="+full.ID+"&pId="+pId,
					success : function(result) {
						if(result=="nomove"){
							window.parent.toastr[MES_INFO]("已在末位！");
						}else{
							initTable({pId:pData.ID,FLAG:"1"});
						}
					},
					error : function() {
						window.parent.toastr[MES_ERROR]("数据保存失败！");
					}
				});
				break;
			case "addBtn":
				pData = full;
				if(full.ID==0){
					$("#itemForm select[name=FLAG]").html(
						'<option value="0">字典项</option>'
					);
				}else{
					$("#itemForm select[name=FLAG]").html(
						'<option value="1">字典值</option>'
					);
				}
				editid=null;
				$("#PARENT_ID").val(full.ITEM_NAME);
				$("#itemForm input[name=ID]").val("");
				$("#itemForm input[name=PARENT_ID]").val(full.ID);
				$("h4").text("新增");
				$('#itemModal').modal('show');
				$('#itemModal').on('shown.bs.modal', function() {
					$('input[name=ITEM_CODE]').focus();
				});
				break;
			case "editBtn":
				if(full.PARENT_ID==0){
					$("#itemForm select[name=FLAG]").html(
						'<option value="0">字典项</option>'
					);
					$("#PARENT_ID").val("业务字典根节点");
					$("#itemForm input[name=PARENT_ID]").val(0);
				}else{
					$("#itemForm select[name=FLAG]").html(
						'<option value="1">字典值</option>'
					);
					$("#PARENT_ID").val(pData.ITEM_NAME);
					$("#itemForm input[name=PARENT_ID]").val(pData.ID);
				}
				$("#itemForm input[name=ITEM_CODE]").val(full.ITEM_CODE);
				$("#itemForm input[name=ITEM_NAME]").val(full.ITEM_NAME);
				$("#itemForm input[name=DESCN]").val(full.DESCN);
				//设置状态修改 跳转修改url
				$("#itemForm input[name=ID]").val(full.ID);
				editid =full.ITEM_CODE;
				$("h4").text("修改");
				$('#itemModal').modal('show');
				$('#itemModal').on('shown.bs.modal', function() {
					$('input[name=DEPTID]').focus();
				});
				break;
		}
	}
	function btn_add_click() {
		$("#itemForm select[name=FLAG]").html(
			'<option value="0">字典项</option>'
		);
		editid=null;
		$("#PARENT_ID").val("业务字典根节点");
		$("#itemForm input[name=PARENT_ID]").val(0);
		$("h4").text("新增");
		$('#itemModal').modal('show');
		$('#itemModal').on('shown.bs.modal', function() {
			$('input[name=ITEM_CODE]').focus();
		});
	}
	function btn_save_click(){
		$.ajax({
			url:ctx+"/dict/itemcodeisone",
			type : "POST",
			data : {
				itemcode:$("#itemForm input[name=ITEM_CODE]").val(),
				pId:$("#itemForm input[name=PARENT_ID]").val()
			},
			async: false,
			cache: false,
			success : function(result) {
				if (result == "success" ||$("#itemForm input[name=ITEM_CODE]").val() == editid) {
					var url = "dict/add";
					if ($("#itemForm input[name=ID]").val() != "") {
						url = "dict/edit";
					}
					var form = $("#itemForm");
					if (form.valid()) {
						$.ajax({
							url:ctx+"/"+url,
							type: "POST",
							data: $("#itemForm").serialize() + "&DEPT_FUN=" + $("#itemForm select[name=DEPT_CATE]").find("option:selected").text(),
							success: function (result) {
								if (result == "success") {
									$("#itemModal").modal("hide");
									window.parent.toastr[MES_SUCCESS]("保存成功");
									if($("#itemForm input[name=PARENT_ID]").val()!="0"){
										initTable({pId:pData.ID,FLAG:"1"});
									}else{
										initpTable();
									}
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
				}else{
					window.parent.toastr[MES_WARN]("字典项代码已存在");
					$("#itemForm input[name=ITEM_CODE]").val("");
				}
			}
		});
	}
	//验证
	$("#itemForm").validate({
		rules : {
			ITEM_CODE : {
				required : true,
				maxlength : 30
			},
			ITEM_NAME : {
				required : true,
				maxlength : 100
			}
		},
		messages : {
			ITEM_CODE : {
				required : "请输入字典代码",
				maxlength : "内容过长."
			},
			ITEM_NAME : {
				required : "请输入字典名称",
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