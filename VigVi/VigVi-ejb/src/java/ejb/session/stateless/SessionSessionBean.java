/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.CustomerSession;
import entity.GymClass;
import entity.Session;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.ClassIDExistException;
import util.exception.GymClassNotFoundException;
import util.exception.SessionNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author JiaYunTeo
 */
@Stateless
public class SessionSessionBean implements SessionSessionBeanLocal {

    @EJB(name = "ClassSessionBeanLocal")
    private ClassSessionBeanLocal classSessionBeanLocal;

    @PersistenceContext(unitName="VigVi-ejbPU")
    private EntityManager em;
    
    
    
    @Override
    public Long createNewSession(Long classId, Session newSession) throws ClassIDExistException , UnknownPersistenceException, GymClassNotFoundException{
        try{
        //check whether same date?    
        GymClass gymClassEntity = classSessionBeanLocal.retrieveClassByClassId(classId);
        newSession.setGymClass(gymClassEntity);
        gymClassEntity.getSessions().add(newSession);
        newSession.setAvailableSlot(gymClassEntity.getClassSize());
        em.persist(newSession);
        em.flush();
        return newSession.getSessionId();
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
    
     @Override
    public Session retrieveSessionBySessionId(Long sessionId)throws SessionNotFoundException{
        Session sessionEntity = em.find(Session.class, sessionId);

        try{
            return sessionEntity;
        }catch (NoResultException | NonUniqueResultException ex){
            throw new SessionNotFoundException ("Session" + sessionId + "does not exist");
        }
    }
    
    @Override
    public List<Session> retrieveAllOngoingSessionsByClassId(Long classId){
        Query query = em.createQuery("SELECT s from Session s where s.gymClass.classId = :id and s.sessionStatus = :status");
        query.setParameter("id", classId);
        query.setParameter("status", Session.SessionStatus.ONGOING);
        return query.getResultList();
    }
    
    @Override
    public Session updateSessionAvailableSlot (Long sessionId, Integer newSlotSize) throws SessionNotFoundException{
        Session sessionEntity = retrieveSessionBySessionId(sessionId);
        
        sessionEntity.setAvailableSlot(newSlotSize);
    
        return sessionEntity;

    }
    
    
    @Override
    public Session endSession (Long sessionId) throws SessionNotFoundException{
         Session sessionEntity = retrieveSessionBySessionId(sessionId);
         
         sessionEntity.setSessionStatus(Session.SessionStatus.COMPLETED);
         
         classSessionBeanLocal.deactivateClass(sessionEntity.getGymClass().getClassId());
         
         return sessionEntity;
    }
}
