var firstURL = "welcome";

$(document).ready(function() {
	
	//loadFirstURL(firstURL);
	// 禁止右键
	document.oncontextmenu=function(e){
		return false;
	}; 
});

function loadFirstURL(url) {
	s = ($(".page-content"), $(".page-content .page-content-body"));
    $.ajax({
    	type: "GET",
    	cache: !1,
    	url: url,
    	dataType: "html",
    	success: function(res) {
    		var pageContent = $(res);
    		s.html(pageContent);
    	}
    })  
} 

// 系统退出
function logout() {
	bootbox.dialog({
    	message: "确定要退出系统吗？",
    	title: "提示",
 		buttons: {
       		cancel: {
              label: "取消",
              className: "btn btn-default"
            },
            ok: {
              label: "确认",
              className: "btn btn-primary",
              callback: function() {
            	  window.location = "logout";
              }
      		}
		}
	});
}