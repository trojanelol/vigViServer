/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.Customer;

/**
 *
 * @author JiaYunTeo
 */
public class CustomerLoginRsp {


    private Customer customerEntity;

    public CustomerLoginRsp() {
    }

    public CustomerLoginRsp(Customer customerEntity) {
        this.customerEntity = customerEntity;
    }
    
    public Customer getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(Customer customerEntity) {
        this.customerEntity = customerEntity;
    }
            
}
