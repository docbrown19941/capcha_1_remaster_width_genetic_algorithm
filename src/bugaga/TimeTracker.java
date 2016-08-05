package bugaga;

import bugaga.io.Str;

import java.util.HashMap;

/**
 * Утилита для профилирования кусков кода.
 */
public class TimeTracker
{
    private static class Data
    {
        long timeStart = -1;
        long timeWork = 0;
    }

    private static HashMap map = new HashMap<String, Data> ();

    public static synchronized void start (String _id)
    {
        Data data;
        if (map.containsKey (_id))
        {
            data = (Data) map.get (_id);
        }
        else
        {
            data = new Data ();
            map.put (_id, data);
        }

        data.timeStart = java.lang.System.currentTimeMillis ();
    }

    public static synchronized double stop (String _id)
    {
        Data data = (Data) map.get (_id);
        data.timeWork += (java.lang.System.currentTimeMillis () - data.timeStart);

        return (double) data.timeWork;
    }

    public static synchronized String stopAndGetGen (String _id)
    {
        stop (_id);
        return getGen (_id);
    }

    public static synchronized String getGen (String _id)
    {
        Data data = (Data) map.get (_id);
        double time = (double) data.timeWork / 1000 / 100;
        return _id + ": " + Str.getPercentage (time) + " sec / " + (Str.getPercentage (time / 60)) + " min / " +
               (Str.getPercentage (time / 60 / 60)) + " hour.";
    }
}
