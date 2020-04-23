/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.resources;

import ejb.session.stateless.MerchantSessionBeanLocal;
import entity.Merchant;
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
import ws.restful.model.CreateNewMerchantReq;
import ws.restful.model.CreateNewMerchantRsp;
import ws.restful.model.ErrorRsp;
import ws.restful.model.RetrieveAllOngoingSessionsReq;
import ws.restful.model.RetrieveAllOngoingSessionsRsp;

/**
 * REST Web Service
 *
 * @author JiaYunTeo
 */
@Path("Merchant")
public class MerchantResource {

    MerchantSessionBeanLocal merchantSessionBean = lookupMerchantSessionBeanLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of MerchantResource
     */
    public MerchantResource() {
    }

    /**
     * Retrieves representation of an instance of ws.restful.resources.MerchantResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of MerchantResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewMerchant(CreateNewMerchantReq createNewMerchantReq) {
        
        if(createNewMerchantReq != null)
        {
            try
            {
                
                Long merchant = merchantSessionBean.createNewMerchant(createNewMerchantReq.getNewMerchant());
                
                CreateNewMerchantRsp createNewMerchantRsp = new CreateNewMerchantRsp(merchant);
                
                return Response.status(Response.Status.OK).entity(createNewMerchantRsp).build();
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

    private MerchantSessionBeanLocal lookupMerchantSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (MerchantSessionBeanLocal) c.lookup("java:global/VigVi/VigVi-ejb/MerchantSessionBean!ejb.session.stateless.MerchantSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
