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
public class CreateNewMerchantReq {

    public Merchant getNewMerchant() {
        return newMerchant;
    }

    public void setNewMerchant(Merchant newMerchant) {
        this.newMerchant = newMerchant;
    }
    
    private Merchant newMerchant;

    public CreateNewMerchantReq() {
    }

    public CreateNewMerchantReq(Merchant newMerchant) {
        this.newMerchant = newMerchant;
    }

    
}
