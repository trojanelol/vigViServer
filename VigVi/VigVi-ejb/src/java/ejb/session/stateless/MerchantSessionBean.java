/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Currency;
import entity.Merchant;
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
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CurrencyNotFoundException;
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

   @EJB(name = "CurrencySessionBeanLocal")
    private CurrencySessionBeanLocal currencySessionBeanLocal;

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
        Query query = em.createQuery("SELECT m FROM Merchant m WHERE m.merchantEmail = :inMerchantName");
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
    public Merchant merchantLogin(String username, String password) throws InvalidLoginCredentialException
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
    public Long createNewMerchant(Long currencyId, Merchant newMerchant) throws InputDataValidationException, MerchantUsernameExistException, UnknownPersistenceException, CurrencyNotFoundException{
        
        try
        {
            Set<ConstraintViolation<Merchant>>constraintViolations = validator.validate(newMerchant);
        
            if(constraintViolations.isEmpty())
            {
                Currency currencyEntity = currencySessionBeanLocal.retrieveCurrencyByCurrencyId(currencyId);
                newMerchant.setCurrency(currencyEntity);
                currencyEntity.getMerchants().add(newMerchant);
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
    
   @Override
    public void updateMerchant(Merchant merchant) throws InputDataValidationException, MerchantNotFoundException
    {
        if(merchant != null && merchant.getMerchantId()!= null)
        {
            Set<ConstraintViolation<Merchant>>constraintViolations = validator.validate(merchant);
        
            if(constraintViolations.isEmpty())
            {
                Merchant merchantEntityToUpdate = this.retrieveMerchantByMerchantId(merchant.getMerchantId());
                    
                    merchantEntityToUpdate.setMerchantName(merchant.getMerchantName());
                    merchantEntityToUpdate.setMerchantDesc(merchant.getMerchantDesc());
                    merchantEntityToUpdate.setBankName(merchant.getBankName());
                    merchantEntityToUpdate.setBankAccount(merchant.getBankAccount());
                    merchantEntityToUpdate.setCommissionRate(merchant.getCommissionRate());
                    merchantEntityToUpdate.setMerchantAddress(merchant.getMerchantAddress());
                    merchantEntityToUpdate.setMerchantContactNo(merchant.getMerchantContactNo());
                    merchantEntityToUpdate.setMerchantEmail(merchant.getMerchantEmail());
                    merchantEntityToUpdate.setMerchantPw(merchant.getMerchantPw());
                    merchantEntityToUpdate.setMerchantStatus(merchant.getMerchantStatus());
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new MerchantNotFoundException("Merchant ID not provided for product to be updated");
        }
    }
    
}
