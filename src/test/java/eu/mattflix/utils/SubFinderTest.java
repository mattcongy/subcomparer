package eu.mattflix.utils;

import eu.mattflix.captions.TimedTextResource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class SubFinderTest {

    public static Logger LOG = LoggerFactory.getLogger(SubComparerTest.class);
    private TimedTextResource original;
    private SubFinder sFinder;
    @Before
    public void setUp() throws Exception {
        original = SubParser.parseFileFromResources("original.srt");
        sFinder = new SubFinder(original);
    }

    @After
    public void tearDown() throws Exception {
        original = null;
        sFinder = null;
    }

    @Test
    public void compareMultipleFiles() {



        sFinder.addCompareFile(SubParser.parseFileFromResources("compare.srt"));
        sFinder.addCompareFile(SubParser.parseFileFromResources("compare_2.srt"));

        ArrayList<SubComparerResult> results = sFinder.compare();

        for (SubComparerResult result:results
             ) {
            LOG.info(result.toString());
        }


        LOG.debug("Best Result is : {} with ratio {}",sFinder.getBestResult().getFileName(),sFinder.getBestResult().getMatchRatio() );

        assertTrue(sFinder.getBestResult().getMatchRatio() == 100);


    }


}
