package com.purlingnayuki.util.xml2mdict.rules.EPWING;

import java.io.*;

/**
 * Title      : pictureExtractor.java
 * Description:
 * Copyright  : BY-NC-SA 3.0
 *
 * @author      meigen@pdawiki.com
 * @version     1.0
 * @author      Purling Nayuki
 * @version     1.0-fix
 */

public class pictureExtractor {
    public static void main(String[] args) {
        if (args.length == 2)
            getColorPic(args[0], args[1]);
    }

    private final static char[] DIGITS = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    private final static String CHARS = "0123456789ABCDEF";

    private static String toHex(int i) {
        char[] buf = {'0', '0', '0', '0', '0', '0', '0', '0'};
        int charPos = 8;
        int mask = 0xf;
        do {
            buf[--charPos] = DIGITS[i & mask];
            i >>>= 4;
        } while (i != 0);
        if (charPos > 4) charPos = 4;
        return new String(buf, charPos, (8 - charPos));
    }

    /**
     * Get color picture from text files extracted by EBDump.
     * @param f     Dump file
     * @param path  Path to save restored pictures
     */
    public static void getColorPic(String f, String path) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "Shift_JIS"));
            OutputStream out = null;
            String line = "";
            String block = "";
            byte[] bs = new byte[2048];
            int idx = 0;
            if (!path.endsWith(File.separator)) path += File.separator;
            File fl = new File(path);
            if (!fl.exists()) fl.mkdirs();
            while (((line = reader.readLine()) != null)) {
                if (line.startsWith("block")) {
                    block = line.substring(6, line.indexOf("("));
                    System.out.println("Found a block: " + block);
                    idx = 0;
                }
                if (line.startsWith("0")) {
                    for (int i = 0; i < 16; i++) {
                        int a = CHARS.indexOf(line.charAt(5 + 3 * i));
                        int b = CHARS.indexOf(line.charAt(6 + 3 * i));
                        bs[idx++] = (byte) (a << 4 | b);
                    }
                }
                int dts = -1;
                int ws = 0;
                if (idx == 2048) {
                    for (int i = 0; i < 2044; i++) {
                        if (bs[i] == 0x64 && bs[i + 1] == 0x61 && bs[i + 2] == 0x74 && bs[i + 3] == 0x61) {
                            dts = i;
                        }
                    }
                    if (dts != -1) {
                        if (out != null) {
                            out.write(bs, 0, dts);
                            out.flush();
                            out.close();
                        }
                        out = new BufferedOutputStream(new FileOutputStream(path + block + toHex(dts) + ".bmp"));
                        if (dts > 2040)
                            ws = dts - 2040;
                        else
                            out.write(bs, dts + 8, 2040 - dts);
                    }
                    else if (out != null)
                        out.write(bs, ws, 2048 - ws);
                    idx = 0;
                }
            }
            reader.close();
            if (out != null) {
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}