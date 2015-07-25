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
package org.facewindu.breastfeedingbuddy.view;

import org.facewindu.breastfeedingbuddy.model.Feed;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * Representation of a {@link Feed} in the {@link ListView}
 */
public class FeedCell extends ListCell<Feed> {
	@Override
	public void updateItem(Feed feed, boolean empty) {
		super.updateItem(feed, empty);
		if (feed == null || empty) {
			setText(null);
			setGraphic(null);
		} else {
			// use only first letter of the boob (for now, should be a nice
			// image in the end)
			Label boobLabel = new Label(feed.getBoob().toString().substring(0, 1));
			Label dateTimeLabel = new Label(feed.getStartFeedingTimeString());
			boobLabel.getStyleClass().addAll("label");
			dateTimeLabel.getStyleClass().addAll("label");
			Button editButton = new Button("", new ImageView(new Image(getClass().getResourceAsStream("edit.png"))));
			editButton.setTooltip(new Tooltip("edit feed info"));
			editButton.setOnAction(evt -> feed.editProperty().set(true));
			Button deleteButton = new Button("",
					new ImageView(new Image(getClass().getResourceAsStream("delete.png"))));
			deleteButton.setOnAction(evt -> feed.deleteProperty().set(true));
			deleteButton.setTooltip(new Tooltip("delete feed"));
			HBox buttonBox = new HBox(2, editButton, deleteButton);
			HBox hFill = new HBox();
			HBox.setHgrow(hFill, Priority.ALWAYS);
			HBox hbox = new HBox(10, boobLabel, dateTimeLabel, hFill, buttonBox);
			hbox.setAlignment(Pos.CENTER);
			setGraphic(hbox);
		}
	}
}
