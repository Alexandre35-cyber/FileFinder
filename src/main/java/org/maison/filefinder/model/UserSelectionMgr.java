package org.maison.filefinder.model;

public class UserSelectionMgr {

    private static UserSelectionMgr instance;

    private String directorySelected;

    private String extension;

    public static UserSelectionMgr get(){
        if (instance == null){
            instance = new UserSelectionMgr();
        }
        return instance;
    }

    private UserSelectionMgr(){

    }

    public void setDirectorySelected(String directorySelected){
        this.directorySelected = directorySelected;
    }

    public String getSelectedDirectory(){
        return this.directorySelected;
    }

    public void setSelectedExtension(String extension){
        this.extension = extension;
    }

    public String getSelectedExtension(){
        return this.extension;
    }
}
