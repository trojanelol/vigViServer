/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.resources;

import ejb.session.stateless.ClassSessionBeanLocal;
import ejb.session.stateless.CustomerSessionSessionBeanLocal;
import ejb.session.stateless.SessionSessionBeanLocal;
import entity.CustomerSession;
import entity.CustomerSessionId;
import entity.GymClass;
import entity.Merchant;
import entity.Session;
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
import ws.restful.model.CreateNewSessionReq;
import ws.restful.model.CreateNewSessionRsp;
import ws.restful.model.ErrorRsp;
import ws.restful.model.RetrieveAllOngoingSessionsReq;
import ws.restful.model.RetrieveAllOngoingSessionsRsp;
import ws.restful.model.RetrieveAttendanceBySessionRsp;
import ws.restful.model.GlobalRsp;

/**
 * REST Web Service
 *
 * @author JiaYunTeo
 */
@Path("Session")
public class SessionResource {

    CustomerSessionSessionBeanLocal customerSessionSessionBean = lookupCustomerSessionSessionBeanLocal();

    ClassSessionBeanLocal classSessionBean = lookupClassSessionBeanLocal();

    SessionSessionBeanLocal sessionSessionBean = lookupSessionSessionBeanLocal();

   

    @Context
    private UriInfo context;
    
    

    /**
     * Creates a new instance of SessionResource
     */
    public SessionResource() {
    }

    /**
     * Retrieves representation of an instance of ws.restful.resources.SessionResource
     * @return an instance of java.lang.String
     */
   
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }
    
    @Path("CreateSession")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewSession(CreateNewSessionReq createNewSessionReq)
    {
        try
        {
            Long sessionEntity =  sessionSessionBean.createNewSession(createNewSessionReq.getClassId(), createNewSessionReq.getNewSession());
            System.out.println("********** SessionResource.createSession(): Session " + sessionEntity + " has been created");
            
            return Response.status(Status.OK).entity(new CreateNewSessionRsp(sessionEntity)).build();
        }
        catch(Exception ex)
        {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }

    /**
     * PUT method for updating or creating an instance of SessionResource
     * @param content representation for the resource
     */
    @Path("ClassDetails")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllOngoingSessionsReqByClassId(RetrieveAllOngoingSessionsReq retrieveAllOngoingSessionsReq) {
        
        if(retrieveAllOngoingSessionsReq != null)
        {
            try
            {
                
                List<Session> sessions = sessionSessionBean.retrieveAllOngoingSessionsByClassId(retrieveAllOngoingSessionsReq.getClassId());
                
                GymClass gymclass = classSessionBean.retrieveClassByClassId(retrieveAllOngoingSessionsReq.getClassId());
                
                Merchant merchant = classSessionBean.retrieveMerchantByClassId(retrieveAllOngoingSessionsReq.getClassId());
                
                RetrieveAllOngoingSessionsRsp retrieveAllOngoingSessionsRsp = new RetrieveAllOngoingSessionsRsp(sessions, gymclass, merchant);
                
                return Response.status(Status.OK).entity(retrieveAllOngoingSessionsRsp).build();
            }
            catch(Exception ex)
            {
                ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());

                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
            }
        }
        else
        {
            ErrorRsp errorRsp = new ErrorRsp("Invalid request");
            
            return Response.status(Status.BAD_REQUEST).entity(errorRsp).build();
        }
    }
    
    @Path("Attendance")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAttendanceBySessionId(@QueryParam("merchantId") Long merchantId, 
                                                    @QueryParam("sessionId") Long sessionId ) {
        
        try
        {
            List<CustomerSession> temp = customerSessionSessionBean.retrieveAllActiveCustomerSessionsBySessionId(sessionId);
            
            if(temp.get(0).getSession().getGymClass().getMerchant().getMerchantId()!= merchantId){
                ErrorRsp errorRsp = new ErrorRsp("Invalid access, wrong merchant ID");
            
                return Response.status(Status.BAD_REQUEST).entity(errorRsp).build();
            }

            RetrieveAttendanceBySessionRsp retrieveAttendanceBySessionRsp = new RetrieveAttendanceBySessionRsp(temp);

            return Response.status(Status.OK).entity(retrieveAttendanceBySessionRsp).build();
        }catch(ArrayIndexOutOfBoundsException ex){
            
            ErrorRsp errorRsp = new ErrorRsp("No sign up so far");
            
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
        catch(Exception ex)
        {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
     @Path("cancelSession")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cancelSession(@QueryParam("sessionId") Long sessionId ) {
        
        try
        {
            sessionSessionBean.cancelSession(sessionId);   

            return Response.status(Status.OK).entity(new GlobalRsp(true)).build();
        }catch(ArrayIndexOutOfBoundsException ex){
            
            ErrorRsp errorRsp = new ErrorRsp("No sign up so far");
            
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
        catch(Exception ex)
        {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
    @Path("EndSession")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response endSession(@QueryParam("merchantId") Long merchantId, 
                                                    @QueryParam("sessionId") Long sessionId ) {
        
        try
        {
            
          //merchant
            
          Session emp = sessionSessionBean.retrieveSessionBySessionId(sessionId);
          
          if(emp.getGymClass().getMerchant().getMerchantId()!= merchantId){
              
              ErrorRsp errorRsp = new ErrorRsp("Invalid access, wrong merchant");
              
              return Response.status(Status.BAD_REQUEST).entity(errorRsp).build();
          }

          sessionSessionBean.endSession(sessionId);
          
          GlobalRsp signUpClassRsp = new GlobalRsp(true);

            return Response.status(Status.OK).entity(signUpClassRsp).build();
        }catch(ArrayIndexOutOfBoundsException ex){
            
            ErrorRsp errorRsp = new ErrorRsp("No sign up so far");
            
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
        catch(Exception ex)
        {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
     @Path("MarkAttendance")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response markAttendance(@QueryParam("merchantId") Long merchantId, 
                                                    @QueryParam("sessionId") Long sessionId, 
                                                    @QueryParam("customerId") Long customerId,
                                                    @QueryParam("attendance") boolean attendance) {
        
        try
        {
            List<CustomerSession> temp = customerSessionSessionBean.retrieveAllActiveCustomerSessionsBySessionId(sessionId);
            
            if(temp.get(0).getSession().getGymClass().getMerchant().getMerchantId()!= merchantId){
                ErrorRsp errorRsp = new ErrorRsp("Invalid access, wrong merchant ID");
            
                return Response.status(Status.BAD_REQUEST).entity(errorRsp).build();
            }
            
            CustomerSessionId customerSessionId = customerSessionSessionBean.retrieveCustomerSessionByCustomerAndSessionId(customerId, sessionId).get(0).getCustomerSessionId();
            
            customerSessionSessionBean.markAttendance(customerSessionId, attendance);
            
            List<CustomerSession> after = customerSessionSessionBean.retrieveAllActiveCustomerSessionsBySessionId(sessionId);

            RetrieveAttendanceBySessionRsp retrieveAttendanceBySessionRsp = new RetrieveAttendanceBySessionRsp(after);

            return Response.status(Status.OK).entity(retrieveAttendanceBySessionRsp).build();
        }catch(ArrayIndexOutOfBoundsException ex){
            
            ErrorRsp errorRsp = new ErrorRsp("No sign up so far");
            
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
        catch(Exception ex)
        {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
    

    private SessionSessionBeanLocal lookupSessionSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (SessionSessionBeanLocal) c.lookup("java:global/VigVi/VigVi-ejb/SessionSessionBean!ejb.session.stateless.SessionSessionBeanLocal");
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


