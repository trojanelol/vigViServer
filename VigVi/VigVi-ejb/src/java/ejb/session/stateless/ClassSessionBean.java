/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Merchant;
import entity.GymClass;
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
import util.exception.MerchantNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author JiaYunTeo
 */
@Stateless
public class ClassSessionBean implements ClassSessionBeanLocal {

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
    

    
    @Override
    public GymClass retrieveClassByClassId(Long classId)throws GymClassNotFoundException{
        GymClass gymClassEntity = em.find(GymClass.class, classId);

        try{
            return gymClassEntity;
        }catch (NoResultException | NonUniqueResultException ex){
            throw new GymClassNotFoundException ("Gym Class" + classId + "does not exist");
        }
    }
}
