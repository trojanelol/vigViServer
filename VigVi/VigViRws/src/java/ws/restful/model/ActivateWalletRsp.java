/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

/**
 *
 * @author JiaYunTeo
 */
public class ActivateWalletRsp {

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }
    
    private Long walletId;

    public ActivateWalletRsp() {
    }

    public ActivateWalletRsp(Long walletId) {
        this.walletId = walletId;
    }
    
    
    
}
