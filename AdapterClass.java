package com.example.gpskeychain;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterClass extends RecyclerView.Adapter<AdapterClass.UserViewHolder> {

    private List<UserClass> list;

    public AdapterClass(List<UserClass> list){
        this.list=list;
    }

    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler,parent,false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.textViewId.setText(Integer.toString(list.get(position).getId()));
        holder.textViewFname.setText(list.get(position).getFname());
        holder.textViewLname.setText(list.get(position).getLname());
        holder.textViewUsername.setText(list.get(position).getUsername());
        holder.textViewPassword.setText(list.get(position).getPassword());
    }

    @Override
    public int getItemCount() {
        Log.v(AdapterClass.class.getSimpleName(),""+list.size());
        return list.size();
    }


    public class UserViewHolder extends RecyclerView.ViewHolder {
        public AppCompatTextView textViewId;
        public AppCompatTextView textViewFname;
        public AppCompatTextView textViewLname;
        public AppCompatTextView textViewUsername;
        public AppCompatTextView textViewPassword;


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewId=itemView.findViewById(R.id.textViewId);
            textViewFname=itemView.findViewById(R.id.textViewFname);
            textViewLname=itemView.findViewById(R.id.textViewLname);
            textViewUsername=itemView.findViewById(R.id.textViewUsername);
            textViewPassword=itemView.findViewById(R.id.textViewPassword);
        }
    }
}
