package com.math4.user.mathplace;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.math4.user.mathplace.Distrib.allInf;

public class ShowOlymp extends Fragment {

    RecyclerView categoriesView;
    static Context context;
    int cnt;
    View view;
    static FirebaseUser user;
    static Boolean checkOlymp;
    static FirebaseFirestore db;
    static ArrayList<String> allOlympToken=new ArrayList<>();
    static ArrayList<String> allOlympName=new ArrayList<>();
    static ArrayList<String> allOlympAuthor=new ArrayList<>();
    static ArrayList<Integer> allOlympItems=new ArrayList<>();
    static ArrayList<String> allOlympTheme=new ArrayList<>();
    static List<Subcategory> getRandomData() {
        final ArrayList<Subcategory> arrayListReturn = new ArrayList<>();
        Log.e("checkcheckShowOlymp",String.valueOf(allOlympName.size())+ " "+String.valueOf(allOlympToken.size()));
        if(!checkOlymp) {
            arrayListReturn.add(new ShowOlymp.Subcategory("", "", "", 0, ""));
        }
        for(int i2=0;i2<allOlympToken.size();i2++){
            final int i=i2;
            String nameTheme=allOlympToken.get(i).toString();
//            subcategoryList.add(new MakeContest.Subcategory(nameTheme.toString()));
            arrayListReturn.add(new ShowOlymp.Subcategory(nameTheme,allOlympName.get(i).toString(),allOlympAuthor.get(i).toString(),allOlympItems.get(i),allOlympTheme.get(i)));
        }
        return arrayListReturn;
    }


    @Override
    // Переопределяем метод onCreateView
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_show_olymp, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        context = getActivity();
        db = FirebaseFirestore.getInstance();


        checkOlymp=false;
        final DocumentReference docRe = db.collection("account").document(user.getUid());
        docRe.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Map<String, Object> inf = documentSnapshot.getData();
                        if(inf.get("ad_material")==null){
                            checkOlymp=false;
                        }else{
                            checkOlymp=(Boolean) inf.get("ad_material");
                        }
                    }
                }
            }
        });


        db.collection("olympiad")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<QuerySnapshot> task2) {
                        if (task2.isSuccessful()) {
                            cnt = 0;
                            allOlympName.clear();
                            allOlympToken.clear();
                            ;
                            for (final QueryDocumentSnapshot documentTask : task2.getResult()) {
//                                Log.e("checkcheckArrayToken",allOlympToken.get(allOlympToken.size()-1).toString());
                                DocumentReference docRefContest = db.collection("olympiad").document(documentTask.getId().toString());
                                docRefContest.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task3) {
                                        if (task3.isSuccessful()) {
                                            DocumentSnapshot documentSnapshot = task3.getResult();
                                            if (documentSnapshot.exists()) {
                                                Map<String, Object> inf = documentSnapshot.getData();
                                                allOlympToken.add(documentTask.getId().toString());
                                                allOlympName.add(inf.get("name").toString());
                                                allOlympAuthor.add(inf.get("author").toString());
                                                allOlympItems.add(((Long)inf.get("items")).intValue());
                                                allOlympTheme.add(inf.get("theme").toString());
                                                Log.e("checkcheckArrayName", allOlympName.get(allOlympName.size() - 1).toString());
                                                cnt++;
                                                if (cnt == task2.getResult().size()) {
                                                    categoriesView = view.findViewById(R.id.categories_list_themeShowOlymp);
                                                    List<Subcategory> CircleCategories = ShowOlymp.getRandomData();
                                                    categoriesView.setHasFixedSize(true);
                                                    categoriesView.setLayoutManager(new LinearLayoutManager(
                                                            getActivity(),
                                                            RecyclerView.VERTICAL,
                                                            false
                                                    ));
                                                    categoriesView.setAdapter(new ShowOlymp.SubcategoriesAdapter(CircleCategories));
                                                }
                                            }
                                        }
                                    }
                                });
//                                if((documentTask.getData().get("theme").toString()).equals("графы")){  //и выбираем те где в параметрах написано что это алгебра
//                                    GrafNameTheme.add(documentTask.getId().toString());//добавляем их в наш массив с темами по алгебре. Массив лежит в Distrib. все подобные массивы обязательно хранить в начале Distrib
//                                }
                            }
                        }
                    }
                });
    }


        static class Subcategory {
        String token,name,author,theme;
        int all_taskTheme;

        Subcategory(String token, String name, String author, int all_taskTheme, String theme)
        {
            this.token=token;
            this.name = name;
            this.author=author;
            this.all_taskTheme=all_taskTheme;
            this.theme=theme;
        }
    }

    //----------------------------------------------------------------------------------------------

    static class SubcategoriesAdapter extends RecyclerView.Adapter<SubcategoriesAdapter.ViewHolder> {

        List<Subcategory> data;
        Boolean check;

        SubcategoriesAdapter(List<Subcategory> data) {
            this.data = data; this.check=checkOlymp;
        }


        @Override
        public ShowOlymp.SubcategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            if(viewType==0){
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.example_ad_material, parent, false);
            }else {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.subcategory_list_item_olympiad, parent, false);
            }
            return new ShowOlymp.SubcategoriesAdapter.ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(final ShowOlymp.SubcategoriesAdapter.ViewHolder holder, final int position) {
            final ShowOlymp.Subcategory subcategory = data.get(position);

            if(position==0&&!this.check){
                holder.imageViewTheme.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeItem(position);
                    }
                });

            }else {
                holder.subcategoryName.setText(subcategory.name);
                holder.subcategoryAuthor.setText(subcategory.author);
                if (subcategory.theme.equals("алгебра")) {
                    holder.imageViewTheme.setImageResource(R.drawable.algebra);
                } else if (subcategory.theme.equals("геометрия")) {
                    holder.imageViewTheme.setImageResource(R.drawable.geometry);
                } else if (subcategory.theme.equals("комбинаторика")) {
                    holder.imageViewTheme.setImageResource(R.drawable.komba);
                } else if (subcategory.theme.equals("графы")) {
                    holder.imageViewTheme.setImageResource(R.drawable.graph);
                } else if (subcategory.theme.equals("логика")) {
                    holder.imageViewTheme.setImageResource(R.drawable.logic);
                } else if (subcategory.theme.equals("идеи")) {
                    holder.imageViewTheme.setImageResource(R.drawable.idea);
                } else if (subcategory.theme.equals("огэ")) {
                    holder.imageViewTheme.setImageResource(R.drawable.examination);
                } else {
                    holder.imageViewTheme.setImageResource(R.drawable.school);
                }


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity.textLoad.setVisibility(View.VISIBLE);
                        final Intent intent = new Intent(Menu.context_this, OlympTopic.class);
                        intent.putExtra("thisTheme", subcategory.name);
                        intent.putExtra("tokenTheme", subcategory.token);
                        intent.putExtra("prevTheme", 2);
                        Something.itemViewPrev = holder.itemView;
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        FirebaseUser user = mAuth.getCurrentUser();
                        final DocumentReference docRefUser = db.collection("account").document(user.getUid());
                        docRefUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if (documentSnapshot.exists()) {
                                        Map<String, Object> inf = documentSnapshot.getData();
                                        Map<String, Object> m = new HashMap<>();
                                        if (inf.get(subcategory.token) == null) {
                                            final DocumentReference docRefNewTheme = db.collection("olympiad").document(subcategory.token);
                                            docRefNewTheme.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot documentSnapshot2 = task.getResult();
                                                        if (documentSnapshot2.exists()) {
                                                            Log.e("checkcheckDosument", String.valueOf(subcategory.token));
                                                            Map<String, Object> infTheme = documentSnapshot2.getData();
                                                            int themeTask = ((Long) infTheme.get("items")).intValue();
                                                            ArrayList newThemeUser = new ArrayList();
                                                            for (int i3 = 0; i3 < themeTask; i3++) {
                                                                newThemeUser.add(1L);
                                                            }
                                                            HashMap<String, Object> m3 = new HashMap<>();
                                                            m3.put(subcategory.token, newThemeUser);
                                                            m3.put(subcategory.token + "Solution", newThemeUser);
                                                            docRefUser.set(m3, SetOptions.merge());
                                                            allInf.put(subcategory.token, newThemeUser);
                                                            allInf.put(subcategory.token + "Solution", newThemeUser);
                                                            Menu.context_this.startActivity(intent);
                                                        }
                                                    }
                                                }
                                            });

                                        } else if (((ArrayList) inf.get(subcategory.token)).size() != subcategory.all_taskTheme) {
                                            ArrayList newThemeUser = ((ArrayList) inf.get(subcategory.token));
                                            for (int i3 = newThemeUser.size(); i3 < subcategory.all_taskTheme; i3++) {
                                                newThemeUser.add(1L);
                                            }
                                            HashMap<String, Object> m3 = new HashMap<>();
                                            m3.put(subcategory.token, newThemeUser);
                                            m3.put(subcategory.token + "Solution", newThemeUser);
                                            docRefUser.set(m3, SetOptions.merge());
                                            allInf.put(subcategory.token, newThemeUser);
                                            allInf.put(subcategory.token + "Solution", newThemeUser);
                                            Menu.context_this.startActivity(intent);
                                        } else {
                                            Menu.context_this.startActivity(intent);
                                        }
                                        m.put("lastTheme", subcategory.name);
                                        docRefUser.set(m, SetOptions.merge());
                                    }
                                }
                            }
                        });
                        CurrentUser.setLastTheme(subcategory.name);
                        Menu.textViewStudy.setText(CurrentUser.lastTheme);
                    }
                });
            }
        }

        public void removeItem(int position) {
            data.remove(position);
            this.check = true;
            notifyItemRemoved(position);
//            notifyDataSetChanged();
            notifyItemRangeChanged(position, getItemCount());

            final DocumentReference docRefUser = db.collection("account").document(user.getUid());
            docRefUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()) {
                            Map<String, Object> inf = documentSnapshot.getData();
                            inf.put("ad_material",true);
                            docRefUser.set(inf,SetOptions.merge());
                        }
                    }
                }
            });
        }

        @Override
        public int getItemViewType(final int position) {
            int res;
            if(position==0&&!check){
                res=0;
            }else{
                res=1;
            }
            return res;
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView subcategoryName,subcategoryAuthor,subcategoryLike;
            ImageView imageViewTheme;

            ViewHolder(View itemView) {
                super(itemView);

                imageViewTheme=itemView.findViewById(R.id.imageViewTheme);
                subcategoryAuthor= itemView.findViewById(R.id.textViewAuthorShowOlymp);
//                checkBox=itemView.findViewById(R.id.checkBox);
//                linearLayout=itemView.findViewById(R.id.linearLayoutMakeContest);
                subcategoryName = itemView.findViewById(R.id.textViewNameShowOlymp);
            }
        }
    }

}
