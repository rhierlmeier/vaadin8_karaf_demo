package de.rhierlmeier.vaadin8osgi.rest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsApplicationSelect;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsResource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Sample Resource
 */
@Component(service = HelloService.class, property = { "osgi.jaxrs.resource=true" })
@JaxrsResource
@JaxrsApplicationSelect("(osgi.jaxrs.name=myApp)")
public class HelloService
{
    @GET
    @Path(("/hello"))
    public String sayHello() {
        return "hello";
    }
}
