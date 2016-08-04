package bugaga.proxy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.ArrayList;

import bugaga.curl.*;
import bugaga.io.Str;

/**
 * Сбор проксей для работы.
 *
 * @author GoGo
 */
public class ProxyTools
{
    /**
     * Таймаут для чека
     */
    private static int timeoutCheck;

    /**
     * Число потоков для чека
     */
    private static int countThreadsCheck;

    /**
     * Лист со ВСЕМИ загруженными проксями
     */
    protected static ArrayList<Proxy> listAll;

    /**
     * Лист со подходящими проксями
     */
    protected static ArrayList<Proxy> listGood;

    /**
     * Текущий ид прокси для чека
     */
    private static int currentCheckId = 0;

    /**
     * Параметры для запросов
     */
    protected static QueryParams params;

    /**
     * Объект курла для запросов
     */
    protected Curl curl;

    /**
     * Массив с потоками чекера
     */
    protected static ArrayList<CheckThread> threads = new ArrayList<> ();

    /**
     * Число реально запущенных потоков
     */
    protected static int realThreadsCount = 0;

    /**
     * Время начала чека
     */
    protected static long timeStartCheck;

    /**
     * Сайт для тестирования прокси
     */
    protected static String detectUrl;

    /**
     * Сообщение, по наличию которого на проверочном сайте будет делаться вывод о работоспособности прокси
     */
    protected static String detectMessage;

    /**
     * Конструктор
     */
    public ProxyTools (String dUrl, String dMessage)
    {
        // Считываем параметры
        detectUrl = dUrl;
        detectMessage = dMessage;

        // Задаем настройки
        params = new QueryParams ();
        params.followLocations = true;
        params.isCookie = false;
//        params.timeout // Таймаут зададим при запросах
        params.userAgent = "Пылесос Rowenta Energy 7000";
        params.referer = "http://www.fbi.gov/";
        params.charset = QueryParams.UTF_8;

        curl = new Curl (params);

        // Инициализируем коллекции
        listAll = new ArrayList<> ();
        listGood = new ArrayList<> ();

        // Для начала загрузим сами прокси
        System.out.println ("Proxies downloading...");
        download ();

        System.out.println ("Proxies downloaded: " + listAll.size ());
    }

    /**
     * Загрузить прокси.
     *
     * @return Число загруженных проксей
     */
    private synchronized void download ()
    {
        downloadFromFineProxyOrg ();
        downloadFromProfikomRu ();
        downloadFromJimdoCom ();
        downloadFromPrimeSpeedRu ();
        downloadFromCoolTestsCom ();
    }

    /**
     * Начать чек.
     *
     * @param countThreads Число потоков.
     * @param timeoutP     Таймаут для чека.
     *
     * @return Лист хороших проксей.
     */
    public synchronized ArrayList<Proxy> check (int countThreads, int timeoutP)
    {
        // Сначала сбросим параметры прошлого чека
        listGood.clear ();
        currentCheckId = 0;
        realThreadsCount = 0;

        // Применим параметры
        timeoutCheck = timeoutP;
        countThreadsCheck = countThreads;
        timeStartCheck = System.currentTimeMillis ();

        try
        {
            // Запустим потоки
            for (int x = 0; x < countThreadsCheck; x++)
            {
                threads.add (new CheckThread ("Check thread #" + x));
                ++realThreadsCount;
            }
        }
        catch (OutOfMemoryError e)
        {
            System.err.println ("Not anough memory! Message: \"" + e.getMessage () + "\".");
        }

        // Ждем завершения остальных потоков
        try
        {
            for (CheckThread thread : threads)
            {
                thread.t.join ();
            }
        }
        catch (InterruptedException e)
        {
            System.err.println ("Главный поток прерван!");
        }

        double time = (double) (System.currentTimeMillis () - timeStartCheck) / 1000 / 100;
        System.out.println ("Time: "
                + Str.getPercentage (time) + " sec / "
                + (Str.getPercentage (time / 60)) + " min.");

        // Сбросим показания счетчика строк
        Str.resetStringCount ();

        return listGood;
    }

    /**
     * Загрузить прокси с fineproxy.org.
     */
    private synchronized void downloadFromFineProxyOrg ()
    {
        String page = curl.get ("http://fineproxy.org/%D1%81%D0%B2%D0%B5%D0%B6%D0%B8%D0%B5-%D0%BF%D1%80%D0%BE%D0%BA%D1%81%D0%B8/");

        // Спарсим инфу
        Pattern pattern = Pattern.compile ("([a-zA-Z0-9-\\.]+):([0-9]+)<br/>\n");
        Matcher matcher = pattern.matcher (page);

        while (matcher.find ())
        {
            Proxy proxy = new Proxy (matcher.group (1), Integer.valueOf (matcher.group (2)));

            // Добавляем только УНИКАЛЬНЫЕ элементы
            if (!listAll.contains (proxy))
            {
                listAll.add (proxy);
            }
        }

        System.out.println ("Size: " + listAll.size ());
    }

    /**
     * Загрузить прокси с profikom.ru.
     */
    private synchronized void downloadFromProfikomRu ()
    {
        String page = curl.get ("http://anonimsurfer.profikom.ru/?proxy_list");

        // Спарсим инфу
        Pattern pattern = Pattern.compile ("([a-zA-Z0-9-\\.]+):([0-9]+)\r\n<br>");
        Matcher matcher = pattern.matcher (page);

        while (matcher.find ())
        {
            Proxy proxy = new Proxy (matcher.group (1), Integer.valueOf (matcher.group (2)));

            // Добавляем только УНИКАЛЬНЫЕ элементы
            if (!listAll.contains (proxy))
            {
                listAll.add (proxy);
            }
        }

        System.out.println ("Size: " + listAll.size ());
    }

    /**
     * Загрузить прокси с jimdo.com.
     */
    private synchronized void downloadFromJimdoCom ()
    {
        String page = curl.get ("http://whitecatsss.jimdo.com/proxi-%D0%BB%D0%B8%D1%81%D1%82-%D1%81%D0%B2%D0%B5%D0%B6%D0%B8%D0%B5-%D0%BF%D1%80%D0%BE%D0%BA%D1%81%D0%B8/");

        // Спарсим инфу
        Pattern pattern = Pattern.compile ("([a-zA-Z0-9-\\.]+):([0-9]+)<br/>\n");
        Matcher matcher = pattern.matcher (page);

        while (matcher.find ())
        {
            Proxy proxy = new Proxy (matcher.group (1), Integer.valueOf (matcher.group (2)));

            // Добавляем только УНИКАЛЬНЫЕ элементы
            if (!listAll.contains (proxy))
            {
                listAll.add (proxy);
//                System.out.println (proxy.getFull ());
            }
        }

        System.out.println ("Size: " + listAll.size ());
    }

    /**
     * Загрузить прокси с prime-speed.ru.
     */
    private synchronized void downloadFromPrimeSpeedRu ()
    {
        String page = curl.get ("http://www.prime-speed.ru/proxy/free-proxy-list/all-working-proxies.php");

        // Спарсим инфу
        Pattern pattern = Pattern.compile ("([a-zA-Z0-9-\\.]+):([0-9]+)");
        Matcher matcher = pattern.matcher (page);

        while (matcher.find ())
        {
            Proxy proxy = new Proxy (matcher.group (1), Integer.valueOf (matcher.group (2)));

            // Добавляем только УНИКАЛЬНЫЕ элементы
            if (!listAll.contains (proxy))
            {
                listAll.add (proxy);
//                System.out.println (proxy.getFull ());
            }
        }

        System.out.println ("Size: " + listAll.size ());
    }

    /**
     * Загрузить прокси с cool-tests.com.
     */
    private synchronized void downloadFromCoolTestsCom ()
    {
        String page = curl.get ("http://www.cool-tests.com/all-working-proxies.php");

        // Спарсим инфу
        Pattern pattern = Pattern.compile ("([a-zA-Z0-9-\\.]+):([0-9]+)\r\n");
        Matcher matcher = pattern.matcher (page);

        while (matcher.find ())
        {
            Proxy proxy = new Proxy (matcher.group (1), Integer.valueOf (matcher.group (2)));

            // Добавляем только УНИКАЛЬНЫЕ элементы
            if (!listAll.contains (proxy))
            {
                listAll.add (proxy);
//                System.out.println (proxy.getFull ());
            }
        }

        System.out.println ("Size: " + listAll.size ());
    }

    /**
     * Добавить новую проксю к списку гудов.
     *
     * @param proxy
     */
    protected static synchronized void addNewGood (Proxy proxy)
    {
        listGood.add (proxy);
    }

    /**
     * Получить следующую проксю для чека
     *
     * @return
     */
    protected static synchronized Proxy getNextProxy ()
    {
        return listAll.get (currentCheckId++);
    }

    public static int getTimeoutCheck ()
    {
        return timeoutCheck;
    }

    public static int getCurrentCheckId ()
    {
        return currentCheckId;
    }

    public static ArrayList<Proxy> getAll ()
    {
        return listAll;
    }

    public static ArrayList<Proxy> getGood ()
    {
        return listGood;
    }

    public static int getCountThreadsCheck ()
    {
        return countThreadsCheck;
    }
}
