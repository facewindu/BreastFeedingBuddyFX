/**
 * Copyright (c) 2015-2015, Fran�ois Guillot
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.facewindu.breastfeedingbuddy.platform;

import com.gluonhq.charm.down.common.Platform;
import com.gluonhq.charm.down.common.PlatformFactory;

import android.util.Log;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafxports.android.FXActivity;

public class AndroidPlatformProvider implements PlatformProvider {

	private final BooleanProperty stop = new SimpleBooleanProperty();

	{
		// For android, change java.io.tmpdir to a android specific cache
		// directory
		Log.v("Provider", "Temp dir");
		System.setProperty("java.io.tmpdir", FXActivity.getInstance().getCacheDir().getAbsolutePath());

		PlatformFactory.getPlatform().setOnLifecycleEvent((Platform.LifecycleEvent param) -> {
			switch (param) {
			case START:
				stop.set(false);
				break;
			case PAUSE:
				stop.set(true);
				break;
			case RESUME:
				stop.set(false);
				break;
			case STOP:
				stop.set(true);
				break;
			}
			return null;
		});

	}

	@Override
	public BooleanProperty stopProperty() {
		return stop;
	}

	@Override
	public void exit() {
		FXActivity.getInstance().finish();
	}
}