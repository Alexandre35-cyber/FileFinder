package org.maison.filefinder.model.criteria;

import org.maison.filefinder.model.DirectorySelectionListener;

import java.io.File;
import java.util.*;

public class DuplicateSearchCriteria extends SearchCriteria implements DirectorySelectionListener {
    private List<String> filesList = new ArrayList<>();
    private Set<String> filesSet = new HashSet<>();
    private boolean searchEnded = false;

    @Override
    public String getName() {
        return "Recherche de doublons";
    }


    public boolean searchEnded(){
        return searchEnded;
    }

    public void accept(SearchCriteriaVisitor visitor) throws Exception {
        visitor.visitDuplicateSearchCriteria(this);
    }


    public boolean duplicatesFound(){
        return filesSet.size() != filesList.size();
    }

    public List<String> getDuplicates(){
        List copyL = new ArrayList();
        copyL.addAll(filesList);

        for (String s:filesSet){
            copyL.remove(s);
        }

        return copyL;
    }

    private void exploreSubDirectory(File root){
        if (root.isDirectory()) {
            File[] files = root.listFiles();
            if (files == null) return;
            for (File f : files) {
                exploreSubDirectory(f);
            }
        } else {
            filesList.add(root.getName());
            filesSet.add(root.getName());
        }
    }


    @Override
    public void directorySelected(String path) {
        File root = new File(path);
        filesList.clear();
        filesSet.clear();
        searchEnded = false;
        exploreSubDirectory(root);
        searchEnded = true;
        System.out.println("Liste " + filesList);
        System.out.println("Set " + filesSet);
    }
}
