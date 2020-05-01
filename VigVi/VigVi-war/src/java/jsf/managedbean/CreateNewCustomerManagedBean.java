/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CurrencySessionBeanLocal;
import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.MerchantSessionBeanLocal;
import entity.Currency;
import entity.Customer;
import entity.Merchant;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.exception.CurrencyNotFoundException;
import util.exception.CustomerUsernameExistException;
import util.exception.InputDataValidationException;
import util.exception.MerchantUsernameExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author JiaYunTeo
 */
@Named(value = "createNewCustomerManagedBean")
@RequestScoped
public class CreateNewCustomerManagedBean {

    public Customer getNewCustomer() {
        return newCustomer;
    }

    public void setNewCustomer(Customer newCustomer) {
        this.newCustomer = newCustomer;
    }

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;
   
    //model
    private Customer newCustomer;   
    /**
     * Creates a new instance of CreateNewMerchantManagedBean
     */
    public CreateNewCustomerManagedBean() {
        
        newCustomer = new Customer();
        
    }
    
    @PostConstruct
    public void postConstruct()
    {
        
    }
    
    
    //actioneventlistener
    public void createNewCustomer(ActionEvent event) throws InputDataValidationException, UnknownPersistenceException, CustomerUsernameExistException{
        
        
        Long newCustomerId = customerSessionBeanLocal.createNewCustomer(getNewCustomer());
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New customer created successfully: " + newCustomerId, null));
        
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
