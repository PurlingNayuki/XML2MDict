package com.purlingnayuki.util.xml2mdict.Provider.OxfordCD;

import com.purlingnayuki.util.xml2mdict.Provider.PhoneticSymbol.DictBats;
import com.purlingnayuki.util.xml2mdict.datatype.Provider;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * Title      : OxfordXMLSource.java
 * Description:
 * Copyright  : BY-NC-SA 3.0
 *
 * @author Purling Nayuki
 * @version 1.0
 */

public final class OxfordXMLSource extends Provider {
    public OxfordXMLSource(File[] ins, boolean isSeperate) {
        super(ins, isSeperate);
    }

    /**
     * Get all headwords by a set rule
     * @return      Headword get by the set rule
     */
    public ArrayList<Element> getHeadwords() {
        ArrayList<Element> result = new ArrayList<>();
        for (File fi: in) {
            SAXReader reader = new SAXReader();
            try {
                Document doc = reader.read(fi);
                roots.add(doc.getRootElement());
            }
            catch (DocumentException e) {
                log.warning("Cannot read " + fi.getName() + ", skipping");
                roots.add(null);
            }
        }
        // <entry id="a_abbr_e" guid="000000000">(root)<h-g><hwd-g><h id="a_34">A</h>
        for (Element root: roots) {
            if (root != null)
                result.add(root.element("h-g").element("hwd-g").element("h"));
            else
                result.add(null);
        }
        return result;
    }


    /**
     * Parse a xml file and convert elements into HTML div tags.
     * @param elem      The root element of the xml file
     * @return          Converted HTML body, &lt;div&gt; tags only
     */
    public String getContent(Element elem) {
        handleElement(elem.getDocument().getRootElement());
        return elem.getDocument().getRootElement().asXML();
    }

    private void handleElement(Element elem) {
        log.info("Handling: " + elem.getName());
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
                    elem.setText(new DictBats(elem.getText()).toUnicode());
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
    }
}
