package com.ariel.universalmachine.ws;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api")
public class WebServiceApplication extends Application {
	
	private Set<Object> singletons = new HashSet<Object>();
	 
    public WebServiceApplication() {
        singletons.add(new ExecutadorContextoService());
    }
 
    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }

}
