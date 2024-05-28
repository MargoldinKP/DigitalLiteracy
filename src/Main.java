import java.util.Scanner;

public class Main
{
    private static void exponentNum(String inNum)// вывод числа в экспоненциальном виде в десятичной СС
    {
        boolean k = true;
        String sign = Double.parseDouble(inNum) >= 0 ? "" : "-";//определение знака числа
        inNum = Double.toString(Math.abs(Double.parseDouble(inNum)));//избавляемся от знака и получаем строку с положительным числом
        boolean isNumBiggerThanOne = Float.parseFloat(inNum) >= 1;//если число больше либо равно 1 то тру
        String mantissa = "";

        int count_non_symbols = 0; //ининциализация счетчика
        for (int i = 0; i < inNum.length(); i++) //цикл проходит по всем символам числа
        {
            if (inNum.charAt(i) == '0')//явл. ли символ 0
                count_non_symbols++;
            else if (inNum.charAt(i) == '.') { //явл. ли точкой
                count_non_symbols++;
                k = false;
            } else
                break;
        }

// Индекс, с которого начинается обработка mantissa (дробной части числа)
        int start_index = count_non_symbols;

// Инициализация переменной count значениями в зависимости от count_non_symbols
        int count = count_non_symbols > 0 ? count_non_symbols - 1 : -1;

// Итерация по символам числа начиная с индекса start_index
        for (int i = start_index; i < inNum.length(); i++) {
            // Проверка текущего символа на точку
            if (inNum.charAt(i) != '.')
                mantissa += inNum.charAt(i); // Добавление символа к mantissa, если не точка
            else
                k = false; // Если точка, установка k в false, чтобы завершить добавление к mantissa

            // Обновление значения count в зависимости от условий
            if (k && isNumBiggerThanOne)
                count++; // Увеличение count, если k и число больше единицы
            else if (k) {
                count--; // Уменьшение count, если k и число не больше единицы
            }
        }

// Вывод нормализованного и денормализованного представлений числа в консоль
        if (isNumBiggerThanOne) {
            System.out.println("Normalized: " + sign + mantissa.charAt(0) + "." + mantissa.substring(1) + "E+" + count);
            System.out.println("Denormalized: " + sign + "0." + mantissa + "E+" + (count + 1));
        } else {
            System.out.println("Normalized: " + sign + mantissa.charAt(0) + "." + mantissa.substring(1) + "E-" + count);
            System.out.println("Denormalized: " + sign + "0." + mantissa + "E-" + (count - 1));
        }
    }

        //Метод Binary для перевода дробной части числа в двоичную систему счисления:
     static String Binary(double fract, int integer_len){
        String fract_bin = "";
        int i = 0;
        while (Math.floor(fract) != fract)
        {

            if (i == (23 - integer_len))// нужно чтобы мантисса была не больше 23 бит
                break;

            // когда мы переводили целые числа в двоичную СС то делили на 2
            // и брали остатки и записывали в обратном порядке
            // здесь  мы умножаем на 2 и берем целую часть от получившегося числа
            // до тех пор, пока у нас не получится число у которого дробная часть == .0
            fract_bin += (int) Math.floor(fract * 2) % 2;
            fract *= 2;
            i++;
        }

        if (fract_bin.isEmpty())
            fract_bin = "0";

        return fract_bin;
    }


    private static int findExponenta(String binary_num) // нахождение экспоненты
    {
        int count = -1; // Инициализация счетчика значением -1

        // Итерация по каждому символу в двоичном представлении числа
        for (int i = 0; i < binary_num.length(); i++)
        {
            // Проверка текущего символа на отсутствие точки
            if (binary_num.charAt(i) != '.')
                count++; // Увеличение счетчика, если символ не точка
            else
                return count; // Возврат текущего значения счетчика, если найдена точка
        }

        return 0; // Возврат 0 если точка не найдена (например, если число целое)
    }



    public static String IEEE754(double num)// метод конвертации в IEEE754(binary32)
    {
        // считываем знак числа и продолжаем вычисления уже для модуля числа
        String sign = num > 0 ? "0" : "1";
        num = Math.abs(num);

        int integer = (int) Math.floor(num); // целая часть числа
        int exp = 0;
        String moved_exp = "";
        double fractional = num - integer; // дробная часть числа

        String integer_bin = Integer.toBinaryString(integer); // целая часть числа в 2-ой сс
        String fract_bin = Binary(fractional, integer_bin.length()); // дробная часть числа в 2-ой сс

        String mantissa = integer_bin + fract_bin; // мантисса
        String binary_num = integer_bin + "." + fract_bin; // число в 2-ой сс

        exp = findExponenta(binary_num);
        moved_exp = Integer.toBinaryString(exp + 127);

        String binaryStr = sign + moved_exp + mantissa.substring(1); // само число в представлении IEEE754

        // если длина binaryStr меньше 32 -> дополняем нулями до 32 символов строку
        if (binaryStr.length() < 32)
        {
            for (int i = binaryStr.length(); i < 32; i++)
                binaryStr += "0";
        }

        System.out.println("Binary: "+ binaryStr);
        System.out.println("\t\t" + binary_num);
        return binaryStr;
    }

    public static void IEEE754_hex(String num)
    {
        System.out.println("Hexadecimal(шестнадцатиричный): " + HexString(num));
    }


    private static String byteHex(String byteStr)//метод byteHex для перевода 4-битных блоков в 16-ричную систему
    {
        int decimal = Integer.parseInt(byteStr, 2); // переводим из 2-ой в 10-ю
        return Integer.toHexString(decimal); // возвращаем переведенное значение из 10-ой в 16-ю
    }

    private static String HexString(String binaryStr)//метод toHexString для конвертации двоичной строки в 16-ричную
    {
        String hexStr = "";
        // берём каждые 4 бита (т.к. значение идет от 0000 (0) до 1111 (15)) и переводим их в 16-ую систему
        for (int i = 0; i <= binaryStr.length() - 4; i+=4)
        {
            hexStr += byteHex(binaryStr.substring(i, i+4));
        }
        return hexStr;
    }
    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in); // Создание объекта Scanner для ввода данных с клавиатуры
        System.out.print("Enter number: ");
        String inNum = in.next(); // Ввод числа с клавиатуры и сохранение его в строковой переменной inNum

        exponentNum(inNum); // Вызов метода exponentNum для обработки экспоненты введенного числа
        String out = IEEE754(Double.parseDouble(inNum)); // Вызов метода IEEE754 для преобразования числа в формат IEEE754 и сохранение результата в строковой переменной out
        IEEE754_hex(out); // Вызов метода IEEE754_hex для вывода шестнадцатеричного представления результата в консоль
    }
}