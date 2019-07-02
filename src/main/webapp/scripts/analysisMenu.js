var _gas_analysis = {
    _this: this,
    isEmpty: function (object) {
        for (var name in object) {
            return false;
        }
        return true;
    },
    loadMenu: function (menuid, showFirst) {
        var _this = this;

        $.post(ctx + '/analysis/web/common/queryMenu', {typeid: menuid}, function (data) {
            //console.log(data);
            _this.appendHtml(data, showFirst);
        });
    },
    appendHtml: function (data, showFirst) {
        var _this = this;

        var htmlContent = '';
        var url;
        var icon;
        var num = 0;

        data.forEach(function (item) {
            //console.log(item.mname);
            //item.children

            icon = item.icon;
            if (icon == undefined || icon == 'null') {
                icon = 'icon-home';
            }

            if (showFirst == true && num == 0) {
                htmlContent += '<li class="nav-item start open">';
            } else {
                htmlContent += '<li class="nav-item ">';
            }

            htmlContent += '<a href="javascript:;" class="nav-link nav-toggle">' +
                '<i class="' + icon + '"></i>' +
                '<span class="title">' + item.mname + '</span>' +
                '<span class="arrow"></span>' +
                '</a>';

            appendChildren(item, num);

            htmlContent += '</li>';

            num++;

            return false;
        });

        $('.page-sidebar-menu').append(htmlContent);

        function appendChildren(data, num) {

            if (data.children.length > 0) {
                if (num == 0) {
                    htmlContent += '<ul class="sub-menu" style="display:block">';
                } else {
                    htmlContent += '<ul class="sub-menu">';
                }

                data.children.forEach(function (item) {

                    url = item.murl;
                    if (url == undefined || url == 'null') {
                        url = '#';
                    }

                    icon = item.icon;
                    if (icon == undefined || icon == 'null') {
                        icon = 'icon-home';
                    }

                    htmlContent += '<li class="nav-item">' +
                        '<a href="#" url="' + url + '" class="nav-link " mid="' + item.mid + '">' +
                        '<i class="' + icon + '"></i>' +
                        '<span class="title">' + item.mname + '</span>' +
                        '</a>';

                    appendChildren(item);

                    htmlContent += '</li>';

                });

                htmlContent += '</ul>';
            }

        }

        //console.log($('.page-sidebar-menu').html());

        _this.initLinkUrl();

    },
    initLinkUrl: function () {
        var _this = this;

        $('.page-sidebar-wrapper .nav-item .sub-menu a').each(function () {

            $(this).on('click', function (e) {
                var url = $(this).attr('url');

                if (url == undefined || url == null || url == '#')return;

                var pageContent;

                if (url.indexOf('?') != -1) {
                    url += "&";
                } else {
                    url += "?";
                }

                var id = $(this).attr('mid');
                var url = url + "mid=" + id;
                var title = $(this).text();

                _this.addTabsByAjax(id, url, title);

            });
        });
    },
    addTabsByAjax: function (id, url, title, close) {
        var _this = this;

        var item = {};
        item.id = id;
        item.url = ctx + url;
        item.title = title;
        item.frameHeight = $(document).height() - 150;
        item.close = close == undefined ? true : close;

        if(url.indexOf('model/')!=-1){
            _this.addTabs(item);
            return;
        }

        $.ajax({
            type: "GET",
            cache: false,
            url: item.url,
            dataType: "html",
            success: function (res) {
                item.content = res;

                _this.addTabs(item);

            },
            error: function (xhr, ajaxOptions, thrownError) {
                //pageContent = '<h4>不能加载内容……</h4>';
            }
        });
    },
    addTabs: function (item, reload) {
        var id = item.id;
        $('.nav-tabs .active').removeClass('active');
        $('.tab-content .active').removeClass('active');

        if (!$('#tab_' + id)[0]) {
            var title = '<li id="tab_' + id + '"><a href="#tab_content_' + id + '" data-toggle="tab"> ' + item.title + '';
            if (item.close) {
                // title += "<span style='padding-left: 3px;cursor: pointer;font-size: 10px;position: absolute;right: 3px;top: 3px;' class='glyphicon glyphicon-remove red' tabclose='" + id + "'></span>";
                title += "<span class='close' style='width: 14px;height: 14px;cursor: pointer;font-size: 10px;position: absolute;right: 0px;top: 3px;background-image: url("+ctx+"/resources/guanbi.png)' tabclose='" + id + "'></span>";
            }
            title += '</a></li>';

            var content;
            if (item.content) {
                content = '<div class="tab-pane fade in" id="tab_content_' + id + '">' + item.content + '</div>';
            } else {
                var mainHeight;
                if (item.frameHeight) {
                    mainHeight = item.frameHeight;
                } else {
                    mainHeight = $(document.body).height();
                }
                content = '<div class="tab-pane fade in" id="tab_content_' + id + '"><iframe scrolling="auto" src="' + item.url + '" width="100%" height="' + mainHeight + '" frameborder="0" border="0" marignwidth="0" marginheight="0" scrolling="no" allowtransparency="yes"></iframe></div>';
            }

            $('.nav-tabs').append(title);
            $('.tab-content').append(content);
            if($('#rightBloder').length>0) {
                hiddenShow($('#rightBloder'));
            }
            //$('#clickAffter_2 :nth-child(1) :nth-child(2)').remove();

        } else if (reload == true) {

            $('#tab_content_' + id + " iframe").attr('src', item.url);

        }
        if (!$('#tab_content_' + id).hasClass('in')) {
            $('#tab_content_' + id).addClass('in');
        }
        if (!$('#tab_' + id).hasClass('in')) {
            $('#tab_' + id).addClass('in');
        }
        $('#tab_' + id).addClass('active');
        $('#tab_content_' + id).addClass('active');

    },
    closeTab: function (id) {
        //if($('li .active').attr('id')=='tab_'+id){

        if($('#tab_' + id).parent().hasClass('dropdown-menu')){

            var item = $('#tab_' + id).parent().children(':first');
            var thisId=item.attr('id').replace('tab_','');
            if(id==thisId){
                if($('#tab_' + id).parent().children().length>1){
                    item = $('#tab_' + id).parent().children('li:eq(1)');
                    thisId=item.attr('id').replace('tab_','');
                }else{
                    item = $('#tab_' + id).parents('.nav-tabs').children(':last');
                    thisId=item.attr('id').replace('tab_','');
                }
            }
            item.addClass('active');
            $('#tab_content_' + thisId).addClass(' in active');

        }else if($('#tab_' + id).hasClass('active') && $('#tab_content_' + id).hasClass('active')){

            $('#tab_' + id).prev().addClass('active');
            $('#tab_content_' + id).prev().addClass(' in active');

        }



        $('#tab_' + id).remove();
        $('#tab_content_' + id).remove();

        $(window).resize();

    },
    createTable: function (_tableId, _pageId, item) {
        tableId = "#" + _tableId;
        pageId = "#" + _pageId;

        var option = {
            data: item.data == undefined ? null : item.data,
            url: item.url == undefined ? null : ctx + item.url,
            datatype: item.url == undefined ? "local" : "json",
            height: item.height,
            styleUI: 'Bootstrap',
            //colNames: item.colNames,
            colModel: item.colModels,
            rowNum: 20,
            rowList: [20, 50, 100],
            pager: pageId,
            viewrecords: true,
            altRows: true,
            rownumbers: true,
            // toppager: true,

            multiselect: false,
            // multikey: "ctrlKey",
            multiboxonly: false,

            loadComplete: function () {
            },

            onSelectRow: function (rowid) {
                if (item.itemClickCallback == undefined)
                    return

                if (typeof(item.itemClickCallback) == 'function') {
                    item.itemClickCallback(tableId, pageId, rowid);
                } else {
                    try {
                        eval(item.itemClickCallback)(tableId, pageId, rowid);
                    } catch (e) {
                        console.log(e);
                    }
                }
            },

            editurl: item.editUrl == undefined ? "" : item.editUrl,
            caption: "",
            //width:[item.width==undefined?1100:item.width],
            shrinkToFit: false, //[item.shrinkToFit==undefined?true:false],
            autowidth: true,
            toolbar: [item.toolbar == undefined ? false : true, 'top']
        };

        if (item.data == undefined) {
            delete option.data;
        }

        jQuery(tableId).jqGrid(option);

        jQuery(tableId).jqGrid('navGrid', pageId, {
            edit: item.edit == undefined ? false : true,
            add: item.add == undefined ? false : true,
            del: item.del == undefined ? false : true,
            search: item.search == undefined ? false : true,
            refresh: item.refresh == undefined ? false : true,
            view: item.view == undefined ? false : true,
        }, {}, {}, {}, {
            //search form
            recreateForm: true,
            multipleSearch: true
        });


        addBtn2Grid($("#t_" + _tableId), item.btnDefines);


        function addBtn2Grid(p, btnDefines) {
            if (!btnDefines)return;
            if (typeof(btnDefines) == "string") {
                btnDefines = $.parseJSON(btnDefines);
            }
            for (var i = 0; i < btnDefines.length; i++) {

                var b = btnDefines[i];
                var color = b.color;
                if (color.indexOf("#") == 0) {
                    color = "\" style=\"background:" + color + ";margin:5px;";
                } else {
                    color = b.color + "\" style=\"margin:5px;";
                }
                var newBtn = $("<a href=\"javascript:;\" class=\"btn btn-sm " + color + "\"><i class=\"fa " + b.icon + "\"></i>" + b.title + "</a>");
                newBtn.on("click", b.clkMethod);
                p.append(newBtn);
            }
        }

    },
    showTables: function (filter) {
        var _this = this;

        var selector;
        if (filter != undefined) {
            selector = '.tableGrid' + filter + ' table';
        } else {
            selector = '.tableGrid table';
        }

        $(selector).each(function () {

            var tid = $(this).parent().attr('tid');
            if (tid == undefined)return;

            var tableid = $(this).attr('id');
            var pageid = $(this).next('div').attr('id');


            $.post(ctx + '/analysis/layout/queryTableInfo', {tid: tid}, function (data) {
                var item = {};
                item.url = data.url + "?tid=" + tid + "&caseid=" + caseid;
                item.height = data.height;
                item.colModels = data.columns;
                item.search = true;
                item.view = true;
                _this.createTable(tableid, pageid, item);
            });
        });
    },
    deleteEmptyProperty: function (object) {
        var _this = this;

        for (var i in object) {
            var value = object[i];
            if (typeof value === 'object') {
                if (Array.isArray(value)) {
                    if (value.length == 0) {
                        delete object[i];
                        continue;
                    }
                }
                _this.deleteEmptyProperty(value);

                if (_this.isEmpty(value)) {
                    delete object[i];
                }
            } else {
                if (value === '' || value === null || value === undefined) {
                    delete object[i];
                } else {
                }
            }
        }
    },
    buildChart: function (divid, tid) {
        var _this = this;

        require.config({
            paths: {
                echarts: ctx + '/bootstrap/metronic/global/plugins/echarts/'
            }
        });
        require(
            [
                'echarts',
                'echarts/chart/bar',
                'echarts/chart/chord',
                'echarts/chart/eventRiver',
                'echarts/chart/force',
                'echarts/chart/funnel',
                'echarts/chart/gauge',
                'echarts/chart/heatmap',
                'echarts/chart/k',
                'echarts/chart/line',
                'echarts/chart/map',
                'echarts/chart/pie',
                'echarts/chart/radar',
                'echarts/chart/scatter',
                'echarts/chart/tree',
                'echarts/chart/treemap',
                'echarts/chart/venn',
                'echarts/chart/wordCloud'
            ],
            function (ec) {
                var myChart = ec.init(document.getElementById(divid));
                $.post(ctx + '/analysis/layout/charLine', {tid: tid}, function (data) {
                    _this.deleteEmptyProperty(data);
                    myChart.setOption(data);
                });
            }
        );
    }

}

$(function () {

    /**
     * header 样式
     */
    $('.hor-menu .classic-menu-dropdown').each(function (e) {
        $(this).removeClass("active");

        var windowUrl = window.location.href;
        if (windowUrl.indexOf($(this).find("a").attr("href")) != -1) {
            $(this).addClass("active");
        }

    });

//setTimeout("initFrame()", 1000);


    $('.tab-content .tab-pane iframe').each(function () {
        $(this).height($(document).height() - 150);
    });


    $('.nav-tabs').on('click', '[tabclose]', function (e) {
        var id = $(this).attr('tabclose');
        _gas_analysis.closeTab(id);
    });


    // if (mid != '') {
    //     _gas_analysis.loadMenu(mid, true);
    // }

    //_gas_analysis.showTables();

});



