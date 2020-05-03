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
public class GlobalRsp {

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    private boolean success;

    public GlobalRsp() {
    }

    public GlobalRsp(boolean success) {
        this.success = success;
    }
    
    
    
}
