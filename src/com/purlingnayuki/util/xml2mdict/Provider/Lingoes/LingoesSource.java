package com.purlingnayuki.util.xml2mdict.Provider.Lingoes;

import com.purlingnayuki.util.xml2mdict.datatype.Provider;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

/**
 * Title      : LingoesSource.java
 * Description: 
 * Copyright  : BY-NC-SA 3.0
 * @author      Purling Nayuki
 * @version     1.0
 */
 
public class LingoesSource extends Provider {
    /**
     * Standard constructor for providers.
     * @param ins           An Array of File class to indicate input files
     * @param isSeperate    Whether all input files should be combined together
     */
    public LingoesSource(File[] ins, boolean isSeperate) {
        this.in = new ArrayList<>();
        Collections.addAll(this.in, ins);
        this.isSeperate = isSeperate;
        roots = new ArrayList<>();
        doc = DocumentHelper.createDocument();
        doc.addElement("Dict");
        log = Logger.getLogger("!" + this.getClass().getName());
    }

    /**
     * Get all headwords in the file by a set rule
     *
     * @return An ArrayList of Element of headwords get by the set rule
     */
    @Override
    public ArrayList<Element> getHeadwords() {
        Element root = doc.getRootElement();
        ArrayList<Element> result = new ArrayList<>();
        BufferedReader reader;
        String line;
        String[] sepResult;
        int counter = 0;

        for (File file : in) {
            try {
                reader = new BufferedReader(new FileReader(file));
                while ((line = reader.readLine()) != null) {
                    sepResult = line.split(" = ");
                    Element curr = root.addElement("entry_" + counter);
                    curr.setText(sepResult[0]);
                    curr.addElement("def").setText(sepResult[1]);
                    result.add(curr);
                    counter += 1;
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Get entry content by a set rule
     *
     * @param elem The root element of the target entry
     * @return Content get by the set rule
     */
    @Override
    public String getContent(Element elem) {
        return "<div class=\"def\">" + elem.element("def").getTextTrim() + "</div>";
    }

    private Document doc;
    private final boolean isSeperate;
}
