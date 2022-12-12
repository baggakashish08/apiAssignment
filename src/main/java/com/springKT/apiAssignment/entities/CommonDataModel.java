package com.springKT.apiAssignment.entities;

import jakarta.persistence.*;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

@MappedSuperclass
public class CommonDataModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
