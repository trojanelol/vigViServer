/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import java.io.Serializable;
import java.util.TimeZone;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author JiaYunTeo
 */
@Named(value = "timeManagedBean")
@ViewScoped
public class TimeManagedBean implements Serializable {

    /**
     * Creates a new instance of TimeManagedBean
     */
    public TimeManagedBean() {
    }
    
        
    public TimeZone getTimeZone() {  
        TimeZone timeZone = TimeZone.getDefault();  
        return timeZone;  
    }  
    
}
