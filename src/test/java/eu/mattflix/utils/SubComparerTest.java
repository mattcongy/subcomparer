package eu.mattflix.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertTrue;

public class SubComparerTest {

    public static Logger LOG = LoggerFactory.getLogger(SubComparerTest.class);

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void test_original_and_compare() {

        SubComparer sc = SubComparer.getInstance();

        sc.setOriginalResource(SubParser.parseFileFromResources("original.srt"));
        sc.setComparedResource(SubParser.parseFileFromResources("compare.srt"));

        SubComparerResult scResult = sc.compare();

        LOG.info("Final ratio is {}",scResult.getMatchRatio());

        assertTrue(scResult.getMatchRatio() == 100);

    }

    @Test
    public void test_tvshow_en_fr() {
        SubComparer sc = SubComparer.getInstance();

        sc.setOriginalResource(SubParser.parseFileFromResources("Suits.S07E03.720p.HDTV.x264-AVS.srt"));
        sc.setComparedResource(SubParser.parseFileFromResources("Suits.S07E03.720p.HDTV.x264-AVS_fr.srt"));

        SubComparerResult scResult = sc.compare();

        LOG.info("Final ratio is {}",scResult.getMatchRatio());

        assertTrue(scResult.getMatchRatio() >= 90);
    }


}