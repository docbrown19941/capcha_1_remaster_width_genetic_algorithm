package bugaga.neuro;

/**
 * Класс нейрона.
 *
 * @author GoGo
 */
public class Neuron
{

    /**
     * Число входов нейрона.
     */
    protected int countIns;

    /**
     * Порог активации.
     */
    protected double threshold;

    /**
     * Входные сигналы.
     */
    protected double[] inputSignals;

    /**
     * Веса.
     */
    protected double[] weights;

    /**
     * Конструктор.
     *
     * @param _countIns     Число входов.
     * @param _inputSignals Массив со входящими сигналами.
     * @param _threshold    Порог активации. (используется пороговая функция активации)
     */
    public Neuron (int _countIns, double[] _inputSignals, double _threshold)
    {
        // Считываем параметры
        countIns = _countIns;
        threshold = _threshold;

        inputSignals = new double[countIns];
        weights = new double[countIns];

        // Заполняем массив входящих сигналов.
        System.arraycopy (_inputSignals, 0, inputSignals, 0, inputSignals.length);
    }

    /**
     * Вернуть сумму сигналов на входах.
     *
     * @return
     */
    public double getSum ()
    {
        double sum = 0;

        for (int x = 0; x < inputSignals.length; x++)
        {
            // Если на входе что-то больше нуля (хотя и меньше быть не должно)
            if (inputSignals[x] > 0)
                sum += weights[x];
        }

        // Только если порог активации превышен, отдаем сумму
        return sum >= threshold ? sum : 0;
    }
}
