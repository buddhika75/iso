package lk.gov.health.dailymail.facades;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lk.gov.health.dailymail.entity.ClientProcessStep;

@Stateless
public class ClientProcessStepFacade extends AbstractFacade<ClientProcessStep> {

    @PersistenceContext(unitName = "pu")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClientProcessStepFacade() {
        super(ClientProcessStep.class);
    }
    
}
