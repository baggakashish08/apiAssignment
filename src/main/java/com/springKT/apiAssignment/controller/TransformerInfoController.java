package com.springKT.apiAssignment.controller;

import com.springKT.apiAssignment.entities.TransformerInfo;
import com.springKT.apiAssignment.service.TransformerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TransformerInfoController {

    @Autowired
    public TransformerInfoService transformerInfoService;

    public List<TransformerInfo> getData(){
        return transformerInfoService.getItems();
    }

    public TransformerInfo getDataById(Integer id){
        return transformerInfoService.getItemById(id);
    }

    public List<TransformerInfo> addData(TransformerInfo transformerInfo){
        return transformerInfoService.addItem(transformerInfo);
    }

    public TransformerInfo updateData(TransformerInfo transformerInfo){
        return transformerInfoService.updateItem(transformerInfo);
    }

    public void deleteEntry(Integer id){
        transformerInfoService.deleteItem(id);
    }
}
