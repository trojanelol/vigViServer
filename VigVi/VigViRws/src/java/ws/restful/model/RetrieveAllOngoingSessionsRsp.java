/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.GymClass;
import entity.Merchant;
import entity.Session;
import java.util.List;

/**
 *
 * @author JiaYunTeo
 */
public class RetrieveAllOngoingSessionsRsp {

    public GymClass getGymClass() {
        return gymClass;
    }

    public void setGymClass(GymClass gymClass) {
        this.gymClass = gymClass;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    
    private List<Session> sessions;
    private GymClass gymClass;
    private Merchant merchant;        

    public RetrieveAllOngoingSessionsRsp() {
    }

    public RetrieveAllOngoingSessionsRsp(List<Session> sessions, GymClass gymClass, Merchant merchant) {
        this.sessions = sessions;
        this.gymClass = gymClass;
        this.merchant = merchant;
    }

    
    
    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }
            
            
}
