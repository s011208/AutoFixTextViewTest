package bj4.yhh.autofixtextviewtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextViewDefault, mTextViewMainItem, mTextViewNumberInList, mTextViewSubTitle;
    private EditText mEditTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewDefault = (TextView) findViewById(R.id.autofit_default);
        mTextViewMainItem = (TextView) findViewById(R.id.autofit_mainitem);
        mTextViewNumberInList = (TextView) findViewById(R.id.autofit_number_in_list);
        mTextViewSubTitle = (TextView) findViewById(R.id.autofit_sub_title);
        mEditTextView = (EditText) findViewById(R.id.edit);

        mEditTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mTextViewDefault.setText(editable.toString() + editable.toString());
                mTextViewMainItem.setText(editable.toString() + editable.toString());
                mTextViewNumberInList.setText(editable.toString() + editable.toString());
                mTextViewSubTitle.setText(editable.toString() + editable.toString());
            }
        });
    }
}
