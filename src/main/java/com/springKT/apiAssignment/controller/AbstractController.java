package com.springKT.apiAssignment.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.springKT.apiAssignment.DTO.AbstractDTO;
import com.springKT.apiAssignment.service.TestAllService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/generic")
public class AbstractController {

    @Autowired
    TestAllService genericService;

    @GetMapping
    public List<JsonNode> getData(@RequestParam String entityName){
        return genericService.getItems(entityName);
    }

    @GetMapping("/{id}")
    public JsonNode getDataById(@PathVariable String id, @RequestParam(value="entityName") String entityName){
        return genericService.getItemById(Integer.valueOf(id), entityName);
    }

    @PostMapping
    public List<JsonNode> saveData(@RequestBody AbstractDTO dto){
        return genericService.saveEntity(dto);
    }

    @PutMapping
    public JsonNode updateData(@RequestBody AbstractDTO dto){
        return genericService.updateItem(dto);
    }

    @DeleteMapping("/{id}")
    public String deleteEntry(@PathVariable String id, @RequestParam(value="entityName") String entityName){
        return genericService.deleteItem(Integer.valueOf(id), entityName);
    }
}
