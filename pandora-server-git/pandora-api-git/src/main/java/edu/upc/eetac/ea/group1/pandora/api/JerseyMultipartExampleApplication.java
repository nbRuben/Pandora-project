package edu.upc.eetac.ea.group1.pandora.api;

import java.util.Enumeration;
import java.util.ResourceBundle;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class JerseyMultipartExampleApplication extends ResourceConfig {
	public JerseyMultipartExampleApplication() {
		super();
		register(MultiPartFeature.class);
		ResourceBundle bundle = ResourceBundle.getBundle("application");
		Enumeration<String> keys = bundle.getKeys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			property(key, bundle.getObject(key));
		}
	}
}