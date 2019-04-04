package com.example.android.sip;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements Filterable {

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<Contact> contactList;

    public void setContactList(ArrayList<Contact> contactList) {
        this.contactList = contactList;
    }

    public ArrayList<Contact> getContactList() {
        return contactList;
    }

    public ArrayList<Contact> getContactListFull() {
        return contactListFull;
    }

    private ArrayList<Contact> contactListFull;
    private Context mContext;

    public RecyclerViewAdapter(Context mContext, ArrayList<Contact> contactList) {
        this.contactList = contactList;
        contactListFull = new ArrayList<>(contactList);
        this.mContext = mContext;
    }

    public void setContactListFull(ArrayList<Contact> contactListFull) {
        this.contactListFull.clear();
        this.contactListFull.addAll(contactListFull);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        Log.d(TAG, "onBindViewHolder: called.");
//        Glide.with(mContext)
//                .asBitmap()
//                .load(contactList.get(i).getImage())
//                .into(viewHolder.image);
//
        viewHolder.imageName.setText(contactList.get(i).getImageName());



        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: cliked on " + contactList.get(i).getImageName());
                Toast.makeText(mContext, contactList.get(i).getImageName(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Contact> filteredList = new ArrayList<>();
//            System.out.println("filter list size"+contactListFull.size());
//            System.out.println("original list size"+contactListFull.size());

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(contactListFull);
            } else {
                String filterPatern = constraint.toString().toLowerCase().trim();

                for (Contact c : contactListFull) {

                    if (c.getImageName().toLowerCase().contains(filterPatern)) {
                        filteredList.add(c);
                    }

                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            contactList.clear();
            contactList.addAll((ArrayList<Contact>) results.values);
            notifyDataSetChanged();

        }


    };


    public class ViewHolder extends RecyclerView.ViewHolder {


        CircleImageView image;
        TextView imageName;
        RelativeLayout parentLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            imageName = itemView.findViewById(R.id.tvImageName);
            parentLayout = itemView.findViewById(R.id.parent_layout);

        }
    }


}