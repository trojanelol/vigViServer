/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.GymClass;
import entity.Merchant;
import java.util.List;
import javax.ejb.Local;
import util.exception.ClassIDExistException;
import util.exception.GymClassNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.MerchantNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author JiaYunTeo
 */
@Local
public interface ClassSessionBeanLocal {

    public Long createNewClass(Long merchantId, GymClass newClass) throws ClassIDExistException, UnknownPersistenceException, MerchantNotFoundException;

    public GymClass retrieveClassByClassId(Long classId) throws GymClassNotFoundException;

    public List<GymClass> retrieveAllActiveClasses();

    public Merchant retrieveMerchantByClassId(Long classId) throws GymClassNotFoundException;

    public Long deactivateClass(Long classId);

    public void updateClass(GymClass gymClass) throws GymClassNotFoundException;
    
}
