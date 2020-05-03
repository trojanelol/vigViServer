/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.resources;

import ejb.session.stateless.ClassSessionBeanLocal;
import ejb.session.stateless.CustomerSessionSessionBeanLocal;
import entity.GymClass;
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
import util.exception.AmountNotSufficientException;
import ws.restful.model.CreateNewClassReq;
import ws.restful.model.CreateNewClassRsp;
import ws.restful.model.ErrorRsp;
import ws.restful.model.RetrieveAllActiveClassesRsp;
import ws.restful.model.SignUpClassRsp;

/**
 * REST Web Service
 *
 * @author JiaYunTeo
 */
@Path("GymClass")
public class GymClassResource {

    CustomerSessionSessionBeanLocal customerSessionSessionBean = lookupCustomerSessionSessionBeanLocal();

    ClassSessionBeanLocal classSessionBean = lookupClassSessionBeanLocal();
    

    @Context
    private UriInfo context;

    
    
    /**
     * Creates a new instance of GymClassResource
     */
    public GymClassResource() {
    }

    /**
     * Retrieves representation of an instance of ws.restful.resources.GymClassResource
     * @return an instance of java.lang.String
     */
    @Path("ActiveClasses")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllActiveClasses() {
        
        try
        {
            List<GymClass> gymclasses = classSessionBean.retrieveAllActiveClasses();

            RetrieveAllActiveClassesRsp retrieveAllActiveClassesRsp = new RetrieveAllActiveClassesRsp(gymclasses);

            return Response.status(Status.OK).entity(retrieveAllActiveClassesRsp).build();
        }
        catch(Exception ex)
        {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
    @Path("MerchantClasses")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllActiveClassesByMerchant(@QueryParam("merchantId") Long merchantId) {
        
        try
        {
            List<GymClass> gymclasses = classSessionBean.retrieveAllClassesByMerchantId(merchantId);

            RetrieveAllActiveClassesRsp retrieveAllActiveClassesRsp = new RetrieveAllActiveClassesRsp(gymclasses);

            return Response.status(Status.OK).entity(retrieveAllActiveClassesRsp).build();
        }
        catch(Exception ex)
        {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
    
    
    @Path("SignUpClass")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response staffLogin(@QueryParam("customerId") Long customerId, 
                                @QueryParam("sessionId") Long sessionId)
    {
        try
        {
            customerSessionSessionBean.signUpClass(customerId, sessionId);
            
            SignUpClassRsp signUpClassRsp = new SignUpClassRsp(true);
                
            return Response.status(Response.Status.OK).entity(signUpClassRsp).build();
            
        }catch(AmountNotSufficientException ex){
            
            ErrorRsp errorRsp = new ErrorRsp("Please top up to sign up for this session");
            
            return Response.status(Status.BAD_REQUEST).entity(errorRsp).build();
        }
        catch(Exception ex)
        {
            ErrorRsp errorRsp = new ErrorRsp("You have already signed up for this session");
            
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }

    /**
     * PUT method for updating or creating an instance of GymClassResource
     * @param content representation for the resource
     */
    @Path("CreateClass")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewMerchant(CreateNewClassReq createNewClassReq) {
        
        if(createNewClassReq != null)
        {
            try
            {
                
                Long classId = classSessionBean.createNewClass(createNewClassReq.getMerchantId(),createNewClassReq.getNewGymClass());
               
                CreateNewClassRsp createNewClassRsp = new CreateNewClassRsp(classId);
                
                return Response.status(Response.Status.OK).entity(createNewClassRsp).build();
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

    private ClassSessionBeanLocal lookupClassSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (ClassSessionBeanLocal) c.lookup("java:global/VigVi/VigVi-ejb/ClassSessionBean!ejb.session.stateless.ClassSessionBeanLocal");
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
}
