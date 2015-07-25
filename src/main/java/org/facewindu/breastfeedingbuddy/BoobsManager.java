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

import java.util.Collections;

import org.facewindu.breastfeedingbuddy.model.Boob;
import org.facewindu.breastfeedingbuddy.model.Feed;
import org.facewindu.breastfeedingbuddy.model.FeedingList;
import org.facewindu.breastfeedingbuddy.platform.PlatformService;
import org.facewindu.breastfeedingbuddy.view.BoobButton;
import org.facewindu.breastfeedingbuddy.view.FeedCell;
import org.facewindu.breastfeedingbuddy.view.LeftBoobButton;
import org.facewindu.breastfeedingbuddy.view.RightBoobButton;

import com.gluonhq.charm.down.common.PlatformFactory;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import jfxtras.scene.control.LocalDateTimePicker;

/**
 * Core class of the application.<br>
 * Responsible for displaying the feeds + showing buttons to the user, and
 * managing all events and listeners callback.
 */
public class BoobsManager extends Group {

	public static final int TOOLBAR_HEIGHT = 40;

	public static final int BUTTON_HEIGHT = 120;

	private final VBox vbox;

	private final HBox toolbar;

	private final HBox boobsBox;

	private final BoobButton leftBoob;

	private final BoobButton rightBoob;

	private final ListView<Feed> listView;

	private final FeedingList feedingList;

	private final BooleanProperty overlayProperty;

	private final VBox overlay;

	public BoobsManager() {
		feedingList = new FeedingList(this);
		vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		getChildren().add(vbox);

		// Button box
		leftBoob = new LeftBoobButton(feedingList);
		rightBoob = new RightBoobButton(feedingList);
		boobsBox = new HBox(10, leftBoob, rightBoob);
		boobsBox.setPadding(new Insets(5, 20, 5, 20));
		boobsBox.setAlignment(Pos.CENTER);
		vbox.getChildren().add(boobsBox);

		// ListView
		listView = new ListView<>(feedingList);
		listView.setCellFactory(list -> new FeedCell());
		listView.prefWidthProperty().bind(boobsBox.widthProperty());
		vbox.getChildren().add(listView);

		// Toolbar
		toolbar = new HBox();
		toolbar.setAlignment(Pos.CENTER);
		toolbar.setMinHeight(TOOLBAR_HEIGHT);
		toolbar.setPrefHeight(TOOLBAR_HEIGHT);
		toolbar.setMaxHeight(TOOLBAR_HEIGHT);
		toolbar.setPadding(new Insets(5, 20, 5, 20));
		toolbar.prefWidthProperty().bind(boobsBox.widthProperty());
		vbox.getChildren().add(toolbar);
		Button about = new Button("", new ImageView(new Image(getClass().getResourceAsStream("about.png"))));
		about.setOnAction(evt -> {
			createAboutOverlay();
		});
		Button quit = new Button("", new ImageView(new Image(getClass().getResourceAsStream("quit.png"))));
		quit.setOnAction(evt -> {
			PlatformService.getInstance().stopProperty().set(true);
			PlatformService.getInstance().exit();
		});
		toolbar.getChildren().addAll(about, quit);

		// deal with overlay
		overlayProperty = new SimpleBooleanProperty(false);
		overlay = new VBox();
		overlay.setAlignment(Pos.CENTER);
		overlay.setTranslateY(BUTTON_HEIGHT);
		toolbar.disableProperty().bind(overlayProperty);
		leftBoob.disableProperty().bind(overlayProperty);
		rightBoob.disableProperty().bind(overlayProperty);
		listView.disableProperty().bind(overlayProperty);
		overlayProperty.addListener((ov, oldValue, newValue) -> {
			if (!newValue) {
				getChildren().removeAll(overlay);
			}
		});

		// restore previously saved session
		restoreSession();
	}

	/**
	 * Set scale to adjust overall game size
	 * 
	 * @param scale
	 */
	public void setScale(double scale) {
		setScaleX(scale);
		setScaleY(scale);
	}

	public void save() {
		new SaveManager().save(feedingList);
	}

	public final void restoreSession() {
		feedingList.setAll(new SaveManager().restore(this));
		Collections.sort(feedingList);
	}

	/**
	 * Creates an overlay to edit the given feed.
	 * 
	 * @param feed
	 */
	public void createEditOverlay(Feed feed) {
		VBox vbox = new VBox();
		overlay.getChildren().setAll(vbox);

		ChoiceBox<Boob> boobBox = new ChoiceBox<>(FXCollections.observableArrayList(Boob.values()));
		boobBox.getSelectionModel().select(feed.getBoob());
		LocalDateTimePicker localdateTimePicker = new LocalDateTimePicker(feed.getStartFeedingTime());
		Button save = new Button("Save Edit");
		Button cancel = new Button("Cancel Edit");
		vbox.getChildren().add(new Label("Boob:"));
		vbox.getChildren().add(boobBox);
		vbox.getChildren().add(new Label("FeedingTime: "));
		vbox.getChildren().add(localdateTimePicker);
		vbox.getChildren().add(new HBox(save, cancel));
		save.setOnAction(evt -> {
			feedingList.remove(feed);
			feedingList.add(
					new Feed(boobBox.getSelectionModel().getSelectedItem(), localdateTimePicker.getLocalDateTime()));
			// possibly the list shall be sorted
			// FIXME Use Comparator.comparing when Dalvik JVM is compliant with
			// JDK 1.8 (whenever, wherever)
			// Comparator<Feed> comparator = Comparator.comparing(f ->
			// f.getStartFeedingTime());
			// feedingList.sort(comparator.reversed());
			Collections.sort(feedingList);
			overlayProperty.set(false);
		});
		cancel.setOnAction(evt -> {
			feed.editProperty().set(false);
			overlayProperty.set(false);
		});

		getChildren().remove(overlay);
		getChildren().add(overlay);
		overlayProperty.set(true);
	}

	/**
	 * Creates an overlay to delete the given feed.
	 * 
	 * @param feed
	 */
	public void createDeleteOverlay(Feed feed) {
		VBox vbox = new VBox();
		overlay.getChildren().setAll(vbox);

		Button delete = new Button("Delete Feed");
		Button cancel = new Button("Cancel");

		vbox.getChildren().add(new Label("Are you sure to delete Feed ?"));
		vbox.getChildren().add(new HBox(delete, cancel));

		delete.setOnAction(evt -> {
			feedingList.remove(feed);
			overlayProperty.set(false);
		});
		cancel.setOnAction(evt -> {
			feed.deleteProperty().set(false);
			overlayProperty.set(false);
		});

		getChildren().remove(overlay);
		getChildren().add(overlay);
		overlayProperty.set(true);
	}

	/**
	 * Create about overlay
	 */
	private void createAboutOverlay() {
		VBox vbox = new VBox();
		overlay.getChildren().setAll(vbox);

		Button goBack = new Button("Go Back");
		TextFlow flow = new TextFlow();
		flow.setTextAlignment(TextAlignment.CENTER);
		flow.setPadding(new Insets(10, 0, 0, 0));
		Text t0 = new Text("BreastFedding Buddy FX\n");
		Text t1 = new Text("JavaFX game - " + PlatformFactory.getPlatform().getName() + " version\n\n");

		Text t2 = new Text("Powered by ");
		Hyperlink link1 = new Hyperlink();
		link1.setText("JavaFXPorts");
		link1.setOnAction(e -> {
			PlatformService.getInstance().launchURL("http://javafxports.org/page/home");
		});
		Text t21 = new Text(" Project \n");

		Text t22 = new Text("Powered by ");
		Hyperlink link2 = new Hyperlink();
		link2.setText("JFXTras");
		link2.setOnAction(e -> {
			PlatformService.getInstance().launchURL("http://jfxtras.org/");
		});
		Text t23 = new Text(" Project \n\n");

		Text t24 = new Text("\u00A9 ");
		Hyperlink link3 = new Hyperlink();
		link3.setText("Facewindu");
		link3.setOnAction(e -> {
			PlatformService.getInstance().launchURL("https://github.com/facewindu/");
		});
		Text t25 = new Text("\n\n");

		Text t31 = new Text(" Version " + BreastFeedingBuddyFX.VERSION + " - 2015\n\n");

		flow.getChildren().setAll(t0, t1, t2, link1, t21, t22, link2, t23, t24, link3, t25, t31);
		vbox.getChildren().add(flow);
		vbox.getChildren().add(new HBox(goBack));

		goBack.setOnAction(evt -> {
			overlayProperty.set(false);
		});

		getChildren().remove(overlay);
		getChildren().add(overlay);
		overlayProperty.set(true);
	}
}
