package eu.mattflix;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void mainTestEmpty() {
        // Simulate args.
        String[] args = {""};

        try {
            App.main(args);
        }
        catch (Exception e) {
            if (e.getClass().getName().equals("org.apache.commons.cli.ParseException")) {
                assertTrue(e.getMessage().startsWith("Missing parameters"));
            }
            else {
                assertTrue(false);
            }
        }






    }

}
