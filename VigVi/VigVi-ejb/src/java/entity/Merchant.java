/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author JiaYunTeo
 */
@Entity
public class Merchant implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long merchantId;
    private String merchantName;
    private String merchantDesc;
    private Double commissionRate;
    private String merchantEmail;
    private String merchantPw;
    private Boolean merchantStatus;
    private String bankName;
    private String bankAccount;
    private String merchantImage;

    public Merchant() {
    }

    public Merchant(String merchantName, String merchantDesc, Double commissionRate, String merchantEmail, String merchantPw, Boolean merchantStatus, String bankName, String bankAccount, String merchantImage) {
        this.merchantName = merchantName;
        this.merchantDesc = merchantDesc;
        this.commissionRate = commissionRate;
        this.merchantEmail = merchantEmail;
        this.merchantPw = merchantPw;
        this.merchantStatus = merchantStatus;
        this.bankName = bankName;
        this.bankAccount = bankAccount;
        this.merchantImage = merchantImage;
    }
    
    
    

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (merchantId != null ? merchantId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the merchantId fields are not set
        if (!(object instanceof Merchant)) {
            return false;
        }
        Merchant other = (Merchant) object;
        if ((this.merchantId == null && other.merchantId != null) || (this.merchantId != null && !this.merchantId.equals(other.merchantId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Merchant[ id=" + merchantId + " ]";
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantDesc() {
        return merchantDesc;
    }

    public void setMerchantDesc(String merchantDesc) {
        this.merchantDesc = merchantDesc;
    }

    public Double getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(Double commissionRate) {
        this.commissionRate = commissionRate;
    }

    public String getMerchantEmail() {
        return merchantEmail;
    }

    public void setMerchantEmail(String merchantEmail) {
        this.merchantEmail = merchantEmail;
    }

    public String getMerchantPw() {
        return merchantPw;
    }

    public void setMerchantPw(String merchantPw) {
        this.merchantPw = merchantPw;
    }

    public Boolean getMerchantStatus() {
        return merchantStatus;
    }

    public void setMerchantStatus(Boolean merchantStatus) {
        this.merchantStatus = merchantStatus;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getMerchantImage() {
        return merchantImage;
    }

    public void setMerchantImage(String merchantImage) {
        this.merchantImage = merchantImage;
    }
    
}
