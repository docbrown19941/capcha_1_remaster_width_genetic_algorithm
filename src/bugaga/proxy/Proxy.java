package bugaga.proxy;

import java.util.Objects;

/**
 * Класс прокси для либы.
 *
 * @author GoGo
 */
public class Proxy
{

    /**
     * Хост
     */
    protected String host;

    /**
     * Порт
     */
    protected int port;

    /**
     * Конструктор - инициализация параметров.
     *
     * @param h Хост.
     * @param p Порт.
     */
    public Proxy (String h, int p)
    {
        host = h;
        port = p;
    }

    /**
     * Сравнение объектов.
     * ЗАМЕТКА: сделать сравнение через hashCode ().
     *
     * @param ob
     *
     * @return
     */
    public boolean equals (Object ob)
    {
//        Proxy proxy = (Proxy) ob;
//
//        return host.equals (proxy.getHost ()) && port == proxy.getPort ();

        return hashCode () == ob.hashCode ();
    }

    /**
     * Переопределяем хеш-код.
     *
     * @return хэш
     */
    @Override
    public int hashCode ()
    {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode (this.host);
        hash = 67 * hash + this.port;
        return hash;
    }

    /**
     * Вернуть хост.
     *
     * @return
     */
    public String getHost ()
    {
        return host;
    }

    /**
     * Вернуть порт.
     *
     * @return
     */
    public int getPort ()
    {
        return port;
    }

    /**
     * Вернуть полный адрес.
     *
     * @return
     */
    public String getFull ()
    {
        return host + ":" + port;
    }
}
