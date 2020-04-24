/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.resources;

import ejb.session.stateless.ClassSessionBeanLocal;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import ws.restful.model.ErrorRsp;
import ws.restful.model.RetrieveAllActiveClassesRsp;

/**
 * REST Web Service
 *
 * @author JiaYunTeo
 */
@Path("GymClass")
public class GymClassResource {

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

    /**
     * PUT method for updating or creating an instance of GymClassResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
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
