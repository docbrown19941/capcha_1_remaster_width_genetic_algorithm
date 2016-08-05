package bugaga.curl;

import java.util.ArrayList;
import java.util.Random;

import bugaga.proxy.Proxy;

/**
 * Класс с параметрами запроса
 *
 * @author GoGo
 */
public class QueryParams
{

    /**
     * Кодировка UTF-8
     */
    public static final String UTF_8 = "UTF-8";

    /**
     * Кодировка Windows-1251
     */
    public static final String WIN_1251 = "CP1251";

    /**
     * Кодировка CP866 (терминал Windows)
     */
    public static final String CP866 = "CP866";

    /**
     * Юзер-Агент FireFox
     */
    public static final String UA_FIREFOX_23 = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0";

    /**
     * Таймаут в МИЛИсекундах
     */
    public int timeout;

    /**
     * Следовать ли редиректам?
     */
    public boolean followLocations;

    /**
     * Юзер-агент
     */
    public String userAgent;

    /**
     * Реферер
     */
    public String referer;

    /**
     * Кодировка
     */
    public String charset;

    /**
     * Прокси
     */
    public Proxy proxy;

    /**
     * Принимать ли куки?
     */
    public boolean isCookie = true;

    /**
     * Объект рандома для данного объекта.
     */
    private static Random rand = new Random ();

    /**
     * Лист с мобильными юа.
     */
    protected static ArrayList<String> listMobileUA = new ArrayList<> ();

    public QueryParams ()
    {
        if (listMobileUA.isEmpty ())
        {

            // Добавляем МОБИЛЬНЫЕ юзер-агенты
            // Да, хардкод это ебануто, но эффективно
            listMobileUA.add ("NokiaN70-1/2.0536.0.2 Series60/2.8 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("NokiaN71-1/2.0613 Series60/3.0 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("NokiaN72/2.0635.2.0.2/SN354568011645684 Series60/2.8 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("NokiaN73-1/3.0705.1.0.31 Series60/3.0 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("NokiaN76-1/10.0.035 SymbianOS/9.2 Series60/3.1 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("NokiaN80-1/3.0 (3.0611.0.8) Series60/3.0 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("NokiaN90-1/5.0607.7.3 Series60/2.8 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("NokiaN95/20.0.015 SymbianOS/9.2 Series60/3.1 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("NokiaE50-1/3.0 (06.41.3.0) SymbianOS/9.1 Series60/3.0 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("NokiaN-Gage/1.0 SymbianOS/6.1 Series60/1.2 Profile/MIDP-1.0 Configuration/CLDC-1.0 ");
            listMobileUA.add ("NokiaN-GageQD/1.0 SymbianOS/6.1 Series60/1.2 Profile/MIDP-1.0 Configuration/CLDC-1.0");
            listMobileUA.add ("Nokia2600c/2.0 (05.21) Profile/MIDP-2.1 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia2610/2.0 (04.90) Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia2626/2.0 (03.51) Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia2865/2.0 (FL190V0200.nep) UP.Browser/6.2.3.8 MMP/2.0");
            listMobileUA.add ("Nokia3100/1.0 (05.02) Profile/MIDP-1.0 Configuration/CLDC-1.0");
            listMobileUA.add ("Nokia3109c/2.0 (05.30) Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia3110c/2.0 (04.91) Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia3200/1.0 (5.34) Profile/MIDP-1.0 Configuration/CLDC-1.0");
            listMobileUA.add ("Nokia3230/2.0 (3.0505.2) SymbianOS/7.0s Series60/2.1 Profile/MIDP-2.0 Configuration/CLDC-1.0");
            listMobileUA.add ("Nokia3250/2.0 (04.14) SymbianOS/9.1 Series60/3.0 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia3500c/2.0 (05.51) Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia3510i/1.0 (04.44) Profile/MIDP-1.0 Configuration/CLDC-1.0");
            listMobileUA.add ("Nokia3595/1.0 (8.12) Profile/MIDP-1.0 Configuration/CLDC-1.0");
            listMobileUA.add ("Nokia3650/1.0 (4.13) SymbianOS/6.1 Series60/1.2 Profile/MIDP-1.0 Configuration/CLDC-1.0");
            listMobileUA.add ("Nokia5070/2.0 (04.21) Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia5140/2.0 (3.13) Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia5200/2.0 (03.92) Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia5300/2.0 (03.92) Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia5310XpressMusic/2.0 (03.63) Profile/MIDP-2.1 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia5500d/2.0 (03.55) SymbianOS/9.1 Series60/3.0 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia5700/3.83.1 Series60/3.1 Profile/MIDP-2.0 Configuration/CLDC-1.1 SymbianOS/9.2");
            listMobileUA.add ("Nokia6020/2.0 (04.50) Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia6021/2.0 (04.10) Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia6030/2.0 (y4.10) Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia6070/2.0 (03.20) Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia6080/2.0 (03.40) Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia6085/2.0 (04.26) Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia6100/1.0 (05.51) Profile/MIDP-1.0 Configuration/CLDC-1.0");
            listMobileUA.add ("Nokia6101/2.0 (04.90) Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia6102i/2.0 (04.69) Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia6103/2.0 (04.61) Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia6108/1.0 (05.04) Profile/MIDP-1.0 Configuration/CLDC-1.0");
            listMobileUA.add ("Nokia6111/2.0 (03.58) Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia6125/2.0 (03.51) Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia6131/2.0 (05.50) Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia6151/2.0 (04.10) Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia6170/2.0 (03.25) Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia6230/2.0 (05.35) Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia6230i/2.0 (03.46) Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia6233/2.0 (04.91) Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia6260/2.0 (3.0448.0) SymbianOS/7.0s Series60/2.1 Profile/MIDP-2.0 Configuration/CLDC-1.0");
            listMobileUA.add ("Nokia6267/2.0 (03.25) Profile/MIDP-2.1 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia6270/2.0 (03.85) Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia6280/2.0 (03.81) Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia6288/2.0 (05.92) Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia6290/3.06 SymbianOS/9.2  Series60/3.1 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia6300/2.0 (04.71) Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia6500s-1/2.0 (03.80) Profile/MIDP-2.1 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia6600/1.0 (5.27.0) SymbianOS/7.0s Series60/2.0 Profile/MIDP-2.0 Configuration/CLDC-1.0");
            listMobileUA.add ("Nokia6610I/1.0 (4.20) Profile/MIDP-1.0 Configuration/CLDC-1.0");
            listMobileUA.add ("Nokia6630/1.0 (4.03.38) SymbianOS/8.0 Series60/2.6 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia6670/2.0 (4.0437.4) SymbianOS/7.0s Series60/2.1 Profile/MIDP-2.0 Configuration/CLDC-1.0");
            listMobileUA.add ("Nokia6680/1.0 (2.04.14) SymbianOS/8.0 Series60/2.6 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia6681/2.0 (3.10.6) SymbianOS/8.0 Series60/2.6 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia6820/2.0 (5.28) Profile/MIDP-1.0 Configuration/CLDC-1.0");
            listMobileUA.add ("Nokia7250I/1.0 (4.22) Profile/MIDP-1.0 Configuration/CLDC-1.0");
            listMobileUA.add ("Nokia7270/2.0 (03.24) Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia7360/2.0 (03.21) Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia7370/2.0 (03.52) Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia7390/2.0 (04.51) Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia7500/2.0 (04.62) Profile/MIDP-2.1 Configuration/CLDC-1.1");
            listMobileUA.add ("Nokia7610/2.0 (4.0421.4) SymbianOS/7.0s Series60/2.1 Profile/MIDP-2.0 Configuration/CLDC-1.0");
            listMobileUA.add ("Nokia8800/2.0 (03.78) Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SIE-M55/91 Profile/MIDP-1.0 Configuration/CLDC-1.0 UP.Browser/6.1.0.7.3 (GUI) MMP/1.0");
            listMobileUA.add ("SIE-A60/20 Profile/MIDP-1.0 Configuration/CLDC-1.0 UP.Browser/6.1.0.7.3 (GUI) MMP/1.0");
            listMobileUA.add ("SIE-A65/15 Profile/MIDP-1.0 Configuration/CLDC-1.0 UP.Browser/6.1.0.7.3 (GUI) MMP/1.0");
            listMobileUA.add ("SIE-CF75/22 UP.Browser/7.0.2.2.decoder.7(GUI) MMP/2.0 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SIE-EF81/48 UP.Browser/7.0.0.1.181 (GUI) MMP/2.0 Profile/MIDP-2.0 Configuration/CLDC-1.1 Browser/UP.Browser/7.1.1.3.h (GUI) MMP/2.0");
            listMobileUA.add ("SIE-S68/22 UP.Browser/7.1.0.e.12(GUI) MMP/2.0 Profile/MIDP-2.0 Configuration/CLDC-1.1 Browser/UP.Browser/7.1.0.e.12 (GUI) MMP/2.0");
            listMobileUA.add ("SIE-S65/12 UP.Browser/7.0.0.1.c.3 (GUI) MMP/2.0 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SIE-SL65/12 UP.Browser/7.0.0.1.c.3 (GUI) MMP/2.0 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SIE-MC60/10 Profile/MIDP-1.0 Configuration/CLDC-1.0 UP.Browser/6.1.0.7.3 (GUI) MMP/1.0");
            listMobileUA.add ("SIE-M65/50 UP.Browser/7.0.2.2.decoder.3(GUI) MMP/2.0 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SIE-C60/27 Profile/MIDP-1.0 Configuration/CLDC-1.0 UP.Browser/6.1.0.7.3 (GUI) MMP/1.0");
            listMobileUA.add ("SIE-C65/25 UP.Browser/7.0.0.1.c.3 (GUI) MMP/2.0 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SIE-C72/22 UP.Browser/7.0.2.2.decoder.7(GUI) MMP/2.0 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SIE-C75/11 UP.Browser/7.0.2.2.decoder.5(GUI) MMP/2.0 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SIE-C81/40 UP.Browser/7.1.0.e.20(GUI) MMP/2.0 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SIE-ME75/19 UP.Browser/7.0.2.2.decoder.7(GUI) MMP/2.0 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SIE-CX65/16 UP.Browser/7.0.0.1.c.3 (GUI) MMP/2.0 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SIE-CX70/25 UP.Browser/7.0.0.1.c.3 (GUI) MMP/2.0 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SIE-CX75/05 UP.Browser/7.0.2.2.decoder.3(GUI) MMP/2.0 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SIE-S75/12 UP.Browser/7.1.0.e.8(GUI) MMP/2.0 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SIE-SX1/1.1 SymbianOS/6.1 Series60/1.2 Profile/MIDP-1.0 Configuration/CLDC-1.0");
            listMobileUA.add ("SIE-E71/37 UP.Browser/7.1.0.k.4(GUI) MMP/2.0 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("BenQ-E61/1.00/WAP2.0 UP.Browser/6.3.0.4.c.1.102 (GUI) MMP/2.0");
            listMobileUA.add ("SIE-EL71/22 UP.Browser/7.1.0.e.23(GUI) MMP/2.0 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SIE-AX72/01 Profile/MIDP-1.0 Configuration/CLDC-1.0 UP.Browser/6.1.0.7.3.102 (GUI) MMP/1.0");
            listMobileUA.add ("BenQ-S88/1.00/WAP2.0/MIDP2.0/CLDC1.0 UP.Browser/6.3.0.4.c.1.102 (GUI) MMP/2.0");
            listMobileUA.add ("SonyEricssonJ300i/R2AT SEMC-Browser/4.0.3 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SonyEricssonK500i/R2AE SEMC-Browser/4.0.3 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SonyEricssonK300i/R2BA SEMC-Browser/4.0.3 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SonyEricssonK310i/R4DA Browser/NetFront/3.3 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SonyEricssonK320i/R4EA Browser/NetFront/3.3 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SonyEricssonK508i/R2AT SEMC-Browser/4.0.3 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SonyEricssonK510i/R4CH Browser/NetFront/3.3 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SonyEricssonK550i/R1JD Browser/NetFront/3.3 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SonyEricssonK600i/R2T Browser/SEMC-Browser/4.2 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SonyEricssonK608i/R2T Browser/SEMC-Browser/4.2 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SonyEricssonK610i/R1CF Browser/NetFront/3.3 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SonyEricssonK700i/R2AE SEMC-Browser/4.0.3 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SonyEricssonK750i/R1AA Browser/SEMC-Browser/4.2 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SonyEricssonK790i/R1CG Browser/NetFront/3.3 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SonyEricssonK800i/R1CB Browser/NetFront/3.3 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SonyEricssonZ300i/R3U/TelecaBrowser/4.08");
            listMobileUA.add ("SonyEricssonZ520i/R3J Browser/SEMC-Browser/4.2 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SonyEricssonZ530i/R6AC Browser/NetFront/3.3 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SonyEricssonZ550i/R6BA Browser/NetFront/3.3 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SonyEricssonZ600/R401 Profile/MIDP-1.0 Configuration/CLDC-1.0");
            listMobileUA.add ("SonyEricssonZ610i/R1ED Browser/NetFront/3.3 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SonyEricssonZ710i/R1EF Browser/NetFront/3.3 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SonyEricssonZ750i/R1CA Browser/NetFront/3.4 Profile/MIDP-2.1 Configuration/CLDC-1.1");
            listMobileUA.add ("SonyEricssonZ800/R1Y Browser/SEMC-Browser/4.1 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SonyEricssonT610/R101 Profile/MIDP-1.0 Configuration/CLDC-1.0");
            listMobileUA.add ("SonyEricssonT630/R601 Profile/MIDP-1.0 Configuration/CLDC-1.0");
            listMobileUA.add ("SonyEricssonS500i/R8BA Browser/NetFront/3.3 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SonyEricssonS700i/R3B SEMC-Browser/4.0.1 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SonyEricssonW200i/R4GB Browser/NetFront/3.3 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SonyEricssonW300i/R9A Browser/NetFront/3.3 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SonyEricssonW550i/R4BA Browser/NetFront/3.3 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SonyEricssonW600i/R7CA Browser/NetFront/3.3 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SonyEricssonW610i/R1JD Browser/NetFront/3.3 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SonyEricssonW660i/R8BB Browser/NetFront/3.3 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SonyEricssonW700i/R1DB Browser/SEMC-Browser/4.2 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SonyEricssonW710i/R1EE Browser/NetFront/3.3 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SonyEricssonW800i/R1AA Browser/SEMC-Browser/4.2 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SonyEricssonW810i/R4EA Browser/NetFront/3.3 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SonyEricssonW850i/R1ED Browser/NetFront/3.3 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SonyEricssonW880i/R1JC Browser/NetFront/3.3 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SonyEricssonW900i/R5BB Browser/NetFront/3.3 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SonyEricssonP900/R101 Profile/MIDP-2.0 Configuration/CLDC-1.0");
            listMobileUA.add ("SonyEricssonP910i/R5B SEMC-Browser/Symbian/3.0 Profile/MIDP-2.0 Configuration/CLDC-1.0");
            listMobileUA.add ("MOT-C650/0B.D2.2FR MIB/2.2.1 Profile/MIDP-2.0 Configuration/CLDC-1.0");
            listMobileUA.add ("MOT-E398/0E.30.45R MIB/2.2.1 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("MOT-C380/0B.D2.27R MIB/2.2.1 Profile/MIDP-2.0 Configuration/CLDC-1.0");
            listMobileUA.add ("MOT-C390/0B.A0.0FR MIB/2.2.1 Profile/MIDP-2.0 Configuration/CLDC-1.0");
            listMobileUA.add ("MOT-RAZRV3x/85.83.E7P MIB/BER2.2 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("MOT-V3/0E.42.0ER MIB/2.2.1 Profile/MIDP-2.0 Configuration/CLDC-1.0");
            listMobileUA.add ("MOT-V3i/08.B4.8DR MIB/2.2.1 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("MOT-V220/0B.D2.23R MIB/2.2.1 Profile/MIDP-2.0 Configuration/CLDC-1.0");
            listMobileUA.add ("MOT-V235/0A.30.6CR MIB/2.2.1 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("MOT-V360/08.B7.30R MIB/2.2.1 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("MOT-E770v/85.97.C3P MIB/BER2.2 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("MOT-E1000/82.32.30I MIB/2.2.1 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("MOT-V600/0B.09.38R MIB/2.2 Profile/MIDP-2.0 Configuration/CLDC-1.0");
            listMobileUA.add ("MOT-V635/08.48.88R MIB/2.2.1 Profile/MIDP-2.0 Configuration/CLDC-1.0");
            listMobileUA.add ("MOT-E680/R51_G_0F.48.A2P MIB/2.2 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("MOT-PEBL U6/08.83.76R MIB/2.2.1 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("MOT-L6/0A.52.26R MIB/2.2.1 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("MOT-L7/08.B7.DCR MIB/2.2.1 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("MOT-L7e/AAUG2136AA 08.01.0AR/10.21.2005 MIB/BER2.2 Profile/MIDP-2.0 Configuration/CLDC-1.1 EGE/1.0");
            listMobileUA.add ("MOT-Z3/08.05.04R MIB/BER2.2 Profile/MIDP-2.0 Configuration/CLDC-1.1 ");
            listMobileUA.add ("MOT-V180/0B.D1.09R MIB/2.2.1 Profile/MIDP-2.0 Configuration/CLDC-1.0");
            listMobileUA.add ("MOT-V980/80.2F.93I MIB/2.2.1 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("MOT-V360/08.B7.2ER MIB/2.2.1 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("MOT-E1 iTunes/0E.30.42R MIB/2.2.1 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("MOTOROKR Z6/R60_G_80.xx.yyI Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SAMSUNG-SGH-F250/F250XEGL6 Profile/MIDP-2.0 Configuration/CLDC-1.1 UP.Browser/6.2.3.3.c.1.101 (GUI) MMP/2.0");
            listMobileUA.add ("SAMSUNG-SGH-X100/PEARL UP.Browser/6.1.0.6 (GUI) MMP/1.0");
            listMobileUA.add ("SAMSUNG-SGH-X640/1.0 UP.Browser/6.2.2.6 (GUI) MMP/1.0");
            listMobileUA.add ("SAMSUNG-SGH-X700/1.0 Profile/MIDP-2.0 Configuration/CLDC-1.1 UP.Browser/6.2.3.3.c.1.101 (GUI) MMP/2.0");
            listMobileUA.add ("SAMSUNG-SGH-D500E/1.0 Profile/MIDP-2.0 Configuration/CLDC-1.1 UP.Browser/6.2.3.3.c.1.101 (GUI) MMP/2.0");
            listMobileUA.add ("SAMSUNG-SGH-D600E/1.0 Profile/MIDP-2.0 Configuration/CLDC-1.1 UP.Browser/6.2.3.3.c.1.101 (GUI) MMP/2.0");
            listMobileUA.add ("SAMSUNG-SGH-E200/1.0 Profile/MIDP-2.0 Configuration/CLDC-1.1 UP.Browser/6.2.3.3.c.1.101 (GUI) MMP/2.0");
            listMobileUA.add ("SAMSUNG-SGH-E250/1.0 Profile/MIDP-2.0 Configuration/CLDC-1.1 UP.Browser/6.2.3.3.c.1.101 (GUI) MMP/2.0 UP.Link/6.3.1.12.0");
            listMobileUA.add ("SAMSUNG-SGH-E340/1.0 Profile/MIDP-2.0 Configuration/CLDC-1.1 UP.Browser/6.2.3.3.c.1.101 (GUI) MMP/2.0");
            listMobileUA.add ("SAMSUNG-SGH-E360/1.0 Profile/MIDP-2.0 Configuration/CLDC-1.1 UP.Browser/6.2.3.3.c.1.101 (GUI) MMP/2.0");
            listMobileUA.add ("SAMSUNG-SGH-E590/1.0 Profile/MIDP-2.0 Configuration/CLDC-1.1 UP.Browser/6.2.3.3.c.1.101 (GUI) MMP/2.0");
            listMobileUA.add ("SAMSUNG-SGH-E700/BSI UP.Browser/6.1.0.6 (GUI) MMP/1.0");
            listMobileUA.add ("SAMSUNG-SGH-E730/1.0 Profile/MIDP-2.0 Configuration/CLDC-1.1 UP.Browser/6.2.3.3.c.1.101 (GUI) MMP/2.0");
            listMobileUA.add ("SAMSUNG-SGH-E760/1.0 Profile/MIDP-2.0 Configuration/CLDC-1.1 UP.Browser/6.2.3.3.c.1.101 (GUI) MMP/2.0");
            listMobileUA.add ("SAMSUNG-SGH-D500E/1.0 Profile/MIDP-2.0 Configuration/CLDC-1.1 UP.Browser/6.2.3.3.c.1.101 (GUI) MMP/2.0");
            listMobileUA.add ("SAMSUNG-SGH-D600E/1.0 Profile/MIDP-2.0 Configuration/CLDC-1.1 UP.Browser/6.2.3.3.c.1.101 (GUI) MMP/2.0");
            listMobileUA.add ("SAMSUNG-SGH-D900i/1.0 Profile/MIDP-2.0 Configuration/CLDC-1.1 UP.Browser/6.2.3.3.c.1.101 (GUI) MMP/2.0");
            listMobileUA.add ("SAMSUNG-SGH-G600/G600XEGH2 NetFront/3.4 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SEC-SGHF300/1.0 NetFront/3.2  Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SEC-SGHD520/1.0 NetFront/3.2 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SEC-SGHE570/1.0 NetFront/3.2 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SEC-SGHD730 SymbianOS/7.0s Series60/2.1 Profile/MIDP-2.0 Configuration/CLDC-1.0");
            listMobileUA.add ("SEC-SGHE770/1.0 NetFront/3.2 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SEC-SGHD800/1.0 NetFront/3.2 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SEC-SGHX820/1.0 NetFront/3.2 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SEC-SGHD830/1.0 NetFront/3.2 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SEC-SGHE900/1.0 NetFront/3.2 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SEC-SGHU600/1.0 NetFront/3.2 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SEC-SGHD840/1.0 NetFront/3.2 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SEC-SGHX210/1.0");
            listMobileUA.add ("SEC-SGHX510/1.0");
            listMobileUA.add ("SEC-SGHE570/1.0 NetFront/3.2 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SEC-SGHX660/1.0 TSS/2.5");
            listMobileUA.add ("SEC-SGHU600/BOGD4 NetFront/3.2 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SEC-SGHE840/1.0 NetFront/3.4 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SEC-SGHX680/1.0 TSS/2.5");
            listMobileUA.add ("SEC-SGHJ600E/1.0 NetFront/3.2 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SEC-SGHE900/1.0 NetFront/3.2 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SEC-SGHE950/1.0 NetFront/3.4 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("SAGEM-my301X/1.0/ MIDP-2.0 Configuration/CLDC-1.1 UP.Browser/6.2.3.3.g.2.106 (GUI) MMP/2.0");
            listMobileUA.add ("SAGEM-myX5-2v/1.0 Profile/MIDP-2.0 Configuration/CLDC-1.0 UP.Browser/6.2.3.3.g.2.103 (GUI) MMP/2.0");
            listMobileUA.add ("SAGEM-myC-4/1.0 Profile/MIDP-2.0 Configuration/CLDC-1.0 UP.Browser/6.2.2.5.decoder.6 (GUI) MMP/1.0 ");
            listMobileUA.add ("SAGEM-my700x/1.0 Profile/MIDP-2.0 Configuration/CLDC-1.1 Browser/UP.Browser/7.1.0.f.1.161 (GUI)");
            listMobileUA.add ("SAGEM-myX6-2/1.0 Profile/MIDP-2.0 Configuration/CLDC-1.1 Browser/UP.Browser/7.1.0.f.1.144 (GUI)");
            listMobileUA.add ("LG/KU250/v1.0 Profile/MIDP-2.0 Configuration/CLDC-1.1");
            listMobileUA.add ("LG-KP200 Obigo/WAP2.0 MIDP-2.0/CLDC-1.1");
            listMobileUA.add ("LG-KG110 AU/4.10 Profile/MIDP-1.0 Configuration/CLDC-1.0");
            listMobileUA.add ("LG-CU575/V10d Obigo/Q05A Profile/MIDP-2.0 Configuration/CLDC-1.1 UP.Link/6.3.1.17.0");
            listMobileUA.add ("LG-KG290 Obigo/WAP2.0 MIDP-2.0/CLDC-1.1 UP.Link/6.3.1.13.0");
            listMobileUA.add ("LG-KG245/MIC/WAP2.0 MIDP-2.0/CLDC-1.1");
            listMobileUA.add ("LG-CU720/V1.0g Obigo/Q05A Profile/MIDP-2.0 Configuration/CLDC-1.1 UP.Link/6.3.0.0.0");
            listMobileUA.add ("LG-C3380 MIC/WAP2.0 MIDP-2.0/CLDC-1.1");
            listMobileUA.add ("LG-F2300 MIC/WAP2.0 MIDP-2.0/CLDC-1.0");
            listMobileUA.add ("LG-B2000 MIC/WAP2.0 MIDP-2.0/CLDC-1.0");
            listMobileUA.add ("LG-G7050 UP.Browser/6.2.2 (GUI) MMP/1.0 Profile/MIDP-1.0 Configuration/CLDC-1.0");
            listMobileUA.add ("LG-P7200 MIC/WAP2.0 MIDP-2.0/CLDC-1.1");
            listMobileUA.add ("LG-KG225/MIC/WAP2.0/MIDP-2.0/CLDC-1.1");
            listMobileUA.add ("LG-MG320 MIC/WAP2.0 MIDP-2.0/CLDC-1.1");
            listMobileUA.add ("LG-G1600 AU/4.10 Profile/MIDP-1.0 Configuration/CLDC-1.0");
            listMobileUA.add ("LG-M4410 MIC/WAP2.0 MIDP-2.0/CLDC-1.1");
            listMobileUA.add ("LG-F2100 MIC/WAP2.0 MIDP-2.0/CLDC-1.0 ");
            listMobileUA.add ("LG-T5100 UP.Browser/6.2.2 (GUI) MMP/1.0 Profile/MIDP-1.0 Configuration/CLDC-1.0");
            listMobileUA.add ("LG-CU500 Obigo/WAP2.0 Profile/MIDP-2.0 Configuration/CLDC-1.1 UP.Link/6.3.0.0.0");
            listMobileUA.add ("LG-B2060 V100 AU/4.10 Profile/MIDP-1.0 Configuration/CLDC-1.0");
            listMobileUA.add ("LG-G1800 MIC/WAP2.0 MIDP-2.0/CLDC-1.0");
            listMobileUA.add ("LG-F2400 MIC/WAP2.0 MIDP-2.0/CLDC-1.1");
            listMobileUA.add ("LG-G1600 AU/4.10 Profile/MIDP-1.0 Configuration/CLDC-1.0");
            listMobileUA.add ("LG-L1100 UP.Browser/6.2.2 (GUI) MMP/1.0 Profile/MIDP-1.0 Configuration/CLDC-1.0");
            listMobileUA.add ("LG-S5200 MIC/WAP2.0 MIDP-2.0/CLDC-1.1");
        }
    }

    /**
     * Получить рандомный Юзер-Агент.
     *
     * @return
     */
    public static String getRandUA ()
    {
        return listMobileUA.get (rand.nextInt (listMobileUA.size ()));
    }
}
