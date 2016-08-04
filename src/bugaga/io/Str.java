package bugaga.io;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Класс для работы со строками
 *
 * ИДЕИ:
 * - Сделать статический синхронизированный вывод в нужной кодировке, с счетчиком
 *
 * @author GoGo
 */
public class Str
{

    /**
     * Режим вывода - windows или linux
     */
    public static String mode = "linux";

    /**
     * Счетчик строк для вывода
     */
    private static int count = 0;

    /**
     * Вернуть строку в нужной кодировке.
     *
     * @param str Строка.
     *
     * @return Строка в новой кодировке.
     */
    public static String getCharset (String str, String charset)
    {
        try
        {
            return new String (str.getBytes (charset));
        }
        catch (java.io.UnsupportedEncodingException e)
        {
            return "bugaga/Str: Неподдерживаемая кодировка";
        }
    }

    /**
     * Печать строки в нужной кодировки, в соответствии с режимом.
     * Строки по умолчанию не считаются.
     *
     * @param str Строка.
     */
    public static synchronized void println (String str)
    {
        basePrintln (str, false, -1);
    }

    /**
     * Печать строки в нужной кодировки, в соответствии с режимом.
     *
     * @param str     Строка.
     * @param isCount Считать ли строки при выводе?
     * @param of      Сколько всего выводимых строк существует (если надо вообще считать строки).
     *                Скрипт будет выводить в стиле 1235/of).
     */
    public static synchronized void println (String str, boolean isCount, int of)
    {
        basePrintln (str, isCount, of);
    }

    /**
     * Печать строки в нужной кодировки, в соответствии с режимом
     *
     * @param str     Строка.
     * @param isCount Считать ли строки при выводе?
     * @param of      Сколько всего выводимых строк существует.
     *                Скрипт будет выводить в стиле 1235/of).
     */
    private static synchronized void basePrintln (String str, boolean isCount, int of)
    {
        switch (mode)
        {
            case "linux":
                str = getCharset (str, "UTF-8");
                break;

            default:
            case "windows":
                str = getCharset (str, "CP-866");
                break;
        }


        if (isCount)
        {
            System.out.print (++count + (of > 0 ? "/" + of : "") + (of > 0 ? " ["
                    + getPercentage ((double) count / of) + "%]" : "") + ") ");
        }

        System.out.println (str);
    }

    /**
     * Получить процентное представление входящих данных.
     *
     * @param init
     *
     * @return
     */
    public static double getPercentage (double init)
    {
        BigDecimal bd = new BigDecimal (init * 100).setScale (2, RoundingMode.CEILING);

        return bd.doubleValue ();
    }

    /**
     * Сбросить счетчик строк при выводе
     */
    public static synchronized void resetStringCount ()
    {
        count = 0;
    }

    /**
     * Вернуть индекс максимального элемента массива.
     * Ява ебананутая хуета, я должен для это дерьма свой велосипед писать, фейспалм...
     *
     * @param array
     *
     * @return
     */
    public static int getMaxValueIndex (double[] array)
    {
        double currentMax = array[0];
        int currentMaxValueIndex = 0;

        for (int i = 0; i < array.length; i++)
        {
            if (array[i] > currentMax)
            {
                currentMax = array[i];
                currentMaxValueIndex = i;
            }
        }

        return currentMaxValueIndex;
    }

    public static int getMaxValueIndex (int[] array)
    {
        double currentMax = array[0];
        int currentMaxValueIndex = 0;

        for (int i = 0; i < array.length; i++)
        {
            if (array[i] > currentMax)
            {
                currentMax = array[i];
                currentMaxValueIndex = i;
            }
        }

        return currentMaxValueIndex;
    }
}
