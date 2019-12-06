package com.fas.search.search.controller;

import com.fas.base.log.SystemControllerLog;
import com.fas.base.model.Page;
import com.fas.base.shiro.UserVO;
import com.fas.search.manage.entity.ZsSubject;
import com.fas.search.manage.entity.ZsUserRecord;
import com.fas.search.manage.service.ZsSubjectService;
import com.fas.search.manage.service.ZsUserRecordService;
import com.fas.search.search.exception.UploadFileException;
import com.fas.search.search.service.IndexPageService;
import com.fas.search.util.common.FileCommonUtil;
import com.fas.search.util.user.UserVOUtil;
import com.fas.search.util.view.ReturnDataUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @auther wuzy
 * @description 智能搜索首页搜功能controller
 * @date 2019/8/14
 */
@RestController
@RequestMapping("index")
public class IndexPageControler {

    @Autowired
    private ZsSubjectService zsSubjectService;

    @Autowired
    private ZsUserRecordService zsUserRecordService;

    @Autowired
    private IndexPageService indexPageService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 智能搜索首页，获取所有主体信息
     * @return
     */
    @RequestMapping(value = "/subject",method = RequestMethod.GET)
    @SystemControllerLog(description = "智能搜索首页，获取所有主体信息")
    public String listSubject(){
        ZsSubject subject = new ZsSubject();
        subject.setEnable("0");
        List<ZsSubject> zsSubjects = zsSubjectService.listByCondition(subject, null);
        return ReturnDataUtil.getData(zsSubjects);
    }


    /**
     * 获取用户搜索记录
     * @return
     */
    @RequestMapping(value = "/record" ,method = RequestMethod.GET)
    @SystemControllerLog(description = "获取用户搜索记录")
    public String listUserRecord(){
        List<String> maps = zsUserRecordService.latelyRecord(UserVOUtil.getUserID());
        return ReturnDataUtil.getData(maps);
    }


    /**
     * 下载批量查询模板
     * @param request
     * @param response
     */
    @RequestMapping(value = "download-template",method = RequestMethod.GET)
    @SystemControllerLog(description = "下载批量查询模板")
    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response){
        try {
            //获取当前工作路径
            String workPath = FileCommonUtil.getWorkPath();
            String[] classes = workPath.split("classes");
            //找到下载模板文件
            InputStream is = new FileInputStream(new File(classes[0]+"file/template.xlsx"));
            //设置下载格式
            response.setContentType("application/x-download");
            response.setHeader("Content-Disposition","attachement;filename=template.xlsx");
            //获取输出流
            ServletOutputStream outputStream = response.getOutputStream();
            //写入数据
            IOUtils.copy(is,outputStream);
            outputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 上传并解析批量查询文件
     * @param file
     * @return
     */
    @RequestMapping(value = "upload-file",method = RequestMethod.POST)
    @SystemControllerLog(description = "上传并解析批量查询文件")
    public String uploadFile(MultipartFile file,HttpSession session){
        //解析上传文件
        List<Set<String>> keys  = indexPageService.analysisSearchExcel(file);
        if (CollectionUtils.isEmpty(keys))
            throw new UploadFileException("上传文件未解析出内容，请确认后再试！");
        Set<String> allKeys = new HashSet<>();
        for (int i = 0; i < keys.size(); i++) {
            allKeys.addAll(keys.get(i));
        }
        if (CollectionUtils.isEmpty(allKeys))
            throw new UploadFileException("上传文件未解析出内容，请确认后再试！");
        redisTemplate.delete("fas_search_queryFile_"+UserVOUtil.getUserID());
        redisTemplate.opsForSet().add("fas_search_queryFile_"+UserVOUtil.getUserID(),allKeys.toArray());
        //返回处理结果
        return ReturnDataUtil.saveOrUpdateOrDel(1);
    }
}
