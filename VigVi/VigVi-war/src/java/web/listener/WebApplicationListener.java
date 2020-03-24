/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Web application lifecycle listener.
 *
 * @author JiaYunTeo
 */
public class WebApplicationListener implements ServletContextListener, HttpSessionListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
    
        sce.getServletContext().setAttribute("visitorCounter", 0);
        System.out.println("**********context Initialized: " + sce.getServletContext().getAttribute("visitorCounter"));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
       
    }
    
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        se.getSession().getServletContext().setAttribute("visitorCounter", ((int)se.getSession().getServletContext().getAttribute("visitorCounter"))+1);
        System.out.println("**********sessionCreated: " + se.getSession().getServletContext().getAttribute("visitorCounter"));
    }
    
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
