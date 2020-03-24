/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Merchant;
import entity.GymClass;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.exception.ClassIDExistException;
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
}
