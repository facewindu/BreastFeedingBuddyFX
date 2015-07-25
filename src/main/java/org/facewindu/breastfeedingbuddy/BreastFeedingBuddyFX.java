/**
 * Copyright (c) 2015-2015, François Guillot
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
package org.facewindu.breastfeedingbuddy;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.facewindu.breastfeedingbuddy.platform.PlatformService;

import com.gluonhq.charm.down.common.PlatformFactory;

import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Entry point of the application.
 */
public class BreastFeedingBuddyFX extends Application {

	private BoobsManager boobsManager;
	private Bounds bounds;
	private final static int MARGIN = 36;
	private static final String TITLE = "BreastFeedingBuddyFX";
	public static final String VERSION = "1.0.0";
	private StackPane root;

	@Override
	public void init() {
		System.setOut(new PrintStream(new DevNull()));
	}

	private class DevNull extends OutputStream {

		@Override
		public void write(int b) throws IOException {
		}

	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		boobsManager = new BoobsManager();
		bounds = boobsManager.getLayoutBounds();
		root = new StackPane(boobsManager);
		// root.getStyleClass().addAll("game-root");
		ChangeListener<Number> resize = ((obs, oldValue, newValue) -> gameResize());
		root.widthProperty().addListener(resize);
		root.heightProperty().addListener(resize);

		final Scene scene;
		if (PlatformFactory.getPlatform().getName().equals(PlatformFactory.ANDROID)) {
			Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
			scene = new Scene(root, visualBounds.getWidth(), visualBounds.getHeight());
		} else {
			scene = new Scene(root);
		}
		// scene.getStylesheets().add(getClass().getResource("game.css").toExternalForm());

		if (PlatformFactory.getPlatform().getName().equals(PlatformFactory.DESKTOP) && isARMDevice()) {
			primaryStage.setFullScreen(true);
			primaryStage.setFullScreenExitHint("");
		}

		if (Platform.isSupported(ConditionalFeature.INPUT_TOUCH)) {
			scene.setCursor(Cursor.NONE);
		}

		if (PlatformFactory.getPlatform().getName().equals(PlatformFactory.DESKTOP)) {
			Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
			double factor = Math.min(visualBounds.getWidth() / (bounds.getWidth() + MARGIN),
					visualBounds.getHeight() / (bounds.getHeight() + MARGIN));
			primaryStage.setMinWidth(bounds.getWidth() / 2d);
			primaryStage.setMinHeight(bounds.getHeight() / 2d);
			primaryStage.setWidth((bounds.getWidth() + MARGIN) * factor);
			primaryStage.setHeight((bounds.getHeight() + MARGIN) * factor);
		}
		PlatformService.getInstance().stopProperty().addListener(((obs, oldValue, newValue) -> {
			if (newValue) {
				boobsManager.save();
				PlatformService.getInstance().exit();
			}
		}));

		primaryStage.setTitle(TITLE);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void gameResize() {
		bounds = boobsManager.getLayoutBounds();
		double scale = Math.min((root.getWidth() - MARGIN) / bounds.getWidth(),
				(root.getHeight() - MARGIN) / bounds.getHeight());
		boobsManager.setScale(scale);

		boobsManager.setLayoutX((root.getWidth() - bounds.getWidth()) / 2d);
		boobsManager.setLayoutY((root.getHeight() - bounds.getHeight()) / 2d);
	}

	private boolean isARMDevice() {
		return System.getProperty("os.arch").toUpperCase().contains("ARM");
	}

	@Override
	public void stop() {
		boobsManager.save();
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
