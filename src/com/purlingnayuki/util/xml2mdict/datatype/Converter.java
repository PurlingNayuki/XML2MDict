package com.purlingnayuki.util.xml2mdict.datatype;

import org.dom4j.DocumentException;

import java.util.ArrayList;

/**
 * Title      : Converter.java
 * Description:
 * Copyright  : BY-NC-SA 3.0
 *
 * @author Purling Nayuki
 * @version 1.0
 */

public interface Converter {
    ArrayList<String> convert() throws DocumentException;
    Converter setParameter(String name, String value);
}
