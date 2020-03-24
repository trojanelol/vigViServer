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
import javax.persistence.ManyToOne;

/**
 *
 * @author JiaYunTeo
 */
@Entity
public class GymClass implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long classId;
    @ManyToOne
    private Merchant merchant;
    private String className;
    private String classDesc;
    private String classImg;
    private Double classPrice;
    private Integer classSize;
    private String startTime;
    private String endTime;

    public GymClass() {
    }

    public GymClass(String className, String classDesc, String classImg, Double classPrice, Integer classSize, String startTime, String endTime) {
        this.className = className;
        this.classDesc = classDesc;
        this.classImg = classImg;
        this.classPrice = classPrice;
        this.classSize = classSize;
        this.startTime = startTime;
        this.endTime = endTime;
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

    public String getClassImg() {
        return classImg;
    }

    public void setClassImg(String classImg) {
        this.classImg = classImg;
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
    
}
