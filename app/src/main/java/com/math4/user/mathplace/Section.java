package com.math4.user.mathplace;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

public class Section extends AppCompatActivity {

    static String NumberClass;
    static RecyclerView categoriesView;
    static ArrayList<String> arrayListClass=new ArrayList<>();
    static FirebaseUser user;
    static FirebaseAuth mAuth;
    static FirebaseFirestore db;
    static int all_task=0;

    static List<Category> getRandomData() {
        final ArrayList<Category> arrayListReturn = new ArrayList<>();
        final ArrayList<Subcategory> subcategoryList = new ArrayList<>();
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        for(int i=0;i<arrayListClass.size();i++){
            subcategoryList.add(new Section.Subcategory(arrayListClass.get(i)));
        }
        arrayListReturn.add(new Section.Category(subcategoryList));
        return arrayListReturn;
    }
    static ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);
        Toolbar toolbar = findViewById(R.id.toolbar_section);
        setSupportActionBar(toolbar);
        ActionBar actionBar =getSupportActionBar();
//        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("MathPlace");
        mAuth=FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        Bundle bundle=getIntent().getExtras();
        progressBar=findViewById(R.id.textViewLoadSection);
        progressBar.setVisibility(View.INVISIBLE);
        NumberClass=bundle.getString("class");
        if(NumberClass.equals("seven")){
            setTitle("Уроки для 7 класса");
        }else if(NumberClass.equals("eight")){
            setTitle("Уроки для 8 класса");
        }else if(NumberClass.equals("nine")){
            setTitle("Уроки для 9 класса");
        }else{
            setTitle("Дополнительные уроки");
        }
        Log.e("checkcheckSectionStart","Good");
        db=FirebaseFirestore.getInstance();
        arrayListClass.clear();
        db.collection("task2")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                        if (task2.isSuccessful()) {
                            int cnt=0;
                            for (QueryDocumentSnapshot documentTask : task2.getResult()) {//здесь мы бежим по всем темам
//                                    Log.e("checkcheckDocumentTask",documentTask.getId());
                                if((documentTask.getData().get("class").toString()).equals(NumberClass)){  //и выбираем те где в параметрах написано что это алгебра
                                    arrayListClass.add(documentTask.getId().toString());

                                }
                                cnt++;
                                if(cnt==task2.getResult().size()){
                                    List<Category> categories = getRandomData();
                                    categoriesView = findViewById(R.id.categories_section);
                                    categoriesView.setHasFixedSize(true);
                                    categoriesView.setLayoutManager(new LinearLayoutManager(
                                                                            getApplicationContext(),
                                                                            RecyclerView.VERTICAL,
                                                                            false
                                                                    )
                                                                    {
                                                                        @Override
                                                                        public boolean canScrollVertically() {
                                                                            return false;
                                                                        }
                                                                    }
                                    );
//        categoriesView.setNestedScrollingEnabled(false);
                                    categoriesView.setAdapter(new Section.CategoriesAdapter(categories));

                                }
                            }
                        }
                    }
                });
        if(categoriesView!=null){
            Log.e("checkcheckSectionFinish","GOOD");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        progressBar.setVisibility(View.INVISIBLE);
//        textLoad2.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void share(MenuItem item){
        final Intent intent = new Intent(Intent.ACTION_SEND);
        Intent intent1= Something.share(intent);
        try {
            startActivity(Intent.createChooser(intent1,"Поделиться с помощью:" ));
        }
        catch (android.content.ActivityNotFoundException e)
        {
            Toast.makeText(getApplicationContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show();
        }
    }


    static class Category {
        List<Subcategory> subcategories;

        Category(List<Subcategory> subcategories) {
            this.subcategories = subcategories;
        }
    }

    static class Subcategory {
        String name;
        int all_taskTheme;

        Subcategory(String name) {
            this.name = name;
        }
    }

    //----------------------------------------------------------------------------------------------

    static class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        List<Category> data;

        CategoriesAdapter(List<Category> data) {
            this.data = data;
        }


        @Override
        public Section.CategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.category_list_item_section, parent, false);
            return new Section.CategoriesAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(Section.CategoriesAdapter.ViewHolder holder, int position) {
            final Section.Category category = data.get(position);
            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    holder.subcategoriesList.getContext(),
                    RecyclerView.VERTICAL,
                    false
            );
//            holder.categoryName.setText(category.name);
            holder.subcategoriesList.setLayoutManager(layoutManager);
            holder.subcategoriesList.setAdapter(new Section.SubcategoriesAdapter(category.subcategories));
            holder.subcategoriesList.setRecycledViewPool(viewPool);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            RecyclerView subcategoriesList;
            TextView subcategoryTheme;
//            TextView categoryName;
            ViewHolder(View itemView) {
                super(itemView);
//                subcategoryTheme= itemView.findViewById(R.id.checkTheme);
//                categoryName=itemView.findViewById(R.id.category_name);
                subcategoriesList = itemView.findViewById(R.id.subcategories_list_section);
            }
        }
    }

    //----------------------------------------------------------------------------------------------

    static class SubcategoriesAdapter extends RecyclerView.Adapter<SubcategoriesAdapter.ViewHolder> {

        List<Subcategory> data;

        SubcategoriesAdapter(List<Subcategory> data) {
            this.data = data;
        }


        @Override
        public Section.SubcategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.subcategory_list_item_section, parent, false);
            return new Section.SubcategoriesAdapter.ViewHolder(view);
        }
        ArrayList task_1;
        @Override
        public void onBindViewHolder(final Section.SubcategoriesAdapter.ViewHolder holder, final int position) {
            final Section.Subcategory subcategory = data.get(position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.textLoad.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    final Intent intent=new Intent(Menu.context_this, Topic.class);
                    intent.putExtra("thisTheme",subcategory.name);
                    intent.putExtra("prevTheme",2);
                    Something.itemViewPrev=holder.itemView;
                    final DocumentReference docRef = db.collection("account").document(user.getUid());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if (documentSnapshot.exists()) {
                                    Map<String, Object> inf = documentSnapshot.getData();
                                    Map<String, Object> m = new HashMap<>();
                                    if(inf.get(subcategory.name)==null){
                                        final DocumentReference docRefNewTheme = db.collection("task2").document(subcategory.name);
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
                                                        m3.put(subcategory.name,newThemeUser);
                                                        m3.put(subcategory.name+"Solution",newThemeUser);
                                                        docRef.set(m3,SetOptions.merge());
                                                        allInf.put(subcategory.name,newThemeUser);
                                                        allInf.put(subcategory.name+"Solution",newThemeUser);
                                                        Menu.context_this.startActivity(intent);
                                                    }
                                                }
                                            }
                                        });

                                    }else if(((ArrayList)inf.get(subcategory.name)).size()!=subcategory.all_taskTheme){
                                        ArrayList newThemeUser=((ArrayList)inf.get(subcategory.name));
                                        for(int i3=newThemeUser.size();i3<subcategory.all_taskTheme;i3++){
                                            newThemeUser.add(1L);
                                        }
                                        HashMap<String, Object> m3 = new HashMap<>();
                                        m3.put(subcategory.name,newThemeUser);
                                        m3.put(subcategory.name+"Solution",newThemeUser);
                                        docRef.set(m3,SetOptions.merge());
                                        allInf.put(subcategory.name,newThemeUser);
                                        allInf.put(subcategory.name+"Solution",newThemeUser);
                                        Menu.context_this.startActivity(intent);
                                    }else{
                                        Menu.context_this.startActivity(intent);
                                    }
                                    m.put("lastTheme",subcategory.name);
                                    docRef.set(m, SetOptions.merge());
                                }
                            }
                        }
                    });
                    CurrentUser.setLastTheme(subcategory.name);
                    Menu.textViewStudy.setText(CurrentUser.lastTheme);
                }
            });
            final DocumentReference docRes3 = db.collection("task2").document(subcategory.name);
            Log.e("checkcheckClass","getObject");
            docRes3.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    Log.e("checkcheckClass","getObjectFirst");
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        Log.e("checkcheckClass","getObjectSecond");
                        if (documentSnapshot.exists()) {
                            Log.e("checkcheckClass","данные получены "+subcategory.name);
                            Map<String, Object> inf = documentSnapshot.getData();
                            all_task=((Long)inf.get("cnt_task")).intValue();
                            ArrayList<Long> taskProgress=(ArrayList<Long>) allInf.get(subcategory.name);
                            int right=0;
                            if(taskProgress!=null){
                                for(int i=0;i<taskProgress.size();i++){
                                    if(((Long)taskProgress.get(i)).intValue()==2){
                                        right++;
                                    }
                                }
                            }
                            subcategory.all_taskTheme=((Long)inf.get("items")).intValue();
                            holder.subcategoryText.setText("Всего задач "+String.valueOf(all_task));
                            if(all_task!=0) {
                                holder.subcategoryProgressBar.setProgress(right * 100 / all_task);
                            }
                        }
                    }
                }
            });
            final DocumentReference documentReference=db.collection("task2").document(subcategory.name);
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    Log.e("checkhcheckStartLoad","START");
                    if(task.isSuccessful()){
                        Log.e("checkhcheckStartLoad","EEEEEEE");
                        DocumentSnapshot documentSnapshot=task.getResult();
                        if(documentSnapshot.exists()){
                            Log.e("checkhcheckStartLoad","WHAT");
                            Map<String,Object> inf2=documentSnapshot.getData();
                            holder.subcategoryLike.setText(String.valueOf(inf2.get("like")));
                        }
                    }
                }
            });
            holder.subcategoryName.setText(subcategory.name);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView subcategoryName,subcategoryText,subcategoryLike;
            ProgressBar subcategoryProgressBar;


            ViewHolder(View itemView) {
                super(itemView);
                subcategoryLike=itemView.findViewById(R.id.textViewLikeThemeSection);
                subcategoryName = itemView.findViewById(R.id.textViewNameSection);
                subcategoryText = itemView.findViewById(R.id.textViewTextSection);
                subcategoryProgressBar = itemView.findViewById(R.id.progressBarTheme);
            }
        }
    }
}
