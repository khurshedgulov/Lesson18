# Урок 18
На этом уроке работа над проектом по разработке приложения справочник закончена

## В этом уроке добавлены следующие возможности в приложение:
- Возможность поиска из базы данных
- Добавление новых записей в базу данных
- Удаление записей из базы данных
- Показ контекстного меню для удаления при долгом нажатии на элемент списка
- Изменены цвета оформления в файле res/colors.xml
- Добавлено диалоговое окно (DialogFragment) для показа окна ввода новой информации
- Добавлено текстовое поле для поиска

## Изменены и добавлены следующие файлы
Файлы классов [здесь](https://github.com/khurshedgulov/Lesson18/tree/master/app/src/main/java/company/my/lesson15)

Файлы ресурсов [здесь](https://github.com/khurshedgulov/Lesson18/tree/master/app/src/main/res)

- Добавлен файл меню contextdelete.xml (если у вас нет папки menu в папке res, нажмите правой кнопкой мыши на папку res и выберите New -> Android Resource Directory, затем Resource Type выберите menu и потом нажимая правую кнопку в папке menu выберите New -> Menu Resource File)
- Изменен макет master_fragment.xml
- Изменен файл adapter_item.xml
- Добавлен макет диалогового окна add_items.xml

### Классы Java
- Добавлен AddItemsDialog.java
- Изменен Database.java
- Изменен MasterFragment.java
- Изменен MainActivity.java
