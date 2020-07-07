package com.math4.user.mathplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.android.gms.tasks.OnCompleteListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.math4.user.mathplace.Distrib.allInf;
import static com.math4.user.mathplace.Topic.thisContext;
import static com.math4.user.mathplace.Topic.type;
import static com.math4.user.mathplace.Task.interpolator;


public class Comment extends AppCompatActivity {


    RecyclerView categoriesView;
    LinearLayout linearLayoutTask;
    static FirebaseFirestore db;
    DocumentReference docRef;
    DocumentReference docRefr;
    static int allComment=0;
    FirebaseAuth mAuth;
    static EditText editTextPost;
    static FirebaseUser user;
    String title;
    static Context contextThis;
    int position;
    static String title2;
    static int position2;
    static List<Comment.Subcategory> getRandomData() {
        final Random random = new Random();
        final ArrayList<Comment.Subcategory> subcategoryList = new ArrayList<>();
        for(int i=0;i<allComment;i++){
            subcategoryList.add(new Subcategory(title2,position2));
            subcategoryList.add(new Subcategory(title2,position2));
        }
        return subcategoryList;

    }


    class BounceInterpolator implements android.view.animation.Interpolator {
        private double mAmplitude;
        private double mFrequency;

        BounceInterpolator(double amplitude, double frequency) {
            mAmplitude = amplitude;
            mFrequency = frequency;
        }

        public float getInterpolation(float time) {
            return (float) (-1 * Math.pow(Math.E, -time / mAmplitude) *
                    Math.cos(mFrequency * time) + 1);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);


        Bundle arguments=getIntent().getExtras();
        String text=arguments.getString("text");
        title=arguments.getString("title");
        position=arguments.getInt("position");



        Toolbar toolbar = findViewById(R.id.toolbarComment);
        setSupportActionBar(toolbar);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Вопросы к задачам");
        contextThis=this;

        editTextPost=findViewById(R.id.editTestPost);

        db= FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        Button buttonPost=findViewById(R.id.post);
        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final CollectionReference colref = db.collection("task2").document(title).collection("comments");
                docRefr = db.collection("task2").document(title).collection("comments").document("task"+String.valueOf(position));
                docRef = db.collection("task2").document(title).collection("comments").document("task"+String.valueOf(position));
                final Map<String, Object> m = new HashMap<>();
                m.put("good","good");
                docRef.set(m,SetOptions.merge());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
//                            Toast.makeText(getApplicationContext(),"Good",Toast.LENGTH_LONG).show();
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                final Map<String, Object> m = documentSnapshot.getData();
                                ArrayList msg = new ArrayList<>();
                                Date date = new Date();
//                                Timestamp time = new Timestamp(date);
                                msg.add(allInf.get("name"));
                                msg.add(editTextPost.getText().toString());
                                editTextPost.setText("");
                                msg.add(0L);
                                msg.add("|");
                                msg.add(String.valueOf(user.getUid()));
//                                msg.add(ServerValue.TIMESTAMP);
                                if(m.get("all_message")!=null) {
                                    Long all = Long.parseLong(m.get("all_message").toString());
                                    all++;
                                    m.put("all_message", all);
                                    m.put("message" + String.valueOf(all-1), msg);
                                }else{
                                    m.put("all_message", 1L);
                                    m.put("message0", msg);
                                }
                                docRef.set(m, SetOptions.merge());
                                docRefr.set(m,SetOptions.merge());
                                Log.e("SEND", "done");
                            }
                        }
                    }
                });
            }
        });
        final Context context=this;


        docRef=db.collection("task2").document(this.title).collection("comments").document("task"+String.valueOf(position));
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Map<String, Object> inf = documentSnapshot.getData();
                        title2=title;
                        position2=position;
                        allComment=((Long)inf.get("all_message")).intValue();
                        categoriesView=findViewById(R.id.categories_list_themeComment);
                        List<Comment.Subcategory> CircleCategories = Comment.getRandomData();
                        categoriesView.setHasFixedSize(true);
                        categoriesView.setLayoutManager(new LinearLayoutManager(
                                context,
                                RecyclerView.VERTICAL,
                                false
                        ));
                        categoriesView.setAdapter(new Comment.SubcategoriesAdapter(CircleCategories));

                    }
                }
            }
        });




        linearLayoutTask=findViewById(R.id.linearLayoutTaskComment);



        setText(text);
    }



    public void setText(String text){
        Log.e("checkchecksetTextTask",text);
        int last=0;
        String res="";
        for(int i=0;i<text.length()-1;i++){
            if(text.substring(i,i+2).equals("\\n")){
                Log.e("checkcheckTheoryN",String.valueOf(i));
                res=res+text.substring(last,i)+"\n";
                if(i!=last){ addText(text.substring(last,i));}
                last=i+2;
            }else if(text.substring(i,i+2).equals("[h")){
                int j=i;
                while(j<text.length()&&!text.substring(j,j+1).equals("]")){
                    j++;
                }
                addText(text.substring(last,i));
                last=j+1;
                addImage(text.substring(i+1,last-1));
            }
        }
        if(text.length()!=last){
            Log.e("checkcheckTextTheory",text.substring(last,text.length()));
            addText(text.substring(last,text.length()));
        }
    }


    public void addImage(String url){
        Log.e("checkcheckUri",url);
        final ImageView imageView=new ImageView(this);
        RelativeLayout.LayoutParams MyParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        MyParams.leftMargin=8;
        MyParams.rightMargin=8;
        imageView.setLayoutParams(MyParams);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(this).load(url).into(imageView);
        linearLayoutTask.addView(imageView);
    }
    public void addText(String text){
        Log.e("checkcheckAddText",text);
        final TextView textView=new TextView(this);
        RelativeLayout.LayoutParams MyParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        MyParams.topMargin=16;
        MyParams.leftMargin=16;
        MyParams.rightMargin=8;
        textView.setLayoutParams(MyParams);
        textView.setText(text);
        textView.setTypeface(type);
        textView.setTextSize(20);
        textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextColor(Color.parseColor("#5B6175"));
        linearLayoutTask.addView(textView);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                showInterstitial();
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    static class Subcategory {
        String title,name;
        int position;
        int all_taskTheme;
        int progress,task;

        Subcategory(String title, int position)
        {
            this.title=title;this.position=position;
        }
    }


    static class SubcategoriesAdapter extends RecyclerView.Adapter<Comment.SubcategoriesAdapter.ViewHolder> {

        List<Comment.Subcategory> data;

        SubcategoriesAdapter(List<Comment.Subcategory> data) {
            this.data = data;
        }


        @Override
        public Comment.SubcategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.subcategory_list_item_comment, parent, false);
            return new Comment.SubcategoriesAdapter.ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(final Comment.SubcategoriesAdapter.ViewHolder holder, final int position) {
            final Comment.Subcategory subcategory = data.get(position);
            holder.imageViewLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Animation animation = AnimationUtils.loadAnimation(thisContext, R.anim.bounce);

                    // amplitude 0.2 and frequency 20
//                    Task.BounceInterpolator interpolator = new Task.BounceInterpolator(0.2, 20);
                    animation.setInterpolator(interpolator);

                    holder.imageViewLike.startAnimation(animation);
                    holder.subcategoryLike.setText(String.valueOf(Integer.parseInt(holder.subcategoryLike.getText().toString())+1));
                    holder.imageViewLike.setImageResource(R.drawable.like);
                    final DocumentReference docRef3 = db.collection("task2").document(subcategory.title).collection("comments").document("task"+String.valueOf(subcategory.position));
                    docRef3.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if (documentSnapshot.exists()) {
                                    Map<String, Object> inf = documentSnapshot.getData();
                                    Map<String, Object> m = new HashMap<>();
                                    ArrayList arrayList= (ArrayList) inf.get("message"+String.valueOf(position));
                                    arrayList.set(3,arrayList.get(3)+String.valueOf(user.getUid()));
                                    m.put("message"+String.valueOf(position),arrayList);
                                    docRef3.set(m,SetOptions.merge());
                                }
                            }
                        }
                    });

                }
            });
            holder.reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editTextPost.setText(subcategory.name+", ");
                    editTextPost.setSelection(subcategory.name.length()+2);
                    editTextPost.setFocusable(true);
//                    editText.requestFocus();
//                    InputMethodManager imm = (InputMethodManager) contextThis.getSystemService(Activity.INPUT_METHOD_SERVICE);
//                    View view = new View(contextThis);
//                    imm.hideSoftInputFromWindow(view.getWindowToken(), 2);
                }
            });

            final DocumentReference docRef3 = db.collection("task2").document(subcategory.title).collection("comments").document("task"+String.valueOf(subcategory.position));
            docRef3.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()) {
                            Map<String, Object> inf = documentSnapshot.getData();
                            ArrayList arrayList=(ArrayList) inf.get("message"+String.valueOf(position));
                            subcategory.name=arrayList.get(0).toString();
                            holder.subcategoryName.setText(arrayList.get(0).toString());
                            holder.subcategoryText.setText(arrayList.get(1).toString());
                            holder.subcategoryLike.setText(arrayList.get(2).toString());
                        }
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return allComment;
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView subcategoryName,subcategoryText,subcategoryLike;
            ImageView imageViewLike;
            Button reply;

            ViewHolder(View itemView) {
                super(itemView);
                reply=itemView.findViewById(R.id.buttonReply);
                imageViewLike=itemView.findViewById(R.id.imageView9Comment);
                subcategoryLike=itemView.findViewById(R.id.textViewLikeComment);
                subcategoryName = itemView.findViewById(R.id.textViewNameComment);
                subcategoryText = itemView.findViewById(R.id.textViewTextComment);
            }
        }
    }

}
