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
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;

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
    @OneToMany(mappedBy="merchant")
    private List<GymClass> classes;
    @Column(unique = true)
    private String merchantName;
    private String merchantDesc;
    private Double commissionRate;
    private String merchantEmail;
    private String merchantPw;
    private Boolean merchantStatus;
    private String bankName;
    private String bankAccount;
    private String merchantImage;
    private String contactNumber;
    private String address;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date createdDate;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date updatedDate;
//    private Date deactivatedDate;
    
    @PrePersist
    protected void onCreate() {
        createdDate = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedDate = new Date();
    }

    public Merchant() {
    }

    public Merchant(String merchantName, String merchantDesc, Double commissionRate, String merchantEmail, String merchantPw, Boolean merchantStatus, String bankName, String bankAccount, String merchantImage, String contactNumber, String address) {
        this.merchantName = merchantName;
        this.merchantDesc = merchantDesc;
        this.commissionRate = commissionRate;
        this.merchantEmail = merchantEmail;
        this.merchantPw = merchantPw;
        this.merchantStatus = merchantStatus;
        this.bankName = bankName;
        this.bankAccount = bankAccount;
        this.merchantImage = merchantImage;
        this.contactNumber = contactNumber;
        this.address = address;
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
    
        public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<GymClass> getClasses() {
        return classes;
    }

    public void setClasses(List<GymClass> classes) {
        this.classes = classes;
    }
    
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
    
    



    
    
    
    
}
