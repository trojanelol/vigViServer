package ejb.session.stateless;

import entity.Admin;
import java.util.List;
import javax.ejb.Local;
import util.exception.AdminNotFoundException;
import util.exception.AdminUsernameExistException;
import util.exception.DeleteAdminException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateAdminException;

@Local
public interface AdminSessionBeanLocal {
    public Long createNewAdmin(Admin newAdmin) throws AdminUsernameExistException, UnknownPersistenceException, InputDataValidationException;
    
    public List<Admin> retrieveAllAdmins();
    
    public Admin retrieveAdminByAdminId(Long adminId) throws AdminNotFoundException;
    
    public Admin retrieveAdminByUsername(String username) throws AdminNotFoundException;
    
    public Admin adminLogin(String username, String password) throws InvalidLoginCredentialException;
    
    public void updateAdmin(Admin admin) throws AdminNotFoundException, UpdateAdminException, InputDataValidationException;
    
    public void deleteAdmin(Long adminId) throws AdminNotFoundException, DeleteAdminException;
}
