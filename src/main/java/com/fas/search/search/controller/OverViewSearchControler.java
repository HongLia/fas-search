package com.fas.search.search.controller;

import com.fas.base.log.SystemControllerLog;
import com.fas.base.model.LogTypeConstants;
import com.fas.base.model.Page;
import com.fas.search.manage.entity.ZsInfobar;
import com.fas.search.manage.entity.ZsInfobarEntityDTO;
import com.fas.search.manage.entity.ZsSubject;
import com.fas.search.manage.service.*;
import com.fas.search.search.bo.SearchResultBO;
import com.fas.search.search.controller.annotation.SearchControllerLog;
import com.fas.search.search.exception.SearchNoFileException;
import com.fas.search.search.searchenum.SearchType;
import com.fas.search.search.searchenum.ZsSearchType;
import com.fas.search.search.service.IndexPageService;
import com.fas.search.search.service.ZsOverviewSearchService;
import com.fas.search.search.vo.OverviewDataVo;
import com.fas.search.util.common.DateParseUtil;
import com.fas.search.util.common.FileCommonUtil;
import com.fas.search.util.user.UserVOUtil;
import com.fas.search.util.view.ReturnDataUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

/**
 * @auther wuzy
 * @description 智能搜索首页搜功能controller
 * @date 2019/8/14
 */
@RestController
@RequestMapping("ovsearch")
public class OverViewSearchControler {

    @Autowired
    private ZsOverviewSearchService zsOverviewSearchService;

    @Autowired
    private ZsSubjectService zsSubjectService;

    @Autowired
    private ZsInfobarService zsInfobarService;

    @Autowired
    private ZsInfobarEntityService zsInfobarEntityService;

    @Autowired
    private ZsEntityService zsEntityService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 数据概览，根据主题查询概览数据
     * @param key 搜索关键字
     * @param subject_id 主题id
     * @param page 分页
     * @return
     */
    @RequestMapping(value = "/subject-data/{key}/{subject_id}",method = RequestMethod.GET)
    @SystemControllerLog(description = "根据主题查询概览数据" , operType = LogTypeConstants.SELECT)
    @SearchControllerLog(keyIndex = 0,subjectIdIndex = 1,type = SearchType.GENERAL_SESRCH)
    public String subjectData(@PathVariable("key") String key,@PathVariable("subject_id") String subject_id, Page page){
        OverviewDataVo overviewDataVo = zsOverviewSearchService.listSubjectData(key, subject_id, page, "1", true);
        return ReturnDataUtil.getData(overviewDataVo);
    }


    /**
     * 数据概览，根据选择主题和上传文件查询概览数据
     * @param subject_id 主题id
     * @param page 分页信息
     * @return
     */
    @RequestMapping(value = "/subject-filed-data/{subject_id}",method = RequestMethod.GET)
    @SystemControllerLog(description = "根据主题查询概览数据" , operType = LogTypeConstants.SELECT)
    @SearchControllerLog(keyIndex = 0,subjectIdIndex = 1,type = SearchType.FILE_GENERAL_SESRCH)
    public String subjectFileData(@PathVariable("subject_id") String subject_id, Page page){
        String uploadFileKey = getUploadFileKey();
        OverviewDataVo overviewDataVo = zsOverviewSearchService.listSubjectData(uploadFileKey, subject_id, page, "2", true);
        return ReturnDataUtil.getData(overviewDataVo);
    }



    /**
     * 导出主题查询的数据
     * @param key 搜索关键字
     * @param subject_id 对应主题id
     * @return
     */
    @RequestMapping(value = "/subject-export/{key}/{subject_id}",method = RequestMethod.GET)
    @SystemControllerLog(description = "导出主题查询的数据" , operType = LogTypeConstants.SELECT)
    public void subjectExport(@PathVariable("key") String key, @PathVariable("subject_id") String subject_id, HttpServletResponse response) throws IOException {
        ZsSubject subject = zsSubjectService.getById(subject_id);
        //查找数据
        SXSSFWorkbook excel = zsOverviewSearchService.getSubjectExcel(key,subject_id,"1");
        String fileName = subject.getName() + "" + DateParseUtil.getTimeStampString(new Date())+".xlsx";
        exportFile(fileName,response,excel);
    }
    /**
     * 导出主题查询的数据
     * @param subject_id 对应主题id
     * @return
     */
    @RequestMapping(value = "/subject-file-export/{subject_id}",method = RequestMethod.GET)
    @SystemControllerLog(description = "导出主题查询的数据" , operType = LogTypeConstants.SELECT)
    public void subjectFileExport( @PathVariable("subject_id") String subject_id, HttpServletResponse response) throws IOException {
        ZsSubject subject = zsSubjectService.getById(subject_id);
        String uploadFileKey = getUploadFileKey();
        //查找数据
        SXSSFWorkbook excel = zsOverviewSearchService.getSubjectExcel(uploadFileKey,subject_id,"2");
        String fileName = subject.getName() + "" + DateParseUtil.getTimeStampString(new Date())+".xlsx";
        exportFile(fileName,response,excel);
    }


    /**
     * 数据概览，根据主题查询概览数据
     * @param key 搜索关键字
     * @param page 分页
     * @return
     */
    @RequestMapping(value = "/all-data/{key}",method = RequestMethod.GET)
    @SystemControllerLog(description = "根据主题查询概览数据" , operType = LogTypeConstants.SELECT)
    @SearchControllerLog(keyIndex = 0,type = SearchType.GENERAL_SESRCH)
    public String allData(@PathVariable("key") String key, Page page){
        OverviewDataVo overviewDataVo = zsOverviewSearchService.listAllData(key, page, "1", true);
        return ReturnDataUtil.getData(overviewDataVo);
    }

    /**
     * 数据概览，根据主题查询概览数据
     * @param page 分页
     * @return
     */
    @RequestMapping(value = "/all-file-data",method = RequestMethod.GET)
    @SystemControllerLog(description = "根据主题查询概览数据" , operType = LogTypeConstants.SELECT)
    @SearchControllerLog(type = SearchType.FILE_GENERAL_SESRCH)
    public String allFileData( Page page){
        String uploadFileKey = getUploadFileKey();
        OverviewDataVo overviewDataVo = zsOverviewSearchService.listAllData(uploadFileKey, page, "2", true);
        return ReturnDataUtil.getData(overviewDataVo);
    }



    /**
     * 导出主题查询的数据
     * @param key 搜索关键字
     * @return
     */
    @RequestMapping(value = "/all-export/{key}",method = RequestMethod.GET)
    @SystemControllerLog(description = "导出主题查询的数据" , operType = LogTypeConstants.SELECT)
    public void allExport(@PathVariable("key") String key, HttpServletResponse response) throws IOException {
        //查找数据
        SXSSFWorkbook excel = zsOverviewSearchService.getAllExcel(key,"1",null);
        exportFile(key + "" + DateParseUtil.getTimeStampString(new Date())+".xlsx",response,excel);
    }
    /**
     * 导出主题查询的数据
     * @return
     */
    @RequestMapping(value = "/all-file-export",method = RequestMethod.GET)
    @SystemControllerLog(description = "导出主题查询的数据" , operType = LogTypeConstants.SELECT)
    public void allExport( HttpServletResponse response) throws IOException {
        String uploadFileKey = getUploadFileKey();
        //查找数据
        SXSSFWorkbook excel = zsOverviewSearchService.getAllExcel(uploadFileKey,"2",null);
        exportFile(uploadFileKey + "" + DateParseUtil.getTimeStampString(new Date())+".xlsx",response,excel);
    }



    /**
     * 根据主体id获取信息栏信息
     * @param subject_id
     * @return
     */
    @RequestMapping(value = "/list-infobar/{subject_id}",method = RequestMethod.GET)
    @SystemControllerLog(description = "根据主体id获取信息栏信息",operType = LogTypeConstants.SELECT)
    public String listBySubjectId(@PathVariable("subject_id") String subject_id){
        //查询
        List<ZsInfobar> zsInfobars = zsInfobarService.listBySubjectId(subject_id);
        //返回
        return ReturnDataUtil.getData(zsInfobars);
    }


    /**
     * 列表获取信息栏下纬度信息
     * @param infobar_id 信息栏id
     * @return
     */
    @RequestMapping(value = "/infobar-entity/{infobar_id}",method = RequestMethod.GET)
    @SystemControllerLog(description = "列表获取信息栏下纬度信息",operType = LogTypeConstants.SELECT)
    public String infobarEntity(@PathVariable("infobar_id") String infobar_id){
        List<ZsInfobarEntityDTO> zsInfobarEntityDTOS = zsInfobarEntityService.listInfobarEntitys(infobar_id);
        return ReturnDataUtil.getData(zsInfobarEntityDTOS);
    }

    /**
     * 列表获取信息栏下纬度信息
     * @param entity_id 实体id
     * @param infobar_entity_id 实体信息栏id
     * @param key 关键字
     * @param page 分页信息
     * @return
     */
    @RequestMapping(value = "/entity-data/{entity_id}/{infobar_entity_id}/{key}/{subject_id}",method = RequestMethod.GET)
    @SystemControllerLog(description = "列表获取信息栏下纬度信息",operType = LogTypeConstants.SELECT)
    @SearchControllerLog(entityIdIndex = 0,keyIndex = 2,subjectIdIndex = 3,type = SearchType.INFOBAR_SEARCH)
    public String entityData(@PathVariable("entity_id") String entity_id, @PathVariable("infobar_entity_id") String infobar_entity_id, @PathVariable("key") String key,@PathVariable("subject_id")String subject_id, Page page){
        SearchResultBO searchResultBO = zsOverviewSearchService.entityData(key, page, entity_id, infobar_entity_id, "1", true);
        return ReturnDataUtil.getData(searchResultBO);
    }
    /**
     * 列表获取信息栏下纬度信息
     * @param entity_id 实体id
     * @param infobar_entity_id 实体信息栏id
     * @param page 分页信息
     * @return
     */
    @RequestMapping(value = "/entity-file-data/{entity_id}/{infobar_entity_id}/{subject_id}",method = RequestMethod.GET)
    @SystemControllerLog(description = "列表获取信息栏下纬度信息",operType = LogTypeConstants.SELECT)
    @SearchControllerLog(entityIdIndex = 0,subjectIdIndex = 2,type = SearchType.FILE_INFOBAR_SEARCH)
    public String entityFileData(@PathVariable("entity_id") String entity_id, @PathVariable("infobar_entity_id") String infobar_entity_id, @PathVariable("subject_id")String subject_id, Page page){
        String uploadFileKey = getUploadFileKey();
        SearchResultBO searchResultBO = zsOverviewSearchService.entityData(uploadFileKey, page, entity_id, infobar_entity_id, "2", true);
        return ReturnDataUtil.getData(searchResultBO);
    }


    /**
     * 获取主题信息栏的某实体详细信息
     * @param entity_id
     * @param infobar_entity_id
     * @param data_id
     * @return
     */
    @RequestMapping(value = "/entity-detail-data/{entity_id}/{infobar_entity_id}/{data_id}",method = RequestMethod.GET)
    @SystemControllerLog(description = "获取主题信息栏的某实体详细信息",operType = LogTypeConstants.SELECT)
    public String entityDetailData(@PathVariable("entity_id") String entity_id,@PathVariable("infobar_entity_id") String infobar_entity_id, @PathVariable("data_id") String data_id){
        //SearchResultBO searchResultBO = zsOverviewSearchService.entityData(key, page, entity_id, infobar_entity_id, "1", true);
        Map<String, Object> data = zsOverviewSearchService.entityDetailData(entity_id, infobar_entity_id, data_id);
        return ReturnDataUtil.getData(data);
    }


    /**
     * 导出主题查询的数据
     * @param key 搜索关键字
     * @return
     */
    @RequestMapping(value = "/infobar-export/{key}/{infobar_id}",method = RequestMethod.GET)
    @SystemControllerLog(description = "导出主题查询的数据" , operType = LogTypeConstants.SELECT)
    public void infobarExport(@PathVariable("key") String key, @PathVariable("infobar_id") String infobar_id, HttpServletResponse response) throws IOException {
        //查找数据
        SXSSFWorkbook excel = zsOverviewSearchService.getInfobarExcel(key,infobar_id,"1",false);
        exportFile(key + "" + DateParseUtil.getTimeStampString(new Date())+".xlsx",response,excel);
    }

    /**
     * 导出主题查询的数据
     * @return
     */
    @RequestMapping(value = "/infobar-file-export/{infobar_id}",method = RequestMethod.GET)
    @SystemControllerLog(description = "导出主题查询的数据" , operType = LogTypeConstants.SELECT)
    public void infobarFileExport( @PathVariable("infobar_id") String infobar_id, HttpServletResponse response) throws IOException {
        //查找数据
        String uploadFileKey = getUploadFileKey();
        SXSSFWorkbook excel = zsOverviewSearchService.getInfobarExcel(uploadFileKey,infobar_id,"2",false);
        exportFile( DateParseUtil.getTimeStampString(new Date())+".xlsx",response,excel);
    }

    public void exportFile(String fileName,HttpServletResponse response,SXSSFWorkbook excel) throws IOException {
        response.setContentType("apalication/vnd.ms-excel");
        response.setHeader("Content-Disposition","attachment;filename="+new String(fileName.getBytes("utf-8"),"ISO8859-1"));
        // response.setCharacterEncoding("utf-8");
        OutputStream out = response.getOutputStream();
        //out.write(FileUtils.readFileToByteArray(mubiao));
        excel.write(out);
        out.flush();
        out.close();
    }


    public String getUploadFileKey(){
        SetOperations setOperations = redisTemplate.opsForSet();
        Set<String> keys = setOperations.members("fas_search_queryFile_" + UserVOUtil.getUserID());
        if (CollectionUtils.isEmpty(keys)){
            throw new SearchNoFileException("请上传文件以后再进行文件搜索");
        }
        StringBuilder sb = new StringBuilder();
        for (Object key: keys) {
            sb.append(key).append(" ");
        }
        return sb.toString();
    }
}