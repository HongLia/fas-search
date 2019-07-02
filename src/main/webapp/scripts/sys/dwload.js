$(function () {
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
            url:ctx+"/dwload/list",
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
                data : "typeCode",
            }, {
                data : "filepath",
            }, {
                data : "fileName"
            },{
                data : "status",
            }, {
                data : "creator"
            }, {
                data : "createtime",
            }
        ],
        aoColumnDefs : [
            {
                targets: 1,
                render: function (data, type, full) {
                    return data.substr(0,data.indexOf('('));
                }
            }, {
                targets: 4,
                render: function (data, type, full) {
                    if (data == "0") {
                        return "<span class='label label-sm label-primary'>正常</span>";
                    } else {
                        return "<span class='label label-sm label-danger'>已注销</span>";
                    }
                }
            },{
                targets: 6,
                render: function (data, type, full) {
                    return formatDateTime(data);
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
});
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
function initjdt() {
    $("#xzfile").show();$("#filejd").hide();
    $("#filejd div").addClass("progress-bar-info");
    $("#filejd div").removeClass("progress-bar-suncess");
    $("#filejd div").removeClass("progress-bar-danger");
    $("#filejd span").html("正在上传...");
    $("#sc").show();
}
function btn_add_click() {
    $("#itemForm input[name=id]").val("");
    initjdt();
    $("#delfile").click();
    $("h4").text("上传");
    $('#itemModal').modal('show');
    $("#itemForm select[name=typeCode]").val(null);
    $('#itemModal').on('shown.bs.modal');
}
function btn_edit_click() {
    initjdt();
    $("#delfile").click();
    var row = $('#itemTable').DataTable().rows('.selected').data();
    if (row.length == 0) {
        window.parent.toastr[MES_WARN]("请选中需要修改的记录！");
        return false;
    }
    var val =row[0].typeCode.substr(row[0].typeCode.indexOf('(')+1,row[0].typeCode.indexOf(')')-row[0].typeCode.indexOf('(')-1);
    $("#itemForm select[name=typeCode]").val(val);
    $("#itemForm input[name=id]").val(row[0].id);

    $("h4").text("修改");
    $('#itemModal').modal('show');
    $('#itemModal').on('shown.bs.modal');
}
function btn_set_click() {
    var row = $('#itemTable').DataTable().rows('.selected').data();
    if (row.length == 0) {
        window.parent.toastr[MES_WARN]("请选中需要修改的记录！");
        return false;
    }
    $.ajax({
        url:ctx+"/dwload/set",
        type : "POST",
        data : {id:row[0].id,status:row[0].status},
        success : function(result) {
            if (result == "success") {
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
function btn_save_click() {
    var url = "/dwload/add";
    if ($("#itemForm input[name=id]").val() != "") {
        url = "/dwload/edit";
    }
    var form = $("#itemForm");
    if (form.valid()&&$("input[name=file]").val()!="") {
        $("#xzfile").hide();$("#filejd").show();$("#sc").hide();
        var formData=new FormData($("#itemForm")[0]);
        $.ajax({
            url:ctx+ url,
            type : "POST",
            data : formData,
            cache: false,
            contentType: false,
            processData: false,
            success : function(result) {
                if(result=='success'){
                    $("#filejd div").removeClass("progress-bar-info");
                    $("#filejd div").addClass("progress-bar-suncess");
                    $("#filejd span").html("上传成功！！");
                    setTimeout(function () {
                        $("#itemModal").modal("hide");
                        $("#itemTable").dataTable().fnDraw(false);
                    },1000)
                }else {
                    $("#filejd div").removeClass("progress-bar-info");
                    $("#filejd div").addClass("progress-bar-danger");
                    $("#filejd span").html("上传失败！！");
                }
            },
            error : function() {
                $("#filejd div").removeClass("progress-bar-info");
                $("#filejd div").addClass("progress-bar-danger");
                $("#filejd span").html("请选择小于31M文件！！");
            }
        });
    }
}

//验证
$("#itemForm").validate({
    rules : {
        typeCode : {
            required : true
        },
        file : {
            required : true
        }
    },
    messages : {
        typeCode : {
            required : "请输入文件类型",
        },
        file : {
            required : "请选择文件"
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