package bugaga.antigate;

/**
 * Исключение антигейта.
 *
 * @author GoGo
 */
public class AntigateException extends Exception
{

    /**
     * Конструктор.
     *
     * @param message Сообщение.
     */
    public AntigateException (String message)
    {
        super (message);
    }
}
