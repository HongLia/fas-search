$(document).ready(function() {
    $(".bs-select").selectpicker({
        iconBase: 'fa',
        tickIcon: 'fa-check'
    });
});
/*初始化表格*/
function initTable(obj) {
    var table = $('#detailsTable');
    var oTable = table.DataTable({
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
            url:ctx+"/transfer/filetransfer_history",
            type : "POST",
            data: {
                type:obj
            },
            error: AjaxError
        },
        select : {
            style : 'single'
        },
        columns : [
            {
                data : "SENDUSERNAME",
                width:"10%"
            }, {
                data : "NAME",
                width:"70%"
            }, {
                data : "SENDTIME",
                width:"20%"
            }
        ],
        createdRow:function (row,data,dataIndex) {
            utils.tdShowMaxLength(row,50);// td文字超出10字以...显示
        },
        aoColumnDefs : [
            {
                targets : 1,
                render : function(data, type, full) { //返回自定义内容
                    return "<a class='showemail'><i class='fa fa-file-text-o'></i> "+data+" </a>";
                }
            },
        ],
        language : {
            url:ctx+"/resources/lang-zh_CN.json"
        },
        dom: "<'row'<'col-sm-12'tr>>\n\t\t\t<'row'<'col-sm-12 col-md-5'i><'col-sm-12 col-md-7 dataTables_pager'lp>>",
    });
    /*查看详情*/
    table.on('click', '.showemail', function (e) {
        $("h4").text("文件详情");
        e.preventDefault();
        var data = table.DataTable().row($(this).parents("tr")).data();
        var arr=data.RECEIVEUSER.split(',');
        var str="";
        $.each(arr,function(index,j){
            str+=j;
            str+='&nbsp;&nbsp';
        })
        alert(str);
        $("#showemail div[name=NAME]").html(data.NAME);
        $("#showemail div[name=SENDUSER]").html(data.SENDUSERNAME);
        $("#showemail div[name=RECEIVEUSER]").html(str);
        $("#showemail div[name=SENDTIME]").html(data.SENDTIME);
        $("#showemail div[name=CONTENT]").html(data.CONTENT);
        $("#showemail input[name=FILENAME_LOOK]").val(data.FILENAME);
        $.ajax({
            url:ctx+"/transfer/getfiles",
            type : "POST",
            cache: false,
            data:{
                id:data.ID
            },
            success : function(result) {
                var html='';
                var dw='';
                $.each(result,function (index,item) {
                    html+='<i class="fa fa fa-download" style="cursor:pointer" onclick="dwload('+this.id+')">&nbsp;&nbsp;';
                    html+=this.filename;
                    html+='</i>&nbsp;&nbsp;';
                });
                $("#showemail div[name=FILE]").html(html);
                $('#showemail').modal('show');
            },
            error : function() {
                window.parent.toastr[MES_ALERT]("系统错误！");
            }
        });
    })
}
function dwload(fileid) {
    window.location.href=ctx+"/transfer/download?id="+fileid;
}
//异常错误提示
function AjaxError( xhr, textStatus, error ) {
    if ( textStatus === 'timeout' ) {
        window.parent.toastr[MES_ERROR]("服务器没有响应！");
    }
    else {
        window.parent.toastr[MES_ERROR]("服务器出错,请重试！");
    }
    $("#detailsTable").dataTable().fnProcessingIndicator(false );
}
/*文件提交数据处理*/
function filesInput(value) {

    if(value != null && value != ""){

        var ajaxUrl = ctx + "/transfer/filesValue";
        var form = new FormData(document.getElementById("fileForm"));
        $.ajax({
            url:ajaxUrl,
            type:"post",
            data:form,
            cache:false,
            processData:false,
            contentType:false,
            success:function (data) {
            }
        });

        /*filesValue*/
        var lastIndexOf = value.lastIndexOf("\\");
        var substring = value.substring(lastIndexOf + 1);
        var buttons = '<button href="javascript:;" class="btn m-btn m-btn--label-info m-btn--pill" style="border:1px dashed;background: none;margin-right:10px;margin-bottom:5px;">' +
            '<i class="fa fa-file-word-o" style="margin-right: 5px;"></i>' + substring +
            '<i class="fa fa-close" onclick="fileEmpty();" style="margin-left: 5px;"></i>' +
            '</button>';
        $("#buttons").append(buttons);
    }
}

/*清空文件*/
function fileEmpty() {
    $("#buttons").empty();
    var ajaxUrl = ctx + "/transfer/emptyValues";
    $.post(ajaxUrl,{},function (data) {
        if(data > 0){
            alert("清空成功!");
        }else {
            alert("清空失败!");
        }
    });
}

/*文件传输提交*/
function emailSubmit() {

    var ajaxUrl = ctx + "/transfer/addFiles";
    var form = new FormData(document.getElementById("fileForm"));
    $.ajax({
        url:ajaxUrl,
        type:"post",
        data:form,
        cache:false,
        processData:false,
        contentType:false,
        success:function (data) {
            if(data == "success"){
                window.parent.toastr[MES_SUCCESS]("发送成功！");
                location.reload();
            }else{
                window.parent.toastr[MES_ERROR]("发送失败!");
            }
        }
    });
}
//tree
function innttree(){
    var setting = {
        async:{
            enable:true,
            type:"post",
            url:ctx+"/transfer/tree",
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
        }
    };
    //修改部门树加载全部部门
    $.ajax({
        url:ctx+"/transfer/tree",
        type:"POST",
        dataType:"json",
        success:function(data){
            $.fn.zTree.init($("#depttree"), setting, data);
            $('#usershowemail').modal('show');
        }
    });
    //查询用户树 登录用户所在部门树
    $.ajax({
        url:ctx+"/transfer/tree",
        type:"POST",
        dataType:"json",
        success:function(data){
            $.fn.zTree.init($("#deptquerytree"), setting, data);
            //强行展开一级
            ChildNodes($.fn.zTree.getZTreeObj("deptquerytree"));
        }
    });
}
function userInitTable(obj) {
    var table = $('#itemTable');
    var oTable = table.DataTable({
        scrollY: true,
        processing: true,
        serverSide: true,
        bSort: false,
        searching: false,
        pagingType: "full_numbers",
        lengthChange: true,
        deferRender: true,
        bDestroy: true,
        lengthMenu: [10, 25, 50],
        ajax: {
            url: ctx + "/transfer/dataListTree",
            type: "POST",
            data: {
                type: obj
            },
            error: AjaxError
        },
        select: {
            style: 'single'
        },
        columns: [
            {
                data: "OPERATORNAME",
                width: "10%"
            }
        ],
        createdRow: function (row, data, dataIndex) {
            utils.tdShowMaxLength(row, 50);// td文字超出10字以...显示
        },
        aoColumnDefs: [
            {
                targets: 1,
                render: function (data, type, full) { //返回自定义内容
                    return "<a class='showemail'><i class='fa fa-file-text-o'></i> " + data + " </a>";
                }
            },
        ],
        language: {
            url: ctx + "/resources/lang-zh_CN.json"
        },
        dom: "<'row'<'col-sm-12'tr>>\n\t\t\t<'row'<'col-sm-12 col-md-5'i><'col-sm-12 col-md-7 dataTables_pager'lp>>",
    });
}
