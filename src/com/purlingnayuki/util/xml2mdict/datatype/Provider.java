package com.purlingnayuki.util.xml2mdict.datatype;

import org.dom4j.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

/**
 * Title      : Provider.java
 * Description:
 * Copyright  : BY-NC-SA 3.0
 *
 * @author Purling Nayuki
 * @version 1.0
 */

public abstract class Provider {
    public Provider() {
        this.in = new ArrayList<>();
        this.in.add(new File("in"));
        this.isSeperate = true;
        log = Logger.getLogger("!" + this.getClass().getName());
    }

    /**
     * Standard constructor for providers.
     * @param ins           An Array of File class to indicate input files
     * @param isSeperate    Whether all input files should be combined together
     */
    public Provider(File[] ins, boolean isSeperate) {
        this.in = new ArrayList<>();
        Collections.addAll(this.in, ins);
        this.isSeperate = isSeperate;
        roots = new ArrayList<>();
        log = Logger.getLogger("!" + this.getClass().getName());
    }

    /**
     * Get all headwords in the file by a set rule
     * @return An ArrayList of Element of headwords get by the set rule
     */
    public abstract ArrayList<Element> getHeadwords();

    /**
     * Get entry content by a set rule
     * @param elem  The root element of the target entry
     * @return      Content get by the set rule
     */
    public abstract String getContent(Element elem);

    public ArrayList<String> getContent() {
        ArrayList<Element> headwords = getHeadwords();
        ArrayList<String> contentList = new ArrayList<>();
        for (Element hw: headwords) {
            contentList.add(getContent(hw));
        }
        return contentList;
    }

    public boolean isSeperate() {
        return isSeperate;
    }

    protected final boolean isSeperate;
    protected ArrayList<File> in;
    protected ArrayList<Element> roots;
    protected Logger log;

}
