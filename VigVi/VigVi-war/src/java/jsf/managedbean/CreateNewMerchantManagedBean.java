/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.MerchantSessionBeanLocal;
import entity.Merchant;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.exception.InputDataValidationException;
import util.exception.MerchantUsernameExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author JiaYunTeo
 */
@Named(value = "createNewMerchantManagedBean")
@RequestScoped
public class CreateNewMerchantManagedBean {

    @EJB(name = "MerchantSessionBeanLocal")
    private MerchantSessionBeanLocal merchantSessionBeanLocal;
    
    //model
    private Merchant newMerchant;    
    /**
     * Creates a new instance of CreateNewMerchantManagedBean
     */
    public CreateNewMerchantManagedBean() {
        
        newMerchant = new Merchant();
        
    }
    
    //actioneventlistener
    public void createNewMerchant(ActionEvent event) throws InputDataValidationException, MerchantUsernameExistException, UnknownPersistenceException{
        
        Long newMerchantId = merchantSessionBeanLocal.createNewMerchant(newMerchant);
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New merchant created successfully: " + newMerchantId, null));
        
    }

    public Merchant getNewMerchant() {
        return newMerchant;
    }

    public void setNewMerchant(Merchant newMerchant) {
        this.newMerchant = newMerchant;
    }
    
}
