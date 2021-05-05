package de.rhierlmeier.vaadin8osgi;

import java.io.IOException;
import java.net.URL;

public interface VaadinStaticResourceService {
    URL findResourceURL(String filename) throws IOException;
}
