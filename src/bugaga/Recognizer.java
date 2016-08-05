package bugaga;

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
    protected static double[][][] brainTest = new double[10][Decoder.LENGTH_Y_RESULT_ARRAY][Decoder.LENGTH_X_RESULT_ARRAY];

    /**
     * Массив с МАКСИМАЛЬНЫМИ значениями частоты для каждой цифры.
     */
    protected static double maxFrequencies[] = new double[10];

    /**
     * Распознать код на капче.
     *
     * @param _capchaArray Обработанная картинка с капчей.
     * @param _brainArray  Мозг для этого распознавания.
     *
     * @return Код на капче.
     */
    public static String recognizeBase (boolean[][][] _capchaArray, double[][][] _brainArray)
    {
        TimeTracker.stop ("SEND_DATA_TIME         ");
        TimeTracker.start ("RECOGNIZE_TIME         ");
        String result = "";

        // Обходим все 4 цифры изображения
        for (int n = 0; n < _capchaArray.length; n++)
        {
            // Получим вероятности для первой цифры
            double[] frequencies = getSums (_capchaArray[n], _brainArray);

            // Массив различий
            double[] diffs = new double[10];

            // Распечатаем массив вероятностей
            for (int i = 0; i < frequencies.length; i++)
            {
                diffs[i] = maxFrequencies[i] - frequencies[i];
            }

            int indexMax = getMaxValueIndex (frequencies);// Собственно, это распознанная цифра
            result += indexMax;
        }

        TimeTracker.stop ("RECOGNIZE_TIME         ");
        return result;
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
     * @param _inputArray Массив с цифрой.
     * @param _brain      Массив с мозгом.
     *
     * @return Массив с вероятностями определения каждой цифры на данном изображении.
     */
    protected static double[] getSums (boolean[][] _inputArray, double[][][] _brain)
    {
        double[] result = new double[10];

        // Обходим каждую цифру мозга и для каждой считаем вероятность определения
        for (int i = 0; i < _brain.length; i++)
        {
            // Обходим по y
            for (int y = 0; y < _brain[0].length; y++)
            {
                // Обходим по x
                for (int x = 0; x < _brain[0][0].length; x++)
                {
                    if (_inputArray[y][x])
                    {
                        // Если пиксель черный, суммируем вероятности появления черного цвета в этой точке в общую сумму
                        result[i] += _brain[i][y][x];
                    }
                }
            }
        }

        return result;
    }

    /**
     * Загрузить мозг.
     */
    public static double[][][] getBrainArrayFromBrainString (String _brainString)
    {
        // Создадим массив для мозга
        double[][][] brainArray = new double[10][Decoder.LENGTH_Y_RESULT_ARRAY][Decoder.LENGTH_X_RESULT_ARRAY];

        String[] array = _brainString.split ("\n");

        for (String currentStr : array)
        {
            String[] info = currentStr.split ("\\|");

            // Собственно, пишем данные в память
            brainArray[Integer.valueOf (info[0])][Integer.valueOf (info[1])][Integer.valueOf (info[2])] = Double
                    .parseDouble (info[3]);
        }

        // Посчитаем максимальные вероятности для каждой цифры
        for (int i = 0; i < brainArray.length; i++)
        {
            for (int y = 0; y < brainArray[0].length; y++)
            {
                for (int x = 0; x < brainArray[0][0].length; x++)
                {
                    maxFrequencies[i] += brainArray[i][y][x];
                }
            }
        }

        return brainArray;
    }
}
