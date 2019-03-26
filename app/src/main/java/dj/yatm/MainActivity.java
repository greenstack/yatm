package dj.yatm;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import dj.yatm.model.AbstractListItem;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mainList;
    private RecyclerView.LayoutManager mainListManager;
    private MainListAdapter mainListAdapter;


    public void initVariables(){
        mainListManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mainList.setLayoutManager(mainListManager);
        mainListAdapter = new MainListAdapter((this));
        mainList.setAdapter(mainListAdapter);
    }

    public void assignIDs(){
        mainList = this.findViewById(R.id.list_recycler);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<AbstractListItem> list = null;
        assignIDs();
        initVariables();

    }

    class MainListAdapter extends RecyclerView.Adapter<MainListHolder> {
        private LayoutInflater inflater;
        private List<AbstractListItem> items;

        public MainListAdapter(Context context, List<AbstractListItem> items){
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
            AbstractListItem item = items.get(position); // ListItem
            holder.bind(item);
        }

        @Override
        public int getItemCount() {
            return items.size(); // number of items
        }
    }

    class MainListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        AbstractListItem item;


        public MainListHolder(final View view){
            super(view);
            view.setOnClickListener(this);
            title = view.findViewById(R.id.title);
        }

        void bind(AbstractListItem item){
            title.setText(item.getTitle());
            return;
        }
        @Override
        public void onClick(View view){
            return;
        }
    }

}
