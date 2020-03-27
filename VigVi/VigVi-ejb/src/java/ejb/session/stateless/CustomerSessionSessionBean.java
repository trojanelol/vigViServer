/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.CustomerSession;
import entity.CustomerSessionId;
import entity.Session;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.exception.ClassIDExistException;
import util.exception.CustomerNotFoundException;
import util.exception.SessionNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author JiaYunTeo
 */
@Stateless
public class CustomerSessionSessionBean implements CustomerSessionSessionBeanLocal {

    @EJB(name = "SessionSessionBeanLocal")
    private SessionSessionBeanLocal sessionSessionBeanLocal;

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @PersistenceContext(unitName="VigVi-ejbPU")
    private EntityManager em;
    
    
    
    @Override
    public Long signUpClass(Long customerId, Long sessionId) throws ClassIDExistException , UnknownPersistenceException, CustomerNotFoundException, SessionNotFoundException{
        try{
        Customer customerEntity = customerSessionBeanLocal.retrieveCustomerByCustomerId(customerId);
        Session sessionEntity = sessionSessionBeanLocal.retrieveSessionBySessionId(sessionId);
        CustomerSessionId customerSessionId = new CustomerSessionId(customerId, sessionId);
        CustomerSession customerSession = new CustomerSession(customerSessionId);
        customerSession.setCustomerSessionStatus(CustomerSession.CustomerSessionStatus.ACTIVE);
        customerSession.setCustomer(customerEntity);
        customerSession.setSession(sessionEntity);
        customerEntity.getSignedUpClass().add(customerSession);
        sessionEntity.getSignedUpCustomer().add(customerSession);
        em.persist(customerSession);
        em.flush();
        return customerSession.getSession().getSessionId();
        } catch(PersistenceException ex){
            if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
            {
                if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                {
                    throw new ClassIDExistException();
                }
                else
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
            else
            {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }
}
