package ru.delivery.system.controller;


import ru.delivery.system.dao.JobManager;
import ru.delivery.system.service.UserService;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/echo")
public class DeliveryController {

    @EJB
    private UserService userService;
    @EJB
    private JobManager jobManager;

    @GET
    @Path("/settings")
    @Produces(MediaType.APPLICATION_XML)
    public Response settings() {
        try {
            List<String> jobs = jobManager.getJobNames();
            StringBuilder sb = new StringBuilder();
            jobs.forEach(job -> sb.append(job).append("\n"));
            sb.append("Jobs count:").append(jobs.size());
            return Response.status(Response.Status.OK).entity("<tag>" + sb.toString() + "</tag>").build();
        } catch (Exception e) {
            return Response.status(Response.Status.OK).entity("<tag>" + e + "</tag>").build();
        }
    }

}
