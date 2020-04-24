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
public class CreateNewClassRsp {
    
    private Long gymClassId;

    public CreateNewClassRsp() {
    }

    public CreateNewClassRsp(Long gymClassId) {
        this.gymClassId = gymClassId;
    }

    public Long getGymClassId() {
        return gymClassId;
    }

    public void setGymClassId(Long gymClassId) {
        this.gymClassId = gymClassId;
    }
    
    
    
}
