/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.MerchantSessionBeanLocal;
import entity.Merchant;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.exception.MerchantNotFoundException;

/**
 *
 * @author JiaYunTeo
 */
@Named(value = "updateMerchantManagedBean")
@ViewScoped
public class UpdateMerchantManagedBean implements Serializable {

    public Long getMerchantIdToUpdate() {
        return merchantIdToUpdate;
    }

    public void setMerchantIdToUpdate(Long merchantIdToUpdate) {
        this.merchantIdToUpdate = merchantIdToUpdate;
    }

    public Merchant getMerchantEntityToUpdate() {
        return merchantEntityToUpdate;
    }

    public void setMerchantEntityToUpdate(Merchant merchantEntityToUpdate) {
        this.merchantEntityToUpdate = merchantEntityToUpdate;
    }

    @EJB(name = "MerchantSessionBeanLocal")
    private MerchantSessionBeanLocal merchantSessionBeanLocal;
    
    private Long merchantIdToUpdate;
    private Merchant merchantEntityToUpdate;

    
    
    /**
     * Creates a new instance of UpdateMerchantManagedBean
     */
    public UpdateMerchantManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct()
    {
        setMerchantIdToUpdate((Long)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("merchantIdToUpdate"));
        
        try
        {            
            setMerchantEntityToUpdate(merchantSessionBeanLocal.retrieveMerchantByMerchantId(getMerchantIdToUpdate()));

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
    
//    public void info() {
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "PrimeFaces Rocks."));
//    }
//     
//    public void warn() {
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning!", "Watch out for PrimeFaces."));
//    }
//     
//    public void error() {
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Contact admin."));
//    }
//     
//    public void fatal() {
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal!", "System Error"));
//    }
    
    
    public void updateMerchant(ActionEvent event)
    {
            
        
        try
        {
            merchantSessionBeanLocal.updateMerchant(getMerchantEntityToUpdate());
            

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Merchant updated successfully", null));
        }
        catch(MerchantNotFoundException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating merchant: " + ex.getMessage(), null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    
}
