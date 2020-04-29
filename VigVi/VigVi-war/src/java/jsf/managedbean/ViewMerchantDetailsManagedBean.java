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
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpServletRequest;
import util.exception.MerchantNotFoundException;

/**
 *
 * @author JiaYunTeo
 */
@Named(value = "viewMerchantDetailsManagedBean")
@RequestScoped
public class ViewMerchantDetailsManagedBean implements Serializable {

    @EJB(name = "ClassSessionBeanLocal")
    private ClassSessionBeanLocal classSessionBeanLocal;
    
    

    public Boolean getCreateMode() {
        return createMode;
    }

    public void setCreateMode(Boolean createMode) {
        this.createMode = createMode;
    }

    public GymClass getNewGymClass() {
        return newGymClass;
    }

    public void setNewGymClass(GymClass newGymClass) {
        this.newGymClass = newGymClass;
    }

    public List<GymClass> getClassEntitiesToView() {
        return classEntitiesToView;
    }

    public void setClassEntitiesToView(List<GymClass> classEntitiesToView) {
        this.classEntitiesToView = classEntitiesToView;
    }

    public Long getMerchantIdToView() {
        return merchantIdToView;
    }

    public void setMerchantIdToView(Long merchantIdToView) {
        this.merchantIdToView = merchantIdToView;
    }

    public Merchant getMerchantEntityToView() {
        return merchantEntityToView;
    }

    public void setMerchantEntityToView(Merchant merchantEntityToView) {
        this.merchantEntityToView = merchantEntityToView;
    }

    @EJB(name = "MerchantSessionBeanLocal")
    private MerchantSessionBeanLocal merchantSessionBeanLocal;
    
    private Long merchantIdToView;
    private Merchant merchantEntityToView;
    private List<GymClass> classEntitiesToView;
    private GymClass newGymClass;   
    private Boolean createMode;
    
    /**
     * Creates a new instance of UpdateMerchantManagedBean
     */
    public ViewMerchantDetailsManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct()
    {

        setCreateMode(false);
 
        
        setMerchantIdToView((Long)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("merchantIdToView"));
 
        try
        {            
            setMerchantEntityToView(merchantSessionBeanLocal.retrieveMerchantByMerchantId(getMerchantIdToView()));
            this.setClassEntitiesToView(merchantEntityToView.getClasses());

        }
        catch(MerchantNotFoundException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while retrieving the merchant details: " + ex.getMessage(), null));
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
    
    public void activateCreateMode() throws IOException{
        this.setCreateMode(true);
       this.reload();
    }
    
    //instantiate classEntityToUpdate By Row Editor
    
    
      public void updateClass(ActionEvent event)
    {
//  
//        try
//        {
//            classSessionBeanLocal.updateClass(this.);
//            
//
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Merchant updated successfully", null));
//        }
//        catch(MerchantNotFoundException ex)
//        {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating product: " + ex.getMessage(), null));
//        }
//        catch(Exception ex)
//        {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
//        }
    }

    
}
