/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

/**
 *
 * @author JiaYunTeo
 */
@Named(value = "jasperReportManagedBean")
@RequestScoped
public class JasperReportManagedBean {

    @Resource(name = "VigViDataSource")
    private DataSource vigViDataSource;

    /**
     * Creates a new instance of JasperReportManagedBean
     */
    public JasperReportManagedBean() {
        
    }
    
      public void generateReport(ActionEvent event){
          
        try 
        {
            InputStream reportStream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasperreports/MerchantReport.jasper");
            OutputStream outputStream = FacesContext.getCurrentInstance().getExternalContext().getResponseOutputStream();
            
            JasperRunManager.runReportToPdfStream(reportStream, outputStream, new HashMap<>(), vigViDataSource.getConnection());
            
        }
        catch (IOException | JRException | SQLException ex) 
        {
            Logger.getLogger(JasperReportManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
          
      }
    
    
    
}
