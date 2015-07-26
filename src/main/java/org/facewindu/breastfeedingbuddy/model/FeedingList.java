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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.facewindu.breastfeedingbuddy.BoobsManager;
import org.facewindu.breastfeedingbuddy.listener.FeedDeleteListener;
import org.facewindu.breastfeedingbuddy.listener.FeedEditListener;

import javafx.collections.ListChangeListener;
import javafx.collections.ModifiableObservableListBase;

public class FeedingList extends ModifiableObservableListBase<Feed>implements ListChangeListener<Feed> {

	private final List<Feed> backedList;

	private final Map<Feed, FeedDeleteListener> deleteListeners;

	private final Map<Feed, FeedEditListener> editListeners;

	private final BoobsManager boobsManager;

	public FeedingList(BoobsManager aBoobsManager) {
		boobsManager = aBoobsManager;
		backedList = new ArrayList<>();
		deleteListeners = new HashMap<>();
		editListeners = new HashMap<>();
		addListener(this);
	}

	/**
	 * Creates a new feed for the given boob, using {@link LocalDateTime#now()}.
	 * 
	 * @param theBoob
	 */
	public void saveOneFeed(Boob theBoob) {
		add(new Feed(theBoob, LocalDateTime.now()));
	}

	@Override
	public Feed get(int index) {
		return backedList.get(index);
	}

	@Override
	public int size() {
		return backedList.size();
	}

	@Override
	protected void doAdd(int index, Feed element) {
		backedList.add(index, element);
	}

	@Override
	protected Feed doSet(int index, Feed element) {
		return backedList.set(index, element);
	}

	@Override
	protected Feed doRemove(int index) {
		return backedList.remove(index);
	}

	/**
	 * When a Feed is added or removed, create/release the corresponding
	 * listeners for edit / delete actions.
	 */
	@Override
	public void onChanged(Change<? extends Feed> c) {
		c.next();
		// FIXME use forEach when dalvik JVM understands them
		for (Feed f : c.getAddedSubList()) {
			FeedEditListener fel = new FeedEditListener(f, boobsManager);
			f.editProperty().addListener(fel);
			editListeners.put(f, fel);
			FeedDeleteListener fdl = new FeedDeleteListener(f, boobsManager);
			f.deleteProperty().addListener(fdl);
			deleteListeners.put(f, fdl);
		}
		// FIXME use forEach when dalvik JVM understands them
		for (Feed f : c.getRemoved()) {
			FeedEditListener fel = editListeners.remove(f);
			if (fel != null) {
				f.editProperty().removeListener(fel);
			}
			FeedDeleteListener fdl = deleteListeners.remove(f);
			if (fdl != null) {
				f.deleteProperty().removeListener(fdl);
			}
		}
	}
}
