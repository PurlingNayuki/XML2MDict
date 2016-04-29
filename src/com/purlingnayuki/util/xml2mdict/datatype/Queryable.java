package com.purlingnayuki.util.xml2mdict.datatype;

import org.dom4j.Element;

/**
 * Title      : Queryable.java
 * Description:
 * Copyright  : BY-NC-SA 3.0
 *
 * @author Purling Nayuki
 * @version 1.0
 */

public interface Queryable {
    /**
     * Get headword by a set rule
     * @param root  The root element of the xml file
     * @return      Headword get by the set rule
     */
    String getHeadword(Element root);
}
