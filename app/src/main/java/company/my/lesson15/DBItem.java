package company.my.lesson15;

// Класс для хранения объектов запрошенных с базы данных
// структура класса соответствует структуре необходимых нам данных
public class DBItem {
    int id;
    String title;
    String content;

    // Пустой конструктор служит для инициализации
    // экземпляров этого класса в других классах без
    // первоначальных значений
    public DBItem() {}

    // Конструктор принимающий два значения служит
    // для заполнения только id и заголовка элементов
    // в этом примере этот конструктор используется в
    // запросе функции getTitles() класса Database
    public DBItem(int id, String title) {
        this.id = id;
        this.title = title;
    }

    // Конструктор принимающий три значений используется
    // для инициализации переменной для получения данных по
    // id через функцию getItemById, который возвращает значения строк
    // для столбцов значения id, которых соответствует запрошенному id
    public DBItem(int id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}