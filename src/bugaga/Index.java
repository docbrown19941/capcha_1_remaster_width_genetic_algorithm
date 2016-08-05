package bugaga;

import bugaga.io.*;
import bugaga.system.SystemTools;

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
        long timeStart = java.lang.System.currentTimeMillis ();

        // Активируем настройки
        Config.init (CONF_FILENAME);

        //        teachSystem ();
        testSystem ();

        Str.println (SystemTools.getGen (timeStart));
    }
    //--------------------------------------------------------------------------

    protected static void teachSystem ()
    {
        Teacher t = new Teacher (Config.getString ("capchaTeachFolder"));
        t.reTeach ();
    }

    protected static void testSystem ()
    {
        Test.run (Config.getString ("brainFilename"), Config.getString ("capchaTestFolder"));
    }
}
