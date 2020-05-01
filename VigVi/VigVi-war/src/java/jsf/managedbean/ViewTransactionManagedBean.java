/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;


import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.PayableTransactionSessionBeanLocal;
import ejb.session.stateless.ReceivableTransactionSessionBeanLocal;
import entity.Customer;
import entity.PayableTransaction;
import entity.ReceivableTransaction;
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
import util.exception.CustomerNotFoundException;
import util.exception.PayableTransactionNotFoundException;


/**
 *
 * @author JiaYunTeo
 */
@Named(value = "viewTransactionManagedBean")
@ViewScoped
public class ViewTransactionManagedBean implements Serializable  {

    public List<PayableTransaction> getPayableTransactionEntities() {
        return payableTransactionEntities;
    }

    public void setPayableTransactionEntities(List<PayableTransaction> payableTransactionEntities) {
        this.payableTransactionEntities = payableTransactionEntities;
    }

    @EJB(name = "PayableTransactionSessionBeanLocal")
    private PayableTransactionSessionBeanLocal payableTransactionSessionBeanLocal;



    @EJB(name = "ReceivableTransactionSessionBeanLocal")
    private ReceivableTransactionSessionBeanLocal receivableTransactionSessionBeanLocal;

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    
 
    //model

    private Long customerId;
    private List<Customer> customerEntities;
    private List<ReceivableTransaction> transactionEntities;
    private List<PayableTransaction> payableTransactionEntities;
    /**
     * Creates a new instance of CreateNewMerchantManagedBean
     */
    public ViewTransactionManagedBean() {
        
        
        
    }
    
    @PostConstruct
    public void postConstruct()
    {
        
        setCustomerId((Long)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("customerIdToSearch"));
        
        
        setCustomerEntities(customerSessionBeanLocal.retrieveAllCustomers());
        
        setTransactionEntities(receivableTransactionSessionBeanLocal.retrieveAllReceivableTransactions());
        
        setPayableTransactionEntities(payableTransactionSessionBeanLocal.retrieveAllPayableTransactions());
         

    }


    
    public TimeZone getTimeZone() {  
        TimeZone timeZone = TimeZone.getDefault();  
        return timeZone;  
    }
    
    private Date todayDate = new Date();

    public Date getTodayDate() {
        return todayDate;
    }
    
    public void onCustomerChange() throws CustomerNotFoundException {
        if(getCustomerId() !=null && !customerId.equals("")){
            setTransactionEntities(receivableTransactionSessionBeanLocal.retrieveAllReceivableTransactionsByCustomerId(customerId));
            setPayableTransactionEntities(payableTransactionSessionBeanLocal.retrieveAllPayableTransactionsByCustomerId(customerId));
        }
        else{
            setTransactionEntities(null);
        }
    }
    
        public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<Customer> getCustomerEntities() {
        return customerEntities;
    }

    public void setCustomerEntities(List<Customer> customerEntities) {
        this.customerEntities = customerEntities;
    }

    public List<ReceivableTransaction> getTransactionEntities() {
        return transactionEntities;
    }

    public void setTransactionEntities(List<ReceivableTransaction> transactionEntities) {
        this.transactionEntities = transactionEntities;
    }


}
