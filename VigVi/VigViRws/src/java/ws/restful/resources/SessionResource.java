/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.resources;

import ejb.session.stateless.ClassSessionBeanLocal;
import ejb.session.stateless.SessionSessionBeanLocal;
import entity.GymClass;
import entity.Merchant;
import entity.Session;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import ws.restful.model.ErrorRsp;
import ws.restful.model.RetrieveAllOngoingSessionsReq;
import ws.restful.model.RetrieveAllOngoingSessionsRsp;

/**
 * REST Web Service
 *
 * @author JiaYunTeo
 */
@Path("Session")
public class SessionResource {

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
}


