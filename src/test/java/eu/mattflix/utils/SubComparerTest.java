package eu.mattflix.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertTrue;

public class SubComparerTest {

    public static Logger LOG = LoggerFactory.getLogger(SubComparerTest.class);

    private SubComparer sc;

    @Before
    public void setUp() throws Exception {
        sc = SubComparer.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        LOG.info("tearDown testing");
        // Force removing object from memory ?!
        sc = null;

    }


    @Test
    public void testOriginalAndCompare() {



        sc.setOriginalResource(SubParser.parseFileFromResources("original.srt"));
        sc.setComparedResource(SubParser.parseFileFromResources("compare.srt"));

        SubComparerResult scResult = sc.compare();

        LOG.info("Final ratio is {}",scResult.getMatchRatio());

        assertTrue(scResult.getMatchRatio() == 100);

    }


    @Test
    public void testOriginalAndCompareWithTolerance() {
        sc.setOriginalResource(SubParser.parseFileFromResources("original_tolerance.srt"));
        sc.setComparedResource(SubParser.parseFileFromResources("compare_tolerance.srt"));

        SubComparerResult scResult = sc.compareWithTolerance();

        LOG.info("Final ratio is {}",scResult.getMatchRatio());
    }

    @Test
    public void testComparaisonToleranceStart() {
        double tolerance_start = 200;
        boolean result = true;
        SubComparerResult scResult;

        sc.setOriginalResource(SubParser.parseFileFromResources("original_tolerance.srt"));
        sc.setComparedResource(SubParser.parseFileFromResources("compare_tolerance.srt"));


        while(result) {
            sc.setTolerance_start(tolerance_start);
            scResult = sc.compareWithTolerance();

            LOG.info("Ratio with Tolerance {} is {}", tolerance_start, scResult.getMatchRatio());

            // Reduce tolerance at each loop
            tolerance_start -= 10;

            result = (scResult.getMatchRatio() >= 90.0);
        }


    }



    @Test
    public void testTVShow() {
        SubComparer sc = SubComparer.getInstance();

        sc.setOriginalResource(SubParser.parseFileFromResources("Suits.S07E03.720p.HDTV.x264-AVS.srt"));
        sc.setComparedResource(SubParser.parseFileFromResources("Suits.S07E03.720p.HDTV.x264-AVS_fr.srt"));

        SubComparerResult scResult = sc.compare();

        LOG.info("Final ratio is {}",scResult.getMatchRatio());

        assertTrue(scResult.getMatchRatio() >= 90);
    }


}