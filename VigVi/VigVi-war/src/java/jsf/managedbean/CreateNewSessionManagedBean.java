/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.ClassSessionBeanLocal;
import ejb.session.stateless.CurrencySessionBeanLocal;
import ejb.session.stateless.MerchantSessionBeanLocal;
import ejb.session.stateless.SessionSessionBeanLocal;
import entity.Currency;
import entity.GymClass;
import entity.Merchant;
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
import util.exception.SessionNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.WalletNotFoundException;

/**
 *
 * @author JiaYunTeo
 */
@Named(value = "createNewSessionManagedBean")
@ViewScoped
public class CreateNewSessionManagedBean implements Serializable  {

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
    private Long merchantId;
    private List<Merchant> merchantEntities;
    private List<GymClass> gymClassEntities;
    private List<Session> sessionEntities;
    /**
     * Creates a new instance of CreateNewMerchantManagedBean
     */
    public CreateNewSessionManagedBean() {
        
        newSession = new Session();
        
    }
    
    @PostConstruct
    public void postConstruct()
    {
        
        setGymClassId((Long)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("gymClassIdToCreate"));
        
        setMerchantId((Long)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("merchantIdToCreate"));
        
        
        setMerchantEntities(merchantSessionBeanLocal.retrieveAllMerchants());
        
        setGymClassEntities(classSessionBeanLocal.retrieveAllClasses());
        
        setSessionEntities(sessionSessionBeanLocal.retrieveAllSessionsByClassId(gymClassId));
         

    }

    //actioneventlistener
    public void createNewSession(ActionEvent event) throws InputDataValidationException, MerchantUsernameExistException, UnknownPersistenceException, CurrencyNotFoundException, ClassIDExistException, MerchantNotFoundException, GymClassNotFoundException{
        
        System.out.println("create session for gymClassId: " + gymClassId);
        
        System.out.println("create session " + newSession);
        
        Long newGymClassId = sessionSessionBeanLocal.createNewSession(gymClassId, newSession);
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New session created successfully: " + newGymClassId, null));
        
        if(gymClassId !=null && !gymClassId.equals("")){
            setSessionEntities(classSessionBeanLocal.retrieveClassByClassId(gymClassId).getSessions());
        }
        else{
            setGymClassEntities(null);
        }
        
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
            setGymClassEntities(merchantSessionBeanLocal.retrieveMerchantByMerchantId(merchantId).getClasses());
        }
        else{
            setGymClassEntities(classSessionBeanLocal.retrieveAllClasses());
        }
    }
    
    public void onClassChange() throws MerchantNotFoundException, GymClassNotFoundException {
        if(gymClassId !=null && !gymClassId.equals("")){
            setSessionEntities(classSessionBeanLocal.retrieveClassByClassId(gymClassId).getSessions());
        }
        else{
            setGymClassEntities(null);
        }
    }
    
     public void viewAttendance(ActionEvent event) throws IOException
    {
        Long sessionIdToSearch = (Long)event.getComponent().getAttributes().get("sessionId");
        System.out.println("get class ID " + sessionIdToSearch);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("sessionIdToSearch", sessionIdToSearch);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewAttendance.xhtml");
    }
     
     public void endSession(ActionEvent event) throws SessionNotFoundException, CustomerSessionNotFoundException, CurrencyNotFoundException, WalletNotFoundException, AmountNotSufficientException, ClassIDExistException, UnknownPersistenceException, CustomerSessionAttendanceNullException, IOException{
         Long sessionId = (Long)event.getComponent().getAttributes().get("sessionId");
         this.sessionSessionBeanLocal.endSession(sessionId);
         FacesContext.getCurrentInstance().getExternalContext().getFlash().put("gymClassIdToCreate", gymClassId);
         FacesContext.getCurrentInstance().getExternalContext().getFlash().put("merchantIdToCreate", merchantId);
         FacesContext.getCurrentInstance().getExternalContext().redirect("viewAllSessions.xhtml");
     }
     
     public void cancelSession(ActionEvent event) throws SessionNotFoundException, CustomerSessionNotFoundException, CurrencyNotFoundException, WalletNotFoundException, AmountNotSufficientException, ClassIDExistException, UnknownPersistenceException, CustomerSessionAttendanceNullException, IOException{
         Long sessionId = (Long)event.getComponent().getAttributes().get("sessionId");
         this.sessionSessionBeanLocal.cancelSession(sessionId);
         FacesContext.getCurrentInstance().getExternalContext().getFlash().put("gymClassIdToCreate", gymClassId);
         FacesContext.getCurrentInstance().getExternalContext().getFlash().put("merchantIdToCreate", merchantId);
         FacesContext.getCurrentInstance().getExternalContext().redirect("viewAllSessions.xhtml");
     }

    public void reload() throws IOException {
        
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }
    
}
