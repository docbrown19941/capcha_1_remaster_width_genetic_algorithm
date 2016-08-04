package bugaga;

/**
 * Класс треда теста.
 */
public class TestThread implements Runnable
{
    protected Thread t;

    public TestThread (String _threadName)
    {
        // Создаем поток
        t = new Thread (this, _threadName);
        t.start ();
    }

    public void run ()
    {
    }
}
