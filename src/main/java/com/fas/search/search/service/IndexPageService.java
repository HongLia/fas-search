package com.fas.search.search.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

/**
 * 智能搜索
 */
public interface IndexPageService {

    /**
     * 解析智搜首页上传的需要搜索的excel文件
     * @param file
     * @return
     */
    List<Set<String>> analysisSearchExcel(MultipartFile file);
}
