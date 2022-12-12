package com.springKT.apiAssignment.Dao;

import com.springKT.apiAssignment.entities.QueryInfo;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QueryInfoDao extends JpaRepository<QueryInfo, Integer> {

}
