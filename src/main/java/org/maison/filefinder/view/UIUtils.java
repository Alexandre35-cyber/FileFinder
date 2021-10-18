package org.maison.filefinder.view;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.text.DecimalFormat;

public class UIUtils {


    private static final long K = 1024;
    private static final long M = K * K;
    private static final long G = M * K;
    private static final long T = G * K;

    public static String convertToStringRepresentation(final long value){
        final long[] dividers = new long[] { T, G, M, K, 1 };
        final String[] units = new String[] { "TB", "GB", "MB", "KB", "B" };
        if(value < 1)
            //throw new IllegalArgumentException("Invalid file size: " + value);
            return "0 KB";
        String result = null;
        for(int i = 0; i < dividers.length; i++){
            final long divider = dividers[i];
            if(value >= divider){
                result = format(value, divider, units[i]);
                break;
            }
        }
        return result;
    }

    private static String format(final long value,
                                 final long divider,
                                 final String unit){
        final double result =
                divider > 1 ? (double) value / (double) divider : (double) value;
        return new DecimalFormat("#,##0.#").format(result) + " " + unit;
    }



    public static JTextField createTextField(int maxSize){
        JTextField txtField = new JTextField();
        AbstractDocument document = (AbstractDocument) txtField.getDocument();

        document.setDocumentFilter(new DocumentFilter(){
            public void replace(FilterBypass fb, int offs, int length,
                                String insertedString, AttributeSet a) throws BadLocationException {
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                currentText += insertedString;
                if ((fb.getDocument().getLength() + insertedString.length()) <= maxSize
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
                if ((fb.getDocument().getLength() + insertedString.length()) <= maxSize
                        && currentText.matches("^[0-9]+$")) {
                    super.insertString(fb, offs, insertedString, a);
                } else {
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        });
        return txtField;
    }


}
