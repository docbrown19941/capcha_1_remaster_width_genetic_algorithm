package bugaga.neuro;

/**
 * Класс информации синапса (координаты присоединения к источнику сигнала и вес).
 */
class InfoSynapses
{

    /**
     * Номер слоя данных
     */
    public int nConnectDataLayer;

    /**
     * Координата в векторе данных
     */
    public int nConnectVectorCoordinate;

    /**
     * Вес
     */
    public double weight;

    public InfoSynapses (int _dataLayer, int _vectorCoordinate, double _weight)
    {
        nConnectDataLayer = _dataLayer;
        nConnectVectorCoordinate = _vectorCoordinate;
        weight = _weight;
    }
}