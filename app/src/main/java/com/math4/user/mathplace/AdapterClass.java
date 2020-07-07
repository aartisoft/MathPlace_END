package com.math4.user.mathplace;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.math4.user.mathplace.Distrib.allInf;
import static com.math4.user.mathplace.SearchActivity.progressBar;

public class AdapterClass extends BaseAdapter {


    Context mContext;
    LayoutInflater inflater;
    private List<SearchActivity.SearchQuery> searchQueries = null;
    private ArrayList<SearchActivity.SearchQuery> arraylist;

    public AdapterClass(Context context, List<SearchActivity.SearchQuery> searchQueries) {
        mContext = context;
        this.searchQueries = searchQueries;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<SearchActivity.SearchQuery>();
        this.arraylist.addAll(searchQueries);
    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        return searchQueries.size();
    }

    @Override
    public SearchActivity.SearchQuery getItem(int position) {
        return searchQueries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            ViewGroup.LayoutParams layoutParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            view = inflater.inflate(R.layout.example_search, null);
            // Locate the TextViews in listview_item.xml
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBar.setVisibility(View.VISIBLE);
                    final Intent intent=new Intent(mContext, Topic.class);
                    intent.putExtra("thisTheme",holder.name.getText());
                    intent.putExtra("prevTheme",1);
                    FirebaseAuth mAuth=FirebaseAuth.getInstance();
                    FirebaseUser user=mAuth.getCurrentUser();
                    final FirebaseFirestore db=FirebaseFirestore.getInstance();
                    final DocumentReference docRefUser = db.collection("account").document(user.getUid());
                    docRefUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if (documentSnapshot.exists()) {
                                    Map<String, Object> inf = documentSnapshot.getData();
                                    Map<String, Object> m = new HashMap<>();
                                    if(inf.get(holder.name.getText())==null){
                                        final DocumentReference docRefNewTheme = db.collection("task2").document(holder.name.getText().toString());
                                        docRefNewTheme.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if(task.isSuccessful()){
                                                    DocumentSnapshot documentSnapshot2=task.getResult();
                                                    if(documentSnapshot2.exists()){
                                                        Map<String,Object> infTheme=documentSnapshot2.getData();
                                                        int themeTask=((Long)infTheme.get("items")).intValue();
                                                        ArrayList newThemeUser=new ArrayList();
                                                        for(int i3=0;i3<themeTask;i3++){
                                                            newThemeUser.add(1L);
                                                        }
                                                        HashMap<String, Object> m3 = new HashMap<>();
                                                        m3.put(holder.name.getText().toString(),newThemeUser);
                                                        m3.put(holder.name.getText()+"Solution",newThemeUser);
                                                        docRefUser.set(m3, SetOptions.merge());
                                                        allInf.put(holder.name.getText().toString(),newThemeUser);
                                                        allInf.put(holder.name.getText()+"Solution",newThemeUser);
                                                        Menu.context_this.startActivity(intent);
                                                    }
                                                }
                                            }
                                        });

                                    }else{
                                        Menu.context_this.startActivity(intent);
                                    }
                                    m.put("lastTheme",holder.name.getText());
                                    docRefUser.set(m, SetOptions.merge());
                                }
                            }
                        }
                    });
                    CurrentUser.setLastTheme(holder.name.getText().toString());
                    Menu.textViewStudy.setText(CurrentUser.lastTheme);
                }
            });
            holder.name = (TextView) view.findViewById(R.id.name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(searchQueries.get(position).getQuery());
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        searchQueries.clear();
        if (charText.length() == 0) {
            searchQueries.addAll(arraylist);
        } else {
            for (SearchActivity.SearchQuery wp : arraylist) {
                if (wp.getQuery().toLowerCase(Locale.getDefault()).contains(charText)) {
                    searchQueries.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}