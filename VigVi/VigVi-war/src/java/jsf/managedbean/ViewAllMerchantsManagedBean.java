/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.MerchantSessionBeanLocal;
import entity.Merchant;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author JiaYunTeo
 */
@Named(value = "viewAllMerchantsManagedBean")
@RequestScoped
public class ViewAllMerchantsManagedBean{

    @EJB(name = "MerchantSessionBeanLocal")
    private MerchantSessionBeanLocal merchantSessionBeanLocal;

    private List<Merchant> merchants;
    
    /**
     * Creates a new instance of ViewAllMerchantsManagedBean
     */
    public ViewAllMerchantsManagedBean() {
    }
    
    @PostConstruct 
    public void PostConstruct(){
        merchants = merchantSessionBeanLocal.retrieveAllMerchants();
    }

    public List<Merchant> getMerchants() {
        return merchants;
    }

    public void setMerchants(List<Merchant> merchants) {
        this.merchants = merchants;
    }
    
    public void updateMerchantDetails(ActionEvent event) throws IOException
    {
        Long merchantIdToUpdate = (Long)event.getComponent().getAttributes().get("merchantId");
        System.out.println("get merchant ID " + merchantIdToUpdate);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("merchantIdToUpdate", merchantIdToUpdate);
        FacesContext.getCurrentInstance().getExternalContext().redirect("updateMerchant.xhtml");
    }
    
        public void viewMerchantDetails(ActionEvent event) throws IOException
    {
        Long merchantIdToView = (Long)event.getComponent().getAttributes().get("merchantId");
        System.out.println("get merchant ID " + merchantIdToView);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("merchantIdToView", merchantIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewMerchantDetails.xhtml");
    }
    
    public void activateMerchant(ActionEvent event) throws IOException{
        Long merchantIdToActivate = (Long)event.getComponent().getAttributes().get("merchantId");
        System.out.println("to activate merchant " + merchantIdToActivate);
        merchantSessionBeanLocal.approveMerchant(merchantIdToActivate);
        this.reload();
    }
    
     public void deactivateMerchant(ActionEvent event) throws IOException{
        Long merchantIdToActivate = (Long)event.getComponent().getAttributes().get("merchantId");
        System.out.println("to deactivate merchant " + merchantIdToActivate);
        merchantSessionBeanLocal.deactivateMerchant(merchantIdToActivate);
        this.reload();
    }
    
    public void reload() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }

    
}
