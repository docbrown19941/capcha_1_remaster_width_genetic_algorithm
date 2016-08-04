package bugaga.io;

import java.io.File;
import java.io.IOException;

import org.ini4j.Ini;
import org.ini4j.Profile.Section;

/**
 * Класс для доступа к настройкам из ini-файла.
 *
 * @author GoGo
 */
public class Config
{

    /**
     * Config filename.
     */
    private static String confFilename;

    /**
     * The main config section.
     */
    private static Section main;

    /**
     * Get some STRING param.
     *
     * @param name Param name.
     *
     * @return Param value.
     */
    public static String getString (String name)
    {
        return main.get (name);
    }

    /**
     * Get some INTEGER param.
     *
     * @param name Param name.
     *
     * @return Param value.
     */
    public static int getInt (String name)
    {
        return Integer.valueOf (main.get (name));
    }

    /**
     * Initialisation
     *
     * @param _filename Файл с конфигом
     */
    public static void init (String _filename)
    {
        confFilename = _filename;

        try
        {
            // Парсим конфиг из ini файла
            Ini ini = new Ini (new File (confFilename));
            main = ini.get ("main");
        }
        catch (IOException e)
        {
            System.err.println (e.getMessage ());
        }
    }
}
