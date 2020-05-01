/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CustomerSessionBeanLocal;
import entity.Customer;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.exception.CustomerNotFoundException;

/**
 *
 * @author JiaYunTeo
 */
@Named(value = "updateCustomerManagedBean")
@ViewScoped
public class UpdateCustomerManagedBean implements Serializable {

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    public Long getCustomerIdToUpdate() {
        return customerIdToUpdate;
    }

    public void setCustomerIdToUpdate(Long customerIdToUpdate) {
        this.customerIdToUpdate = customerIdToUpdate;
    }

    public Customer getCustomerEntityToUpdate() {
        return customerEntityToUpdate;
    }

    public void setCustomerEntityToUpdate(Customer customerEntityToUpdate) {
        this.customerEntityToUpdate = customerEntityToUpdate;
    }

    private Long customerIdToUpdate;
    private Customer customerEntityToUpdate;

    /**
     *
     */
    public UpdateCustomerManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {
        setCustomerIdToUpdate((Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("customerIdToUpdate"));

        try {
            setCustomerEntityToUpdate(customerSessionBeanLocal.retrieveCustomerByCustomerId(customerIdToUpdate));

        } catch (CustomerNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while retrieving the customer details: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public TimeZone getTimeZone() {
        TimeZone timeZone = TimeZone.getDefault();
        return timeZone;
    }

    private Date todayDate = new Date();

    public Date getTodayDate() {
        return todayDate;
    }

    public void back(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewCustomers.xhtml");
    }

    public void foo() {
    }

    public void updateCustomer(ActionEvent event) {

        try {
            customerSessionBeanLocal.updateCustomer(getCustomerEntityToUpdate());

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Customer updated successfully", null));
        } catch (CustomerNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating customer: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

}
