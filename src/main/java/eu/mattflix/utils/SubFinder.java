package eu.mattflix.utils;

import eu.mattflix.captions.TimedTextResource;
import eu.mattflix.comparators.SubComparerResultsComparator;

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


    public void addCompareFile(TimedTextResource fileToAdd) {
        compareResources.add(fileToAdd);
    }

    public ArrayList<SubComparerResult> compare() {

        launchComparaison();

        Collections.sort(results,new SubComparerResultsComparator());
        return results;
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
