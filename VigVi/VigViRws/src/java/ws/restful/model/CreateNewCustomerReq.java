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
public class CreateNewCustomerReq {

    public Customer getNewCustomer() {
        return newCustomer;
    }

    public void setNewCustomer(Customer newCustomer) {
        this.newCustomer = newCustomer;
    }
    private Customer newCustomer;

    public CreateNewCustomerReq() {
    }

    public CreateNewCustomerReq(Customer newCustomer) {
        this.newCustomer = newCustomer;
    }
    
    
}
