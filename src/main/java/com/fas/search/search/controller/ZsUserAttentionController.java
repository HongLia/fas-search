package com.fas.search.search.controller;

import com.fas.base.log.SystemControllerLog;
import com.fas.base.model.LogTypeConstants;
import com.fas.base.model.Page;
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
 * @description 用户收藏Controller
 * @date 2019/11/26
 */
@RestController
@RequestMapping("attention")
public class ZsUserAttentionController {

    @Autowired
    private ZsUserAttentionService zsUserAttentionService;

    /**
     * 当前用户关注数据统计
     * @return
     */
    @RequestMapping(value = "my-num",method = RequestMethod.GET)
    @SystemControllerLog(description = "当前用户关注数据统计" , operType = LogTypeConstants.SELECT)
    public String myNum(){
        List<Map<String, Object>> maps = zsUserAttentionService.userAttentionNum(UserVOUtil.getUserID());
        return ReturnDataUtil.getData(maps);
    }


    /**
     * 查询用户收藏此数据数据量
     * @param subject_id 主题id
     * @param entity_id 实体id
     * @param data_id 数据id
     * @return
     */
    @RequestMapping("/attention-num/{subject_id}/{entity_id}/{data_id}")
    @SystemControllerLog(description = "查询收藏此数据数据量" , operType = LogTypeConstants.SELECT)
    public String attentionNum(@PathVariable("subject_id") String subject_id, @PathVariable("entity_id") String entity_id,@PathVariable("data_id") String data_id){
        Integer num = zsUserAttentionService.attentionNum(subject_id, entity_id, data_id);
        return ReturnDataUtil.getData(num);
    }

    /**
     * 获取用户关注数据，概览信息
     * @param page
     * @param subject_id
     * @return
     */
    @RequestMapping(value = "/",method = RequestMethod.GET)
    @SystemControllerLog(description = "获取用户关注数据，概览信息" , operType = LogTypeConstants.SELECT)
    public String queryAttentionData(Page page,String subject_id){
        List<Map<String, Object>> maps = zsUserAttentionService.queryAttentionData(page, subject_id);
        return ReturnDataUtil.getData(maps);
    }


}
