/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.Merchant;

/**
 *
 * @author JiaYunTeo
 */
public class MerchantLoginRsp {

    private Merchant merchantEntity;

    public MerchantLoginRsp() {
    }

    public MerchantLoginRsp(Merchant merchantEntity) {
        this.merchantEntity = merchantEntity;
    }

    public Merchant getMerchantEntity() {
        return merchantEntity;
    }

    public void setMerchantEntity(Merchant merchantEntity) {
        this.merchantEntity = merchantEntity;
    }
            
}
