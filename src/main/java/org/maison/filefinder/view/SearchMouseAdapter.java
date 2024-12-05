package org.maison.filefinder.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class SearchMouseAdapter extends MouseAdapter {

    private JPopupMenu menu;
    private JMenuItem  item;
    private MainWindow window;

    public SearchMouseAdapter(MainWindow window){
        this.window = window;
        this.menu = new JPopupMenu();
        this.item = new JMenuItem("Rechercher");
    }

    @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3){
                item.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        window.setDialogVisible();
                        menu.setVisible(false);
                    }
                });
                menu.add(item);
                menu.show(this.window, e.getX(), e.getY()+100);
            }

        }
}
