/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class ReceivableTransaction implements Serializable {

    public String getConversionCurrency() {
        return conversionCurrency;
    }

    public void setConversionCurrency(String conversionCurrency) {
        this.conversionCurrency = conversionCurrency;
    }

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long receivableTransactionId;
    @ManyToOne
    private Wallet wallet;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date createdDate;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date updatedDate;
    // in SGD
    private double topUpAmount;
    // in $V
    private double issuedAmount;
    // store the current one
    private String conversionCurrency;
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

    public ReceivableTransaction() {
    }

    public ReceivableTransaction(double topUpAmount, double issuedAmount, String conversionCurrency) {
        this.topUpAmount = topUpAmount;
        this.issuedAmount = issuedAmount;
        this.conversionCurrency = conversionCurrency;
    }
    
    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    
    public Long getReceivableTransactionId() {
        return receivableTransactionId;
    }

    public void setReceivableTransactionId(Long receivableTransactionId) {
        this.receivableTransactionId = receivableTransactionId;
    }


    public double getTopUpAmount() {
        return topUpAmount;
    }

    public void setTopUpAmount(double topUpAmount) {
        this.topUpAmount = topUpAmount;
    }


    public double getIssuedAmount() {
        return issuedAmount;
    }

    public void setIssuedAmount(double issuedAmount) {
        this.issuedAmount = issuedAmount;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (receivableTransactionId != null ? receivableTransactionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the receivableTransactionId fields are not set
        if (!(object instanceof ReceivableTransaction)) {
            return false;
        }
        ReceivableTransaction other = (ReceivableTransaction) object;
        if ((this.receivableTransactionId == null && other.receivableTransactionId != null) || (this.receivableTransactionId != null && !this.receivableTransactionId.equals(other.receivableTransactionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Transaction[ id=" + receivableTransactionId + " ]";
    }
    
}
