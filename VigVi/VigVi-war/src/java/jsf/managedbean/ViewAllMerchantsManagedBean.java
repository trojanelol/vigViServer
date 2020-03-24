/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.MerchantSessionBeanLocal;
import entity.Merchant;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author JiaYunTeo
 */
@Named(value = "viewAllMerchantsManagedBean")
@RequestScoped
public class ViewAllMerchantsManagedBean {

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
    
}
