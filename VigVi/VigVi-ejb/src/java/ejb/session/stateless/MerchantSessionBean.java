/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Merchant;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.MerchantNotFoundException;
import util.exception.MerchantUsernameExistException;
import util.exception.UnknownPersistenceException;
import util.security.CryptographicHelper;

/**
 *
 * @author JiaYunTeo
 */
@Stateless


public class MerchantSessionBean implements MerchantSessionBeanLocal {

    @PersistenceContext(unitName="VigVi-ejbPU")
    private EntityManager em;
    
     private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public MerchantSessionBean()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
     @Override
    public Merchant retrieveMerchantByEmail(String email) throws MerchantNotFoundException
    {
        Query query = em.createQuery("SELECT m FROM Merchant m WHERE m.merchantName = :inMerchantName");
        query.setParameter("inMerchantName", email);
        
        try
        {
            return (Merchant)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new MerchantNotFoundException("Merchant email " + email + " does not exist!");
        }
    }
    
    @Override
    public Merchant MerchantLogin(String username, String password) throws InvalidLoginCredentialException
    {
        try
        {
            Merchant merchant = retrieveMerchantByEmail(username);            
            String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + merchant.getSalt()));
            
            if(merchant.getMerchantPw().equals(passwordHash))
            {             
                return merchant;
            }
            else
            {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }
        }
        catch(MerchantNotFoundException ex)
        {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Merchant>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
    
    @Override
    public Long createNewMerchant(Merchant newMerchant) throws InputDataValidationException, MerchantUsernameExistException, UnknownPersistenceException{
        
        try
        {
            Set<ConstraintViolation<Merchant>>constraintViolations = validator.validate(newMerchant);
        
            if(constraintViolations.isEmpty())
            {
                em.persist(newMerchant);
                em.flush();
        
                return newMerchant.getMerchantId();
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }            
        }
        catch(PersistenceException ex)
        {
            if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
            {
                if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                {
                    throw new MerchantUsernameExistException();
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
    
    /**
     *
     * @return
     */
    @Override
    public List<Merchant> retrieveAllMerchants() {

        Query query = em.createQuery("SELECT m from Merchant m");

        return query.getResultList();

    }
    
    @Override
    public Merchant retrieveMerchantByMerchantId(Long merchantId)throws MerchantNotFoundException{
        Merchant merchantEntity = em.find(Merchant.class, merchantId);

        try{
            return merchantEntity;
        }catch (NoResultException | NonUniqueResultException ex){
            throw new MerchantNotFoundException ("Merchant" + merchantId + "does not exist");
        }
    }
    
    @Override
    public Long approveMerchant(Long merchantId){
        Merchant merchantEntity = em.find(Merchant.class, merchantId);
        
        merchantEntity.setMerchantStatus(Boolean.TRUE);
        
        return merchantEntity.getMerchantId();
    }
    
    @Override
    public Long deactivateMerchant(Long merchantId){
        Merchant merchantEntity = em.find(Merchant.class, merchantId);
        
        merchantEntity.setMerchantStatus(Boolean.FALSE);
        
        return merchantEntity.getMerchantId();
    }
    
    
}
