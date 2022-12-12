package com.springKT.apiAssignment.controller;

import com.springKT.apiAssignment.entities.QueryInfo;
import com.springKT.apiAssignment.service.QueryInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class QueryInfoController {

    @Autowired
    public QueryInfoService queryInfoService;

    public List<QueryInfo> getData(){
        return queryInfoService.getItems();
    }

    public QueryInfo getDataById(Integer id){
        return queryInfoService.getItemById(id);
    }

    public List<QueryInfo> addData(QueryInfo queryInfo){
         return queryInfoService.addItem(queryInfo);
    }

    public QueryInfo updateData(QueryInfo queryInfo){
        return queryInfoService.updateItem(queryInfo);
    }

    public void deleteEntry(Integer id){
        queryInfoService.deleteItem(id);
    }
}
