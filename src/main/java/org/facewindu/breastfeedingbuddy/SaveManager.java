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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.facewindu.breastfeedingbuddy.model.Feed;
import org.facewindu.breastfeedingbuddy.model.FeedingList;

import com.gluonhq.charm.down.common.PlatformFactory;

/**
 * Save and restore data.
 */
public class SaveManager {

	public final String PROPERTIES_FILENAME = "breastfeeding.save";
	private File path;

	public SaveManager() {
		try {
			path = PlatformFactory.getPlatform().getPrivateStorage();
		} catch (IOException e) {
			String tmp = System.getProperty("java.io.tmpdir");
			path = new File(tmp);
		}
		Logger.getLogger(getClass().getName()).log(Level.INFO, "path: " + path);
	}

	/**
	 * Saves the whole feeding list.<br>
	 * FIXME could be improved to only save each feed when entered
	 * 
	 * @param feedingList
	 *            the feeding list to save
	 */
	public void save(FeedingList feedingList) {
		try {
			final BufferedWriter outputWriter = new BufferedWriter(new FileWriter(new File(path, PROPERTIES_FILENAME)));
			// FIXME use forEach when dalvik JVM understands them
			for (Feed feed : feedingList) {
				try {
					outputWriter.write(feed.toLine());
					outputWriter.newLine();
				} catch (IOException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				}
			}
			outputWriter.flush();
			outputWriter.close();
		} catch (IOException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Restore a previous save, stored in the returned {@link FeedingList}. <br>
	 * An empty list is returned if no previous session is found.
	 * 
	 * @return a feeding list
	 */
	public FeedingList restore(BoobsManager boobsManager) {
		Scanner scanner = null;
		final FeedingList feedingList = new FeedingList(boobsManager);
		try {
			File file = new File(path, PROPERTIES_FILENAME);
			scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				feedingList.add(new Feed(scanner.nextLine()));
			}
		} catch (FileNotFoundException ignored) {
			return feedingList;
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
		return feedingList;
	}

}
