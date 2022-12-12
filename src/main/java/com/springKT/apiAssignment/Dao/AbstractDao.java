package com.springKT.apiAssignment.Dao;

import com.springKT.apiAssignment.entities.CommonDataModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

@Repository
public interface AbstractDao<T extends CommonDataModel,E> extends JpaRepository<T, E> {

}
