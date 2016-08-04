package bugaga.curl;

import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.net.URLDecoder;
import java.net.Proxy;
import java.net.InetSocketAddress;
import java.net.CookieManager;
import java.net.CookieHandler;
import java.net.CookiePolicy;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Random;

import org.apache.commons.io.*;

import bugaga.io.*;
import java.io.UnsupportedEncodingException;

/**
 * Новая либа для работы с сетью, основана на HttpURLConnection.
 *
 * Идеи:
 * - добавить вместо методов .get() и .post() метод для распечатки структуры запроса: .stata ().
 * - перенести класс Config для работы с ini в библиотеку.
 *
 * Changelog:
 * v.3 - добавлена работа с проксями и куками.
 * v.4 - исправлен мелкий баг со значением null в isCookie по умолчанию.
 * v.5 - исправлен баг с обработкой кук - куки обрабатывались всегда, вне зависимости от значения isCookie.
 * v.6 - добавлено скачивание и сохранение бинарного файла.
 * v.7:
 * - загрузка файла на сервер в формате multipart/form-data
 * - добавление любого хедера к запросу
 * - динамическая подстройка кодировки
 * - режим дебага (будут выводиться ошибки запросов и таймауты)
 * - исключение TimeoutException теперь никогда не генериться
 * - также другие мелкие, но важные изменения
 * v.8 - очистка кук
 * v.9:
 * - подсчет количества запросов (для всего класса)
 * - подсчет количества успешных запросов (счетчик увеличивается через метод)
 * - вывод процента успешных запросов
 * - метод .dump () для автоматического сохранения результата запроса в файл
 * - в File добавлен метод загрузки акков из файла в лист
 * - добавлен класс для доступа к настройкам - Config
 * - либа полностью переведена на ручную обработку кук. Теперь у каждого потока 100% свои куки
 * - добавлена короткая обертка для регулярок - класс Preg
 * - добавлен класс SystemTools, облегчающий запуск потоков и др. рутинные задачи
 *
 * @author GoGo
 * @version 9
 */
public class Curl
{

    /**
     * Включить режим дебага?
     * В режиме дебага будут выводиться исключения либы.
     */
    public static boolean DEBUG = false;

    /**
     * Общий объект для ВСЕХ запросов
     */
    private HttpURLConnection connect;

    /**
     * Менеджер кук для данного объекта курл
     */
    private CookieManager cookieManager;

    /**
     * Текущие параметры
     */
    private ArrayList<CurlParam> currentParams;

    /**
     * Хедеры, добавленные пользователем
     */
    protected LinkedHashMap<String, String> userHeaders = new LinkedHashMap<> ();

    /**
     * Карта с куками.
     * Куки поддерживаются не идеально, домены и сроки жизни не учитываются.
     */
    protected LinkedHashMap<String, String> cookies = new LinkedHashMap<> ();

    /**
     * Вид текущего запроса.
     */
    private boolean isMultipart = false;

    /**
     * Если загружаем файл, здесь сохраняем его имя.
     */
    private String currentDownloadingFilename;

    /**
     * Сохранять ли результат запроса в файл?
     */
    private boolean isSave = false;

    /**
     * Текущая метка для сохранения запроса
     */
    private String currentSavingMark;

    /**
     * Текущий код ответа сервера
     */
    private int currentResponseCode = -1;

    /**
     * Текущие настройки
     */
    protected QueryParams currentConfig;

    /**
     * Стандартные настройки
     */
    protected QueryParams defaultConfig;

    /**
     * Число всех запросов (и номер текущего запроса)
     */
    private static int countAllQueries = 0;

    /**
     * Число успешных запросов
     */
    private static int countSuccessfulQueries = 0;

    //--------------------------------------------------------------------------
    // Публичные методы
    //--------------------------------------------------------------------------
    /**
     * Конструктор.
     *
     * @param params Параметры запросов
     */
    public Curl (QueryParams params)
    {
        defaultConfig = params;

        // Так как в яве нельзя просто так взять и скопировать переменную (WTF?),
        // создаем НОВЫЙ объект и копируем члены вручную
        currentConfig = new QueryParams ();

        // Обработка кук
        cookieManager = new CookieManager ();
        CookieHandler.setDefault (cookieManager);
        // так как куки обрабатываем вручную, то автоматически не принимаем
        cookieManager.setCookiePolicy (CookiePolicy.ACCEPT_NONE);

        // Сохранение параметров
        currentParams = new ArrayList<> ();

        resetConfig ();
    }

    /**
     * Задать настройки запросов
     */
    public void changeConfig (QueryParams params)
    {
        defaultConfig = params;
        resetConfig ();// Надо сбросить настройки, иначе они применяться только после очередного запроса, а не прям сразу
    }

    /**
     * Очистить куки
     */
    public void clearCookies ()
    {
        cookies.clear ();
    }

    /**
     * Добавить параметр.
     *
     * @param key   Название параметра.
     * @param value Значение параметра.
     *
     * @return Экземпляр текущего объекта
     */
    public Curl p (String key, String value)
    {
        currentParams.add (new CurlParam (key, value, false));

        return this;
    }

    /**
     * Добавить файл к текущему запросу.
     *
     * @param paramName Имя параметра.
     * @param filename  Имя файла.
     *
     * @return Экземпляр текущего объекта
     */
    public Curl file (String paramName, String filename)
    {
        // Зададим вид текущего запроса.
        isMultipart = true;
        currentParams.add (new CurlParam (paramName, filename, true));

        return this;
    }

    /**
     * Добавить к запросу какой-либо HTTP заголовок.
     *
     * @param name  Имя заголовка.
     * @param value Значение.
     *
     * @return
     */
    public Curl header (String name, String value)
    {
        userHeaders.put (name, value);
        return this;
    }

    //--------------------------------------------------------------------------
    // Системные методы
    //--------------------------------------------------------------------------
    /**
     * Спарсить куки и записать их в карту.
     */
    private void parseCookies ()
    {
        String charset = "UTF-8";

        Map<String, List<String>> mapHeaders = connect.getHeaderFields ();
        Set<Map.Entry<String, List<String>>> set = mapHeaders.entrySet ();

        for (Map.Entry<String, List<String>> elem : set)
        {
            if (elem.getKey () != null && elem.getKey ().equals ("Set-Cookie"))
            {
//                System.out.println ("[" + elem.getKey () + "]:");

                for (String value : elem.getValue ())
                {
                    String need = value.split ("; ")[0];
                    String end[] = need.split ("=");

//                    try
//                    {
//                        cookies.put (URLDecoder.decode (end[0], charset),
//                                     URLDecoder.decode (end[1], charset));
                    cookies.put (end[0], end[1]);
//                    }
//                    catch (UnsupportedEncodingException e)
//                    {
//                        System.err.println (e.getMessage ());
//                    }

//                    System.out.println ("    [" + value + "]");
                }
            }
        }

//        System.out.println ("Cookies:");
//        Set<Map.Entry<String, String>> setCookies = cookies.entrySet ();
//        for (Map.Entry<String, String> elem : setCookies)
//        {
//            System.out.println ("    [" + elem.getKey () + "] => [" + elem.getValue () + "]");
//        }
//
//        System.out.println ("---------------------------------");
    }

    /**
     * Добавить куки к текущему HTTP запросу.
     */
    private void sendCookies ()
    {
        // Если сервер вообще задал куки, отправляем их ему
        if (cookies.size () != 0)
        {
            String charset = "UTF-8";
            String cookieString = "";

            Set<Map.Entry<String, String>> setCookies = cookies.entrySet ();
            for (Map.Entry<String, String> elem : setCookies)
            {
                // Если мы уже что-то записали в строку, надо дописать разделитель
                if (!cookieString.equals (""))
                    cookieString += "; ";

//                try
//                {
//                    cookieString += URLEncoder.encode (elem.getKey (), charset)
//                            + "=" + URLEncoder.encode (elem.getValue (), charset);
                cookieString += elem.getKey () + "=" + elem.getValue ();
//                }
//                catch (UnsupportedEncodingException e)
//                {
//                    e.getMessage ();
//                }
            }

//            System.out.println ("[Cookie: " + cookieString + "]");

            connect.setRequestProperty ("Cookie", cookieString);
        }
    }

    /**
     * Записать данные в поток в формате application/x-www-form-urlencoded.
     *
     * @param s Поток для записи.
     */
    private void writeToStreamUrlencoded (OutputStream s)
    {
        try
        {
            DataOutputStream stream = new DataOutputStream (s);
            stream.writeBytes (buildStringQuery ());

            s.flush ();
            s.close ();
        }
        catch (IOException e)
        {
            System.err.println (e.getMessage ());
        }
    }

    /**
     * Записать данные в поток в формате multipart/form-data.
     *
     * @param s Поток для записи
     */
    private void writeToStreamMultipart (OutputStream s, String boundary)
    {
        try
        {
            // Разделитель данных
            String ls = "\r\n";
//                String ls = "\n\n";

            PrintWriter writer = new PrintWriter (new OutputStreamWriter (s), true);

            for (CurlParam param : currentParams)
            {
                // Send normal param.
                writer.append ("--" + boundary).append (ls);

                // Записываем данные в зависимости от того, бинарные ли текущие данные
                if (param.getIsBinary ())
                {
                    writer.append ("Content-Disposition: form-data; name=\""
                            + param.getName () + "\"; filename=\"" + param.getValue () + "\"").append (ls);
                    writer.append ("Content-Type: " + HttpURLConnection.guessContentTypeFromName (param.getValue ())).append (ls);
                    writer.append ("Content-Transfer-Encoding: binary").append (ls);
                    writer.append (ls).flush ();

                    // Пишем файл в поток
                    InputStream input = new FileInputStream (param.getValue ());
                    byte[] buffer = new byte[2048];
                    for (int length = 0; (length = input.read (buffer)) > 0;)
                    {
                        s.write (buffer, 0, length);
                    }
                    s.flush ();
                    writer.append (ls).flush ();
                }
                else
                {
                    writer.append ("Content-Disposition: form-data; name=\"" + param.getName () + "\"").append (ls);
                    writer.append ("Content-Type: text/plain").append (ls);
                    writer.append (ls);
                    writer.append (param.getValue ()).append (ls).flush ();
                }
            }

            writer.append ("--" + boundary + "--" + ls).flush ();

            s.flush ();
            s.close ();
        }
        catch (IOException e)
        {
            System.err.println (e.getMessage ());
        }
    }

    /**
     * Собрать запрос в формате application/x-www-form-urlencoded (обычный POST или GET).
     *
     * @return
     */
    private String buildStringQuery ()
    {
        String charset = "UTF-8";

        try
        {
            String params = "";
            for (CurlParam param : currentParams)
            {
                if (!params.equals (""))
                    params += "&";

                params += URLEncoder.encode (param.getName (), charset)
                        + "="
                        + URLEncoder.encode (param.getValue (), charset);
            }

            return params;
        }
        catch (java.io.UnsupportedEncodingException e)
        {
            e.printStackTrace ();
            return "CURL: buildStringQuery - Unsupported encoding";
        }
    }

    /**
     * Применить текущие настройки
     */
    protected void applyConfig ()
    {
        HttpURLConnection.setFollowRedirects (currentConfig.followLocations);
        connect.setRequestProperty ("User-Agent", currentConfig.userAgent);
        connect.setRequestProperty ("referer", currentConfig.referer);

        // Таймауты
        connect.setConnectTimeout (currentConfig.timeout);// Таймаут на подключение
        connect.setReadTimeout (currentConfig.timeout);// Таймаут на чтение из объекта

        // Куки
        if (currentConfig.isCookie)
            sendCookies ();

        // Добавим к запросу пользовательские хедеры
        Set<Map.Entry<String, String>> set = userHeaders.entrySet ();
        for (Map.Entry<String, String> entry : set)
        {
            connect.setRequestProperty (entry.getKey (), entry.getValue ());
        }
    }

    /**
     * Сбросить текущие настройки.
     */
    private void resetConfig ()
    {
        currentConfig.isCookie = defaultConfig.isCookie;
        currentConfig.followLocations = defaultConfig.followLocations;
        currentConfig.proxy = defaultConfig.proxy;
        currentConfig.referer = defaultConfig.referer;
        currentConfig.timeout = defaultConfig.timeout;
        currentConfig.userAgent = defaultConfig.userAgent;
        currentConfig.charset = defaultConfig.charset;
    }
    //--------------------------------------------------------------------------
    // Основной метод для отправки всех запросов
    //--------------------------------------------------------------------------

    /**
     * Выполнить GET / POST запрос.
     *
     * @param url            URL запроса
     * @param method         Метод запроса - GET или POST
     * @param isDownloadFile Загружается ли файл?
     *
     * @return Ответ
     */
    protected String doBaseQuery (String url, String method, boolean isDownloadFile)
    {
        String urlQuery;// Параметры для добавления в url для GET

        // Параметры для GET
        if (method.equals ("GET"))
            urlQuery = currentParams.isEmpty () ? "" : "?" + buildStringQuery ();
        else
            urlQuery = "";

        try
        {
            // Создаем объект урла
            URL urlO = new URL (url + urlQuery);

            // Создаем соединение
            if (currentConfig.proxy != null)
            {
                connect = (HttpURLConnection) urlO.openConnection (
                        new Proxy (Proxy.Type.HTTP, new InetSocketAddress (currentConfig.proxy.getHost (),
                                                                           currentConfig.proxy.getPort ())));
            }
            else
                connect = (HttpURLConnection) urlO.openConnection ();

            // Настраиваем его
            connect.setRequestMethod (method);
            connect.setUseCaches (false);
            connect.setDoInput (true);
            connect.setDoOutput (true);

            // Применим текущие настройки
            applyConfig ();

            if (method.equals ("POST"))
            {
                if (isMultipart)
                {
                    // Генерим рандомный разделитель
                    Random rand = new Random ();
                    String boundary = Long.toHexString (System.currentTimeMillis ())
                            + Long.toHexString (rand.nextLong ())
                            + Long.toHexString (rand.nextLong ())
                            + Long.toHexString (rand.nextLong ());

                    connect.setRequestProperty ("Content-Type", "multipart/form-data; boundary=" + boundary);
                    writeToStreamMultipart (connect.getOutputStream (), boundary);
                }
                else
                    writeToStreamUrlencoded (connect.getOutputStream ());
            }

            // Выполняем запрос и получаем поток
            InputStream input;

            // Текущий код ответа
            currentResponseCode = connect.getResponseCode ();

            if (currentResponseCode >= 400)
            {
                input = connect.getErrorStream ();
                if (DEBUG)
                    System.err.println ("CURL: HTTP_ERROR! Response code: " + currentResponseCode + " on \"" + (url + urlQuery) + "\"");
            }
            else
                input = connect.getInputStream ();

            // Если в текущем потоке файл, сохраним его
            if (isDownloadFile)
                File.saveFromStream (input, currentDownloadingFilename);

            // Собственно, ответ сервера
            String answer = IOUtils.toString (input, currentConfig.charset);

            // Если необходимо, сохраняем результат запроса в файл
            if (isSave)
                File.saveToFile (answer, "DUMP_QUERIES/" + currentSavingMark + "/" + System.currentTimeMillis () + "_" + countAllQueries + ".html");

            // Если вообще надо, парсим куки
            if (currentConfig.isCookie)
                parseCookies ();

            return answer;
        }
        catch (java.net.MalformedURLException e)
        {
            e.printStackTrace ();
            return "CURL: INVALID URL!";
        }
        catch (java.net.SocketTimeoutException e)
        {
            String message = "CURL: TIMEOUT HAS EXPIRED!";
//            if (DEBUG)
//                System.err.println (message);
            return message;
        }
        catch (java.io.IOException e)
        {
            // Нет коннекта с интернетом / Сервер вернул код ошибки по каким-либо причинам
            String message = "CURL: IOException! on \"" + (url + urlQuery) + "\"";
            if (DEBUG)
                System.err.println (message);
            return message;
        }
        finally
        {
            resetConfig ();// При каждом запросе сбрасываем добавленные динамические настройки
            ++countAllQueries;// Увеличим счетчик запросов
            currentParams.clear ();// Сбросим параметры
            userHeaders.clear ();// Сбросим добавленные хедеры
            isMultipart = false;// Сбросим тип запроса
            isSave = false;// Сбросим необходимость сохранения запроса
            currentResponseCode = -1;// Сбросим код ответа
            currentDownloadingFilename = null;// Очистим имя текущего загружаемого файла
            currentSavingMark = null;// Очистим текущую метку сохранения

            // Вырубаем соединение
            if (connect != null)
                connect.disconnect ();
        }
    }
    //--------------------------------------------------------------------------
    // GET / POST запросы
    //--------------------------------------------------------------------------

    /**
     * Выполнить GET запрос.
     *
     * !!!ВНИМАНИЕ!!! Не рекомендуется задавать GET параметры и в URL и через параметры p ().
     * Нужно использовать что-то одно.
     *
     * @param url URL запроса
     *
     * @return Ответ
     */
    public String get (String url)
    {
        return doBaseQuery (url, "GET", false);
    }

    /**
     * Выполнить POST запрос.
     *
     * @param url URL запроса.
     *
     * @return Ответ
     */
    public String post (String url)
    {
        return doBaseQuery (url, "POST", false);
    }

    /**
     * Загрузить файл. Если файл уже существует, он перезапишется.
     *
     * @param url      URL файла.
     * @param filename Имя, с которым файл будет сохранен.
     */
    public void loadFile (String url, String filename)
    {
        currentDownloadingFilename = filename;
        doBaseQuery (url, "GET", true);
    }

    //--------------------------------------------------------------------------
    // Работа со статистикой запросов
    //--------------------------------------------------------------------------
    /**
     * Увеличить число успешных запросов
     */
    public static synchronized void increaseSuccessfulQueriesCount ()
    {
        ++countSuccessfulQueries;
    }

    /**
     * Вернуть процент успешных запросов.
     *
     * @return
     */
    public static double getQueriesPercentage ()
    {
        return countAllQueries > 0 ? Str.getPercentage ((double) countSuccessfulQueries / countAllQueries) : 0;
    }

    //--------------------------------------------------------------------------
    // Динамические настройки запросов
    //--------------------------------------------------------------------------
    /**
     * Установить Юзер-Агент
     *
     * @return Экземпляр текущего объекта
     */
    public Curl userAgent (String ua)
    {
        currentConfig.userAgent = ua;
        return this;
    }

    /**
     * Установить Реферер
     *
     * @return Экземпляр текущего объекта
     */
    public Curl referer (String ref)
    {
        currentConfig.referer = ref;
        return this;
    }

    /**
     * Кодировка ответа.
     *
     * @param encoding Кодировка.
     *
     * @return
     */
    public Curl charset (String encoding)
    {
        currentConfig.charset = encoding;
        return this;
    }

    /**
     * Задать timeout
     *
     * @param time Время в МИЛИсекундах
     *
     * @return Экземпляр текущего объекта
     */
    public Curl timeout (int time)
    {
        currentConfig.timeout = time;
        return this;
    }

    /**
     * Следовать ли редиректам?
     *
     * @param is true - да, false - нет
     *
     * @return Экземпляр текущего объекта
     */
    public Curl followLocations (boolean is)
    {
        currentConfig.followLocations = is;
        return this;
    }

    /**
     * Задать прокси
     *
     * @param proxy Прокся
     *
     * @return Экземпляр текущего объекта
     */
    public Curl proxy (bugaga.proxy.Proxy proxy)
    {
        currentConfig.proxy = proxy;
        return this;
    }

    /**
     * Задать файл с куками.
     *
     * @param is Принимать ли куки?
     *
     * @return Экземпляр текущего объекта
     */
    public Curl isCookie (boolean is)
    {
        currentConfig.isCookie = is;
        return this;
    }

    /**
     * Сохранить результат запроса в файл.
     *
     * @param mark Метка запроса - папка, куда он будет сохранен.
     *
     * @return
     */
    public Curl dump (String mark)
    {
        isSave = true;
        currentSavingMark = mark;
        return this;
    }
}
