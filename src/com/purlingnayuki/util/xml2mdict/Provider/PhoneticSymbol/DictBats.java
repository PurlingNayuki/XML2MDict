package com.purlingnayuki.util.xml2mdict.Provider.PhoneticSymbol;

import com.purlingnayuki.util.xml2mdict.datatype.PhoneticSymbol;

/**
 * Title      : DictBats.java
 * Description:
 * Copyright  : BY-NC-SA 3.0
 *
 * @author Purling Nayuki
 * @version 1.0
 */

public class DictBats extends PhoneticSymbol {
    public DictBats(String str) {
        psData = str;
    }

    public String toString() {
        return psData;
    }

    /**
     * Convert Oxford DictBats symbols to Unicode symbols
     * @return  Converted symbols
     */
    public String toUnicode() {
        String phonetic = this.toString();
        phonetic = phonetic.replace("A", "ɑ")
                            .replace("D", "ð")
                            .replace("E", "ɛ")
                            .replace("I", "ɪ")
                            .replace("J", "ə")
                            .replace("N", "ŋ")
                            .replace("O", "ɔ")
                            .replace("Q", "ɒ")
                            .replace("S", "ʃ")
                            .replace("T", "θ")
                            .replace("U", "ʊ")
                            .replace("V", "ʌ")
                            .replace("W", "w")
                            .replace("Y", "ɡ")
                            .replace("Z", "ʒ")
                            .replace("&", "æ")
                            .replace(":", "ː")
                            .replace("\"", "ˈ")
                            .replace("%", "ˌ");
        return phonetic;
    }

    private String psData;
}
