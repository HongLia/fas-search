$(document).ready(function() {
	$("#deptquerytree").height($("body").height()-120);
	// 初始化日期选择控件
	if (jQuery().datepicker) {
		$('.date-picker').datepicker({
			rtl: App.isRTL(),
			orientation: "left",
			autoclose: true
		});
	}
	$('.multi-select').multiSelect({
		selectableHeader: "<input type='text' class='search-input' autocomplete='off' placeholder='未选角色'>",
		selectionHeader: "<input type='text' class='search-input' autocomplete='off' placeholder='已选角色'>",
		afterInit: function(ms){
			var that = this,
				$selectableSearch = that.$selectableUl.prev(),
				$selectionSearch = that.$selectionUl.prev(),
				selectableSearchString = '#'+that.$container.attr('id')+' .ms-elem-selectable:not(.ms-selected)',
				selectionSearchString = '#'+that.$container.attr('id')+' .ms-elem-selection.ms-selected';
			that.qs1 = $selectableSearch.quicksearch(selectableSearchString)
				.on('keydown', function(e){
					if (e.which === 40){
						that.$selectableUl.focus();
						return false;
					}
				});

			that.qs2 = $selectionSearch.quicksearch(selectionSearchString)
				.on('keydown', function(e){
					if (e.which == 40){
						that.$selectionUl.focus();
						return false;
					}
				});
		},
		afterSelect: function(){
			this.qs1.cache();
			this.qs2.cache();
		},
		afterDeselect: function(){
			this.qs1.cache();
			this.qs2.cache();
		}
	});
	$(".ms-container").width("100%");
	initTable();
	innttree();
});
function btn_import_click() {
	$("h4").text("导入用户");
	$('#fileModal').modal('show');
}
function btn_dwload_mb() {
	window.location.href=ctx+"/download/dwloadmb";
}
function btn_savefile_click() {
	if($("#fileUpload").val()){
		var formData=new FormData($("#fileForm")[0]);
		$.ajax({
			url:ctx+ "/user/importfile",
			type : "POST",
			data : formData,
			cache: false,
			contentType: false,
			processData: false,
			dataType : "json",
			success: function (data,status){
				$(".delfile").click();
				if(data.length>0){
					initerrorTable(data);
					$('#fileModal').modal('hide');
					$("h4").text("导入失败数据(失败原文可能为:用户名已存在)");
					$('#errorModal').modal('show');
				}
			},
			error: function (data){
				$(".delfile").click();
				if(data.responseText!="error"){
					$('#fileModal').modal('hide');
					$("#itemTable").dataTable().fnDraw(false);
				}
				if(data.responseText=="success"){
					window.parent.toastr[MES_SUCCESS]("导入成功！！");
				}else if(data.responseText=="error") {
					window.parent.toastr[MES_WARN]("模板有误请重新选择！！");
				}
			}
		});
	}else{
		window.parent.toastr[MES_WARN]("请选择文件！！");
		return true;
	}
}
function initerrorTable(data) {
	var table = $('#errorTable');
	var oTable = table.dataTable({
		processing : true,
		serverSide : false,
		bSort : false,
		searching : true,
		pagingType : "full_numbers",
		lengthChange : true,
		deferRender: true,
		bDestroy:true,
		lengthMenu : [ 10, 25, 50 ],
		data:data,
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
				data : "deptid",
			}, {
				data : "deptname",
			}, {
				data : "operatorname"
			}, {
				data : "userid",
			}, {
				data : "identity",
			}, {
				data : "telno",
			}
		],
		language : {
			url:ctx+"/resources/lang-zh_CN.json"
		}
	});
}
var olduserid="";
var serchvalue;
var deptid;
//表格初始化
function initTable(deptid) {
	deptid=deptid;
	var oTable = $('#itemTable').DataTable({
		dom:"<'row'<'search text-left'f>r>t<'row pageRow'<'col-sm-3 col-md-3 col-lg-3 data_len'l><'col-sm-3 col-md-3 col-lg-3'i><'col-sm-6 col-md-6 col-lg-6'p>>",
		scrollY:true,
		processing : true,
		serverSide : true,
		bSort : false,
		searching : true,
		pagingType : "full_numbers",
		lengthChange : true,
		deferRender: true,
		bDestroy:true,
		lengthMenu : [ 10, 25, 50 ],
		ajax : {
			url:ctx+"/user/list",
			type : "POST",
			data:{
				deptid:deptid,
				searchValue:$("#itemTable_filter input").val(),
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
				data : "USERID",
				sWidth:'50px',
			}, {
				data : "OPERATORNAME",
				sWidth:'100px',
			}, {
				data : "DEPTNAME",
				sWidth:'500px',
			}, {
				data : "IDENTITY",
				sWidth:'100px',
				// }, {
				// 	data : "EMAIL",
				// }, {
				// 	data : "POSITION",
			}, {
				data : "STATUS",
				sWidth:'50px',
				// }, {
				// 	data : "IPADDRESS",
				// }, {
				// 	data : "MACCODE",
				// }, {
				// 	data : "LASTLOGIN",
				// }, {
				// 	data : "ERRCOUNT",
			}, {
				data : "SUPERADMIN",
				sWidth:'100px',
			}
		],
		aoColumnDefs : [
			{
				targets : 5,
				data : "STATUS",
				render : function(data, type, full) { //返回自定义内容
					if (data == "0") {
						return "<span class='label label-sm label-primary'>正常</span>";
					} else {
						return "<span class='label label-sm label-danger'>已注销</span>";
					}
				}
			},
			{
				targets : 6,
				data : "SUPERADMIN",
				render : function(data, type, full) { //返回自定义内容
					if (data == "0") {
						return "否";
					} else {
						return "是";
					}
				}
			}
		],
		language : {
			url:ctx+"/resources/lang-zh_CN.json"
		},
		initComplete:function () {
			tableHeight();
			$("#itemTable_filter input").attr("title","选中");
			$("#itemTable_filter input").attr("placeholder","输入警号搜索");
			$("#itemTable_filter input").val(serchvalue);
			$("#itemTable_filter input").focus();
			$("#itemTable_filter").append('<i id="btn-search-i" class="fa fa-search" style="cursor:pointer;margin:6px 0px 1px 1px;"></i>');
			$("#itemTable_filter").addClass("input-icon right");
			$("#itemTable_filter input").unbind();
			$("#itemTable_filter input").on("keypress",function(e){
				if(e.keyCode!=13)return;
				serchvalue=$(this).val();
				initTable(deptid);
			});
			$("#itemTable_filter i").on("click",function(e){
				serchvalue=$("#itemTable_filter input").val();
				initTable(deptid);
			});
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
	$("#itemForm input[name=OPERATORID]").val("");
	olduserid=null;
	$("#zmfile").css({"width":"246px","height":"280px"});
	$("#fmfile").css({"width":"246px","height":"280px"});
	$("h4").text("新增");
	document.getElementById("IDOPERATORNAME").readOnly=false;
	var sex =document.getElementById("IDGENDER");
	sex.onclick=function () {

		this.onchange  = function () {
			this.selectedIndex=this.selectedIndex;
		}
	}
	$('#itemModal').modal('show');
}
//刷新缓存
function loginbyredis() {
	var row = $('#itemTable').DataTable().rows('.selected').data();
	if (row.length == 0) {
		window.parent.toastr[MES_WARN]("请选中需要修改的记录！");
		return false;
	}
	utils.showLoadding();
	$.ajax({
		url:ctx+"/loginbyredis",
		type : "POST",
		data : "userid="+row[0].USERID,
		success : function(result) {
			utils.hideLoadding();
			if(result=="success"){
				window.parent.toastr[MES_SUCCESS]("刷新成功！！！");
			}else{
				window.parent.toastr[MES_ERROR]("刷新失败！！！");
			}
		}
	});
}
function loginbyredis_dept() {
	var treeObj = $.fn.zTree.getZTreeObj("deptquerytree");
	var node = treeObj.getSelectedNodes()[0];
	if(node){
		utils.showLoadding("正在刷新,请稍后！！！");
		$.ajax({
			url:ctx+"/loginbyredis_dept",
			type : "POST",
			data : "deptid="+node.id,
			success : function(result) {
				utils.hideLoadding();
				if(result=="nouser"){
					window.parent.toastr[MES_SUCCESS]("所属部门无用户");
				}else if(result=="error"){
					window.parent.toastr[MES_ERROR]("刷新失败！！！");
				}else{
					window.parent.toastr[MES_SUCCESS]("刷新成功！刷新用户："+result);
				}
			}
		});
	}else{
		window.parent.toastr[MES_WARN]("请选择部门！");
	}
}

//修改
function btn_edit_click() {
	var row = $('#itemTable').DataTable().rows('.selected').data();
	if (row.length == 0) {
		window.parent.toastr[MES_WARN]("请选中需要修改的记录！");
		return false;
	}
	olduserid =row[0].USERID;
	$("#itemForm input[name=OPERATORID]").val(row[0].OPERATORID);
	$("#itemForm input[name=USERID]").val(row[0].USERID);
	$("#itemForm input[name=OPERATORNAME]").val(row[0].OPERATORNAME);
	$("#itemForm input[name=DEPTNAME]").val(row[0].DEPTNAME);
	$("#itemForm input[name=DEPTID]").val(row[0].DEPTID);
	$("#itemForm input[name=IDENTITY]").val(row[0].IDENTITY);
	$("#itemForm input[name=EMAIL]").val(row[0].EMAIL);
	$("#itemForm select[name=POSITION]").val(row[0].POSITION);
	$("#itemForm select[name=SUPERADMIN]").val(row[0].SUPERADMIN);
	$("#itemForm input[name=PASSWDINVALDATE]").val(formatDateTime(row[0].PASSWDINVALDATE).substr(0,10));
	$.ajax({
		url:ctx+"/user/getuserinfo",
		cache : false,
		type : "POST",
		data : {
			OPERATORID : row[0].OPERATORID
		},
		success : function(result) {
			$("#itemForm input[name=BIRTHDATE]").val(result.birthdate);
			$("#itemForm select[name=GENDER]").val(result.gender);
			$("#itemForm input[name=TELNO]").val(result.telno);
			$("#itemForm input[name=PHONENO]").val(result.phoneno);
			$("#itemForm select[name=JZ]").val(result.jz);
			$("#itemForm input[name=AGE]").val(result.age);
			$("#itemForm input[name=WHCD]").val(result.whcd);
			$("#itemForm input[name=GRRY]").val(result.grry);
			$("#itemForm input[name=JTRY]").val(result.jtry);
			$("#itemForm select[name=ZJK]").val(result.zjk);
			// $("#itemForm input[name=JZMJBH]").val(result.jzmjbh);
			// $("#itemForm input[name=JZMJZW]").val(result.jzmjzw);
		}
	});


	$("#zmfile").html("<img src='"+ctx+"/user/getimg?type=0&&userid="+row[0].USERID+"' onerror='imgerror()'>");
	$("#fmfile").html("<img src='"+ctx+"/user/getimg?type=1&&userid="+row[0].USERID+"' onerror='imgerror()'>");
	$("#zmfile").css({"width":"246px","height":"280px"});
	$("#fmfile").css({"width":"246px","height":"280px"});
	$("h4").text("修改");
	document.getElementById("IDOPERATORNAME").readOnly=true;
	var sex = document.getElementById("IDGENDER");
	sex.onclick = function () {
		var index = this.selectedIndex;
		this.onchange = function () {
			this.selectedIndex = index;
		}
	}
	$('#itemModal').modal('show');
}
function imgerror() {
	$("#zmfile").css({"width":"246px","height":"280px"});
	$("#fmfile").css({"width":"246px","height":"280px"});
	$("#zmfile").html("");
	$("#fmfile").html("");
}
function btn_userinfo_click() {
	var row = $('#itemTable').DataTable().rows('.selected').data();
	if (row.length == 0) {
		window.parent.toastr[MES_WARN]("请选中需要设置用户信息的记录！");
		return false;
	}
	$("#userForm input[name=OPERATORID]").val(row[0].OPERATORID);
	$.ajax({
		url:ctx+"/user/getuserinfo",
		cache : false,
		type : "POST",
		data : {
			OPERATORID : row[0].OPERATORID
		},
		success : function(result) {
			$("#userForm input[name=BIRTHDATE]").val(result.birthdate);
			$("#userForm select[name=GENDER]").val(result.gender);
			$("#userForm input[name=TELNO]").val(result.telno);
			$("#userForm input[name=PHONENO]").val(result.phoneno);
			$("#userForm input[name=JZMJBH]").val(result.jzmjbh);
			$("#userForm input[name=JZMJZW]").val(result.jzmjzw);
		}
	});
	$("h4").text("用户基本信息");
	$('#userModal').modal('show');
}
function btn_saveuserinfo_click() {
	var form = $("#userForm");
	if (form.valid()) {
		$.ajax({
			url:ctx+"/user/saveuserinfo",
			type : "POST",
			data : form.serialize(),
			cache: false,
			success : function(result) {
				if (result == "success") {
					$("#userModal").modal("hide");
					window.parent.toastr[MES_SUCCESS]("保存成功");
					$("#itemTable").dataTable().fnDraw(false);
					btn_close_click();
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
//设置状态
function btn_set_click() {
	var row = $('#itemTable').DataTable().rows('.selected').data();
	if (row.length == 0) {
		window.parent.toastr[MES_WARN]("请选中需要设置状态的记录！");
		return false;
	}
	if (row[0].STATUS=="0"){
		msg = "确定要注销用户‘" + row[0].OPERATORNAME + "’吗？";
	}else{
		msg = "确定要恢复用户‘" + row[0].OPERATORNAME + "’吗？";
	}

	bootbox.confirm(msg, function(result) {
		if (result) {
			$.ajax({
				url:ctx+"/user/set",
				cache : false,
				type : "POST",
				data : {
					operatorid : row[0].OPERATORID,
					status:row[0].STATUS
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
var deptusers="";
//设置部门角色
function btn_setdeptrole_click() {
	var treeObj = $.fn.zTree.getZTreeObj("deptquerytree");
	var node = treeObj.getSelectedNodes()[0];
	if(node){
		ajaxmultiselect("admin");
		$("h4").text("设置部门用户角色");
		$('#userroleModal').modal('show');
		deptusers=node.id;
	}else{
		window.parent.toastr[MES_WARN]("请选择部门！");
	}
}
//设置角色
function btn_setrole_click() {
	//批量授权编号组设置null 表明是单独设置角色
	deptusers="";
	var row = $('#itemTable').DataTable().rows('.selected').data();
	if (row.length == 0) {
		window.parent.toastr[MES_WARN]("请选中需要设置角色的用户！");
		return false;
	}
	$("#userroleForm input[name=operatorid]").val(row[0].OPERATORID);
	ajaxmultiselect(row[0].OPERATORID);
	$("h4").text("设置用户角色");
	$('#userroleModal').modal('show');
}
function ajaxmultiselect(operatorid){
	$.ajax({
		url:ctx+"/user/getuserrole",
		type : "POST",
		data : "operatorid="+operatorid,
		success : function(result) {
			var html = "";
			$.each(result,function () {
				html+="<option value='"+this.ROLEID+"' ";
				if (this.ROLEID==this.setROLEID) {
					html+="selected='selected'";
				}
				html+=">"+this.NAME+"</option>";
			});
			$("#userroleForm select[name=Role]").html(html);
			$('.multi-select').multiSelect("refresh");
			$(".ms-container").width("100%");
		}
	});
}
function btn_save_click() {
	var valid=true;
	var form = $("#itemForm");
	if (form.valid()) {
		var url = "user/add";
		var formData = new FormData($("#itemForm")[0]);
		if ($("#itemForm input[name=OPERATORID]").val() != "") {
			url = "user/edit";
			formData.append("olduserid", olduserid);
		}else{
			$.ajax({
				url: ctx + "/user/useridisone",
				type: "POST",
				data: {
					IDENTITY: $("#itemForm input[name=IDENTITY]").val(),
					USERID: $("#itemForm input[name=USERID]").val()
				},
				async: false,
				cache: false,
				success: function (result) {
					if (result != "success") {
						valid=false;
						if (result == "jhycz") {
							window.parent.toastr[MES_WARN]("警号已存在！");
							$("#itemForm input[name=USERID]").val("");
						} else {
							window.parent.toastr[MES_WARN]("身份证已存在！");
							$("#itemForm input[name=IDENTITY]").val("");
						}
					}
				}
			});
		}
		if(valid){
			formData.append("JZMC",$("#itemForm select[name=JZ]").find("option:selected").text());
			formData.append("ZJKMC",$("#itemForm select[name=ZJK]").find("option:selected").text());
			$.ajax({
				url: ctx + "/" + url,
				type: "POST",
				data: formData,
				async: false,
				cache: false,
				contentType: false,
				processData: false,
				success: function (result) {
					switch (result) {
						case "success":
							$("#itemModal").modal("hide");
							window.parent.toastr[MES_SUCCESS]("保存成功");
							$("#itemTable").dataTable().fnDraw(false);
							userclose();
							break;
						case "zmmax":
							window.parent.toastr[MES_WARN]("正面警官证过大(请选择小于256k图片)");
							break;
						case "fmmax":
							window.parent.toastr[MES_WARN]("背面警官证过大(请选择小于256k图片)");
							break;
					}
				},
				error: function () {
					window.parent.toastr[MES_ERROR]("数据保存失败！");
				}
			});
		}
	}
}
function  btn_saveuserrole_click() {
	if(deptusers==""){
		saveuserrole($("#userroleForm input[name=operatorid]").val());
	}else{
		saveuserrole(null,deptusers)
	}
}
function saveuserrole(operatorid,deptusers) {
	$.ajax({
		url:ctx+"/user/saveuserrole",
		cache : false,
		type : "POST",
		data :{
			Roles:$("#userroleForm select[name=Role]").val()==null?null:$("#userroleForm select[name=Role]").val().join(),
			operatorid:operatorid,
			deptusers:deptusers
		},
		success : function(result) {
			if (result == "success") {
				$("#userroleModal").modal("hide");
				window.parent.toastr[MES_SUCCESS]("保存成功");
				$("#itemTable").trigger("reloadGrid");
				btn_close_click();
			} else {
				window.parent.toastr[MES_ERROR]("状态设置失败");
			}
		},
		error : function(error) {
			window.parent.toastr[MES_ERROR]("状态设置失败");
		}
	});
}
function userclose(){
	btn_close_click();
	$("#zmfile").html("");
	$("#fmfile").html("");
	//多个图的时候 重置form 无效（选择两个图片 关闭窗口重置之后 但是再次打开窗口 总会有一个fileinput的图片还在）
	$("a[data-dismiss=fileinput]").click();
}
//tree
function innttree(){
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
				$("#itemTable_filter input").val(null);
				serchvalue=null;
				if(treeId=="depttree"){
					$("#itemForm input[name=DEPTNAME]").val(treeNode.name);
					$("#itemForm input[name=DEPTID]").val(treeNode.id);
					hideMenu();
				}else{
					initTable(treeNode.id);
				}
			}
		}
	};
	//修改部门树加载全部部门
	$.ajax({
		url:ctx+"/dept/tree",
		type:"POST",
		dataType:"json",
		success:function(data){
			$.fn.zTree.init($("#depttree"), setting, data);
		}
	});
	//查询用户树 登录用户所在部门树
	$.ajax({
		url:ctx+"/dept/tree",
		type:"POST",
		dataType:"json",
		success:function(data){
			$.fn.zTree.init($("#deptquerytree"), setting, data);
			//强行展开一级
			ChildNodes($.fn.zTree.getZTreeObj("deptquerytree"));
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
		USERID : {
			required : true,
			number:true,
			minlength:8,
			maxlength : 8
		},
		OPERATORNAME:{
			required : true,
			maxlength : 60
		},
		DEPTNAME:{
			required : true
		},
		IDENTITY:{
			required : true,
			IDENTITY:true,
		},
		EMAIL:{
			EMAIL: true
		},
		TELNO : {
			PHONE:true,
			maxlength : 20
		},
		PHONENO: {
			PHONE:true,
			maxlength : 20
		},
		WHCD:{
			maxlength : 200
		},
		GRRY:{
			maxlength : 200
		},
		JTRY:{
			maxlength : 200
		},
		JZ:{
			required : true
		}
	},
	messages : {
		USERID : {
			required : "请输入警号",
			number:"请输入数字",
			maxlength:"请输入八位警号",
			minlength:"请输入八位警号"
		},
		OPERATORNAME : {
			required : "请输入姓名",
			maxlength : "内容过长"
		},
		DEPTNAME:{
			required : "请选择部门",
		},
		IDENTITY:{
			required : "请输入身份证"
		},
		TELNO : {
			maxlength : "内容过长"
		},
		PHONENO : {
			maxlength : "内容过长"
		},
		WHCD : {
			maxlength : "内容过长"
		},
		GRRY : {
			maxlength : "内容过长"
		},
		JTRY : {
			maxlength : "内容过长"
		},
		JZ:{
			required : "请选择"
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
function bidentity(obj) {
	var val =$(obj).val();
	if(/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/.test(val)){
		var tmp = val.substr(6,8);
		$("#itemForm input[name=BIRTHDATE]").val(tmp.substr(0,4)+'-'+tmp.substr(4,2)+'-'+tmp.substr(6));
		var time = new Date();
		var y = time.getFullYear();
		$("#itemForm input[name=AGE]").val(y-tmp.substr(0,4));
	}
}