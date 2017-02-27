package com.sbrf.appkiosk.resource;

import com.sbrf.appkiosk.VersionQueryResponse;
import com.sbrf.appkiosk.exceptions.AppException;
import com.sbrf.appkiosk.services.ApplicationService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
@Produces(MediaType.APPLICATION_JSON+";charset=UTF-8")
public class ApplicationResource {

    @Inject
    ApplicationService applicationService;

    @GET
    @Path("/getVersion/{os}")
    public Response getVersion(@PathParam("os") String os) throws AppException {
        System.out.println("AppService.getVersion() " + os);
        VersionQueryResponse versionQueryResponce = applicationService.getVersionQueryResponse(os);
        return Response.status(Response.Status.OK).entity(versionQueryResponce).build();
    }
}
