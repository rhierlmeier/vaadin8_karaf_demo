package de.rhierlmeier.vaadin8osgi.rest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsApplicationSelect;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsExtension;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.annotation.Priority;
import javax.ws.rs.ext.Provider;

/**
 * Sample Authentication Filter
 */
@Component
@JaxrsExtension
@JaxrsApplicationSelect("(osgi.jaxrs.name=myApp)")
@PreMatching
@Priority(value = 20)
public class AuthenticationFilter implements
  ContainerRequestFilter {

  public AuthenticationFilter() {
    System.out.println("SamplePreMatchContainerRequestFilter starting up");
  }

  @Override
  public void filter(ContainerRequestContext requestContext) {
    System.out.println("SamplePreMatchContainerRequestFilter.filter() invoked");
  }
}