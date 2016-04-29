package com.purlingnayuki.util.xml2mdict.datatype;

import org.dom4j.Element;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Title      : XMLSource.java
 * Description:
 * Copyright  : BY-NC-SA 3.0
 *
 * @author Purling Nayuki
 * @version 1.0
 */

public class XMLSource implements Queryable {
    public XMLSource() {
        Scanner in = new Scanner(new InputStreamReader(System.in));
    }
    /**
     * Constructor for OxfordXMLSource class.
     * @param in File instance to input xml or directory that contains xml-s
     */
    public XMLSource(File in) {
        this.in = in;
        log = Logger.getLogger("!" + this.getClass().getName());
    }

    public XMLSource(String in) throws IOException {
        this(new File(in));
    }

    /**
     * Get headword by a set rule
     * @param root The root element of the xml file
     * @return Headword get by the set rule
     */
    public String getHeadword(Element root) {
        return root.attributeValue("hw");
    }

    /**
     * Parse xml file and generate MDict style source.
     * @param css   External or assigned css path, or null if no
     * @return      Generated source content
     */
    public String toMDictSource(String css) {
        return "XML2MDict\r\n" +
                "A simple tool for xml-based MDict dictionary maker.\r\n" +
                "</>";
    }

    private File in;
    private Logger log;

}
