/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.MerchantSessionBeanLocal;
import ejb.session.stateless.SessionSessionBeanLocal;
import entity.Customer;
import entity.CustomerSession;
import entity.Merchant;
import entity.Session;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.BarChartSeries;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author JiaYunTeo
 */
@Named(value = "dashboardManagedBean")
@ViewScoped
public class DashboardManagedBean implements Serializable {

    public LineChartModel getAnimatedModelSignUpCustomers() {
        return animatedModelSignUpCustomers;
    }

    public void setAnimatedModelSignUpCustomers(LineChartModel animatedModelSignUpCustomers) {
        this.animatedModelSignUpCustomers = animatedModelSignUpCustomers;
    }

    @EJB(name = "SessionSessionBeanLocal")
    private SessionSessionBeanLocal sessionSessionBeanLocal;

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }

    @EJB(name = "MerchantSessionBeanLocal")
    private MerchantSessionBeanLocal merchantSessionBeanLocal;

    public List<Merchant> getMerchants() {
        return merchants;
    }

    public void setMerchants(List<Merchant> merchants) {
        this.merchants = merchants;
    }

    public PieChartModel getPieModelCustomerStatus() {
        return pieModelCustomerStatus;
    }

    public void setPieModelCustomerStatus(PieChartModel pieModelCustomerStatus) {
        this.pieModelCustomerStatus = pieModelCustomerStatus;
    }

    public PieChartModel getPieModelCustomerGender() {
        return pieModelCustomerGender;
    }

    public void setPieModelCustomerGender(PieChartModel pieModelCustomerGender) {
        this.pieModelCustomerGender = pieModelCustomerGender;
    }

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;
    
    

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }
    
    

    /**
     * Creates a new instance of DashboardManagedBean
     */
    public DashboardManagedBean() {
    }
    
    private List<Customer> customers;
    private List<Merchant> merchants;
    private List<Session> sessions;
    private PieChartModel pieModelCustomerGender;
    private PieChartModel pieModelCustomerStatus;
    private LineChartModel animatedModelSignUpCustomers;
    
    @PostConstruct
    public void postConstruct()
    {
        
        setCustomers(customerSessionBeanLocal.retrieveAllCustomers());
        setMerchants(merchantSessionBeanLocal.retrieveAllMerchants());
        setSessions(sessionSessionBeanLocal.retrieveAllSessions());
        
        this.customerGenderPieChart();
        this.customerActivePieChart();
        this.customerSignUpChart();

    }

    
    private void customerGenderPieChart() {
        setPieModelCustomerGender(new PieChartModel());
 
        int female = (int)customers.stream().filter(o -> o.getCustomerGender() == Customer.Gender.Female).count();
        int male = (int)customers.stream().filter(o -> o.getCustomerGender() == Customer.Gender.Male).count();
  
        pieModelCustomerGender.set("Female", female);
        pieModelCustomerGender.set("Male", male);
 
        pieModelCustomerGender.setTitle("Customer Gender");
        pieModelCustomerGender.setLegendPosition("w");
        pieModelCustomerGender.setShadow(true);
    }
    
    private void customerActivePieChart() {
        setPieModelCustomerStatus(new PieChartModel());
 
        int active = (int)customers.stream().filter(o -> o.getCustomerStatus() == true).count();
        int inactive = (int)customers.stream().filter(o -> o.getCustomerStatus() == false).count();
  
        pieModelCustomerStatus.set("Active Customer", active);
        pieModelCustomerStatus.set("Non-Active Customer", inactive);
 
        pieModelCustomerStatus.setTitle("Customer Status");
        pieModelCustomerStatus.setLegendPosition("w");
        pieModelCustomerStatus.setShadow(true);
    }
    
    private void customerSignUpChart() {
        setAnimatedModelSignUpCustomers(new LineChartModel());
 
        BarChartSeries series1 = new BarChartSeries();
        series1.setLabel("Signed Up");
        LineChartSeries series2 = new LineChartSeries();
        series2.setLabel("Max Class Size");
        BarChartSeries series3 = new BarChartSeries();
        series3.setLabel("Withdrawn");
//        
//        int ongoing = (int) sessions.stream().filter(o->o.getSessionStatus() == Session.SessionStatus.ONGOING).count();
//        int cancelled = (int) sessions.stream().filter(o->o.getSessionStatus() == Session.SessionStatus.CANCELLED).count();
//        int completed = (int) sessions.stream().filter(o->o.getSessionStatus() == Session.SessionStatus.COMPLETED).count();
//        
//        series1.set("Ongoing", ongoing);
//        series1.set("Ongoing", ongoing);
 
        for(int i=0; i<sessions.size(); i++){
            Long sessionId = sessions.get(i).getSessionId();
            int signUpCustomers = sessions.get(i).getSignedUpCustomer().size();
            int maxClassSize = sessions.get(i).getGymClass().getClassSize();
            int withdrawn = (int) sessions.get(i).getSignedUpCustomer().stream().filter(o->o.getCustomerSessionStatus() == CustomerSession.CustomerSessionStatus.WITHDRAWN).count();
            series1.set(sessionId, signUpCustomers);
            series2.set(sessionId, maxClassSize);
            series3.set(sessionId, withdrawn);
        }    
        
 
        animatedModelSignUpCustomers.addSeries(series1);
        animatedModelSignUpCustomers.addSeries(series2);
        animatedModelSignUpCustomers.addSeries(series3);
        
        animatedModelSignUpCustomers.setTitle("Signed Up Status");
        animatedModelSignUpCustomers.setAnimate(true);
        animatedModelSignUpCustomers.setLegendPosition("se");
        Axis yAxis = animatedModelSignUpCustomers.getAxis(AxisType.Y);
        yAxis.setMin(0);
 
    }
}
