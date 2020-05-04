/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.resources;

import ejb.session.stateless.ClassSessionBeanLocal;
import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.CustomerSessionSessionBeanLocal;
import entity.Customer;
import entity.CustomerSession;
import entity.GymClass;
import entity.Session;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.InvalidLoginCredentialException;
import ws.restful.model.CreateNewCustomerReq;
import ws.restful.model.CreateNewCustomerRsp;
import ws.restful.model.CustomerLoginRsp;
import ws.restful.model.ErrorRsp;
import ws.restful.model.GlobalRsp;
import ws.restful.model.RetrieveAllOngoingSessionsRsp;

/**
 * REST Web Service
 *
 * @author JiaYunTeo
 */
@Path("Customer")
public class CustomerResource {

    ClassSessionBeanLocal classSessionBean = lookupClassSessionBeanLocal();

    CustomerSessionSessionBeanLocal customerSessionSessionBean = lookupCustomerSessionSessionBeanLocal();

    CustomerSessionBeanLocal customerSessionBean = lookupCustomerSessionBeanLocal();
    
    

    @Context
    private UriInfo context;
    
    

    /**
     * Creates a new instance of CustomerResource
     */
    public CustomerResource() {
    }

    /**
     * Retrieves representation of an instance of ws.restful.resources.CustomerResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }
    
    
    @Path("Login")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response customerLogin(@QueryParam("email") String email, 
                                @QueryParam("password") String password)
    {
        try
        {
            Customer customerEntity = customerSessionBean.customerLogin(email, password);
            System.out.println("********** CustomerResource.customerLogin(): Customer " + customerEntity.getCustomerId() + " login remotely via web service");
            
            return Response.status(Status.OK).entity(new CustomerLoginRsp(customerEntity)).build();
        }
        catch(InvalidLoginCredentialException ex)
        {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            
            return Response.status(Status.UNAUTHORIZED).entity(errorRsp).build();
        }
        catch(Exception ex)
        {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
     @Path("Class")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveActiveRegisteredClass(@QueryParam("customerId") Long customerId)
    {
        try
        {
            List<Session> emp = customerSessionSessionBean.retrieveCustomerSessionByCustomerId(customerId);
            
            return Response.status(Status.OK).entity(new RetrieveAllOngoingSessionsRsp(emp)).build();
        }
        catch(Exception ex)
        {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
       @Path("Withdraw")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response withdrawSession(@QueryParam("customerId") Long customerId, @QueryParam("sessionId") Long sessionId)
    {
        try
        {
            
            CustomerSession temp = customerSessionSessionBean.retrieveCustomerSessionByCustomerAndSessionId(customerId, sessionId).get(0);
            
            CustomerSession emp = customerSessionSessionBean.withdrawSession(temp.getCustomerSessionId());
            
            return Response.status(Status.OK).entity(new GlobalRsp(true)).build();
        }
        catch(Exception ex)
        {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
   

    

    /**
     * PUT method for updating or creating an instance of CustomerResource
     * @param content representation for the resource
     */
    @Path("Register")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewCustomer(CreateNewCustomerReq createNewCustomerReq) {
        
        if(createNewCustomerReq != null)
        {
            try
            {
  
                Long customer = customerSessionBean.createNewCustomer(createNewCustomerReq.getNewCustomer());
                
                CreateNewCustomerRsp createNewCustomerRsp = new CreateNewCustomerRsp(customer);
                
                return Response.status(Response.Status.OK).entity(createNewCustomerRsp).build();
            }
            catch(Exception ex)
            {
                ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());

                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
            }
        }
        else
        {
            ErrorRsp errorRsp = new ErrorRsp("Invalid request");
            
            return Response.status(Response.Status.BAD_REQUEST).entity(errorRsp).build();
        }
    }

    private CustomerSessionBeanLocal lookupCustomerSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CustomerSessionBeanLocal) c.lookup("java:global/VigVi/VigVi-ejb/CustomerSessionBean!ejb.session.stateless.CustomerSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private CustomerSessionSessionBeanLocal lookupCustomerSessionSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CustomerSessionSessionBeanLocal) c.lookup("java:global/VigVi/VigVi-ejb/CustomerSessionSessionBean!ejb.session.stateless.CustomerSessionSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private ClassSessionBeanLocal lookupClassSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (ClassSessionBeanLocal) c.lookup("java:global/VigVi/VigVi-ejb/ClassSessionBean!ejb.session.stateless.ClassSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
