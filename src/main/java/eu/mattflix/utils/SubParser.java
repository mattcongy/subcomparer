package eu.mattflix.utils;


import eu.mattflix.captions.TimedTextHelper;
import eu.mattflix.captions.TimedTextResource;
import eu.mattflix.captions.io.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;


public class SubParser {

    public static Logger LOG = LoggerFactory.getLogger(SubParser.class);

    public static TimedTextResource parseFile(String path) {
        // TODO : With Utils, verify if the path is correct.
        File FileFromPath = new File(path);
        Parser parser = TimedTextHelper.getParser(FileFromPath);
        return TimedTextHelper.getTimedTextResource(FileFromPath);
    }

    public static TimedTextResource parseFileFromResources(String name) {

        ClassLoader classLoader = SubParser.class.getClassLoader();
        File fileFromPath = new File(classLoader.getResource("files/" + name).getFile());
        if (fileFromPath != null) {
            TimedTextResource ttr = TimedTextHelper.getTimedTextResource(fileFromPath);
            ttr.setName(name);
            return ttr;
        } else
            return null;
    }


}
