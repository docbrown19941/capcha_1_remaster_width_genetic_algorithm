package bugaga;

import java.util.List;

import bugaga.io.*;

/**
 * Собственно, распознователь кода на капче.
 *
 * @author GoGo
 */
public class Recognizer
{

    /**
     * Массив с дампом мозга.
     * [цифра][y][x] = вероятность появления черного цвета.
     */
    protected static double[][][] brain = new double[10][Decoder.LENGTH_Y_RESULT_ARRAY][Decoder.LENGTH_X_RESULT_ARRAY];

    /**
     * Массив с МАКСИМАЛЬНЫМИ значениями частоты для каждой цифры.
     */
    protected static double maxFrequencies[] = new double[10];

    /**
     * Глобальный конструктор.
     */
    public static synchronized void init (String brainFilename)
    {
        System.out.println ("Initialize the Recognizer...");

        // Грузим мозги в память
        loadBrain (brainFilename);
    }

    /**
     * Распознать код на капче.
     *
     * @param filename Картинка с капчей.
     *
     * @return Код на капче.
     */
    public static String recognize (String filename)
    {
        Decoder d = new Decoder ();
        String result = "";

        try
        {
            // Получим массив от декодера
            boolean[][][] dArray = d.getArray (filename);

            // Обходим все 4 цифры изображения
            for (int n = 0; n < dArray.length; n++)
            {

                // Получим вероятности для первой цифры
                double[] frequencies = getSums (dArray[n]);

                // Массив различий
                double[] diffs = new double[10];

                // Распечатаем массив вероятностей
                for (int i = 0; i < frequencies.length; i++)
                {
                    diffs[i] = maxFrequencies[i] - frequencies[i];

//                    System.out.println (i + ") " + frequencies[i] + " Diff: " + diffs[i]);
                }

                int indexMax = getMaxValueIndex (frequencies);// Собственно, это распознанная цифра
                result += indexMax;

                int indexDiffMax = getMinValueIndex (diffs);
//                result += indexDiffMax;

//                System.out.println ();
////                System.out.println ("Max value: " + frequencies[indexMax]);
//                System.out.println ("Max index: " + indexMax);
////                System.out.println ("Max diff value: " + diffs[indexDiffMax]);
//                System.out.println ("Max diff index: " + indexDiffMax);
//                System.out.println ();
            }

            return result;
        }
        catch (Decoder.DecoderException e)
        {
            System.err.println (e.getMessage ());
            return e.getMessage ();
        }
    }

    /**
     * Вернуть индекс максимального элемента массива.
     * Ява ебананутая хуета, я должен для это дерьма свой велосипед писать, фейспалм...
     *
     * @param array
     *
     * @return
     */
    protected static int getMaxValueIndex (double[] array)
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

    protected static int getMinValueIndex (double[] array)
    {
        double currentMin = array[0];
        int currentMinValueIndex = 0;

        for (int i = 0; i < array.length; i++)
        {
            if (array[i] < currentMin)
            {
                currentMin = array[i];
                currentMinValueIndex = i;
            }
        }

        return currentMinValueIndex;
    }

    /**
     * Получить суммы, характеризующие данную цифру. Максимальная сумма помогает определить цифру.
     *
     * @param inputArray Массив с цифрой.
     *
     * @return Массив с вероятностями определения каждой цифры на данном изображении.
     */
    protected static double[] getSums (boolean[][] inputArray)
    {
        double[] result = new double[10];

        // Обходим каждую цифру мозга и для каждой считаем вероятность определения
        for (int i = 0; i < brain.length; i++)
        {
            // Обходим по y
            for (int y = 0; y < brain[0].length; y++)
            {
                // Обходим по x
                for (int x = 0; x < brain[0][0].length; x++)
                {
                    if (inputArray[y][x])
                    {
                        // Если пиксель черный, суммируем вероятности появления черного цвета в этой точке в общую сумму
                        result[i] += brain[i][y][x];
                    }
//                    else
//                        result[i] += (1 - brain[i][y][x]);
                }
            }
        }

        return result;
    }

    /**
     * Загрузить мозг.
     */
    protected static synchronized void loadBrain (String brainFilename)
    {
        // Получаем лист со ВСЕМИ строками из мозг-файла, в.ч. и пустыми
        List<String> list = File.loadByLines (brainFilename);

        System.out.println ("Brain size: " + list.size () + ".");
        System.out.println ();

        for (String currentStr : list)
        {
            String[] info = currentStr.split ("\\|");

            // Собственно, пишем данные в память
            brain[Integer.valueOf (info[0])][Integer.valueOf (info[1])][Integer.valueOf (info[2])] = Double.parseDouble (info[3]);
        }

        // Посчитаем максимальные вероятности для каждой цифры
//        System.out.println ("Max frequencies:");

        for (int i = 0; i < brain.length; i++)
        {
            for (int y = 0; y < brain[0].length; y++)
            {
                for (int x = 0; x < brain[0][0].length; x++)
                {
                    maxFrequencies[i] += brain[i][y][x];
                }
            }
        }

//        for (int i = 0; i < maxFrequencies.length; i++)
//        {
//            System.out.println (i + "# " + maxFrequencies[i]);
//        }
//        System.out.println ();
    }
}
