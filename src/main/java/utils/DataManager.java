package utils;

/* DataManager.java
 *
 * Copyright (C) 2017 Kevin Lopez Andrade <kevin@kevlopez.com>
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

import electionresults.model.*;
import electionresults.persistence.io.DataAccessLayer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class DataManager {

	private static DataManager dataManager;
	private List<Integer> yearList = DataAccessLayer.getElectionYears();
	private List<ElectionResults> electionResultList = DataAccessLayer.getAllElectionResults();
	private HashMap<Integer,ElectionResults> map = new HashMap<>(10);
	private List<String> regionsList = new ArrayList<>();

	private DataManager () {

		for(ElectionResults i: electionResultList) {
			map.put(i.getYear(),i);
		}

		Map<String,ProvinceInfo> aux = map.get(2011).getProvinces();
		List<String> auxList = new ArrayList<String>(aux.keySet());
		for (String i : auxList) {
			regionsList.addAll(aux.get(i).getRegions());
		}

	}

	public static DataManager getDataManager () {
		if(dataManager == null) {
			dataManager = new DataManager();
			return dataManager;
		}
		else {
			return dataManager;
		}
	}

	public static List<Integer> getElectionYears() {
		return dataManager.yearList;
	}

	public static List<ElectionResults> getAllElectionsResults () {
		return dataManager.electionResultList;
	}

	public static ElectionResults getElectionResults (int year) {
		return dataManager.map.get(year);
	}

	public static List<String> getAllRegions  () {
		return dataManager.regionsList;
	}

}
