package com.math4.user.mathplace;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.math4.user.mathplace.Distrib.allInf;

public class ThemeActivity extends AppCompatActivity {
    String theme;
    static FirebaseAuth mAuth;
    static int all_task=0;
    static FirebaseFirestore db;
    static FirebaseUser user;
    static String nameTheme="";
    static ArrayList<String> arrayList;
    RecyclerView categoriesView;
    static List<Category> getRandomData() {
        final Random random = new Random();
        final ArrayList<Category> arrayListReturn = new ArrayList<>();
        final ArrayList<Subcategory> subcategoryList = new ArrayList<>();

        for(int i2=0;i2<arrayList.size();i2++){
            final int i=i2;
            nameTheme=arrayList.get(i).toString();
            Log.e("checkcheckFinishTheme",arrayList.get(i).toString());
            subcategoryList.add(new Subcategory(nameTheme.toString()));
        }
        arrayListReturn.add(new Category("Геометрия",subcategoryList));
        return arrayListReturn;
    }

    // Переопределяем метод onCreateView
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_menu);
        mAuth= FirebaseAuth.getInstance();
        Toolbar toolbar = findViewById(R.id.toolbarTheme);
        setSupportActionBar(toolbar);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        arrayList=new ArrayList<>();
        user=mAuth.getCurrentUser();
        final Bundle arguments = getIntent().getExtras();
        this.theme=arguments.getString("theme_name");
        categoriesView = findViewById(R.id.categories_list_theme);
        db=FirebaseFirestore.getInstance();
        String title = theme;
        if(theme.equals("огэ")){
            title = theme.toUpperCase();
        }
        else {
            title = theme.substring(0,1).toUpperCase() + theme.substring(1);
        }
        setTitle(title);

//        arrayList.clear();

        if(theme.equals("алгебра")){
            arrayList= Distrib.algebraNameTheme;
            Log.e("checkcheckThemeMenu",String.valueOf(arrayList.size()));
        }else if(theme.equals("геометрия")){
            arrayList= Distrib.geometryNameTheme;
//            }
        }else if(theme.equals("комбинаторика")){
            arrayList= Distrib.kombaNameTheme;
        }else if(theme.equals("логика")){
            arrayList= Distrib.logikaNameTheme;
        }else if(theme.equals("огэ")){
            arrayList= Distrib.ogeNameTheme;
        }else if(theme.equals("графы")){
            arrayList= Distrib.graphNameTheme;
        }else if(theme.equals("идеи")){
            arrayList= Distrib.ideaNameTheme;
        }else if(theme.equals("школа")){
            arrayList= Distrib.schoolNameTheme;
        }else{
            arrayList= Distrib.algebraNameTheme;
        }
        List<Category> CircleCategories = ThemeActivity.getRandomData();
        categoriesView.setHasFixedSize(true);
        Menu.context_this=this;
        categoriesView.setLayoutManager(new LinearLayoutManager(
                this,
                RecyclerView.VERTICAL,
                false
        ));
        categoriesView.setAdapter(new CategoriesAdapter(CircleCategories));
        if(arrayList==null||arrayList.size()==0){
            Intent intent=new Intent(this, Zactavka.class);
            startActivity(intent);
//            showAlertDialog();
        }
    }

    @Override
    public void onResume() {
        super.onResume();


    }
        //----------------------------------------------------------------------------------------------

    static class Category {
//        String name;
        List<Subcategory> subcategories;

        Category(String name, List<Subcategory> subcategories) {
//            this.name = name;
            this.subcategories = subcategories;
        }
    }

    static class Subcategory {
        String name;
        int all_taskTheme;
        int progress,task;

        Subcategory(String name)
        {
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
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.category_list_item_menu, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Category category = data.get(position);
            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    holder.subcategoriesList.getContext(),
                    RecyclerView.VERTICAL,
                    false
            );

            holder.subcategoriesList.setLayoutManager(layoutManager);
            holder.subcategoriesList.setAdapter(new SubcategoriesAdapter(category.subcategories));
            holder.subcategoriesList.setRecycledViewPool(viewPool);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            RecyclerView subcategoriesList;
//            TextView categoryName;
            ViewHolder(View itemView) {
                super(itemView);

//                categoryName=itemView.findViewById(R.id.subcategory_name_circle);
                subcategoriesList = itemView.findViewById(R.id.subcategories_list_menu);
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
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.subcategory_list_item_menu, parent, false);
            return new ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final Subcategory subcategory = data.get(position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.textLoad.setVisibility(View.VISIBLE);
                    final Intent intent=new Intent(Menu.context_this, Topic.class);
                    intent.putExtra("thisTheme",subcategory.name);
                    intent.putExtra("prevTheme",2);
                    Something.itemViewPrev=holder.itemView;
                    FirebaseAuth mAuth=FirebaseAuth.getInstance();
                    FirebaseUser user=mAuth.getCurrentUser();
                    final DocumentReference docRefUser = db.collection("account").document(user.getUid());
                    docRefUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
                                                        docRefUser.set(m3,SetOptions.merge());
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
                                        docRefUser.set(m3,SetOptions.merge());
                                        allInf.put(subcategory.name,newThemeUser);
                                        allInf.put(subcategory.name+"Solution",newThemeUser);
                                        Menu.context_this.startActivity(intent);
                                    }else{
                                        Menu.context_this.startActivity(intent);
                                    }
                                    m.put("lastTheme",subcategory.name);
                                    docRefUser.set(m, SetOptions.merge());
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
                            subcategory.all_taskTheme=((Long)inf.get("cnt_task")).intValue();
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
                subcategoryLike=itemView.findViewById(R.id.textViewLikeTheme);
                subcategoryName = itemView.findViewById(R.id.textViewName);
                subcategoryText = itemView.findViewById(R.id.textViewText);
                subcategoryProgressBar = itemView.findViewById(R.id.progressBarTheme);
            }
        }
    }
    public void showAlertDialog(){
        new AlertDialog.Builder(this, R.style.AlertDialogTheme)
                .setTitle("Произошла ошибка")
                .setMessage("Обновите пожалуйста приложение")

                .setPositiveButton("Обновить", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(ThemeActivity.this, Zactavka.class);
                        startActivity(intent);
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setCancelable(false)
                .show();

    }
    public void startA(Intent intent){
        startActivity(intent);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
