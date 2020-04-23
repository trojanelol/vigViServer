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
public class RetrieveAllOngoingSessionsReq {


    private Long classId;

    public RetrieveAllOngoingSessionsReq() {
    }

    public RetrieveAllOngoingSessionsReq(Long classId) {
        this.classId = classId;
    }
    
    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }
}
