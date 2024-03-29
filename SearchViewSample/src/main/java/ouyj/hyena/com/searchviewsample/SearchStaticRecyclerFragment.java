package ouyj.hyena.com.searchviewsample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SearchStaticRecyclerFragment extends Fragment {

    public SearchStaticRecyclerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search_static_recycler, container, false);
        RecyclerView recyclerView = ((RecyclerView) rootView.findViewById(R.id.search_static_recycler));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            strings.add(i+" -- \n"+getString(R.string.lorem_1));
        }
        ListViewAdapter adapter = new ListViewAdapter(strings);
        recyclerView.setAdapter(adapter);
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        return rootView;
    }
    
    // Define a public click listener interface for items of the v7.RecyclerView which has no OnItemClickListener by default
    public interface ListOnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ListViewHolder> {
        public List<String> mStringList;
        public ListViewAdapter(List<String> stringList) {
            this.mStringList = stringList;
        }
        
        private ListOnItemClickListener mOnItemClickListener;
        public void setListOnItemClickListener(ListOnItemClickListener mOnItemClickListener) {
            this.mOnItemClickListener = mOnItemClickListener;
        }

        class ListViewHolder extends RecyclerView.ViewHolder {
            private final TextView mDetailText;
            public ListViewHolder(View itemView) {
                super(itemView);
                mDetailText = (TextView) itemView.findViewById(R.id.card_details);
            }
        }

        @Override
        public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ListViewHolder(LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.search_static_list_item, parent, false));
        }

        @Override
        public int getItemCount() {
            return mStringList.size();
        }

        @Override
        public void onBindViewHolder(final ListViewHolder viewHolder, int position) {
            viewHolder.mDetailText.setText(mStringList.get(position));
            
            // Click event called here
            if (mOnItemClickListener != null) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = viewHolder.getLayoutPosition();
                        mOnItemClickListener.onItemClick(viewHolder.itemView, pos);
                    }
                });

                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int pos = viewHolder.getLayoutPosition();
                        mOnItemClickListener.onItemLongClick(viewHolder.itemView, pos);
                        return false;
                    }
                });
            }
            
        }
    }
}
