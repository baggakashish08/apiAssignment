package com.springKT.apiAssignment.service;

import com.springKT.apiAssignment.Dao.AbstractDao;
import com.springKT.apiAssignment.Dao.QueryInfoDao;
import com.springKT.apiAssignment.entities.QueryInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class QueryInfoService extends AbstractService<QueryInfo> {

    @Autowired
    QueryInfoDao queryInfoDao;

    QueryInfoService(AbstractDao<QueryInfo, Integer> abstractDao) {
        super(abstractDao);
    }

    public List<QueryInfo> addItem( QueryInfo queryInfo){
        super.addItemAbstract(queryInfo);
        return queryInfoDao.findAll();
    }

    public List<QueryInfo> getItems(){
        super.getItemsAbstract();
        return queryInfoDao.findAll();
    }

    public QueryInfo getItemById(Integer id){
        super.getItemByIdAbstract(id);
        return queryInfoDao.findById(id).orElse(null);
    }

    public QueryInfo updateItem(QueryInfo queryInfo){
        super.updateItemAbstract(queryInfo);
        return queryInfo;
    }

    public void deleteItem(Integer id){
        super.deleteItemAbstract(id);
        queryInfoDao.deleteById(id);
    }
}
