package bugaga;

import bugaga.io.Str;
import bugaga.system.SystemTools;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.sql.Time;

/**
 * Тестируем скрипт на обучающей выборке.
 *
 * @author GoGo
 */
public class Test
{
    public static class Task
    {
        public boolean isExit;
        public boolean[][][] capchaArray;
        public String validCode;
        public String filename;


        public Task (boolean _isExit, boolean[][][] _capchaArray, String _validCode, String _filename)
        {
            isExit = _isExit;
            capchaArray = _capchaArray;
            validCode = _validCode;
            filename = _filename;
        }
    }
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Количество тредов для тестинга.
     */
    protected static final int THREADS_COUNT = 4;

    /**
     * Файлы подготовленных капч.
     */
    protected static File[] files;
    protected static boolean[][][][] processedFilesArray;
    protected static int currentTaskIndex = 0;
    protected static double[][][] brainArray;
    protected static Decoder decoder;


    //------------------------------------------------------------------------------------------------------------------
    public static void runMultiThread (String _recognizerBrainFilename, String _recognizedCapchiesFolder)
    {
        // Получим мозг.
        String brainString;
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
        initCapchaFiles (_recognizedCapchiesFolder);

        //--------------------------------------------------------------------------------------------------------------
        // Создаем и запускаем треды.
        int countRealThreads = SystemTools.runThreads (THREADS_COUNT, TestThread.class);

        //--------------------------------------------------------------------------------------------------------------
        Str.println ("");
        Str.println ("Total rate: " +
                     Str.getPercentage ((double) TestThread.getOrIncreaseCountGood (false) / Test.getFilesCount ()) +
                     "%");
        Str.println ("Threads: " + countRealThreads);
        printTiming ();
    }

    protected static void initCapchaFiles (String _recognizedCapchiesFolder)
    {
        decoder = new Decoder (_recognizedCapchiesFolder);
        files = decoder.getFiles ();
        processedFilesArray = decoder.getProcessedFilesArray ();
    }

    protected static void printTiming ()
    {
        Str.println ("");
        Str.println (TimeTracker.getGen ("DECODER_SCANDIR_TIME   "));
        Str.println (TimeTracker.getGen ("SINGLE_FILE_PARSE_TIME "));
        Str.println (TimeTracker.getGen ("DECODER_PREPROCESS_TIME"));
        Str.println (TimeTracker.getGen ("RECOGNIZE_TIME         "));
    }

    /**
     * Получить задание и проверить, не надо ли завершить работу.
     *
     * @return
     */
    public static synchronized Task getTaskAndCheckExit ()
    {
        // Происходит какое-то странное дерьмо. Работает только без слова synchronized.
        // Понял, если 1 метод со словом synchronized, его вызвал какой-то другой метод.
        // А этот первый synchronized-метод вызывает другой synchronized-метод, то ожидание суммируется
        // и ни один из вторых synchronized-методов не сможет вызваться.
        boolean isExit;
        boolean[][][] capchaArray = {};
        String validCode = "";
        String filename = "";

        if (currentTaskIndex >= files.length)
        {
            isExit = true;
        }
        else
        {
            isExit = false;
            capchaArray = decoder.getProcessedFilesArray ()[currentTaskIndex];
            filename = files[currentTaskIndex].getName ();
            validCode = filename.split ("_")[0];

            ++currentTaskIndex;
        }

        return new Task (isExit, capchaArray, validCode, filename);
    }

    public static double[][][] getBrainArray ()
    {
        return brainArray;
    }

    public static int getFilesCount ()
    {
        return files.length;
    }

    public static double getBrainRate (double[][][] _brainArray)
    {
        // Обходим массив обработанных капчей.
        int countGood = 0;
        for (int i = 0; i < decoder.getProcessedFilesArray ().length; i++)
        {
            boolean[][][] capcha = decoder.getProcessedFilesArray ()[i];
            String validCode = files[i].getName ().split ("_")[0];
            String code = Recognizer.recognizeBase (capcha, _brainArray);

            if (code.equals (validCode))
            {
                ++countGood;
            }
        }

        return Str.getPercentage ((double) countGood / files.length);
    }

    public static void runGeneration (String _recognizerBrainFilename, String _recognizedCapchiesFolder)
    {
        initCapchaFiles (_recognizedCapchiesFolder);

        // Получим мозг.
        String brainString;
        double[][][] brain = {};
        try
        {
            brainString = FileUtils.readFileToString (new File (_recognizerBrainFilename), "UTF-8");
            brain = Recognizer.getBrainArrayFromBrainString (brainString);
        }
        catch (IOException e)
        {
            e.printStackTrace ();
        }

        double rate = getBrainRate (brain);
        printTiming ();
        Str.println ("Brain rate: " + rate + "%");
    }
}
