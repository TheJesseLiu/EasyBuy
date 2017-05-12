package com.example.ee4901.project2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>  {
    public interface OnItemClickListener {
        void onItemClick(MyData item, View view);
    }
    private Context context;
    private List<MyData> my_data;
    private final OnItemClickListener listener;

    public CustomAdapter(Context context, List<MyData> my_data, OnItemClickListener listener) {
        this.context = context;
        this.my_data = my_data;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_card_view,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.description.setText("Prodcut Name: "+my_data.get(position).getItemName()+"\nPrice: "+my_data.get(position).getPrice());
        Glide.with(context).load(my_data.get(position).getLink()).into(holder.imageView);
        holder.bind(my_data.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }

    /*
    public MyData getItem(int position) {
        return my_data.get(position);
    }
    */

    static class ViewHolder extends  RecyclerView.ViewHolder{

        private TextView description;
        private ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            description = (TextView) itemView.findViewById(R.id.description);
            imageView = (ImageView) itemView.findViewById(R.id.image);
        }

        public void bind(final MyData item, final OnItemClickListener listener) {
            /*
            name.setText(item.name);
            Picasso.with(itemView.getContext()).load(item.imageUrl).into(image);
            */
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item, itemView);
                }
            });
            description.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item, itemView);
                }
            });
        }
    }
}
