package bugaga;

import java.util.Random;

import bugaga.curl.*;
import bugaga.io.*;
import bugaga.regex.*;

/**
 * Класс потока загрузки изображений.
 *
 * @author GoGo
 */
public class DownThread // implements Runnable
{

//    /**
//     * Класс параметров запуска.
//     */
//    private static class Params
//    {
//
//        private int downTimeout;
//
//        private int countDownImages;
//
//        private String capchaFolder;
//
//        private String capchaRecognizedFolder;
//
//        private String recognizerBrainFilename;
//
//    }
//
//    public Thread t;
//
//    private static Random rand = new Random ();
//
//    /**
//     * Число гудов
//     */
//    private static volatile int currentImageNumber = 0;
//
//    /**
//     * Общее число капч, которые скрипт попытался распознать.
//     */
//    private static volatile int countAll = 0;
//
//    private java.io.File currentCapchaFile;
//
//    protected QueryParams qweryParams = new QueryParams ();
//
//    protected Curl curl;
//
//    /**
//     * Параметры запуска потоков.
//     */
//    protected static Params params = new Params ();
//
//    /**
//     * Глобальный конструктор.
//     *
//     * @param _capchaFolder            Папка для первоначального сохранения капч.
//     * @param _capchaRecognizedFolder  Папка для распознанных капч.
//     * @param _recognizerBrainFilename Имя файла с мозгом для рекогнайзера.
//     * @param _countDownImages         Сколько капч грузить?
//     * @param _downTimeout             Таймаут загрузки.
//     */
//    public static void init (
//            String _capchaFolder,
//            String _capchaRecognizedFolder,
//            String _recognizerBrainFilename,
//            int _countDownImages,
//            int _downTimeout)
//    {
//        params.capchaFolder = _capchaFolder;
//        params.capchaRecognizedFolder = _capchaRecognizedFolder;
//        params.recognizerBrainFilename = _recognizerBrainFilename;
//        params.countDownImages = _countDownImages;
//        params.downTimeout = _downTimeout;
//
//    }
//
//    public DownThread ()
//    {
//        qweryParams.timeout = params.downTimeout;
//        qweryParams.followLocations = true;
//        qweryParams.isCookie = true;
//        qweryParams.userAgent = QueryParams.getRandUA ();
//        qweryParams.referer = "LoL";
//        qweryParams.charset = QueryParams.UTF_8;
//        curl = new Curl (qweryParams);
//
//        (t = new Thread (this, "DownThread")).start ();
//    }
//
//    @Override
//    public void run ()
//    {
//        while (currentImageNumber < params.countDownImages)
//        {
//            // Чистим куки
//            curl.clearCookies ();
//            qweryParams.userAgent = QueryParams.getRandUA ();
//
//            // Грузим страницу ввода ника
//            String page1 = curl.get ("http://spaces.ru/reg/?step_new=1");
//
//            //name="step_new2" value="111938207490" />
//            // Парсим токены
//            Preg preg11 = new Preg ("name=\"step_new2\" value=\"([0-9]+)\" />", page1);
//            String token11 = "";
//            if (preg11.get (1).isEmpty ())
//            {
////                Str.println ("ERROR: token11 not found!");
//            }
//            else
//            {
//                token11 = preg11.get (1).get (0);
////                Str.println ("Token11: " + token11);
//            }
//
//            Preg preg12 = new Preg ("name=\"sid\" value=\"([0-9]+)\" />", page1);
//            String token12 = "";
//            if (preg12.get (1).isEmpty ())
//            {
////                Str.println ("ERROR: token12 not found!");
//            }
//            else
//            {
//                token12 = preg12.get (1).get (0);
////                Str.println ("Token12: " + token12);
//            }
//
//            // Текущее имя юзера
//            String currentUserName = "b"
//                    + rand.nextInt (9) + rand.nextInt (9) + rand.nextInt (9) + rand.nextInt (9)
//                    + rand.nextInt (9) + rand.nextInt (9) + rand.nextInt (9) + rand.nextInt (9)
//                    + rand.nextInt (9) + rand.nextInt (9);
//
//            // Отправляем данные
//            String page2 = curl
//                    .referer ("http://spaces.ru/reg/?step_new=1")
//                    .p ("reg_name", currentUserName)
//                    .p ("sid", token12)
//                    .p ("step_new2", token11)
//                    .p ("referal", "1")
//                    .post ("http://spaces.ru/reg2/?sid=" + token12);
//
//            // Парсим URL картинки
//            Preg preg2 = new Preg ("capcha/([0-9]+).gif", page2);
//            String token2 = "";
//            if (preg2.get (1).isEmpty ())
//            {
//                Str.println ("ERROR: CapchaID not found!");
////            Str.println (page2);
//            }
//            else
//            {
//                token2 = preg2.get (1).get (0);
//
//                // Зададим имя текущей капчи
//                currentCapchaFile = new java.io.File (params.capchaFolder + "/" + token2 + "_" + rand.nextInt (1000) + ".gif");
//
//                // Грузим файл
//                curl
//                        .referer ("http://spaces.ru/reg/?step_new=1")
//                        .loadFile ("http://spaces.ru/capcha/" + token2 + ".gif", currentCapchaFile.getAbsolutePath ());
//            }
//
//            // Парсим токен для отправки данных
//            Preg preg3 = new Preg ("name=\"step_new3\" value=\"(.+)\"", page2);
//            String token3 = "";
//            if (preg3.get (1).isEmpty ())
//            {
//                Str.println ("ERROR! Token3 not found!");
//            }
//            else
//            {
//                token3 = preg3.get (1).get (0);
//            }
//
//            //------------------------------------------------------------------
//            // Распознаем капчу текущим рекогнайзером,
//            // отправляем данные на спейс,
//            // узнаем верно ли распознали, и если верно, переименовываем файл.
//
//            if (!token3.equals (""))
//            {
//                // Распознаем капчу
//                String capchaCode = Recognizer.recognize (currentCapchaFile.getAbsolutePath ());
//                ++countAll;
//
//                String page3 = curl
//                        .p ("password", rand.nextInt (1000000000) + "bg")
//                        .p ("email", rand.nextInt (1000000000) + "sk@gmail.com")
//                        .p ("sid", "")
//                        .p ("step_new3", token3)
//                        .p ("referal", "0")
//                        .p ("reg_name", currentUserName)
//                        .p ("pic_code", capchaCode)
//                        .post ("http://spaces.ru/reg3/?sid=");
//
//                // Проверяем, верно ли распознана капча
//
//                if (page3.contains ("Секретный код с картинки введен неверно"))
//                {
//                    Str.println (currentCapchaFile.getName () + " - FUCK");
//
//                    // Удаляем файл капчи
////                    currentCapchaFile.delete ();
//                }
//                else
//                {
//                    // Переименовываем файл капчи
//                    currentCapchaFile.renameTo (new java.io.File (params.capchaRecognizedFolder + "/"
//                            + capchaCode + "_" + rand.nextInt (1000000000) + ".gif"));
//
//                    // Увеличиваем счетчик обработанных капч
//                    ++currentImageNumber;
//
//                    Str.println ("THREADS: " + params.countDownImages + " / "
//                            + "GOOD: " + Str.getPercentage ((double) currentImageNumber / countAll) + "% / "
//                            + currentCapchaFile.getName () + " - OK",
//                                 true, params.countDownImages);
//                }
//            }
//        }
//
//    }
}
