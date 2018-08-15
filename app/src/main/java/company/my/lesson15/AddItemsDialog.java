package company.my.lesson15;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddItemsDialog extends DialogFragment {

    // Интерфейс это объявление функций, только без кода.
    // Как бы стандарт которого надо придерживаться в классах в которх
    // через ключевое слово implements, объявляется соответствие интерфейсу
    // в данном случае MainActivity должен соответствовать интерфейсу IRefreshList
    // и для соответствия в MainActivity должны быть функции объявленные в IRefreshList
    // В данном случае интерфейс IRefreshList требует написания кода для всего одной
    // функции с именем RefreshList(), который не возвращает значения (void)
    interface IRefreshList {
        void RefreshList();
    }

    EditText titleText;
    EditText contentText;
    IRefreshList activity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.add_items, null);
        builder.setView(v);
        builder.setMessage("Добавить новую запись");
        titleText = v.findViewById(R.id.titleText);
        contentText = v.findViewById(R.id.contentText);
        builder.setIcon(R.mipmap.ic_launcher);

        builder.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Database db = new Database(getContext());
                if(db.addItem(new DBItem(0, titleText.getText().toString(), contentText.getText().toString()))) {
                    Toast.makeText(getContext(), "Запись добавлена", Toast.LENGTH_SHORT).show();
                    // Обновить ListView в activity после добавления записи, для показа новых данных
                    activity.RefreshList();
                } else {
                    Toast.makeText(getContext(), "Ошибка при добавлении записи повторите заново", Toast.LENGTH_SHORT).show();
                }
                dismiss();
            }
        });

        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        return builder.create();
    }

    // Функцию вызывается при показе фрагмента в activity
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Используется приведение типа context, которым является activity в котором показывается это окно
        // к типу интерфейса IRefreshList, который декларирует что необходимо написать функцию RefreshList
        activity = (IRefreshList) context;
    }
}
