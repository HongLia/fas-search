var utils = (function(){
    function mainWrapHeight(total,accordion){
        var pageContentHeight = parseInt($('.page-content').css('min-height'));
        var navHeight = $('.nav').outerHeight() + parseInt($('.nav').css('margin-bottom')) + parseInt($('.nav').css('border-bottom-width'));
        $('.main_wrap').height(pageContentHeight-navHeight-accordion);
        var totalHeight = $('.main_wrap').height();
        this.setTbodyH(totalHeight,total);
    }
    function setTbodyH(totalHeight,total){
        // 1) totalHeight 总高度
        // 2）你可能会新增加一行额外的元素要计算，写在此处后面
        var searchEle = $('.srarch-setheight-wrap').height() + parseInt($('.srarch-setheight-wrap').css('margin-bottom'));

        // 3）亘古不变的元素部队
        var tableOuterHeight = parseInt($('.table-scrollable').css('margin-top')) + parseInt($('.table-scrollable').css('margin-bottom'));
        var dataTablesHcrollMH = parseInt($('.dataTables_scroll').css('margin-bottom'));
        var dataTablesScrollHeadBH = parseInt($('.dataTables_scrollHead').css('border-bottom-width'));
        var pageRowHeight = $('.pageRow').height();

        // 4) 计算tbody高度 这里-10 因为当点下拉列表展示50条或出滚动横条后，会增加约10像素距离，所以要-10  //total 是新增自定义的元素高度总和
        var tbodyHeight = totalHeight-(searchEle+tableOuterHeight+dataTablesHcrollMH+dataTablesScrollHeadBH+pageRowHeight)-25-total;

        // 5) 准备了这么多就是为了给它(tbody)赋值高度 -> $('.dataTables_scrollBody')
        $('.dataTables_scrollBody').height(tbodyHeight);
        // 6) 重置pageRow宽度
        $('.pageRow').outerWidth($('.table-scrollable').outerWidth());
    }
    function tdShowMaxLength(row,maxLength){
        var taLength = 0;
        if(typeof maxLength == 'undefined' || maxLength == null || maxLength == ''){
            taLength = maxShowLength;
        }else{
            taLength = maxLength;
        }
        for(var i = 0;i<$(row)[0].cells.length;i++){
            if(i>0){
                var value = $(($(row)[0].cells)[i]).text();
                if(value.length>taLength){
                    $(row).children('td').eq(i).attr('title',value);
                    var tdText = value.substring(0,taLength)+'...';
                    $(row).children('td').eq(i).text(tdText);

                }
            }
        }
    }
    function rowspanf(tbody) {
        var trList=$(tbody).children("tr");
        for (var i = 0; i < trList.length; i++) {
            if (i == 0) {
                var newTdText = "全市";
                trList.eq(i).find('td').eq(0).attr("rowspan", "1").css("width", "70px").css("background-color", "#ACB5C3").text('').append(newTdText);
            } else if (i == 1) {
                var newTdText = "<div class='hbdqgnq'><br><br>都市功能核心区</div>";
                trList.eq(i).find('td').eq(0).attr("rowspan", "11").css("background-color", "#ACB5C3").text('').append(newTdText);
            } else if (i == 12) {
                var newTdText = "<div class='hbdqgnq'><br><br>城市发展新区</div>";
                trList.eq(i).find('td').eq(0).attr("rowspan", "15").css("background-color", "#ACB5C3").text('').append(newTdText);
            } else if (i == 27) {
                var newTdText = "<div class='hbdqgnq'><br><br>渝东北生态涵养发展区</div>";
                trList.eq(i).find('td').eq(0).attr("rowspan", "12").css("background-color", "#ACB5C3").text('').append(newTdText);
            } else if (i == 39) {
                var newTdText = "<div class='hbdqgnq'><br><br>渝东南生态涵养保护区</div>";
                trList.eq(i).find('td').eq(0).attr("rowspan", "7").css("background-color", "#ACB5C3").text('').append(newTdText);
            } else if (i == 46) {
                var newTdText = "<div class='hbdqgnq'><br><br>其他</div>";
                trList.eq(i).find('td').eq(0).attr("rowspan", "2").css("background-color", "#ACB5C3").text('').append(newTdText);
            }else {
                trList.eq(i).find('td').eq(0).remove();
            }

        }
    }

    function setInputSearch(){
        $("#caseTable_filter input").attr("placeholder","编号,名称,回车搜索");
        $("#caseTable_filter input").attr("title","支持案件编号、名称的模糊查询");
        $("#caseTable_filter label").css("margin-left","10px");
        $("#caseTable_filter").append('<i id="btn-search-i" class="fa fa-search" style="cursor:pointer;margin:6px 22px 1px 1px;"></i>');
        $("#caseTable_filter").addClass("input-icon right");
        $("#btn-search-i").on("click",function(e){
            var nr=$("#caseTable_filter input").val();
            if(nr==""){
                return;
            }
            doSearch(nr);
        });
        $("#caseTable_filter input").attr("style","width:280px!important");
        $("#caseTable_filter input").on("keypress",function(e){
            if(e.keyCode!=13)return;
            var nr=$(this).val();
            doSearch(nr);
        });
        $("th").css("text-align","center");
    }
    function AjaxError( xhr, textStatus, error ) {
        if ( textStatus === 'timeout' ) {
            window.parent.toastr[MES_ERROR]("服务器没有响应！");
        }
        else {
            window.parent.toastr[MES_ERROR]("服务器出错,请重试！");
        }
        $("#itemTable").dataTable().fnProcessingIndicator(false );
    }
    function getNewTime(){
        function p(s) {
            return s < 10 ? '0' + s: s;
        }
        var myDate = new Date();
        //获取当前年
        var year=myDate.getFullYear();
        //获取当前月
        var month=myDate.getMonth()+1;
        //获取当前日
        var date=myDate.getDate();
        var h=myDate.getHours();       //获取当前小时数(0-23)
        var m=myDate.getMinutes();     //获取当前分钟数(0-59)
        var s=myDate.getSeconds();

        var now=year+'-'+p(month)+"-"+p(date)+" "+p(h)+':'+p(m)+":"+p(s);

        //如果需要月份加1的话
        var myDate2 = new Date();
        var myNewDate =  new Date();
        myDate2.setMonth(month - 1);//上月
        myNewDate.setMonth(month);

        //获取当前年
        var year2=myDate2.getFullYear();
        var year2myNewDate=myNewDate.getFullYear();
        //获取当前月
        var month2=myDate2.getMonth();
        var month2myNewDate=myNewDate.getMonth();
        //获取当前日
        var date2=myDate2.getDate();
        var date2myNewDate=myNewDate.getDate();

        var now2=year2+'.'+p(month2)+"."+p(date2);
        var now3=year2myNewDate+"."+p(month2myNewDate)+"."+p(date2myNewDate);
        $('.monthDate').val(now2+' - '+now3);
    }
    function getDate(m){
        $("#"+m).datetimepicker({
            isRTL: App.isRTL(),
            language:"zh",
            format: "yyyy-mm-dd",
            minView:"month",
            showMeridian: true,
            autoclose: true,
            pickerPosition: (App.isRTL() ? "bottom-right" : "bottom-left"),
            todayBtn: true
        });
    }
    function getData(sSource,aoData,fnCallback){
        getSortDefine(aoData);//加载排序字段
        for(var i=0;i<params.length;i++){
            aoData.push({"name":params[i].name,"value":params[i].value});
        }
        $.ajax({
            "type":"POST",
            "contentType":"application/x-www-form-urlencoded",
            "url":sSource,
            "dataType":"json",
            "timeout":10000,
            "data":{"params":JSON.stringify(aoData)},
            "success":function(resp){
                if(resp.aaData=="error"){
                    toastr[MES_WARN]("获取案件数据失败,请联系管理员!","系统提示");
                    return;
                }
                fnCallback(resp);
            },
            "complete":function(request,status){
                if(status=='timeout'){
                    toastr[MES_WARN]("获取案件数据超时,请稍后再试!","系统提示");
                }
            }
        });
    }
    function getDateTime(m){
        $("#"+m).datetimepicker({
            isRTL: App.isRTL(),
            language:"zh",
            format: "yyyy-mm-dd hh:ii:ss",
            showMeridian: true,
            autoclose: true,
            pickerPosition: (App.isRTL() ? "bottom-right" : "bottom-left"),
            todayBtn: true
        });
    }

    function showCheckBox(thead,appendPar) {
        var tdList = $(thead).children("tr").children();
        for (var i = 0; i < tdList.length; i++) {
            var tdText = tdList.eq(i).text();
            var toogleAll = $('<div class="checkbox-list"><label for=""><input type="checkbox" class="toogle-all" data-column="0" name="toogle-vis-all" id="toogle-all">全选</label></div>');
            var toogleList = $('<div class="checkbox-list"><label for=""><input type="checkbox" class="toogle-vis" data-column="'+ i +'" name="toogle-vis">'+ tdText +'</label></div>');

            if(i==0){
              //  appendPar.append(toogleAll);  // 没有全选功能，这里不要打开
            }else{
                appendPar.append(toogleList);
            }
        }
    }

    function columnVisible(el,table){
        el.on('change', function (e) {
            e.preventDefault();
			console.log(table)
            var column = table.column($(this).attr('data-column'));
            column.visible(!column.visible());
        });
    }
    function showLoadding(text) {
        var modalaBackdrop = $('<div class="modal-backdrop fade in"></div>');
        var loadding =  $('<div class="loader" id="loadding">' +
            '<div class="loader-inner ball-spin-fade-loader">' +
            '<div></div>' +
            '<div></div>' +
            '<div></div>' +
            '<div></div>' +
            '<div></div>' +
            '<div></div>' +
            '<div></div>' +
            '<div></div>' +
            '</div>' +
            '<div class="text">'+text+'</div>' +
            '</div>' +
            '<div class="modal-backdrop fade in"></div>');
        $('body').append(loadding);
        var eleTop = ($('.modal-backdrop').height() - $('.ball-spin-fade-loader').height())/2;
        var eleLeft = ($('.modal-backdrop').width() - $('.ball-spin-fade-loader').width())/2;

        $('#loadding').css({
            'top':eleTop,
            'left':eleLeft,
            'display': 'block'
        });
        $('.modal-backdrop').addClass('in').css('display','block');
    }
    function hideLoadding() {
        $('#loadding').css('display', 'none');
        $('.modal-backdrop').removeClass('in').css('display','none');
    }
    return {
        mainWrapHeight : mainWrapHeight,    //1 计算main_wrap高度
        setTbodyH : setTbodyH,              //2 给table tbody赋值高度
        tdShowMaxLength : tdShowMaxLength,  //3 tbody内容过长用...拼接
        rowspanf : rowspanf,                //4 datatable合并列

        setInputSearch : setInputSearch,    //5 设置input搜索框
        AjaxError : AjaxError,              //6 异常错误提示
        getNewTime : getNewTime,            //7 加载日期框 - 年月日 ldy
        getData : getData,                  //8 加载日期框 - 年月日 lmm
        getDateTime : getDateTime,          //9 加载日期时间框 - 年月日-时分秒

        columnVisible:columnVisible,        // 显示隐藏列
        showCheckBox:showCheckBox,          // 动态创建显示隐藏列的input框
        showLoadding:showLoadding,          // 显示loadding
        hideLoadding:hideLoadding,          // 隐藏loadding
    }
})();