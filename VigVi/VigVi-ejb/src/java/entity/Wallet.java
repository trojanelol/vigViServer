/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;

/**
 *
 * @author JiaYunTeo
 */
@Entity
public class Wallet implements Serializable {

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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public enum CardType
    { 
        Visa, 
        Mastercard, 
        Amex
    } 
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long walletId;
    private Double currentBalance;
    private Double holdBalance;
    private String ccName;
    private String ccBillingAddress;
    private CardType ccType;
    @Column(unique = true)
    private String ccNumber;
    @OneToOne(mappedBy = "wallet")
    private Customer customer;
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

    public Wallet() {
    }

    public Wallet(Double balance, String ccName, String ccBillingAddress, CardType ccType, String ccNumber) {
        this.currentBalance = balance;
        this.ccName = ccName;
        this.ccBillingAddress = ccBillingAddress;
        this.ccType = ccType;
        this.ccNumber = ccNumber;
    }
    
    
    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (walletId != null ? walletId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the walletId fields are not set
        if (!(object instanceof Wallet)) {
            return false;
        }
        Wallet other = (Wallet) object;
        if ((this.walletId == null && other.walletId != null) || (this.walletId != null && !this.walletId.equals(other.walletId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Wallet[ id=" + walletId + " ]";
    }

    public Double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(Double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public Double getHoldBalance() {
        return holdBalance;
    }

    public void setHoldBalance(Double holdBalance) {
        this.holdBalance = holdBalance;
    }

    public String getCcName() {
        return ccName;
    }

    public void setCcName(String ccName) {
        this.ccName = ccName;
    }

    public String getCcBillingAddress() {
        return ccBillingAddress;
    }

    public void setCcBillingAddress(String ccBillingAddress) {
        this.ccBillingAddress = ccBillingAddress;
    }

    public CardType getCcType() {
        return ccType;
    }

    public void setCcType(CardType ccType) {
        this.ccType = ccType;
    }

    public String getCcNumber() {
        return ccNumber;
    }

    public void setCcNumber(String ccNumber) {
        this.ccNumber = ccNumber;
    }
    
}
