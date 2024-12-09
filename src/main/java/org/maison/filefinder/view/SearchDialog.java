package org.maison.filefinder.view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import org.maison.filefinder.model.TextualSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SearchDialog extends JDialog {

	private static Logger LOGGER = LoggerFactory.getLogger(SearchDialog.class.getName());
	
    private static String TITLE = "Rechercher";

    private static Dimension DIMENSION = new Dimension(400,150);

    public SearchDialog(JFrame f, TextualSearch search){
        super(f, TITLE);
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        Insets insets = new Insets(10,10,10,10);
        getContentPane().setLayout(new GridBagLayout());
        setPreferredSize(DIMENSION);
        setMinimumSize(DIMENSION);
        setMaximumSize(DIMENSION);
        setResizable(false);
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.2;
        c.weighty = 0.2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = insets;
        JLabel text = new JLabel("Rechercher");
        getContentPane().add(text, c);
        JTextField txtField = new JTextField();
        c.gridx = 1;
        c.weightx = 0.8;
        getContentPane().add(txtField, c);
        JButton button = new JButton("Trouver");
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0.5;
        button.addActionListener(e -> {
        	LOGGER.info("Search " + txtField.getText());
            search.search(txtField.getText());
        });

        txtField.addActionListener(e -> {
            search.search(txtField.getText());
        });

        txtField.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                search.resetTextualSearch();
            }
        });

        getContentPane().add(button, c);
        JButton buttonReset = new JButton("Effacer");
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0.5;
        buttonReset.addActionListener(e -> {
        	LOGGER.info("Effacer ");
            search.resetTextualSearch();
        });
        getContentPane().add(buttonReset, c);
        pack();
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setPreferredSize(new Dimension(800,600));
        f.pack();
        f.setVisible(true);
        SearchDialog dialog = new SearchDialog(f, new TextualSearch() {
            @Override
            public void search(String text) {
            	LOGGER.info("SearchDialog::Recherche de:" + text);
            }

            @Override
            public void resetTextualSearch() {
            	LOGGER.info("SearchDialog::Reset");
            }
        });
        dialog.setVisible(true);
    }

}
