/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CurrencySessionBeanLocal;
import ejb.session.stateless.MerchantSessionBeanLocal;
import entity.Currency;
import entity.Merchant;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.exception.CurrencyNotFoundException;
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

    public List<Currency> getCurrencyEntities() {
        return currencyEntities;
    }

    public void setCurrencyEntities(List<Currency> currencyEntities) {
        this.currencyEntities = currencyEntities;
    }


    @EJB(name = "CurrencySessionBeanLocal")
    private CurrencySessionBeanLocal currencySessionBeanLocal;

    @EJB(name = "MerchantSessionBeanLocal")
    private MerchantSessionBeanLocal merchantSessionBeanLocal;
    
    //model
    private Merchant newMerchant;   
    private Long currencyId;
    private List<Currency> currencyEntities;
    /**
     * Creates a new instance of CreateNewMerchantManagedBean
     */
    public CreateNewMerchantManagedBean() {
        
        newMerchant = new Merchant();
        
    }
    
    @PostConstruct
    public void postConstruct()
    {
        setCurrencyEntities(currencySessionBeanLocal.retrieveAllCurrencies());
    }
    
    
    //actioneventlistener
    public void createNewMerchant(ActionEvent event) throws InputDataValidationException, MerchantUsernameExistException, UnknownPersistenceException, CurrencyNotFoundException{
        
        //by default, selecting Singapore Region
        if(currencyId == 0){
          Long currencyId = currencySessionBeanLocal.retrieveAllCurrencies().get(0).getCurrencyId();
        }
        
        Long newMerchantId = merchantSessionBeanLocal.createNewMerchant(currencyId, newMerchant);
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New merchant created successfully: " + newMerchantId, null));
        
    }

    public Merchant getNewMerchant() {
        return newMerchant;
    }

    public void setNewMerchant(Merchant newMerchant) {
        this.newMerchant = newMerchant;
    }
    
    
    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }
}
