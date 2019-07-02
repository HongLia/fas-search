$(function () {
    $(".nav-tabs").tabdrop();
    $.each($.parseJSON(filetype),function (index,item) {
        if(index==0){
            fileclick(this.ITEM_CODE,this.ITEM_NAME);
        }
    });
});
function fileclick(itemcode,name) {
    var item = {};
    item.frameHeight = $(document).height() - 70;
    item.id = itemcode;
    item.url = ctx+"download/getfile?itemcode="+itemcode;
    item.title = name;
    item.close =true;
    _gas_analysis.addTabs(item);//添加tab（默认添加的一行放不下会放两行）
    $(window).resize();//手动触发窗体resize时间  触发tabdrop
}