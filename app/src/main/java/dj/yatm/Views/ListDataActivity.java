package dj.yatm.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Date;

import dj.yatm.R;
import dj.yatm.model.ListItem;

public class ListDataActivity extends AppCompatActivity {

    private EditText name;
    private Spinner type;
    private Spinner priority;
    private Button saveButton;
    private Presenter presenter;
    public ListItem current;
    public ListItem parent;
    public CalendarView calendarView;

    public void initVariables(){
        name = findViewById(R.id.name_edit_text);
        type = findViewById(R.id.type_spinner);
        priority = findViewById(R.id.priority_spinner);
        saveButton = findViewById(R.id.save_setup_button);
        calendarView = findViewById(R.id.calendarView);
        presenter = new Presenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_up_list);
        initVariables();
        Bundle bundle = this.getIntent().getExtras();
        this.parent = null;

        // Setting up info from previous activity
        if (bundle != null) {
            this.parent = (ListItem) bundle.getSerializable("parent");
        }
        if (bundle.getSerializable("Current") != null){ // If you are editing
            this.current = (ListItem) bundle.getSerializable("Current");
            this.name.setText(this.current.getTitle());
            this.priority.setSelection(this.current.getPriority());
//            this.priority.setSelection(this.current.getCategory());
            getSupportActionBar().setTitle("Edit Task");
        } else {
            getSupportActionBar().setTitle("Create New Task");
        }

        Log.d("yatm", parent.toString());
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numPriority = 0;
                switch(priority.getSelectedItem().toString()){
                    case "High":
                        numPriority = 1;
                        break;
                    case "Medium":
                        numPriority = 2;
                        break;
                    case "Low":
                        numPriority = 3;
                        break;
                }
                if (current == null) {
                    Date newDate = new Date(calendarView.getDate());
                    current = new ListItem(
                            name.getText().toString(),
                            numPriority,
                            type.getSelectedItem().toString(),
                            newDate,
                            true
                    );
                    // This is where set date will go.
                    //presenter.createTask(listItem);
                    parent.addItem(current);

                } else {
                    current.setTitle(name.getText().toString());
                    current.setPriority(numPriority);
                    current.setCategory(type.getSelectedItem().toString());
                    current.setDueDate(new Date(calendarView.getDate()));
                    // set the date here.
                }
                finish();
            }
        });
    }


}
