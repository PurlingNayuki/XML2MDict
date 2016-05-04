package com.purlingnayuki.util.xml2mdict.datatype;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * Title      : Config.java
 * Description: 
 * Copyright  : BY-NC-SA 3.0
 * @author      Purling Nayuki
 * @version     1.0
 */
 
public class Config {
    public Config() {
        cfg = DocumentHelper.createDocument();
        cfg.addElement("config");
    }

    public Config set(String name, String value) {
        Element cfg = this.cfg.getRootElement().element(name);
        if (cfg == null)
            this.cfg.getRootElement().addElement(name).setText(value);
        else
            cfg.setText(value);
        return this;
    }

    public String get(String name) {
        Element cfg = this.cfg.getRootElement();
        return cfg.element(name).getText();
    }

    private Document cfg;
}
