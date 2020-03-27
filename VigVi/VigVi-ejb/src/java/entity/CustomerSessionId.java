/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author JiaYunTeo
 */
@Embeddable
public class CustomerSessionId implements Serializable {

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomer(Long customerId) {
        this.customerId = customerId;
    }

    public Long getSession() {
        return sessionId;
    }

    public void setSession(Long sessionId) {
        this.sessionId = sessionId;
    }

    public CustomerSessionId() {
    }

    public CustomerSessionId(Long customerId, Long sessionId) {
        this.customerId = customerId;
        this.sessionId = sessionId;
    }



    private Long customerId;
    private Long sessionId;



}