package com.springKT.apiAssignment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springKT.apiAssignment.DTO.AbstractDTO;
import com.springKT.apiAssignment.Dao.AbstractDao;
import com.springKT.apiAssignment.controller.QueryInfoController;
import com.springKT.apiAssignment.controller.TransformerInfoController;
import com.springKT.apiAssignment.entities.CommonDataModel;
import com.springKT.apiAssignment.entities.QueryInfo;
import com.springKT.apiAssignment.entities.TransformerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestAllService extends AbstractService<CommonDataModel>{

    @Autowired
    AbstractDao<CommonDataModel,Integer> dao;
    @Autowired
    ApplicationContext context;

    @Autowired
    private QueryInfoController queryInfoController;

    @Autowired
    private TransformerInfoController transformerInfoController;
    private QueryInfo queryInfo;
    private TransformerInfo transformerInfo;

    private final ObjectMapper objectMapper = new ObjectMapper();

    TestAllService(AbstractDao<CommonDataModel, Integer> abstractDao) {
        super(abstractDao);
    }

    public List<JsonNode> getItems(String entityName){
        if(entityName.equalsIgnoreCase("Query")){
            return queryInfoController.getData().stream().map(obj ->
                    objectMapper.convertValue(obj, JsonNode.class)
            ).collect(Collectors.toList());
        }
        else{
            return transformerInfoController.getData().stream().map(obj ->
                    objectMapper.convertValue(obj, JsonNode.class)
            ).collect(Collectors.toList());
        }

    }
    public JsonNode getItemById(Integer id, String entityName){
        if(entityName.equalsIgnoreCase("Query"))
            return objectMapper.convertValue(queryInfoController.getDataById(id), JsonNode.class);
        else
            return objectMapper.convertValue(transformerInfoController.getDataById(id), JsonNode.class);
    }
    public List<JsonNode> saveEntity(AbstractDTO dto){
        String entityName = dto.getName();
        JsonNode attributes = dto.getData();

        if(entityName.equalsIgnoreCase("Query")){
            queryInfo = objectMapper.convertValue(attributes, QueryInfo.class);
            return queryInfoController.addData(queryInfo).stream().map(obj ->
                    objectMapper.convertValue(obj, JsonNode.class)).collect(Collectors.toList());
        }
        else {
            transformerInfo = objectMapper.convertValue(attributes, TransformerInfo.class);
            return transformerInfoController.addData(transformerInfo).stream().map(obj ->
                    objectMapper.convertValue(obj, JsonNode.class)).collect(Collectors.toList());
        }

    }
    public JsonNode updateItem(AbstractDTO dto){
        String entityName = dto.getName();
        JsonNode attributes = dto.getData();

        if(entityName.equalsIgnoreCase("Query")){
            queryInfo = objectMapper.convertValue(attributes, QueryInfo.class);
            return objectMapper.convertValue(queryInfoController.updateData(queryInfo), JsonNode.class);
        }
        else {
            transformerInfo = objectMapper.convertValue(attributes, TransformerInfo.class);
            return objectMapper.convertValue(transformerInfoController.updateData(transformerInfo), JsonNode.class);
        }
    }

    public String deleteItem(Integer id, String entityName){
        if(entityName.equalsIgnoreCase("Query")){
            queryInfoController.deleteEntry(id);
        }
        else{
            transformerInfoController.deleteEntry(id);
        }
        return "Entry deleted";
    }

}
