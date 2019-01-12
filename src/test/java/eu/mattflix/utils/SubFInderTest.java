package eu.mattflix.utils;

import eu.mattflix.captions.TimedTextResource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class SubFInderTest {

    public static Logger LOG = LoggerFactory.getLogger(SubComparerTest.class);

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void compareMultipleFiles() {

        TimedTextResource original = SubParser.parseFileFromResources("original.srt");
        SubFinder sFinder = new SubFinder(original);

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
