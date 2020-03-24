/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.GymClass;
import javax.ejb.Local;
import util.exception.ClassIDExistException;
import util.exception.MerchantNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author JiaYunTeo
 */
@Local
public interface ClassSessionBeanLocal {

    public Long createNewClass(Long merchantId, GymClass newClass) throws ClassIDExistException, UnknownPersistenceException, MerchantNotFoundException;
    
}
