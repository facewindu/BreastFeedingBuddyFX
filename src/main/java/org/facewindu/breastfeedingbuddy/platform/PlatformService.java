/**
 * Copyright (C) 2013-2015 2048FX 
 * Jose Pereda, Bruno Borges & Jens Deters
 * All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.facewindu.breastfeedingbuddy.platform;

import static java.lang.String.format;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.logging.Logger;

import com.gluonhq.charm.down.common.PlatformFactory;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Singleton allowing to fork behaviour, based on the implemented plateform (at
 * runtime).
 */
public class PlatformService {

	public static final String DESKTOP = "Desktop";
	public static final String ANDROID = "Android";
	public static final String IOS = "iOS";

	private static final Logger LOG = Logger.getLogger(PlatformService.class.getName());

	private static PlatformService instance;

	public static synchronized PlatformService getInstance() {
		if (instance == null) {
			instance = new PlatformService();
		}
		return instance;
	}

	private final ServiceLoader<PlatformProvider> serviceLoader;
	private PlatformProvider provider;

	private PlatformService() {
		serviceLoader = ServiceLoader.load(PlatformProvider.class);
		try {
			Iterator<PlatformProvider> iterator = serviceLoader.iterator();
			while (iterator.hasNext()) {
				if (provider == null) {
					provider = iterator.next();
					LOG.info(format("Using PlatformProvider: %s", provider.getClass().getName()));
				} else {
					LOG.info(format("This PlatformProvider is ignored: %s", iterator.next().getClass().getName()));
				}
			}
		} catch (Exception e) {
			throw new ServiceConfigurationError("Failed to access + ", e);
		}
		if (provider == null) {
			LOG.severe("No PlatformProvider implementation could be found!");
		}
	}

	public BooleanProperty stopProperty() {
		return provider == null ? new SimpleBooleanProperty() : provider.stopProperty();
	}

	public void exit() {
		if (provider != null) {
			provider.exit();
		}
	}

	public void launchURL(String url) {
		try {
			PlatformFactory.getPlatform().launchExternalBrowser(url);
		} catch (IOException | URISyntaxException ex) {
			LOG.severe(ex.getMessage());
		}
	}
}
