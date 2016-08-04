package bugaga.io;

import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Класс для работы с файлами.
 *
 * ИДЕИ:
 * - Сделать буфферную запись в файл.
 * Методом добавляются сколь угодно мелкие порции данных, и когда буфер наполняется до нужного размера, записываются в файл.
 * Чтобы не нагружать файл-систему.
 *
 * @author GoGo
 */
public class File
{

    /**
     * Размер буффера - количество порций данных - для поэтапной записи
     */
    public static int bufferSize = 500;

    /**
     * Буфер для поэтапной записи
     */
    private static String buffer = "";

    /**
     * Дописать данные в файл.
     *
     * @param data     Дописываемые данные.
     * @param filename Имя файла.
     */
    public static synchronized void appendToFile (String data, String filename)
    {
        baseWriteToFile (data, filename, true);
    }

    /**
     * Сохранить данные в файл.
     *
     * @param data     Записываемые данные.
     * @param filename Имя файла.
     */
    public static synchronized void saveToFile (String data, String filename)
    {
        baseWriteToFile (data, filename, false);
    }

    /**
     * Записать / дописать данные в файл.
     * ПРИМЕЧАНИЕ: если путь к файлу не существует, директории будут созданы.
     *
     * @param data     Дописываемые данные.
     * @param filename Имя файла.
     * @param isAppend ДОПИСЫВАТЬ ли данные?
     */
    private static void baseWriteToFile (String data, String filename, boolean isAppend)
    {
        // Объект заданного ФАЙЛА
        java.io.File file = new java.io.File (filename);

        // Объект ПУТИ к этому файлу
        java.io.File path = file.getParentFile ();

        // Если директория не существует, создадим ее
        if (path != null && !path.exists ())
            path.mkdirs ();

        try (FileWriter someText = new FileWriter (file, isAppend))
        {
            someText.write (data + "\n");
        }
        catch (java.io.IOException e)
        {
            System.out.println ("CURL: FUCK! Данные не записаны! " + e.getMessage ());
        }
    }

    /**
     * Добавить данные к буферу для последующей записи.
     *
     * @param data     Дописываемые данные.
     * @param filename Имя файла.
     */
    public static synchronized void appendToBuffer (String data, String filename)
    {
    }

    /**
     * Сохранить (перезаписать) файл из InputStream.
     *
     * @param stream   Входной поток.
     * @param filename Имя файла.
     */
    public static void saveFromStream (InputStream stream, String filename)
    {
        // Задаем размер буффера
        byte[] buf = new byte[4096];
        int n = -1;

        try
        {
            OutputStream output = new FileOutputStream (filename);
            while ((n = stream.read (buf)) != -1)
            {
                if (n > 0)
                {
                    output.write (buf, 0, n);
                }
            }
            output.close ();
        }
        catch (java.io.IOException e)
        {
            System.err.println (e.getMessage ());
        }
    }

    /**
     * Считать построчно файл в лист.
     * (UTF-8, формат строк - UNIX).
     *
     * @param filename Имя файла.
     *
     * @return
     */
    public static List<String> loadByLines (String filename)
    {
        try
        {
            byte[] encoded = Files.readAllBytes (Paths.get (filename));
            Charset charset = Charset.forName ("UTF-8");

            String str = charset.decode (ByteBuffer.wrap (encoded)).toString ();
            String[] lines = str.split ("\n");

            return Arrays.asList (lines);
        }
        catch (IOException e)
        {
            System.err.println (e.getMessage ());
            return new ArrayList<> ();
        }
    }
}
