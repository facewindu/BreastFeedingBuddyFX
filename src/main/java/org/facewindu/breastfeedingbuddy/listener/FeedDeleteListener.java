/**
 * Copyright (c) 2015-2015, Francois Guillot
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

public class FeedDeleteListener implements ChangeListener<Boolean> {

	private final Feed feed;

	private final BoobsManager boobsManager;

	public FeedDeleteListener(Feed aFeed, BoobsManager aBoobsManager) {
		feed = aFeed;
		boobsManager = aBoobsManager;
	}

	@Override
	public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		// delete property has changed
		if (newValue) {
			// user asked for a feed deletion
			// display confirmation popup
			boobsManager.createDeleteOverlay(feed);

			// Alert alert = new Alert(AlertType.CONFIRMATION);
			// alert.setTitle("Delete Feed ?");
			// alert.setHeaderText(feed.toLinePretty());
			// alert.setContentText("Are you sure to delete this feed ?");
			//
			// Optional<ButtonType> result = alert.showAndWait();
			// if (result.get() == ButtonType.OK) {
			// list.remove(feed);
			// feed.deleteProperty().set(false);
			// } else {
			// // ... user chose CANCEL or closed the dialog
			// // reset the delete property
			// feed.deleteProperty().set(false);
			// }
		}
	}

}
