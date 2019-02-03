package eu.mattflix.captions;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class TimedTextHelperTest {

    public static Logger LOG = LoggerFactory.getLogger(TimedTextHelperTest.class);

    @Test
    public void gettimedtextresourcetest() {

        //Original.srt contains 5 indexes
        ClassLoader classLoader = TimedTextHelperTest.class.getClassLoader();
        File sourceFile = new File(classLoader.getResource("files/original.srt").getFile());
        TimedTextResource ttResource = TimedTextHelper.getTimedTextResource(sourceFile);

        LOG.debug("getTimedTextResourceTest - size of ttResources : {}",ttResource.getTimedTexts().size());
        assertEquals(5,ttResource.getTimedTexts().size());


    }

}
