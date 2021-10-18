package org.maison.filefinder.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.maison.filefinder.model.criteria.PatternSearchCriteria;
import org.maison.filefinder.model.criteria.SearchCriteria;

public class FileSearchServiceTest {

	private FileSearchService service;
	
	@Before
	public void setUp() {
		new File("C:\\Tmp\\essai.txt").delete();
		service = new FileSearchService();
	}
	

	private static void creerFichierSansReelExecutable(String nomFichier, String editorPath) throws IOException {
		Writer fw = new FileWriter(nomFichier);
		fw.append("editor.executable=" + editorPath);
		fw.flush();
		fw.close();
	}
	
	@Test
	public void testFind() throws Exception  {

		TheSearchListener listener = new TheSearchListener();
		service.addResultsListener(listener);
		
		
		service.setFileFilter("essai.txt");
		service.setSearchDirectory("C:\\Tmp");
						
		Assert.assertEquals(0, listener.searchEnded);
		Assert.assertEquals(0, listener.searchStarted);
		Assert.assertNull(listener.lastResults);
		Assert.assertEquals(-1, listener.lastSize);
		
		service.find();
		
		
		Assert.assertEquals(1, listener.searchEnded);
		Assert.assertEquals(1, listener.searchStarted);
		Assert.assertNull(listener.lastResults);
		Assert.assertEquals(-1, listener.lastSize);
		
		listener.reset();
		creerFichierSansReelExecutable("C:\\Tmp\\essai.txt", "path");
		service.find();
		
		Assert.assertEquals(1, listener.searchEnded);
		Assert.assertEquals(1, listener.searchStarted);		
		Assert.assertEquals(1, listener.resultsList.size());		
	}
	
	
	@Test
	public void testPremier() throws Exception {
		service.addCriteria(new SearchCriteria() {

			public String getName() {
				// TODO Auto-generated method stub
				return "TestNotActif";
			}
			 public boolean isActive() {
			        return false;
			 }			
		});
		
		service.addCriteria(new SearchCriteria() {

			public String getName() {
				// TODO Auto-generated method stub
				return "TestInactif";
			}
			 public boolean isActive() {
			        return false;
			 }			
		});
		
		try {
		service.checkCriteria();
		Assert.fail("Devrait planter");
		} catch (FileFinderException e) {
			Assert.assertEquals("Aucun critère de recherche n'a été saisi.", e.getMessage());
		}
		
	
		service.addCriteria(new SearchCriteria() {			

			public String getName() {
				// TODO Auto-generated method stub
				return "TestActif";
			}
			 public boolean isActive() {
			        return true;
			 }			
		});
	
		service.checkCriteria();					
	}

	@Test
	public void testRechercheAvecPbDeLignes() throws Exception {
		service.addCriteria(new PatternSearchCriteria() {
			@Override
			public String getPattern() {
				return "Urban";
			}

			@Override
			public boolean isRegexpEnabled() {
				return false;
			}

			public boolean isActive(){
				return true;
			}
		});

		TheTestListener l = new TheTestListener();
		service.addResultsListener(l);
		service.searchWithFile("C:\\Tmp", "Bookmarks.bak");
		Assert.assertTrue(l.check("C:\\Tmp\\Bookmarks.bak"));
	}

}
