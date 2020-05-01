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
@Named(value = "makePaymentManagedBean")
@ViewScoped
public class MakePaymentManagedBean implements Serializable  {

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public List<PayableTransaction> getTransactionEntities() {
        return transactionEntities;
    }

    public void setTransactionEntities(List<PayableTransaction> transactionEntities) {
        this.transactionEntities = transactionEntities;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    @EJB(name = "PayableTransactionSessionBeanLocal")
    private PayableTransactionSessionBeanLocal payableTransactionSessionBeanLocal;

    public List<Session> getSessionEntities() {
        return sessionEntities;
    }

    public void setSessionEntities(List<Session> sessionEntities) {
        this.sessionEntities = sessionEntities;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public List<GymClass> getGymClassEntities() {
        return gymClassEntities;
    }

    public void setGymClassEntities(List<GymClass> gymClassEntities) {
        this.gymClassEntities = gymClassEntities;
    }

    public List<Merchant> getMerchantEntities() {
        return merchantEntities;
    }

    public void setMerchantEntities(List<Merchant> merchantEntities) {
        this.merchantEntities = merchantEntities;
    }

    @EJB(name = "MerchantSessionBeanLocal")
    private MerchantSessionBeanLocal merchantSessionBeanLocal;

    @EJB(name = "SessionSessionBeanLocal")
    private SessionSessionBeanLocal sessionSessionBeanLocal;

    @EJB(name = "ClassSessionBeanLocal")
    private ClassSessionBeanLocal classSessionBeanLocal;
    
    
 
    //model
    private Session newSession;   
    private Long gymClassId;
    private Long transactionId;
    private Long merchantId;
    private String paymentReference;
    private List<Merchant> merchantEntities;
    private List<GymClass> gymClassEntities;
    private List<Session> sessionEntities;
    private List<PayableTransaction> transactionEntities;
    /**
     * Creates a new instance of CreateNewMerchantManagedBean
     */
    public MakePaymentManagedBean() {
        
        
        
    }
    
    @PostConstruct
    public void postConstruct()
    {
        
        setMerchantId((Long)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("merchantIdToSearch"));
        
        
        setMerchantEntities(merchantSessionBeanLocal.retrieveAllMerchants());
        
        setTransactionEntities(payableTransactionSessionBeanLocal.retrieveAllPayableTransactions());
         

    }

    //actioneventlistener
    public void makePayment(ActionEvent event) throws PayableTransactionNotFoundException, IOException{
        
        System.out.println("pay for transactionId: " + transactionId);
        
        System.out.println("reference ID " + paymentReference);
        
//        String paymentReferenceevent.getComponent().getAttributes().get("paymentReference");
        
        if(payableTransactionSessionBeanLocal.payMerchant(transactionId, paymentReference)){
        
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Payment recorded successfully for Transaction ID: " + transactionId + " with reference" + paymentReference, null));
        
        }
        
//        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("merchantIdToSearch",merchantId);
//        FacesContext.getCurrentInstance().getExternalContext().redirect("viewTransactionsByMerchant.xhtml");

        
    }


    public Session getNewSession() {
        return newSession;
    }

    public void setNewSession(Session newSession) {
        this.newSession = newSession;
    }
    
    
    public Long getGymClassId() {
        return gymClassId;
    }

    public void setGymClassId(Long gymClassId) {
        this.gymClassId = gymClassId;
    }
    
    public TimeZone getTimeZone() {  
        TimeZone timeZone = TimeZone.getDefault();  
        return timeZone;  
    }
    
    private Date todayDate = new Date();

    public Date getTodayDate() {
        return todayDate;
    }
    
    public void onMerchantChange() throws MerchantNotFoundException {
        if(merchantId !=null && !merchantId.equals("")){
            setTransactionEntities(payableTransactionSessionBeanLocal.retrieveUnpaidPayableTransactionsByMerchantId(merchantId));
        }
        else{
            setTransactionEntities(null);
        }
    }


}
