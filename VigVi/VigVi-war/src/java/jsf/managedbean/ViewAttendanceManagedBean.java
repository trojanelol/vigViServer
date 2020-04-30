/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.ClassSessionBeanLocal;
import ejb.session.stateless.CurrencySessionBeanLocal;
import ejb.session.stateless.CustomerSessionSessionBeanLocal;
import ejb.session.stateless.MerchantSessionBeanLocal;
import ejb.session.stateless.SessionSessionBeanLocal;
import entity.Currency;
import entity.Customer;
import entity.CustomerSession;
import entity.CustomerSessionId;
import entity.GymClass;
import entity.Merchant;
import entity.Session;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpServletRequest;
import util.exception.ClassIDExistException;
import util.exception.CurrencyNotFoundException;
import util.exception.GymClassNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.MerchantNotFoundException;
import util.exception.MerchantUsernameExistException;
import util.exception.SessionNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author JiaYunTeo
 */
@Named(value = "viewAttendanceManagedBean")
@ViewScoped
public class ViewAttendanceManagedBean implements Serializable  {

    public Session getSessionEntityToInitialise() {
        return sessionEntityToInitialise;
    }

    public void setSessionEntityToInitialise(Session sessionEntityToInitialise) {
        this.sessionEntityToInitialise = sessionEntityToInitialise;
    }

    @EJB(name = "CustomerSessionSessionBeanLocal")
    private CustomerSessionSessionBeanLocal customerSessionSessionBeanLocal;

    public List<CustomerSession> getSignedUpCustomer() {
        return signedUpCustomer;
    }

    public void setSignedUpCustomer(List<CustomerSession> signedUpCustomer) {
        this.signedUpCustomer = signedUpCustomer;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

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
    private Long sessionId; 
    private Long gymClassId;
    private Long merchantId;
    private List<Merchant> merchantEntities;
    private List<GymClass> gymClassEntities;
    private List<Session> sessionEntities;
    private List<CustomerSession> signedUpCustomer;
    private Session sessionEntityToInitialise;
    /**
     * Creates a new instance of CreateNewMerchantManagedBean
     */
    public ViewAttendanceManagedBean() {
        
        
        
    }
    
    @PostConstruct
    public void postConstruct()
    {
        
        
        setSessionId((Long)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("sessionIdToSearch"));
        
        setSessionEntities(sessionSessionBeanLocal.retrieveAllSessions());
        
        if(sessionId == null && sessionId.equals("")){
            setSessionId(sessionEntities.get(0).getSessionId());
        }
        
        try {
            setSessionEntityToInitialise(sessionSessionBeanLocal.retrieveSessionBySessionId(sessionId));
            setGymClassId(sessionEntityToInitialise.getGymClass().getClassId());
            setMerchantId(sessionEntityToInitialise.getGymClass().getMerchant().getMerchantId());
            setSignedUpCustomer(customerSessionSessionBeanLocal.retrieveAllCustomerSessionsBySessionId(sessionId));
        } catch (SessionNotFoundException ex) {
            setSessionEntityToInitialise(null);
            setSignedUpCustomer(customerSessionSessionBeanLocal.retrieveAllCustomerSessions());
            
        }

        setMerchantEntities(merchantSessionBeanLocal.retrieveAllMerchants());
        
        setGymClassEntities(classSessionBeanLocal.retrieveAllClasses());
        
        setSessionEntities(sessionSessionBeanLocal.retrieveAllSessions());
        
        
 

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
            setGymClassEntities(merchantSessionBeanLocal.retrieveMerchantByMerchantId(merchantId).getClasses());
        }
        else{
            setGymClassEntities(classSessionBeanLocal.retrieveAllClasses());
            setSessionEntities(null);
        }
    }
    
    public void onClassChange() throws MerchantNotFoundException, GymClassNotFoundException {
        if(gymClassId !=null && !gymClassId.equals("")){
            setSessionEntities(classSessionBeanLocal.retrieveClassByClassId(gymClassId).getSessions());
        }
        else{
            setSessionEntities(null);
        }
    }
    
    public void onSessionChange() throws SessionNotFoundException {
        if(sessionId !=null && !sessionId.equals("")){
            setSignedUpCustomer(customerSessionSessionBeanLocal.retrieveAllCustomerSessionsBySessionId(sessionId));
            System.out.println("signed up customer" + this.signedUpCustomer);
        }
        else{
            setSignedUpCustomer(null);
        }
    }
    
    public void present (ActionEvent event) throws IOException, SessionNotFoundException{
        CustomerSessionId customerSessionId;
        customerSessionId = (CustomerSessionId)event.getComponent().getAttributes().get("customerSessionId");
        sessionId = (Long)event.getComponent().getAttributes().get("sessionId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("sessionIdToSearch", sessionId);

        customerSessionSessionBeanLocal.markAttendance(customerSessionId,true);
        this.reload();
    }
    
     public void absent (ActionEvent event) throws IOException, SessionNotFoundException{
        CustomerSessionId customerSessionId;
        customerSessionId = (CustomerSessionId)event.getComponent().getAttributes().get("customerSessionId");
        sessionId = (Long)event.getComponent().getAttributes().get("sessionId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("sessionIdToSearch", sessionId);
        customerSessionSessionBeanLocal.markAttendance(customerSessionId,false);
        this.reload();
    }
      
    public void reload() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }
     
    
}
