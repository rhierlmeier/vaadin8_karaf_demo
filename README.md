Demo for deploying a simple Vaadin 8 application in Apache Karaf 4
==================================================================

I tried in the beginning the vaadin-osgi-integration. However it is broken.
See http://karaf.922171.n3.nabble.com/Vaadin-8-application-on-Karaf-4-3-1-td4059899.html.

Installation on Karaf 4:

```bash
mvn install
```

Login in into the Karaf console and execute
```bash
karaf@root()> feature:repo-add mvn:de.rhierlmeier/vaadin8osgi/1.0.0-SNAPSHOT/xml/features
Adding feature url mvn:de.rhierlmeier/vaadin8osgi/1.0.0-SNAPSHOT/xml/features
karaf@root()> feature:install vaadin8osgi
```

Got to the web browser and open http://localhost:8181/ui


