/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.ClassSessionBeanLocal;
import ejb.session.stateless.CurrencySessionBeanLocal;
import ejb.session.stateless.MerchantSessionBeanLocal;
import ejb.session.stateless.PayableTransactionSessionBeanLocal;
import ejb.session.stateless.SessionSessionBeanLocal;
import entity.Currency;
import entity.GymClass;
import entity.Merchant;
import entity.PayableTransaction;
import entity.Session;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpServletRequest;
import util.exception.AmountNotSufficientException;
import util.exception.ClassIDExistException;
import util.exception.CurrencyNotFoundException;
import util.exception.CustomerSessionAttendanceNullException;
import util.exception.CustomerSessionNotFoundException;
import util.exception.GymClassNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.MerchantNotFoundException;
import util.exception.MerchantUsernameExistException;
import util.exception.PayableTransactionNotFoundException;
import util.exception.SessionNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.WalletNotFoundException;

/**
 *
 * @author JiaYunTeo
 */
@Named(value = "manageCurrencyManagedBean")
@ViewScoped
public class ManageCurrencyManagedBean implements Serializable  {

    public double getNewRate() {
        return newRate;
    }

    public void setNewRate(double newRate) {
        this.newRate = newRate;
    }

    @EJB(name = "CurrencySessionBeanLocal")
    private CurrencySessionBeanLocal currencySessionBeanLocal;

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public List<Currency> getCurrencyEntities() {
        return currencyEntities;
    }

    public void setCurrencyEntities(List<Currency> currencyEntities) {
        this.currencyEntities = currencyEntities;
    }


    
    
 
    //model
    private Long currencyId;
    private List<Currency> currencyEntities;
    private double newRate;
    /**
     * Creates a new instance of CreateNewMerchantManagedBean
     */
    public ManageCurrencyManagedBean() {
        
        
        
    }
    
    @PostConstruct
    public void postConstruct()
    {
   
        setCurrencyEntities(currencySessionBeanLocal.retrieveAllCurrencies());
        
        setCurrencyId(this.getCurrencyEntities().get(0).getCurrencyId());
        
        setNewRate(0.0);
        

    }

    //actioneventlistener
    public void updateConversionRate(ActionEvent event) throws IOException{
        
        System.out.println("update currency Id: " + currencyId);
        
        System.out.println("new Rate " + newRate);

        
        currencySessionBeanLocal.updateConversionRate(currencyId, newRate);
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New currency rate recorded successfully for Currency ID: " + currencyId, null));
        
        
        FacesContext.getCurrentInstance().getExternalContext().redirect("manageCurrency.xhtml");

        
    }

    public TimeZone getTimeZone() {  
        TimeZone timeZone = TimeZone.getDefault();  
        return timeZone;  
    }
    
    private Date todayDate = new Date();

    public Date getTodayDate() {
        return todayDate;
    }



}
