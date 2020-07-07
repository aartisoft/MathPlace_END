package com.math4.user.mathplace.olymp;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.math4.user.mathplace.Distrib;
import com.math4.user.mathplace.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.math4.user.mathplace.olymp.MainOlymp.AllTask;


//import static com.math4.user.mathplace.Distrib.AllNameTheme;

public class MakeContest extends AppCompatActivity {


    static TextView textViewHead;
    static ImageView star3,star2;

    ExpandableListView expListView;
    ExpandableListAdapter expListAdapter;
    List<String> expListTitle;
    HashMap<String, List<String>> expListDetail;


    static Context context,context2;
    static CategoriesAdapter mAdapter;
    static ArrayList<Subcategory> arrayListTask=new ArrayList();
    static ArrayList<ArrayList> AllTaskContest=new ArrayList<ArrayList>();
    RecyclerView categoriesView;
    static List<Category> getRandomData() {
        final ArrayList<Category> arrayListReturn = new ArrayList<>();
        for(int i2 = 0; i2< Distrib.allNameTheme.size(); i2++){
            final int i=i2;
            String nameTheme=Distrib.allNameTheme.get(i).toString();
            Log.e("checkcheckFinishTheme",Distrib.allNameTheme.get(i).toString());
            arrayListReturn.add(new MakeContest.Category(nameTheme.toString(),new ArrayList<Subcategory>()));
        }
        return arrayListReturn;
    }
    static FirebaseFirestore db;
    static LayoutInflater inflater2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_contest);
        context=getApplicationContext();
        context2=this;
        inflater2 = getLayoutInflater();
        db= FirebaseFirestore.getInstance();
        Toolbar toolbar = findViewById(R.id.toolbarMakeContest);
        setSupportActionBar(toolbar);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Выбор задач");
        categoriesView = findViewById(R.id.categories_list_themeMakeContest);
        List<Category> CircleCategories = MakeContest.getRandomData();
        categoriesView.setHasFixedSize(false);
        categoriesView.setLayoutManager(new LinearLayoutManager(
                this,
                RecyclerView.VERTICAL,
                false
        ));
        mAdapter=new MakeContest.CategoriesAdapter(CircleCategories);
        categoriesView.setAdapter(mAdapter);







//        expListView = findViewById(R.id.expListView);
//        expListDetail = ListData.loadData();
//
//        expListTitle = new ArrayList<>(expListDetail.keySet());
//        expListAdapter = new ListAdapter(context, expListTitle, expListDetail);
//
//        expListView.setAdapter(expListAdapter);
//        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
//
//            @Override
//            public void onGroupExpand(int groupPosition) {
//
//            }
//        });
//
//        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
//
//            @Override
//            public void onGroupCollapse(int groupPosition) {
//
//            }
//        });
//
//        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v,
//                                        int groupPosition, int childPosition, long id) {
//                final String name = expListDetail.get(expListTitle.get(groupPosition)).get(childPosition);
//                final String group = expListTitle.get(groupPosition);
//
//                //expListDetail.get(expListTitle.get(groupPosition)).get(childPosition)
//                //Something.itemViewPrev=holder.itemView;
//                FirebaseAuth mAuth=FirebaseAuth.getInstance();
//                FirebaseUser user=mAuth.getCurrentUser();
//                final DocumentReference docRefUser = db.collection("account").document(user.getUid());
//                docRefUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            DocumentSnapshot documentSnapshot = task.getResult();
//                            if (documentSnapshot.exists()) {
//                                Map<String, Object> inf = documentSnapshot.getData();
//                                final DocumentReference docRes3 = db.collection("task2").document(name);
//                                Log.e("checkcheckClass","getObject");
//                                docRes3.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                        Log.e("checkcheckClass","getObjectFirst");
//                                        if (task.isSuccessful()) {
//                                            DocumentSnapshot documentSnapshot = task.getResult();
//                                            Log.e("checkcheckClass","getObjectSecond");
//                                            if (documentSnapshot.exists()) {
//                                                Map<String, Object> inf = documentSnapshot.getData();
//                                                final  int all_taskTheme=((Long)inf.get("cnt_task")).intValue();
//                                                Map<String, Object> m = new HashMap<>();
//                                                if(inf.get(name)==null){
//                                                    final DocumentReference docRefNewTheme = db.collection("task2").document(name);
//                                                    docRefNewTheme.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                                        @Override
//                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                                            if(task.isSuccessful()){
//                                                                DocumentSnapshot documentSnapshot2=task.getResult();
//                                                                if(documentSnapshot2.exists()){
//                                                                    Map<String,Object> infTheme=documentSnapshot2.getData();
//                                                                    int themeTask=((Long)infTheme.get("items")).intValue();
//                                                                    ArrayList newThemeUser=new ArrayList();
//                                                                    for(int i3=0;i3<themeTask;i3++){
//                                                                        newThemeUser.add(1L);
//                                                                    }
//                                                                    HashMap<String, Object> m3 = new HashMap<>();
//                                                                    m3.put(name,newThemeUser);
//                                                                    m3.put(name+"Solution",newThemeUser);
//                                                                    docRefUser.set(m3, SetOptions.merge());
//                                                                }
//                                                            }
//                                                        }
//                                                    });
//
//                                                }else if(((ArrayList)inf.get(name)).size()!=all_taskTheme){
//                                                    ArrayList newThemeUser=((ArrayList)inf.get(name));
//                                                    for(int i3=newThemeUser.size();i3<all_taskTheme;i3++){
//                                                        newThemeUser.add(1L);
//                                                    }
//                                                    HashMap<String, Object> m3 = new HashMap<>();
//                                                    m3.put(name,newThemeUser);
//                                                    m3.put(name+"Solution",newThemeUser);
//                                                    docRefUser.set(m3,SetOptions.merge());
//                                                }else{
//                                                }
//                                                m.put("lastTheme",name);
//                                                docRefUser.set(m, SetOptions.merge());
//                                            }
//                                        }
//                                    }
//                                });
//                            }
//                        }
//                    }
//                });
//                return false;
//            }
//        });

    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_make_olymp,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                showInterstitial();
                this.finish();
                return true;
            case R.id.action_ok:
                for(int i=0;i<AllTaskContest.size();i++){
                    AllTask.add(AllTaskContest.get(i));
                }
                this.finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    static class Category {
                String name;
                int click;
                int angle=0;
        List<Subcategory> subcategories;

        Category(String name, List<Subcategory> subcategories) {
            this.name = name;
            this.click=0;
            this.subcategories = subcategories;
        }
    }

    static class Subcategory {
        String name;
        String title;
        int id;

        Subcategory(String name,String title, int id)
        {
            this.name = name; this.title=title; this.id=id;
        }
    }

    //----------------------------------------------------------------------------------------------

    static class CategoriesAdapter extends RecyclerView.Adapter<MakeContest.CategoriesAdapter.ViewHolder> {
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        List<Category> data;

        CategoriesAdapter(List<Category> data) {
            this.data = data;
        }


        @Override
        public MakeContest.CategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.category_list_item_contest, parent, false);
            return new MakeContest.CategoriesAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MakeContest.CategoriesAdapter.ViewHolder holder,final int position) {
            final MakeContest.Category category = data.get(position);
            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    holder.subcategoriesList.getContext(),
                    RecyclerView.VERTICAL,
                    false
            );
            List<Subcategory> subcategoryList=new ArrayList<Subcategory>();
            subcategoryList.add(new Subcategory("SUbcategoryNew",category.name,0));
            final SubcategoriesAdapter subcategoriesAdapter=new MakeContest.SubcategoriesAdapter(category.subcategories);
            holder.subcategoriesList.setHasFixedSize(false);
            holder.subcategoriesList.setLayoutManager(layoutManager);
            holder.subcategoriesList.setAdapter(subcategoriesAdapter);
//            holder.subcategoriesList.setRecycledViewPool(viewPool);

            if(category.angle==0){
                holder.categoryImage.setRotation(0);
            }else{
                holder.categoryImage.animate().rotation(90);
            }

            holder.categoryName.setText(category.name);
            holder.categoryImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(category.click%2==0) {
                        category.angle=90;
//                        holder.categoryImage.animate().rotation(90);
                        final ArrayList arrayList22=new ArrayList();
                        final List<Subcategory> list=new ArrayList<Subcategory>();
//                        subcategoriesAdapter.setItems(new Subcategory("SUbcategoryNew",category.name,0));
//                        setItems(new MakeContest.Category("New",arrayList22));
//                        category.subcategories.clear();
                        DocumentReference docRef22=db.collection("task2").document(category.name);
                        docRef22.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot documentSnapshot=task.getResult();
                                    if(documentSnapshot.exists()){
                                        final ArrayList<Subcategory> subcategoryList2 = new ArrayList<>();
                                        Map<String,Object> inf = documentSnapshot.getData();
                                        int all_task=((Long)inf.get("items")).intValue();
                                        for(int i2=0;i2<all_task;i2++){
                                            final int i=i2;
                                            String nameTheme;
                                            if(((ArrayList)inf.get("task"+String.valueOf(i))).get(1).toString().equals("theory")) {
                                                nameTheme = "Теория";
                                            }else{
                                                nameTheme = "Задача";
                                            }
                                            list.add(new Subcategory(nameTheme.toString(),category.name,i));
//                                            category.subcategories.add(new Subcategory(nameTheme.toString(),category.name,i));
//                                            subcategoriesAdapter.notifyDataSetChanged();
//                                            subcategoriesAdapter.notifyItemInserted(category.subcategories.size());
//                                            break;
//                                            subcategoryList2.add(new MakeContest.Subcategory(nameTheme.toString(),category.name,i));
                                        }
                                        subcategoriesAdapter.setItems(list);
                                        setItems(new MakeContest.Category("New",arrayList22),position,holder.categoryImage);
//                                        holder.categoryImage.animate().rotation(90);
//                                        holder.categoryImage.setRotation(90);
//                                        holder.subcategoriesList.setAdapter(new MakeContest.SubcategoriesAdapter(subcategoryList2));
                                    }
                                }
                            }
                        });
//                        holder.categoryImage.setRotation(90);
                    }else{
                        category.angle=0;
                        holder.categoryImage.animate().rotation(0);
                        subcategoriesAdapter.clearItems();
                    }
                    category.click++;
                }
            });
        }


        public void setItems(Category tweet, int position, ImageView imageView) {
//            data.add(tweet);
//            notifyItemChanged(position);
            notifyDataSetChanged();
//            imageView.animate().rotation(90);
        }

        public void clearItems() {
            data.clear();
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            RecyclerView subcategoriesList;
                        TextView categoryName;
                        ImageView categoryImage;
            ViewHolder(View itemView) {
                super(itemView);
                categoryImage=itemView.findViewById(R.id.openThemeContest);
                categoryName=itemView.findViewById(R.id.category_nameContest);
                subcategoriesList = itemView.findViewById(R.id.subcategories_listTaskContest);
            }
        }
    }

    //----------------------------------------------------------------------------------------------

    static class SubcategoriesAdapter extends RecyclerView.Adapter<MakeContest.SubcategoriesAdapter.ViewHolder> {

        List<Subcategory> data;

        SubcategoriesAdapter(List<Subcategory> data) {
            this.data = data;
        }


        @Override
        public MakeContest.SubcategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.subcategory_list_item_choose, parent, false);
            return new MakeContest.SubcategoriesAdapter.ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(final MakeContest.SubcategoriesAdapter.ViewHolder holder, final int position) {
            final MakeContest.Subcategory subcategory = data.get(position);
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context2);
//
//                    LayoutInflater inflater2 = getLayoutInflater();
                    View dialogView;

                    if(!subcategory.name.equals("Теория")) {

                        dialogView = inflater2.inflate(R.layout.alertdialog_task, null);
                    }else{
                        dialogView = inflater2.inflate(R.layout.alertdialog_theory, null);
                    }
//
//                    // Specify alert dialog is not cancelable/not ignorable
                    builder.setCancelable(true);
//
//                    // Set the custom layout as alert dialog view
                    builder.setView(dialogView);

                    final LinearLayout linearLayout3=(LinearLayout) dialogView.findViewById(R.id.linearLayoutTaskTextContest);

                    // Get the custom alert dialog view widgets reference
                    Button btn_positive = dialogView.findViewById(R.id.dialog_positive_btnTask);
                    TextView btn_negative = dialogView.findViewById(R.id.dialog_negative_btnTask);
                    if(subcategory.name.equals("Задача")) {
                        textViewHead = dialogView.findViewById(R.id.textViewHead);
                        star3 = dialogView.findViewById(R.id.star2);
                        star2 = dialogView.findViewById(R.id.star1);
                    }
//                    final EditText et_name = (EditText) dialogView.findViewById(R.id.et_name);


                    final DocumentReference docRefContest = db.collection("task2").document(subcategory.title);
                    docRefContest.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if (documentSnapshot.exists()) {
                                    Map<String, Object> inf = documentSnapshot.getData();

                                    final Boolean theory;

                                    if(((String)((ArrayList) inf.get("task"+String.valueOf(position))).get(1)).equals("theory")){
                                        theory=true;
                                    }else{
                                        theory=false;
                                    }
                                    if(!theory) {
                                        int stars = Integer.parseInt((String) ((ArrayList) inf.get("task" + String.valueOf(position))).get(2));
                                        if (stars == 1) {
                                            star3.setVisibility(View.INVISIBLE);
                                            star2.setVisibility(View.INVISIBLE);
                                        } else if (stars == 2) {
                                            star3.setVisibility(View.INVISIBLE);
                                        }
                                        textViewHead.setText("Задача");
                                    }
                                    String text=(String)(((ArrayList) inf.get("task"+String.valueOf(position))).get(0));
                                    int last=0;
                                    String res="";
                                    for(int i=0;i<text.length()-1;i++){
                                        if(text.substring(i,i+2).equals("\\n")){
                                            res=res+text.substring(last,i)+"\n";
                                            if(i!=last){ addText(text.substring(last,i),linearLayout3);}
                                            last=i+2;
                                        }else if(text.substring(i,i+2).equals("[h")){
                                            int j=i;
                                            while(j<text.length()&&!text.substring(j,j+1).equals("]")){
                                                j++;
                                            }
                                            addText(text.substring(last,i),linearLayout3);
                                            last=j+1;
                                            addImage(text.substring(i+1,last-1),linearLayout3);
                                        }
                                    }
                                    if(text.length()!=last){
//                                        Log.e("checkcheckTextTheory",text.substring(last,text.length()));
                                        addText(text.substring(last,text.length()),linearLayout3);
                                    }
                                }
                            }
                        }
                    });

                    // Create the alert dialog
                    final AlertDialog dialog = builder.create();

                    // Set positive/yes button click listener
                    btn_positive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            // Dismiss the alert dialog
                            if(holder.checkBox.isChecked()) {
                                Toast.makeText(context, "Вы её уже выбрали", Toast.LENGTH_LONG).show();
                            }else{
                                holder.checkBox.setChecked(true);
                            }
                            dialog.cancel();
                        }
                    });
//
//                    // Set negative/no button click listener
                    btn_negative.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Dismiss/cancel the alert dialog
                            //dialog.cancel();
                            dialog.dismiss();
                        }
                    });

                    // Display the custom alert dialog on interface
                    dialog.show();
                }
            });
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        FirebaseFirestore db= FirebaseFirestore.getInstance();
                        final DocumentReference docRef = db.collection("task2").document(subcategory.title);
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if (documentSnapshot.exists()) {
                                        Map<String, Object> inf = documentSnapshot.getData();
                                        ArrayList m=(ArrayList)inf.get("task"+String.valueOf(subcategory.id));
                                        AllTaskContest.add(m);
                                    }
                                }
                            }
                        });
                    }else {
                        FirebaseFirestore db= FirebaseFirestore.getInstance();
                        final DocumentReference docRef = db.collection("task2").document(subcategory.title);
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if (documentSnapshot.exists()) {
                                        Map<String, Object> inf = documentSnapshot.getData();
                                        ArrayList m=(ArrayList)inf.get("task"+String.valueOf(subcategory.id));
                                        AllTaskContest.remove(m);
                                    }
                                }
                            }
                        });
                    }
                }
            });

            holder.subcategoryName.setText(subcategory.name);
        }


        public void setItems(Collection<Subcategory> tweet) {
            data.addAll(tweet);
            notifyDataSetChanged();
        }

        public void clearItems() {
            data.clear();
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView subcategoryName,subcategoryText,subcategoryLike;
            LinearLayout linearLayout;
            ProgressBar subcategoryProgressBar;
            CheckBox checkBox;

            ViewHolder(View itemView) {
                super(itemView);
                checkBox=itemView.findViewById(R.id.checkBox);
                linearLayout=itemView.findViewById(R.id.linearLayoutMakeContest);
//                subcategoryLike=itemView.findViewById(R.id.textViewLikeTheme);
                subcategoryName = itemView.findViewById(R.id.textViewContest);
//                subcategoryText = itemView.findViewById(R.id.textViewText);
//                subcategoryProgressBar = itemView.findViewById(R.id.progressBarTheme);
            }
        }
    }




    static public void addImage(String url, LinearLayout linearLayoutTask){
        final ImageView imageView=new ImageView(context);
        RelativeLayout.LayoutParams MyParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        MyParams.leftMargin=8;
        MyParams.rightMargin=8;
        imageView.setLayoutParams(MyParams);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(context).load(url).into(imageView);
        linearLayoutTask.addView(imageView);
    }
    static public void addText(String text, LinearLayout linearLayoutTask){
        final TextView textView=new TextView(context);
        RelativeLayout.LayoutParams MyParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        MyParams.topMargin=16;
        MyParams.leftMargin=16;
        MyParams.rightMargin=8;
        textView.setLayoutParams(MyParams);
        textView.setText(text);
        textView.setTextSize(20);
        textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextColor(Color.parseColor("#5B6175"));
        linearLayoutTask.addView(textView);
    }
}
