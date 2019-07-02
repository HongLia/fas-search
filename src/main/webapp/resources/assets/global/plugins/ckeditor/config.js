/**
 * @license Copyright (c) 2003-2014, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */
CKEDITOR.editorConfig = function( config ) {
	// Define changes to default configuration here. For example:
	config.language = 'zh-cn';
    config.height = 400;
    config.resize_enabled = false;
    config.toolbar=[
        //源码
        //['Source'],
        //加粗     斜体，     下划线      穿过线      下标字        上标字
        ['Bold','Italic','Underline'],//,'Strike','Subscript','Superscript'],
        //文本颜色     背景颜色
        ['TextColor','BGColor'],
        // 数字列表          实体列表            减小缩进    增大缩进
        ['NumberedList','BulletedList','-','Outdent','Indent'],
        //左对 齐             居中对齐          右对齐          两端对齐
        ['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
        //复选      单选       单行文本     多行文本    下拉框
        ['Checkbox','Radio','TextField','Textarea','Select'],
        //超链接  取消超链接 锚点 
        ['Link','Unlink','Anchor'],
        //表格       水平线            表情       特殊字符        分页符 图片 Image  flash  Flash 视频 Video
        ['Table','HorizontalRule','Smiley','SpecialChar','PageBreak','Image','Flash'],
        // 样式       格式      字体    字体大小
        ['Styles','Format','Font','FontSize'],
        //全屏           显示区块
        ['Maximize', 'ShowBlocks','-']
    ];
    config.filebrowserImageUploadUrl = "ckuploadfile";
    config.filebrowserFlashUploadUrl = "ckuploadfile";
    config.font_names='微软雅黑;楷体_GB2312;新宋体;宋体;仿宋;黑体;';
    config.extraPlugins = "video";
};
