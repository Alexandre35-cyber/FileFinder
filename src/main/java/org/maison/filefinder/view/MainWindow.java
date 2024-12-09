package org.maison.filefinder.view;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;

import org.maison.filefinder.model.FileFinderException;
import org.maison.filefinder.model.FileOpener;
import org.maison.filefinder.model.Preferences;
import org.maison.filefinder.model.ResultLine;
import org.maison.filefinder.model.SearchEngine;
import org.maison.filefinder.model.SearchListener;
import org.maison.filefinder.model.TextualSearch;
import org.maison.filefinder.model.UserSelectionMgr;
import org.maison.filefinder.model.criteria.SearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainWindow extends JFrame implements SearchListener, TextualSearch, Preferences {
	 private static Logger LOGGER = LoggerFactory.getLogger(MainWindow.class.getName());
    // An instance of the private subclass of the default highlight painter
    Highlighter.HighlightPainter myHighlightPainter = new MyHighlightPainter(Color.yellow);
    private boolean ascending = false;
    private static final String TITLE = "File Finder";
    private static final Dimension DIMENSION = new Dimension(800, 600);
    private static int MAX_RESULTS = 100;
    private java.util.List<ResultLine> lines = new ArrayList<ResultLine>();
    private JTextField textFieldDirectory;
    private JTextField textFieldFilter;
    private JEditorPane area;
    private SearchEngine engine;
    private StringBuilder builder = new StringBuilder();
    private ActionListener listener;
    private int cpt = -1;
    private boolean warningShown = false;
    private boolean endForced = false;
    private JScrollPane pane;
    private JPanel filterPanel;
    private JPanel directoryPanel;
    private JPanel resultsPanel;
    private SearchDialog dialog;
    private int lignesMax = MAX_RESULTS;
    int pos = 0;
    int nbOccurences = 0;
    private FileOpener opener;
    private PreferencesDialog prefsDialog;

    public MainWindow(SearchEngine engine, FileOpener opener) {
        super(TITLE);
        this.opener = opener;
        this.engine = engine;
        setPreferredSize(DIMENSION);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        JMenu menuFichier = new JMenu("Fichier");
        JMenuItem menuItemRechecher = new JMenuItem("Rechercher");
        menuItemRechecher.addActionListener(e -> {
            MainWindow.this.setDialogVisible();
        });
        menuFichier.add(menuItemRechecher);


        JMenu menuPreferences = new JMenu("Préferences");
        JMenuItem itemPreferences = new JMenuItem("Définir");
        itemPreferences.addActionListener(e -> {
            MainWindow.this.setPreferencesDialogVisible();
        });
        menuPreferences.add(itemPreferences);

        menuBar.add(menuFichier);
        menuBar.add(menuPreferences);

        setJMenuBar(menuBar);

        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 10, 5, 10);
        filterPanel = createFilterZone();
        getContentPane().add(filterPanel, c);

        c.gridy = 1;
        c.insets = new Insets(5, 10, 10, 10);
        directoryPanel = createDirectoryZone();
        getContentPane().add(directoryPanel, c);

        c.gridy = 2;

        getContentPane().add(createGraphicalCriterias(), c);

        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        resultsPanel = createResultsZone();
        getContentPane().add(resultsPanel, c);
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton button = new JButton("Lancer la recherche");
        button.addActionListener(getActionListener());
        panel.add(button);
        c.gridy = 4;
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        getContentPane().add(panel, c);
        pack();
        center(this);
        this.dialog = new SearchDialog(this, this);
        center(this.dialog);
        this.prefsDialog = new PreferencesDialog(this, this);
        center(this.prefsDialog);
    }


    public void doClick(String filepath) throws MalformedURLException {
        this.area.fireHyperlinkUpdate(new HyperlinkEvent(this, HyperlinkEvent.EventType.ACTIVATED, URI.create(filepath).toURL()));
    }

    private JPanel createGraphicalCriterias() {
        int cpty = 0;
        JPanel criteriaPanel = new JPanel();
        criteriaPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = cpty;
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.BOTH;
        // c.insets = new Insets(10,10,10,10);

        GraphicalSearchCriteriaFactory f = new GraphicalSearchCriteriaFactory();
        for (SearchCriteria criteria : engine.getCriterias()) {
            try {
                criteria.accept(f);
            } catch (Exception e) {
                e.printStackTrace();
            }
            JPanel p = f.getPanel();
            p.setBorder(BorderFactory.createEtchedBorder());
            criteriaPanel.add(p, c);
            cpty++;
            c.gridy = cpty;
        }

        criteriaPanel.setBorder(BorderFactory.createTitledBorder("Critères"));
        return criteriaPanel;
    }

    private void center(Container component) {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (dim.width - component.getPreferredSize().width) / 2;
        int y = (dim.height - component.getPreferredSize().height) / 2;
        component.setLocation(x, y);
    }

    @Override
    public void search(String text) {
        if ("".equals(text) || text == null) return;
        highlight(area, text);
    }

    @Override
    public void reset() {
        area.setText("");
    }


    public void display() {
        setVisible(true);
    }

    public void setDialogVisible() {
        this.dialog.setVisible(true);
    }

    public void setPreferencesDialogVisible() {
        this.prefsDialog.setVisible(true);
    }

    private JPanel createResultsZone() {
        JPanel p = new JPanel();
        p.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        area = new JEditorPane();
        area.addMouseListener(new SearchMouseAdapter(this));
        area.setEditable(false);

        area.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {

                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                        try {
                            if (e.getURL().toString().contains("file-sort")) {

                                MainWindow.this.sortResultsFile(!MainWindow.this.ascending);
                                MainWindow.this.refresh();
                                MainWindow.this.ascending = !MainWindow.this.ascending;
                                return;
                            }
                            if (e.getURL().toString().contains("date-sort")) {
                                MainWindow.this.sortResultsDate(!MainWindow.this.ascending);
                                MainWindow.this.refresh();
                                MainWindow.this.ascending = !MainWindow.this.ascending;
                                return;
                            }
                            if (e.getURL().toString().contains("size-sort")) {
                                MainWindow.this.sortResultsSize(!MainWindow.this.ascending);
                                MainWindow.this.refresh();
                                MainWindow.this.ascending = !MainWindow.this.ascending;
                                return;
                            }
                            //Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+ e.getURL());                            

                            MainWindow.this.opener.open(e.getURL());
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        });
        area.setContentType("text/html");
        pane = new JScrollPane(area);
        p.add(pane, c);
        return p;
    }

    private void sortResultsFile(boolean ascending) {
        lines.sort(new Comparator<ResultLine>() {
            @Override
            public int compare(ResultLine o1, ResultLine o2) {
                if (ascending) {
                    return o1.getFile().compareTo(o2.getFile());
                }
                return o2.getFile().compareTo(o1.getFile());
            }
        });

    }

    private void sortResultsDate(boolean ascending) {
        lines.sort((o1, o2) -> {
            return ascending ? o1.getDate().compareTo(o2.getDate()) : o2.getDate().compareTo(o1.getDate());
        });

    }

    private void sortResultsSize(boolean ascending) {
        lines.sort((o1, o2) -> {
            if (o1.getSize() < o2.getSize()) {
                return ascending ? -1 : 1;
            }
            if (o1.getSize() > o2.getSize()) {
                return ascending ? 1 : -1;
            }
            if (o1.getSize() == o2.getSize()) {
                return 0;
            }
            return -1;
        });

    }
    private JPanel createDirectoryZone(){
        JPanel p = new JPanel();
        p.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;

        c.fill = GridBagConstraints.HORIZONTAL;
        JLabel label = new JLabel("Entrer le reprtoire de recherche:");
        c.weightx = 0.0;
        p.add(label, c);
        c.insets = new Insets(0,5,0,0);
        textFieldDirectory = new JTextField();

        //textFieldDirectory.addActionListener(getActionListener());
        textFieldDirectory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (!validatePath(textFieldDirectory.getText())){
                    return;
                }
                File file = new File(textFieldDirectory.getText());
                if (file.isDirectory() && file.exists()) {
                	LOGGER.info("Repertoire entré: " + file.getAbsolutePath());
                    UserSelectionMgr.get().setDirectorySelected(textFieldDirectory.getText());
                }else{
                    JOptionPane.showMessageDialog(MainWindow.this, "Le répertoire " + file.getAbsolutePath() + " n'existe pas." , "Directory does not exist", JOptionPane.ERROR_MESSAGE);
                }
            }});

        c.weightx = 1;
        c.gridx = 1;
        p.add(textFieldDirectory, c);
        p.setBorder(BorderFactory.createEtchedBorder());

        JButton button = new JButton("...");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = chooser.showOpenDialog(MainWindow.this);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    if (validatePath(file.getAbsolutePath())) {
                        textFieldDirectory.setText(file.getAbsolutePath());
                        UserSelectionMgr.get().setDirectorySelected(textFieldDirectory.getText());
                    }else{
                        textFieldDirectory.setText("");
                        UserSelectionMgr.get().setDirectorySelected(null);
                    }

                }

            }
        });


        c.weightx = 0.1;
        c.gridx = 2;
        c.insets = new Insets(2,5,2,0);
        p.add(button, c);

        return p;
    }


    private boolean validatePath(String filepath){
        String regexp = "^(.*)[ ](.*)$";
        LOGGER.info("TEXT:" + filepath);
        if (filepath.matches(regexp)){
            JOptionPane.showMessageDialog(null,
                    "Les répertoires  un espace ne sont pas autorisés.",
                    "Espace dans le nom d'un répertoire", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private ActionListener getActionListener() {
        if (listener == null) {
            listener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (checkDirectory()) {
                        if (checkFileFilter()) {
                   
                            launchSearch();
                        } else {
                        	LOGGER.info("bad filefilter");
                        }
                    } else {
                    	LOGGER.info("bad directory");
                    }
                }
            };
        }
        return listener;
    }

    public void launchSearch() {
        try {
            this.engine.reset();
            area.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            if (textFieldFilter.getText().contains("*.")) {
                this.engine.searchWithExtension(textFieldDirectory.getText(), textFieldFilter.getText());
            } else {
                this.engine.searchWithFile(textFieldDirectory.getText(), textFieldFilter.getText());
            }


        } catch (FileFinderException e) {
            JOptionPane.showMessageDialog(MainWindow.this, e.getMessage());
        }
    }

    public void searchStarted() {
        lines.clear();
        cpt = -1;
        warningShown = false;
        endForced = false;
        builder = new StringBuilder();
        area.setText("<html><b>" + DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss").format(LocalDateTime.now()) + "-  Recherche en cours...</b>\n<html>");
    }

    @Override
    public void searchEnded() {
        if (!endForced) {
            String text = area.getText();
            text = text.replaceAll("<html>", "");
            text = text.replaceAll("</html>", "");
            text = text.replaceAll("</body>", "");
            String finTable = "</table></center>";
            String textFinal = "<html>" + text + builder.toString() + (builder.toString().contains("<table") ? finTable : "") + "<br><b>" + DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss").format(LocalDateTime.now()) + "-  Recherche terminée.</b></body></html>";
            area.setText(textFinal);
            LOGGER.info(textFinal);
            updateUI();
            LOGGER.info(">>>>>>>>>>>>>>>>>>MAINWINDOW END<<<<<<<<<<<<<<<<<");
            area.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

        }
    }

    public void addResult(String results, long size, String date) {
        date = date.replaceAll("T", " ");
        if (date.indexOf('.') != -1) {
            date = date.substring(0, date.indexOf('.'));
        } else {
            date = date.substring(0, date.indexOf('Z'));
        }
        cpt++;
        ResultLine line;
        if (cpt == 0) {
            builder.append("<center><table border=\"1\"><tr><th><a href=file:///file-sort>Fichier</a></th><a href=file:///date-sort>Date</a></th><th><a href=file:///size-sort>Taille</a></th></tr>");
        }
        String color = (cpt % 2 != 0 ? "style=\"background-color:white;\"" : "style=\"background-color:#ada2a1;\"");
        if (cpt < getLignesMax()) {
            line = new ResultLine(date, results, size);
            lines.add(line);
            //builder.append("<br/><a href=file:///" + results + ">" + results + "</a>&nbsp;"+size);
            builder.append("<tr><td " + color + "><a href=file:///" + line.getFile() + ">" + line.getFile() + "</a></td><td " + color + ">" + line.getDate() + "</td><td " + color + ">" + UIUtils.convertToStringRepresentation(line.getSize()) + "</td></tr>");
        } else {
            if (warningShown == false) {
                JOptionPane.showMessageDialog(null, "Seuls les " + getLignesMax() + " premiers résultats seront affichés.");
                warningShown = true;
                forceEnd();
            }

        }
    }

    private void refresh() {
        area.setText("");
        builder = new StringBuilder();
        builder.append("<html><b>" + DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss").format(LocalDateTime.now()) + "-  Recherche en cours...</b>\n");
        builder.append("<center><table border=\"1\"><tr><th><a href=file:///file-sort>Fichier</a></th><a href=file:///date-sort>Date</a></th><th><a href=file:///size-sort>Taille</a></th></tr>");

        ResultLine line;
        for (int i = 0; i < lines.size(); i++) {
            line = lines.get(i);
            String color = (i % 2 != 0 ? "style=\"background-color:white;\"" : "style=\"background-color:#ada2a1;\"");

            builder.append("<tr><td " + color + "><a href=file:///" + line.getFile() + ">" + line.getFile() + "</a></td><td " + color + ">" + line.getDate() + "</td><td " + color + ">" + UIUtils.convertToStringRepresentation(line.getSize()) + "</td></tr>");
        }
        endForced = false;
        searchEnded();
    }

    private void updateUI() {
        area.update(area.getGraphics());
        area.repaint();
        pane.update(pane.getGraphics());
        pane.repaint();
        filterPanel.update(filterPanel.getGraphics());
        filterPanel.repaint();
        directoryPanel.update(directoryPanel.getGraphics());
        directoryPanel.repaint();
        resultsPanel.update(resultsPanel.getGraphics());
        resultsPanel.repaint();
        MainWindow.this.validate();
    }

    public void forceEnd() {
        endForced = false;
        engine.stopSearch();
        searchEnded();
        endForced = true;
    }


    private boolean checkDirectory() {
        String text = textFieldDirectory.getText();
        if ("".equals(text) || text == null) {
            JOptionPane.showMessageDialog(null, "Vous devez entrer un nom de répertoire existant sur la machine.");
            return false;
        } else {
            File directory = new File(text);
            if (!directory.exists() || !directory.canRead() || !directory.isDirectory()) {
                JOptionPane.showMessageDialog(null, "Verifier l'existence du répertoire '" + text + "' ou les droits d'accès en lecture.");
                return false;
            }
            UserSelectionMgr.get().setDirectorySelected(textFieldDirectory.getText());
            UserSelectionMgr.get().setSelectedExtension(textFieldFilter.getText());
        }
        return true;
    }

    private boolean checkFileFilter() {
        String filterText = textFieldFilter.getText();
        if ("".equals(filterText) || filterText == null) {
            JOptionPane.showMessageDialog(null, "Vous devez entrer une extension de fichier (Ex:*.jpg)");
            return false;
        } else {
            if (filterText.contains("*")) {
                if (!filterText.matches("\\*\\.(([A-Za-z0-9]+)|([\\.\\*]))")) {
                    JOptionPane.showMessageDialog(null, "Vous devez entrer une extension de fichier au format '*.ABC(D)' ou '*.abc(d)'");
                    return false;
                }
            }
        }
        return true;
    }

    public void resetTextualSearch() {
        // First remove all old highlights
        removeHighlights(area);
        pos = 0;
        nbOccurences = 0;
    }

    private JPanel createFilterZone() {
        JPanel p = new JPanel();
        p.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.weighty = 0.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        JLabel label = new JLabel("Entrer l'extension de fichiers ou un nom de fichier:");
        c.weightx = 0.0;
        p.add(label, c);
        c.insets = new Insets(0, 5, 0, 0);
        textFieldFilter = new JTextField();
        textFieldFilter.addActionListener(getActionListener());
        c.weightx = 1.0;
        p.add(textFieldFilter, c);
        p.setBorder(BorderFactory.createEtchedBorder());
        return p;
    }

    @Override
    public void setLignesMax(int lignesMax) {
        this.lignesMax = lignesMax;
    }

    @Override
    public int getLignesMax() {
        return this.lignesMax;
    }


    // A private subclass of the default highlight painter
    class MyHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {
        public MyHighlightPainter(Color color) {
            super(color);
        }
    }


    // Creates highlights around all occurrences of pattern in textComp
    public void highlight(JTextComponent textComp, String pattern) {
        // First remove all old highlights
        try {
            Highlighter hilite = textComp.getHighlighter();
            Document doc = textComp.getDocument();
            String text = doc.getText(0, doc.getLength());
            //int pos = 0;

            // Search for pattern
            // see I have updated now its not case sensitive
            //while ((pos = text.toUpperCase().indexOf(pattern.toUpperCase(), pos)) >= 0)
            if ((pos = text.indexOf(pattern, pos)) >= 0) {
                // Create highlighter using private painter and apply around pattern
                hilite.addHighlight(pos, pos + pattern.length(), myHighlightPainter);
                area.setCaretPosition(pos + 1);
                pos += pattern.length();
                nbOccurences++;
            } else {
                pos = 0;
                JOptionPane.showMessageDialog(this, nbOccurences + " occurences de la chaîne '" + pattern + "' trouvées.");
                nbOccurences = 0;
            }
        } catch (BadLocationException e) {
        }

    }

    // Removes only our private highlights
    public void removeHighlights(JTextComponent textComp) {
        Highlighter hilite = textComp.getHighlighter();
        Highlighter.Highlight[] hilites = hilite.getHighlights();
        for (int i = 0; i < hilites.length; i++) {
            if (hilites[i].getPainter() instanceof MyHighlightPainter) {
                hilite.removeHighlight(hilites[i]);
            }
        }
    }

    }