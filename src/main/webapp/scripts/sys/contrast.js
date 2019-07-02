	$(document).ready(function() {
		initpTable();
	});
	var pData=null;
	//表格初始化
	function initpTable() {
		var table = $('#pTable');
		var oTable = table.dataTable({
			dom:"<'row'<'search text-left'f>r>t<'row pageRow'<'col-sm-3 col-md-3 col-lg-3 data_len'l><'col-sm-3 col-md-3 col-lg-3'i><'col-sm-6 col-md-6 col-lg-6'p>>",
			scrollX:true,
			scrollY:true,
			processing : true,
			serverSide : true,
			bSort : false,
			searching : false,
			pagingType : "simple",
			lengthChange : true,
			deferRender: true,
			bDestroy:true,
			lengthMenu : [ 10, 25, 50 ],
			ajax : {
				url:ctx+"/contrast/list",
				type : "POST",
				data:{pId:"0",SOURCE:"0",TARGET:"0"},
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
					data : "ID",
					width:'70px',
					sClass:"text-center",
				},{
					data : "SOURCENAME",
				}, {
					data : "TARGETNAME",
				}, {
					data : "DESCN",
				}
			],
			aoColumnDefs : [
				{
					targets : 1,
					data : "ID",
					render : function(data, type, full) { //返回自定义内容
						var addStr = "<span style='margin: 0px 10px;cursor: pointer;' onclick='Bindevent(this,event)' class='font-blue icon-plus' opt='addBtn' title='添加对照关系' onfocus='this.blur();'></span>";
						var editStr = "<span style='margin: 0px 10px;cursor: pointer;' onclick='Bindevent(this,event)' class='font-blue-steel icon-note' opt='editBtn' title='修改' onfocus='this.blur();'></span>";
						return addStr+=editStr;
					}
				}
			],
			language : {
				url:ctx+"/resources/lang-zh_CN.json"
			},initComplete:function () {
				tableHeight();
			}
		});
		//行点击
		$('#pTable tbody').on('click', 'tr', function (e) {
			e.preventDefault();
			pData = table.DataTable().row($(this)).data();
			initTable({pId:pData.ID,SOURCE:pData.SOURCEID,TARGET:pData.TARGETID});
		} );
	}
	function initTable(data) {
		var table = $('#Table');
		var oTable = table.dataTable({
			dom:"<'row'<'search text-left'f>r>t<'row pageRow'<'col-sm-3 col-md-3 col-lg-3 data_len'l><'col-sm-3 col-md-3 col-lg-3'i><'col-sm-6 col-md-6 col-lg-6'p>>",
			scrollX:true,
			scrollY:true,
			processing : true,
			serverSide : true,
			bSort : false,
			searching : false,
			pagingType : "simple",
			lengthChange : true,
			deferRender: true,
			bDestroy:true,
			lengthMenu : [ 10, 25, 50 ],
			ajax : {
				url:ctx+"/contrast/list",
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
					data : "ID",
					width:'50px',
					sClass:"text-center",
				},{
					data : "SOURCENAME",
				}, {
					data : "TARGETNAME",
				}, {
					data : "DESCN",
				}
			],
			aoColumnDefs : [
				{
					targets : 1,
					data : "ID",
					render : function(data, type, full) { //返回自定义内容
						var editStr = "<span style='margin: 0px 10px;cursor: pointer;' onclick='Bindevent(this,event)' class='font-blue-steel icon-note' opt='editBtn' title='修改' onfocus='this.blur();'></span>";
						return editStr;
					}
				}
			],
			language : {
				url:ctx+"/resources/lang-zh_CN.json"
			},initComplete:function () {
				tableHeight();
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
		$("#itemTable").dataTable().fnProcessingIndicator(false );
	}
	function Bindevent(obj,e) {
		e.stopPropagation();
		var full = $(obj).parents("table").DataTable().row($(obj).parents("tr")).data();
		//绑定事件
		switch ($(obj).attr("opt")){
			case "addBtn":
				var filter = getresource({pId:full.ID,SOURCE:full.SOURCEID,TARGET:full.TARGETID,length:-1});
				pData = full;
				getdic($("#itemForm select[name=SOURCE_CODE]"),filter,{pId:full.SOURCEID,FLAG:"1"});
				getdic($("#itemForm select[name=TARGET_CODE]"),[],{pId:full.TARGETID,FLAG:"1"});
				$("h4").text("新增"+full.SOURCENAME+"-->"+full.TARGETNAME+"对应关系");
				$("#itemForm input[name=ID]").val("");
				$("#itemForm input[name=PARENT_ID]").val(full.ID);
				$('#itemModal').modal('show');
				$('#itemModal').on('shown.bs.modal', function() {
					$('input[name=SOURCE_CODE]').focus();
				});
				break;
			case "editBtn":
				if(full.PARENT_ID==0){
					// var filter = getresource($("#pTable"));
					// filter.splice($.inArray(full.SOURCE_CODE,filter),1);
					getdic($("#itemForm select[name=SOURCE_CODE]"),[]);
					getdic($("#itemForm select[name=TARGET_CODE]"));
					$("#itemForm input[name=PARENT_ID]").val(0);
				}else{
					var filter = getresource({pId:pData.ID,SOURCE:pData.SOURCEID,TARGET:pData.TARGETID,length:-1});
					filter.splice($.inArray(full.SOURCE_CODE,filter),1);
					getdic($("#itemForm select[name=SOURCE_CODE]"),filter,{pId:pData.SOURCEID,FLAG:"1"});
					getdic($("#itemForm select[name=TARGET_CODE]"),[],{pId:pData.TARGETID,FLAG:"1"});
					$("#itemForm input[name=PARENT_ID]").val(pData.ID);
				}
				$("#itemForm select[name=SOURCE_CODE]").val(full.SOURCE_CODE);
				$("#itemForm select[name=TARGET_CODE]").val(full.TARGET_CODE);
				$("#itemForm input[name=DESCN]").val(full.DESCN);
				//设置状态修改 跳转修改url
				$("#itemForm input[name=ID]").val(full.ID);
				$("h4").text("修改");
				$('#itemModal').modal('show');
				$('#itemModal').on('shown.bs.modal', function() {
					$('input[name=SOURCE_CODE]').focus();
				});
				break;
		}
	}
	function btn_add_click() {
		//var filter = getresource($("#pTable"));
		getdic($("#itemForm select[name=SOURCE_CODE]"),[]);
		getdic($("#itemForm select[name=TARGET_CODE]"),[]);
		$("#itemForm input[name=PARENT_ID]").val(0);
		$("h4").text("新增字典项对照");
		$('#itemModal').modal('show');
		$('#itemModal').on('shown.bs.modal', function() {
			$('input[name=SOURCE_CODE]').focus();
		});
	}
	function btn_save_click(){
		var url = "contrast/add";
		if ($("#itemForm input[name=ID]").val() != "") {
			url = "contrast/edit";
		}
		var form = $("#itemForm");
		if (form.valid()) {
			$.ajax({
				url:ctx+"/"+url,
				type: "POST",
				data: $("#itemForm").serialize(),
				success: function (result) {
					if (result == "success") {
						$("#itemModal").modal("hide");
						window.parent.toastr[MES_SUCCESS]("保存成功");
						if($("#itemForm input[name=PARENT_ID]").val()!="0"){
							initTable({pId:pData.ID,SOURCE:pData.SOURCEID,TARGET:pData.TARGETID});
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
	}
	//验证
	$("#itemForm").validate({
		rules : {
			SOURCE_CODE : {
				required : true
			},
			TARGET_CODE : {
				required : true
			},
			DESCN:{
				maxlength:255
			}
		},
		messages : {
			SOURCE_CODE : {
				required : "请选择",
			},
			TARGET_CODE : {
				required : "请选择",
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
	//获取需要过滤的数据
	function getresource(param) {
		var text = [];
		$.ajax({
			url:ctx+"/contrast/list",
			type: "POST",
			data:param,
			async:false,
			success: function (result) {
				$.each(result.data,function () {
					text.push(this.SOURCE_CODE);
				});
			}
		});
		return  text;
	}
	function getdic(obj,filter,param) {
		$.ajax({
			url:ctx+"/contrast/getdic",
			type: "POST",
			async:false,
			data: param,
			success: function (result) {
				var html = "<option hidden='hidden'></option>";
				$.each(result,function () {
					if($.inArray(this.item_CODE,filter)==-1){
						html+="<option value='"+this.item_CODE+"' ";
						html+=">"+this.item_NAME+"</option>";
					}
				});
				obj.html(html);
			},
			error: function () {
				window.parent.toastr[MES_ERROR]("数据保存失败！");
			}
		});
	}
	function dicvila() {
		if($("#itemForm input[name=PARENT_ID]").val()==0) {
			var source = $("#itemForm select[name=SOURCE_CODE]").val();
			var target = $("#itemForm select[name=TARGET_CODE]").val();
			if (source == target) {
				window.parent.toastr[MES_WARN]("源字典与目标字典不能相同,请重新选择！");
				$("#itemForm select[name=TARGET_CODE]").val("");
			}
		}
	}