package com.springKT.apiAssignment.DTO;

import com.fasterxml.jackson.databind.JsonNode;

public class AbstractDTO {

    private String name;
    private JsonNode data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JsonNode getData() {
        return data;
    }

    public void setData(JsonNode data) {
        this.data = data;
    }
}
