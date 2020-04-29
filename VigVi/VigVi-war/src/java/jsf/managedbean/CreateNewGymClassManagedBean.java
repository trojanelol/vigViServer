/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.ClassSessionBeanLocal;
import ejb.session.stateless.CurrencySessionBeanLocal;
import ejb.session.stateless.MerchantSessionBeanLocal;
import entity.Currency;
import entity.GymClass;
import entity.Merchant;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.exception.ClassIDExistException;
import util.exception.CurrencyNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.MerchantNotFoundException;
import util.exception.MerchantUsernameExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author JiaYunTeo
 */
@Named(value = "createNewGymClassManagedBean")
@RequestScoped
public class CreateNewGymClassManagedBean {

    @EJB(name = "ClassSessionBeanLocal")
    private ClassSessionBeanLocal classSessionBeanLocal;


    
    
    //model
    private GymClass newGymClass;   
    private Long merchantId;
    /**
     * Creates a new instance of CreateNewMerchantManagedBean
     */
    public CreateNewGymClassManagedBean() {
        
        newGymClass = new GymClass();
        
    }
    
    @PostConstruct
    public void postConstruct()
    {

    }
    
    
    //actioneventlistener
    public void createNewMerchant(ActionEvent event) throws InputDataValidationException, MerchantUsernameExistException, UnknownPersistenceException, CurrencyNotFoundException, ClassIDExistException, MerchantNotFoundException{
        
        //by default, selecting Singapore Region
        setMerchantId((Long)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("merchantId"));
        
        Long newGymClassId = classSessionBeanLocal.createNewClass(merchantId, newGymClass);
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New class created successfully: " + newGymClassId, null));
        
    }

    public GymClass getNewGymClass() {
        return newGymClass;
    }

    public void setNewGymClass(GymClass newGymClass) {
        this.newGymClass = newGymClass;
    }
    
    
    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }
}
