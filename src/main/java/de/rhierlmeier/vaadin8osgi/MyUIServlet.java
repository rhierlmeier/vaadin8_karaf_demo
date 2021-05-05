package de.rhierlmeier.vaadin8osgi;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

import javax.servlet.Servlet;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Pattern;

@Component(service= Servlet.class)
@WebServlet(urlPatterns = { "/ui/*", "/VAADIN/*" }, name = "MyUIServlet", asyncSupported = true)
@VaadinServletConfiguration(ui = MyUI.class, productionMode = true)
public class MyUIServlet extends VaadinServlet {

    private static Logger m_Log = Logger.getLogger("MyUIServlet");

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    private VaadinStaticResourceService m_ResourceService;

    @Override
    public URL findResourceURL(String filename) throws IOException {
        URL ret =  super.findResourceURL(filename);
        if(ret == null) {
            VaadinStaticResourceService resService = m_ResourceService;
            if(resService != null) {
                ret = resService.findResourceURL(filename);
            }
            m_Log.warning(() -> "resource " + filename + " not found");
        }
        return ret;
    }



}
