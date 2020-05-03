/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.CustomerSession;
import java.util.List;

/**
 *
 * @author JiaYunTeo
 */
public class RetrieveAttendanceBySessionRsp {

    public List<CustomerSession> getCustomerSessions() {
        return customerSessions;
    }

    public void setCustomerSessions(List<CustomerSession> customerSessions) {
        this.customerSessions = customerSessions;
    }
    
    private List<CustomerSession> customerSessions;

    public RetrieveAttendanceBySessionRsp() {
    }

    public RetrieveAttendanceBySessionRsp(List<CustomerSession> customerSessions) {
        this.customerSessions = customerSessions;
    }
    
    
    
}
