/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author JiaYunTeo
 */
@Entity
public class CustomerSession implements Serializable {


    public enum CustomerSessionStatus{
        ACTIVE,
        CANCELLED,
        COMPLETED
    }
    
    @EmbeddedId
    private CustomerSessionId customerSessionId;
    @ManyToOne
    private Customer customer;
    @ManyToOne
    private Session session;
    private CustomerSessionStatus customerSessionStatus;
    private Boolean customerAttendance;

    public CustomerSession() {
    }

    public CustomerSession(CustomerSessionId customerSessionId) {
        this.customerSessionId = customerSessionId;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customerSessionId != null ? customerSessionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the customerSessionId fields are not set
        if (!(object instanceof CustomerSession)) {
            return false;
        }
        CustomerSession other = (CustomerSession) object;
        if ((this.customerSessionId == null && other.customerSessionId != null) || (this.customerSessionId != null && !this.customerSessionId.equals(other.customerSessionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CustomerSession[ id=" + customerSessionId + " ]";
    }
    
        public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public CustomerSessionStatus getCustomerSessionStatus() {
        return customerSessionStatus;
    }

    public void setCustomerSessionStatus(CustomerSessionStatus customerSessionStatus) {
        this.customerSessionStatus = customerSessionStatus;
    }

    public Boolean getCustomerAttendance() {
        return customerAttendance;
    }

    public void setCustomerAttendance(Boolean customerAttendance) {
        this.customerAttendance = customerAttendance;
    }
    
}
