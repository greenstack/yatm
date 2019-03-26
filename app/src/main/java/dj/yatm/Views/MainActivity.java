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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dj.yatm.R;
import dj.yatm.model.AbstractListItem;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mainList;
    private RecyclerView.LayoutManager mainListManager;
    private MainListAdapter mainListAdapter;
    private ImageButton addListButton;
    private TextView title;


    public void initVariables(ArrayList<String> strings){
        mainListManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mainList.setLayoutManager(mainListManager);
        if (strings == null) {
            strings = new ArrayList<>();
            strings.add("Tasks");
            strings.add("Walk Dog");
            strings.add("Ask for Raise");
            strings.add("Make Model");
            strings.add("Chicken Salad List");
        }
        title.setText(strings.get(0));
        strings.remove(0);
        mainListAdapter = new MainListAdapter(this, strings);
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
        setContentView(R.layout.activity_main);
        Bundle bundle = this.getIntent().getExtras();
        ArrayList<String> strings = null;
        if (bundle != null) {
            strings = (ArrayList<String>) bundle.getSerializable("strings");
        }
        List<AbstractListItem> list = null;
        assignIDs();
        initVariables(strings);

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
        private List<String> items;

        public MainListAdapter(Context context, List<String> items){
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
            String item = items.get(position);
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
        String item;
        boolean isChecked;


        public MainListHolder(final View view){
            super(view);
            view.setOnClickListener(this);
            title = view.findViewById(R.id.title);
            check = view.findViewById(R.id.check_button);
            isChecked = false;
            check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isChecked) {
                        check.setImageResource(R.drawable.check);
                        isChecked = false;
                    } else {
                        check.setImageResource(R.drawable.check_checked);
                        isChecked = true;
                    }
                }
            });
        }

        void bind(String item){
            title.setText(item); //item.getTitle());
            this.item = item;
            return;
        }
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            ArrayList<String> newData = new ArrayList<>();
            if (item.equals("Chicken Salad List")){
                newData.add(item);
                newData.add("chicken");
                newData.add("lettuce");
                newData.add("Dressing");
            } else {
                newData.add(item);
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable("strings", newData);
            intent.putExtras(bundle);
            startActivity(intent);

        }
    }

}
