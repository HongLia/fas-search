$(function () {
    var table = $('#itemTable');
    var oTable = table.dataTable({
        processing : true,
        serverSide : true,
        bSort : false,
        searching : false,
        pagingType : "full_numbers",
        dom:"<'row'<'search text-left'f>r>t<'row pageRow'<'col-sm-3 col-md-3 col-lg-3 data_len'l><'col-sm-3 col-md-3 col-lg-3'i><'col-sm-6 col-md-6 col-lg-6'p>>",
        lengthChange : true,
        deferRender: true,
        bDestroy:true,
        lengthMenu : [ 10, 25, 50 ],
        ajax : {
            url:ctx+"/download/getfilelist?itemcode="+itemcode,
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
            },{
                data : "fileName"
            }, {
                data : "creator"
            }, {
                data : "createtime",
            }, {
                data : "id",
                sClass:"text-center",
            }
        ],
        aoColumnDefs : [
            {
                targets: 1,
                render: function (data, type, full) {
                    return data.substr(0,data.indexOf('('));
                }
            },{
                targets: 4,
                render: function (data, type, full) {
                    return formatDateTime(data);
                }
            },{
                targets: 5,
                render: function (data, type, full) {
                    return '<i class="fa fa fa-download" style="cursor:pointer" onclick="downloadfile('+data+')"></i>';
                }
            }
        ],
        language : {
            url:ctx+"/resources/lang-zh_CN.json"
        }
    });
});
function downloadfile(id) {
    window.location.href=ctx+"/download/downloadbyid?id="+id;
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