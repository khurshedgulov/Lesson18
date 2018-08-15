package company.my.lesson15;

import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MasterFragment extends Fragment {
    // Виджет для показа списка
    ListView listView;
    // Переменная для получения доступа к функциям работы с базой данных
    Database db;

    // Плавающая кнопка в интерфейсе для добавления новых записей
    FloatingActionButton addNew;
    // Текстовое поле
    EditText searchField;

    // Функция вызывается при показе фрагмента внутри (в контексте) Activity
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Инициализация переменной доступа к функциям работы с базой данных
        db = new Database(getContext());

        // Добавить файл макета master_fragment.xml к этому фрагменту
        View v = inflater.inflate(R.layout.master_fragment, container, false);

        // Присвоить переменной класса ссылку к текстовому полю в файле макета
        searchField = v.findViewById(R.id.searchField);
        // Привязать обработчик событий нажатия кнопки поиск
        // (заменяет enter, при указании атрибутов imeOptions="actionSearch" и указании inputType="text")
        // и при нажатии на кнопку поиска вызывается функция onEditorAction и передаётся actionId нажатой кнопки
        searchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // Проверить, если нажатая кнопка это кнопка поиск
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (searchField.getText().length() > 0) {
                        // Передать функции search текст введенный в текстовом поле поиска
                        search(searchField.getText().toString());
                    } else {
                        // Если текстовое поле пустое показать список всех записей
                        setAdapterToList();
                    }
                }
                return true;
            }
        });

        // Присвоить ссылку к объекту FloatingActionButton (круглая кнопка в правом нижнем углу экрана)
        addNew = v.findViewById(R.id.addNewItem);
        // Установить обработчик событий click по кнопке
        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Объявить и инициализировать переменную класса AddItemsDialog
                // для показа диалогового окна добавления записей
                AddItemsDialog addItems = new AddItemsDialog();
                // Показать диаологовое окно для добавления новой записи
                addItems.show(getActivity().getSupportFragmentManager(), "AddItemsDialog");
            }
        });

        // Получение экземпляра ListView размещенного в макете
        listView = v.findViewById(R.id.list);
        // Заполнить listView всеми записями из базы данных
        setAdapterToList();

        // Зарегистрировать элемент для контекстного меню
        // без этого при долгом нажатии контекстное меню не вызывается
        registerForContextMenu(listView);

        // Эту строку можно не писать она для примера использования нескольких
        // контекстных меню в одном окне, но для разных элементов
        registerForContextMenu(addNew);

        // Указать обработчик кликов по элементам списка
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // Каждый раз при клике на какой-либо элемент вызывается функция onItemClick
            // и ей передаётся в качестве параметра корневой элемент макета, в данном случае LinearLayout
            // позиция выбранного элемента и id элемента
            // Через корневой элемент макета переданного через экземпляр класса View
            // можно получить ссылку к любому объекту в макете. Например, через их id.
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Получить ссылку на TextView в файле макета по id
                TextView textView = view.findViewById(R.id.text);
                // Получисть и присвоить значение заголовка выбранного элемента переменной
                String title = textView.getText().toString();
                // Получить и присвоить значение ID из атрибута tag TextView переменной
                int index = (int) textView.getTag();

                // Объявлеине экземпляра переменной DetailFragment
                DetailFragment detail = new DetailFragment();
                // Переменная класса Bundle для передачи информации экземпляру класса DetailFragment
                Bundle args = new Bundle();
                // Добавить целочисленное значение с ключом id и индексом выбранного элемента из списка
                args.putInt("id", index);
                // Установить аргументы в экземпляр фрагмента
                detail.setArguments(args);

                // Получить ссылку на Activity в котором показывается данный фрагмент
                getActivity()
                        // Получить доступ менеджеру фрагментов
                        .getSupportFragmentManager()
                        // Начать транзакцию фрагментов
                        .beginTransaction()
                        // Заменить этот фрагмент со списком заголовков на фрагмент с текстом описания
                        .replace(R.id.fragmentContainer, detail)
                        // Добавить в стэк инициализированных фрагментов, чтобы при нажатии на кнопку назад
                        // пользователю выводился этот фрагмент, если эту строку не писать при нажатии
                        // на кнопку назад Activity закрывается сразу
                        .addToBackStack(null)
                        // Установить анимацию для показа DetailFragment
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        // Завершить транзакцию фрагмента
                        .commit();
            }
        });
        // Вернуть View, в данном случае LinearLayout из файла master_fragment.xml
        return v;
    }

    // Функция для получения искомой строки в базе данных, отправки запроса, получения результатов,
    // создания нового адаптера с новыми результатами (с новым массивом)
    // и привязки адаптера к ListView
    void search(String s) {
        // Получение ссылки к классу базы данных для отправки запроса
        Database d = new Database(getContext());
        // Объявить новый List для хранения объектов DBItem и присвоить значения полученные
        // в результате поиска информации в базе данных
        List<DBItem> results = d.findItems(s);
        // Объявление и инициализация нового адаптера
        SampleListAdapter adapter = new SampleListAdapter(getContext(), results);
        // Привязать адаптер к ListView
        listView.setAdapter(adapter);
    }

    // Функция для получения всех записей и показа в списке
    void setAdapterToList() {
        // Объявление и инициализация экземпляра адаптера класса SimpleListAdapter
        // адаптер служит для соединения данных с виджетом для показа
        // Функция getTitles() написанная в классе Database служит для получения заголовков
        // хранимой в базе данных информации
        SampleListAdapter adapter = new SampleListAdapter(getContext(), db.getTitles());
        // привязать адаптер к ListView
        listView.setAdapter(adapter);
    }

    // Функция для показа контекстного меню
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getActivity().getMenuInflater();
        // Через MenuInflater привязваем файл меню с нужными пунктами к нашему View
        // В одном окне можно зарегистрировать несколько контекстных меню и какое
        // показывать можете указать проверяя id элемента на которого было произведено долгое нажатие
        if (v.getId() == R.id.list) {
            menuInflater.inflate(R.menu.contextdelete, menu);
        } else {
            menuInflater.inflate(R.menu.anothermenu, menu);
        }
    }

    // Функция вызывается при клике на элемент контекстного меню
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // Этот объект нужен для получения позиции нажатого элемента в списке
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        // Проверяем id выбранного меню и делаем необходимые действия в зависимости от этого
        if (item.getItemId() == R.id.delete) {
            Database db = new Database(getContext());
            // По выбранному элементу через выбранную позицию элемента с адаптера получаем объект и его id
            int id = ((DBItem) listView.getAdapter().getItem(info.position)).id;
            // id передаётся функции для удаления записи с базы данных
            // и при правильном удалении список обновляется
            if (db.deleteItemById(id)) {
                setAdapterToList();
            }

        }
        return super.onContextItemSelected(item);
    }
}
