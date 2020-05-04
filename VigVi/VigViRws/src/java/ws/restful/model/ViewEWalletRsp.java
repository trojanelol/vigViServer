/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.PayableTransaction;
import entity.Wallet;
import java.util.List;

/**
 *
 * @author JiaYunTeo
 */
public class ViewEWalletRsp {

    public List<PayableTransaction> getPayables() {
        return payables;
    }

    public void setPayables(List<PayableTransaction> payables) {
        this.payables = payables;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }
    
    private Wallet wallet;
    private List<PayableTransaction> payables;

    public ViewEWalletRsp() {
    }

    public ViewEWalletRsp(Wallet wallet, List<PayableTransaction> payables) {
        this.wallet = wallet;
        this.payables = payables;
    }
    
    
    
}
