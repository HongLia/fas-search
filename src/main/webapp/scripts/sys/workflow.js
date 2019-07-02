$(document).ready(function() {
	initTable();
	handleTable();
});
var oTable;//工作流配置表格对象
var WORKFLOWID;//当前选择工作流id
var WORKFLOWNAME;//当前选择工作流NAME
var nEditing = null;//当前编辑行
var parmkeys=[];//已存在key
function btn_save_pks() {
	if (nEditing !== null) {
		window.parent.toastr[MES_WARN]("当前存在编辑行,请先保存或取消！");
	}else{
		var rowdata =oTable.fnGetNodes();
		var workroles=[];
		for(var i=0;i<rowdata.length;i++){
			var temp =oTable.fnGetData(rowdata[i]);
			workroles.push(temp[0]+'-'+temp[1]+'-'+temp[3])
		}
		$.ajax({
			url:ctx+"/workflow/saveworkrole",
			type : "POST",
			data : {
				WORKFLOWID:WORKFLOWID,
				workroles:workroles
			},
			cache: false,
			success : function(result) {
				btn_clear_pks();
				window.parent.toastr[MES_SUCCESS]("数据保存成功！");
				$('#workroleModal').modal('hide');
			},
			error : function() {
				window.parent.toastr[MES_ERROR]("数据保存失败！");
			}
		});
	}
}
function btn_clear_pks() {
	nEditing = null;//当前编辑行
	parmkeys=[];//已存在key
	oTable.fnClearTable();
}
function getworkrolelist(WORKFLOW_ID) {
	parmkeys=[];
	$.ajax({
		url:ctx+"/workflow/workrolelist",
		type : "POST",
		data : {WORKFLOW_ID:WORKFLOW_ID},
		dataType:"json",
		async:false,
		cache: false,
		success : function(result) {
			if(result.length>0){
				$.each(result,function () {
					parmkeys.push(this[0]);
				});
				oTable.fnAddData(result);
			}
			$("h4").text("配置工作流");
			$('#workroleModal').modal('show');
		},
		error : function() {
			window.parent.toastr[MES_ERROR]("数据获取失败！");
		}
	});
}
var handleTable = function () {
	var roles=$("#roles").html();
	var save="确定";
	var cancel="删除";
	var edit="修改";
	var del="删除";
	var edipk=null;
	function restoreRow(oTable, nRow) {
		var aData = oTable.fnGetData(nRow);
		var jqTds = $('>td', nRow);

		for (var i = 0, iLen = jqTds.length; i < iLen; i++) {
			oTable.fnUpdate(aData[i], nRow, i, false);
		}

		oTable.fnDraw();
	}

	function editRow(oTable, nRow) {
		var aData = oTable.fnGetData(nRow);
		var jqTds = $('>td', nRow);
		jqTds[0].innerHTML = '<input type="text" class="form-control" value="' + aData[0] + '">';
		var temp =roles.indexOf(aData[1])+aData[1].length+1;
		if(roles.indexOf(aData[1])>0){
			jqTds[1].innerHTML = roles.substr(0,temp)+"selected='selectted'"+roles.substr(temp,roles.length);
		}else{
			jqTds[1].innerHTML= roles;
		}
		jqTds[2].innerHTML = WORKFLOWNAME;
		jqTds[3].innerHTML = '<input type="text" class="form-control" value="' + aData[3] + '">';
		jqTds[4].innerHTML = '<a class="edit" href="">'+save+'</a>';
		jqTds[5].innerHTML = '<a class="cancel" href="">'+cancel+'</a>';
	}

	function saveRow(oTable, nRow) {
		var jqInputs = $('.form-control', nRow);
		if(edipk!=null){
			parmkeys.splice($.inArray(edipk,parmkeys),1);
			edipk=null;
		}
		if(jqInputs[0].value!=""){
			if($.inArray(jqInputs[0].value,parmkeys)==-1){
				oTable.fnUpdate(jqInputs[0].value, nRow, 0, false);
				oTable.fnUpdate(jqInputs[1].value, nRow, 1, false);
				//oTable.fnUpdate(jqInputs[2].value, nRow, 2, false);
				oTable.fnUpdate(jqInputs[2].value, nRow, 3, false);
				oTable.fnUpdate('<a class="edit" href="">'+edit+'</a>', nRow, 4, false);
				oTable.fnUpdate('<a class="delete" href="">'+del+'</a>', nRow, 5, false);
				oTable.fnDraw();
				parmkeys.push(jqInputs[0].value);
			}else{
				window.parent.toastr[MES_WARN]("KEY已存在,请重新输入！");
				oTable.fnDeleteRow(nRow);
			}
		}else{
			window.parent.toastr[MES_WARN]("请输入参数编码！");
			oTable.fnDeleteRow(nRow);
		}
	}

	function cancelEditRow(oTable, nRow) {
		var jqInputs = $('input', nRow);
		oTable.fnUpdate(jqInputs[0].value, nRow, 0, false);
		oTable.fnUpdate(jqInputs[1].value, nRow, 1, false);
		oTable.fnUpdate(jqInputs[2].value, nRow, 2, false);
		oTable.fnUpdate(jqInputs[3].value, nRow, 3, false);
		oTable.fnUpdate('<a class="edit" href="">'+edit+'</a>', nRow, 4, false);
		oTable.fnDraw();
	}

	var table = $('#workroleTable');

	oTable = table.dataTable({
		processing : true,
		searching : true,
		pagingType : "full_numbers",
		lengthChange : true,
		deferRender: true,
		bDestroy:true,
		lengthMenu : [ 10, 25, 50 ],
		language : {
			url:ctx+"/resources/lang-zh_CN.json"
		}
	});
	var nNew = false;

	$('#sample_editable_1_new').click(function (e) {
		e.preventDefault();

		if (nEditing) {
			if (confirm("当前存在未编辑行,是否保存?")) {
				saveRow(oTable, nEditing); // save
				nEditing = null;
				nNew = false;

			} else {
				oTable.fnDeleteRow(nEditing); // cancel
				nEditing = null;
				nNew = false;

				return;
			}
		}

		var aiNew = oTable.fnAddData(['', '', '', '', '', '']);
		var nRow = oTable.fnGetNodes(aiNew[0]);
		editRow(oTable, nRow);
		nEditing = nRow;
		nNew = true;
	});

	table.on('click', '.delete', function (e) {
		e.preventDefault();

		if (confirm("确定删除该行数据么?") == false) {
			return;
		}

		var nRow = $(this).parents('tr')[0];
		oTable.fnDeleteRow(nRow);
		parmkeys.splice($.inArray($('>td', nRow)[0].innerHTML,parmkeys),1);
		//alert("Deleted! Do not forget to do some ajax to sync with backend :)");
	});

	table.on('click', '.cancel', function (e) {
		e.preventDefault();
		if (nNew) {
			oTable.fnDeleteRow(nEditing);
			nEditing = null;
			nNew = false;
		} else {
			restoreRow(oTable, nEditing);
			nEditing = null;
		}
	});

	table.on('click', '.edit', function (e) {
		e.preventDefault();

		/* Get the row as a parent of the link that was clicked on */
		var nRow = $(this).parents('tr')[0];

		if (nEditing !== null && nEditing != nRow) {
			/* Currently editing - but not this row - restore the old before continuing to edit mode */
			// restoreRow(oTable, nRow);
			// editRow(oTable, nRow);
			// nEditing = nRow;
			window.parent.toastr[MES_WARN]("当前存在编辑行,请先保存或取消！");
		} else if (nEditing == nRow && this.innerHTML == save) {
			/* Editing this row and want to save it */
			saveRow(oTable, nEditing);
			nEditing = null;
			//alert("Updated! Do not forget to do some ajax to sync with backend :)");
		} else {
			/* No edit in progress - let's start one */
			edipk=$('>td', nRow)[0].innerHTML;
			editRow(oTable, nRow);
			nEditing = nRow;
		}
	});
}

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
			url:ctx+"/workflow/list",
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
				sClass:"text-center",
				data : "WORKFLOW_ID"
			}, {
				data : "APP_ID"
			}, {
				data : "NAME"
			}, {
				data : "REMARKS"
			}, {
				data : "ENABLED"
			}
		],
		aoColumnDefs : [
			{
				targets : 1,
				data : "ENABLED",
				render : function(data, type, full) { //返回自定义内容
					//<i class='fa fa-tasks'></i>
					return "<a class='setworkrole'>工作流角色配置</a>";
				}
			},{
			targets : 5,
			data : "ENABLED",
			render : function(data, type, full) { //返回自定义内容
				if (data == "0") {
					return "<span class='label label-sm label-primary'>正常</span>";
				} else {
					return "<span class='label label-sm label-danger'>注销</span>";
				}
			}
		}
		],		
		language : {
			url:ctx+"/resources/lang-zh_CN.json"
		},initComplete:function () {
			tableHeight();
		}
	});
	//查看通告
	table.on('click', '.setworkrole', function (e) {
		e.preventDefault();
		var data = table.DataTable().row($(this).parents("tr")).data();
		WORKFLOWNAME=data.NAME;
		WORKFLOWID=data.WORKFLOW_ID;
		getworkrolelist(data.WORKFLOW_ID);
	})
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
	$("#itemForm input[name=WORKFLOW_ID]").val("");
	$("h4").text("新增");
	$('#itemModal').modal('show');
    $('#itemModal').on('shown.bs.modal', function() {
    	$('input[name=PARM_KEY]').focus();
    });	  
}

//修改
function btn_edit_click() {
	var row = $('#itemTable').DataTable().rows('.selected').data();
	if (row.length == 0) {
		window.parent.toastr[MES_WARN]("请选中需要修改的记录！");
		return false;
	}
	$("#itemForm input[name=WORKFLOW_ID]").val(row[0].WORKFLOW_ID);
	$("#itemForm select[name=APP_ID]").val(row[0].APP_ID);
	$("#itemForm input[name=NAME]").val(row[0].NAME);
	$("#itemForm textarea[name=REMARKS]").val(row[0].REMARKS);

	$("h4").text("修改");
	$('#itemModal').modal('show');
    $('#itemModal').on('shown.bs.modal', function() {
        $('input[name=PARM_KEY]').focus();
    });
}
// 保存按钮单击事件
function btn_save_click() {
	function sava() {
		$.ajax({
			url:ctx+ url,
			type : "POST",
			data : form.serialize(),
			success : function(result) {
				if (result == "success") {
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
	var url = "workflow/add";
	var form = $("#itemForm");
	if (form.valid()) {
		if($("#itemForm input[name=WORKFLOW_ID]").val()!= ""){
			url = "workflow/edit";
			sava();
		}else{
			$.ajax({
				url:ctx+"/workflow/isone",
				type : "POST",
				data : "key="+$("#itemForm input[name=WORKFLOW_ID]").val(),
				success : function(result) {
					if (result == "success") {
						sava();
					}else{
						$("#itemForm input[name=PARM_KEY]").val(null);
						window.parent.toastr[MES_WARN]("工作流ID已存在！");
					}
				}
			});
		}
	}
}
//设置状态
function btn_set_click() {
	var ENABLED;
	var row = $('#itemTable').DataTable().rows('.selected').data();
	if (row.length == 0) {
		window.parent.toastr[MES_WARN]("请选中需要设置状态的记录！");
		return false;
	}
	if (row[0].ENABLED=="0"){
		ENABLED=1;
		msg = "确定要注销工作流' " + row[0].NAME + " '吗？";
	}else{
		ENABLED=0;
		msg = "确定要恢复工作流' " + row[0].NAME + " '吗？";
	}

	bootbox.confirm(msg, function(result) {
		if (result) {
			$.ajax({
				url:ctx+"workflow/edit",
				cache : false,
				type : "POST",
				data : {
					WORKFLOW_ID : row[0].WORKFLOW_ID,
					ENABLED:ENABLED
				},
				success : function(result) {
					if (result == "success") {
						window.parent.toastr[MES_SUCCESS]("状态设置成功");
						$("#itemTable").dataTable().fnDraw(false);
					} else {
						window.parent.toastr[MES_ERROR]("状态设置失败");
					}
				},
				error : function(error) {
					window.parent.toastr[MES_ERROR]("状态设置失败");
				}
			});
		}
	});

}
//验证
$("#itemForm").validate({
	rules : {
		NAME : {
			required : true,
			maxlength : 100
		},
		REMARKS:{
			maxlength : 100
		}
	},
	messages : {
		NAME : {
			required : "请输入工作流名称",
			maxlength : "内容过长"
		},
		REMARKS:{
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