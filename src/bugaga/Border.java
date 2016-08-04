package bugaga;

/**
 * Класс границы какой-либо области.
 */
class Border
{

    int xMin;

    int xMax;

    int yMin;

    int yMax;

    /**
     * Инициализировать параметры.
     *
     * @param _xMin
     * @param _xMax
     * @param _yMin
     * @param _yMax
     */
    public Border (int _xMin, int _xMax, int _yMin, int _yMax)
    {
        xMin = _xMin;
        xMax = _xMax;
        yMin = _yMin;
        yMax = _yMax;
    }

    /**
     * Конструктор без инициализации параметров.
     */
    public Border ()
    {
    }
}