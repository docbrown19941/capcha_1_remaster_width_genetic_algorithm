package bugaga;

import bugaga.io.Str;
import bugaga.system.SystemTools;
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
    /**
     * Количество тредов для тестинга.
     */
    protected static final int THREADS_COUNT = 10;

    /**
     * Файлы подготовленных капч.
     */
    protected static File[] files;
    protected static int currentFileIndex = 0;
    protected static double[][][] brainArray = {};

    //------------------------------------------------------------------------------------------------------------------
    public static void run (String _recognizerBrainFilename, String _recognizedCapchiesFolder)
    {
        loadFilenamesToArray (_recognizedCapchiesFolder);

        // Получим мозг.
        String brainString = "";
        try
        {
            brainString = FileUtils.readFileToString (new File (_recognizerBrainFilename), "UTF-8");
            brainArray = Recognizer.getBrainArrayFromBrainString (brainString);
        }
        catch (IOException e)
        {
            e.printStackTrace ();
        }

        //--------------------------------------------------------------------------------------------------------------
        // Создаем и запускаем треды.
        int countRealThreads = SystemTools.runThreads (THREADS_COUNT, TestThread.class);

        //--------------------------------------------------------------------------------------------------------------
        Str.println ("");
        Str.println ("Total rate: " + Str.getPercentage ((double) TestThread.countGood / Test.getFilesCount ()) + "%");
        Str.println ("Threads: " + countRealThreads);
        Decoder.printTime ();
    }

    /**
     * Просканить папку с капчами и получить массив со всеми ее файлами.
     */
    protected static synchronized void loadFilenamesToArray (String folder)
    {
        File fileDir = new java.io.File (folder);
        files = fileDir.listFiles ();

        System.out.println ("Total capchies for test: " + files.length + "...");
    }


    /**
     * Получить задание и проверить, не надо ли завершить работу.
     *
     * @return
     */
    public static synchronized String[] getTaskAndCheckExit ()
    {
        // Происходит какое-то странное дерьмо. Работает только без слова synchronized.
        // Понял, если 1 метод со словом synchronized, его вызвал какой-то другой метод.
        // А этот первый synchronized-метод вызывает другой synchronized-метод, то ожидание суммируется
        // и ни один из вторых synchronized-методов не сможет вызваться.

        String[] taskArr = new String[4];
        if (currentFileIndex >= files.length)
        {
            taskArr[0] = "true";
            taskArr[1] = "";
            taskArr[2] = "";
            taskArr[3] = "";
        }
        else
        {
            java.io.File file = files[currentFileIndex++];
            String filename = file.getName ();
            String validCode = filename.split ("_")[0];

            // isExit, name, absolutePath, validCode
            taskArr[0] = "false";
            taskArr[1] = filename;
            taskArr[2] = file.getAbsolutePath ();
            taskArr[3] = validCode;
        }

        return taskArr;
    }

    public static double[][][] getBrainArray ()
    {
        return brainArray;
    }

    public static int getFilesCount ()
    {
        return files.length;
    }
}
