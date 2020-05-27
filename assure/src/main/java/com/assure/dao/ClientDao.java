package com.assure.dao;

import com.assure.pojo.Client;
import com.commons.enums.ClientType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ClientDao extends AbstractDao<Client> {

    private static final String SELECT_BY_NAME = "Select c From Client c where c.name=:name";
    private static final String SELECT_ALL_CUSTOMER = "Select c From Client c where c.type=:type";
    private static final String SELECT_ALL_CLIENT = "Select c From Client c where c.type=:type";

    public ClientDao() {
        super(Client.class);
    }

    @Transactional(readOnly = true)
    public Client getClientByName(String clientName) {
        TypedQuery<Client> query  = getQuery(SELECT_BY_NAME);
        query.setParameter("name", clientName);
        return getSingle(query);
    }

    @Transactional(readOnly = true)
    public List<Client> getAllCustomerDetails() {
        TypedQuery<Client> query  = getQuery(SELECT_ALL_CUSTOMER);
        query.setParameter("type", ClientType.CUSTOMER);
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    public List<Client> getAllClientDetails() {
        TypedQuery<Client> query  = getQuery(SELECT_ALL_CLIENT);
        query.setParameter("type", ClientType.CLIENT);
        return query.getResultList();
    }
}
