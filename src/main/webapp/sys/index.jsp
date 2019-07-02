<%@ page import="org.apache.shiro.subject.Subject" %>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@ page import="com.fas.base.shiro.UserVO" %>
<%@ page import="net.sf.json.JSONArray" %>
<%@ page import="net.sf.json.JSONObject" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <title>用户权限统一管理系统</title>
    <%@ include file="js.jsp"%>
    <%
        Subject subject = SecurityUtils.getSubject();
        UserVO userVO = (UserVO) subject.getPrincipal();
        String menu = userVO.getMENUS();
        JSONArray menuArray = null;
        JSONArray jsonArray = JSONArray.fromObject(menu);
        for(int i = 0;i<jsonArray.size();i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if(jsonObject.get("APP_ID").equals("fas-os")){
                menuArray = JSONArray.fromObject(jsonObject.get("MENU"));
            }
        }
        request.setAttribute("menus",menuArray);
    %>
</head>
<style>
    .ecuser_title{
        padding-left:20px;
        text-align:left;
        font-size:16pt;
        font-family: '微软雅黑';
        color: #fff;
        font-weight: bold;
    }
    #titletext{
        margin-top: 10px;
    }
    .nav-tabs>li {
        border-right: 1px solid #ddd;
        border-left: 1px solid #ddd;
    }
    .close:hover{
        background-image: url("${ctx}/resources/guanbi2.png");
    }
    .nav-tabs>li>a{
        margin-left: 2px;
        margin-right: 2px;
    }
    .nav>li>a {
        padding: 8px 15px;
    }
    .page-content-wrapper .page-content {
        padding:0px!important;
    }
    .tabbable{
        border: 2px solid #ededed;
    }
    .tabbable >ul{
        background-color: #ededed;
    }
</style>
<body>
<!-- BEGIN CONTAINER -->
<div class="page-container">
    <!-- BEGIN SIDEBAR -->
    <div class="page-sidebar-wrapper">
        <div class="page-sidebar navbar-collapse collapse">
            <!-- BEGIN SIDEBAR MENU -->
            <ul class="page-sidebar-menu  page-header-fixed " data-keep-expanded="false" data-auto-scroll="true" data-slide-speed="200" style="font-family: 微软雅黑">
                <li class="sidebar-toggler-wrapper">
                    <div class="page-logo">
                        <div class='ecuser_title'>
                            <%--<a href="${ctx}"><img id="titletext" src="resources/logo.png" alt="logo" class="logo-default" /></a>--%>
                            <div onclick="$('#titletext').toggle()" class="menu-toggler sidebar-toggler" style="margin: 8px;"/>
                        </div>
                    </div>
                </li>
                <c:forEach items="${menus}" var="menu" varStatus="status">
                    <c:if test="${menu.map.VALUES!=''}">
                        <c:if test="${status.first}">
                            <li class="nav-item active open">
                        </c:if>
                        <c:if test="${!status.first}">
                            <li class="nav-item">
                        </c:if>
                            <a href="javascript:;" class="nav-link nav-toggle">
                                ${menu.map.ICONCLS}<span class="title">${menu.map.NAME}</span>
                                <span class="arrow"></span>
                                <span class="selected"></span>
                            </a>
                            <ul class="sub-menu">
                                <c:forEach items="${menu.children}" var="VALUE">
                                    <li class="nav-item"><a class="ajaxify" ecid="${VALUE.map.RESOURCEID}" ecicon="${VALUE.map.ICONCLS}" ecaction="${VALUE.map.ACTION}" ecname="${VALUE.map.NAME}" onclick="menuclick(this);">${VALUE.map.ICONCLS}&nbsp;${VALUE.map.NAME}</a></li>
                                </c:forEach>
                            </ul>
                        </li>
                    </c:if>
                </c:forEach>
            </ul>
            <!-- END SIDEBAR MENU -->
        </div>
    </div>
    <!-- END SIDEBAR -->

    <!-- BEGIN CONTENT -->
    <div class="page-content-wrapper">
        <div class="page-content">
            <div class="tabbable"><!-- tabbable-custom 有外边距 谷歌会出滚动条 -->
                <ul class="nav nav-tabs">

                </ul>
                <div class="tab-content">
                </div>
            </div>
        </div>
    </div>
    <!-- END CONTENT -->
</div>
<!-- END FOOTER -->
<script type="application/javascript">
    $(function () {
        $(".nav-tabs").tabdrop();
        menuclick();
    });
    function  menuclick(obj) {
        var item = {};
        item.frameHeight = $(document).height() - 70;
        item.id = "home";
        item.url = ctx + "/sys/user.jsp";
        //<i class='icon-home'></i>
        item.title = "首页";
        item.close =false;
        if(obj){
            var url = $(obj).attr("ecaction"); var icon = $(obj).attr("ecicon"); var title = $(obj).attr("ecname");
            var ecid = $(obj).attr("ecid");
            item.id = ecid;
            if(url.indexOf("http://")==0){
                item.url = url;
            }else{
                item.url = ctx + url;
            }
            item.title = icon+title;
            item.close =true;
        }
        _gas_analysis.addTabs(item);//添加tab（默认添加的一行放不下会放两行）
        $(window).resize();//手动触发窗体resize时间  触发tabdrop
    }
</script>
</body>
</html>