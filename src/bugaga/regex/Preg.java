package bugaga.regex;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Так как регулярки в яве пиздец громоздкие, решил написать обертку покороче.
 * Возврадаться должен или пустой лист (если ничего не найдено или не существует скобки под этим номером)
 * или с результатами поиска
 *
 * @author GoGo
 */
public class Preg
{

    /**
     * Матчер регулярки.
     */
    protected Matcher matcher;

    /**
     * Скомпилить регулярку.
     *
     * @param expression Регулярное выражение.
     * @param search     Где ищем.
     */
    public Preg (String expression, String search)
    {
        Pattern pattern = Pattern.compile (expression);
        matcher = pattern.matcher (search);
    }

    /**
     * Вернуть лист с совпадениями по нужной скобке.
     *
     * @param bracketNumber Порядковый номер пары скобок, выражение в которых нужно найти.
     *
     * @return Лист со всеми найденными элементами или же пустой лист, если ничего не найдено или не существует пары скобок с заданным номером.
     */
    public ArrayList<String> get (int bracketNumber)
    {
        ArrayList<String> result = new ArrayList<> ();

        // Проходимся по регулярке и добавляем в результат все найденные элементы
        while (matcher.find ())
        {
            result.add (matcher.group (bracketNumber));
        }

        matcher.reset ();
        return result;
    }
}
