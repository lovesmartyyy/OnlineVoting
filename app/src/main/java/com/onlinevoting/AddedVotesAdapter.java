package com.onlinevoting;



import android.app.Activity;
import android.app.StartForegroundCalledOnStoppedServiceException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class AddedVotesAdapter extends RecyclerView.Adapter<AddedVotesAdapter.ViewHolder> {
    Context context;
    ArrayList<VotingData> votingData = new ArrayList<>();
    String className="";
    AddedVotesAdapter(Context context,ArrayList<VotingData> votingData){
        this.context = context;
        this.votingData=votingData;
    }
    AddedVotesAdapter(Context context,ArrayList<VotingData> votingData,String whichClass ){
        this.context = context;
        this.votingData=votingData;
        this.className=whichClass;
    }
    @NonNull
    @Override
    public AddedVotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.added_votes_recycler_view,parent,false);
//        RecyclerOptionAdapter.ViewHolder viewHolder = new RecyclerOptionAdapter.ViewHolder(v);
        AddedVotesAdapter.ViewHolder viewHolder = new AddedVotesAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AddedVotesAdapter.ViewHolder holder, int position) {

        holder.question.setText(votingData.get(holder.getAbsoluteAdapterPosition()).getQuestion());
        holder.instruction.setText(votingData.get(position).getInstruction());
        holder.option1.setText(votingData.get(position).getOption1());
        holder.option2.setText(votingData.get(position).getOption2());
        if(className.equals("HomeFragment")){
            holder.Img.setImageResource(R.drawable.elections);
            holder.instruction.setText("");
            holder.option1.setText("startdate"+votingData.get(position).getStrStartDate());
            holder.option2.setText("enddate"+votingData.get(position).getStrEndDate());
            holder.Img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   vote(position);
                }
            });
        }else if(className.equals("ElectedFragment")){
            holder.Img.setImageResource(R.drawable.candidates);
            holder.instruction.setText("");
            holder.option1.setText("you selected vote");
            holder.option2.setText(votingData.get(position).getSelectedOption());
           // Toast.makeText(context, "You already voted this", Toast.LENGTH_LONG).show();




        }else if(className.equals("ResultFragment")){
            holder.Img.setImageResource(R.drawable.success);
            holder.instruction.setText("");
            holder.option1.setText("winner");
            holder.option2.setText(votingData.get(position).winner);


        }else if(className.equals("AddedVotes")){
            holder.Img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String voteId = votingData.get(position).getVoteId();
                    FirebaseFirestore ref = FirebaseFirestore.getInstance();
                    ref.collection("votingData").document(voteId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            votingData.remove(position);
                            notifyItemRemoved(position);
                            Toast.makeText(v.getContext(), "successfully deleted to the firestore",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }





    }

    private void vote(int postion) {
       String userId = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore ref = FirebaseFirestore.getInstance();

        ref.collection("votingData")
                .document(votingData.get(postion).getVoteId())
                .collection("electedVoter").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                      if(task.isSuccessful()) {
                          DocumentSnapshot documentSnapshot = task.getResult();
                          if(documentSnapshot.exists()){
                              Toast.makeText(context,"You already voted this",Toast.LENGTH_LONG).show();
                          }else{
                              Intent intent = new Intent(context, VotingActivity.class);
                              // Retrieve specific elements from the ArrayList based on positions
                              String voteId = votingData.get(postion).getVoteId();// Add item at position 0
                              intent.putExtra("voteId", voteId);
                              context.startActivity(intent);
                          }
                      }
                    }
                });

    }


    @Override
    public int getItemCount() {
        return votingData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView question,instruction,option1,option2;
        ImageView Img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.added_votes_recycler_view_question_text_view);
            instruction = itemView.findViewById(R.id.added_votes_recycler_view_instruction_text_view);
            option1 = itemView.findViewById(R.id.added_votes_recycler_view_option1_text_view);
            option2 = itemView.findViewById(R.id.added_votes_recycler_view_option2_text_view);
            Img= itemView.findViewById(R.id.added_votes_recycler_view_image_view);


        }
    }
}
