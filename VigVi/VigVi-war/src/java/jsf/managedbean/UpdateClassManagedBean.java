/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.ClassSessionBeanLocal;
import entity.GymClass;
import java.io.IOException;
import java.io.Serializable;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.exception.GymClassNotFoundException;

/**
 *
 * @author JiaYunTeo
 */
@Named(value = "updateClassManagedBean")
@ViewScoped
public class UpdateClassManagedBean implements Serializable {

    @EJB(name = "ClassSessionBeanLocal")
    private ClassSessionBeanLocal classSessionBeanLocal;

    public Long getGymClassIdToUpdate() {
        return gymClassIdToUpdate;
    }

    public void setGymClassIdToUpdate(Long gymClassIdToUpdate) {
        this.gymClassIdToUpdate = gymClassIdToUpdate;
    }

    public GymClass getGymClassEntityToUpdate() {
        return gymClassEntityToUpdate;
    }

    public void setGymClassEntityToUpdate(GymClass gymClassEntityToUpdate) {
        this.gymClassEntityToUpdate = gymClassEntityToUpdate;
    }

    /**
     * Creates a new instance of UpdateClassManagedBean
     */
    public UpdateClassManagedBean() {
    }
    
    
    
    private Long gymClassIdToUpdate;
    private GymClass gymClassEntityToUpdate;
    
    @PostConstruct
    public void postConstruct()
    {
        setGymClassIdToUpdate((Long)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("gymClassIdToUpdate"));
        
        try
        {            
            setGymClassEntityToUpdate(classSessionBeanLocal.retrieveClassByClassId(getGymClassIdToUpdate()));

        }
        catch(GymClassNotFoundException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while retrieving the gym class details: " + ex.getMessage(), null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    
//    
//    public void back(ActionEvent event) throws IOException
//    {
//        
//        FacesContext.getCurrentInstance().getExternalContext().redirect("viewMerchantDetails.xhtml");
//    }
    
    
    public void updateClass(ActionEvent event) 
    {
       try
            {
                classSessionBeanLocal.updateClass(getGymClassEntityToUpdate());

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Gym Class updated successfully", null));
            }
            catch(GymClassNotFoundException ex)
            {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating gym class: " + ex.getMessage(), null));
            }
            catch(Exception ex)
            {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
            }
        
    }
    
    public TimeZone getTimeZone() {  
        TimeZone timeZone = TimeZone.getDefault();  
        return timeZone;  
    }  
    
}
