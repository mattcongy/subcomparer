package eu.mattflix.utils;

import eu.mattflix.captions.TimedTextHelper;
import eu.mattflix.captions.TimedTextResource;
import eu.mattflix.comparators.SubComparerResultsComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * SubFinder
 *
 * A class that help to compare an original file with multiple files.
 * Give the ration of each file, and can return the best file path
 */
public class SubFinder {

    public static Logger LOG = LoggerFactory.getLogger(SubFinder.class);

    private SubComparer comparerInstance = SubComparer.getInstance();

    private TimedTextResource       originalResource;
    private Set<TimedTextResource>  compareResources;
    private ArrayList<SubComparerResult> results;

    /**
     * Create an instance of SubFinder.
     * @param original Original file representing the source file to compare with the list
     */
    public SubFinder(TimedTextResource original) {
        originalResource = original;
        compareResources = new HashSet<>();
        results = new ArrayList<>();
    }

    /**
     * Create an instance of Subfinder with all parameters
     * @param original Original file representing the source file to compare with the list
     * @param filesToCompare Set of files to compare
     */
    public SubFinder(TimedTextResource original, Set<TimedTextResource> filesToCompare) {
        originalResource = original;
        compareResources = filesToCompare;
        results = new ArrayList<>();
    }


    public SubFinder(File originalFile, File folderToCompare) {
        LOG.debug("SubFinder creation");
        FileFilter filter = new FileNameExtensionFilter("Srt files", "srt");
        originalResource = TimedTextHelper.getTimedTextResource(originalFile);

        if (folderToCompare != null) {
            if (folderToCompare.isDirectory()) {
                compareResources = new HashSet<>();
                // Parse each element of the folder
                for(File f: folderToCompare.listFiles(new java.io.FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        return pathname.getName().endsWith(".srt");
                    }
                })) {

                    LOG.debug("Add file {} to the pool",f.getName());
                    compareResources.add(TimedTextHelper.getTimedTextResource(f));
                }

            }
            else {
                LOG.error("Path given {} is not a folder. Exiting...",folderToCompare.getPath());
            }
        }


        results = new ArrayList<>();
    }


    public void addCompareFile(TimedTextResource fileToAdd) {
        compareResources.add(fileToAdd);
    }

    public ArrayList<SubComparerResult> compare() {

        launchComparaison();

        Collections.sort(results,new SubComparerResultsComparator());
        return results;
    }

    /**
     *
     * @return Best result (100% or best ratio). <br/>If two results are at the same ratio, the system return arbitraty the first one.
     */
    public SubComparerResult getBestResult() {

        double lastRatio = 0.0;
        SubComparerResult lastResult = null;
        for (SubComparerResult result:results
             ) {

            if (result.getMatchRatio() > lastRatio) {
                lastRatio = result.getMatchRatio();
                lastResult = result;
            }

            // Stop if ratio is 100%
            if (lastRatio == 100.00)
                break;

        }

        return lastResult;

    }



    private void launchComparaison() {
        comparerInstance.setOriginalResource(originalResource);

        for (TimedTextResource ttr: compareResources
                ) {
            comparerInstance.setComparedResource(ttr);
            results.add(comparerInstance.compare());
        }
    }

}
