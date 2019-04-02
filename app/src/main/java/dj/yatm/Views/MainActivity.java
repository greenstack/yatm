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

    public void initVariables(ArrayList<ListItem> items){
        mainListManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mainList.setLayoutManager(mainListManager);
        if (items == null) {
            items = new ArrayList<>();

            ListItem newItem = new ListItem(database);
            newItem.setTitle("Todo List");
            items.add(newItem);

            newItem = new ListItem(database);
            newItem.setTitle("Walk Dog");
            items.add(newItem);

            newItem = new ListItem(database);
            newItem.setTitle("Ask for Raise");
            items.add(newItem);

            newItem = new ListItem(database);
            newItem.setTitle("Make Model");
            items.add(newItem);

            newItem = new ListItem(database);
            newItem.setTitle("Chicken Salad List");
            items.add(newItem);
        }
        title.setText(items.get(0).getTitle());
        items.remove(0);
        mainListAdapter = new MainListAdapter(this, items);
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
        database = null;//Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "yatm").build();
        setContentView(R.layout.activity_main);
        Bundle bundle = this.getIntent().getExtras();
        ArrayList<ListItem> items = null;
        if (bundle != null) {
            items = (ArrayList<ListItem>) bundle.getSerializable("tasks");
        }
        List<AbstractListItem> list = null;
        assignIDs();
        initVariables(items);

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
        private List<ListItem> items;

        public MainListAdapter(Context context, List<ListItem> items){
            inflater = LayoutInflater.from(context);
            this.items = items;
        }

        @Override
        public MainListHolder onCreateViewHolder(ViewGroup parent, int viewType){
            View view = inflater.inflate(R.layout.list_item, parent, false);
            return new MainListHolder(view);
        }

        @Override
        public void onBindViewHolder(MainListHolder holder, int position){
            ListItem item = items.get(position);
            holder.bind(item);
        }

        @Override
        public int getItemCount() {
            return items.size(); // number of items
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
                    if (item.isComplete()) {
                        check.setImageResource(R.drawable.check);
                        item.setCompletenessForAll(true, true);
                    } else {
                        check.setImageResource(R.drawable.check_checked);
                        item.setCompletenessForAll(false, true);
                    }
                }
            });
        }

        void bind(ListItem item){
            title.setText(item.getTitle()); //item.getTitle());
            this.item = item;
            return;
        }
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            ListItem newData = new ListItem(database);
            if (item.getTitle().equals("Chicken Salad List")){
                newData.addItem(item);
                ListItem newItem = new ListItem(database);
                newData.setTitle("chicken");

                newItem = new ListItem(database);
                newData.addItem(newItem);

                newItem = new ListItem(database);
                newItem.setTitle("lettuce");
                newData.addItem(newItem);

                newItem = new ListItem(database);
                newItem.setTitle("Dressing");
                newData.addItem(newItem);
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
