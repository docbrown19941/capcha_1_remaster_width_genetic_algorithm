package bugaga;

import java.util.Random;

import bugaga.io.*;
import bugaga.antigate.*;

/**
 * Поток, распознающий скачанные капчи в файлах на антигейте.
 *
 * @author GoGo
 */
public class AntigateThread implements Runnable
{

    /**
     * Папка для капч, предназначенных для распознавания.
     */
    public static String capchaDir = "capcha";

    /**
     * Папка для распознанных капч.
     */
    public static String recognizedCapchiesDir = "recognized_capchies";

    public Thread t;

    protected static Random rand = new Random ();

    protected static java.io.File[] files;

    protected static volatile int currentCapchaFileNumber = 0;

    protected Antigate anti;

    /**
     * Глобальный конструктор.
     */
    public static synchronized void init ()
    {
        System.out.println ();
        System.out.println ("Start the Antigate recognize...");
        scanDir ();
    }

    /**
     * Конструктор
     */
    public AntigateThread ()
    {
        anti = new Antigate (Config.getString ("antigateApiKey"));// Создаем объект антигейта для данного трэда
        (t = new Thread (this, "AntigateThread")).start ();// Стартуем, епта!
    }

    /**
     * Точка запуска
     */
    @Override
    public void run ()
    {
        try
        {
            while (true)
            {
                java.io.File currentFile = getNextCapchaFile ();

                try
                {
                    // Для точного результата распознаем капчи 2 раза
                    String text1 = anti.recognize (currentFile.getAbsolutePath ());
                    String text2 = anti.recognize (currentFile.getAbsolutePath ());

                    // Только если тексты совпали, продолжаем
                    if (text1.equals (text2))
                    {

                        // Переименовываем текущий файл для дальнейшего обучения
                        currentFile.renameTo (new java.io.File (
                                recognizedCapchiesDir + "/" + text1 + "_" + rand.nextInt (1000000000) + ".gif"));

                        Str.println (currentFile.getName () + " - RECOGNIZED: " + text1, true, files.length);
                    }
                    else
                        Str.println (currentFile.getName () + " - ERROR: TEXTS WERE NOT MATCH", true, files.length);
                }
                catch (AntigateException e)
                {
                    Str.println (currentFile.getName () + " - " + e.getMessage (), true, files.length);
                }
            }
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            // При попытке обратиться к несуществующему элементу массива, нас просто выкинет из потока
            // nothing
        }
    }

    /**
     * Получить следующий файл с капчей.
     *
     * @return
     */
    protected static synchronized java.io.File getNextCapchaFile ()
    {
        return files[currentCapchaFileNumber++];
    }

    /**
     * Просканить папку с капчами и получить массив со всеми ее файлами
     */
    protected static synchronized void scanDir ()
    {
        java.io.File fileDir = new java.io.File (capchaDir);
        files = fileDir.listFiles ();

        System.out.println ("Total capchies for learning: " + files.length);
    }
}
