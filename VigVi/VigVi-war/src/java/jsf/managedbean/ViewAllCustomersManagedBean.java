/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.MerchantSessionBeanLocal;
import entity.Customer;
import entity.Merchant;
import java.io.IOException;
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
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author JiaYunTeo
 */
@Named(value = "viewAllCustomersManagedBean")
@RequestScoped
public class ViewAllCustomersManagedBean{

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    private List<Customer> customers;
    
    /**
     * Creates a new instance of ViewAllMerchantsManagedBean
     */
    public ViewAllCustomersManagedBean() {
    }
    
    @PostConstruct 
    public void PostConstruct(){
        customers = customerSessionBeanLocal.retrieveAllCustomers();
    }

    public void updateCustomerDetails(ActionEvent event) throws IOException
    {
        Long customerIdToUpdate = (Long)event.getComponent().getAttributes().get("customerId");
        System.out.println("get customer ID " + customerIdToUpdate);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("customerIdToUpdate", customerIdToUpdate);
        FacesContext.getCurrentInstance().getExternalContext().redirect("updateCustomer.xhtml");
    }
    
        public void viewCustomerDetails(ActionEvent event) throws IOException
    {
        Long customerIdToView = (Long)event.getComponent().getAttributes().get("customerId");
        System.out.println("get customer ID " + customerIdToView);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("customerIdToSearch", customerIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewTransactionsByCustomer.xhtml");
    }
    
    public void activateCustomer(ActionEvent event) throws IOException{
        Long customerIdToActivate = (Long)event.getComponent().getAttributes().get("customerId");
        System.out.println("to activate customer " + customerIdToActivate);
        customerSessionBeanLocal.approveCustomer(customerIdToActivate);
        this.reload();
    }
    
     public void deactivateCustomer(ActionEvent event) throws IOException{
        Long customerIdToActivate = (Long)event.getComponent().getAttributes().get("customerId");
        System.out.println("to deactivate customer " + customerIdToActivate);
        customerSessionBeanLocal.deactivateCustomer(customerIdToActivate);
        this.reload();
    }
    
    public void reload() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }

        public TimeZone getTimeZone() {  
        TimeZone timeZone = TimeZone.getDefault();  
        return timeZone;  
    }
    
    
}
