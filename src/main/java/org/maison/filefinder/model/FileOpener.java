package org.maison.filefinder.model;

import java.net.URL;

public interface  FileOpener {

    void open(URL fileToOpen)  throws Exception;
}
