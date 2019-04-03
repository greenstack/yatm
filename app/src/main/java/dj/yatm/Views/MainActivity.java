package dj.yatm.Views;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

//import androidx.room.Room;
import dj.yatm.R;
import dj.yatm.model.AbstractListItem;
import dj.yatm.model.IListItemObserver;
import dj.yatm.model.ListItem;
import dj.yatm.model.TaskListContract;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mainList;
    private RecyclerView.LayoutManager mainListManager;
    private MainListAdapter mainListAdapter;
    private ImageButton addListButton;
    private TextView title;

    private static IListItemObserver database;

    public static IListItemObserver getDatabase() {
        return database;
    }

    public void initVariables(ListItem listItem){
        mainListManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mainList.setLayoutManager(mainListManager);
        if (listItem == null) {
            listItem = new ListItem(database);

            ListItem newItem = new ListItem(database);
            newItem.setTitle("Walk Dog");
            listItem.addItem(newItem);

            newItem = new ListItem(database);
            newItem.setTitle("Ask for Raise");
            listItem.addItem(newItem);

            newItem = new ListItem(database);
            newItem.setTitle("Make Model");
            listItem.addItem(newItem);

            newItem = new ListItem(database);
            newItem.setTitle("Chicken Salad List");
            newItem.addItem(new ListItem(database, "Chicken", 1, null));
            newItem.addItem(new ListItem(database, "Lettuce", 1, null));
            newItem.addItem(new ListItem(database, "Dressing", 1, null));
            listItem.addItem(newItem);
        }
        title.setText(listItem.getTitle());
        mainListAdapter = new MainListAdapter(this, listItem);
        mainList.setAdapter(mainListAdapter);
    }

    public void assignIDs(){
        mainList = this.findViewById(R.id.list_recycler);
        addListButton = this.findViewById(R.id.add_list_button);
        title = this.findViewById(R.id.title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = TaskListContract.init(getApplicationContext());
        setContentView(R.layout.activity_main);
        Bundle bundle = this.getIntent().getExtras();
        ListItem listItem = null;
        if (bundle != null) {
            listItem = (ListItem) bundle.getSerializable("tasks");
        }
        assignIDs();
        initVariables(listItem);

        addListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListDataActivity.class);
                startActivity(intent);
            }
        });

    }

    class MainListAdapter extends RecyclerView.Adapter<MainListHolder> {
        private LayoutInflater inflater;
        private ListItem list;

        public MainListAdapter(Context context, ListItem list){
            inflater = LayoutInflater.from(context);
            this.list = list;
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

    class MainListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        ImageButton check;
        ListItem item;

        public MainListHolder(final View view){
            super(view);
            view.setOnClickListener(this);
            title = view.findViewById(R.id.title);
            check = view.findViewById(R.id.check_button);
            check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!item.isComplete()) {
                        check.setImageResource(R.drawable.check_checked);
                        item.setCompletenessForAll(true, true);
                    } else {
                        check.setImageResource(R.drawable.check);
                        item.setCompletenessForAll(false, true);
                    }
                }
            });
        }

        void bind(ListItem item){
            title.setText(item.getTitle()); //item.getTitle());
            check.setImageResource(item.isComplete() ? R.drawable.check_checked : R.drawable.check);
            this.item = item;
            return;
        }
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            ListItem newData = new ListItem(database);
            if (item.countItems() > 0){
                for (AbstractListItem ali : item.getSubTasks()) {
                    newData.addItem(ali, false);
                }
            } else {
                newData.addItem(item);
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable("tasks", newData);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

}
