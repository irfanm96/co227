package com.example.android.sip;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements Filterable {

    private static final String TAG = "APP_DEBUG";

    private ArrayList<Contact> contactList;
    Dialog myDialog;

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

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contact_item, viewGroup, false);

        final ViewHolder viewHolder=new ViewHolder(view);
        myDialog=new Dialog(mContext);
        myDialog.setContentView(R.layout.dialog_contact);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Context context=viewGroup.getContext();
        final BaseActivity baseActivity=(BaseActivity)context;

        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView dialog_name=myDialog.findViewById(R.id.tv_dialog_name);
                TextView dialog_phone=myDialog.findViewById(R.id.tv_dialog_phone);
                dialog_name.setText(contactList.get(viewHolder.getAdapterPosition()).getName());
                dialog_phone.setText(contactList.get(viewHolder.getAdapterPosition()).getEmail());
                myDialog.show();
//
//
//                Log.d(TAG, "onClick: clicked");
//                //TODO make this as the sip number which has this
//                BaseActivity.setSipAddress("4001@192.168.43.229");
//                baseActivity.initiateCall();

            }
        });



        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        Log.d(TAG, "onBindViewHolder: called.");
//        Glide.with(mContext)
//                .asBitmap()
//                .load(contactList.get(i).getImage())
//                .into(viewHolder.image);
//
        viewHolder.tv_name.setText(contactList.get(i).getName());
        viewHolder.tv_phone.setText(contactList.get(i).getEmail());

//
//
//        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Log.d(TAG, "onClick: cliked on " + contactList.get(i).getName());
//
//                String sipAddress=contactList.get(i).getPhone()+"@j.veg.lv";
////                WalkieTalkieActivity.sipAddress=sipAddress;
//                Toast.makeText(mContext, sipAddress, Toast.LENGTH_SHORT).show();
//
//                //                sipAddress = textField.getText().toString();
////                initiateCall();
//            }
//        });

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

                    if (c.getName().toLowerCase().contains(filterPatern)) {
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


        ImageView image;
        TextView tv_name;
        TextView tv_phone;
//        RelativeLayout parentLayout;

        LinearLayout linearLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.iv_contact);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_phone = itemView.findViewById(R.id.tv_phone);
//            parentLayout = itemView.findViewById(R.id.parent_layout);
            linearLayout=itemView.findViewById(R.id.item_contact);

        }
    }


}
