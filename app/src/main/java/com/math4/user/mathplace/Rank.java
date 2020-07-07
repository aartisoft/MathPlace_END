package com.math4.user.mathplace;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;


public class Rank extends Fragment {
    View view;
    DocumentReference docRef1;
    SubcategoriesAdapter mAdapter;
    static int cnt2=0;
    static TextView positionUser,rightUser;
    static FirebaseUser userThis;
    FirebaseAuth mAuth;
    static ArrayList<ArrayList<String>> arrayListsUser=new ArrayList<>();
    static RecyclerView categoriesView;
    static FirebaseFirestore db;
    static ArrayList<Subcategory> subcategoryGeometry = new ArrayList<>();
    static List<Subcategory> getRandomData() {
        final Random random = new Random();
        int cnt=0;
        int prev=-1;
//        ArrayList<Rank.Category> arrayList = new ArrayList<>();
        for(int i=0;i<arrayListsUser.size();i++){
            if(prev!=Integer.parseInt(arrayListsUser.get(i).get(0))){
                cnt++;
                prev=Integer.parseInt(arrayListsUser.get(i).get(0));
            }
            String color;
            int progress=Integer.parseInt(arrayListsUser.get(i).get(0));
            if(progress<5){
                color="#A8961F";
            }else if(progress<50){
                color="#173E77";
            }else{
                color="#751D28";
            }
            if(arrayListsUser.get(i).get(2).equals(String.valueOf(userThis.getUid()))){
                if(i==0){
                    positionUser.setText("I");
                }
                else if(i == 1){
                    positionUser.setText("II");
                }
                else if (i == 2){
                    positionUser.setText("III");
                }
                else
                    positionUser.setText(String.valueOf(i+1));
                rightUser.setText("Вы решили "+String.valueOf(arrayListsUser.get(i).get(0))+" задач");
                subcategoryGeometry.add(new Rank.Subcategory("Вы",Integer.parseInt(arrayListsUser.get(i).get(0)),cnt,color, arrayListsUser.get(i).get(2)));
            }else{
                subcategoryGeometry.add(new Rank.Subcategory(arrayListsUser.get(i).get(1),Integer.parseInt(arrayListsUser.get(i).get(0)),cnt,color, arrayListsUser.get(i).get(2)));
            }
        }
        return subcategoryGeometry;
    }
    Comparator<List<String>> comparator = new Comparator<List<String>>() {
        public int compare(List<String> left, List<String> right) {
            return (Integer.compare(Integer.parseInt(right.get(0)),Integer.parseInt(left.get(0))));
        }
    };
    static ProgressBar progressBar;
    int cnt=0;
    String progressUser="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_rank, container, false);
//        textLoad.setVisibility(View.VISIBLE);
        db=FirebaseFirestore.getInstance();
        final RecyclerView recyclerView=view.findViewById(R.id.categories_listRank);
        progressUser="";
        mAuth= FirebaseAuth.getInstance();
        userThis=mAuth.getCurrentUser();
        final View view_user=view.findViewById(R.id.example_this_user);
        positionUser=(TextView)view_user.findViewById(R.id.textViewPositionRankUser);
        rightUser=(TextView)view_user.findViewById(R.id.textViewTextRankUser);
//        positionUser.setText("1");
        ((TextView)view_user.findViewById(R.id.textViewNameRankUser)).setText("Вы");
        final TextView rankYou=view_user.findViewById(R.id.textViewTextRankUser);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        arrayListsUser.clear();
        subcategoryGeometry.clear();
        db.collection("account")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                        final int len = task2.getResult().size();
                        cnt2=0;

                        for (final QueryDocumentSnapshot documentTask : task2.getResult()) {
//                                ArrayList<Long> thisTask=(ArrayList<Long>)m.get(documentTask.getId());
                            docRef1 = db.collection("account").document(documentTask.getId());
                            docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot documentSnapshot = task.getResult();
                                        if (documentSnapshot.exists()) {
//                                                Map<String,Object>inf=documentSnapshot.getData();
                                            ArrayList<String> user = new ArrayList<>();
                                            user.add(String.valueOf(documentSnapshot.getData().get("right")));
                                            user.add(documentSnapshot.getData().get("name").toString());
                                            user.add(String.valueOf(documentTask.getId()));
                                            arrayListsUser.add(user);
                                            Log.e("checkcheckRank", "GOOD " + String.valueOf(arrayListsUser.size()));
                                            if (len-1 == cnt2) {
                                                Collections.sort(arrayListsUser, comparator);
                                                List<Subcategory> CircleCategories = Rank.getRandomData();
                                                categoriesView = Rank.this.view.findViewById(R.id.categories_listRank);
                                                categoriesView.setHasFixedSize(true);
                                                categoriesView.setLayoutManager(new LinearLayoutManager(
                                                        getActivity(),
                                                        RecyclerView.VERTICAL,
                                                        false
                                                ));
                                                Log.e("checkcheckRankRank", String.valueOf(arrayListsUser.size()));
                                                mAdapter = new Rank.SubcategoriesAdapter(CircleCategories);
                                                categoriesView.setAdapter(mAdapter);
                                                recyclerView.setBackgroundColor(Color.parseColor("#9E9999"));
                                            }
                                            cnt2++;
                                        }
                                    }
                                }
                            });
                        }
                    }
                });
        return this.view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        textLoad.setVisibility(View.INVISIBLE);
    }

    static class Subcategory {
        String name;
        int position;
        int progress,task;
        String color;
        String id;

        Subcategory(String name,int progress,int position,String color, String id) {
            this.name = name;this.progress=progress;this.position=position;this.color=color;
            this.id = id;
        }
    }


    //----------------------------------------------------------------------------------------------

    final static class SubcategoriesAdapter extends RecyclerView.Adapter<SubcategoriesAdapter.ViewHolder> {

        List<Subcategory> data;

        SubcategoriesAdapter(List<Subcategory> data) {
            this.data = data;
        }


        @Override
        public Rank.SubcategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.subcategory_list_item_rank, parent, false);
            return new Rank.SubcategoriesAdapter.ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(final Rank.SubcategoriesAdapter.ViewHolder holder, final int position) {
            final Rank.Subcategory subcategory = data.get(position);
            final DocumentReference docRes3 = db.collection("account").document(subcategory.name);
            Log.e("checkcheckClass","getObject");
            holder.subcategoryText.setText("Решено задач "+String.valueOf(subcategory.progress));
            holder.subcategoryName.setText(subcategory.name);
            if(subcategory.position == 1){
                holder.subcategoryPositioon.setText("I");
            }
            else if(subcategory.position == 2){
                holder.subcategoryPositioon.setText("II");
            }
            else if(subcategory.position == 3){
                holder.subcategoryPositioon.setText("III");
            }
            else
                holder.subcategoryPositioon.setText(String.valueOf(subcategory.position));
            holder.subcategoryName.setTextColor(Color.parseColor(subcategory.color));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent( categoriesView.getContext() , MessageActivity.class);
                    intent.putExtra("userid", subcategory.id );
                    categoriesView.getContext().startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView subcategoryName,subcategoryText,subcategoryPositioon;
            ImageView subcategoryImage;

            ViewHolder(View itemView) {
                super(itemView);
                subcategoryName = itemView.findViewById(R.id.textViewNameRank);
                subcategoryText = itemView.findViewById(R.id.textViewTextRank);
//                subcategoryImage = itemView.findViewById(R.id.imageViewRank);
                subcategoryPositioon=itemView.findViewById(R.id.textViewPositionRank);
            }
        }
    }
//    public void setRank(){
//        ListView listView=view.findViewById(R.id.listViewRank);
//        Log.e("checkcheckCount",String.valueOf(rankUser.size()));
//        for(int i=0;i<rankUser.size();i++){
//            rankUser.set(i,String.valueOf(i+1)+") "+rankUser.get(i));
//        }
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
//                R.layout.list_item, rankUser);
//        listView.setAdapter(adapter);
//    }
}
