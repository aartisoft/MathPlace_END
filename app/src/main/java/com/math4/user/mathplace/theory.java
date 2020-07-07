package com.math4.user.mathplace;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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

import static com.math4.user.mathplace.Distrib.allInf;
import static com.math4.user.mathplace.Topic.thisContext;
import static com.math4.user.mathplace.Topic.thisTheme;
import static com.math4.user.mathplace.Topic.type;

public class theory extends Fragment {
  // OnDataPass dataPasser;
  String text;
  int position;
  TextView textView;
    LinearLayout linearLayout;
    static FirebaseAuth mAuth;
    static int allComment=0;
    static Context contextActivity;
    static FirebaseUser user;
    static String title2;
    static int position2;
    static EditText editTextPost;
    Animation animation;
    static theory.BounceInterpolator interpolator;
    static  FirebaseFirestore db;




    static List<theory.Subcategory> getRandomData() {
        final ArrayList<theory.Subcategory> subcategoryList = new ArrayList<>();
        for(int i=0;i<allComment;i++){
            subcategoryList.add(new theory.Subcategory(title2,position2));
            subcategoryList.add(new theory.Subcategory(title2,position2));
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
  // Переопределяем метод onCreateView
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.activity_theory, container, false);
      Bundle arguments=getArguments();
      this.text=arguments.getString("text").toString();
      this.position=arguments.getInt("position");
      linearLayout=view.findViewById(R.id.textTheory);


        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);

        // amplitude 0.2 and frequency 20
        interpolator = new theory.BounceInterpolator(0.2, 20);
        animation.setInterpolator(interpolator);

      contextActivity=getActivity();


      mAuth= FirebaseAuth.getInstance();
      user=mAuth.getCurrentUser();
      db= FirebaseFirestore.getInstance();




        final View viewComment = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_bottom_sheet_comment, null);//Комменты
        final BottomSheetDialog bottomSheetDialogComment = new BottomSheetDialog(getActivity());
        bottomSheetDialogComment.setContentView(viewComment);


        ImageButton openComment=view.findViewById(R.id.imageViewOpenComment);
        openComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialogComment.show();
                final DocumentReference docRefComment = db.collection("task2").document(thisTheme).collection("comments").document("task"+String.valueOf(position));
                editTextPost=viewComment.findViewById(R.id.editTestPost);
                final Button buttonPost=viewComment.findViewById(R.id.post);
                editTextPost.addTextChangedListener(new TextWatcher(){

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count){
//                      Toast.makeText(getActivity(),String.valueOf(count),Toast.LENGTH_LONG).show();
                        if(!s.toString().equals("")){
                            buttonPost.setBackgroundResource(R.drawable.btn_rounded_corner);
                        }else{
                            buttonPost.setBackgroundResource(R.drawable.btn_rounded_corner_gray);
                        }
                        // действия, когда вводится какой то текст
                        // s - то, что вводится, для преобразования в строку - s.toString()
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
//                      Toast.makeText(getActivity(),"djdds",Toast.LENGTH_LONG).show();

                        // действия после того, как что то введено
                        // editable - то, что введено. В строку - editable.toString()
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                      Toast.makeText(getActivity(),"djdds",Toast.LENGTH_LONG).show();

                        // действия перед тем, как что то введено
                    }
                });



                buttonPost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!editTextPost.getText().toString().equals("")) {
                            final Map<String, Object> m = new HashMap<>();
//                          editTextPost.setFocusable(false);
                            if(docRefComment==null) {
                                m.put("all_message", 0);
                                docRefComment.set(m, SetOptions.merge());
                            }
                            docRefComment.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getActivity(),"Опубликовано",Toast.LENGTH_LONG).show();
                                        DocumentSnapshot documentSnapshot = task.getResult();
                                        if (documentSnapshot.exists()) {
                                            final Map<String, Object> m = documentSnapshot.getData();
                                            ArrayList msg = new ArrayList<>();
                                            msg.add(allInf.get("name"));
                                            msg.add(editTextPost.getText().toString());
                                            editTextPost.setText("");
                                            msg.add(0L);
                                            msg.add("|");
                                            msg.add(String.valueOf(user.getUid()));
                                            if (m.get("all_message") != null) {
                                                Long all = Long.parseLong(m.get("all_message").toString());
                                                all++;
                                                m.put("all_message", all);
                                                m.put("message" + String.valueOf(all - 1), msg);
                                            } else {
                                                m.put("all_message", 1L);
                                                m.put("message0", msg);
                                            }
                                            docRefComment.set(m, SetOptions.merge());
                                        }else{
                                            final Map<String, Object> m = new HashMap<>();
                                            ArrayList msg = new ArrayList<>();
                                            msg.add(allInf.get("name"));
                                            msg.add(editTextPost.getText().toString());
                                            editTextPost.setText("");
                                            msg.add(0L);
                                            msg.add("|");
                                            msg.add(String.valueOf(user.getUid()));
                                            m.put("all_message", 1L);
                                            m.put("message0", msg);
                                            docRefComment.set(m, SetOptions.merge());
                                        }
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(getActivity(),"Введеите комментарий",Toast.LENGTH_LONG).show();
                        }
                        bottomSheetDialogComment.cancel();
                    }
                });


                final DocumentReference docRefComment2=db.collection("task2").document(thisTheme).collection("comments").document("task"+String.valueOf(position));
                docRefComment2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                Map<String, Object> inf = documentSnapshot.getData();
                                title2=thisTheme;
                                position2=position;
                                allComment=((Long)inf.get("all_message")).intValue();
                                RecyclerView categoriesView=viewComment.findViewById(R.id.categories_list_themeComment);
                                List<theory.Subcategory> CircleCategories = theory.getRandomData();
                                categoriesView.setHasFixedSize(true);
                                categoriesView.setLayoutManager(new LinearLayoutManager(
                                        contextActivity,
                                        RecyclerView.VERTICAL,
                                        false
                                ));
                                theory.SubcategoriesAdapter adapter=new theory.SubcategoriesAdapter(CircleCategories);
//                              adapter.addComment();
                                categoriesView.setAdapter(adapter);

                            }
                        }
                    }
                });



            }
        });




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
      Button button=view.findViewById(R.id.buttonNextTheory);
      button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Topic.viewPager.setCurrentItem(position+1,true);
        }
      });
      Log.e("checkcheckAddFinishTheo","GOOD");
      return view;
  }


    static class Subcategory {
        String title,name;
        int position;
        boolean like;
        int all_taskTheme;
        int progress,task;

        Subcategory(String title, int position)
        {
            this.title=title;this.position=position;
        }
    }

    static class SubcategoriesAdapter extends RecyclerView.Adapter<theory.SubcategoriesAdapter.ViewHolder> {

        static List<theory.Subcategory> data;

        SubcategoriesAdapter(List<theory.Subcategory> data) {
            this.data = data;
        }


        @Override
        public theory.SubcategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.subcategory_list_item_comment, parent, false);
            return new theory.SubcategoriesAdapter.ViewHolder(view);
        }

        public void addComment(){
//            save
//            data.remove(position);
            Toast.makeText(contextActivity,"AddComment", Toast.LENGTH_LONG).show();
            allComment++;
            data.add(new theory.Subcategory(title2,position2));
            notifyItemChanged(data.size()-1);
//            notifyItemRemoved(position);
//            notifyItemRangeChanged(position, data.size());
        }

        @Override
        public void onBindViewHolder(final theory.SubcategoriesAdapter.ViewHolder holder, final int position) {
            final theory.Subcategory subcategory = data.get(position);
            holder.imageViewLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Animation animation = AnimationUtils.loadAnimation(thisContext, R.anim.bounce);


                    // amplitude 0.2 and frequency 20
//                    Task.BounceInterpolator interpolator = new Task.BounceInterpolator(0.2, 20);
                    animation.setInterpolator(interpolator);

                    holder.imageViewLike.startAnimation(animation);
                    if(!subcategory.like){
                        subcategory.like=true;
                        holder.imageViewLike.setImageResource(R.drawable.like);
                        holder.subcategoryLike.setText(String.valueOf(Integer.parseInt(holder.subcategoryLike.getText().toString())+1));
                    }else{
                        subcategory.like=false;
                        holder.imageViewLike.setImageResource(R.drawable.like_empty);
                        holder.subcategoryLike.setText(String.valueOf(Integer.parseInt(holder.subcategoryLike.getText().toString())-1));
                    }
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
                                    if(subcategory.like) {
                                        arrayList.set(3, arrayList.get(3) + String.valueOf(user.getUid()) + "|");
                                        arrayList.set(2, (Long) arrayList.get(2) + 1);
                                    }else{
//                                        String text=
                                        arrayList.set(3, ((String)arrayList.get(3)).replaceAll(user.getUid().toString(),"").toString());
                                        arrayList.set(2, (Long) arrayList.get(2) - 1);
                                    }
                                    m.put("message"+String.valueOf(position),arrayList);
                                    docRef3.set(m, SetOptions.merge());
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
                            String name_like=arrayList.get(3).toString();
//                            Toast.makeText(contextActivity,name_like,Toast.LENGTH_LONG).show();
                            if(name_like.contains(user.getUid().toString())){
                                subcategory.like=true;
                                holder.imageViewLike.setImageResource(R.drawable.like);
                            }else{
                                subcategory.like=false;
                                holder.imageViewLike.setImageResource(R.drawable.like_empty);
                            }
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





    public void addImage(String url){
      Log.e("checkcheckUri",url);
      final ImageView imageView=new ImageView(getContext());
      RelativeLayout.LayoutParams MyParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT    );
      MyParams.leftMargin=8;
      MyParams.rightMargin=8;
      imageView.setLayoutParams(MyParams);
      imageView.setScaleType(ImageView.ScaleType.FIT_XY);
      Glide.with(getActivity()).load(url).into(imageView);
      linearLayout.addView(imageView);
  }
    public void addText(String text){
      Log.e("checkcheckAddText",text);
        final TextView textView=new TextView(getContext());
        RelativeLayout.LayoutParams MyParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        MyParams.topMargin=16;
        MyParams.leftMargin=16;
        MyParams.rightMargin=8;
        MyParams.bottomMargin=8;
        textView.setLayoutParams(MyParams);
        textView.setText(text);
        textView.setLinkTextColor(Color.parseColor("#4D43BD"));
        textView.setLinksClickable(true);
        Linkify.addLinks(textView,Linkify.WEB_URLS);
        textView.setTypeface(type);
        textView.setTextSize(20);
        textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextColor(Color.parseColor("#5B6175"));
        linearLayout.addView(textView);
    }
}