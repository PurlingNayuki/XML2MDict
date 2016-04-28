package com.purlingnayuki.util.xml2mdict;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.Iterator;
import java.util.Stack;
import java.util.logging.Logger;


/**
 * Title      : XMLSource.java
 * Description:
 * Copyright  : BY-NC-SA 3.0
 *
 * @author Purling Nayuki
 * @version 1.0
 */

public class XMLSource {
    /**
     * Constructor for XMLSource class.
     * @param in File instance to input xml or directory that contains xml-s
     * @param out   File instance to output MDict source file
     */
    public XMLSource(File in) {
        this.in = in;
        log = Logger.getLogger("!" + this.getClass().getName());
    }

    public String toMDictSource() {
        Element element;
        try {
            element = getElementFromXML(this.in);
            return getHeadwordByNode(element) + "\r\n" + handleElement(element, new StringBuilder()) + "\r\n</>\r\n";
        }
        catch (DocumentException e) {
            log.warning("Cannot read xml file " + this.in.getAbsolutePath() + ", skipping");
        }
        return null;
    }

    protected Element getElementFromXML(File xml) throws DocumentException{
        SAXReader reader = new SAXReader();
        Document doc = reader.read(xml);
        return doc.getRootElement();
    }

    protected String getHeadwordByNode(Element root) {
        // <entry id="a_abbr_e" guid="000000000">(root)<h-g><hwd-g><h id="a_34">A</h>
        return root.element("h-g").element("hwd-g").element("h").getTextTrim();
    }


    protected String handleElement(Element curr, StringBuilder result) {
        /* div body */
        result.append("<div class=\"").append(curr.getName()).append("\"");
        // attributes
        Iterator<Attribute> attributeIterator = curr.attributeIterator();
        while (attributeIterator.hasNext()) {
            Attribute attr = attributeIterator.next();
            result.append(" ").append(attr.getName()).append("=\"").append(attr.getData()).append("\"");
        }
        result.append(">").append(curr.getTextTrim());
        // cope with all child elements
        Iterator<Element> elementIterator = curr.elementIterator();
        while (elementIterator.hasNext()) {
            handleElement(elementIterator.next(), result);
        }
        result.append("</div>");
        return result.toString();
    }

    private File in;
    private Logger log;
}
