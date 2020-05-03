/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.resources;

import ejb.session.stateless.CurrencySessionBeanLocal;
import ejb.session.stateless.WalletSessionBeanLocal;
import entity.Wallet;
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
import util.exception.ClassIDExistException;
import util.exception.CurrencyNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.ReferralCodeNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.WalletNotFoundException;
import ws.restful.model.ActivateWalletReq;
import ws.restful.model.ActivateWalletRsp;
import ws.restful.model.ErrorRsp;

/**
 * REST Web Service
 *
 * @author JiaYunTeo
 */
@Path("Wallet")
public class WalletResource {

    CurrencySessionBeanLocal currencySessionBean = lookupCurrencySessionBeanLocal();

    WalletSessionBeanLocal walletSessionBean = lookupWalletSessionBeanLocal();

    @Context
    private UriInfo context;
    
    

    /**
     * Creates a new instance of WalletResource
     */
    public WalletResource() {
    }

    /**
     * Retrieves representation of an instance of ws.restful.resources.WalletResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of WalletResource
     * @param content representation for the resource
     */
    @Path("Activate")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response activateWallet(ActivateWalletReq activateWalletReq) {
        
        if(activateWalletReq != null)
        {
            
            System.out.println("***********************************" + activateWalletReq.getReferralCode());
            
            System.out.println("***********************************" + activateWalletReq.getReferralCode().equals("nocode"));
            System.out.println("***********************************" + (activateWalletReq.getReferralCode() != "nocode"));
            
            if(activateWalletReq.getReferralCode().equals("nocode")){
                
                
                
                try{
                
                Long currencyId = currencySessionBean.retrieveAllCurrencies().get(0).getCurrencyId();

                Long id =  walletSessionBean.activateWalletWithoutCode(activateWalletReq.getCustomerId(), activateWalletReq.getNewWallet(), currencyId);

                return Response.status(Response.Status.OK).entity(new ActivateWalletRsp(id)).build();

                }catch(Exception e){

                    ErrorRsp errorRsp = new ErrorRsp(e.getMessage());

                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();

                }
            
                
                
            }else{
                
                try{
                    
                walletSessionBean.retrieveWalletByReferralCode(activateWalletReq.getReferralCode());

                Long currencyId = currencySessionBean.retrieveAllCurrencies().get(0).getCurrencyId();

                Long id =  walletSessionBean.activateWallet(activateWalletReq.getCustomerId(), activateWalletReq.getNewWallet(), currencyId, activateWalletReq.getReferralCode());

                return Response.status(Response.Status.OK).entity(new ActivateWalletRsp(id)).build();

                }catch(Exception e){

                    ErrorRsp errorRsp = new ErrorRsp("Wrong Referral Code");

                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();

                }
                
            }


        }
        else
        {
            ErrorRsp errorRsp = new ErrorRsp("Invalid request");
            
            return Response.status(Response.Status.BAD_REQUEST).entity(errorRsp).build();
        }

    }

    private WalletSessionBeanLocal lookupWalletSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (WalletSessionBeanLocal) c.lookup("java:global/VigVi/VigVi-ejb/WalletSessionBean!ejb.session.stateless.WalletSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private CurrencySessionBeanLocal lookupCurrencySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CurrencySessionBeanLocal) c.lookup("java:global/VigVi/VigVi-ejb/CurrencySessionBean!ejb.session.stateless.CurrencySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
