package org.maison.filefinder.view;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import org.jdatepicker.JDatePicker;
import org.maison.filefinder.model.criteria.DateSearchCriteria;
import org.maison.filefinder.model.criteria.DuplicateSearchCriteria;
import org.maison.filefinder.model.criteria.PatternSearchCriteria;
import org.maison.filefinder.model.criteria.SearchCriteria;
import org.maison.filefinder.model.criteria.SearchCriteriaVisitor;
import org.maison.filefinder.model.criteria.SizeSearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphicalSearchCriteriaFactory implements SearchCriteriaVisitor {
    private JPanel panel;
    private static Logger LOGGER = LoggerFactory.getLogger(GraphicalSearchCriteriaFactory.class.getName());
    
    public JPanel getPanel(){
        return panel;
    }


    @Override
    public void visitCriteria(SearchCriteria criteria) {

    }

    @Override
    public void visitSizeCriteria(SizeSearchCriteria criteria) {
        panel = createSizeCriteria(criteria);
    }

    public JPanel createSizeCriteria(SizeSearchCriteria criteria){
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        GridBagConstraints c = new GridBagConstraints();

        JLabel lblMin = new JLabel("Taille min:");
        panel.add(lblMin);
        JTextField txtMax = UIUtils.createTextField(5);
        txtMax.setColumns(5);
        JTextField txtMin = UIUtils.createTextField(5);
        txtMin.setColumns(5);
        final JTextField finalTxt = txtMin;

        txtMax.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                criteria.setActive(e.getDot()>0);
            }
        });

        txtMin.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                criteria.setActive(e.getDot()>0);
            }
        });

        txtMin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    if (!"".equals(finalTxt.getText()) && finalTxt.getText() != null){
                        criteria.setActive(true);
                        criteria.setSizeMin(Integer.parseInt(finalTxt.getText()));
                    }else{
                        if ("".equals(txtMax.getText()) || txtMax.getText() == null){
                            criteria.setActive(false);
                        }

                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
        if (criteria.getSizeMin()>=0) {
            txtMin.setText(String.valueOf(criteria.getSizeMin()));
        }
        panel.add(txtMin);

        JLabel lblMax = new JLabel("Taille max:");
        panel.add(lblMax);

        if (criteria.getSizeMax()>=0) {
            txtMax.setText(String.valueOf(criteria.getSizeMax()));
        }
        txtMax.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (!"".equals(txtMax.getText()) && txtMax.getText() != null) {
                        criteria.setActive(true);
                        criteria.setSizeMax(Integer.parseInt(txtMax.getText()));
                    }else{
                        if ("".equals(finalTxt.getText()) || finalTxt.getText() != null){
                            criteria.setActive(false);
                        }

                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        panel.add(txtMax);


        JPanel pButtons = new JPanel();
        pButtons.setLayout(new FlowLayout(FlowLayout.CENTER));

        JRadioButton buttonKos = new JRadioButton("KOs");
        pButtons.add(buttonKos);

        JRadioButton buttonMos = new JRadioButton("MOs");
        pButtons.add(buttonMos);

        JRadioButton buttonGos = new JRadioButton("GOs");
        pButtons.add(buttonGos);

        ButtonGroup bGroup = new ButtonGroup();
        bGroup.add(buttonKos);
        bGroup.add(buttonMos);
        bGroup.add(buttonGos);

        switch (criteria.getChosenUnit()){
            case KOS:buttonKos.setEnabled(true);
                     buttonKos.doClick();
                LOGGER.info("KOS");
                break;
            case MOS: buttonMos.setEnabled(true);
            	LOGGER.info("MOS");
                break;
            case GOS: buttonGos.setEnabled(true);
            	LOGGER.info("GOS");
                break;
            default: buttonKos.setEnabled(true);
            	LOGGER.info("default");
            break;
        }

        panel.add(pButtons);
        return panel;
    }


    @Override
    public void visitDateCriteria(DateSearchCriteria criteria) {
        this.panel = createDateCriteria(criteria);
    }

    public JPanel createDateCriteria(DateSearchCriteria criteria) {
        JPanel panel = new JPanel(new FlowLayout());
        JLabel dateMinLbl = new JLabel("Date min:");
        JDatePicker pickerMin = new JDatePicker();
        JDatePicker pickerMax = new JDatePicker();
        if (criteria.getMinDate() != null) {
            pickerMin.getFormattedTextField().setText(criteria.getMinDate().toString());
        }
        pickerMin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                criteria.setActive(true);
                GregorianCalendar c = (GregorianCalendar) pickerMin.getModel().getValue();
                LOGGER.info("Date min YYYY:" + c.get(GregorianCalendar.YEAR));
                LOGGER.info("Date min MM:" + (c.get(GregorianCalendar.MONTH)+1));
                LOGGER.info("Date min DD:" + (c.get(GregorianCalendar.DAY_OF_MONTH)));
                try {
                    criteria.setMinDate(LocalDate.of(c.get(GregorianCalendar.YEAR),
                            (c.get(GregorianCalendar.MONTH)+1),
                            (c.get(GregorianCalendar.DAY_OF_MONTH))));
                } catch (Exception e1){
                    JOptionPane.showMessageDialog(null, e1.getMessage());
                    pickerMin.getFormattedTextField().setText("");
                    pickerMax.getFormattedTextField().setText("");
                    criteria.setActive(false);
                }
            }
        });
        JLabel dateMaxLbl = new JLabel("Date max:");

        if (criteria.getMaxDate() != null) {
            pickerMax.getFormattedTextField().setText(criteria.getMaxDate().toString());
        }
        pickerMax.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                criteria.setActive(true);
                GregorianCalendar c = (GregorianCalendar) pickerMax.getModel().getValue();
                LOGGER.info("Date max YYYY:" + c.get(GregorianCalendar.YEAR));
                LOGGER.info("Date max MM:" + (c.get(GregorianCalendar.MONTH)+1));
                LOGGER.info("Date max DD:" + (c.get(GregorianCalendar.DAY_OF_MONTH)));

                try {
                    criteria.setMaxDate(LocalDate.of(c.get(GregorianCalendar.YEAR),
                            (c.get(GregorianCalendar.MONTH)+1),
                            (c.get(GregorianCalendar.DAY_OF_MONTH))));
                } catch (Exception e1){
                    JOptionPane.showMessageDialog(null, e1.getMessage());
                    pickerMin.getFormattedTextField().setText("");
                    pickerMax.getFormattedTextField().setText("");
                    criteria.setActive(false);
                }
            }
        });
        panel.add(dateMinLbl);
        panel.add(pickerMin);
        panel.add(dateMaxLbl);
        panel.add(pickerMax);

        JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                criteria.reset();
                pickerMax.getFormattedTextField().setText("");
                pickerMin.getFormattedTextField().setText("");
                criteria.setActive(false);
            }
        });
        panel.add(reset);
        return panel;
    }

    @Override
    public void visitPatternCriteria(PatternSearchCriteria criteria) {
        this.panel = createPatternCriteria(criteria);
    }

    @Override
    public void visitDuplicateSearchCriteria(DuplicateSearchCriteria criteria) {
        this.panel = createDuplicateSearchCriteria(criteria);
    }


    public JPanel createDuplicateSearchCriteria(DuplicateSearchCriteria criteria){
        JPanel panel = new JPanel();
        FlowLayout layout = new FlowLayout(FlowLayout.CENTER);
        panel.setLayout(layout);
        JCheckBox box = new JCheckBox("Trouver les doublons");
        box.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                criteria.setActive(box.isSelected());
            }
        });
        panel.add(box);
        panel.setBorder(BorderFactory.createEtchedBorder());
        return panel;
    }

    public JPanel createPatternCriteria(PatternSearchCriteria criteria) {
        JPanel panel = new JPanel();
        JLabel txt=  new JLabel("Texte à rechercher:");
        JTextField txtField = new JTextField();
        txtField.setColumns(20);
        JCheckBox chkBox = new JCheckBox("Activer mode RegExp");
        chkBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                criteria.setActive(true);
                criteria.setRegularExpressionMode(chkBox.isSelected());
            }
        });
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.add(txt);
        panel.add(txtField);
        txtField.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                criteria.setActive(e.getDot()>0);
                if (!"".equals(txtField.getText())) {
                    criteria.setPattern(txtField.getText());
                }
            }
        });
        txtField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!"".equals(txtField.getText())) {
                    criteria.setActive(true);
                    criteria.setPattern(txtField.getText());
                }else{
                    criteria.setActive(false);
                }
            }
        });
        panel.add(chkBox);
        return panel;
    }

}
