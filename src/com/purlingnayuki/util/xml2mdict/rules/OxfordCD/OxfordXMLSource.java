package com.purlingnayuki.util.xml2mdict.rules.OxfordCD;

import com.purlingnayuki.util.xml2mdict.datatype.Queryable;
import com.purlingnayuki.util.xml2mdict.datatype.XMLSource;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.logging.Logger;


/**
 * Title      : OxfordXMLSource.java
 * Description:
 * Copyright  : BY-NC-SA 3.0
 *
 * @author Purling Nayuki
 * @version 1.0
 */

public final class OxfordXMLSource extends XMLSource implements Queryable {
    /**
     * Constructor for OxfordXMLSource class.
     * @param in File instance to input xml or directory that contains xml-s
     */
    public OxfordXMLSource(File in) {
        this.in = new ArrayList<>();
        this.in.add(in);
        log = Logger.getLogger("!" + this.getClass().getName());
    }

    public OxfordXMLSource(String in) {
        this(new File(in));
        log = Logger.getLogger("!" + this.getClass().getName());
    }

    public OxfordXMLSource(File[] ins) {
        this.in = new ArrayList<>();
        Collections.addAll(this.in, ins);
        log = Logger.getLogger("!" + this.getClass().getName());
    }

    public OxfordXMLSource(String[] ins) {
        this.in = new ArrayList<>();
        for (String fn: ins) {
            this.in.add(new File(fn));
        }
        log = Logger.getLogger("!" + this.getClass().getName());
    }

    /**
     * Parse xml file and generate MDict style source.
     * @param css   External or assigned css path, or null if no
     * @return  Generated source content
     */
    public String toMDictSource(String css) {
        Element element;
        StringBuilder result = new StringBuilder();

        // generate css info if css is assigned
        Document cssInfo = DocumentHelper.createDocument();
        cssInfo.addElement("link")
                .addAttribute("rel",    "stylesheet")
                .addAttribute("type",   "text/css")
                .addAttribute("href",   css)
                .setText("");

        for (File in: this.in) {
            try {
                element = getElementFromXML(in);
                result.append(getHeadword(element)).append("\r\n");
                if (css != null)
                    result.append(cssInfo.getRootElement().asXML().replace("</link>", ""));
                result.append(handleElement(element))
                        .append("\r\n</>\r\n");

                return result.toString();
            }
            catch (DocumentException e) {
                log.warning("Cannot read xml file " + in.getAbsolutePath() + ", skipping");
            }
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
    public String getHeadword(Element root) {
        // <entry id="a_abbr_e" guid="000000000">(root)<h-g><hwd-g><h id="a_34">A</h>
        return root.element("h-g").element("hwd-g").element("h").getTextTrim();
    }


    /**
     * Parse a xml file and convert elements into HTML div tags.
     * @param elem      The root element of the xml file
     * @return          Converted HTML body, &lt;div&gt; tags only
     */
    private String handleElement(Element elem) {
        switch (elem.getName()) {
            case "sk_img":
                String fn = elem.attributeValue("file");
                if (fn == null) {
                    // Pictures
                    String src = elem.attributeValue("src");
                    Element newElem = elem.getParent().addElement("div").addAttribute("class", "img")
                            .addElement("img").addAttribute("src", "/img/" + src);
                    elem.getParent().remove(elem);
                    elem = newElem;
                }
                else {
                    String type = elem.attributeValue("type", "");
                    String[] soundfn = fn.split("/");
                    String soundfile = soundfn[soundfn.length - 1].replace('#', '$');
                    Element newElem = elem.getParent().addElement("div").addAttribute("class", "sound " + type)
                            .addElement("a").addAttribute("href", "sound://sounds/" + soundfile)
                            .addElement("img").addAttribute("src", "/icons/snd_" + type + ".png");
                    elem.getParent().remove(elem);
                    elem = newElem;
                }
                break;
            // Phonetic symbols
            case "i":
                if ("i-g".equals(elem.getParent().getData()) || "i-g".equals(elem.getParent().attributeValue("class")))
                    elem.setText(new PhoneticSymbol(elem.getText()).toUnicode());
            default:
                /* div body */
                String divClass = elem.getName();
                elem.setName("div");
                elem.addAttribute("class", divClass);
        }
        // trim elementless tag
        if ("".equals(elem.getText()))
            elem.setText("");
        // cope with all child elements
        Iterator<Element> elementIterator = elem.elementIterator();
        while (elementIterator.hasNext()) {
            handleElement(elementIterator.next());
        }
        return elem.getDocument().getRootElement().asXML();
    }



    private ArrayList<File> in;
    private Logger log;
}
