package bugaga.neuro;

import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;

import com.google.gson.*;

import bugaga.io.*;
import bugaga.Decoder;
import java.io.FileNotFoundException;

/**
 * Структура сети. Координаты присоединения связей к источнику сигнала и их веса.
 */
class Struct
{

    /**
     * Класс для хранения данных структуры.
     */
    class Data
    {

        /**
         * Число входов каждого нейрона для каждого слоя
         */
        protected int[] countEachNeuronSynapses;

        /**
         * Общее число пикселей нулевого слоя
         */
        protected int countZeroLayerPixels;

        /**
         * Число нейронов в слоях
         */
        protected int[] sizes;

        /**
         * Информация о связях каждого нейрона сети - синапсах.
         *
         * [номер слоя нейрона][номер нейрона][номер связи] = (Номер слоя данных, Координата в векторе данных
         */
        protected InfoSynapses[][][] infoSynapses;

    }

    protected Data data;

    /**
     * Файл текущей структуры
     */
    private java.io.File currentFile;

    /**
     * Объект рандома для этого класса.
     */
    private static Random rand = new Random ();

    /**
     * Инициализировать структуру.
     *
     * @param filename Имя файла со структурой.
     */
    public Struct (String filename)
    {
        currentFile = new java.io.File (filename);
        data = new Data ();
    }

    /**
     * Создать рандомную структуру.
     *
     * 2-x слойный перцептрон.
     */
    protected void createRand ()
    {
        data.countEachNeuronSynapses = new int[2];// 2 слоя
        data.countEachNeuronSynapses[0] = 5;// 1-й слой
        data.countEachNeuronSynapses[1] = 20;// 2-й слой

        data.countZeroLayerPixels = Decoder.LENGTH_X_RESULT_ARRAY * Decoder.LENGTH_Y_RESULT_ARRAY;

        data.sizes = new int[2];// 2 слоя
        data.sizes[0] = 150;
        data.sizes[1] = 10;

        // [номер слоя нейрона][номер нейрона][номер связи]
        data.infoSynapses = new InfoSynapses[data.sizes.length][data.sizes[Str.getMaxValueIndex (data.sizes)]][data.countEachNeuronSynapses[Str.getMaxValueIndex (data.countEachNeuronSynapses)]];

        // Проходимся по нейронам 1-го слоя
        for (int nNeuron = 0; nNeuron < data.sizes[0]; nNeuron++)
        {
            // Соединяем связи с пикселями
            for (int nSynapse = 0; nSynapse < data.countEachNeuronSynapses[0]; nSynapse++)
            {
                // [номер слоя нейрона][номер нейрона][номер связи] = (Номер слоя данных, Координата в векторе данных)
                data.infoSynapses[0][nNeuron][nSynapse] = new InfoSynapses (0,
                                                                            rand.nextInt (data.countZeroLayerPixels),
                                                                            (1 + rand.nextInt (2)));
            }
        }

        // Проходимся по нейронам 2-го слоя
        for (int nNeuron = 0; nNeuron < data.sizes[1]; nNeuron++)
        {
            // Соединяем связи с пикселями
            for (int nSynapse = 0; nSynapse < data.countEachNeuronSynapses[1]; nSynapse++)
            {
                // [номер слоя нейрона][номер нейрона][номер связи] = (Номер слоя данных, Координата в векторе данных)
                data.infoSynapses[1][nNeuron][nSynapse] = new InfoSynapses (1,
                                                                            rand.nextInt (data.sizes[0]),
                                                                            (1 + rand.nextInt (2)));
            }
        }
    }

    public void dump ()
    {
        String out = new Gson ().toJson (data);
        File.saveToFile (out, currentFile.getAbsolutePath ());
    }

    public void unDump ()
    {
        Gson gson = new Gson ();

        try
        {
            BufferedReader br = new BufferedReader (new FileReader (currentFile));

            data = gson.fromJson (br, Data.class);
        }
        catch (FileNotFoundException e)
        {
            System.err.println (e.getMessage ());
        }
    }
}