package dj.yatm.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import dj.yatm.R;

public class ListDataActivity extends AppCompatActivity {

    private EditText name;
    private Spinner type;
    private Spinner priority;
    private Button saveButton;

    public void initVariables(){
        name = findViewById(R.id.name_edit_text);
        type = findViewById(R.id.type_spinner);
        priority = findViewById(R.id.priority_spinner);
        saveButton = findViewById(R.id.save_setup_button);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_up_list);
        initVariables();
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
