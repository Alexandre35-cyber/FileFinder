package org.maison.filefinder.model;

import java.io.File;
import java.net.URL;

public interface  FileOpener {

    void open(URL fileToOpen)  throws Exception;
}
