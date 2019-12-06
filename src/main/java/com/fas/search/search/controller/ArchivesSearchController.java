package com.fas.search.search.controller;

import com.fas.base.log.SystemControllerLog;
import com.fas.base.model.LogTypeConstants;
import com.fas.base.model.Page;
import com.fas.search.manage.entity.ZsArchivesDTO;
import com.fas.search.manage.entity.ZsUserRecord;
import com.fas.search.manage.service.ZsArchivesService;
import com.fas.search.manage.service.ZsUserRecordService;
import com.fas.search.search.bo.SearchResultBO;
import com.fas.search.search.controller.annotation.SearchControllerLog;
import com.fas.search.search.searchenum.SearchType;
import com.fas.search.search.service.ArchivesSearchService;
import com.fas.search.search.service.ZsUserAttentionService;
import com.fas.search.util.user.UserVOUtil;
import com.fas.search.util.view.ReturnDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @auther wuzy
 * @description 档案数据相关接口
 * @date 2019/11/19
 */
@RestController
@RequestMapping("searcharchives")
public class ArchivesSearchController {

    @Autowired
    private ArchivesSearchService archivesSearchService;

    @Autowired
    private ZsUserAttentionService zsUserAttentionService;

    @Autowired
    private ZsUserRecordService zsUserRecordService;

    @Autowired
    private ZsArchivesService zsArchivesService;


    /**
     * 获取档案信息的基本信息
     * @param entity_id 实体id
     * @param subject_id 主题id
     * @param data_id 数据id
     * @return
     */
    @RequestMapping(value = "/baseinfo/{entity_id}/{subject_id}/{data_id}/{object_id}",method = RequestMethod.GET)
    @SystemControllerLog(description = "获取档案信息的基本信息" , operType = LogTypeConstants.SELECT)
    @SearchControllerLog(entityIdIndex = 0,subjectIdIndex = 1,dataIdIndex = 2,keyIndex = 3,type = SearchType.ARCHIVES_SEARCH)
    public String baseinfo(@PathVariable("entity_id")String entity_id,@PathVariable("subject_id")String subject_id,@PathVariable("data_id")String data_id,@PathVariable("object_id")String object_id){
        Map<String, Object> baseinfo = archivesSearchService.baseinfo(entity_id, subject_id, data_id);
        return ReturnDataUtil.getData(baseinfo);
    }


    /**
     * 查看当前用户是否有收藏此数据
     * @param entity_id
     * @param subject_id
     * @param data_id
     * @return
     */
    @RequestMapping(value = "/attention/{entity_id}/{subject_id}/{data_id}",method = RequestMethod.GET)
    @SystemControllerLog(description = "查看当前用户是否有收藏此数据" , operType = LogTypeConstants.SELECT)
    public String getUserIsAttention(@PathVariable("entity_id")String entity_id,@PathVariable("subject_id")String subject_id,@PathVariable("data_id")String data_id){
        String userID = UserVOUtil.getUserID();
        Integer countNum = zsUserAttentionService.getUserIsAttention(userID, entity_id, subject_id, data_id);
        return ReturnDataUtil.getData(countNum);
    }
    /**
     * 收藏当前数据
     * @param entity_id
     * @param subject_id
     * @param data_id
     * @return
     */
    @RequestMapping(value = "/attention/{entity_id}/{subject_id}/{data_id}/{object_id}",method = RequestMethod.POST)
    @SystemControllerLog(description = "收藏当前数据" , operType = LogTypeConstants.SELECT)
    public String userAttention(@PathVariable("entity_id")String entity_id,@PathVariable("subject_id")String subject_id,@PathVariable("data_id")String data_id,@PathVariable("object_id")String object_id){
        String userID = UserVOUtil.getUserID();
        Integer countNum = zsUserAttentionService.addAttention(userID,entity_id,subject_id,data_id,object_id);
        return ReturnDataUtil.getData(countNum);
    }

    /**
     * 取消当前收藏数据
     * @param entity_id
     * @param subject_id
     * @param data_id
     * @return
     */
    @RequestMapping(value = "/attention/{entity_id}/{subject_id}/{data_id}",method = RequestMethod.DELETE)
    @SystemControllerLog(description = "取消当前收藏数据" , operType = LogTypeConstants.SELECT)
    public String delAttention(@PathVariable("entity_id")String entity_id,@PathVariable("subject_id")String subject_id,@PathVariable("data_id")String data_id){
        String userID = UserVOUtil.getUserID();
        Integer result = zsUserAttentionService.removeAttention(userID,entity_id,subject_id,data_id);
        return ReturnDataUtil.getData(result);
    }

    /**
     * 查看当前数据对应被浏览次数
     * @param entity_id 实体id
     * @param subject_id 主题id
     * @param data_id 数据id
     * @return
     */
    @RequestMapping(value = "/look-number/{entity_id}/{subject_id}/{data_id}",method = RequestMethod.GET)
    @SystemControllerLog(description = "查看当前数据对应被浏览次数" , operType = LogTypeConstants.SELECT)
    public String lookNumber(@PathVariable("entity_id") String entity_id,@PathVariable("subject_id") String subject_id,@PathVariable("data_id") String data_id){
        //构造查询实体
        ZsUserRecord zsUserRecord = new ZsUserRecord();
        zsUserRecord.setEntity_id(entity_id);
        zsUserRecord.setSubject_id(subject_id);
        zsUserRecord.setDataid(data_id);
        //统计数量
        Long lookNumber = zsUserRecordService.countRecord(zsUserRecord);
        //返回数据
        return ReturnDataUtil.getData(lookNumber);
    }


    /**
     * 获取当前主题所有档案维度
     * @param subject_id 主题id
     * @return
     */
    @RequestMapping(value = "list/{subject_id}",method = RequestMethod.GET)
    @SystemControllerLog(description = "获取当前主题所有档案维度" , operType = LogTypeConstants.SELECT)
    public String listArchives(@PathVariable("subject_id") String subject_id){
        List<ZsArchivesDTO> zsArchivesDTOS = zsArchivesService.listDetailArchives(subject_id);
        return ReturnDataUtil.getData(zsArchivesDTOS);
    }


    /**
     * 获取档案中配置表格信息维度数据
     * @param object_id
     * @param archive_id
     * @return
     */
    @RequestMapping(value = "/table-data/{object_id}/{archive_id}",method = RequestMethod.GET)
    @SystemControllerLog(description = "获取档案中配置表格信息维度数据" , operType = LogTypeConstants.SELECT)
    public String tableData(@PathVariable("object_id") String object_id, @PathVariable("archive_id") String archive_id, Page page){
        SearchResultBO searchResultBO = archivesSearchService.tableData(archive_id, object_id, page);
        return ReturnDataUtil.getData(searchResultBO);
    }


    /**
     * 获取档案中配置表格信息维度数据
     * @param object_id
     * @param archive_id
     * @return
     */
    @RequestMapping(value = "/table-data-detail/{data_id}/{archive_id}",method = RequestMethod.GET)
    @SystemControllerLog(description = "获取档案中配置表格信息维度数据" , operType = LogTypeConstants.SELECT)
    public String tableDataDetail(@PathVariable("data_id") String object_id, @PathVariable("archive_id") String archive_id, Page page){
        Map<String,Object> resultData = archivesSearchService.tableDataDetail(archive_id, object_id);
        return ReturnDataUtil.getData(resultData);
    }
    /**
     * 获取档案中配置记录列表数据
     * @param object_id
     * @param archive_id
     * @return
     */
    @RequestMapping(value = "/recode-data/{object_id}/{archive_id}",method = RequestMethod.GET)
    @SystemControllerLog(description = "获取档案中配置表格信息维度数据" , operType = LogTypeConstants.SELECT)
    public String recodeData(@PathVariable("object_id") String object_id, @PathVariable("archive_id") String archive_id, Page page){
        List<Map<String, String>> resultData = archivesSearchService.recordData(archive_id, object_id);
        return ReturnDataUtil.getData(resultData);
    }


}
