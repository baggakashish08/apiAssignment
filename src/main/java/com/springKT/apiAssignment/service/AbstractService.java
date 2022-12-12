package com.springKT.apiAssignment.service;

import com.springKT.apiAssignment.Dao.AbstractDao;
import com.springKT.apiAssignment.entities.CommonDataModel;

import java.util.List;

public class AbstractService<T extends CommonDataModel> {

//    @Autowired
    private AbstractDao<T, Integer> abstractDao;

    AbstractService(AbstractDao<T, Integer> abstractDao){
        this.abstractDao = abstractDao;
    }

    public void getItemsAbstract(){
    }

    public void getItemByIdAbstract(Integer id){
    }

    public void addItemAbstract(T object){
        abstractDao.save(object);
    }

    public void updateItemAbstract(T object){
        abstractDao.save(object);
    }

    public void deleteItemAbstract(Integer id){

    }
}
