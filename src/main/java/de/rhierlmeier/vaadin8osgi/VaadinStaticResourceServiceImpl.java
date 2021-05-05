package de.rhierlmeier.vaadin8osgi;

import com.vaadin.osgi.resources.*;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Pattern;

@Component(service = VaadinStaticResourceService.class)
public class VaadinStaticResourceServiceImpl implements VaadinStaticResourceService {

    private static Logger m_Log = Logger.getLogger("VaadinStaticResourceServiceImpl");

    private Map<Pattern,Object> m_PatterToContributor = new HashMap<>();

    @Override
    public URL findResourceURL(String filename) throws IOException {

        URL ret = null;
        synchronized (m_PatterToContributor) {

            for (Map.Entry<Pattern, Object> entry : m_PatterToContributor.entrySet()) {
                if (entry.getKey().matcher(filename).matches()) {
                    ret = entry.getValue().getClass().getClassLoader().getResource(filename);
                    if (ret != null) {
                        break;
                    }
                }
            }

        }
        if(ret == null) {
            m_Log.warning(() -> "resource " + filename + " not found");
        }
        return ret;
    }


    @Reference(
            cardinality = ReferenceCardinality.MULTIPLE,
            service = OsgiVaadinContributor.class,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "removeContributor"
    )
    public void addContributor(OsgiVaadinContributor contributor) {

        synchronized (m_PatterToContributor) {
            for (OsgiVaadinResource resource : contributor.getContributions()) {
                Pattern pattern = toPattern("", resource.getName());
                m_Log.info(() -> "Contributor " + contributor + " brings resource " + pattern.pattern());
                m_PatterToContributor.put(pattern, contributor);
            }
        }
    }

    public void removeContributor(OsgiVaadinContributor contributor) {
        removePatterns(contributor);
    }

    private void removePatterns(Object obj) {
        List<String> removedPatterns = new ArrayList<>();
        synchronized (m_PatterToContributor) {
            Iterator<Map.Entry<Pattern, Object>> iter = m_PatterToContributor.entrySet().iterator();
            while(iter.hasNext()) {
                Map.Entry<Pattern, Object> entry = iter.next();
                if(entry.getValue().equals(obj)) {
                    removedPatterns.add(entry.getKey().pattern());
                    iter.remove();
                    break;
                }
            }
        }
        m_Log.info(() -> "Removed patterns for " + obj + ": " + removedPatterns);
    }


    @Reference(
            cardinality = ReferenceCardinality.MULTIPLE,
            service = OsgiVaadinTheme.class,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "removeTheme"
    )
    public void addTheme(OsgiVaadinTheme theme) {
        Pattern pattern = toPattern("themes/", theme.getName() +"*");
        synchronized (m_PatterToContributor) {
            m_PatterToContributor.put(pattern, theme);
        }
    }

    public void removeTheme(OsgiVaadinTheme theme) {
        removePatterns(theme);
    }

    @Reference(
            cardinality = ReferenceCardinality.MULTIPLE,
            service = OsgiVaadinWidgetset.class,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "removeWidgetset"
    )
    public void addWidgetset(OsgiVaadinWidgetset widgetset) {
        Pattern pattern = toPattern("widgetsets/", widgetset.getName() + "*");
        synchronized (m_PatterToContributor) {
            m_PatterToContributor.put(pattern, widgetset);
        }
    }

    public void removeWidgetset(OsgiVaadinWidgetset widgetset) {
        removePatterns(widgetset);
    }

    private static Pattern toPattern(String prefix, String name) {
        StringBuilder buf = new StringBuilder();
        buf.append("^/VAADIN/");
        buf.append(prefix);
        buf.append(globToReqExp(name));
        buf.append("$");
        return Pattern.compile(buf.toString());
    }

    private static String globToReqExp(String str) {
        StringBuilder ret = new StringBuilder();

        StringBuilder currentPattern = new StringBuilder();

        for(int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if(c == '*') {
                if(currentPattern.length() > 0) {
                    ret.append(Pattern.quote(currentPattern.toString()));
                    currentPattern.setLength(0);
                }
                ret.append(".*");
            } else {
                currentPattern.append(c);
            }
        }
        if(currentPattern.length() > 0) {
            ret.append(Pattern.quote(currentPattern.toString()));
            currentPattern.setLength(0);
        }
        return ret.toString();
    }

}
