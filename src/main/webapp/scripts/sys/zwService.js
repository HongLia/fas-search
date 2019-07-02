var maxid = 0;
var count = 0;
var divID = "";
// 采集指纹
function gerherZw(did) {
    divID = did;
    bootbox.alert("请按手指");
    getZwDataCount();
}

// 获取用户的指纹信息
function showZwData(){
    var userID = $("#zwUserID").val();
    if (userID == null || userID == '' || userID == 'undefined'){
        bootbox.alert("获取警号失败");
        return;
    }
    $.ajax({
        url: ctx+'user/showZwData',
        cache: false,
        data:{
            userID:userID
        },
        type: "POST",
        dataType: 'json',
        success: function (data) {
            if (data != null && data != ''&& data != 'undefined'){
                for (var i = 0;i<data.length;i++){
                    var info = data[i];
                    var id = info.id;
                    var sortID = info.sortID;
                    $("#zwID_"+sortID).val(id);
                    $("#zwDiv_"+sortID).html("").html('<img src="'+ctx+'resources/img/zw.png" style="width: 100%;height: 100%">')
                }
            }else{
                for (var i = 0;i<3;i++){
                    var id = i+ 1;
                    $("#zwID_"+id).val('');
                    $("#zwDiv_"+id).html("").html('<button class="btn default btn-file" style="margin-top: 40%;" onclick="gerherZw('+id+')">采集</button>')
                }
            }
        },
        error: function (error) {
            bootbox.alert(error)
        }
    });
}

function getZwMaxIdAndCount(){
    $.ajax({
        url: ctx+'user/getZwMaxIdAndCount',
        cache: false,
        type: "POST",
        async:false,
        dataType: 'json',
        success: function (data) {
            maxid = data.maxId;
            count = data.count;
        },
        error: function (error) {
            bootbox.alert(error)
        }
    });
}

// 获取指纹数据
function getZwData(startData){
    $.ajax({
        url: ctx+'user/getZwData',
        cache: false,
        data:{
            startData:startData
        },
        async:false,
        type: "POST",
        dataType: 'text',
        success: function (data) {
            getZwService(data);
        },
        error: function (error) {
            bootbox.alert(error)
        }
    });
}

// 查询指纹服务是否有指纹数据，有则不需要传输
function getZwDataCount(){
    $.ajax({
        url: 'http://localhost:18080/queryZwData',
        cache: false,
        type: "POST",
        async:false,
        dataType: 'text',
        success: function (data) {
            getZwMaxIdAndCount();
            if (data == '0'){
                for (var i = 0;i<count;i++){
                    getZwData(i);
                    i += 500;
                    i--;
                }
            }
            addZwData();
        },
        error: function (error) {
            bootbox.alert(error)
        }
    });
}

// 初始化服务端的指纹数据
function getZwService(data){
    $.ajax({
        url: 'http://localhost:18080/initZwData',
        cache: false,
        type: "POST",
        async:false,
        data:{
            data:data
        },
        dataType: 'text',
        success: function (data) {

        },
        error: function (error) {
            bootbox.alert(error)
        }
    });
}

// 开始获取指纹
function addZwData(){
    $.ajax({
        url: 'http://localhost:18080/addZwInfoMethod',
        cache: false,
        data:{
            maxid : maxid
        },
        type: "POST",
        dataType: 'text',
        success: function (data) {
            returnInfo (data,null);
        },
        error: function (error) {
            bootbox.alert(error)
        }
    });
}

function returnInfo (data,type){
    if (data == null || data == '' || data == 'undefined'){
        bootbox.alert("指纹获取失败");
    }else if(data == 'error_1'){
        bootbox.alert("指纹机异常");
    }else if(data == 'error_2'){
        bootbox.alert("请先关闭指纹仪");
    }else if(data == 'error_3'){
        bootbox.alert("请连接指纹仪");
    }else if(data == 'error_4'){
        bootbox.alert("没有设备连接");
    }else if(data == 'error_5'){
        bootbox.alert("打开指纹仪失败");
    }else if(data == 'error_6'){
        bootbox.alert("初始化指纹库失败");
    }else if(data == 'error_7'){
        bootbox.alert("指纹库获取失败");
    }else if(data == 'error_8'){
        bootbox.alert("请重新打开指纹仪");
    }else if(data == 'error_9'){
        bootbox.alert("指纹库添加指纹失败");
    }else if(data == 'error_10'){
        bootbox.alert("指纹已经存在");
    }else if(data == 'error_11'){
        bootbox.alert("指纹采集失败");
    }else if(data == 'error_12'){
        bootbox.alert("指纹合并失败");
    }else if(data == 'info_1'){
        bootbox.alert("请按同一个手指三次");
    }else if(data == 'success_info_2'){
        bootbox.alert("请按第三次");
        addZwData();
    }else if(data == 'success_info_3'){
        bootbox.alert("请按第二次");
        addZwData();
    }else if(data == 'success_info_4'){
        bootbox.alert("请按第一次");
        addZwData();
    }else if(data == 'info_5'){
        bootbox.alert("获取指纹超时");
    }else if(data == 'info_6'){
        bootbox.alert("假指纹");
    }else if(data == 'info_7'){
        bootbox.alert("比对失败，请确认是否已经录取指纹！");
    }else if(data.indexOf('comparSuccess') >= 0 ){
        var fid = data.substring("comparSuccess_".length);
        if (fid != null && fid != '' && fid != 'undefined') {
            getUserInfoByFid(fid,type);
        }else{
            bootbox.alert("获取指纹id失败");
        }
    }else if(data.indexOf('addSucce') >= 0 ){
        var s = data.substr("addSucce,".length);
        var fid = s.substr(0,s.indexOf(","));
        var zw = s.substr(s.indexOf(",")+1);
        var userID = $("#zwUserID").val();
        addZwDataByUserID(userID,fid,zw);
    }else{
        bootbox.alert(data);
    }
}

// 将指纹信息添加到数据库
function addZwDataByUserID(userID,fid,zw){

    $.ajax({
        url: ctx+'user/addZwDataByUserID',
        cache: false,
        type: "POST",
        data:{
            divID:divID,
            userID:userID,
            fid:fid,
            zw:zw
        },
        dataType: 'text',
        success: function (data) {
            $("#zwID_"+divID).val(fid);
            $("#zwDiv_"+divID).html("");
            $("#zwDiv_"+divID).html('<img src="'+ctx+'resources/img/zw.png" style="width: 100%;height: 100%">');

        },
        error: function (error) {
            bootbox.alert(error)
        }
    });
}

function deleteZw(zid){
    var zwid = $("#zwID_"+zid).val();
    if (zwid != null && zwid != '' && zwid != 'undefined'){
        $.ajax({
            url: ctx+'user/deleteZwByID',
            cache: false,
            type: "POST",
            data:{
                zwid:zwid
            },
            dataType: 'text',
            success: function (data) {
                $("#zwID_"+zid).val('');
                $("#zwDiv_"+zid).html("");
                $("#zwDiv_"+zid).html('<button class="btn default btn-file" style="margin-top: 40%;" onclick="gerherZw('+zid+')">采集</button>');
                deleteZwServiceData();
            },
            error: function (error) {
                bootbox.alert(error)
            }
        });
    }else{
        bootbox.alert("指纹删除失败");
    }

}

// 删除指纹后需要清空服务端的缓存
function deleteZwServiceData(){
    $.ajax({
        url: 'http://localhost:18080/deleteZwServiceData',
        cache: false,
        type: "POST",
        error: function (error) {
            bootbox.alert(error)
        }
    });
}

