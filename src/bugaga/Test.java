package bugaga;

import bugaga.io.Str;
import java.io.File;

/**
 * Тестируем скрипт на обучающей выборке.
 *
 * @author GoGo
 */
public class Test
{

    /**
     * Файлы подготовленных капч.
     */
    protected static File[] files;

    public static synchronized void run (String recognizerBrainFilename, String recognizedCapchiesFolder)
    {
        int countGood = 0;// Число удачно распознанных капч
        scanDir (recognizedCapchiesFolder);
        Recognizer.init (recognizerBrainFilename);

        try
        {
            for (int i = 0; i < files.length; i++)
            {
                String filename = files[i].getName ();
                String name[] = filename.split ("_");
                String validCode = name[0];

                // Распознаем
                String code = Recognizer.recognize (files[i].getAbsolutePath ());

                if (code.equals (validCode))
                    ++countGood;

                Str.println (filename + " - " + code + ". GOOD: "
                        + Str.getPercentage ((double) countGood / files.length) + "%",
                             true, files.length);
            }
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            //nothing
        }
    }

    /**
     * Просканить папку с капчами и получить массив со всеми ее файлами.
     */
    protected static synchronized void scanDir (String folder)
    {
        File fileDir = new java.io.File (folder);
        files = fileDir.listFiles ();

        System.out.println ("Total capchies for test: " + files.length + "...");
    }
}
