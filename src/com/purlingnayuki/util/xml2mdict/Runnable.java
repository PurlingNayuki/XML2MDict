package com.purlingnayuki.util.xml2mdict;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Title      : Runnable.java
 * Description: 
 * Copyright  : BY-NC-SA 3.0
 * @author      Purling Nayuki
 * @version     1.0
 */
 
public class Runnable {
    public static void main(String[] args) throws IOException {
        Logger log = Logger.getLogger("!main");
        int count = 0;

        if (1 != args.length) {
            printUsage();
            System.exit(127);
        }

        // initiate files
        File indir  = new File(args[0]);

        log.info("Input set to " + indir.getAbsolutePath());
        if (indir.isDirectory()) {
            String[] xmls = indir.list();
            for (String fn: xmls) {
                XMLSource xmlsource = new XMLSource(new File(indir.getAbsolutePath() + File.separator + fn));
                System.out.print(xmlsource.toMDictSource());
                count += 1;
            }
        }
        else {
            XMLSource xmlsource = new XMLSource(indir);
            System.out.print(xmlsource.toMDictSource());
            count += 1;
        }

        log.info(count + " item(s) processed");
    }

    /**
     * Print the usage when inappropriate inputs.
     */
    private static void printUsage() {
        String jarName = Runnable.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        if (jarName.endsWith(".jar"))
            System.out.println("Usage: java -jar " + jarName + " path/to/XML(s)");
        else
            System.out.println("Parameter: path/to/XML(s)");
    }
}
