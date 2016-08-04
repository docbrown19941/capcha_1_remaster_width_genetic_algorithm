package bugaga;

import bugaga.io.Str;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Тестируем скрипт на обучающей выборке.
 *
 * @author GoGo
 */
public class Test
{
    protected static int currentFileIndex = 0;

    /**
     * Файлы подготовленных капч.
     */
    protected static File[] files;

    public static synchronized void run (String _recognizerBrainFilename, String _recognizedCapchiesFolder)
    {
        // Число удачно распознанных капч
        int countGood = 0;
        scanDir (_recognizedCapchiesFolder);

        // Получим мозг.
        String brainString = "";
        double[][][] brainArray = {};
        try
        {
            brainString = FileUtils.readFileToString (new File (_recognizerBrainFilename), "UTF-8");
            brainArray = Recognizer.getBrainArrayFromBrainString (brainString);
        }
        catch (IOException e)
        {
            e.printStackTrace ();
        }

        try
        {
            for (int i = 0; i < files.length; i++)
            {
                String filename = files[i].getName ();
                String name[] = filename.split ("_");
                String validCode = name[0];

                // Распознаем
                String code = Recognizer.recognizeBase (files[i].getAbsolutePath (), brainArray);

                if (code.equals (validCode))
                {
                    ++countGood;
                }

                Str.println (filename + " - " + code + ". GOOD: " + Str.getPercentage ((double) countGood / files.length) + "%", true, files.length);
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


    public static synchronized String getTask ()
    {
        String task = "";

        return task;
    }
}
