package dj.yatm.Views;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import dj.yatm.model.TaskListDbHelper;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mainList;
    private RecyclerView.LayoutManager mainListManager;
    private MainListAdapter mainListAdapter;
    private ImageButton addListButton;
    private TextView title;
    public ListItem parentList;
    private Presenter presenter;

    private static IListItemObserver database;

    public static IListItemObserver getDatabase() {
        return database;
    }

    public void initVariables(){
        mainListManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mainList.setLayoutManager(mainListManager);
        title.setText(this.parentList.getTitle());
        mainListAdapter = new MainListAdapter(this, this.parentList);
        mainList.setAdapter(mainListAdapter);
    }

    public void assignIDs(){
        mainList = this.findViewById(R.id.list_recycler);
        addListButton = this.findViewById(R.id.add_list_button);
        title = this.findViewById(R.id.title);
    }

    public void updateList(){
        this.parentList = this.presenter.rebuildTree(this.parentList);

        mainListAdapter.notifyDataSetChanged();
        Log.d("yatm", "something");
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
        presenter = new Presenter();
        TaskListDbHelper.init(getApplicationContext());
        TaskListDbHelper.getInstance().totalReset();
        database = TaskListContract.get();
        setContentView(R.layout.activity_main);
        Bundle bundle = this.getIntent().getExtras();
        ListItem listItem = null;
        if (bundle != null) {
            listItem = (ListItem) bundle.getSerializable("tasks");
        }
        assignIDs();

        if (listItem == null) {
            listItem = new ListItem(database, "root", 1, "", null);

            ListItem newItem = new ListItem(database, "Walk Dog", 1, "", null);
            listItem.addItem(newItem);

            newItem = new ListItem(database, "Ask for Raise", 1, "", null);
            listItem.addItem(newItem);

            newItem = new ListItem(database, "Make Model", 1, "", null);
            listItem.addItem(newItem);

            newItem = new ListItem(database, "Chicken Salad List", 1, "", null);
            newItem.addItem(new ListItem(database, "Chicken", 1, "",null));
            newItem.addItem(new ListItem(database, "Lettuce", 1,"", null));
            newItem.addItem(new ListItem(database, "Dressing", 1, "",null));
            listItem.addItem(newItem);
        }
        this.parentList = listItem;
        initVariables();

        Log.d("yatm", this.parentList.toString());


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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TaskListDbHelper.getInstance().close();
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
        private Presenter presenter;

        public MainListHolder(final View view){
            super(view);
            presenter = new Presenter();
            view.setOnClickListener(this);
            title = view.findViewById(R.id.title);
            check = view.findViewById(R.id.check_button);
            check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!item.isComplete()) {
                        check.setImageResource(R.drawable.check_checked);
                        item.setCompletenessForAll(true, true);
                        presenter.updateTask(item);
                    } else {
                        check.setImageResource(R.drawable.check);
                        item.setCompletenessForAll(false, true);
                        presenter.updateTask(item);
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

            Bundle bundle = new Bundle();
            bundle.putSerializable("tasks", item);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

}
