package com.fas.search.manage.controller;

import com.fas.base.log.SystemControllerLog;
import com.fas.base.model.LogTypeConstants;
import com.fas.base.util.FasReturn;
import com.fas.base.util.enums.ResultEnum;
import com.fas.search.manage.entity.ZsEntityCategory;
import com.fas.search.manage.service.ZsEntityCategoryService;
import com.fas.search.util.view.ReturnDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("category")
public class ZsEntityCategoryController {

    @Autowired
    private ZsEntityCategoryService zsEntityCategoryService;

    /**
     * 获取实体分类树
     * @return
     */
    @RequestMapping(value = "treeList",method = RequestMethod.GET)
    @SystemControllerLog(description = "实体分类树",operType = LogTypeConstants.SELECT)
    public String treeList(){
        List<Map> categoryTree = zsEntityCategoryService.listCategoryTree();
        return ReturnDataUtil.getData(categoryTree);
    }

    /**
     * 新增实体分类
     * @param category
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @SystemControllerLog(description = "新增实体分类",operType = LogTypeConstants.ADD)
    public String insert(ZsEntityCategory category){
        int result = zsEntityCategoryService.save(category);
        return ReturnDataUtil.saveOrUpdateOrDel(result);
    }

    /**
     * 修改实体分类
     * @param category
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT )
    @SystemControllerLog(description = "修改实体分类",operType = LogTypeConstants.MODIFY)
    public String update(ZsEntityCategory category){
        int result = zsEntityCategoryService.update(category);
        return ReturnDataUtil.saveOrUpdateOrDel(result);
    }

    /**
     * 删除实体分类
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    @SystemControllerLog(description = "删除实体分类",operType = LogTypeConstants.DELETE)
    public String delete(@PathVariable("id") String id){
        int result = zsEntityCategoryService.removeById(id);
        return ReturnDataUtil.saveOrUpdateOrDel(result);
    }
}
