package de.rhierlmeier.vaadin8osgi.rest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsApplicationBase;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsName;

import javax.ws.rs.core.Application;

/**
 * Sample Rest Application
 */
@Component(service= Application.class)
@JaxrsApplicationBase("rest")
@JaxrsName("myApp")
public class MyApplication extends Application {
}