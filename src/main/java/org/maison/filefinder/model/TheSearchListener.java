package org.maison.filefinder.model;

import java.util.ArrayList;
import java.util.List;

public class TheSearchListener implements SearchListener{

	public int searchStarted = 0;
	public int searchEnded = 0;
	public String lastResults = null;
	public long lastSize = -1;
	
	public List<String> resultsList = new ArrayList<>();
	
	@Override
	public void searchStarted() {
		// TODO Auto-generated method stub
		searchStarted++;
	}

	@Override
	public void searchEnded() {
		// TODO Auto-generated method stub
		searchEnded++;
	}

	@Override
	public void addResult(String results, long size, String date) {
		// TODO Auto-generated method stub
		resultsList.add(new String(results+"-"+size));
		lastResults = results;
		lastSize = size;
	}

	@Override
	public void reset() {
		searchStarted = 0;
		searchEnded = 0;
		lastResults = null;
		lastSize = -1;
	}

}
