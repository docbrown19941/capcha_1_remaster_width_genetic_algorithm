package bugaga;

import bugaga.io.*;
import bugaga.system.SystemTools;
import bugaga.neuro.*;

/**
 * Распознавалка капчи Spaces.ru
 *
 * @author GoGo
 * @version 1
 */
public class Index {

    protected static final String CONF_FILENAME = "conf.ini";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Замеряем время
        long timeStart = java.lang.System.currentTimeMillis();

        // Активируем настройки
        Config.init(CONF_FILENAME);

//        teachSystem ();
        testSystem();

        Str.println(SystemTools.getGen(timeStart));
    }
    //--------------------------------------------------------------------------

    protected static void teachSystem() {
        Teacher t = new Teacher(Config.getString("capchaTeachFolder"));
        t.reTeach();
    }

    protected static void testSystem() {
        Test.run(Config.getString("brainFilename"),
                Config.getString("capchaTestFolder"));
    }

    /**
     * Распознать 1 файл.
     *
     * @param fullCapchaPath Полный путь к капче.
     * @return Текст с капчи.
     */
    protected static String recognizeFile(String fullCapchaPath) {
        Recognizer.init(Config.getString("brainFilename"));
        String code = Recognizer.recognize(fullCapchaPath);

        return code;
    }
}
