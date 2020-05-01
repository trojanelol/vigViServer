package ejb.session.stateless;

import entity.Admin;
import java.util.List;
import java.util.Set;
import javax.ejb.Local;
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
import util.exception.AdminNotFoundException;
import util.exception.AdminUsernameExistException;
import util.exception.DeleteAdminException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateAdminException;
import util.security.CryptographicHelper;

@Stateless
@Local(AdminSessionBeanLocal.class)
public class AdminSessionBean implements AdminSessionBeanLocal {
    
    @PersistenceContext(unitName="VigVi-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public AdminSessionBean()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public Long createNewAdmin(Admin newAdmin) throws AdminUsernameExistException, UnknownPersistenceException, InputDataValidationException
    {
        try
        {
            Set<ConstraintViolation<Admin>>constraintViolations = validator.validate(newAdmin);
        
            if(constraintViolations.isEmpty())
            {
                em.persist(newAdmin);
                em.flush();

                return newAdmin.getAdminId();
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
                    throw new AdminUsernameExistException();
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
    public List<Admin> retrieveAllAdmins()
    {
        Query query = em.createQuery("SELECT a FROM Admin a");
        
        return query.getResultList();
    }

    @Override
    public Admin retrieveAdminByAdminId(Long adminId) throws AdminNotFoundException
    {
        Admin admin = em.find(Admin.class, adminId);
        
        if(admin != null)
        {
            return admin;
        }
        else
        {
            throw new AdminNotFoundException("Admin ID " + adminId + " does not exist!");
        }
    }

    @Override
    public Admin retrieveAdminByUsername(String username) throws AdminNotFoundException
    {
        Query query = em.createQuery("SELECT a FROM Admin a WHERE a.username = :inUsername");
        query.setParameter("inUsername", username);
        
        try
        {
            return (Admin)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new AdminNotFoundException("Admin Username " + username + " does not exist!");
        }
    }
    
    @Override
    public Admin adminLogin(String username, String password) throws InvalidLoginCredentialException
    {
        try
        {
            Admin admin = retrieveAdminByUsername(username);            
            String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + admin.getSalt()));
            
            if(admin.getPassword().equals(passwordHash))
            {           
                return admin;
            }
            else
            {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }
        }
        catch(AdminNotFoundException ex)
        {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }
    
    @Override
    public void updateAdmin(Admin admin) throws AdminNotFoundException, UpdateAdminException, InputDataValidationException
    {
        if(admin != null && admin.getAdminId() != null)
        {
            Set<ConstraintViolation<Admin>>constraintViolations = validator.validate(admin);
        
            if(constraintViolations.isEmpty())
            {
                Admin adminToUpdate = retrieveAdminByAdminId(admin.getAdminId());

                if(adminToUpdate.getUsername().equals(admin.getUsername()))
                {
                    adminToUpdate.setFirstName(admin.getFirstName());
                    adminToUpdate.setLastName(admin.getLastName());               
                }
                else
                {
                    throw new UpdateAdminException("Username of admin record to be updated does not match the existing record");
                }
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new AdminNotFoundException("Admin ID not provided for admin to be updated");
        }
    }

    @Override
    public void deleteAdmin(Long adminId) throws AdminNotFoundException, DeleteAdminException
    {
        Admin adminToRemove = retrieveAdminByAdminId(adminId);
        
        if(adminToRemove != null)
        {
            em.remove(adminToRemove);
        }
        else
        {
            throw new DeleteAdminException("Admin ID " + adminId + " cannot be deleted!");
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Admin>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }

}
