package com.purlingnayuki.util.xml2mdict;

import com.purlingnayuki.util.xml2mdict.datatype.XMLSource;
import com.purlingnayuki.util.xml2mdict.rules.OxfordCD.OxfordXMLSource;
import org.apache.commons.cli.*;

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
    static Options opts = new Options();
    static {
        opts.addOption("c", "css",  true,   "Assign a CSS sheet to all entries");
        opts.addOption("h", "help", false,  "Show this help message");
        opts.addOption("t", "type", true,   "Assign the xml type of the input file(s)")
    }

    public static void main(String[] args) throws IOException, ParseException {
        Logger log = Logger.getLogger("!main");
        int count = 0;
        String css = null;

        CommandLineParser parser = new DefaultParser();
        CommandLine cl = parser.parse(opts, args);

        if (cl.hasOption("help") || args.length <= 0) {
            printUsage(opts);
            return;
        }
        if (cl.hasOption("css")) {
            css = cl.getOptionValue("css");
        }

        for (String fn: cl.getArgs()) {
            File in = new File(fn);

            log.info("Input set to " + in.getAbsolutePath());
            if (in.isDirectory()) {
                String[] xmls = in.list();
                for (String xml : xmls) {
                    XMLSource xmlsource = new OxfordXMLSource(new File(in.getAbsolutePath() + File.separator + xml));
                    String output = xmlsource.toMDictSource(css);
                    if (output != null)
                        System.out.print(output);
                    count += 1;
                }
            } else {
                XMLSource xmlsource = new OxfordXMLSource(in);
                String output = xmlsource.toMDictSource(css);
                if (output != null)
                    System.out.print(output);
                count += 1;
            }

            log.info(count + " item(s) processed");
        }
    }

    /**
     * Print the usage when inappropriate inputs.
     */
    private static void printUsage(Options opts) {
        String[] jarName = Runnable.class.getProtectionDomain().getCodeSource().getLocation().getFile().split(File.separator);
        String cmdEg;
        String paras = "-t XMLType [-ch] file1|directory1 [file2|directory2] ...";
        if (jarName[jarName.length - 1].endsWith(".jar"))
            cmdEg = "java -jar " + jarName[jarName.length - 1] + paras;
        else
            cmdEg = paras;
        HelpFormatter hf = new HelpFormatter();
        hf.printHelp(cmdEg, opts);
    }
}
