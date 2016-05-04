package com.purlingnayuki.util.xml2mdict.datatype;
/**
 * Title      : DictBats.java
 * Description: 
 * Copyright  : BY-NC-SA 3.0
 * @author      Purling Nayuki
 * @version     1.0
 */
 
public abstract class PhoneticSymbol {
    /**
     * Convert phonetic symbols to Unicode symbols
     * @return  Converted symbols
     */
    public abstract String toUnicode();
}
