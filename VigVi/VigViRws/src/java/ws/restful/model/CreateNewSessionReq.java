/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.Session;

/**
 *
 * @author JiaYunTeo
 */
public class CreateNewSessionReq {
    private Long classId;
    private Session newSession;

    public CreateNewSessionReq() {
    }

    public CreateNewSessionReq(Long classId, Session newSession) {
        this.classId = classId;
        this.newSession = newSession;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public Session getNewSession() {
        return newSession;
    }

    public void setNewSession(Session newSession) {
        this.newSession = newSession;
    }

}
