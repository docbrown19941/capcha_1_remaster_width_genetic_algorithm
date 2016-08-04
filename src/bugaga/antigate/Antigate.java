package bugaga.antigate;

import bugaga.curl.*;

/**
 * Класс для распознавания капч через антигейт.
 *
 * @author GoGo
 */
public class Antigate
{

    /**
     * Ключ доступа на Antigate.com
     */
    private String apiKey;

    /**
     * Общий объект курла для запросов
     */
    private Curl curl;

    /**
     * Настройки запросов
     */
    private QueryParams params;

    /**
     * Конструктор
     */
    public Antigate (String key)
    {
        apiKey = key;

        params = new QueryParams ();
        params.followLocations = true;
        params.isCookie = false;
        params.timeout = 5000;
        params.userAgent = "Мотоцикл «Урал» М-52С";
        params.referer = "";

        curl = new Curl (params);
    }

    /**
     * Отослать капчу на антигейт.
     *
     * @param filename Файл с капчей.
     *
     * @return Ид капчи.
     */
    protected int sendCapcha (String filename) throws AntigateException
    {
        String page1 = curl
                .p ("method", "post")
                .p ("key", apiKey)
                .file ("file", filename)
                .post ("http://antigate.com/in.php");

        String answer[] = page1.split ("\\|");

        if (!answer[0].equals ("OK"))
            throw new AntigateException ("Antigate: ERROR: \"" + page1 + "\"");
        else
            return Integer.valueOf (answer[1]);
    }

    /**
     * Запросить статус капчи с данным ид.
     * Если капча разгадана, будет возвращен текст.
     *
     * @param id Ид запрашиваемой капчи.
     *
     * @return
     */
    protected String getCapchaStatus (int id) throws AntigateException
    {
        String page = "CAPCHA_NOT_READY";

        try
        {
            // Запрашиваем в цикле статус капчи.
            // Если капча все еще не готова, спим
            while (page.equals ("CAPCHA_NOT_READY"))
            {
                page = curl.get ("http://antigate.com/res.php?key=" + apiKey + "&action=get&id=" + id);

                // Если возникли ошибки, генерим исключение
                if (page.equals ("ERROR_KEY_DOES_NOT_EXIST")
                        || page.equals ("ERROR_WRONG_ID_FORMAT")
                        || page.equals ("ERROR_CAPTCHA_UNSOLVABLE"))
                    throw new AntigateException ("Antigate: ERROR: \"" + page + "\"");

//                System.out.print (".");
                Thread.sleep (1000);
            }

            return page.split ("\\|")[1];
        }
        catch (InterruptedException e)
        {
            System.err.println (e.getMessage ());
            return "";
        }
    }

    /**
     * Распознать капчу.
     *
     * @param filename Имя файла.
     *
     * @throws AntigateException
     *
     * @return Текст с капчи.
     */
    public String recognize (String filename) throws AntigateException
    {
        int id = sendCapcha (filename);
        return getCapchaStatus (id);
    }
}
