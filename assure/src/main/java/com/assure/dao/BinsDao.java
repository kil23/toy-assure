package com.assure.dao;

import com.assure.pojo.Bin;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BinsDao extends AbstractDao<Bin> {

    public BinsDao() {
        super(Bin.class);
    }

    public List<Bin> getAllBins(){
        return findAll();
    }
}
