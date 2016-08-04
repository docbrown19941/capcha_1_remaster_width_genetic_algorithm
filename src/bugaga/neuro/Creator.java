package bugaga.neuro;

import java.util.Random;
import java.util.LinkedList;

import bugaga.Decoder;

/**
 * Создатель архитектуры сети.
 *
 * @author GoGo
 */
public class Creator
{

    /**
     * Папка для дампа структур
     */
    public static String structFoldername = "structs";

    /**
     * Текущая структура
     */
    protected static Struct struct;

    /**
     * Объект рандома для этого класса.
     */
    private static Random rand = new Random ();

    /**
     * Массив с со слоями нейронов.
     *
     * 0 - первый слой;
     * 1 - второй слой;
     * 2 - и т.д;
     *
     * Слои сети.
     * Слой - группа нейронов с торчащими из них связями.
     * Нулевой слой (пиксели) не считаем.
     */
    protected static Neuron[][] layers;

    /**
     * Лист с векторами данных.
     *
     * 0 - данные с пикселей; НУЛЕВОЙ СЛОЙ. Входные сигналы с изображения.
     * Изображение храниться в виде одномерного вектора.
     *
     * 1 - данные с выходов нейронов 1-го слоя;
     * 2 - данные с выходов нейронов 2-го слоя и т.д;
     */
    protected static LinkedList<LinkedList<Double>> data = new LinkedList<> ();

    /**
     * Инициализировать сеть.
     */
    public static void init ()
    {
        insertPixelsDataArray ();

        String name = structFoldername + "/struct_" + System.currentTimeMillis () + ".txt";
        struct = new Struct (name);

        struct.createRand ();
        struct.dump ();
    }

    protected static void initArrays ()
    {
    }

    protected static void insertPixelsDataArray ()
    {
        Decoder d = new Decoder ();
        try
        {
            boolean[][][] arr = d.getArray ("test/test.gif");

            // Найдем максимально возможный размер масси

            // Инициализируем первый слой данных
            LinkedList<Double> list0 = new LinkedList<> ();

            // Помещаем ПЕРВУЮ цифру капчи в нулевой вектор
            for (int y = 0; y < arr[0].length; y++)
            {
                for (int x = 0; x < arr[0][0].length; x++)
                {
                    // data[0], потому что НУЛЕВОЙ слой
                    list0.add (new Double (arr[0][y][x] ? 1 : 0));
                }
            }
            data.add (list0);
        }
        catch (Decoder.DecoderException e)
        {
            System.err.println (e.getMessage ());
        }
    }
}
