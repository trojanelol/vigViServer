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
public class GymClass implements Serializable {

    public boolean isClassStatus() {
        return classStatus;
    }

    public void setClassStatus(boolean classStatus) {
        this.classStatus = classStatus;
    }

    public String getClassRemarks() {
        return classRemarks;
    }

    public void setClassRemarks(String classRemarks) {
        this.classRemarks = classRemarks;
    }

    public String getClassInstructor() {
        return classInstructor;
    }

    public void setClassInstructor(String classInstructor) {
        this.classInstructor = classInstructor;
    }

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long classId;
    @ManyToOne
    @JsonbTransient
    private Merchant merchant;
    @OneToMany(mappedBy="gymClass")
    @JsonbTransient
    private List<Session> sessions;
    private String className;
    private String classDesc;
    private String classImage;
    private Double classPrice;
    private Integer classSize;
    private String startTime;
    private String endTime;
    private String classRemarks;
    private String classInstructor;
    private boolean classStatus;
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

    public GymClass() {
    }

    public GymClass(String className, String classDesc, String classImg, Double classPrice, Integer classSize, String startTime, String endTime, String classRemarks, String classInstructor) {
        this.className = className;
        this.classDesc = classDesc;
        this.classImage = classImg;
        this.classPrice = classPrice;
        this.classSize = classSize;
        this.startTime = startTime;
        this.endTime = endTime;
        this.classRemarks = classRemarks;
        this.classInstructor = classInstructor;
    }
    
    

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (classId != null ? classId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the classId fields are not set
        if (!(object instanceof GymClass)) {
            return false;
        }
        GymClass other = (GymClass) object;
        if ((this.classId == null && other.classId != null) || (this.classId != null && !this.classId.equals(other.classId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Class[ id=" + classId + " ]";
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassDesc() {
        return classDesc;
    }

    public void setClassDesc(String classDesc) {
        this.classDesc = classDesc;
    }

    public String getClassImage() {
        return classImage;
    }

    public void setClassImage(String classImage) {
        this.classImage = classImage;
    }

    public Double getClassPrice() {
        return classPrice;
    }

    public void setClassPrice(Double classPrice) {
        this.classPrice = classPrice;
    }

    public Integer getClassSize() {
        return classSize;
    }

    public void setClassSize(Integer classSize) {
        this.classSize = classSize;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }
    
}
