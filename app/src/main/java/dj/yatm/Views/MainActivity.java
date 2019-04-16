package dj.yatm.Views;

import android.content.Context;
import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

//import androidx.room.Room;
import java.util.Collections;

import dj.yatm.R;
import dj.yatm.model.AbstractListItem;
import dj.yatm.model.ListItem;
import dj.yatm.model.TaskListDbHelper;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mainList;
    private RecyclerView.LayoutManager mainListManager;
    private MainListAdapter mainListAdapter;
    private ImageButton addListButton;
    public ListItem parentList;
    private Presenter presenter;
    private ImageButton sortButton;
    private Spinner sortSpinner;

    public void initVariables(){
        mainListManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mainList.setLayoutManager(mainListManager);
        getSupportActionBar().setTitle(this.parentList.getTitle());
        mainListAdapter = new MainListAdapter(this, this.parentList);
        mainList.setAdapter(mainListAdapter);
    }

    public void assignIDs(){
        mainList = this.findViewById(R.id.list_recycler);
        addListButton = this.findViewById(R.id.add_list_button);
        sortButton = this.findViewById(R.id.sort_button);
        sortSpinner = this.findViewById(R.id.sort_menu);
    }

    public void updateList(){
        this.parentList = this.presenter.rebuildTree(this.parentList);
        mainListAdapter.update(this.parentList);
    }

    public void refreshList(){
        switch (sortSpinner.getSelectedItem().toString()){
            case "Category":
                parentList.sort(AbstractListItem.CATEGORY_ORDER);
                break;
            case "Name":
                parentList.sort(AbstractListItem.TITLE_ORDER);
                break;
            case "Priority":
                parentList.sort(AbstractListItem.PRIORITY_ORDER);
                break;
            case "Date":
                parentList.sort(AbstractListItem.DUE_DATE_ORDER);
                break;
            case "Completed":
                parentList.sort(AbstractListItem.COMPLETED_ORDER);
                break;
        }
        mainListAdapter.update(this.parentList);
    }

    @Override
    public void onResume(){
        super.onResume();
        if (this.parentList != null) {
            updateList();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("yatm", "first");
        TaskListDbHelper.init(getApplicationContext());
        presenter = new Presenter();
        setContentView(R.layout.activity_main);
        Bundle bundle = this.getIntent().getExtras();
        ListItem listItem;
        if (bundle != null && getIntent().hasExtra("tasks")) {
            listItem = (ListItem) bundle.getSerializable("tasks");
        }
        else {
            try {
                listItem = TaskListDbHelper.getInstance().buildTreeFromId(1);
            } catch (CursorIndexOutOfBoundsException cioobe) {
                listItem = new ListItem("Task List", 1, "", null);
            }
        }
        assignIDs();

        this.parentList = listItem;
        initVariables();

        addListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListDataActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("parent", parentList);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortSpinner.performClick();
            }
        });

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                refreshList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //TaskListDbHelper.getInstance().close();
    }

    class MainListAdapter extends RecyclerView.Adapter<MainListHolder> {
        private LayoutInflater inflater;
        private ListItem list;

        public MainListAdapter(Context context, ListItem list){
            inflater = LayoutInflater.from(context);
            this.list = list;
        }

        public void update(ListItem newList){
            list = newList;
            notifyDataSetChanged();
        }

        @Override
        public MainListHolder onCreateViewHolder(ViewGroup parent, int viewType){
            View view = inflater.inflate(R.layout.list_item, parent, false);
            return new MainListHolder(view);
        }

        @Override
        public void onBindViewHolder(MainListHolder holder, int position){
            ListItem item = (ListItem)list.getAt(position);
            holder.bind(item);
        }

        @Override
        public int getItemCount() {
            return list.countItems(); // number of items
        }
    }

    class MainListHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView title;
        ImageButton check;
        ListItem item;
        private Presenter presenter;
        RelativeLayout wholeItem;
        TextView date;
        TextView subtaskCount;

        public MainListHolder(final View view){
            super(view);
            presenter = new Presenter();
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            title = view.findViewById(R.id.title);
            check = view.findViewById(R.id.check_button);
            wholeItem = view.findViewById(R.id.list_item_whole);
            date = view.findViewById(R.id.date_text_main);
            subtaskCount = view.findViewById(R.id.subtaskCount);
            check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!item.isComplete()) {
                        check.setImageResource(R.drawable.check_checked);
                        item.setCompletenessForAll(true, true);
                        presenter.updateTask(item);
                        wholeItem.setBackgroundColor(Color.parseColor("#ffffff"));
                    } else {
                        check.setImageResource(R.drawable.check);
                        item.setCompletenessForAll(false, true);
                        presenter.updateTask(item);
                        switch(item.getPriority()){
                            case 1:
                                wholeItem.setBackgroundColor(Color.parseColor("#ffd9d9"));
                                break;
                            case 2:
                                wholeItem.setBackgroundColor(Color.parseColor("#fffed9"));
                                break;
                            case 3:
                                wholeItem.setBackgroundColor(Color.parseColor("#d9ffe6"));
                                break;
                        }
                    }
                }
            });
        }

        void bind(ListItem item){
            title.setText(item.getTitle()); //item.getTitle());
            if (item.getDueDate() != null) {
                date.setText(item.getDueDate().toString());
            }
            else
                date.setText("");
            int subCount = item.countItems();
            String strSubCnt = String.format("(%d subtask%s)",
                    subCount,
                    subCount != 1 ? "s" : "");
            subtaskCount.setText(strSubCnt);
            switch(item.getPriority()){
                case 1:
                    this.wholeItem.setBackgroundColor(Color.parseColor("#ffeaea"));
                    break;
                case 2:
                    this.wholeItem.setBackgroundColor(Color.parseColor("#fffeea"));
                    break;
                case 3:
                    this.wholeItem.setBackgroundColor(Color.parseColor("#effff5"));
                    break;
            }
            check.setImageResource(item.isComplete() ? R.drawable.check_checked : R.drawable.check);
            this.item = item;
            return;
        }
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);

            Bundle bundle = new Bundle();
            bundle.putSerializable("tasks", item);
            intent.putExtras(bundle);
            startActivity(intent);
        }

        @Override
        public boolean onLongClick(View view) {
            Intent intent = new Intent(MainActivity.this, ListDataActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("parent", parentList);
            bundle.putSerializable("Current", this.item);
            intent.putExtras(bundle);
            startActivity(intent);
            Log.d("yatm", "It worked");
            return true;
        }

    }

}
