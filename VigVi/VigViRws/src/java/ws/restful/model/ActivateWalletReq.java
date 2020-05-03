/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.Wallet;

/**
 *
 * @author JiaYunTeo
 */
public class ActivateWalletReq {

    public String getReferralCode() {
        if(referralCode == null || referralCode.length() == 0){
         return "VIGVI";
        }else{
        return referralCode;
        }
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public Wallet getNewWallet() {
        return newWallet;
    }

    public void setNewWallet(Wallet newWallet) {
        this.newWallet = newWallet;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    
    private Wallet newWallet;
    private Long customerId;
    private String referralCode;

    public ActivateWalletReq() {
    }

    public ActivateWalletReq(Wallet newWallet, Long customerId, String referralCode) {
        this.newWallet = newWallet;
        this.customerId = customerId;
        this.referralCode = referralCode;
    }
    
    
 

  
    
    
}
