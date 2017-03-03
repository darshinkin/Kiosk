package com.sbrf.appkiosk.resource;

import com.sbrf.appkiosk.VersionQueryResponse;
import com.sbrf.appkiosk.exceptions.AppException;
import com.sbrf.appkiosk.services.ApplicationService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Path("/")
@Produces(MediaType.APPLICATION_JSON+";charset=UTF-8")
public class ApplicationResource {

    @Inject
    ApplicationService applicationService;

    @GET
    @Path("/getVersion/{os}/{app}")
    public Response getVersion(@PathParam("os") String os, @PathParam("app") String app) throws AppException {
        System.out.println(String.format("AppService.getVersion() os: %s, app: %s", os, app));
        VersionQueryResponse versionQueryResponce = applicationService.getVersionQueryResponse(os, app);
        return Response.status(Response.Status.OK).entity(versionQueryResponce).build();
    }

    @GET
    @Path("/file/{os}/{app}/{filetype}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getFile(@PathParam("os") String os, @PathParam("app") String app, @PathParam("filetype") String fileType) throws AppException {
        File file = applicationService.getFile(os, app, fileType);
        return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM_TYPE).
                header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"").
                header("Content-Length", String.valueOf(file.length()))
                .build();
    }

    @GET
    @Path("/file/ios/{app}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getIosFile(@PathParam("app") String app, @Context UriInfo uriDetails) throws AppException, IOException {
        InputStream inputStream = applicationService.getIosFile(app, uriDetails);
        return Response.ok(inputStream, MediaType.APPLICATION_OCTET_STREAM_TYPE).build();
    }

    @GET
    @Path("/file/android/{app}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getFile(@PathParam("app") String app) throws AppException {
        File file = applicationService.getAndroidFile(app);
        return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM_TYPE).
                header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"").
                header("Content-Length", String.valueOf(file.length()))
                .build();
    }
}
