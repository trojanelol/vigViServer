/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.ClassSessionBeanLocal;
import ejb.session.stateless.MerchantSessionBeanLocal;
import entity.GymClass;
import entity.Merchant;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
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
import util.exception.GymClassNotFoundException;
import util.exception.MerchantNotFoundException;

/**
 *
 * @author JiaYunTeo
 */
@Named(value = "viewAllGymClassesManagedBean")
@ViewScoped
public class ViewAllGymClassesManagedBean implements Serializable {

    @EJB(name = "ClassSessionBeanLocal")
    private ClassSessionBeanLocal classSessionBeanLocal;




    public List<GymClass> getClassEntitiesToView() {
        return classEntitiesToView;
    }

    public void setClassEntitiesToView(List<GymClass> classEntitiesToView) {
        this.classEntitiesToView = classEntitiesToView;
    }

    @EJB(name = "MerchantSessionBeanLocal")
    private MerchantSessionBeanLocal merchantSessionBeanLocal;
    
    
    

    private List<GymClass> classEntitiesToView;

    
    /**
     * Creates a new instance of UpdateMerchantManagedBean
     */
    public ViewAllGymClassesManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct()
    {
 
        try
        {            
            this.setClassEntitiesToView(classSessionBeanLocal.retrieveAllClasses());

        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    
    
    public void back(ActionEvent event) throws IOException
    {
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewMerchants.xhtml");
    }

    
    public void foo()
    {        
    }
    
    public void reload() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }
    

    
    public void updateClassDetails(ActionEvent event) throws IOException
    {
        Long gymClassIdToUpdate = (Long)event.getComponent().getAttributes().get("gymClassId");
        System.out.println("get class ID " + gymClassIdToUpdate);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("gymClassIdToUpdate", gymClassIdToUpdate);
        FacesContext.getCurrentInstance().getExternalContext().redirect("updateGymClass.xhtml");
    }
    
        public void createNewClass(ActionEvent event) throws IOException
    {
        FacesContext.getCurrentInstance().getExternalContext().redirect("createGymClass.xhtml");
    }
        
    public void viewMerchantDetails(ActionEvent event) throws IOException{
        Long merchantIdToView = (Long)event.getComponent().getAttributes().get("merchantId");
        System.out.println("get merchant ID " + merchantIdToView);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("merchantIdToView", merchantIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewMerchantDetails.xhtml");
    }   


    
}
