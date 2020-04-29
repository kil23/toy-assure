package com.assure.dao;

import com.assure.pojo.Client;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class ClientDao extends AbstractDao{

    private static final Logger logger = Logger.getLogger(ClientDao.class);

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Client insertClient(Client c){
        logger.info("inserting-new-client");
        em.persist(c);
        em.flush();
        return c;
    }

    @Transactional
    public void updateClient(Client c){
        logger.info("updating-client-with-id("+c.getId()+")");
        em.merge(c);
        em.flush();
    }

    public int deleteClient(int id) {
        logger.info("deleting-client-with-id("+id+")");
        String delete_id = "delete c from Client c where id=:id";
        Query query = em.createQuery(delete_id);
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    public Client getClientById(long id){
        logger.info("getting-client-by-id("+id+")");
        String select_id = "Select c From Client c where id=:id";
        TypedQuery<Client> query = getQuery(select_id, Client.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public List<Client> getAllClients() {
        logger.info("getting-All-clients");
        String select_all = "select c from Client c";
        TypedQuery<Client> query = getQuery(select_all, Client.class);
        return query.getResultList();
    }

    public Client getClientByName(String clientName) {
        logger.info("getting-client-by-client-name("+clientName+")");
        String select_id = "Select c From Client c where name='"+clientName+"'";
        TypedQuery<Client> query = getQuery(select_id, Client.class);
        return getSingle(query);
    }
}
