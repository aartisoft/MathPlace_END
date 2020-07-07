package com.math4.user.mathplace;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
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
import static com.math4.user.mathplace.Topic.checkBookmark;
import static com.math4.user.mathplace.Topic.checkLike;
import static com.math4.user.mathplace.Topic.rewardedAd;
import static com.math4.user.mathplace.Topic.tema;
import static com.math4.user.mathplace.Topic.thisContext;
import static com.math4.user.mathplace.Topic.thisTheme;
import static com.math4.user.mathplace.Topic.type;

public class Task extends Fragment implements Animation.AnimationListener {
  static View view_task;
  int star;
  String task2,answer,title,solutionText;
  ImageView ImageViewStar1,ImageViewStar2,ImageViewStar3,imageHappy;
    LinearLayout linearLayoutTask;
  int position;
  Task con;
  Boolean r;
  static FirebaseFirestore db;
    FirebaseAuth mAuth;
//  TabLayout tabLayout;
    int position_for_title;
  EditText editText;

  private inf impl;
    static int allComment=0;
    static String title2;
    static int position2;
    static FirebaseUser user;
    TextView text_task,head;
    static int clickPositionTheme;
    private AdView mAdView;
    static Context contextActivity;
  static Button buttonSend;
  ImageButton imageButtonAns;
  ImageView imageViewSolv,imageViewBookmark;
  int number;
  String textBookmark, textLike;
  int check;
  Animation animation;
  ImageView imageViewLike;
  TextView textViewLike;
  static BounceInterpolator interpolator;
  Boolean evidence;
  int solution;
  Boolean checkLast;
  Boolean checkSolution=false;
  Button button;
  static EditText editTextPost;

  TextView textViewBookamrk,textViewSolv;
  public interface inf{
    public void data(String link, int position);
  }


    static List<Task.Subcategory> getRandomData() {
        final ArrayList<Task.Subcategory> subcategoryList = new ArrayList<>();
        for(int i=0;i<allComment;i++){
            subcategoryList.add(new Task.Subcategory(title2,position2));
            subcategoryList.add(new Task.Subcategory(title2,position2));
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
       view_task = inflater.inflate(R.layout.layout_task, container, false);
        Bundle bundle=getArguments();
        con=this;
      this.task2=bundle.getString("task");
      this.answer=bundle.getString("answer");
      this.star=bundle.getInt("star");
      this.check=bundle.getInt("check");
      this.solution=bundle.getInt("solution");
      this.solutionText=bundle.getString("solutionText");
      this.position=bundle.getInt("position");
      this.position_for_title=bundle.getInt("position_for_title");
      this.title=bundle.getString("thisTheme");
      this.checkLast=bundle.getBoolean("last");




//      AdView mAdView = view_task.findViewById(R.id.adViewTask);
//      AdRequest adRequest = new AdRequest.Builder().build();
//      mAdView.loadAd(adRequest);
//      ShowAdMob(mAdView);



      animation = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);

      // amplitude 0.2 and frequency 20
      interpolator = new BounceInterpolator(0.2, 20);
      animation.setInterpolator(interpolator);




//      Toast.makeText(getActivity(),"onCreateView",Toast.LENGTH_LONG).show();;




      //--------------------------------------------

      if(answer.equals("null")){
          this.evidence=true;
          view_task = inflater.inflate(R.layout.layout_evidence, container, false);
      }else{
          this.evidence=false;
          view_task = inflater.inflate(R.layout.layout_task, container, false);
          editText= view_task.findViewById(R.id.editTextAnswer);
          imageButtonAns=view_task.findViewById(R.id.buttonOpenAns);
      }
      //--------------------------------------------
      mAuth= FirebaseAuth.getInstance();
      user=mAuth.getCurrentUser();
      db=FirebaseFirestore.getInstance();
      final DocumentReference docRes=db.collection("account").document(user.getUid());



      View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_bottom_sheet, null);//Главные функции
      final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
      bottomSheetDialog.setContentView(view);

      final View viewComment = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_bottom_sheet_comment, null);//Комменты
      final BottomSheetDialog bottomSheetDialogComment = new BottomSheetDialog(getActivity());
      bottomSheetDialogComment.setContentView(viewComment);


      ImageButton openComment=view_task.findViewById(R.id.imageViewOpenComment);
      openComment.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              bottomSheetDialogComment.show();
              final DocumentReference docRefComment = db.collection("task2").document(title).collection("comments").document("task"+String.valueOf(position));
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
                              title2=title;
                              position2=position;
                              allComment=((Long)inf.get("all_message")).intValue();
                              RecyclerView categoriesView=viewComment.findViewById(R.id.categories_list_themeComment);
                              List<Task.Subcategory> CircleCategories = Task.getRandomData();
                              categoriesView.setHasFixedSize(true);
                              categoriesView.setLayoutManager(new LinearLayoutManager(
                                      contextActivity,
                                      RecyclerView.VERTICAL,
                                      false
                              ));
                              SubcategoriesAdapter adapter=new Task.SubcategoriesAdapter(CircleCategories);
//                              adapter.addComment();
                              categoriesView.setAdapter(adapter);

                          }
                      }
                  }
              });



          }
      });


      LinearLayout linearLayoutSolv=view.findViewById(R.id.linearLayoutSolv);
      textViewSolv=view.findViewById(R.id.textViewSolv);
      linearLayoutSolv.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              bottomSheetDialog.cancel();
              if (solution == 2) {
                  showSolution();
              } else {
                  showDialog2(IDD_THREE_BUTTONS);
              }
          }
      });
//      LinearLayout linearLayoutComment=view.findViewById(R.id.linearLayoutChat);
//      linearLayoutComment.setOnClickListener(new View.OnClickListener() {
//          @Override
//          public void onClick(View v) {
//              bottomSheetDialog.cancel();
//              Intent intent=new Intent(getActivity(),Comment.class);
//              intent.putExtra("text",task2);
//              intent.putExtra("title",title);
//              intent.putExtra("position",position);
//              startActivity(intent);
//          }
//      });
      LinearLayout linearLayoutDraft=view.findViewById(R.id.linearLayoutDraft);
      linearLayoutDraft.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              startActivity(new Intent(getActivity(),Draft.class));
          }
      });
      LinearLayout linearLayoutAddBookmark=view.findViewById(R.id.linearLayoutAddBookmark);
      imageViewBookmark=view.findViewById(R.id.imageViewBookmark);
      textViewBookamrk=view.findViewById(R.id.textViewBookmark);
      linearLayoutAddBookmark.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              docRes.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                  @Override
                  public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                      if(task.isSuccessful()){
                          DocumentSnapshot documentSnapshot=task.getResult();
                          if(documentSnapshot.exists()){
                              Map<String,Object> inf=documentSnapshot.getData();
                              ArrayList arrayBookmark=(ArrayList)inf.get("bookmark");
                              if(arrayBookmark.contains(thisTheme)){
                                  arrayBookmark.remove(thisTheme);
                                  ((ArrayList) Distrib.allInf.get("bookmark")).remove(thisTheme);
                                  imageViewBookmark.setImageResource(R.drawable.bookmark);
                                  checkBookmark=false;
                                  textViewBookamrk.setText("Добавить в избранное");
                                  Snackbar.make(getView(), "Тема удалена из избранных", Snackbar.LENGTH_LONG).show();
                              }else{
                                  imageViewBookmark.setImageResource(R.drawable.bookmark_full);
                                  textViewBookamrk.setText("Удалить из избранных");
                                  arrayBookmark.add(thisTheme);
                                  checkBookmark=true;
                                  ((ArrayList) Distrib.allInf.get("bookmark")).add(thisTheme);
                                  Snackbar.make(getView(), "Тема добавлена в избранные", Snackbar.LENGTH_LONG).show();
                              }
                              inf.put("bookmark",arrayBookmark);
                              docRes.set(inf);
                          }
                      }
                  }
              });

              bottomSheetDialog.cancel();
          }
      });
      final LinearLayout linearLayoutWebsite=view.findViewById(R.id.linearLayoutWebsite);
      linearLayoutWebsite.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://mathplace-842f7.web.app/")));
          }
      });
      final LinearLayout linearLayoutLike=view.findViewById(R.id.linearLayoutLikeTask);
      imageViewLike=view.findViewById(R.id.imageViewLike);
      textViewLike=view.findViewById(R.id.textViewTaskLike);
      linearLayoutLike.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              r=false;
              if(!((ArrayList) allInf.get("like")).contains(thisTheme)) {
                  imageViewLike.startAnimation(animation);
                  imageViewLike.setImageResource(R.drawable.like_full);
                  textViewLike.setText("Лайк поставлен\n");
              }else{
                  imageViewLike.setImageResource(R.drawable.like_empty);
                  imageViewLike.startAnimation(animation);
                  textViewLike.setText("Поставить лайк\nэтой теме");
              }
              final DocumentReference docRes=db.collection("account").document(user.getUid());
              docRes.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                  @Override
                  public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                      if(task.isSuccessful()){
                          DocumentSnapshot documentSnapshot=task.getResult();
                          if(documentSnapshot.exists()){
                              Map<String,Object> inf=documentSnapshot.getData();
                              ArrayList arrayLike=(ArrayList)inf.get("like");
                              if(!checkLike){
                                  arrayLike.add(thisTheme);
                                  checkLike=true;
                                  ((ArrayList) Distrib.allInf.get("like")).add(thisTheme);
                                  final DocumentReference docRes2=db.collection("task2").document(thisTheme);
                                  docRes2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                      @Override
                                      public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                                          if(task.isSuccessful()){
                                              DocumentSnapshot documentSnapshot=task.getResult();
                                              if(documentSnapshot.exists()){
                                                  Map<String,Object> inf=documentSnapshot.getData();
                                                  inf.put("like",((Long)inf.get("like")).intValue()+1);
                                                  docRes2.set(inf,SetOptions.merge());
                                              }
                                          }
                                      }
                                  });
                              }else{
                                  arrayLike.remove(thisTheme);
                                  ((ArrayList) Distrib.allInf.get("like")).remove(thisTheme);
                                  checkLike=false;
                                  final DocumentReference docRes2=db.collection("task").document(thisTheme);
                                  docRes2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                      @Override
                                      public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                                          if(task.isSuccessful()){
                                              DocumentSnapshot documentSnapshot=task.getResult();
                                              if(documentSnapshot.exists()){
                                                  Map<String,Object> inf=documentSnapshot.getData();
                                                  inf.put("like",((Long)inf.get("like")).intValue()+1);
                                                  docRes2.set(inf);
                                              }
                                          }
                                      }
                                  });
                              }
                              inf.put("like",arrayLike);
                              docRes.set(inf,SetOptions.merge());
                          }
                      }
                  }
              });




          }
      });

      imageViewSolv=view.findViewById(R.id.imageViewAns);
//
//// настройка возможности скрыть элемент при свайпе вниз
//      bottomSheetBehavior.setHideable(false);
      contextActivity=getActivity();
      linearLayoutTask=view_task.findViewById(R.id.linearLayoutTaskText);
      this.check=((Long) ((ArrayList<Long>) Distrib.allInf.get(title)).get(position)).intValue();
      solution=((Long) ((ArrayList<Long>) Distrib.allInf.get(title+"Solution")).get(position)).intValue();

       getObject();
       drawStar(star);
       mAuth= FirebaseAuth.getInstance();
       user=mAuth.getCurrentUser();

       head.setText("Задача "+String.valueOf(position_for_title));
       if(!this.evidence) {
           imageHappy.setVisibility(View.INVISIBLE);
       }
       setText(task2);
       final Context contextBottomSheet=getActivity();
       drawStar(star);
       if(!this.evidence) {
           imageButtonAns.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   bottomSheetDialog.show();
                   if(!checkLike){
                       textViewLike.setText("Поставить лайк\nэтой теме");
                       imageViewLike.setImageResource(R.drawable.like_empty);
                   }else{
                       textViewLike.setText("Лайк поставлен\n");
                       imageViewLike.setImageResource(R.drawable.like);
                   }
                   if(!checkBookmark){
                       textViewBookamrk.setText("Добавить в избранное");
                       imageViewBookmark.setImageResource(R.drawable.bookmark);
                   }else{
                       textViewBookamrk.setText("Удалить из избранных");
                       imageViewBookmark.setImageResource(R.drawable.bookmark_full);
                   }
               }
           });
       }
       final Button button=view_task.findViewById(R.id.send);
       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(evidence||check==2){
                   if(!checkLast) {
                       Topic.viewPager.setCurrentItem(position + 1, true);
                   }else{
                       getActivity().finish();
                   }
               }else{
                   final DocumentReference docRef = db.collection("account").document(user.getUid());
                   if(editText.getText().toString().equals("")){
                       Toast.makeText(getActivity(),"Введите ответ",Toast.LENGTH_SHORT).show();
                   }else {
                       if (answer.equals(editText.getText().toString())) {
                           //                       impl.data("Правильно",answer);
                           //                        Task thisTask=(Task)AllFragment.get(position);
                           check = 2;
                           //--------------------------------
                           ArrayList inf = (ArrayList) Distrib.allInf.get(title);
                           inf.set(position, 2L);
                           Distrib.allInf.put(title, inf);
                           Distrib.allInf.put("right",((Long) Distrib.allInf.get("right")).intValue()+1L);
                           Distrib.allInf.put("submit",((Long) Distrib.allInf.get("submit")).intValue()+1L);
                           if(Topic.prevTheme==1){
//                               String textPrev=((TextView) Something.itemViewPrev.findViewById(R.id.textViewTextMenu)).getText().toString();
//                               ((TextView) Something.itemViewPrev.findViewById(R.id.textViewTextMenu)).setText("Hello world!");
                           }else if(Topic.prevTheme==2){
                               ProgressBar progressBar3= Something.itemViewPrev.findViewById(R.id.progressBarTheme);
                               int rightPrev=0,all_taskPrev=((ArrayList) Distrib.allInf.get(thisTheme)).size();
                               ArrayList<Long> themePrev=(ArrayList) Distrib.allInf.get(thisTheme);
                               for(int i=0;i<themePrev.size();i++){
                                   if(themePrev.get(i)==2L){
                                       rightPrev++;
                                   }
                               }
                               progressBar3.setProgress(rightPrev*100/themePrev.size());
                           }

                           //устнавливаем изменения
                           imageHappy.setImageResource(R.drawable.smile);
                           if(checkLast) {
                               setPositionTrue("закончить");
                           }else {
                               setPositionTrue("продолжить");
                           }
                           Topic.tabLayout.getTabAt(position).setIcon(R.drawable.mark_true);




                           docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                               @Override
                               public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                                   if (task.isSuccessful()) {
                                       DocumentSnapshot documentSnapshot = task.getResult();
                                       if (documentSnapshot.exists()) {
                                           Map<String, Object> inf = documentSnapshot.getData();
                                           Map<String, Object> m = new HashMap<>();
                                           ArrayList taskRegistr = (ArrayList) inf.get(title);
                                           taskRegistr.set(position, 2L);
                                           inf.put(title, taskRegistr);
                                           docRef.set(inf);
                                           m.put("submit", (Long) inf.get("submit") + 1);
                                           m.put("right", (Long) inf.get("right") + 1);
                                           m.put("money",(Long) inf.get("money") + 3);
                                           m.put("achiv",updateAchiv());
                                           docRef.set(m, SetOptions.merge());
                                       }
                                   }
                               }
                           });
                           if(((Long) allInf.get("right")).intValue()==4){
                               showRate();
                           }

                           Toast.makeText(getActivity(),"+ 3 тугрика",Toast.LENGTH_LONG).show();

                           //-----------------------------------

                       } else {
                           //--------------------------------

                           ArrayList inf = (ArrayList) Distrib.allInf.get(title);
                           inf.set(position, 0L);
                           Distrib.allInf.put("submit",((Long) Distrib.allInf.get("submit")).intValue()+1L);
                           Distrib.allInf.put(title, inf);

                           docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                               @Override
                               public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                                   if (task.isSuccessful()) {
                                       DocumentSnapshot documentSnapshot = task.getResult();
                                       if (documentSnapshot.exists()) {
                                           Map<String, Object> inf = documentSnapshot.getData();
                                           ArrayList taskRegistr = (ArrayList) inf.get(title);
                                           taskRegistr.set(position, 0);
                                           Map<String, Object> m = new HashMap<>();
                                           m.put("submit", (Long) inf.get("submit") + 1);
                                           //                                     inf.put(title, taskRegistr);
                                           docRef.set(inf);
                                           docRef.set(m, SetOptions.merge());
                                       }
                                   }
                               }
                           });
                           //-----------------------------------

                           //устнавливаем изменения
                           imageHappy.setImageResource(R.drawable.unsmile);
                           Topic.tabLayout.getTabAt(position ).setIcon(R.drawable.mark_wrong);

                           //----------------------------------
                           //                       impl.data("Неправильно",answer);
                       }
                       Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_happy);
                       imageHappy.startAnimation(animation);
                   }
               }
         }
       });
      if(this.check==2||evidence){
          if(checkLast) {
              setPositionTrue("закончить");
          }else{
              setPositionTrue("продолжить");
          }
      }else{
          Log.e("checkcheckButtonStart",String.valueOf(check)+" "+String.valueOf(position)+" GOOD");
          setPositionNorm();
      }
       return view_task;
  }
  @Override
  public void onAttach(Context context) {
    super.onAttach(context);

    if (context instanceof inf) {
      impl = (inf) context;
    } else {
      throw new ClassCastException(context.toString()
              + " must implement MyListFragment.OnItemSelectedListener");
    }
  }


  public  void drawStar(int star){
      ImageViewStar1.setImageDrawable(getResources().getDrawable(R.drawable.star_evidence));
      ImageViewStar2.setImageDrawable(getResources().getDrawable(R.drawable.star_evidence));
      ImageViewStar3.setImageDrawable(getResources().getDrawable(R.drawable.star_evidence));
      if(star==2){
          ImageViewStar3.setImageDrawable(getResources().getDrawable(R.drawable.none));
      }else if(star==1){
          ImageViewStar2.setImageDrawable(getResources().getDrawable(R.drawable.none));
          ImageViewStar3.setImageDrawable(getResources().getDrawable(R.drawable.none));
      }
  }


    @Override
    public void onStart() {
        super.onStart();
//        Toast.makeText(getActivity(),"onStart",Toast.LENGTH_LONG).show();;
    }

    @Override
    public void onResume() {
        super.onResume();
//        Toast.makeText(getActivity(),"onResume "+String.valueOf(position),Toast.LENGTH_LONG).show();;
        Topic.positionTopic=position;
        if(!this.answer.equals("null")){
            if(this.solution==1){
                imageViewSolv.setImageResource(R.drawable.lock);
            }else{
                imageViewSolv.setImageResource(R.drawable.lock_opened);
            }
        }

        DocumentReference docRef=db.collection("task2").document(thisTheme);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Map<String, Object> inf2 = documentSnapshot.getData();
                        if (((ArrayList) inf2.get("task" + String.valueOf(position))).get(3).equals("null")) {
                            textViewSolv.setText("Открыть ответ");
                        } else {
                            textViewSolv.setText("Открыть решение");
                        }
                    }
                }
            }
        });

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
//  public static void setButton(){
//      Log.e("proverka","1");
//      buttonSend=view_task.findViewById(R.id.send);
//      buttonSend.setText("Продолжить");
//      buttonSend.getLayoutParams().width=150;
//  }
    @Override
    public void onAnimationStart(Animation animation) {
        imageHappy.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        imageHappy.setVisibility(View.INVISIBLE);
    }
    @Override
    public void onAnimationRepeat(Animation animation) {
//        mImage.setVisibility(View.VISIBLE);
    }
    public void setPositionTrue(String text){
        //------------------------ устанавливаем кнопку, решенной задачи
        Log.e("checkcheckButton","good");
        ViewGroup.LayoutParams params = button.getLayoutParams();
        params.width = 530; // или в пикселях
        button.setLayoutParams(params);
        button.setText(text);
        //-------------------------

        //------------------------- устанавливаем недоступный пользователю editText
        if(!evidence) {
            editText.setEnabled(false);
            editText.setCursorVisible(false);
            editText.setKeyListener(null);
//        editText.setBackgroundColor(Color.parseColor("#FFFFFF"));
            editText.setText(String.valueOf(this.answer));
            editText.setTextSize(25);
            editText.setTextColor(Color.parseColor("#2AD558"));
            //text_task.setBackgroundColor(Color.parseColor("#bdf4b7"));
        }



        //-------------------------

        //устанвливаем кнопку
//        button.setEnabled(false);
    }

    static void showDrafrTask(){
        Intent intent=new Intent(contextActivity,Draft.class);
//        if(position==0){
//            intent.putExtra("text", (com.example.user.mathplace.Topic.tema.get("theory").toString()));
//        }else {
//            intent.putExtra("text", ((ArrayList) com.example.user.mathplace.Topic.tema.get("task" + String.valueOf(position - 1))).get(0).toString());
//        }
        contextActivity.startActivity(intent);
    }


    public void setPositionNorm(){
      Log.e("checkcheckButtonFalse","GOOD");
        Button button=(Button)view_task.findViewById(R.id.send);
        //------------------------ устанавливаем кнопку, решенной задачи
        ViewGroup.LayoutParams params = button.getLayoutParams();
        params.width = 400; // или в пикселях
        button.setLayoutParams(params);
        button.setText("Ответить");
        //-------------------------

        //------------------------- устанавливаем недоступный пользователю editText
        if(!this.evidence) {
            editText.setEnabled(true);
            editText.setCursorVisible(true);
//        editText.setBackgroundColor(Color.parseColor("#FFFFFF"));
            editText.setText("");
            editText.setTextSize(23);
            editText.setTextColor(Color.parseColor("#5B6175"));
            //text_task.setBackgroundColor(Color.parseColor("#f3acac"));
        }

        //-------------------------

        //устанвливаем кнопку
//        button.setEnabled(false);
    }
    public void getObject(){
        db=FirebaseFirestore.getInstance();
        button=(Button)view_task.findViewById(R.id.send);
        if(!this.evidence) {
            editText = view_task.findViewById(R.id.editTextAnswer);
            imageHappy=view_task.findViewById(R.id.imageViewHappy);
        }
        head=view_task.findViewById(R.id.textViewHead);
        ImageViewStar1=view_task.findViewById(R.id.star3);
        ImageViewStar2=view_task.findViewById(R.id.star1);
        ImageViewStar3=view_task.findViewById(R.id.star2);
    }
    private final int IDD_THREE_BUTTONS = 0 , IDD_LISTVIEW_BUTTON=1,IDD_ALERTDIALOG=2;
    public void showDialog2(int id){
        final Context thisC=getActivity();
        switch (id) {
            case IDD_THREE_BUTTONS:
                final DocumentReference docRes2=db.collection("account").document(user.getUid());
                docRes2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if(documentSnapshot.exists()) {
                                final Map<String, Object> inf = documentSnapshot.getData();
                                final int money = ((Long) inf.get("money")).intValue();
                                DocumentReference docRef=db.collection("task2").document(thisTheme);
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot documentSnapshot = task.getResult();
                                            if (documentSnapshot.exists()) {
                                                Map<String, Object> inf2 = documentSnapshot.getData();
                                                String textDialog;
                                                if(((ArrayList)inf2.get("task"+String.valueOf(position))).get(3).equals("null")){
                                                    textDialog="Открыть ответ";
                                                }else {
                                                    textDialog="Открыть решение";
                                                }
                                                new AlertDialog.Builder(thisC, R.style.AlertDialogTheme)
                                                        .setTitle(textDialog)   
                                                        .setMessage("\nУ вас есть "+String.valueOf(money)+" тугриков\nПосмотрев рекламу вы не потратите свои тугрики")

                                                        .setCancelable(true)
                                                        .setNeutralButton("Заплатить 20 тугриков",
                                                                new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                //                                                        dialog.cancel();
                                                                        if(money<20){
                                                                            Toast.makeText(getActivity(),"У вас недостаточно тугриков",Toast.LENGTH_LONG).show();
                                                                        }else{
                                                                            Map<String, Object> m = new HashMap<>();
                                                                            m.put("money", (Long) inf.get("money") - 20);
                                                                            showSolution();
                                                                            docRes2.set(m, SetOptions.merge());
                                                                        }
                                                                    }
                                                                })
                                                        .setPositiveButton("Посмотреть рекламу",
                                                                new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        if (rewardedAd.isLoaded()) {
                                                                            RewardedAdCallback adCallback = new RewardedAdCallback() {
                                                                                @Override
                                                                                public void onRewardedAdOpened() {
//                                                                                     Ad opened.
//                                                                                    Toast.makeText(getActivity(), "Один", Toast.LENGTH_LONG).show();
                                                                                    checkSolution=false;
                                                                                }

                                                                                @Override
                                                                                public void onRewardedAdClosed() {
                                                                                    // Ad closed.
                                                                                    if(checkSolution){
                                                                                        showSolution();
                                                                                    }
//                                                                                    Toast.makeText(getActivity(), "Два", Toast.LENGTH_LONG).show();
                                                                                    rewardedAd = createAndLoadRewardedAd();
                                                                                }

                                                                                @Override
                                                                                public void onUserEarnedReward(@NonNull RewardItem reward) {
                                                                                    // User earned reward.
//                                                                                    Toast.makeText(getActivity(), "Три", Toast.LENGTH_LONG).show();
                                                                                    checkSolution=true;
                //                                                                    Toast.makeText(getActivity(), "ПОздравляю", Toast.LENGTH_LONG).show();
                                                                                }

                                                                                @Override
                                                                                public void onRewardedAdFailedToShow(int errorCode) {
//                                                                                    Toast.makeText(getActivity(), "Четрые", Toast.LENGTH_LONG).show();
                                                                                    // Ad failed to display.
                                                                                }
                                                                            };
                                                                            rewardedAd.show(getActivity(), adCallback);
                                                                        } else {
                //                                                            Toast.makeText(getActivity(), "Реклама не загрузилась.",Toast.LENGTH_LONG).show();
                                                                        }
                                                                        dialog.cancel();
                                                                    }
                                                                })
                                                        .setIcon(R.drawable.lock)
                                                        .show();
                                                            }
                                                        }
                                    }
                                });
                            }
                        }
                    }});
            default:
        }
    }
    public RewardedAd createAndLoadRewardedAd() {
        rewardedAd = new RewardedAd(getActivity(),
                "ca-app-pub-3206990026084887/5708000678");
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
//                Toast.makeText(getActivity(),"Рекалмам загружена",Toast.LENGTH_LONG).show();;
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                // Ad failed to load.
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
        return rewardedAd;
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

    static class SubcategoriesAdapter extends RecyclerView.Adapter<Task.SubcategoriesAdapter.ViewHolder> {

        static List<Task.Subcategory> data;

        SubcategoriesAdapter(List<Task.Subcategory> data) {
            this.data = data;
        }


        @Override
        public Task.SubcategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.subcategory_list_item_comment, parent, false);
            return new Task.SubcategoriesAdapter.ViewHolder(view);
        }

        public void addComment(){
//            save
//            data.remove(position);
            Toast.makeText(contextActivity,"AddComment", Toast.LENGTH_LONG).show();
            allComment++;
            data.add(new Task.Subcategory(title2,position2));
            notifyItemChanged(data.size()-1);
//            notifyItemRemoved(position);
//            notifyItemRangeChanged(position, data.size());
        }

        @Override
        public void onBindViewHolder(final Task.SubcategoriesAdapter.ViewHolder holder, final int position) {
            final Task.Subcategory subcategory = data.get(position);
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




    public void showSolution(){
        solution=2;
        ArrayList infSolv = (ArrayList) Distrib.allInf.get(title+"Solution");
        infSolv.set(position, 2L);
        Distrib.allInf.put(title+"Solution", infSolv);
        final DocumentReference docRef = db.collection("account").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Map<String, Object> inf = documentSnapshot.getData();
                        ArrayList taskRegistr = (ArrayList) inf.get(title+"Solution");
                        taskRegistr.set(position, 2L);
                        inf.put(title+"Solution", taskRegistr);
                        docRef.set(inf);
                    }
                }
            }
        });

//        imageButtonAns.setImageResource(R.drawable.lock_opened);
        imageViewSolv.setImageResource(R.drawable.lock_opened);
        Intent intent=new Intent(getActivity(),Solution.class);
        intent.putExtra("name",title);
        intent.putExtra("text_task",task2);
        intent.putExtra("position",position);
        intent.putExtra("answer",answer);
        intent.putExtra("solution",solutionText);
        startActivity(intent);
    }

    String[] textAchiv={"Решить 1 задачу","Решите 5 задачи","Решите 25 задач","Регите 100 задач","Решите целиком 1 тему","Решите целиком 3 темы","Решите целиком всю алгебру","Решите целиком всю гемметрию"};

    public ArrayList updateAchiv(){
        ArrayList taskRegistr = (ArrayList) Distrib.allInf.get("achiv");
        ArrayList taskRegistrStart=new ArrayList();
        for(int i=0;i<taskRegistr.size();i++){
            taskRegistrStart.add(taskRegistr.get(i));
        }
        if(((Long)taskRegistr.get(0)).intValue()!=100){
            taskRegistr.set(0,100L);
        }
        if(((Long)taskRegistr.get(1)).intValue()!=100){
            taskRegistr.set(1,(Long)taskRegistr.get(1)+20L);
        }
        if(((Long)taskRegistr.get(2)).intValue()!=100){
            taskRegistr.set(2,(Long)taskRegistr.get(2)+4L);
        }
        if(((Long)taskRegistr.get(3)).intValue()!=100){
            taskRegistr.set(3,(Long)taskRegistr.get(3)+1L);
        }
        ArrayList taskRes=(ArrayList) Distrib.allInf.get(thisTheme);
        Boolean checkTheme=true;
        int cntTrue=0;
        for(int i=0;i<taskRes.size();i++){
            if(((Long)taskRes.get(i)).intValue()==2){
                cntTrue++;
            }
        }
        if(cntTrue==((Long)tema.get("cnt_task")).intValue()) {
            if (((Long)taskRegistr.get(4)).intValue()!=1) {
                taskRegistr.set(4, (Long) taskRegistr.get(4) + 1L);
            }
            if (((Long)taskRegistr.get(5)).intValue()!=3) {
                taskRegistr.set(5, (Long) taskRegistr.get(5) + 1L);
            }
            if (Distrib.algebraNameTheme.contains(thisTheme)) {
                taskRegistr.set(6, (Long) taskRegistr.get(6) + 1L);
            }
            if (Distrib.geometryNameTheme.contains(thisTheme)) {
                taskRegistr.set(7, (Long) taskRegistr.get(7) + 1L);
            }

        }

        for(int i=0;i<taskRegistr.size();i++){
            Log.e("checkcheckShowAchiv",String.valueOf(taskRegistr.get(i))+" "+(taskRegistrStart.get(i)));
            if(((Long)taskRegistr.get(i)).intValue()==100&&((Long)taskRegistrStart.get(i)).intValue()!=100) {
                showAchiv("Ты выполнил достижение:"+"\n\""+textAchiv[i]+"\"\n"+"и заработал 15 тугриков");
            }else if(i==4&&((Long)taskRegistr.get(i)).intValue()==1&&((Long)taskRegistrStart.get(i)).intValue()!=1){
                showAchiv("Ты выполнил достижение:"+"\n\""+textAchiv[i]+"\"\n"+"и заработал 15 тугриков");
            }else if(i==5&&((Long)taskRegistr.get(i)).intValue()==3&&((Long)taskRegistrStart.get(i)).intValue()!=3){
                showAchiv("Ты выполнил достижение:"+"\n\""+textAchiv[i]+"\"\n"+"и заработал 15 тугриков");
            }else if(i==6&&((Long)taskRegistr.get(i)).intValue()== Distrib.algebraNameTheme.size()&&((Long)taskRegistrStart.get(i)).intValue()!= Distrib.algebraNameTheme.size()){
                showAchiv("Ты выполнил достижение:"+"\n\""+textAchiv[i]+"\"\n"+"и заработал 15 тугриков");
            }else if(i==7&&((Long)taskRegistr.get(i)).intValue()== Distrib.geometryNameTheme.size()&&((Long)taskRegistrStart.get(i)).intValue()!= Distrib.geometryNameTheme.size()){
                showAchiv("Ты выполнил достижение:"+"\n\""+textAchiv[i]+"\"\n"+"и заработал 15 тугриков");
            }
        }

        Distrib.allInf.put("achiv",taskRegistr);
        return taskRegistr;


//        inf.put("achiv", taskRegistr);

    }

    public void showAchiv(String text){
        final DocumentReference docRef = db.collection("account").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Map<String, Object> inf = documentSnapshot.getData();
                        Map<String, Object> m = new HashMap<>();
                        m.put("money",(Long) inf.get("money") + 15);
                        docRef.set(m, SetOptions.merge());
                    }
                }
            }
        });
        new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme)
                .setTitle("  Поздравляем!    ")
                .setMessage(text)

                .setPositiveButton("Круто", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent=new Intent(getActivity(),Topic.class);
//                        intent.putExtra("thisTheme",((TextView)view).getText().toString());
//                        setLastTheme(((TextView)view).getText().toString());
//                        textViewStudy.setText(lastTheme);
//                        textLoad.setVisibility(View.VISIBLE);
//                        startActivity(intent);
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setIcon(R.drawable.smallachivon)
                .show();

    }


    public void showRate(){
        final Dialog dialog = new Dialog(getActivity(),R.style.DialogStyle);
        dialog.setContentView(R.layout.dialog_rate);
//        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
//                WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();

        RatingBar ratingBar=dialog.findViewById(R.id.ratingbarTask);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(rating>=4){
                    dialog.hide();
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.math4.user.mathplace")));
                }else{
                    dialog.hide();
                    Toast.makeText(getActivity(),"Спасибо!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void addImage(String url){
        Log.e("checkcheckUri",url);
        final ImageView imageView=new ImageView(getActivity());
        RelativeLayout.LayoutParams MyParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        MyParams.leftMargin=8;
        MyParams.rightMargin=8;
        imageView.setLayoutParams(MyParams);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(getActivity()).load(url).into(imageView);
        linearLayoutTask.addView(imageView);
    }
    public void addText(String text){
        Log.e("checkcheckAddText",text);
        final TextView textView=new TextView(getContext());
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

}