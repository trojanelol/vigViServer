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
public class CreateNewSessionRsp {
    
    private Long newSessionId;

    public CreateNewSessionRsp() {
    }

    public CreateNewSessionRsp(Long newSessionId) {
        this.newSessionId = newSessionId;
    }

    public Long getNewSessionId() {
        return newSessionId;
    }

    public void setNewSessionId(Long newSessionId) {
        this.newSessionId = newSessionId;
    }
    
    
}
