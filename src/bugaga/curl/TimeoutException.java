package bugaga.curl;

/**
 * Исключение
 *
 * @author GoGo
 */
public class TimeoutException extends Exception
{

    /**
     * Сообщение
     *
     * @return
     */
    public String getMessage ()
    {
        return "CURL: TIMEOUT HAS EXPIRED!";
    }
}
