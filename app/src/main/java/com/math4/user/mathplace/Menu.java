package com.math4.user.mathplace;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

//import com.math4.user.mathplace.Distrib

import de.hdodenhof.circleimageview.CircleImageView;

import static com.math4.user.mathplace.Distrib.allInf;
import static com.math4.user.mathplace.Distrib.itemsCtnTheme;


public class Menu extends Fragment {
    EditText search;
    static int likeMain1,likeMain2;
    static View view;
    static double cntTrue1=0,cntTrue2=0,cntAll1=1,cntAll2=1;
    static String themeTask;
    static ArrayList<ArrayList<String>> arrayListsTheme=new ArrayList<>();
    static Map<String,Integer> LikeInf=new HashMap<String, Integer>();
    static Map<String,Integer> ItemsInf=new HashMap<String, Integer>();
    static TextView textViewStudy;
    static FirebaseFirestore db;
    static Context context_this;
    public class CustomGridLayoutManager extends LinearLayoutManager {
        private boolean isScrollEnabled = true;

        public CustomGridLayoutManager(Context context) {
            super(context);
        }

        public void setScrollEnabled(boolean flag) {
            this.isScrollEnabled = flag;
        }

        @Override
        public boolean canScrollVertically() {
            //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
            return isScrollEnabled && super.canScrollVertically();
        }
    }
    static List<Category> getRandomData() {
        final Random random = new Random();
        final ArrayList<Category> arrayList = new ArrayList<>();
        final ArrayList<Subcategory> subcategoryPopular = new ArrayList<>();
        final ArrayList<Subcategory> subcategoryGeometry = new ArrayList<>();
        final ArrayList<Subcategory> subcategoryAlgebra = new ArrayList<>();
        final ArrayList<Subcategory> subcategoryOGE = new ArrayList<>();
        final ArrayList<Subcategory> subcategoryKomba = new ArrayList<>();
        final ArrayList<Subcategory> subcategorySchool = new ArrayList<>();
        for(int i=0;i<20;i+=2){
            double cntTrue1=0,cntTrue2=0,cntAll2=1,cntAll1=1;
            if(Distrib.allInf !=null){
                Log.e("checkcheck","Menu он не пустой");
                ArrayList<Long> inf1=(ArrayList<Long>) Distrib.allInf.get(arrayListsTheme.get(i).get(1));
                if(inf1!=null) {
                    cntAll1= itemsCtnTheme.get(arrayListsTheme.get(i).get(1));
                    for (int j = 0; j < inf1.size(); j++) {
                        if (inf1.get(j) == Long.valueOf(2)) {
                            cntTrue1++;
                        }
                    }
                }
                ArrayList<Long> inf2=(ArrayList<Long>) Distrib.allInf.get(arrayListsTheme.get(i+1).get(1));
                if(inf2!=null) {
                    cntAll2 = itemsCtnTheme.get(arrayListsTheme.get(i+1).get(1));
                    for (int j = 0; j < inf2.size(); j++) {
                        if (inf2.get(j) == Long.valueOf(2)) {
                            cntTrue2++;
                        }
                    }
                }
                int image1,image2;

                if(Distrib.algebraNameTheme.contains(arrayListsTheme.get(i).get(1).toString())) {
                    image1= R.drawable.algebra;
                }else if(Distrib.geometryNameTheme.contains(arrayListsTheme.get(i).get(1).toString())) {
                    image1= R.drawable.geometry;
                }else if(Distrib.kombaNameTheme.contains(arrayListsTheme.get(i).get(1).toString())) {
                    image1= R.drawable.komba;
                }else if(Distrib.graphNameTheme.contains(arrayListsTheme.get(i).get(1).toString())) {
                    image1= R.drawable.algebra;
                }else if(Distrib.ideaNameTheme.contains(arrayListsTheme.get(i).get(1).toString())) {
                    image1= R.drawable.idea;
                }else if(Distrib.logikaNameTheme.contains(arrayListsTheme.get(i).get(1).toString())) {
                    image1= R.drawable.logic;
                }else if(Distrib.ogeNameTheme.contains(arrayListsTheme.get(i).get(1).toString())) {
                    image1= R.drawable.examination;
                } else {
                    image1= R.drawable.school;
                }
                if(Distrib.algebraNameTheme.contains(arrayListsTheme.get(i+1).get(1).toString())) {
                    image2= R.drawable.algebra;
                }else if(Distrib.geometryNameTheme.contains(arrayListsTheme.get(i+1).get(1).toString())) {
                    image2= R.drawable.geometry;
                }else if(Distrib.kombaNameTheme.contains(arrayListsTheme.get(i+1).get(1).toString())) {
                    image2= R.drawable.komba;
                }else if(Distrib.graphNameTheme.contains(arrayListsTheme.get(i+1).get(1).toString())) {
                    image2= R.drawable.algebra;
                }else if(Distrib.ideaNameTheme.contains(arrayListsTheme.get(i+1).get(1).toString())) {
                    image2= R.drawable.idea;
                }else if(Distrib.logikaNameTheme.contains(arrayListsTheme.get(i+1).get(1).toString())) {
                    image2= R.drawable.logic;
                }else if(Distrib.ogeNameTheme.contains(arrayListsTheme.get(i+1).get(1).toString())) {
                    image2= R.drawable.examination;
                } else {
                    image2= R.drawable.school;
                }
//                Log.e("checkcheckLikeInf",LikeInf.get(popularTheme[i]).toString());
                likeMain1=Integer.parseInt(arrayListsTheme.get(i).get(0));
                likeMain2=Integer.parseInt(arrayListsTheme.get(i+1).get(0));
                subcategoryPopular.add(new Subcategory(arrayListsTheme.get(i).get(1).toString(), (int) (((double) cntTrue1 / cntAll1) * 100), image1,(int)cntTrue1,arrayListsTheme.get(i+1).get(1).toString(), (int) (((double) cntTrue2 / cntAll2) * 100), image2,(int)cntTrue2,likeMain1,likeMain2));
            }else{
                Log.e("checkcheck","Menu он пустой");
            }
        }

        for(int i = 0; i<(Distrib.geometryNameTheme.size())/2*2; i+=2){
            double cntTrue1=0,cntTrue2=0,cntAll1=1,cntAll2=1;
            if(Distrib.allInf !=null){
                Log.e("checkcheck","Menu он не пустой");
                ArrayList<Long> inf1=(ArrayList<Long>) Distrib.allInf.get(Distrib.geometryNameTheme.get(i));
                if(inf1!=null) {
                    cntAll1 = itemsCtnTheme.get(Distrib.geometryNameTheme.get(i));
                    for (int j = 0; j < inf1.size(); j++) {
                        if (inf1.get(j) == Long.valueOf(2)) {
                            cntTrue1++;
                        }
                    }
                }
                ArrayList<Long> inf2=(ArrayList<Long>) Distrib.allInf.get(Distrib.geometryNameTheme.get(i+1));
                if(inf2!=null) {
                    cntAll2 = itemsCtnTheme.get(Distrib.geometryNameTheme.get(i+1));
                    for (int j = 0; j < inf2.size(); j++) {
                        if (inf2.get(j) == Long.valueOf(2)) {
                            cntTrue2++;
                        }
                    }
                }
                likeMain1=LikeInf.get(Distrib.geometryNameTheme.get(i));
                likeMain2=LikeInf.get(Distrib.geometryNameTheme.get(i+1));
                subcategoryGeometry.add(new Subcategory(Distrib.geometryNameTheme.get(i),(int)(((double)cntTrue1/cntAll1)*100), R.drawable.geometry,(int)cntTrue1,Distrib.geometryNameTheme.get(i+1),(int)(((double)cntTrue2/cntAll2)*100), R.drawable.geometry,(int)cntTrue2,likeMain1,likeMain2));
            }
        }
        for(int i = 0; i<(Distrib.algebraNameTheme.size())/2*2; i+=2){
            double cntTrue1=0,cntTrue2=0,cntAll1=1,cntAll2=1;
            if(Distrib.allInf !=null){
                Log.e("checkcheck","Menu он не пустой");
                ArrayList<Long> inf1=(ArrayList<Long>) Distrib.allInf.get(Distrib.algebraNameTheme.get(i));
                if(inf1!=null) {
                    cntAll1 = itemsCtnTheme.get(Distrib.algebraNameTheme.get(i));
                    for (int j = 0; j < inf1.size(); j++) {
                        if (inf1.get(j) == Long.valueOf(2)) {
                            cntTrue1++;
                        }
                    }
                }
                ArrayList<Long> inf2=(ArrayList<Long>) Distrib.allInf.get(Distrib.algebraNameTheme.get(i+1));
                if(inf2!=null) {
                    cntAll2 = itemsCtnTheme.get(Distrib.algebraNameTheme.get(i+1));
                    for (int j = 0; j < inf2.size(); j++) {
                        if (inf2.get(j) == Long.valueOf(2)) {
                            cntTrue2++;
                        }
                    }
                }
                likeMain1=LikeInf.get(Distrib.algebraNameTheme.get(i));
                likeMain2=LikeInf.get(Distrib.algebraNameTheme.get(i+1));
                subcategoryAlgebra.add(new Subcategory(Distrib.algebraNameTheme.get(i),(int)(((double)cntTrue1/cntAll1)*100), R.drawable.algebra,(int)cntTrue1,Distrib.algebraNameTheme.get(i+1),(int)(((double)cntTrue2/cntAll2)*100), R.drawable.algebra,(int)cntTrue2,likeMain1,likeMain2));
            }
        }
        for(int i = 0; i< Distrib.ogeNameTheme.size()/2*2; i+=2){
            double cntTrue1=0,cntTrue2=0,cntAll1=1,cntAll2=1;
            if(Distrib.allInf !=null){
                Log.e("checkcheck","Menu он не пустой");
                ArrayList<Long> inf1=(ArrayList<Long>) Distrib.allInf.get(Distrib.ogeNameTheme.get(i));
                if(inf1!=null) {
                    cntAll1 = itemsCtnTheme.get(Distrib.ogeNameTheme.get(i));
                    for (int j = 0; j < inf1.size(); j++) {
                        if (inf1.get(j) == Long.valueOf(2)) {
                            cntTrue1++;
                        }
                    }
                }
                ArrayList<Long> inf2=(ArrayList<Long>) Distrib.allInf.get(Distrib.ogeNameTheme.get(i+1));
                if(inf2!=null) {
                    cntAll2 = itemsCtnTheme.get(Distrib.ogeNameTheme.get(i+1));
                    for (int j = 0; j < inf2.size(); j++) {
                        if (inf2.get(j) == Long.valueOf(2)) {
                            cntTrue2++;
                        }
                    }
                }
                likeMain1=LikeInf.get(Distrib.ogeNameTheme.get(i));
                likeMain2=LikeInf.get(Distrib.ogeNameTheme.get(i+1));
                subcategoryOGE.add(new Subcategory(Distrib.ogeNameTheme.get(i),(int)(((double)cntTrue1/cntAll1)*100), R.drawable.examination,(int)cntTrue1, Distrib.ogeNameTheme.get(i+1),(int)(((double)cntTrue2/cntAll2)*100), R.drawable.examination,(int)cntTrue2,likeMain1,likeMain2));
            }
        }
        for(int i = 0; i<(Distrib.schoolNameTheme.size())/2*2; i+=2){
            double cntTrue1=0,cntTrue2=0,cntAll1=1,cntAll2=1;
            if(Distrib.allInf !=null){
                Log.e("checkcheck","Menu он не пустой");
                ArrayList<Long> inf1=(ArrayList<Long>) Distrib.allInf.get(Distrib.schoolNameTheme.get(i));
                if(inf1!=null) {
                    cntAll1 = itemsCtnTheme.get(Distrib.schoolNameTheme.get(i));
                    for (int j = 0; j < inf1.size(); j++) {
                        if (inf1.get(j) == Long.valueOf(2)) {
                            cntTrue1++;
                        }
                    }
                }
                ArrayList<Long> inf2=(ArrayList<Long>) Distrib.allInf.get(Distrib.schoolNameTheme.get(i+1));
                if(inf2!=null) {
                    cntAll2 = itemsCtnTheme.get(Distrib.schoolNameTheme.get(i+1));
                    for (int j = 0; j < inf2.size(); j++) {
                        if (inf2.get(j) == Long.valueOf(2)) {
                            cntTrue2++;
                        }
                    }
                }
                likeMain1=LikeInf.get(Distrib.schoolNameTheme.get(i));
                likeMain2=LikeInf.get(Distrib.schoolNameTheme.get(i+1));
                subcategorySchool.add(new Subcategory(Distrib.schoolNameTheme.get(i),(int)(((double)cntTrue1/cntAll1)*100), R.drawable.school,(int)cntTrue1,Distrib.schoolNameTheme.get(i+1),(int)(((double)cntTrue2/cntAll2)*100), R.drawable.school,(int)cntTrue2,likeMain1,likeMain2));
            }
        }
        for(int i2 = 0; i2<(Distrib.kombaNameTheme.size())/2*2; i2+=2){
            final int i=i2;
            cntTrue1=0;cntTrue2=0;cntAll1=1;cntAll2=1;
            if(Distrib.allInf !=null){
                int like1,like2;
                Log.e("checkcheck","Menu он не пустой");
                ArrayList<Long> inf1=(ArrayList<Long>) Distrib.allInf.get(Distrib.kombaNameTheme.get(i));
                if(inf1!=null) {
                    cntAll1 = itemsCtnTheme.get(Distrib.kombaNameTheme.get(i));
                    for (int j = 0; j < inf1.size(); j++) {
                        if (inf1.get(j) == Long.valueOf(2)) {
                            cntTrue1++;
                        }
                    }
                }
                ArrayList<Long> inf2=(ArrayList<Long>) Distrib.allInf.get(Distrib.kombaNameTheme.get(i+1));
                if(inf2!=null) {
                    cntAll2 = itemsCtnTheme.get(Distrib.kombaNameTheme.get(i+1));
                    for (int j = 0; j < inf2.size(); j++) {
                        if (inf2.get(j) == Long.valueOf(2)) {
                            cntTrue2++;
                        }
                    }
                }
                likeMain1=LikeInf.get(Distrib.kombaNameTheme.get(i));
                likeMain2=LikeInf.get(Distrib.kombaNameTheme.get(i+1));
                subcategoryKomba.add(new Subcategory(Distrib.kombaNameTheme.get(i),(int)(((double)cntTrue1/cntAll1)*100), R.drawable.komba,(int)cntTrue1,Distrib.kombaNameTheme.get(i+1),(int)(((double)cntTrue2/cntAll2)*100), R.drawable.komba,(int)cntTrue2,likeMain1,likeMain2));
            }
        }
        arrayList.add(new Category("Популярные",subcategoryPopular,0));
        arrayList.add(new Category("ОГЭ 2020",subcategoryOGE,2));
        arrayList.add(new Category("Геометрия",subcategoryGeometry,3));
        arrayList.add(new Category("Алгебра",subcategoryAlgebra,4));
        arrayList.add(new Category("Школа",subcategorySchool,1));
        arrayList.add(new Category("Комбинаторика",subcategoryKomba,5));
        return arrayList;
    }
    static RecyclerView categoriesView;
    ProgressBar progressBar;
    TextView textViewProgress;



    //----------------------------------------------------------------------------------------------
    @Override
    // Переопределяем метод onCreateView
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.test_new_menu, container, false);
//        search=view.findViewById(R.id.editTextSearch);

        return view;
    }

    Comparator<List<String>> comparator = new Comparator<List<String>>() {
        public int compare(List<String> left, List<String> right) {
            return (Integer.compare(Integer.parseInt(right.get(0)),Integer.parseInt(left.get(0))));
        }
    };

    double cntAll=1,cntTrue=0;

    @Override
    public void onStart(){
        super.onStart();
        db=FirebaseFirestore.getInstance();
        textViewProgress=view.findViewById(R.id.textViewProgress);
        progressBar=view.findViewById(R.id.progressBarStudy);
        Button buttonStudy=view.findViewById(R.id.buttonStudy);
        context_this=getActivity();
        ImageView imgcrat=view.findViewById(R.id.imgcrat);
        buttonStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context_this, Topic.class);
                intent.putExtra("thisTheme", CurrentUser.lastTheme);
                context_this.startActivity(intent);
                MainActivity.textLoad.setVisibility(View.VISIBLE);
                getActivity().overridePendingTransition(R.anim.slidein, R.anim.slideout);
            }
        });
        textViewStudy=view.findViewById(R.id.textViewStudy);
//        textViewStudy.setText(lastTheme);
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        final DocumentReference docRef22 = db.collection("account").document(user.getUid());
        docRef22.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Map<String, Object> inf = documentSnapshot.getData();
                        CurrentUser.lastTheme=inf.get("lastTheme").toString();
                        textViewStudy.setText(inf.get("lastTheme").toString());
                    }
                }
            }
        });
        Map<String,Object> imageTask= CurrentUser.AllTask.get(CurrentUser.lastTheme);
        if(imageTask!=null&&imageTask.get("theme")=="алгебра"){
            imgcrat.setImageResource(R.drawable.geometry);
        }else{
            imgcrat.setImageResource(R.drawable.algebra);
        }


        CircleImageView view7 = view.findViewById(R.id.class7);
        CircleImageView view8 = view.findViewById(R.id.class8);
        CircleImageView view9 = view.findViewById(R.id.class9);
        CircleImageView viewOlympiad = view.findViewById(R.id.olympiad);
        view7.setImageResource(R.drawable.seven2);
        view7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Section.class);
                intent.putExtra("class","seven");
                startActivity(intent);
            }
        });
        view8.setImageResource(R.drawable.eight2);
        view8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Section.class);
                intent.putExtra("class","eight");
                startActivity(intent);
            }
        });
        view9.setImageResource(R.drawable.nine2);
        view9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Section.class);
                intent.putExtra("class","nine");
                startActivity(intent);
            }
        });
        viewOlympiad.setImageResource(R.drawable.brain);
        viewOlympiad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Section.class);
                intent.putExtra("class","hard");
                startActivity(intent);
            }
        });




        //-----------------------------------------------------------

        DocumentReference docRef=db.collection("account").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                    if(documentSnapshot.exists()){
                        Map<String,Object> inf=documentSnapshot.getData();
                        Distrib.allInf =inf;
                    }
                }
            }
        });

        arrayListsTheme.clear();

        //-------------------------------------------------

        db.collection("task2")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                        if (task2.isSuccessful()) {
                            int cnt=0;
                            for (QueryDocumentSnapshot documentTask : task2.getResult()) {//здесь мы бежим по всем темам
                                Log.e("checkcheckLikeNew",String.valueOf(documentTask.getId().toString()));
                                LikeInf.put(documentTask.getId().toString(),((Long)documentTask.getData().get("like")).intValue());ArrayList<String> user = new ArrayList<>();
                                ItemsInf.put(documentTask.getId().toString(),((Long)documentTask.getData().get("items")).intValue());
                                ArrayList<String> theme = new ArrayList<>();
                                theme.add(documentTask.getData().get("like").toString());
                                theme.add(String.valueOf(documentTask.getId()));
                                arrayListsTheme.add(theme);
                                if(cnt==task2.getResult().size()-1){
                                    Collections.sort(arrayListsTheme, comparator);
                                    Log.e("checkcheckLikeInfAll","GOOD");
                                    List<Category> categories = getRandomData();
                                    categoriesView = view.findViewById(R.id.categories_list);
                                    categoriesView.setHasFixedSize(true);
                                    categoriesView.setLayoutManager(new LinearLayoutManager(
                                                                            context_this,
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
                                    categoriesView.setAdapter(new CategoriesAdapter(categories));
                                }
                                cnt++;
                            }
                        }
                    }
                });

        //----------------------------------------------------------------------------------------
        cntTrue=0;cntAll=1;

        final DocumentReference docRes3 = db.collection("account").document(user.getUid());
        docRes3.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Map<String, Object> inf = documentSnapshot.getData();
                        ArrayList<Long> taskProgress=(ArrayList<Long>) Distrib.allInf.get(CurrentUser.lastTheme);
                        if(taskProgress!=null) {
                            for (int i = 0; i < taskProgress.size(); i++) {
                                if (taskProgress.get(i) == 2) {
                                    cntTrue++;
                                }
                            }
                        }
                        cntAll=1;
                        final DocumentReference docRefUser = db.collection("task2").document(textViewStudy.getText().toString());
                        docRefUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if (documentSnapshot.exists()) {
                                        Map<String, Object> inf = documentSnapshot.getData();
                                        int cntAll=((Long)inf.get("cnt_task")).intValue();
                                        textViewProgress.setText("Текущий прогресс " + String.valueOf((int) (cntTrue * 100 / cntAll)) + "%");
                                        int progress = (int) (((double) (cntTrue / cntAll)) * 100);
                                        progressBar.setProgress(progress);
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });




//        List<CircleMenu.Category> CircleCategories = CircleMenu.getRandomData();
//        categoriesView = view.findViewById(R.id.categories_list_circle);
//        categoriesView.setHasFixedSize(true);
//        categoriesView.setLayoutManager(new LinearLayoutManager(
//                context_this,
//                RecyclerView.VERTICAL,
//                false
//        ));
//        categoriesView.setAdapter(new CircleMenu.CategoriesAdapter(CircleCategories));


        //-----------------------------------------------------------------------------------------
    }

    //----------------------------------------------------------------------------------------------

    static class Category {
        String name;
        int checkScroolTheme;
        List<Subcategory> subcategories;

        Category(String name, List<Subcategory> subcategories, int check) {
            this.name = name;
            this.checkScroolTheme=check;
            this.subcategories = subcategories;
        }
    }

    static class Subcategory {
        String name1;
        int progress1;
        int image1;
        int right1;
        String name2;
        int progress2;
        int image2;
        int right2;
        int like1;int like2;

        Subcategory(String name1, int progress1,int image1,int right1,String name2, int progress2,int image2,int right2,int like1,int like2) {
            this.name1 = name1;
            this.progress1 = progress1;
            this.image1=image1;
            this.right1=right1;
            this.name2 = name2;
            this.progress2 = progress2;
            this.image2=image2;
            this.right2=right2;
            this.like1=like1;
            this.like2=like2;
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
        public CategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.category_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CategoriesAdapter.ViewHolder holder, int position) {
            final Category category = data.get(position);
            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    holder.subcategoriesList.getContext(),
                    RecyclerView.HORIZONTAL,
                    false
            );
            if(category.checkScroolTheme==0){
                holder.subcategoryTheme.setVisibility(View.INVISIBLE);
            }else {
                holder.subcategoryTheme.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent( categoriesView.getContext() , ThemeActivity.class);
                        intent.putExtra("theme_name", category.name );
                        categoriesView.getContext().startActivity(intent);
                    }
                });
            }
            holder.categoryName.setText(category.name);
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
            TextView subcategoryTheme;
            TextView categoryName;
            ViewHolder(View itemView) {
                super(itemView);
                subcategoryTheme= itemView.findViewById(R.id.checkTheme);
                categoryName=itemView.findViewById(R.id.category_name);
                subcategoriesList = itemView.findViewById(R.id.subcategories_list);
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
        public SubcategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.subcategory_list_itemnew, parent, false);
            return new ViewHolder(view);
        }
        ArrayList task_1;
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final Subcategory subcategory = data.get(position);
            holder.view1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent intent=new Intent(context_this, Topic.class);
                    intent.putExtra("thisTheme",subcategory.name1);
                    intent.putExtra("prevTheme",1);
                    Something.itemViewPrev=holder.view1;
                    CurrentUser.setLastTheme(subcategory.name1);
                    FirebaseAuth mAuth=FirebaseAuth.getInstance();
                    FirebaseUser user=mAuth.getCurrentUser();
                    final DocumentReference docRef = db.collection("account").document(user.getUid());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if (documentSnapshot.exists()) {
                                    Map<String, Object> inf = documentSnapshot.getData();
                                    Map<String, Object> m = new HashMap<>();
                                    if(inf.get(subcategory.name1)==null){
                                        final DocumentReference docRefNewTheme = db.collection("task2").document(subcategory.name1);
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
                                                        m3.put(subcategory.name1,newThemeUser);
                                                        m3.put(subcategory.name1+"Solution",newThemeUser);
                                                        docRef.set(m3,SetOptions.merge());
                                                        allInf.put(subcategory.name1,newThemeUser);
                                                        allInf.put(subcategory.name1+"Solution",newThemeUser);
                                                        context_this.startActivity(intent);
                                                    }
                                                }
                                            }
                                        });

                                    }else if(((ArrayList)inf.get(subcategory.name1)).size()!=ItemsInf.get(subcategory.name1)){
                                        ArrayList newThemeUser=((ArrayList)inf.get(subcategory.name1));
                                        for(int i3=newThemeUser.size();i3<ItemsInf.get(subcategory.name1);i3++){
                                            newThemeUser.add(1L);
                                        }
                                        HashMap<String, Object> m3 = new HashMap<>();
                                        m3.put(subcategory.name1,newThemeUser);
                                        m3.put(subcategory.name1+"Solution",newThemeUser);
                                        docRef.set(m3,SetOptions.merge());
                                        allInf.put(subcategory.name1,newThemeUser);
                                        allInf.put(subcategory.name1+"Solution",newThemeUser);
                                        Menu.context_this.startActivity(intent);
                                    }else{
                                        context_this.startActivity(intent);
                                    }
                                    m.put("lastTheme",subcategory.name1);
                                    docRef.set(m, SetOptions.merge());
                                }
                            }
                        }
                    });
                    CurrentUser.setLastTheme(subcategory.name1);
                    textViewStudy.setText(CurrentUser.lastTheme);
                    MainActivity.textLoad.setVisibility(View.VISIBLE);
//                    overridePendingTransition(R.anim.left_anim, R.anim.right_anim);

                }
            });
            holder.view2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent intent=new Intent(context_this, Topic.class);
                    intent.putExtra("thisTheme",subcategory.name2);
                    FirebaseAuth mAuth=FirebaseAuth.getInstance();
                    FirebaseUser user=mAuth.getCurrentUser();
                    Something.itemViewPrev=holder.view1;
                    final DocumentReference docRef = db.collection("account").document(user.getUid());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if (documentSnapshot.exists()) {
                                    Map<String, Object> inf = documentSnapshot.getData();
                                    Map<String, Object> m = new HashMap<>();
                                    if(inf.get(subcategory.name2)==null){
                                        final DocumentReference docRefNewTheme = db.collection("task2").document(subcategory.name2);
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
                                                        m3.put(subcategory.name2,newThemeUser);
                                                        m3.put(subcategory.name2+"Solution",newThemeUser);
                                                        docRef.set(m3,SetOptions.merge());
                                                        allInf.put(subcategory.name2,newThemeUser);
                                                        allInf.put(subcategory.name2+"Solution",newThemeUser);
                                                        context_this.startActivity(intent);
                                                    }
                                                }
                                            }
                                        });

                                    }else if(((ArrayList)inf.get(subcategory.name2)).size()!=ItemsInf.get(subcategory.name2)){
                                        ArrayList newThemeUser=((ArrayList)inf.get(subcategory.name2));
                                        for(int i3=newThemeUser.size();i3<ItemsInf.get(subcategory.name2);i3++){
                                            newThemeUser.add(1L);
                                        }
                                        HashMap<String, Object> m3 = new HashMap<>();
                                        m3.put(subcategory.name2,newThemeUser);
                                        m3.put(subcategory.name2+"Solution",newThemeUser);
                                        docRef.set(m3,SetOptions.merge());
                                        allInf.put(subcategory.name2,newThemeUser);
                                        allInf.put(subcategory.name2+"Solution",newThemeUser);
                                        Menu.context_this.startActivity(intent);
                                    }else{
                                        context_this.startActivity(intent);
                                    }
                                    m.put("lastTheme",subcategory.name2);
                                    docRef.set(m, SetOptions.merge());
                                }
                            }
                        }
                    });
                    CurrentUser.setLastTheme(subcategory.name2);
                    textViewStudy.setText(CurrentUser.lastTheme);
                    MainActivity.textLoad.setVisibility(View.VISIBLE);
//                    overridePendingTransition(R.anim.left_anim, R.anim.right_anim);

                }
            });
            Map<String,Object> thisTask1= CurrentUser.AllTask.get(subcategory.name1);
            Map<String,Object> thisTask2= CurrentUser.AllTask.get(subcategory.name2);
            if(thisTask1!=null&&(thisTask1.get("theme")).equals("алгебра")) {
                holder.subcategoryImage1.setImageResource(R.drawable.algebra);
            }else{
                holder.subcategoryImage1.setImageResource(R.drawable.geometry);
            }
            if(thisTask2!=null&&(thisTask2.get("theme")).equals("алгебра")) {
                holder.subcategoryImage2.setImageResource(R.drawable.algebra);
            }else{
                holder.subcategoryImage2.setImageResource(R.drawable.geometry);
            }
            String s1,s2;
            if(subcategory.right1==1){
                s1=" задача";
            }else if(subcategory.right1==2||subcategory.right1==3||subcategory.right1==4){
                s1=" задачи";
            }else{
                s1=" задач";
            }
            holder.subcategoryImage1.setImageResource(subcategory.image1);
            holder.subcategoryName1.setText(subcategory.name1);
            holder.subcategoryText1.setText("Решено "+String.valueOf(subcategory.right1)+s1);
            holder.subcategoryProgress1.setProgress(subcategory.progress1);
            holder.subcategoryImage2.setImageResource(subcategory.image2);
            holder.subcategoryName2.setText(subcategory.name2);
            if(subcategory.right2==1){
                s2=" задача";
            }else if(subcategory.right2==2||subcategory.right2==3||subcategory.right2==4){
                s2=" задачи";
            }else{
                s2=" задач";
            }
            holder.subcategoryText2.setText("Решено "+String.valueOf(subcategory.right2)+s2);
            holder.subcategoryProgress2.setProgress(subcategory.progress2);

            //--------LIKE

            if(subcategory.like1>=1000){
                holder.subcategoryLike1.setText(String.valueOf((subcategory.like1+500)/1000)+"K");
            }else{
                holder.subcategoryLike1.setText(String.valueOf(subcategory.like1));
            }
            if(subcategory.like2>=1000){
                holder.subcategoryLike2.setText(String.valueOf((subcategory.like2+500)/1000)+"K");
            }else{
                holder.subcategoryLike2.setText(String.valueOf(subcategory.like2));
            }
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams((int)(Distrib.widthScreen-200),
                    FrameLayout.LayoutParams.WRAP_CONTENT); // or set height to any fixed value you want

            holder.linearLayout1.setLayoutParams(lp);
            holder.linearLayout2.setLayoutParams(lp);
// OR
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView subcategoryName1,subcategoryText1,subcategoryLike1,subcategoryLike2;
            ProgressBar subcategoryProgress1;
            ImageView subcategoryImage1;
            TextView subcategoryName2,subcategoryText2;
            ProgressBar subcategoryProgress2;
            ImageView subcategoryImage2;
            View view1,view2;
            LinearLayout linearLayout1,linearLayout2;


            ViewHolder(View itemView) {
                super(itemView);
                view1=itemView.findViewById(R.id.menu1);
                view2=itemView.findViewById(R.id.menu2);
                linearLayout1=view1.findViewById(R.id.linearLayoutWidth);
                linearLayout2=view2.findViewById(R.id.linearLayoutWidth);
                subcategoryLike1=view1.findViewById(R.id.textViewLike);
                subcategoryLike2=view2.findViewById(R.id.textViewLike);
                subcategoryName1 = view1.findViewById(R.id.textViewNameMenu);
                subcategoryText1 = view1.findViewById(R.id.textViewTextMenu);
                subcategoryProgress1 = view1.findViewById(R.id.subcategory_progressMenu);
                subcategoryImage1 = view1.findViewById(R.id.subcategory_imageMenu);
                subcategoryName2 = view2.findViewById(R.id.textViewNameMenu);
                subcategoryText2 = view2.findViewById(R.id.textViewTextMenu);
                subcategoryProgress2 = view2.findViewById(R.id.subcategory_progressMenu);
                subcategoryImage2 = view2.findViewById(R.id.subcategory_imageMenu);
            }
        }
    }

}