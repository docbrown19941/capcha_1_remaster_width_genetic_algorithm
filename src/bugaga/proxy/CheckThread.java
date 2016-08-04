package bugaga.proxy;

import bugaga.curl.*;
import bugaga.io.Str;

/**
 * Поток чекера проксей.
 *
 * @author GoGo
 */
public class CheckThread implements Runnable
{

    /**
     * Текущий поток
     */
    protected Thread t;

    /**
     * Объект курла для запросов
     */
    protected Curl curl;

    /**
     * Счетчик строк
     */
    private static int stringCounter = 1;

    /**
     * Конструктор
     */
    public CheckThread (String threadName)
    {
        // Создаем поток
        t = new Thread (this, threadName);
//        t = new Thread (null, this, threadName, 1024 * 100);

        // При запросах необходимо будет вручную подставлять таймаут
        // Создаем свой объект курла для этого потока
        curl = new Curl (ProxyTools.params);

        // Стартуем!
        t.start ();
    }

    private static synchronized void println (String msg)
    {
        Str.println (stringCounter++ + "/" + ProxyTools.getAll ().size ()
                + " [" + Str.getPercentage ((double) stringCounter / ProxyTools.getAll ().size ()) + "%]) " + msg);
    }

    /**
     * Поехали
     */
    public void run ()
    {
        // Чекаем
        while (ProxyTools.getCurrentCheckId () < ProxyTools.getAll ().size ())
        {
            // Получим проксю для чека
            Proxy proxy = ProxyTools.getNextProxy ();

            Str.mode = "linux";
            String page = "";
            boolean isGood;

            // Выполняем запрос
            page = curl.timeout (ProxyTools.getTimeoutCheck ())
                    .proxy (proxy)
                    .get (ProxyTools.detectUrl);

            // 100%-ая проверка
            if (page.contains (ProxyTools.detectMessage))
            {
                // Если все гуд и нет исключения
                ProxyTools.addNewGood (proxy);

                isGood = true;
            }
            else
                isGood = false;

            // Печатаем стату
            println ("THREADS: " + ProxyTools.realThreadsCount + " of " + ProxyTools.getCountThreadsCheck ()
                    + " / TIME: " + ProxyTools.getTimeoutCheck ()
                    + " / GOOD: " + ProxyTools.getGood ().size () + " ["
                    + Str.getPercentage (((double) ProxyTools.getGood ().size () / stringCounter)) + "%]");

            // Сбросим показания
            page = "";
        }
    }
}
