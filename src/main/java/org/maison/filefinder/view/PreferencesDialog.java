package org.maison.filefinder.view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import org.maison.filefinder.model.Preferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PreferencesDialog extends JDialog {
	 private static Logger LOGGER = LoggerFactory.getLogger(PreferencesDialog.class.getName());
    private static String TITLE = "Preferences";

    private static Dimension DIMENSION = new Dimension(400,150);
    private Preferences search;
    private static int MAXSIZE = 4;
    private JTextField txtField;

    public PreferencesDialog(JFrame f, Preferences search){
        super(f, TITLE);
        this.search = search;
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
        JLabel text = new JLabel("Nombre maximal de lignes à afficher:");
        getContentPane().add(text, c);
        txtField = new JTextField();
        txtField.setText(String.valueOf(search.getLignesMax()));
        AbstractDocument document = (AbstractDocument) txtField.getDocument();

        document.setDocumentFilter(new DocumentFilter(){
            public void replace(FilterBypass fb, int offs, int length,
                                String insertedString, AttributeSet a) throws BadLocationException {
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                currentText += insertedString;
                if ((fb.getDocument().getLength() + insertedString.length()) <= MAXSIZE
                        && currentText.matches("^[0-9]+$")) {
                    super.replace(fb, offs, length, insertedString, a);
                } else {
                    Toolkit.getDefaultToolkit().beep();
                }
            }
            public void insertString(FilterBypass fb, int offs, String insertedString,
                                     AttributeSet a) throws BadLocationException {

                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                currentText += insertedString;
                if ((fb.getDocument().getLength() + insertedString.length()) <= MAXSIZE
                        && currentText.matches("^[0-9]+$")) {
                    super.insertString(fb, offs, insertedString, a);
                } else {
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        });

        c.gridx = 1;
        c.weightx = 0.8;
        getContentPane().add(txtField, c);

        JButton buttonValidate = new JButton("Valider");
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0.5;
        buttonValidate.addActionListener(e -> {
        	LOGGER.info("Ok ");
            if (!"".equals(PreferencesDialog.this.txtField.getText())) {
                this.search.setLignesMax(Integer.parseInt(PreferencesDialog.this.txtField.getText()));
                PreferencesDialog.this.setVisible(false);
            }
        });
        getContentPane().add(buttonValidate, c);
        pack();
    }

    public static void main(String[] args) {
        PreferencesDialog dialog = new PreferencesDialog(new JFrame(), new Preferences() {
            @Override
            public void setLignesMax(int lignesMax) {

            }

            @Override
            public int getLignesMax() {
                return 100;
            }
        });
        dialog.setVisible(true);
    }

}
