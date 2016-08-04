package bugaga.curl;

/**
 * Класс параметра Curl.
 *
 * @author GoGo
 */
public class CurlParam
{

    /**
     * Имя параметра
     */
    private String name;

    /**
     * Значение.
     * Если параметр - бинарный файл, то здесь находится путь к этому файлу.
     */
    private String value;

    /**
     * True, если параметр - бинарный файл
     */
    private boolean isBinary;

    /**
     * Конструктор. Инициализация параметров.
     *
     * @param n  Имя параметра.
     * @param v  Значение параметра.
     * @param is True, если параметр - бинарный файл.
     */
    public CurlParam (String n, String v, boolean is)
    {
        name = n;
        value = v;
        isBinary = is;
    }

    /**
     * Получить имя.
     *
     * @return
     */
    public String getName ()
    {
        return name;
    }

    /**
     * Получить значение.
     *
     * @return
     */
    public String getValue ()
    {
        return value;
    }

    /**
     * Получить значение флага isBinary.
     *
     * @return
     */
    public boolean getIsBinary ()
    {
        return isBinary;
    }
}
