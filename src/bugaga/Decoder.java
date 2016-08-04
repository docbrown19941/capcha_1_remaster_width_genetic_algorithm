package bugaga;

import bugaga.io.Str;

import java.awt.image.*;
import javax.imageio.*;
import java.io.*;

/**
 * Класс для перевода изображения капчи в массив с бинарным (черно-белым) представлением цифр.
 *
 * @author GoGo
 */
public class Decoder
{

    public static long timeTotal = 0;

    /**
     * Класс экзепшена для декодера.
     */
    public class DecoderException extends Exception
    {

        public DecoderException (String message)
        {
            super ("DECODER: " + message);
        }
    }

    /**
     * Длина по X итогового массива.
     */
    public static final int LENGTH_X_RESULT_ARRAY = 20;

    /**
     * Длина по Y итогового массива.
     */
    public static final int LENGTH_Y_RESULT_ARRAY = 30;

    /**
     * Массив с цветами пикселей текущей капчи.
     * Из-за особенностей реализации, СНАЧАЛА X, ПОТОМ Y: (Y;X)
     * <p>
     * TRUE - белый.
     * FALSE - черный.
     */
    protected boolean pixels[][];

    /**
     * Инициализируем массив, в котором будут храниться отдельные очищеные от шума цифры.
     * Массив заведомо больше, чем любая из предполагаемых цифр, чтобы все влезли.
     * Неиспользуемые пиксели внизу справа не будут оказывать влияния на результат,
     * потому что почти всегда там будет пусто.
     * <p>
     * !!!ВНИМАНИЕ!!! Иногда бывает так, что между 2 цифрами не находится даже одного белого столбца.
     * (Такое чаще бывает, если парсить не по цвету самих цифр, а по отсутствию фонового)
     * Поэтому размеры следует выбирать с запасом.
     */
    protected boolean numbers[][][];

    /**
     * Граница местонахождения всех Цифр.
     */
    protected Border borderImage;

    /**
     * Границы всех цифр.
     */
    protected Border[] bordersNumbers;

    /**
     * Объект изображения
     */
    protected BufferedImage bi;

    /**
     * Имя файла с текущей картинкой
     */
    protected String filename;

    /**
     * Минимальная длина линии. Все, что меньше - линией не считается.
     */
    protected static final int MIN_LINE_LENGTH = 5;

    /**
     * Максимальное количество соседних пикселей другого цвета, при котором текущий пиксель считается шумом.
     */
    protected static final int NOISE_MAX_LEVEL = 7;

    /**
     * Минимальное количество пикселей в столбце, чтобы его можно было счесть за границу области нахождения цифр.
     * По X
     */
    protected static final int MIN_IMAGE_BORDER_DETECT_THRESHOLD_X = 2;

    /**
     * Минимальное количество пикселей в столбце, чтобы его можно было счесть за границу области нахождения цифр.
     * По Y
     */
    protected static final int MIN_IMAGE_BORDER_DETECT_THRESHOLD_Y = 3;

    /**
     * Число цифр на картинке.
     */
    protected static final int COUNT_IMAGE_NUMBERS = 4;

    /**
     * Конструктор.
     */
    public Decoder ()
    {
        initNumbers ();
    }

    /**
     * Распознать цифры с переданной картинки.
     *
     * @param _filename Путь к файлу.
     *
     * @return
     * @throws DecoderException Возникает, когда декодеру не удается разделить цифры на изображении.
     */
    public boolean[][][] getArray (String _filename) throws DecoderException
    {
        // Засечем время.
        long timeStart = java.lang.System.currentTimeMillis ();

        // Освободим ресурсы с прошлого раза
        freeResources ();

        // Считаем параметры
        filename = _filename;

        // Парсим цвета в массив
        parsePixelsColors ();

        // Уберем рамку вокруг капчи
        clearBorder ();

        // Убираем шум
        clearNoise ();

        // Находим границу области с цифрами
        borderImage = detectBorders (pixels);

        // Закрасим белым цветом все, что вне найденной области
        pixels = clearArea (borderImage, pixels);

        // Распечатаем изображение
        //        printImage (pixels);

        // Разделим цифры
        separateNumbers ();

        increaseTime ((java.lang.System.currentTimeMillis () - timeStart));

        return numbers;
    }

    protected static synchronized void increaseTime (long _diff)
    {
        timeTotal += _diff;
    }

    public static void printTime ()
    {
        Str.println ("Decoder total time: " + timeTotal + " millis.");
    }

    /**
     * Инициализировать итоговый массив.
     */
    private void initNumbers ()
    {
        numbers = new boolean[COUNT_IMAGE_NUMBERS][LENGTH_Y_RESULT_ARRAY][LENGTH_X_RESULT_ARRAY];
    }

    /**
     * Освободить заполненные массивы пикселей и другие ресурсы, оставшиеся с последнего распознавания.
     */
    protected void freeResources ()
    {
        numbers = null;
        initNumbers ();
        pixels = null;
        filename = null;
        borderImage = null;
        bordersNumbers = null;
        bi = null;
    }

    /**
     * Распечатать изображение.
     */
    public void printImage (boolean[][] pxls)
    {
        // Число окрашеных пикселей
        int countColored = 0;

        // Обходим изображение
        // Обходим по y
        for (int y = 0; y < pxls.length; y++)
        {
            // Обходим по x
            for (int x = 0; x < pxls[y].length; x++)
            {

                //                // Выводим границы области с цифрами
                //                if (x == borderImage.xMin && y == borderImage.yMin)
                //                    System.out.print ("#");
                //
                //                if (x == borderImage.xMax && y == borderImage.yMax)
                //                    System.out.print ("#");

                // Получаем цвет текущего пикселя
                boolean color = pxls[y][x];

                // Выводим
                if (color == true)
                {
                    ++countColored;
                    //                    System.out.print ("1 ");
                    System.out.print ("1");
                }
                else
                {
                    //                    System.out.print ("  ");
                    System.out.print (" ");
                }
            }

            System.out.println ();
        }

        System.out.println ("Count colored pixels: " + countColored + "/" + (pxls.length * pxls[0].length));
    }

    /**
     * Спарсить цвета пикселей в массив.
     * (И сразу обесцветить изображение)
     */
    protected void parsePixelsColors ()
    {
        File file = new File (filename);

        try
        {
            bi = ImageIO.read (file);

            // Инициализируем массив с цветами
            pixels = new boolean[bi.getHeight ()][bi.getWidth ()];

            // Парсим цвета в массив
            // Обходим по y
            for (int y = 0; y < pixels.length; y++)
            {
                // Обходим по x
                for (int x = 0; x < pixels[y].length; x++)
                {
                    int color = bi.getRGB (x, y);

                    //-10066279 - фон.
                    //-86       - основной цвет цифр.
                    pixels[y][x] = color == -86 ? true : false;
                }
            }
        }
        catch (IOException e)
        {
            System.err.println (e.getMessage ());
        }
    }

    /**
     * Убрать шум в данном пикселе.
     *
     * @param x
     * @param y
     */
    protected void clearNoisePixel (int x, int y)
    {
        boolean color = pixels[y][x];

        // Количество совпадений по цвету с соседними пикселями
        int countMatches = 0;

        // Пробуем убрать шум
        // Вверху слева
        if ((x - 1 < 0 || y - 1 < 0) || pixels[y - 1][x - 1] != color)
        {
            ++countMatches;
        }

        // Вверху
        if (y - 1 < 0 || pixels[y - 1][x] != color)
        {
            ++countMatches;
        }

        // Вверху справа
        if ((x + 1 >= pixels[0].length || y - 1 < 0) || pixels[y - 1][x + 1] != color)
        {
            ++countMatches;
        }

        // Слева
        if (x - 1 < 0 || pixels[y][x - 1] != color)
        {
            ++countMatches;
        }

        // Справа
        if (x + 1 >= pixels[0].length || pixels[y][x + 1] != color)
        {
            ++countMatches;
        }

        // Внизу слева
        if ((x - 1 < 0 || y + 1 >= pixels.length) || pixels[y + 1][x - 1] != color)
        {
            ++countMatches;
        }

        // Внизу
        if (y + 1 >= pixels.length || pixels[y + 1][x] != color)
        {
            ++countMatches;
        }

        // Внизу справа
        if ((x + 1 >= pixels[0].length || y + 1 >= pixels.length) || pixels[y + 1][x + 1] != color)
        {
            ++countMatches;
        }

        // Если превышен уровень шума, делаем текущий пиксель белым
        if (countMatches >= NOISE_MAX_LEVEL)
        {
            pixels[y][x] = false;
        }
    }

    /**
     * Убрать рамку вокруг капчи.
     */
    protected void clearBorder ()
    {
        // Обходим изображение
        // Обходим по y
        for (int y = 0; y < pixels.length; y++)
        {
            // Обходим по x
            for (int x = 0; x < pixels[y].length; x++)
            {
                // Собственно, убираем рамку
                if ((y == 1 || y == pixels.length - 2) || (x == 1 || x == pixels[0].length - 2))
                {
                    pixels[y][x] = false;
                }
            }
        }
    }

    /**
     * Убираем шум
     */
    protected void clearNoise ()
    {
        // Обходим изображение
        // Обходим по y
        for (int y = 0; y < pixels.length; y++)
        {
            // Обходим по x
            for (int x = 0; x < pixels[y].length; x++)
            {
                // Убираем шум (цвет пикселя может измениться)
                clearNoisePixel (x, y);
            }
        }
    }

    /**
     * Найти границы информации в заданном массиве.
     *
     * @param pxls Массив, где ищем.
     *
     * @return Найденные границы в координатах переданного массива.
     */
    protected Border detectBorders (boolean pxls[][])
    {
        // Инициализируем границу сначала максимальными/минимальными значениями
        Border border = new Border (pxls[0].length, 0, pxls.length, 0);

        // Массив с количествами окрашеных пикселей в каждом столбце
        int currentBorderLevel_X[] = new int[pxls[0].length];
        // Массив с количествами окрашеных пикселей в каждом столбце
        int currentBorderLevel_Y[] = new int[pxls.length];

        // Обходим изображение
        // Обходим по y
        for (int y = 0; y < pxls.length; y++)
        {
            // Обходим по x
            for (int x = 0; x < pxls[y].length; x++)
            {

                boolean color = pxls[y][x];

                // Определяем, насколько этот пиксель находится дальше/ближе предыдущего
                // (только для закрашеных пикселей)
                if (color)
                {
                    // Массив со встречаемостями пикселей не инициализирован, поэтому,
                    // чтобы не схавтить NullPointerException, учтем эту ситуацию.
                    ++currentBorderLevel_X[x];
                    ++currentBorderLevel_Y[y];

                    // Только если в текущем столбце набралось достаточно окрашеных пикселей,
                    // присваиваем границе новые значения
                    if (currentBorderLevel_X[x] >= MIN_IMAGE_BORDER_DETECT_THRESHOLD_X)
                    {
                        if (x < border.xMin)
                        {
                            border.xMin = x;
                        }
                        if (x > border.xMax)
                        {
                            border.xMax = x;
                        }

                        // Сбрасываем счетчик
                        currentBorderLevel_X[x] = 0;
                    }
                    if (currentBorderLevel_Y[y] >= MIN_IMAGE_BORDER_DETECT_THRESHOLD_Y)
                    {
                        if (y < border.yMin)
                        {
                            border.yMin = y;
                        }
                        if (y > border.yMax)
                        {
                            border.yMax = y;
                        }

                        // Сбрасываем счетчик
                        currentBorderLevel_Y[y] = 0;
                    }
                }
            }
        }

        return border;
    }

    /**
     * Разделить цифры, найти границы и поместить в массив чистые образы.
     * Когда встречается ось X, состоящая полностью из белых пикселей, считаем ее границей цифры.
     */
    protected void separateNumbers () throws DecoderException
    {
        // Номер текущей найденной цифры
        int numberNumber = 0;

        // Левая и правая границы текущей цифры
        int borderLeft = 0;
        int borderRight = 0;

        // Нашли ли цифру к данному моменту?
        boolean isNumberDetected = false;

        // Статус поиска. TRUE, если наткнулись на цифру
        boolean isActiveSearch = false;

        // Число окрашеных пикселей в текущем столбце X
        int countCurrentXColoredPixels;

        // Обходим изображение ПО НАЙДЕННЫМ ГРАНИЦАМ (чтобы сэкономить время и не учитывать шумовые пиксели)
        // Обходим по x
        for (int x = borderImage.xMin; x <= borderImage.xMax; x++)
        {
            // Перед обходом столбца, сбросим счетчик окрашеных пикселей
            countCurrentXColoredPixels = 0;

            // Обходим по y
            for (int y = borderImage.yMin; y <= borderImage.yMax; y++)
            {
                // Если пиксель окрашен
                if (pixels[y][x])
                {
                    ++countCurrentXColoredPixels;
                }
            }

            // Если В ТЕКУЩЕМ СТОЛБЦЕ нашли хоть 1 закрашеный пиксель и, к тому же, еще не начато определение цифры
            if (countCurrentXColoredPixels > 0 && !isActiveSearch)
            {
                borderLeft = x;
                isActiveSearch = true;
            }

            // Если текущий столбец пуст (или подошли к концу области), и, к тому же, мы нашли что-то в прошлый раз
            if ((countCurrentXColoredPixels == 0 || x == borderImage.xMax) && isActiveSearch)
            {
                // Если текущий стоблец пуст, прошлый считаем границой цифры
                borderRight = x - 1;
                isNumberDetected = true;// Закончилась цифра
                isActiveSearch = false;// Определение цифры закончено.
            }

            // Если уже что-то нашли, Пишем цифру в массив
            if (isNumberDetected)
            {
                // Новый массив
                boolean[][] newArray = new boolean[pixels.length][borderRight - borderLeft + 1];

                // Обходим начальную область с уточнением по X
                for (int yN = 0; yN < pixels.length; yN++)
                {

                    // Новый X, отсчитываемый с нуля для нового массива
                    int xNew = 0;

                    for (int xN = borderLeft; xN <= borderRight; xN++)
                    {
                        newArray[yN][xNew] = pixels[yN][xN];
                        ++xNew;
                    }
                }

                // Уточняем границы УЖЕ ЭТОЙ ЦИФРЫ
                Border actualBorder = detectBorders (newArray);

                // Если 2 цифры слились в одну и их не удалось разделить, это является экстренной ситуацией.
                // Генерим исключение.
                try
                {
                    // Помещаем цифру в ИТОГОВЫЙ массив
                    putAreaToNumbers (numberNumber, actualBorder, newArray);
                }
                // Если вылезли за границы массива, значит цифра неверно найдена.
                catch (ArrayIndexOutOfBoundsException e)
                {
                    throw new DecoderException ("INVALID IMAGE");
                }

                //--------------------------------------------------------------
                // Сбрасываем статус
                isNumberDetected = false;

                // Увеличиваем номер следующей цифры
                ++numberNumber;
            }
        }
    }

    /**
     * Поместить выделенную цифру в массив с отдельными цифрами.
     *
     * @param numberNumber Номер цифры по порядку в капче.
     * @param border       Граница цифры в переданном массиве.
     * @param areaArray    Массив с цифрой.
     */
    protected void putAreaToNumbers (int numberNumber, Border border, boolean[][] areaArray)
    {
        // Новые координаты
        int xNew = 0;
        int yNew = 0;

        for (int y = border.yMin; y <= border.yMax; y++)
        {
            xNew = 0;

            for (int x = border.xMin; x <= border.xMax; x++)
            {
                numbers[numberNumber][yNew][xNew] = areaArray[y][x];
                ++xNew;
            }

            ++yNew;
        }
    }

    /**
     * Очистить область с данными.
     * Все, что находится вне переданной границы, будет закрашено белым цветом.
     *
     * @param border Граница области в переданном массиве.
     * @param array  Массив.
     *
     * @return Очищенный массив.
     */
    protected boolean[][] clearArea (Border border, boolean[][] array)
    {
        // Обходим изображение
        // Обходим по y
        for (int y = 0; y < array.length; y++)
        {
            // Обходим по x
            for (int x = 0; x < array[y].length; x++)
            {
                if (x < border.xMin || x > border.xMax || y < border.yMin || y > border.yMax)
                {
                    array[y][x] = false;
                }
            }
        }

        return array;
    }
}
