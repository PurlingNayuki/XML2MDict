package com.purlingnayuki.util.xml2mdict;

import com.purlingnayuki.util.xml2mdict.Converter.MDictConverter;
import com.purlingnayuki.util.xml2mdict.Provider.Lingoes.LingoesSource;
import com.purlingnayuki.util.xml2mdict.Provider.OxfordCD.OxfordXMLSource;
import com.purlingnayuki.util.xml2mdict.datatype.Converter;
import com.purlingnayuki.util.xml2mdict.datatype.Provider;
import org.apache.commons.cli.*;
import org.dom4j.DocumentException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
        opts.addOption("f", "from", true,   "Specify the input format");
        opts.addOption("t", "to",   true,   "Specify the output format");
    }

    public static void main(String[] args) throws IOException, ParseException {
        Logger log = Logger.getLogger("!main");
        int count = 0;
        String css = null;
        ArrayList<File> allFiles = new ArrayList<>();

        CommandLineParser parser = new DefaultParser();
        CommandLine cl = parser.parse(opts, args);

        if (cl.hasOption("help") || args.length <= 0) {
            printUsage(opts);
            return;
        }
        if (cl.hasOption("css")) {
            css = cl.getOptionValue("css");
        }

        // judge the provider and converter to use
        String inputFormat, outputFormat;
        if (cl.hasOption("from"))
            inputFormat = cl.getOptionValue("from");
        else {
            printUsage(opts);
            log.severe("Input format must be specified.");
            return;
        }

        if (cl.hasOption("to"))
            outputFormat = cl.getOptionValue("to");
        else {
            printUsage(opts);
            log.severe("Output format must be specified.");
            return;
        }


        for (String fn: cl.getArgs()) {
            File in = new File(fn);

            if (in.isDirectory()) {
                for (File f: in.listFiles())
                    if (f != null)
                        allFiles.add(f);
            }
            else {
                allFiles.add(in);
            }
        }

        try {
            Provider provider;
            Converter conv;
            switch (inputFormat.toLowerCase()) {
                case "oxfordxml":
                    provider = new OxfordXMLSource(allFiles.toArray(new File[allFiles.size()]), true);
                    break;
                case "lingoes":
                    provider = new LingoesSource(allFiles.toArray(new File[allFiles.size()]), true);
                    break;
                default:
                    printUsage(opts);
                    log.severe("Invalid input format.");
                    return;
            }

            switch (outputFormat.toLowerCase()) {
                case "mdict":
                    conv = new MDictConverter(provider);
                    if (css != null)
                        conv.setParameter("css", css);
                    break;
                default:
                    printUsage(opts);
                    log.severe("Invalid output format.");
                    return;
            }


            ArrayList<String> dist = conv.convert();
            for (String result: dist)
                System.out.println(result);
        }
        catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * Print the usage when inappropriate inputs.
     */
    private static void printUsage(Options opts) {
        String[] jarName = Runnable.class.getProtectionDomain().getCodeSource().getLocation().getFile().split(File.separator);
        String cmdEg;
        String paras = "--from <input format> --to <output format> [-ch] file1|directory1 [file2|directory2] ...";
        if (jarName[jarName.length - 1].endsWith(".jar"))
            cmdEg = "java -jar " + jarName[jarName.length - 1] + paras;
        else
            cmdEg = paras;
        HelpFormatter hf = new HelpFormatter();
        hf.printHelp(cmdEg, opts);
    }
}
