package eu.mattflix.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class SrtFileFilter implements java.io.FileFilter{

    public static Logger LOG = LoggerFactory.getLogger(SrtFileFilter.class);

    @Override
    public boolean accept(File pathname) {
        LOG.debug("FileFilter Pathname : {}",pathname.getAbsolutePath());
        return pathname.getName().endsWith(".srt");


    }
}
