/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author JiaYunTeo
 */
public class MerchantUsernameExistException extends Exception {

    public MerchantUsernameExistException() {
    }

    public MerchantUsernameExistException(String merchant_Name_Exists) {
        super(merchant_Name_Exists);
    }
    
}
