package ru.bumchik.lesson2;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.functions.Cancellable;


public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView textView;
    Observable observable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setObservable();
    }

    private void initViews() {
        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);
    }

    private void setObservable() {
        observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(final ObservableEmitter<Object> emitter) throws Exception {
                final TextWatcher watcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        textView.setText(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (!emitter.isDisposed()) {
                            emitter.onNext(editable.toString());
                        }
                    }
                };
                editText.addTextChangedListener(watcher);
                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        editText.removeTextChangedListener(watcher);
                    }
                });

            }
        }).subscribe();
    }


}
