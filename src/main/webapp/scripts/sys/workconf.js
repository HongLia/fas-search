$(document).ready(function() {
	$("#treediv").css("height",$(window).height()-110);
	//初始部门树
	innttree();
});
var setting = {
    edit: {
        enable: true,
        showRemoveBtn: false,
        showRenameBtn: false
    },
    view: {
        dblClickExpand: true,
        showLine: true,
        selectedMulti: true,
        fontCss: setFontCss
    },
    data: {
        simpleData: {
            enable:true,
        }
    },
    callback: {
        beforeDrop: zTreeBeforeDrop,
        beforeDrag: beforeDrag,
    }
};
function zTreeBeforeDrop(treeId, treeNodes, targetNode, moveType) {
    if(targetNode == null || (moveType != "inner" && !targetNode.parentTId)){
        window.parent.toastr[MES_WARN]("禁止配置为根节点！！！");
        return false;
    }else{
        return true;
    }
};
function beforeDrag(treeId, treeNodes) {
    if (treeNodes[0].drag === false) {
        window.parent.toastr[MES_WARN]("禁止拖拽！！！");
        return false;
    }else{
        return true;
    }
}
function setFontCss(treeId, treeNode) {
    if(treeNode.name.indexOf("_未配置部门") > 0){
        return {color:"red","font-weight":"bold"}
    }
    if(treeNode.name.indexOf("_已配置部门") > 0){
        return {color:"#5cb85c","font-weight":"bold"}
    }
};
//tree
function innttree_n(){
    $.ajax({
        url:ctx+"/workconf/tree_n",
        type:"POST",
        data:{
            appid:$("#APP").val(),
        },
        dataType:"json",
        success:function(data){
            data.unshift({id:0,name:$("#APP").find("option:selected").text()+"_未配置部门",open:true,drag:false});
            $.fn.zTree.init($("#typeIdIdtree_n"), setting, data);
        }
    });
}
//tree
function innttree(){
	$.ajax({
		url:ctx+"/workconf/tree",
		type:"POST",
		data:{
			appid:$("#APP").val(),
		},
		dataType:"json",
		success:function(data){
            data.unshift({id:0,name:$("#APP").find("option:selected").text()+"_已配置部门",open:true,drag:false});
			$.fn.zTree.init($("#typeIdIdtree"), setting, data);
            //初始未配置部门
            innttree_n();
		}
	});
}
function saveTree() {
    var treeobj = $.fn.zTree.getZTreeObj("typeIdIdtree");
    if(treeobj.transformToArray(treeobj.getNodes()).length>1){
        $.ajax({
            url:ctx+"/workconf/saveworkconf",
            cache : false,
            type : "POST",
            data :{
                workconf:JSON.stringify(treeobj.transformToArray(treeobj.getNodes())),
                appid:$("#APP").val()
            },
            success : function(result) {
                window.parent.toastr[MES_SUCCESS]("保存成功");
                innttree();
            },
            error : function(error) {
                window.parent.toastr[MES_ERROR]("保存失败");
            }
        });
	}else{
        window.parent.toastr[MES_WARN]("请配置审核部门树！！！");
	}
}
function saveConfbydept() {
    $.ajax({
        url:ctx+"/workconf/saveConfbydept",
        cache : false,
        type : "POST",
        data :{
            appid:$("#APP").val()
        },
        success : function(result) {
            window.parent.toastr[MES_SUCCESS]("保存成功");
            innttree();
        },
        error : function(error) {
            window.parent.toastr[MES_ERROR]("保存失败");
        }
    });
}
function saveConfbyapp() {
    $.ajax({
        url:ctx+"/workconf/saveConfbyapp",
        cache : false,
        type : "POST",
        data :{
            app_t:$("#APP").val(),
            app_s:$("#APP_S").val()
        },
        success : function(result) {
            $('#itemModal').modal('hide');
            window.parent.toastr[MES_SUCCESS]("保存成功");
            innttree();
        },
        error : function(error) {
            window.parent.toastr[MES_ERROR]("保存失败");
        }
    });
}
function selectapp() {
    $('#itemModal').modal('show');
}