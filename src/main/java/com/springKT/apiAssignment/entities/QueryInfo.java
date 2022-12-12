package com.springKT.apiAssignment.entities;


import jakarta.persistence.Entity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Entity
public class QueryInfo extends CommonDataModel {

    private String owner;
    private String queryName;
    private String query;
    private String queryType;

    public QueryInfo() {
        super();
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    public String getQuery() {
        return query;
    }


    public void setQuery(String query) {
        this.query = query;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public QueryInfo( String owner, String queryName, String query, String queryType) {
        this.owner = owner;
        this.queryName = queryName;
        this.query = query;
        this.queryType = queryType;
    }

    @Override
    public String toString() {
        return "queryInfo{" +
                " owner='" + owner + '\'' +
                ", queryName='" + queryName + '\'' +
                ", query='" + query + '\'' +
                ", queryType='" + queryType + '\'' +
                '}';
    }
}
