package com.purlingnayuki.util.xml2mdict;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.Iterator;
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
     */
    public XMLSource(File in) {
        this.in = in;
        log = Logger.getLogger("!" + this.getClass().getName());
    }


    /**
     * Parse xml file and generate MDict style source.
     * @return  Generated source content
     */
    public String toMDictSource(String css) {
        Element element;
        StringBuilder result = new StringBuilder();
        try {
            element = getElementFromXML(this.in);
            result.append(getHeadwordByNode(element)).append("\r\n");
            if (css != null)
                result.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"").append(css).append("\">");
            result.append(handleElement(element, new StringBuilder())).append("\r\n</>\r\n");

            return result.toString();
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

    /**
     * Get headword by a set rule
     * @param root  The root element of the xml file
     * @return      Headword get by the set rule
     */
    protected String getHeadwordByNode(Element root) {
        // <entry id="a_abbr_e" guid="000000000">(root)<h-g><hwd-g><h id="a_34">A</h>
        return root.element("h-g").element("hwd-g").element("h").getTextTrim();
    }


    /**
     * Parse a xml file and convert elements into HTML div tags.
     * @param elem      The root element of the xml file
     * @param result    Any StringBuilder instance. Used for buffer only
     * @return          Converted HTML body, &lt;div&gt; tags only
     */
    protected String handleElement(Element elem, StringBuilder result) {
        switch (elem.getName()) {
            case "sk_img":
                String fn = elem.attributeValue("file");
                if (fn == null) {
                    // Pictures
                    String src = elem.attributeValue("src");
                    result.append("<div class=\"img\"><img src=\"/img/").append(src).append("\">");
                }
                else {
                    String type = elem.attributeValue("type", "");
                    String[] soundfn = fn.split("/");
                    String soundfile = soundfn[soundfn.length - 1].replace('#', '$');
                    result.append("<div class=\"sound ").append(type).append("\"><a href=\"sound://sounds/").append(soundfile).append("\">");
                    result.append("<img src=\"/icons/snd_").append(type).append(".png\"></a>");
                }
                break;
            default:
                /* div body */
                result.append("<div class=\"").append(elem.getName()).append("\"");
                // attributes
                Iterator<Attribute> attributeIterator = elem.attributeIterator();
                while (attributeIterator.hasNext()) {
                    Attribute attr = attributeIterator.next();
                    result.append(" ").append(attr.getName()).append("=\"").append(attr.getValue()).append("\"");
                }
                result.append(">").append(elem.getTextTrim());
        }
        // cope with all child elements
        Iterator<Element> elementIterator = elem.elementIterator();
        while (elementIterator.hasNext()) {
            handleElement(elementIterator.next(), result);
        }
        result.append("</div>");
        return result.toString();
    }

    private File in;
    private Logger log;
}
