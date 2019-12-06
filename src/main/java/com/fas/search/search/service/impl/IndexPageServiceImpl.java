package com.fas.search.search.service.impl;

import com.fas.search.search.exception.UploadFileException;
import com.fas.search.search.service.IndexPageService;
import com.fas.search.util.excel.ExcelDataWrite;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @auther wuzy
 * @description 智能搜搜首页service实现类
 * @date 2019/8/15
 */
@Service
@Transactional
public class IndexPageServiceImpl implements IndexPageService {


    @Override
    public List<Set<String>> analysisSearchExcel(MultipartFile file) {
        List<Set<String>> sets = null;
        try {
            long size = file.getSize();
            if (size / 1024.00 / 1024.00 > 2){
                throw new UploadFileException("上传文件过大，请减小文件体积以后再试！");
            }
            InputStream inputStream = file.getInputStream();
            Workbook workbook = null;
            if (file.getOriginalFilename().toLowerCase().endsWith("xlsx")){
                //xlsx类型
                workbook = new XSSFWorkbook(inputStream);
            }else if (file.getOriginalFilename().toLowerCase().endsWith("xls")){
                //xls类型
                workbook = new HSSFWorkbook(inputStream);
            }else{
                //文件不合法
                throw new UploadFileException("上传文件类型不合法，请确认后再试！");
            }
            sets = ExcelDataWrite.analysisUploadExcelData(workbook);

        } catch (IOException e) {
            e.printStackTrace();
            throw new UploadFileException("上传文件类型不合法，请确认后再试！");
        }
        return sets ;
    }
}
