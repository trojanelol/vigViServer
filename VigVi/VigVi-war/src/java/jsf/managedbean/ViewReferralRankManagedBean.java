/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.MerchantSessionBeanLocal;
import ejb.session.stateless.WalletSessionBeanLocal;
import entity.Merchant;
import entity.Wallet;
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
@Named(value = "viewReferralRankManagedBean")
@RequestScoped
public class ViewReferralRankManagedBean{

    @EJB(name = "WalletSessionBeanLocal")
    private WalletSessionBeanLocal walletSessionBeanLocal;

    

    private List<Wallet> wallets;
    
    /**
     * Creates a new instance of ViewAllMerchantsManagedBean
     */
    public ViewReferralRankManagedBean() {
    }
    
    @PostConstruct 
    public void PostConstruct(){
        wallets = walletSessionBeanLocal.retrieveAllWallets();
    }

    public List<Wallet> getWallets() {
        return wallets;
    }

    public void setWallets(List<Wallet> wallets) {
        this.wallets = wallets;
    }
  
    public void reload() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }

    
}
