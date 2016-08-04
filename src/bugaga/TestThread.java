package bugaga;

import bugaga.io.Str;

/**
 * Класс треда теста.
 */
public class TestThread implements Runnable
{
    public Thread t;
    protected static int countGood = 0;


    protected static synchronized void increaseCountGood ()
    {
        ++countGood;
    }

    public TestThread ()
    {
        // Создаем поток
        t = new Thread (this, "TestThread");
        t.start ();
    }

    public void run ()
    {
        while (true)
        {
            // Получим задачу.
            String[] task = Test.getTaskAndCheckExit ();

            // Чекаем выход.
            boolean isExit = Boolean.valueOf (task[0]);
            if (isExit)
            {
                break;
            }

            // Распознаем.
            String code = Recognizer.recognizeBase (task[2], Test.getBrainArray ());

            if (code.equals (task[3]))
            {
                increaseCountGood ();
            }

            Str.println (task[1] + " - " + code + ". GOOD: " +
                         Str.getPercentage ((double) countGood / Test.getFilesCount ()) + "%", true, Test
                    .getFilesCount ());
        }
    }
}
