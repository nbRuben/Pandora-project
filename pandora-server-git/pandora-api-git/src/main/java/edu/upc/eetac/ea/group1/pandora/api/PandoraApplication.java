package edu.upc.eetac.ea.group1.pandora.api;

import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class PandoraApplication extends ResourceConfig {
	public PandoraApplication() {
		super();
		register(DeclarativeLinkingFeature.class);
	}
}