package com.onlinevoting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerOptionAdapter extends RecyclerView.Adapter<RecyclerOptionAdapter.ViewHolder> {
    Context context;
    private OnItemClickListener mListener;
    ArrayList<OptionModel> options;
    String activityName="activityName";
    public interface OnItemClickListener {
        void onItemClick(ArrayList<OptionModel> data);
    }
    public void setOnItemClickListner(OnItemClickListener listener) {
        mListener = listener;
    }

    RecyclerOptionAdapter(Context context, ArrayList<OptionModel> options){
        this.context = context;
        this.options = options;
    }
    RecyclerOptionAdapter(Context context, ArrayList<OptionModel> options, String activityName){
        this.context = context;
        this.options = options;
        this.activityName= activityName;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.option_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(activityName!=null){
        if(activityName.equals("VotingActivity")){
            holder.textView.setText(options.get(position).option);
            holder.imageView.setImageResource(options.get(position).image);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String  option = options.get(position).option;
                    holder.imageView.setImageResource(R.drawable.check);
                    options.set(position,new OptionModel(R.drawable.check,option));
                    for(int i =0; i<options.size();i++){
                        String optionn = options.get(i).option;
                        if(i!=position){
                            options.set(i,new OptionModel(R.drawable.edit,optionn));
                            notifyDataSetChanged();
                            if(mListener!=null){
                            mListener.onItemClick(options);}
                        }
                    }

                }
            });

        }else {
            holder.imageView.setImageResource(options.get(position).image);
            holder.textView.setText(options.get(position).option);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    options.remove(position);
                    notifyItemRemoved(position);
                    notifyDataSetChanged();
                }
            });
        }
        }

    }



    @Override
    public int getItemCount() {
        return options.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView  textView;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.option_layout_option);
            imageView = itemView.findViewById(R.id.option_layout_delete);
        }
    }

}
