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
package org.facewindu.breastfeedingbuddy.listener;

import org.facewindu.breastfeedingbuddy.BoobsManager;
import org.facewindu.breastfeedingbuddy.model.Feed;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class FeedEditListener implements ChangeListener<Boolean> {

	private final Feed feed;

	private final BoobsManager boobsManager;

	public FeedEditListener(Feed aFeed, BoobsManager aBoobsManager) {
		feed = aFeed;
		boobsManager = aBoobsManager;
	}

	@Override
	public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		// feed edit value has changed
		if (newValue) {
			// edit = true
			// display the edition popup
			// Create the custom overlay.

			boobsManager.createEditOverlay(feed);

			// FIXME use commented code when Dalvik JVM handles Alert correctly
			// (Optional class not supported by Dalvik JVM)
			// Dialog<Feed> dialog = new Dialog<>();
			// dialog.setTitle("Feed Edition");
			// dialog.setHeaderText("Edit the feed data");
			//
			// // Set the button types.
			// ButtonType loginButtonType = new ButtonType("Save Edit",
			// ButtonData.OK_DONE);
			// dialog.getDialogPane().getButtonTypes().addAll(loginButtonType,
			// ButtonType.CANCEL);
			//
			// GridPane grid = new GridPane();
			// grid.setHgap(10);
			// grid.setVgap(10);
			// grid.setPadding(new Insets(20, 150, 10, 10));
			//
			// ChoiceBox<Boob> boobBox = new
			// ChoiceBox<>(FXCollections.observableArrayList(Boob.values()));
			// boobBox.getSelectionModel().select(feed.getBoob());
			// // using jfxtras would have been nice. But it's not supported yet
			// // by
			// // javafxports
			// LocalDateTimePicker localdateTimePicker = new
			// LocalDateTimePicker(feed.getStartFeedingTime());
			// // DatePicker datePicker = new
			// // DatePicker(feed.getStartFeedingTime().toLocalDate());
			// // TextField timeFieldHH = new TextField();
			// // timeFieldHH.setPromptText("HH");
			// // TextField timeFieldmm = new TextField();
			// // timeFieldmm.setPromptText("mm");
			//
			// grid.add(new Label("Boob:"), 0, 0);
			// grid.add(boobBox, 1, 0);
			// grid.add(new Label("FeedingTime: "), 0, 1);
			// grid.add(localdateTimePicker, 1, 1);
			// // grid.add(new Label("Feeding Date:"), 0, 1);
			// // grid.add(datePicker, 1, 1);
			// // grid.add(new Label("Feeding Time :"), 0, 2);
			// // grid.add(timeFieldHH, 1, 2);
			// // grid.add(timeFieldmm, 2, 2);
			//
			// // // Enable/Disable login button depending on whether a username
			// // was
			// // // entered.
			// // Node loginButton =
			// // dialog.getDialogPane().lookupButton(loginButtonType);
			// // loginButton.setDisable(true);
			// //
			// // // Do some validation (using the Java 8 lambda syntax).
			// // username.textProperty().addListener((observable, oldValue,
			// // newValue) -> {
			// // loginButton.setDisable(newValue.trim().isEmpty());
			// // });
			//
			// dialog.getDialogPane().setContent(grid);
			//
			// // Request focus on the username field by default.
			// // Platform.runLater(() -> username.requestFocus());
			//
			// // Convert the result to a username-password-pair when the login
			// // button is clicked.
			// dialog.setResultConverter(dialogButton -> {
			// if (dialogButton == loginButtonType) {
			// return new Feed(boobBox.getSelectionModel().getSelectedItem(),
			// localdateTimePicker.getLocalDateTime());
			// // return new
			// // Feed(boobBox.getSelectionModel().getSelectedItem(),
			// //
			// LocalDateTime.from(datePicker.getValue()).withHour(Integer.valueOf(timeFieldHH.getText()))
			// // .withMinute(Integer.valueOf(timeFieldmm.getText())));
			// }
			// return null;
			// });
			//
			// Optional<Feed> result = dialog.showAndWait();
			//
			// if (result.isPresent()) {
			// // if the edit was done
			// // delete the former feed, and recreate a new one
			// list.remove(feed);
			// list.add(result.get());
			// // possibly the list shall be sorted
			// Comparator<Feed> comparator = Comparator.comparing(f ->
			// f.getStartFeedingTime());
			// list.sort(comparator.reversed());
			// } else {
			// feed.editProperty().set(false);
			// }
		}
	}
}
