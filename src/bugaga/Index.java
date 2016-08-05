package bugaga;

import bugaga.io.*;

/**
 * Распознавалка капчи Spaces.ru
 *
 * @author GoGo
 * @version 1
 */
public class Index
{

    protected static final String CONF_FILENAME = "conf.ini";

    /**
     * @param args the command line arguments
     */
    public static void main (String[] args)
    {
        // Замеряем время
        TimeTracker.start ("ALL_TIME               ");

        // Активируем настройки
        Config.init (CONF_FILENAME);

        //                teachSystem ();
        //        testSystem ();
        Test.runGeneration (Config.getString ("brainFilename"), Config.getString ("capchaTestFolder"));

        Str.println (TimeTracker.stopAndGetGen ("ALL_TIME               "));
    }
    //--------------------------------------------------------------------------

    protected static void teachSystem ()
    {
        Teacher t = new Teacher (Config.getString ("capchaTeachFolder"));
        t.reTeach ();
    }

    protected static void testSystem ()
    {
        Test.runMultiThread (Config.getString ("brainFilename"), Config.getString ("capchaTestFolder"));
    }
}
