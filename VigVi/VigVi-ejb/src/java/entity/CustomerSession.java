/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;

/**
 *
 * @author JiaYunTeo
 */
@Entity
public class CustomerSession implements Serializable {

    public CustomerSessionId getCustomerSessionId() {
        return customerSessionId;
    }

    public void setCustomerSessionId(CustomerSessionId customerSessionId) {
        this.customerSessionId = customerSessionId;
    }




    public enum CustomerSessionStatus{
        ACTIVE,
        WITHDRAWN,
        COMPLETED, 
        MISSED,
        CANCELLEDBYMERCHANT
    }
    
    @EmbeddedId
    private CustomerSessionId customerSessionId;
    @ManyToOne
    private Customer customer;
    @ManyToOne
    @JsonbTransient
    private Session session;
    private CustomerSessionStatus customerSessionStatus;
    private Boolean customerAttendance;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payableTransactionId", referencedColumnName = "payableTransactionId")
    @JsonbTransient
    private PayableTransaction payableTransaction;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date createdDate;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date updatedDate;
//    private Date deactivatedDate;
    
    @PrePersist
    protected void onCreate() {
        setCreatedDate(new Date());
    }

    @PreUpdate
    protected void onUpdate() {
        setUpdatedDate(new Date());
    }
    
    
    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public CustomerSession() {
    }

    public CustomerSession(CustomerSessionId customerSessionId) {
        this.customerSessionId = customerSessionId;
        this.customerSessionStatus = CustomerSessionStatus.ACTIVE;
    }
    
    public PayableTransaction getPayableTransaction() {
        return payableTransaction;
    }

    public void setPayableTransaction(PayableTransaction payableTransaction) {
        this.payableTransaction = payableTransaction;
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getCustomerSessionId() != null ? getCustomerSessionId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the customerSessionId fields are not set
        if (!(object instanceof CustomerSession)) {
            return false;
        }
        CustomerSession other = (CustomerSession) object;
        if ((this.getCustomerSessionId() == null && other.getCustomerSessionId() != null) || (this.getCustomerSessionId() != null && !this.customerSessionId.equals(other.customerSessionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CustomerSession[ id=" + getCustomerSessionId() + " ]";
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
