/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Merchant;
import entity.GymClass;
import entity.Session;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import util.exception.ClassIDExistException;
import util.exception.GymClassNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.MerchantNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author JiaYunTeo
 */
@Stateless
public class ClassSessionBean implements ClassSessionBeanLocal {

    @EJB(name = "SessionSessionBeanLocal")
    private SessionSessionBeanLocal sessionSessionBeanLocal;

    @PersistenceContext(unitName="VigVi-ejbPU")
    private EntityManager em;
    @EJB
    private MerchantSessionBeanLocal merchantSessionBeanLocal;
    
    
    
    @Override
    public Long createNewClass(Long merchantId, GymClass newClass) throws ClassIDExistException , UnknownPersistenceException, MerchantNotFoundException{
        try{
        Merchant merchantEntity = merchantSessionBeanLocal.retrieveMerchantByMerchantId(merchantId);
        newClass.setMerchant(merchantEntity);
        merchantEntity.getClasses().add(newClass);
        em.persist(newClass);
        em.flush();
        newClass.setClassStatus(true);
        return newClass.getClassId();
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
    
    public List<GymClass> retrieveAllActiveClasses() {
            //retrieveActiveClasses
        Query query = em.createQuery("SELECT c from GymClass c where c.classStatus = :status");
        query.setParameter("status", true);
        return query.getResultList();

    }
    
    public List<GymClass> retrieveAllClasses() {
            //retrieveActiveClasses
        Query query = em.createQuery("SELECT c from GymClass c");
        
        return query.getResultList();

    }
    

    
    @Override
    public GymClass retrieveClassByClassId(Long classId)throws GymClassNotFoundException{
        GymClass gymClassEntity = em.find(GymClass.class, classId);

        try{
            return gymClassEntity;
        }catch (NoResultException | NonUniqueResultException ex){
            throw new GymClassNotFoundException ("Gym Class" + classId + "does not exist");
        }
    }
    
    @Override
    public Merchant retrieveMerchantByClassId(Long classId)throws GymClassNotFoundException{
        GymClass gymClassEntity = em.find(GymClass.class, classId);

        try{
            return gymClassEntity.getMerchant();
        }catch (NoResultException | NonUniqueResultException ex){
            throw new GymClassNotFoundException ("Gym Class" + classId + "does not exist");
        }
    }
    
//    only auto-trigger when there is no ongoing session
    public Long deactivateClass (Long classId){
        
        GymClass gymClassEntity = em.find(GymClass.class, classId);
        
        List<Session>  result = sessionSessionBeanLocal.retrieveAllOngoingSessionsByClassId(classId);
        
        if (result==null || result.isEmpty()){
            gymClassEntity.setClassStatus(false);
        }    
        
        return gymClassEntity.getClassId();
    }

    public void persist(Object object) {
        em.persist(object);
    }
    
    @Override
    public void updateClass(GymClass gymClass) throws GymClassNotFoundException
    {
        if(gymClass != null && gymClass.getClassId()!= null)
        {
//            Set<ConstraintViolation<GymClass>>constraintViolations = validator.validate(gymClass);
        
//            if(constraintViolations.isEmpty())
//            {
                GymClass gymClassEntityToUpdate = this.retrieveClassByClassId(gymClass.getClassId());
                    
                    gymClassEntityToUpdate.setClassName(gymClass.getClassName());
                    gymClassEntityToUpdate.setClassDesc(gymClass.getClassDesc());
                    gymClassEntityToUpdate.setClassInstructor(gymClass.getClassInstructor());
                    gymClassEntityToUpdate.setClassName(gymClass.getClassName());
                    gymClassEntityToUpdate.setClassPrice(gymClass.getClassPrice());
                    gymClassEntityToUpdate.setClassRemarks(gymClass.getClassRemarks());
                    gymClassEntityToUpdate.setClassSize(gymClass.getClassSize());
                    gymClassEntityToUpdate.setEndTime(gymClass.getEndTime());
                    gymClassEntityToUpdate.setStartTime(gymClass.getStartTime());
//            }
//            else
//            {
//                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
//            }
        }
        else
        {
            throw new GymClassNotFoundException("Merchant ID not provided for product to be updated");
        }
    }
    
}
