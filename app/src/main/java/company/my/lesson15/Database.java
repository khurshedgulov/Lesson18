package company.my.lesson15;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

// Класс помощник для работы с базой данных
public class Database extends SQLiteOpenHelper {

    // Версия схемы базы данных
    // Под схемой понимается понятие количество и порядка столбцов базы данных
    // При изменении количества, названия или типа хранимой информации столбцов
    // необходимо увеличить это значение и написать код применяющий изменения
    // в уже существующие таблицы в onUpgrade
    final static int VERSION = 1;

    // Название базы данных
    // Название можно не писать здесь, а передавать через аргументы конструктора
    // если в вашем приложении используется несколько баз данных, но в этом
    // примере название базы данных пишется в этом классе так, как используется
    // только одна база данных для хранения информации
    final static String DB_NAME = "info_sqlite";

    // Переменная для хранения ссылки на контекст инициализирующий экземпляр класса
    Context c;

    // Конструктор класса для инициализации базы данных с соответствующей версией
    public Database(Context context) {
        // Вызов конструктора суперкласса (SQLiteOpenHelper) с передачей контекста, названия базы данных и версии
        super(context, DB_NAME, null, VERSION);
        // Присвоить переменной для хранения контекста значение
        c = context;
    }

    // Функция вызывается во время устаноки приложения
    // Все необходимые для первой работы приложения действия надо прописать здесь
    // В нашем случае эта функция используется для создания таблицы в базе данных для
    // хранения информации о странах
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Запрос на создание новой таблицы с 4 столбцами
        // у каждого столбца как и у каждой переменной есть имя и тип,
        // сначала указывается название столбца, затем тип хранимой информации
        // длина хранимой информации указывается внутри скобок после названия типа
        // int - целочисленный тип, int(11) - для хранения целого числа длиной максимум 11 цифр. Например: 123647975621
        // Команды отправляемые к SQLite называются запросами и запрос CREATE TABLE служит для создания таблицы
        // с указанным названием и столбцами в базе данных
        db.execSQL("CREATE TABLE info (id INTEGER PRIMARY KEY AUTOINCREMENT, title varchar(250), search_field varchar(250), content text);");

        // Запрос INSERT служит для добавления записей в базу данных
        // Формат написания: INSERT INTO название_таблицы VALUES(значения для столбцов таблицы).
        // Количество и тип значений внутри скобок VALUES должны соответствовать столбцам базы данных
        // и значение для каждого столбца должно быть разделено запятой друг от друга
        // Если после названия таблицы в скобках не указываются столбцы для которых следует
        // добавить данные необходимо указать данные для всех столбцов по порядку их создания
        // В случае указания названия столбцов после названия таблицы надо указывать значения с соблюдением порядка
        // и типа данных столбцов указываемых внутри скобок после названия таблицы,
        // например INSERT INTO info(title, content) VALUES ('My sample title', 'Content for my title');
        // При добавлении текстовых значений их следует указывать внутри 'одинарных кавычек'
        // Если в вашем тексте есть одинарные кавычки тогда их нужно экранировать путём добавления
        // ещё одной одинарной кавычки перед существующей кавычкой,
        // например 'My title's content', следует изменить на 'My title''s content'
        // При добавления текста из кода самый удобный вариант экранирования это функция replaceAll класса String
        // mytitle = "It's my life";
        // mytitle.replaceAll("'", "''"); - в данном случае mytitle равен It''s my life
        db.execSQL("INSERT INTO info VALUES(1, 'Тоҷикистон', 'тоҷикистон', \"Маълумот дар бораи Тоҷикистон\")");
        db.execSQL("INSERT INTO info VALUES(2, 'Ӯзбекистон', 'ӯзбекистон', \"Маълумот дар бораи Ӯзбекистон\")");
        db.execSQL("INSERT INTO info VALUES(3, 'Қирғизистон', 'қирғизистон', \"Маълумот дар бораи Қирғизистон\")");
        db.execSQL("INSERT INTO info VALUES(4, 'Қазоқистон', 'қазоқистон', \"Маълумот дар бораи Қазоқистон\")");
        db.execSQL("INSERT INTO info VALUES(5, 'Афғонистон', 'афғонистон', \"Маълумот дар бораи Афғонистон\")");
    }

    // Фукнция вызывается при обновлении существующего приложения
    // Код для изменения данных и структуры данных при обновлении
    // приложения должен быть записан тут
    @Override
    public void onUpgrade(SQLiteDatabase db, int o, int n) {

    }

    // Функция для получения заголовков для показа в ListView в MasterFragment
    // возвращает List с данными класса DBItem, который используем в качестве
    // объекта с необходимой для нас структурой, который удобен при
    // передаче между функциями, так как в одном объекте мы храним
    // сразу три переменные двух типов
    public List<DBItem> getTitles() {
        // Объявление пустого массива
        List<DBItem> items = new ArrayList<>();
        // Получение ссылки к базе данных в режиме только чтение
        SQLiteDatabase db = getReadableDatabase();

        try {
            // Объявить экземпляр класса Cursor и инициализировать с
            // результатами запроса для получения всех данных
            // Запрос SELECT служит для получения информации хранящейся в базе данных
            // После SELECT пишутся названия необходимых столбцов через запятую или
            // пишется * для получения всех столбцов, затем FROM название_таблицы
            Cursor c = db.rawQuery("SELECT id, title FROM info", new String[]{});

            // Cursor по умолчанию ставится в конец полученных записей и
            // необходимо проверить и заодно переместить курсор в начало коллекции
            // делается это через функцию moveToFirst() который возвращает true
            // если в коллекции запрошенных данных есть записи
            if (c.moveToFirst()) {
                // Цикл do while нужен для перебора всех запрошенных данных по очереди
                do {
                    // Добавление текущего объекта полученного в ходе запроса в массив
                    items.add(new DBItem(c.getInt(0), c.getString(1)));
                    // while указывает на условие и пока moveToNext() возвращает true
                    // цикл продолжает выполнение, когда cursor в запрошенной коллекции
                    // достигает конца moveToNext() возвращает fale и цикл заканчивается
                } while (c.moveToNext());
            }
            // Перехват исключений для предотвращения закрытия приложения в случае сбоя
        } catch (Exception e) {
            // Вывести в консоль LogCat детали ошибки
            e.printStackTrace();
        }
        // Вернуть массив items в которой хранятся результаты запроса всех заголовков
        return items;
    }

    // Принцип работы нижеследующих функций аналогичен функции getTitles
    public DBItem getItemById(int id) {
        DBItem item = new DBItem();
        SQLiteDatabase db = getReadableDatabase();
        try {
            Cursor c = db.rawQuery("SELECT * FROM info WHERE id = ?", new String[]{String.valueOf(id)});
            if (c.moveToFirst()) {
                do {
                    item = new DBItem(c.getInt(0), c.getString(1), c.getString(3));
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return item;
    }

    // Функция для поиска информации и возвращения результатов
    // в виде массива List с экземплярами элементова класса DBItem
    // Получает запрос для поиска в виде строки и проверяет
    // значения в столбце search_field на совпадение
    // и выбирает все значения в которых содержится искомый текст
    public List<DBItem> findItems(String s) {
        // Пустой массив элементов для хранения результатов поиска
        // и вовзращения в конце функции через инструкцию return
        List<DBItem> items = new ArrayList<>();
        // Получить ссылку к базе данных в режиме только чтение
        SQLiteDatabase db = getReadableDatabase();

        try {
            // Сформировать и выполнить запрос к базе данных для
            // поиска необходимой информации и присвоение результатов
            // запроса объекту экземпляра класса Cursor
            // like в запросах SQL используется для получения результатов
            // в которых совпадает весь текст или только часть искомого текста
            // В какой части искать совпадение зависит от расположения символов %
            // если символы расположены с обеих сторон искомой строки
            // то выбираются записи в которых встречается поисковой запрос
            // независимо от того в начале, в середине или в конце записи столбца
            // встречается запись
            Cursor c = db.rawQuery("SELECT * FROM info WHERE search_field like ?", new String[]{"%" + s.toLowerCase() + "%"});

            // Метод moveToFirst() класса Cursor переводит курсор
            // к первой записи и возвращает true,
            // если записей нет возвращает false
            if (c.moveToFirst()) {
                // При выполнении данной строки значит хотя бы одна запись
                // в базе данных есть, поэтому можно взять её значения,
                // добавить в массив и затем перейти к следующей записи
                // если следующей записи нет moveToNext() возращает false
                // и цикл прерывается и код выполняется со следующей строки
                // после цикла while
                do {
                    items.add(new DBItem(c.getInt(0), c.getString(1), c.getString(3)));
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Вернуть заполненный массив в случае нахождения искомых записей
        // или вернуть пустой массив если записей нет, потому что
        // если записей нет, в строке c.moveToFirst() возвращает false
        // и код написанный внутри тела if не выполняется
        return items;
    }

    // Функция addItem служит для добавления новых записей в базу данных
    // в качестве параметра источника информации для добавления передаётся
    // объект класса DBItem в котором хранятся заголовок и содержимое
    // для новой записи
    public boolean addItem(DBItem item) {
        // В отличии от других функций тут получаем экземпляр
        // базы данных, в которой можно добавлять новые данные.
        SQLiteDatabase db = getWritableDatabase();
        try {
            // Для добавления новых записей служит запрос Insert,
            // подробней об Insert написано в функции onCreate в этом файле
            // Параметр id, явно не вставляем, потому что в этой версии
            // базы данных столбец id указан как PRIMARY KEY с возможностью
            // AUTOINCREMENT, что означает столбец id служит для хранения
            // уникальных значений, которые увеличиваются автоматически на
            // единицу при добавлении новых записей
            Log.i("TAG", "Add items");
            db.execSQL("INSERT INTO info(title, content) VALUES(?,?)", new String[]{item.title, item.content});
            // В случае выполнения запроса без ошибок, вернуть true
            // это необходимо для уведомления пользователя о том, что
            // запись добавлена или нет
            return true;
        } catch (Exception e) {
            // Если возникла какая-нибудь ошибка блок catch перехватывает её
            // и в конце возвращаем false, чтобы уведомить пользователя
            // о том, что возникла ошибка и запись добавить не получилось
            // текст ошибки выводится через метод printStackTrace()
            e.printStackTrace();
            Log.i("TAG", "Error");
            return false;
        }
    }

    // Удалить ненужную запись из списка
    public boolean deleteItemById(int id) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL("DELETE FROM info WHERE id = ?", new String[]{String.valueOf(id)});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
