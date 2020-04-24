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
public class CreateNewCustomerRsp {

    private Long customerId;

    public CreateNewCustomerRsp() {
    }

    public CreateNewCustomerRsp(Long customerId) {
        this.customerId = customerId;
    }   
    
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
