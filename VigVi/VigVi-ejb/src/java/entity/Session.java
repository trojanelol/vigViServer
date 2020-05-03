/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;

/**
 *
 * @author JiaYunTeo
 */
@Entity
public class Session implements Serializable {
    
    public enum SessionStatus
    { 
        ONGOING,
        CANCELLED,
        COMPLETED;
    } 
  

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;
    @ManyToOne
    @JsonbTransient
    private GymClass gymClass;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date sessionDate;
    private SessionStatus sessionStatus;
    @OneToMany(mappedBy = "session")
    @JsonbTransient
    private List<CustomerSession> signedUpCustomer;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date createdDate;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date updatedDate;
    private Integer availableSlot;
//    private Date deactivatedDate;
    
    @PrePersist
    protected void onCreate() {
        setCreatedDate(new Date());
    }

    @PreUpdate
    protected void onUpdate() {
        setUpdatedDate(new Date());
    }

    public Session() {
        this.setSessionStatus(sessionStatus.ONGOING);
    }

    public Session(Date sessionDate) {
        this();
        this.sessionDate = sessionDate;
        
    }
    
       public Integer getAvailableSlot() {
           
        int activeCustomer = (int) this.signedUpCustomer.stream().filter(o -> o.getCustomerSessionStatus() == CustomerSession.CustomerSessionStatus.ACTIVE).count();   
           
        this.setAvailableSlot(this.gymClass.getClassSize() - activeCustomer);
           
        return availableSlot;
    }

    public void setAvailableSlot(Integer availableSlot) {
        this.availableSlot = availableSlot;
    }

    public List<CustomerSession> getSignedUpCustomer() {
        return signedUpCustomer;
    }

    public void setSignedUpCustomer(List<CustomerSession> signedUpCustomer) {
        this.signedUpCustomer = signedUpCustomer;
    }

//    public List<Customer> getSignedUpCustomer() {
//        return signedUpCustomer;
//    }
//
//    public void setSignedUpCustomer(List<Customer> signedUpCustomer) {
//        this.signedUpCustomer = signedUpCustomer;
//    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
    

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sessionId != null ? sessionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the sessionId fields are not set
        if (!(object instanceof Session)) {
            return false;
        }
        Session other = (Session) object;
        if ((this.sessionId == null && other.sessionId != null) || (this.sessionId != null && !this.sessionId.equals(other.sessionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Session[ id=" + sessionId + " ]";
    }

    public GymClass getGymClass() {
        return gymClass;
    }

    public void setGymClass(GymClass gymClass) {
        this.gymClass = gymClass;
    }

    public Date getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(Date sessionDate) {
        this.sessionDate = sessionDate;
    }

    public SessionStatus getSessionStatus() {
        return sessionStatus;
    }

    public void setSessionStatus(SessionStatus sessionStatus) {
        this.sessionStatus = sessionStatus;
    }
    
}
