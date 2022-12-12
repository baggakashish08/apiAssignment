package com.springKT.apiAssignment.service;

import com.springKT.apiAssignment.Dao.AbstractDao;
import com.springKT.apiAssignment.Dao.TransformerInfoDao;
import com.springKT.apiAssignment.entities.TransformerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TransformerInfoService extends AbstractService<TransformerInfo> {

    @Autowired
    TransformerInfoDao transformerInfoDao;

    TransformerInfoService(AbstractDao<TransformerInfo, Integer> abstractDao) {
        super(abstractDao);
    }
    public List<TransformerInfo> addItem( TransformerInfo transformerInfo){
        super.addItemAbstract(transformerInfo);
        return transformerInfoDao.findAll();
    }

    public List<TransformerInfo> getItems(){
        super.getItemsAbstract();
        return transformerInfoDao.findAll();
    }

    public TransformerInfo getItemById(Integer id){
        super.getItemByIdAbstract(id);
        return transformerInfoDao.findById(id).orElse(null);
    }

    public TransformerInfo updateItem(TransformerInfo transformerInfo){
        super.updateItemAbstract(transformerInfo);
        return transformerInfo;
    }

    public void deleteItem(Integer id){
        super.deleteItemAbstract(id);
        transformerInfoDao.deleteById(id);
    }
}
