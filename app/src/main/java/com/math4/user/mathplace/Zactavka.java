package com.math4.user.mathplace;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
//import com.math4.user.mathplace.Notifications.Client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;

import static com.math4.user.mathplace.Distrib.itemsCtnTheme;

public class Zactavka extends AppCompatActivity {
    ArrayList taskProgress=new ArrayList();
    FirebaseFirestore db;
    FirebaseAuth mAuth;
//    APIService apiService;
    FirebaseUser user;
    Map<String, ArrayList> MapTopic=new HashMap<>();
    static String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



//        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);



        //------------------------------------------
        mAuth= FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
//        sendNotifiaction(user,user.getUid().toString(),user.getUid().toString(),user.getUid().toString());
//        Crashlytics.getInstance().crash();

        db=FirebaseFirestore.getInstance();
        Log.e("checkcheck","getObject");
        if(user==null) {
            Intent intent=new Intent(this, Hello.class);
            startActivity(intent);
            finish();
        }else {

            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });
            // Узнаем размеры экрана из ресурсов
            DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
            Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                    .setLink(Uri.parse("https://www.mathplace.page.link"))
                    .setDomainUriPrefix("https://mathplace.page.link/")
                    .setSocialMetaTagParameters(
                            new DynamicLink.SocialMetaTagParameters.Builder()
                                    .setTitle("Example of a Dynamic Link")
                                    .setDescription("This link works whether the app is installed or not!")
                                    .build())
                    // Set parameters
                    // ...
                    .buildShortDynamicLink()
                    .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                        @Override
                        public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                            if (task.isSuccessful()) {
                                // Short link created
                                Distrib.shortLink = task.getResult().getShortLink();
                                Uri flowchartLink = task.getResult().getPreviewLink();
                            } else {
                                // Error
                                // ...
                            }
                        }
                    });


            FirebaseDynamicLinks.getInstance()
                    .getDynamicLink(getIntent())
                    .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                        @Override
                        public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                            // Get deep link from result (may be null if no link is found)
                            Uri deepLink = null;
                            if (pendingDynamicLinkData != null) {
                                deepLink = pendingDynamicLinkData.getLink();
//                                Toast.makeText(getApplicationContext(),String.valueOf(deepLink),Toast.LENGTH_LONG).show();
                            }


                            // Handle the deep link. For example, open the linked
                            // content, or apply promotional credit to the user's
                            // account.
                            // ...

                            // ...
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            Log.w(TAG, "getDynamicLink:onFailure", e);
                        }
                    });

// узнаем размеры экрана из класса Display
            Display display = getWindowManager().getDefaultDisplay();
            DisplayMetrics metricsB = new DisplayMetrics();
            display.getMetrics(metricsB);
            Distrib.widthScreen=displaymetrics.widthPixels;
            Distrib.heightScreen=displaymetrics.heightPixels;
            Log.e("checkcheckWidth",String.valueOf(Distrib.widthScreen));
            Log.e("checkcheckHeight",String.valueOf(Distrib.heightScreen));

            //----------------------------

            Distrib.allNameTheme.clear();
            Distrib.geometryNameTheme.clear();
            Distrib.ogeNameTheme.clear();
            Distrib.kombaNameTheme.clear();
            Distrib.schoolNameTheme.clear();
            Distrib.logikaNameTheme.clear();
            Distrib.graphNameTheme.clear();
            Distrib.ideaNameTheme.clear();


            //как мы получаем все темы по алгебре
            db.collection("task2")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                            if (task2.isSuccessful()) {
                                for (QueryDocumentSnapshot documentTask : task2.getResult()) {//здесь мы бежим по всем темам
//                                    Log.e("checkcheckDocumentTask",documentTask.getId());
                                    if((documentTask.getData().get("theme").toString()).equals("алгебра")){  //и выбираем те где в параметрах написано что это алгебра
                                        Distrib.algebraNameTheme.add(documentTask.getId().toString());//добавляем их в наш массив с темами по алгебре. Массив лежит в Distrib. все подобные массивы обязательно хранить в начале Distrib
                                    }
                                }
                            }
                        }
                    });
            //как мы получаем все темы по алгебре
            db.collection("task2")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                            if (task2.isSuccessful()) {
                                for (QueryDocumentSnapshot documentTask : task2.getResult()) {//здесь мы бежим по всем темам
                                    if((documentTask.getData().get("theme").toString()).equals("геометрия")){  //и выбираем те где в параметрах написано что это алгебра
                                        Distrib.geometryNameTheme.add(documentTask.getId().toString());//добавляем их в наш массив с темами по алгебре. Массив лежит в Distrib. все подобные массивы обязательно хранить в начале Distrib
                                    }
                                }
                            }
                        }
                    });
            //как мы получаем все темы по алгебре
            db.collection("task2")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                            if (task2.isSuccessful()) {
                                for (QueryDocumentSnapshot documentTask : task2.getResult()) {//здесь мы бежим по всем темам
                                    if((documentTask.getData().get("theme").toString()).equals("огэ")){  //и выбираем те где в параметрах написано что это алгебра
                                        Distrib.ogeNameTheme.add(documentTask.getId().toString());//добавляем их в наш массив с темами по алгебре. Массив лежит в Distrib. все подобные массивы обязательно хранить в начале Distrib
                                    }
                                }
                            }
                        }
                    });
            //как мы получаем все темы по алгебре
            db.collection("task2")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                            if (task2.isSuccessful()) {
                                for (QueryDocumentSnapshot documentTask : task2.getResult()) {//здесь мы бежим по всем темам
                                    if((documentTask.getData().get("theme").toString()).equals("комбинаторика")){  //и выбираем те где в параметрах написано что это алгебра
                                        Distrib.kombaNameTheme.add(documentTask.getId().toString());//добавляем их в наш массив с темами по алгебре. Массив лежит в Distrib. все подобные массивы обязательно хранить в начале Distrib
                                    }
                                }
                            }
                        }
                    });
            //как мы получаем все темы по алгебре
            db.collection("task2")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                            if (task2.isSuccessful()) {
                                for (QueryDocumentSnapshot documentTask : task2.getResult()) {//здесь мы бежим по всем темам
                                    if((documentTask.getData().get("theme").toString()).equals("логика")){  //и выбираем те где в параметрах написано что это алгебра
                                        Distrib.logikaNameTheme.add(documentTask.getId().toString());//добавляем их в наш массив с темами по алгебре. Массив лежит в Distrib. все подобные массивы обязательно хранить в начале Distrib
                                    }
                                }
                            }
                        }
                    });
            //как мы получаем все темы по алгебре
            db.collection("task2")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                            if (task2.isSuccessful()) {
                                for (QueryDocumentSnapshot documentTask : task2.getResult()) {//здесь мы бежим по всем темам
                                    if((documentTask.getData().get("theme").toString()).equals("графы")){  //и выбираем те где в параметрах написано что это алгебра
                                        Distrib.graphNameTheme.add(documentTask.getId().toString());//добавляем их в наш массив с темами по алгебре. Массив лежит в Distrib. все подобные массивы обязательно хранить в начале Distrib
                                    }
                                }
                            }
                        }
                    });
            //как мы получаем все темы по алгебре
            db.collection("task2")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                            if (task2.isSuccessful()) {
                                for (QueryDocumentSnapshot documentTask : task2.getResult()) {//здесь мы бежим по всем темам
                                    if((documentTask.getData().get("theme").toString()).equals("идеи")){  //и выбираем те где в параметрах написано что это алгебра
                                        Distrib.ideaNameTheme.add(documentTask.getId().toString());//добавляем их в наш массив с темами по алгебре. Массив лежит в Distrib. все подобные массивы обязательно хранить в начале Distrib
                                    }
                                }
                            }
                        }
                    });
            //как мы получаем все темы по алгебре
            db.collection("task2")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                            if (task2.isSuccessful()) {
                                for (QueryDocumentSnapshot documentTask : task2.getResult()) {//здесь мы бежим по всем темам
//                                    ArrayList arrayListMapTopic
                                    if((documentTask.getData().get("theme").toString()).equals("школа")){  //и выбираем те где в параметрах написано что это алгебра
                                        Distrib.schoolNameTheme.add(documentTask.getId().toString());//добавляем их в наш массив с темами по алгебре. Массив лежит в Distrib. все подобные массивы обязательно хранить в начале Distrib
                                    }
                                }
                            }
                        }
                    });
            db.collection("task2")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                            if (task2.isSuccessful()) {
                                for (final QueryDocumentSnapshot documentTask : task2.getResult()) {
                                    Distrib.allNameTheme.add(documentTask.getId().toString());
                                    itemsCtnTheme.put(documentTask.getId().toString(),((Long)documentTask.getData().get("cnt_task")).intValue());
//                                    final DocumentReference docRefPrev = db.collection("task").document(documentTask.getId());
//                                    final DocumentReference docRefNew2 = db.collection("task2").document(documentTask.getId());
//                                    docRefPrev.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                        @Override
//                                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
//                                            if (task.isSuccessful()) {
//                                                DocumentSnapshot documentSnapshot = task.getResult();
//                                                if (documentSnapshot.exists()) {
//                                                    Map<String, Object> inf = documentSnapshot.getData();
//                                                    Map<String, Object> inf2=new HashMap<>();
////                                                    inf2=inf;
//                                                    inf2.put("theme",inf.get("theme"));
//                                                    inf2.put("items",(long)(((Long)inf.get("all_task")).intValue()+1));
//                                                    inf2.put("cnt_task",inf.get("all_task"));
//                                                    inf2.put("class",inf.get("class"));
//                                                    inf2.put("like",inf.get("like"));
//                                                    ArrayList taskRegistr3=new ArrayList();
//                                                    taskRegistr3.add(inf.get("theory"));
//                                                    taskRegistr3.add("theory");
//                                                    taskRegistr3.add("null");
//                                                    taskRegistr3.add("null");
//                                                    inf2.put("task0",taskRegistr3);
//                                                    int all_task=((Long)inf.get("all_task")).intValue();
//                                                    for(int i=0;i<all_task;i++) {
////                                                        Log.e("checkcheckTask2",String.valueOf(i));
//                                                        ArrayList taskRegistr = new ArrayList();
//                                                        taskRegistr=(ArrayList) inf.get("task"+String.valueOf(i));
//                                                        ArrayList taskRegistr2=new ArrayList();
//                                                        taskRegistr2.add(taskRegistr.get(0));
//                                                        taskRegistr2.add(taskRegistr.get(1));
//                                                        taskRegistr2.add(taskRegistr.get(2));
//                                                        taskRegistr2.add(taskRegistr.get(3));
////                                                        taskRegistr.add("English version");
////                                                        taskRegistr.add("Треугольником Паскаля называют бесконечную треугольную таблицу чисел, у которой на вершине и по бокам стоят единицы, а каждое число внутри равно сумме двух стоящих над ним чисел. Так, например, третья строка треугольника (1,2,1) содержит два нечетных числа и одно четное. Треугольником Паскаля называют бесконечную треугольную таблицу чисел, у которой на вершине и по бокам стоят единицы, а каждое число внутри равно сумме двух стоящих над ним чисел. Так, например, третья строка треугольника (1,2,1) содержит два нечетных числа и одно четное.");
//                                                        inf2.put("task"+String.valueOf(i+1), taskRegistr2);
////                                                        if (documentTask.getId().equals("Четность")) {
////                                                        }
//                                                        docRefNew2.set(inf2);
//                                                    }
//                                                }
//                                            }
//                                        }
//                                    });
//                                    Log.e("checkcheckTheme",documentTask.getId().toString());
                                }
                                final DocumentReference docRef = db.collection("account").document(user.getUid());
                                if(docRef!=null&&db!=null) {
                                    Log.e("checkcheck", "Vxod prodolshaetsa "+user.getUid());
                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(Task<DocumentSnapshot> task) {
                                            Log.e("checkcheck", "Vxod Nachalo "+user.getUid());
                                            if (task.isSuccessful()) {
                                                Log.e("checkcheck", "Vxod Nachalo222222 "+user.getUid());
                                                DocumentSnapshot documentSnapshot = task.getResult();
                                                if (documentSnapshot.exists()) {
                                                    Log.e("checkcheck", "Vxod Nachalo99999999999 "+user.getUid());
                                                    Map<String, Object> inf = documentSnapshot.getData();
                                                    Distrib.allInf = inf;
                                                    userName=inf.get("name").toString();
                                                    Log.e("checkcheckAllNameTheme",String.valueOf(Distrib.allNameTheme.size()));
                                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });
            Log.e("checkcheck","user not null. Popitka vxoda");

        }
    }


}