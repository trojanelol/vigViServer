/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.GymClass;
import java.util.List;

/**
 *
 * @author JiaYunTeo
 */
public class RetrieveAllActiveClassesRsp {

   
    private List<GymClass>  gymclasses;

    public RetrieveAllActiveClassesRsp() {
    }

    public RetrieveAllActiveClassesRsp(List<GymClass> gymclasses) {
        this.gymclasses = gymclasses;
    }
    
     public List<GymClass> getGymclasses() {
        return gymclasses;
    }

    public void setGymclasses(List<GymClass> gymclasses) {
        this.gymclasses = gymclasses;
    }
}
