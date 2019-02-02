package eu.mattflix.utils;

import eu.mattflix.captions.TimedTextHelper;
import eu.mattflix.captions.TimedTextResource;
import eu.mattflix.comparators.SubComparerResultsComparator;
import eu.mattflix.filters.SrtFileFilter;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * SubFinder
 * <p>
 * A class that help to compare an original file with multiple files.
 * Give the ration of each file, and can return the best file path
 */
public class SubFinder {

    public static Logger LOG = LoggerFactory.getLogger(SubFinder.class);

    private SubComparer comparerInstance = SubComparer.getInstance();

    private TimedTextResource originalResource;
    private Set<TimedTextResource> compareResources;
    private ArrayList<SubComparerResult> results;

    /**
     * Create an instance of SubFinder.
     *
     * @param original Original file representing the source file to compare with the list
     */
    public SubFinder(TimedTextResource original) {
        originalResource = original;
        compareResources = new HashSet<>();
        results = new ArrayList<>();
    }

    /**
     * Create an instance of Subfinder with all parameters
     *
     * @param original       Original file representing the source file to compare with the list
     * @param filesToCompare Set of files to compare
     */
    public SubFinder(TimedTextResource original, Set<TimedTextResource> filesToCompare) {
        originalResource = original;
        compareResources = filesToCompare;
        results = new ArrayList<>();
    }


    public SubFinder(File originalFile, File folderToCompare) {
        originalResource = TimedTextHelper.getTimedTextResource(originalFile);
        originalResource.setName(originalFile.getName());

        if (folderToCompare != null) {
            if (folderToCompare.isDirectory()) {
                compareResources = new HashSet<>();
                // Parse each element of the folder

                for (File f : folderToCompare.listFiles(new SrtFileFilter()))
                {
                    LOG.debug("Add file {} ({}) to the pool", f.getName(),f.getAbsolutePath());
                    compareResources.add(TimedTextHelper.getTimedTextResource(f));
                }

            } else {
                LOG.error("Path given {} is not a folder. Exiting...", folderToCompare.getPath());
            }
        }


        results = new ArrayList<>();
    }


    public void addCompareFile(TimedTextResource fileToAdd) {
        compareResources.add(fileToAdd);
    }

    public ArrayList<SubComparerResult> compare(double tolerance) {

        launchComparaison(tolerance);

        Collections.sort(results, new SubComparerResultsComparator());
        return results;
    }

    /**
     * @return Best result (100% or best ratio). <br/>If two results are at the same ratio, the system return arbitraty the first one.
     */
    public SubComparerResult getBestResult() {

        double lastRatio = 0.0;
        SubComparerResult lastResult = null;
        for (SubComparerResult result : results
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

    public String getJSONBestResult() {

        ObjectMapper mapper = new ObjectMapper();
        String jsonReturn = "";
        try {
            jsonReturn = mapper.writeValueAsString(getBestResult());
            return jsonReturn;

        } catch (Exception e) {

            LOG.error("Error trying to marshall bestResult as JSON...");
        }

        return "";
    }


    private void launchComparaison(double tolerance) {
        comparerInstance.setOriginalResource(originalResource);

        for (TimedTextResource ttr : compareResources
        ) {
            comparerInstance.setComparedResource(ttr);
            if (tolerance != -1) {
                comparerInstance.setTolerance_start(tolerance);
                results.add(comparerInstance.compareWithTolerance());
            }
            else
                results.add(comparerInstance.compare());
        }
    }

    public TimedTextResource getOriginalResource() {
        return originalResource;
    }

    public void setOriginalResource(TimedTextResource originalResource) {
        this.originalResource = originalResource;
    }

    public Set<TimedTextResource> getCompareResources() {
        return compareResources;
    }

    public void setCompareResources(Set<TimedTextResource> compareResources) {
        this.compareResources = compareResources;
    }
}
