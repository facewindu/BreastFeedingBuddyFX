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
package org.facewindu.breastfeedingbuddy.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Feed implements Comparable<Feed> {
	/** formatter for persistence. */
	private static final DateTimeFormatter PERSISTENCE_DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
			.appendPattern("yyyyMMddHHmm").toFormatter();

	/** formatter for pretty print. */
	private static final DateTimeFormatter PRETTY_PRINT_DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
			.appendPattern("dd MM yyyy  | HH:mm").toFormatter();

	private final Boob boob;

	private final LocalDateTime startFeedingTime;

	private final BooleanProperty editProperty;

	private final BooleanProperty deleteProperty;

	public Feed(final Boob aBoob, final LocalDateTime aDate) {
		boob = aBoob;
		startFeedingTime = aDate;
		editProperty = new SimpleBooleanProperty(false);
		deleteProperty = new SimpleBooleanProperty(false);
	}

	public Feed(final String fromLine) {
		final String[] splitted = fromLine.split("\\|");
		if (splitted.length != 2) {
			throw new IllegalStateException(fromLine + " is not parseable");
		}
		boob = Boob.valueOf(splitted[0].trim());
		startFeedingTime = LocalDateTime.parse(splitted[1].trim(), PERSISTENCE_DATE_TIME_FORMATTER);
		editProperty = new SimpleBooleanProperty(false);
		deleteProperty = new SimpleBooleanProperty(false);
	}

	public String toLine() {
		return String.format("%s | %s", boob.toString(), startFeedingTime.format(PERSISTENCE_DATE_TIME_FORMATTER));
	}

	public String toLinePretty() {
		return String.format("%s | %s", boob.toString(), startFeedingTime.format(PRETTY_PRINT_DATE_TIME_FORMATTER));
	}

	@Override
	public String toString() {
		return toLine();
	}

	public Boob getBoob() {
		return boob;
	}

	public LocalDateTime getStartFeedingTime() {
		return startFeedingTime;
	}

	public String getStartFeedingTimeString() {
		return startFeedingTime.format(PRETTY_PRINT_DATE_TIME_FORMATTER);
	}

	public BooleanProperty editProperty() {
		return editProperty;
	}

	public BooleanProperty deleteProperty() {
		return deleteProperty;
	}

	@Override
	public int compareTo(Feed other) {
		return other.startFeedingTime.compareTo(this.startFeedingTime);
	}

}
