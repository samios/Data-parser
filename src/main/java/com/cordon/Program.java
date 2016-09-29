package com.cordon;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by sami on 22/04/16.
 */
public class Program{

    private String dir;
    private String last;
    private ArrayList<String> files;
    private ArrayList<ArrayList<String>> contents;


    /**
     * Program constructor
     *
     * @param dir absolute directory path where the files are located
     */

    public Program(String dir) {
        this.dir=dir;
        this.files= new ArrayList<String>();
        this.contents= new ArrayList<ArrayList<String>>();
    }

    /**
     *  Basic Getters and Setters
     **/

    public String getDir() {
        return dir;
    }

    public String getLast() {
        return this.last;
    }

    public ArrayList<String> getFiles() {
        return this.files;
    }

    public ArrayList<ArrayList<String>> getContents() {
        return this.contents;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public void setFiles(ArrayList<String> files) {
        this.files = files;
    }

    public void setContents(ArrayList<ArrayList<String>> contents) {
        this.contents = contents;
    }

    public void launchP(String dir) {
        this.dir=dir;
        this.setFiles(Scraper.getFileList(dir));
        this.setContents(Scraper.getAllContents(dir));
    }


}

