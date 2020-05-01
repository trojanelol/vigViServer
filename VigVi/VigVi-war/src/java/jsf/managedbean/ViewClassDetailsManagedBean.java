/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.ClassSessionBeanLocal;
import ejb.session.stateless.SessionSessionBeanLocal;
import entity.GymClass;
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
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpServletRequest;
import util.exception.AmountNotSufficientException;
import util.exception.ClassIDExistException;
import util.exception.CurrencyNotFoundException;
import util.exception.CustomerSessionAttendanceNullException;
import util.exception.CustomerSessionNotFoundException;
import util.exception.GymClassNotFoundException;
import util.exception.SessionNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.WalletNotFoundException;

/**
 *
 * @author JiaYunTeo
 */
@Named(value = "viewClassDetailsManagedBean")
@ViewScoped
public class ViewClassDetailsManagedBean implements Serializable {

    @EJB(name = "SessionSessionBeanLocal")
    private SessionSessionBeanLocal sessionSessionBeanLocal;

    public Long getGymClassIdToView() {
        return gymClassIdToView;
    }

    public void setGymClassIdToView(Long gymClassIdToView) {
        this.gymClassIdToView = gymClassIdToView;
    }

    public GymClass getGymClassEntityToView() {
        return gymClassEntityToView;
    }

    public void setGymClassEntityToView(GymClass gymClassEntityToView) {
        this.gymClassEntityToView = gymClassEntityToView;
    }

    public List<Session> getSessionEntitiesToView() {
        return sessionEntitiesToView;
    }

    public void setSessionEntitiesToView(List<Session> sessionEntitiesToView) {
        this.sessionEntitiesToView = sessionEntitiesToView;
    }


    @EJB(name = "ClassSessionBeanLocal")
    private ClassSessionBeanLocal classSessionBeanLocal;

    

    
    private Long gymClassIdToView;
    private GymClass gymClassEntityToView;
    private List<Session> sessionEntitiesToView;

    
    /**
     * Creates a new instance of UpdateMerchantManagedBean
     */
    public ViewClassDetailsManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct()
    {
        
        setGymClassIdToView((Long)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("gymClassIdToView"));
 
        try
        {            
            setGymClassEntityToView(classSessionBeanLocal.retrieveClassByClassId(getGymClassIdToView()));
            this.setSessionEntitiesToView(gymClassEntityToView.getSessions());

        }
        catch(GymClassNotFoundException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while retrieving the class details: " + ex.getMessage(), null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    
    
    public void back(ActionEvent event) throws IOException
    {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("merchantIdToView", gymClassEntityToView.getMerchant().getMerchantId());
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewMerchantDetails.xhtml");
    }

    
    public void foo()
    {        
    }
    
    public void reload() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }
    
    
    public void viewAttendance(ActionEvent event) throws IOException
    {
        Long sessionIdToSearch = (Long)event.getComponent().getAttributes().get("sessionId");
        System.out.println("get class ID " + sessionIdToSearch);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("sessionIdToSearch", sessionIdToSearch);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewAttendance.xhtml");
    }
    
        public void createNewSession(ActionEvent event) throws IOException
    {
        Long gymClassIdToCreate = (Long)event.getComponent().getAttributes().get("gymClassId");
        Long merchantIdToCreate = (Long)event.getComponent().getAttributes().get("merchantId");
        System.out.println("create new class for merchant ID " + gymClassIdToCreate);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("gymClassIdToCreate", gymClassIdToCreate);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("merchantIdToCreate", merchantIdToCreate);
        FacesContext.getCurrentInstance().getExternalContext().redirect("createSession.xhtml");
    }

    
    private Date todayDate = new Date();

    public Date getTodayDate() {
        return todayDate;
    }
    
    public TimeZone getTimeZone() {  
        TimeZone timeZone = TimeZone.getDefault();  
        return timeZone;  
    } 
    
    
    public void endSession(ActionEvent event) throws SessionNotFoundException, CustomerSessionNotFoundException, CurrencyNotFoundException, WalletNotFoundException, AmountNotSufficientException, ClassIDExistException, UnknownPersistenceException, CustomerSessionAttendanceNullException, IOException{
         Long sessionId = (Long)event.getComponent().getAttributes().get("sessionId");
         this.sessionSessionBeanLocal.endSession(sessionId);
         FacesContext.getCurrentInstance().getExternalContext().getFlash().put("gymClassIdToView", gymClassIdToView);
         FacesContext.getCurrentInstance().getExternalContext().redirect("viewClassDetails.xhtml");
     }
    
    public void cancelSession(ActionEvent event) throws SessionNotFoundException, CustomerSessionNotFoundException, CurrencyNotFoundException, WalletNotFoundException, AmountNotSufficientException, ClassIDExistException, UnknownPersistenceException, CustomerSessionAttendanceNullException, IOException{
         Long sessionId = (Long)event.getComponent().getAttributes().get("sessionId");
         this.sessionSessionBeanLocal.cancelSession(sessionId);
         FacesContext.getCurrentInstance().getExternalContext().getFlash().put("gymClassIdToView", gymClassIdToView);
         FacesContext.getCurrentInstance().getExternalContext().redirect("viewClassDetails.xhtml");
     }
    
    
}
