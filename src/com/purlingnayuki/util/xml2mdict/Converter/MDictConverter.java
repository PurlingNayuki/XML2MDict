package com.purlingnayuki.util.xml2mdict.Converter;

import com.purlingnayuki.util.xml2mdict.datatype.Config;
import com.purlingnayuki.util.xml2mdict.datatype.Converter;
import com.purlingnayuki.util.xml2mdict.datatype.Provider;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Title      : MDictConverter.java
 * Description: 
 * Copyright  : BY-NC-SA 3.0
 * @author      Purling Nayuki
 * @version     1.0
 */
 
public class MDictConverter implements Converter {
    public MDictConverter(Provider source) {
        this.source = source;
        cfg = new Config();
        log = Logger.getLogger("!" + this.getClass().getName());
    }

    public Converter setParameter(String name, String value) {
        if (value != null)
            cfg.set(name, value);
        return this;
    }

    /**
     * Parse xml file and generate MDict style source.
     * @return  Generated source content
     */
    public ArrayList<String> convert() throws DocumentException {
        StringBuilder entry = new StringBuilder();
        ArrayList<String> result = new ArrayList<>();
        String css = cfg.get("css");

        // generate css info if css is assigned
        Document cssInfo = DocumentHelper.createDocument();
        if (css != null)
            cssInfo.addElement("link")
                    .addAttribute("rel",    "stylesheet")
                    .addAttribute("type",   "text/css")
                    .addAttribute("href",   css)
                    .setText("");

        ArrayList<Element> headwords = source.getHeadwords();
        for (Element hw: headwords) {
            if (hw != null) {
                entry.append(hw.getText()).append("\r\n");
                if (css != null)
                    entry.append(cssInfo.getRootElement().asXML().replace("</link>", ""));
                entry.append(source.getContent(hw))
                        .append("\r\n</>\r\n");
                if (!source.isSeperate()) {
                    result.add(entry.toString());
                    entry = new StringBuilder();
                }
            }
        }

        if (source.isSeperate())
            result.add(entry.toString());

        return result;
    }

    private Logger log;
    private Provider source;
    private Config cfg;
}
