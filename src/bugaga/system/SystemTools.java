package bugaga.system;

import java.util.ArrayList;
import java.lang.reflect.*;

import bugaga.io.*;

/**
 * Класс для облегчения рутинных действий.
 *
 * @author GoGo
 */
public class SystemTools
{

    /**
     * Создать и запустить потоки.
     * Класс потока должен наследовать интерфейс Runnable и иметь член "t" - объект Thread потока этого класса.
     * В случае нехватки памяти для создания трэда, выводит соответствующее сообщение.
     *
     * @param countThreads  Ожидаемое число потоков.
     * @param runnableClass Class-объект потока, которые будем запускать.
     *
     * @return Число реально запущенных потоков.
     */
    public static synchronized int runThreads (int countThreads, Class runnableClass)
    {
        // Лист с текущими потоками
        ArrayList<Runnable> currentThreads = new ArrayList<> ();

        try
        {
            try
            {
                // Создаем и добавляем в лист трэды
                for (int x = 0; x < countThreads; x++)
                {
                    // Создаем новый объект переданного класса
                    Runnable newObject = (Runnable) runnableClass.newInstance ();
                    currentThreads.add (newObject);
                }
            }
            catch (OutOfMemoryError e)
            {
                java.lang.System.err.println ("Not anough memory! Message: \"" + e.getMessage () + "\".");
            }

            // Присоединяемся к трэдам, чтобы ожидать их завершения
            for (int x = 0; x < currentThreads.size (); x++)
            {
                // Получаем объект поля "t" - поля с объектом Thread данного потока
                Field fieldThread = runnableClass.getDeclaredField ("t");

                // Получаем само ЗНАЧЕНИЕ поля
                Thread t = (Thread) fieldThread.get (currentThreads.get (x));

                // Присоединяемся к трэду, т.е. ждем его завершения
                t.join ();
            }
        }
        catch (InstantiationException | IllegalAccessException | NoSuchFieldException | InterruptedException e)
        {
            java.lang.System.out.println (e.getMessage ());
        }

        return currentThreads.size ();
    }

    /**
     * Вернуть время генерации.
     *
     * @param timeStart Время старта срипта.
     *
     * @return
     */
    public static String getGen (long timeStart)
    {
        double time = (double) (java.lang.System.currentTimeMillis () - timeStart) / 1000 / 100;
        return "Time: "
                + Str.getPercentage (time) + " sec / "
                + (Str.getPercentage (time / 60)) + " min / "
                + (Str.getPercentage (time / 60 / 60)) + " hour.";
    }

    /**
     * Вывести время генерации.
     *
     * @param timeStart Время старта срипта.
     */
    public static void printGen (long timeStart)
    {
        Str.println (getGen (timeStart));
    }
}
