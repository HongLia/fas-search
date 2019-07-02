<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<c:set var="ctx" value="<%= basePath%>"/>
<script>
    var ctx =  "${ctx}";
    var MES_WARN="warning";
    var MES_SUCCESS="success";
    var MES_INFO="info";
    var MES_ERROR="error";
</script>
<link href="${ctx}/resources/assets/global/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/resources/assets/global/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/resources/assets/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/resources/assets/global/plugins/uniform/css/uniform.default.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/resources/assets/global/plugins/bootstrap-switch/css/bootstrap-switch.min.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/resources/assets/global/css/components.min.css" rel="stylesheet" id="style_components" type="text/css" />
<link href="${ctx}/resources/assets/global/css/plugins.min.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/resources/assets/layouts/layout/css/layout.min.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/resources/assets/layouts/layout/css/themes/darkblue.min.css" rel="stylesheet" type="text/css" id="style_color" />
<link href="${ctx}/resources/assets/global/plugins/bootstrap-datepicker/css/bootstrap-datepicker3.min.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/resources/assets/global/plugins/datatables/plugins/bootstrap/datatables.bootstrap.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/resources/assets/global/css/plugins.min.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/resources/assets/global/plugins/tabdrop/css/tabdrop.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/resources/assets/global/plugins/bootstrap-toastr/toastr.min.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/resources/assets/global/plugins/bootstrap-select/css/bootstrap-select.min.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="${ctx}/resources/css/ldy_form.css" type="text/css"/>
<link href="${ctx}/resources/css/custom.css" rel="stylesheet" type="text/css" />
<!--[if lt IE 9]>
<script src="${ctx}/resources/assets/global/plugins/respond.min.js"></script>
<script src="${ctx}/resources/assets/global/plugins/excanvas.min.js"></script>
<![endif]-->
<script src="${ctx}/resources/assets/global/plugins/jquery.min.js" type="text/javascript"></script>
<script src="${ctx}/resources/assets/global/plugins/jquery.form.js" type="text/javascript"></script>
<script src="${ctx}/resources/assets/global/plugins/jquery-validation/js/jquery.validate.min.js" type="text/javascript"></script>
<script src="${ctx}/resources/assets/global/plugins/jquery-validation/js/additional-methods.min.js" type="text/javascript"></script>
<script src="${ctx}/resources/assets/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<script src="${ctx}/resources/assets/global/plugins/bootstrap-hover-dropdown/bootstrap-hover-dropdown.min.js" type="text/javascript"></script>
<script src="${ctx}/resources/assets/global/scripts/app.min.js" type="text/javascript"></script>
<script src="${ctx}/resources/assets/global/plugins/bootbox/bootbox.min.js" type="text/javascript"></script>
<script src="${ctx}/resources/assets/global/plugins/datatables/datatables.min.js" type="text/javascript"></script>
<script src="${ctx}/resources/assets/global/plugins/datatables/plugins/bootstrap/datatables.bootstrap.js" type="text/javascript"></script>
<script src="${ctx}/resources/assets/global/plugins/bootstrap-datepicker/js/bootstrap-datepicker.min.js" type="text/javascript"></script>
<script src="${ctx}/resources/assets/global/plugins/select2/js/select2.full.min.js" type="text/javascript"></script>
<script src="${ctx}/resources/assets/global/plugins/bootstrap-wizard/jquery.bootstrap.wizard.min.js" type="text/javascript"></script>
<script src="${ctx}/resources/assets/global/plugins/tabdrop/js/bootstrap-tabdrop.js" type="text/javascript"></script>
<script src="${ctx}/resources/assets/global/plugins/bootstrap-toastr/toastr.min.js" type="text/javascript"></script>
<script src="${ctx}/resources/assets/global/plugins/bootstrap-select/js/bootstrap-select.min.js" type="text/javascript"></script>
<script src="${ctx}/resources/assets/global/plugins/jquery.cookie.js" type="text/javascript"></script>
<script src="${ctx}/scripts/analysisMenu.js" type="text/javascript"></script>
<script src="${ctx}/scripts/layout.js" type="text/javascript"></script>
<script src="${ctx}/scripts/main.js" type="text/javascript"></script>
<script src="${ctx}/scripts/use_utils.js" type="text/javascript"></script>
<script src="${ctx}/scripts/common.js" type="text/javascript"></script>
<style>
    /*datatable search框位置*/
    .search{
        float: left;
        padding-left: 15px;
    }
    .pageRow{
        position: fixed;
        bottom: 0px;
        width: 100%;
        z-index: 999;
        background-color: #ebebeb;
    }
    /*报错提示label  颜色*/
    .has-error label{
        color: #a94442;
    }
    input[type='search']{
        width: 300px!important;
    }
    /*隐藏列样式*/
    .changeHideBox{
        text-align: left;
        position: relative;
    }
    .changeHideUl{
        position: absolute;
        top: 0px;
        left: 90px;
        width: 200px;
        border: 1px solid #dedede;
        background: #fff;
        z-index: 999;
        padding: 5px 10px;
        display: none;
    }
    .changeHideUl input{
        height: inherit;
    }
    .toggleBox{
        position: absolute;
        cursor: default;
    }
</style>
<!--隐藏列-->
<div class="toggleBox changeHideUl"></div>