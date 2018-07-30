/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.dailymail.facades;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lk.gov.health.dailymail.entity.ClientProcess;

/**
 *
 * @author User
 */
@Stateless
public class ClientProcessFacade extends AbstractFacade<ClientProcess> {

    @PersistenceContext(unitName = "pu")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClientProcessFacade() {
        super(ClientProcess.class);
    }
    
}
