package bugaga;

import java.io.*;

import bugaga.io.Str;
import bugaga.io.Config;

/**
 * Класс учителя. Сканит изображения из обучающей выборки и выдает итоговый файл с конфигом.
 *
 * @author GoGo
 */
public class Teacher
{

    /**
     * Файлы подготовленных капч.
     */
    protected File[] files;

    protected String capchiesDir;

    protected Decoder decoder;

    /**
     * Класс пикселя.
     */
    class Pixel
    {

        public int countBlack = 0;

        public int countWhite = 0;

        public void increaseBlack ()
        {
            ++countBlack;
        }

        public void increaseWhite ()
        {
            ++countWhite;
        }

        /**
         * Вернуть частоту появления черного цвета в этом пикселе.
         *
         * @return
         */
        public double getFriquencyBlack ()
        {
            return (double) countBlack / (countBlack + countWhite);
        }
    }

    /**
     * Массив с частотами раскраски каждого пикселя цифры в черный цвет для каждой цифры.
     * [цифра][Y][X][частота]
     */
    Pixel[][][] pixels = new Pixel[10][Decoder.LENGTH_Y_RESULT_ARRAY][Decoder.LENGTH_X_RESULT_ARRAY];

    /**
     * Конструктор.
     *
     * @param folder Папка с обучающей выборкой.
     */
    public Teacher (String folder)
    {
        capchiesDir = folder;
        decoder = new Decoder (capchiesDir);
    }

    /**
     * Переобучить систему. Сформировать новый конфиг-файл.
     */
    public synchronized void reTeach ()
    {
        scanDir ();

        // Проходимся по капчам
        for (File file : files)
        {
            String out = "";// Строка для вывода

            String num = file.getName ().split ("_")[0];

            out += file.getName () + " - ";

            // Декодируем
            try
            {
                boolean[][][] boolArray = decoder.getArray (file.getAbsolutePath ());

                // Пишем стату в массив вероятностей.
                // Обходим 4 цифры на изображении
                for (int i = 0; i < Decoder.COUNT_IMAGE_NUMBERS; i++)
                {
                    // Обходим черно-белое декодированное изображение
                    for (int x = 0; x < Decoder.LENGTH_X_RESULT_ARRAY; x++)
                    {
                        for (int y = 0; y < Decoder.LENGTH_Y_RESULT_ARRAY; y++)
                        {
                            String lol = String.valueOf (num.charAt (i));
                            int index = Integer.valueOf (lol);

                            // ЕСЛИ НЕОБХОДИМО, создаем объект пикселя в массиве пикселей-частот
                            if (pixels[index][y][x] == null)
                            {
                                pixels[index][y][x] = new Pixel ();
                            }

                            // Сравниваем цвет
                            if (boolArray[i][y][x])
                            {
                                pixels[index][y][x].increaseBlack ();
                            }
                            else
                            {
                                pixels[index][y][x].increaseWhite ();
                            }
                        }
                    }
                }

                out += "OK";
            }
            catch (Decoder.DecoderException e)
            {
                // Если не удалось декодировать изображение
                out += e.getMessage ();
            }

            // Выводим стату
            Str.println (out, true, files.length);
        }

        saveResultToFile ();
    }

    /**
     * Сохранить конфиг в файл.
     * <p>
     * Формат файла:
     * В каждой строке 1 пиксель:
     * цифра|y|x|частота появления черного цвета.
     */
    protected void saveResultToFile ()
    {
        String result = "";

        // Обходим массив пикселей
        for (int i = 0; i < pixels.length; i++)
        {
            for (int y = 0; y < Decoder.LENGTH_Y_RESULT_ARRAY; y++)
            {
                for (int x = 0; x < Decoder.LENGTH_X_RESULT_ARRAY; x++)
                {
                    double frequency = pixels[i][y][x].getFriquencyBlack ();

                    result += i + "|" + y + "|" + x + "|" + frequency + "\n";
                }

                //                result += "\n";
            }

            //            result += "\n";
        }

        bugaga.io.File.saveToFile (result,
                Config.getString ("brainFoldername") + "/brain_" + System.currentTimeMillis () + ".txt");
    }

    /**
     * Просканить папку с капчами и получить массив со всеми ее файлами
     */
    protected synchronized void scanDir ()
    {
        File fileDir = new java.io.File (capchiesDir);
        files = fileDir.listFiles ();

        System.out.println ("Total capchies for learning: " + files.length + "...");
    }
}
