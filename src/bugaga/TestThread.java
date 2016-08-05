package bugaga;

import bugaga.io.Str;

/**
 * Класс треда теста.
 */
public class TestThread implements Runnable
{
    public Thread t;
    protected static int countGood = 0;


    protected static synchronized int getOrIncreaseCountGood (boolean _isIncrease)
    {
        if (_isIncrease)
        {
            ++countGood;
        }

        return countGood;
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
            Test.Task task = Test.getTaskAndCheckExit ();

            // Чекаем выход.
            if (task.isExit)
            {
                break;
            }

            // Распознаем.
            TimeTracker.start ("SEND_DATA_TIME         ");
            String code = Recognizer.recognizeBase (task.capchaArray, Test.getBrainArray ());

            if (code.equals (task.validCode))
            {
                getOrIncreaseCountGood (true);
            }

            Str.println (task.filename + " - " + code + ". GOOD: " +
                         Str.getPercentage ((double) getOrIncreaseCountGood (false) / Test.getFilesCount ()) +
                         "%", true, Test.getFilesCount ());
        }
    }
}
