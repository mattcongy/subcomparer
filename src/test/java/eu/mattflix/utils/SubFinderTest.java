package eu.mattflix.utils;

import eu.mattflix.captions.TimedTextResource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
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
    public void constructorTest() {
       // SubFinder cSFinder = new SubFinder();
    }

    /*
     * Try to set a comparison process with a file and dedicated folder.
     * The number of files compared must be the same than the compareResources property of the SubFinder instance
     */
    @Test
    public void compareFileAndFolders() {

        ClassLoader classLoader = SubParser.class.getClassLoader();
        File original = new File(classLoader.getResource("files/original.srt").getFile());
        File destFolder = new File(classLoader.getResource("files/compare-folder").getFile());
        SubFinder sf1 = new SubFinder(original, destFolder);

        LOG.debug("Size : {}",sf1.getCompareResources().size());
        assert(sf1.getCompareResources().size() == 5);


    }

    @Test
    public void compareMultipleFiles() {
        double tolerance = -1;


        sFinder.addCompareFile(SubParser.parseFileFromResources("compare.srt"));
        sFinder.addCompareFile(SubParser.parseFileFromResources("compare_2.srt"));

        ArrayList<SubComparerResult> results = sFinder.compare(tolerance);

        for (SubComparerResult result:results
             ) {
            LOG.info(result.toString());
        }


        LOG.debug("Best Result is : {} with ratio {}",sFinder.getBestResult().getFileName(),sFinder.getBestResult().getMatchRatio() );

        assertTrue(sFinder.getBestResult().getMatchRatio() == 100);


    }

    @Test
    public void compareWithFolder() {
        ClassLoader classLoader = SubParser.class.getClassLoader();
        File original = new File(classLoader.getResource("files/original.srt").getFile());
        File destFolder = new File(classLoader.getResource("files/compare-folder").getFile());

    }



}
