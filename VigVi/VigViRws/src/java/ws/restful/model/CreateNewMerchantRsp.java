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
public class CreateNewMerchantRsp {

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    private Long merchantId;

    public CreateNewMerchantRsp() {
    }

    public CreateNewMerchantRsp(Long merchantId) {
        this.merchantId = merchantId;
    }
    
    
    
}
