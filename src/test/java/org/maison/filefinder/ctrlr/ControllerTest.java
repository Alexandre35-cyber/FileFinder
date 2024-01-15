package org.maison.filefinder.ctrlr;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.maison.filefinder.model.TheSearchListener;
import org.maison.filefinder.view.UIUtils;

public class ControllerTest {
	
	private Controller ctrlr;
	
	@Before
	public void setUp() throws Exception {
		this.ctrlr = new Controller();
		new File("c:\\Tmp\\test").mkdirs();

		flushDirectory("C:\\Tmp\\test\\");
	}

	@Test
	public void testConfiguration() throws Exception {
		this.ctrlr.setConfigForTests("c:\\Tmp\\test\\un_fichier_inconnu.txt");
		try {		
			this.ctrlr.checkEditor();
			Assert.fail("Devrait planter");
		} catch (Exception e) {
			Assert.assertEquals(e.getMessage(), 
					"c:\\Tmp\\test\\un_fichier_inconnu.txt (Le fichier spécifié est introuvable)");
		}
		this.ctrlr.setConfigForTests(null);
		this.ctrlr.checkEditor();
	}
	
	@Test
	public void testOuvrirUnFichier() throws Exception {		
		try {		
			this.ctrlr.checkEditor();
		} catch (Exception e) {			
		}	
			
		this.ctrlr.open("D:\\Projets\\JAVA\\ARKEA\\VENDREDI\\OperationServiceBack\\Dockerfile");
	}
	
	@Test
	public void testConfigurationPasDexecutable() throws Exception {
		String nomFic = "c:\\Tmp\\test\\config.prop";
		String editorPath = "toto.exe";
		ControllerTest.creerFichierSansReelExecutable(nomFic, editorPath);
		this.ctrlr.setConfigForTests(nomFic);
		try {
		this.ctrlr.checkEditor();
		} catch (Exception e) {
			Assert.assertEquals( "Editor '" + editorPath +"' not found.", e.getMessage());
		}
		nomFic = "c:\\Tmp\\test\\config-vide.prop";
		ControllerTest.creerFichierConfigurationVide(nomFic);
		this.ctrlr.setConfigForTests(nomFic);
		try {
			this.ctrlr.checkEditor();
		} catch (Exception e) {
			Assert.assertEquals( "No editor configured.", e.getMessage());
		}
		
	}

	@Test
	public void testNoEditorConfigured() throws Exception {
		String nomFic = "c:\\Tmp\\test\\config-no-explorer.prop";
		ControllerTest.creerFichierSansReelExecutable(nomFic,"C:/Program Files/Notepad++/notepad++.exe");

		this.ctrlr.setConfigForTests(nomFic);
		try {
			this.ctrlr.checkEditor();
		} catch (Exception e) {
			Assert.assertEquals( "No explorer configured.", e.getMessage());
		}
	}

	@Test
	public void testNoExtensions() throws Exception {
		String nomFic = "c:\\Tmp\\test\\config-no-ext.prop";
		ControllerTest.creerFichierSansExt(nomFic);
		this.ctrlr.setConfigForTests(nomFic);
		try {
			this.ctrlr.checkEditor();
			Assert.fail("Devrait planter.");
		} catch (Exception e) {
			Assert.assertEquals( "No extensions.forbidden field configured.", e.getMessage());
		}

		nomFic = "c:\\Tmp\\test\\config-ext-vides.prop";
		ControllerTest.creerFichierExtVides(nomFic);
		this.ctrlr.setConfigForTests(nomFic);
		this.ctrlr.checkEditor();
	}

	@Test
	public void testOuvrirFichier() throws Exception {
		String fic = "c:\\Tmp\\test\\mon_fichier.txt";
		Writer fw = new FileWriter(fic);
		fw.append("du texte");
		fw.flush();
		fw.close();
		this.ctrlr.checkEditor();
		this.ctrlr.open(fic);
	}
	

	private void flushDirectory(String directory){
		File dir = new File(directory);
		File[] files = dir.listFiles();
		for (File f: files){
			if (f.getName().endsWith(".txt")){
				f.delete();
			}
		}
	}

	@Test
	public void testRecherche() throws Exception {

		Writer fw = new FileWriter("c:\\Tmp\\test\\MyBookmarks.bak");
		fw.append("Urban");
		fw.flush();
		fw.close();

		String fic = "c:\\Tmp\\test\\mon_fichier.txt";
		fw = new FileWriter(fic);
		fw.append("du texte");
		fw.flush();
		fw.close();

		TheSearchListener listener = new TheSearchListener();
		Assert.assertEquals(0, listener.searchEnded);
		Assert.assertEquals(0, listener.searchStarted);
		Assert.assertNull(listener.lastResults);
		Assert.assertEquals(-1, listener.lastSize);
		
		
		this.ctrlr.addResultListener(listener);
		this.ctrlr.searchWithExtension("C:\\Tmp\\test", "*.bak");
		Assert.assertEquals(1, listener.searchEnded);
		Assert.assertEquals(1, listener.searchStarted);		
		Assert.assertEquals("C:\\Tmp\\test\\MyBookmarks.bak" , listener.lastResults);
		Assert.assertEquals(new File("c:\\Tmp\\test\\MyBookmarks.bak").length(), listener.lastSize);

		this.ctrlr.searchWithExtension("C:\\Tmp\\test", "*.txt");
		Assert.assertEquals(1, listener.searchEnded);
		Assert.assertEquals(1, listener.searchStarted);
		Assert.assertEquals("C:\\Tmp\\test\\mon_fichier.txt" , listener.lastResults);
		Assert.assertEquals(new File("c:\\Tmp\\test\\mon_fichier.txt").length(), listener.lastSize);
	}
	
	
	@Test
	public void testRechercheAvecExtension() throws Exception {
		creerFichierSansReelExecutable("c:\\Tmp\\test\\mon_fichier1.txt","");
		creerFichierSansReelExecutable("c:\\Tmp\\test\\mon_fichier2.txt","");
		creerFichierSansReelExecutable("c:\\Tmp\\test\\mon_fichier3.txt","");
		
		TheSearchListener listener = new TheSearchListener();
		Assert.assertEquals(0, listener.searchEnded);
		Assert.assertEquals(0, listener.searchStarted);
		Assert.assertNull(listener.lastResults);
		Assert.assertEquals(-1, listener.lastSize);
		
		
		this.ctrlr.addResultListener(listener);
		this.ctrlr.searchWithExtension("C:\\Tmp\\test", "*.txt");
		Assert.assertEquals(1, listener.searchEnded);
		Assert.assertEquals(1, listener.searchStarted);		
		Assert.assertEquals(3, listener.resultsList.size());
	}

	@Test
	public void testSearchWithFile() throws Exception {
		creerFichierSansReelExecutable("c:\\Tmp\\test\\ASingleFile.txt","");
		TheSearchListener listener = new TheSearchListener();
		Assert.assertEquals(0, listener.searchEnded);
		Assert.assertEquals(0, listener.searchStarted);
		Assert.assertNull(listener.lastResults);
		Assert.assertEquals(-1, listener.lastSize);

		this.ctrlr.addResultListener(listener);
		this.ctrlr.searchWithFile("C:\\Tmp\\test", "ASingleFile.txt");
		Assert.assertEquals(1, listener.searchEnded);
		Assert.assertEquals(1, listener.searchStarted);
		Assert.assertEquals(1, listener.resultsList.size());
	}

	@Test
	public void testRechercheSansFichiersDisponibles() throws Exception {		 	
		
		TheSearchListener listener = new TheSearchListener();
		Assert.assertEquals(0, listener.searchEnded);
		Assert.assertEquals(0, listener.searchStarted);
		Assert.assertNull(listener.lastResults);
		Assert.assertEquals(-1, listener.lastSize);
		
		this.ctrlr.addResultListener(listener);
		this.ctrlr.searchWithExtension("C:\\Tmp\\test", "*.zip");
		Assert.assertEquals(1, listener.searchEnded);
		Assert.assertEquals(1, listener.searchStarted);		
		Assert.assertEquals(0, listener.resultsList.size());				
	}
	
	
	  @Test
	  public void testGlob() throws IOException {
		    final Path file1 = Paths.get("C:/java/test/test.java");
		    final Path file2 = Paths.get("C:/java/test/test.txt");
		    final Path file3 = file1.getFileName();
		    
		    String pattern = "glob:**/*.{java,class}";
		    System.out.println("Pattern " + pattern);
		    
		    PathMatcher matcher = FileSystems.getDefault().getPathMatcher(pattern);
		    System.out.println(file1 + " " + matcher.matches(file1));
		    System.out.format("%-22s %b\n", file2, matcher.matches(file2));
		    System.out.format("%-22s %b\n", file3, matcher.matches(file3));
		    System.out.println("");
		    
		    pattern = "glob:*.java";
		    System.out.println("Pattern " + pattern);
		    matcher = FileSystems.getDefault().getPathMatcher(pattern);
		    System.out.println(file1 + " " + matcher.matches(file1));
		    System.out.format("%-22s %b\n", file3, matcher.matches(file3));
	  }
	
	
	  @Test
	  public void testCreateTempDirectory() throws IOException {
		  Path repertoireTemp = Files.createTempDirectory(null);
		  System.out.println(repertoireTemp);
		  repertoireTemp = Files.createTempDirectory("monApp_");
		  System.out.println(repertoireTemp);
		}
	  
	  
	private static void creerFichierSansReelExecutable(String nomFichier, String editorPath) throws IOException {
		Writer fw = new FileWriter(nomFichier);
		fw.append("editor.executable=" + editorPath);
		fw.flush();
		fw.close();
	}

	private static void creerFichierConfigurationVide(String nomFichier) throws IOException {
		Writer fw = new FileWriter(nomFichier);
		fw.flush();
		fw.close();
	}


	public static void creerFichierSansExt(String nomFichier) throws IOException {
		Writer fw = new FileWriter(nomFichier);
		fw.append("editor.executable=C:/Program Files/Notepad++/notepad++.exe\n");
		fw.append("explorer.path=C:/Windows/explorer.exe\n");
		fw.flush();
		fw.close();
	}


	public static void creerFichierExtVides(String nomFichier) throws IOException {
		Writer fw = new FileWriter(nomFichier);
		fw.append("editor.executable=C:/Program Files/Notepad++/notepad++.exe\n");
		fw.append("explorer.path=C:/Windows/explorer.exe\n");
		fw.append("extensions.forbidden=\n");
		fw.flush();
		fw.close();
	}
}
