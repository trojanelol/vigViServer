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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import util.security.CryptographicHelper;

/**
 *
 * @author JiaYunTeo
 */
@Entity
public class Merchant implements Serializable {

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }


    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long merchantId;
    @OneToMany(mappedBy="merchant")
    @JsonbTransient
    private List<GymClass> classes;
    @Column(unique = true)
    private String merchantName;
    private String merchantDesc;
    private Double commissionRate;
    private String merchantEmail;
    @JsonbTransient
    private String merchantPw;
    private Boolean merchantStatus;
    private String bankName;
    private String bankAccount;
    private String merchantImage;
    private String merchantContactNo;
    private String merchantAddress;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date createdDate;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date updatedDate;
    @JsonbTransient
    private String salt;
    @ManyToOne
    @JsonbTransient
    private Currency currency;
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
        this.salt = CryptographicHelper.getInstance().generateRandomString(32);
        this.merchantStatus = false;
        this.commissionRate = 0.3;
    }

    public Merchant(String merchantName, String merchantDesc, String merchantEmail, String merchantPw, String bankName, String bankAccount, String merchantImage, String contactNumber, String address) {
        
        this();
        
        this.merchantName = merchantName;
        this.merchantDesc = merchantDesc;
        this.commissionRate = commissionRate;
        this.merchantEmail = merchantEmail;
        this.merchantStatus = merchantStatus;
        this.bankName = bankName;
        this.bankAccount = bankAccount;
        this.merchantImage = merchantImage;
        this.merchantContactNo = contactNumber;
        this.merchantAddress = address;
        
        this.setMerchantPw(merchantPw);
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
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
        if(merchantPw != null)
        {
            this.merchantPw = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(merchantPw + this.salt));
        }
        else
        {
            this.merchantPw = null;
        }
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
    
        public String getMerchantContactNo() {
        return merchantContactNo;
    }

    public void setMerchantContactNo(String merchantContactNo) {
        this.merchantContactNo = merchantContactNo;
    }

    public String getMerchantAddress() {
        return merchantAddress;
    }

    public void setMerchantAddress(String merchantAddress) {
        this.merchantAddress = merchantAddress;
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
