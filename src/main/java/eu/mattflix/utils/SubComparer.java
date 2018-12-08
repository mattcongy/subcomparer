
package eu.mattflix.utils;

import com.google.common.base.Stopwatch;
import eu.mattflix.captions.TimedText;
import eu.mattflix.captions.TimedTextResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * SubComparer
 *
 * @author mattcongy
 *
 *
 */

public class SubComparer {

    private static Logger LOG = LoggerFactory.getLogger(SubComparer.class);

    private SubComparer(){}

    /** Holder */
     private static class  SubComparerHolder {
         private final static SubComparer instance = new SubComparer();
    }


    public static SubComparer getInstance() { return SubComparerHolder.instance; }

    private TimedTextResource originalResource;
    private TimedTextResource comparedResource;

    private SubComparerResult result;

    // Getter & Setters
    //********************
    public TimedTextResource getOriginalResource() {
        return originalResource;
    }

    public void setOriginalResource(TimedTextResource originalResource) {
        this.originalResource = originalResource;
    }

    public TimedTextResource getComparedResource() {
        return comparedResource;
    }

    public void setComparedResource(TimedTextResource comparedResource) {
        this.comparedResource = comparedResource;
    }
    //********************




    public SubComparerResult compare() {

        if (result != null ) {
            result = new SubComparerResult("",0);
        }

        if (originalResource != null && comparedResource != null) {
            LOG.debug("Try to compare files");
            return compareResources();
        }
        else {
            return null;
        }

    }



    private SubComparerResult compareResources() {
        // Get Arrays for both files
        ArrayList<TimedText> originalTime = originalResource.getTimedTexts();
        ArrayList<TimedText> comparedTime = comparedResource.getTimedTexts();

        double matchLines = 0;
        double matchLinesWithoutDuration = 0;
        double unmatchLines = 0;

        Stopwatch watcher = Stopwatch.createStarted();

        for (TimedText t:originalTime) {
            //LOG.debug("Index({}) -- Position {} | Texte = {}",t.getIndex(),t.getPosition(),t.getText());

            // Try to look for this duration in the compared file
            boolean bMatch = false;
            for(TimedText ct:comparedTime) {
                //LOG.debug("Comparaison ligne {}\n t.getPosition()={} - ct.getPosition()={}",t.getIndex(),t.getPosition(),ct.getPosition());
                //LOG.debug("t.getDuration()={} - ct.getDuration()={}",t.getDuration(),ct.getDuration());
                if ((t.getPosition() == ct.getPosition() && t.getDuration() != ct.getDuration())) {
                    matchLinesWithoutDuration++;
                    LOG.debug("Find entry, without same duration. (Index :{} , Position :{}, Duration :{})",t.getIndex(),t.getPosition(),t.getDuration());
                    bMatch = true;
                    break;
                }
                else if ((t.getPosition() == ct.getPosition() && t.getDuration() == ct.getDuration())) {
                    matchLines++;
                    bMatch = true;
                    break;
                }



            }

            if (!bMatch) {
                LOG.debug("Entry not found. (Index :{} , Position :{}, Duration :{})",t.getIndex(),t.getPosition(),t.getDuration());
                LOG.debug("{}",t.getText());
                unmatchLines++;
            }


        }

        watcher.stop();
        LOG.info("End of computing. Time elapsed is {}s / {}ms",watcher.elapsed(TimeUnit.SECONDS),watcher.elapsed(TimeUnit.MILLISECONDS));
        LOG.debug("Match Line : {} - MatchLineWithoutDuration : {} - Unmatch Line(s) : {} - Total Lines : {}",matchLines,matchLinesWithoutDuration,unmatchLines,originalTime.size());
        // Compute Ratio
        // 1. Check Matchline + unmatch line = Total lines of original
        double total = matchLines + matchLinesWithoutDuration + unmatchLines;
        double ratio = ((matchLines + matchLinesWithoutDuration)/total) * 100;
        if (total == originalTime.size()) {
            LOG.info("Total is ok (with ratio {})",ratio);
            return new SubComparerResult(comparedResource.getName(),ratio);
        }
        else {
            LOG.error("Total line does not match.");
            return new SubComparerResult(comparedResource.getName(),-1);
        }

    }

}
