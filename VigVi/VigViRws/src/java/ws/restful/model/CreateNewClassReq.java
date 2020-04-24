/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.GymClass;

/**
 *
 * @author JiaYunTeo
 */
public class CreateNewClassReq {

    
    private GymClass newGymClass;
    private Long merchantId;

    public CreateNewClassReq() {
    }

    public CreateNewClassReq(GymClass gymClass, Long merchantId) {
        this.newGymClass = gymClass;
        this.merchantId = merchantId;
    }

    public GymClass getNewGymClass() {
        return newGymClass;
    }

    public void setNewGymClass(GymClass newGymClass) {
        this.newGymClass = newGymClass;
    }
    
    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }
}
