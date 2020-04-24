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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import util.security.CryptographicHelper;

/**
 *
 * @author JiaYunTeo
 */
@Entity
public class Customer implements Serializable {

   

       public enum Gender
    { 
        Male,
        Female, 
        Other
    }   
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    @Column(unique = true)
    private String customerEmail;
    @JsonbTransient
    private String customerPw;
    private String customerImage;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date customerBday;
    private String customerName;
    private Gender customerGender;
    private Boolean customerStatus;
    private String customerContactNo;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "walletId", referencedColumnName = "walletId")
    @JsonbTransient
    private Wallet wallet;
    @OneToMany(mappedBy = "customer")
    @JsonbTransient
    private List<CustomerSession> signedUpClass;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date createdDate;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date updatedDate;
    @Column(columnDefinition = "CHAR(32) NOT NULL")
    @JsonbTransient
    private String salt;
//    private Date deactivatedDate;
    
    @PrePersist
    protected void onCreate() {
        setCreatedDate(new Date());
    }

    @PreUpdate
    protected void onUpdate() {
        setUpdatedDate(new Date());
    }
    

    public Customer() {
        this.salt = CryptographicHelper.getInstance().generateRandomString(32);
    }

    public Customer(String customerEmail, String customerPw, String customerImg, Date customerBday, String customerName, Gender customerGender, String contactNumber) {
        
        this();
        
        this.customerEmail = customerEmail;
        this.customerImage = customerImg;
        this.customerBday = customerBday;
        this.customerName = customerName;
        this.customerGender = customerGender;
        this.customerContactNo = contactNumber;
        
        this.setCustomerPw(customerPw);
    }
    
    
     public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public List<CustomerSession> getSignedUpClass() {
        return signedUpClass;
    }

    public void setSignedUpClass(List<CustomerSession> signedUpClass) {
        this.signedUpClass = signedUpClass;
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

//    public List<Session> getSignedUpSession() {
//        return signedUpSession;
//    }
//
//    public void setSignedUpSession(List<Session> signedUpSession) {
//        this.signedUpSession = signedUpSession;
//    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customerId != null ? customerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the customerId fields are not set
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.customerId == null && other.customerId != null) || (this.customerId != null && !this.customerId.equals(other.customerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Customer[ id=" + customerId + " ]";
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPw() {
        return customerPw;
    }

    public void setCustomerPw(String customerPw) {
        if(customerPw != null)
        {
            this.customerPw = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(customerPw + this.salt));
        }
        else
        {
            this.customerPw = null;
        }
    }

    public String getCustomerImage() {
        return customerImage;
    }

    public void setCustomerImage(String customerImage) {
        this.customerImage = customerImage;
    }

    public Date getCustomerBday() {
        return customerBday;
    }

    public void setCustomerBday(Date customerBday) {
        this.customerBday = customerBday;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Gender getCustomerGender() {
        return customerGender;
    }

    public void setCustomerGender(Gender customerGender) {
        this.customerGender = customerGender;
    }

    public Boolean getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(Boolean customerStatus) {
        this.customerStatus = customerStatus;
    }

    public String getCustomerContactNo() {
        return customerContactNo;
    }

    public void setCustomerContactNo(String customerContactNo) {
        this.customerContactNo = customerContactNo;
    }
    
}
