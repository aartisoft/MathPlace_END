package com.math4.user.mathplace;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

import static com.math4.user.mathplace.Menu.LikeInf;


//import static com.example.user.mathplace.Task.rewardedAd;


//ca-app-pub-3206990026084887~4688381367
//ca-app-pub-3206990026084887~4688381367
public class OlympTopic extends AppCompatActivity {
    View activity;
    static boolean checkLike,checkBookmark;
    static String s,s22;
    static TextView textView,toolbar;
    static EditText editText;
    static Context thisContext;
    static ImageView image_bottom,star1,star2,star3;
    Boolean bookmark_param,like_param;
    static RewardedAd rewardedAd;
    static String task[][];
    FirebaseAuth mAuth;
    static FirebaseFirestore db;
    ImageButton imageButtonBookmark,imageButtonLike;
    String name, topic;
    String like="Поставить лайк",bookmark="Добавить в избранные";
    Button send_answer;
    static int positionTopic=0;
    ImageButton openAnswer;
    static ViewPager viewPager;
//    private RewardedAd rewardedAd;
    //----------------------- для базы данных
    static FirebaseUser user;
    static TabLayout tabLayout;
    static String thisTheme="Четность";
    static Map<String,Object> tema;
    static OlympTask fragmentSend;
    static ArrayList task_1;
    static String tokenTheme;
    static ArrayList AllFragment=new ArrayList();
    static ArrayList taskProgress=new ArrayList();
    static ArrayList<Integer> taskPosition=new ArrayList();
    static ArrayList taskSolution=new ArrayList();
    //----------------------

    public static class MyAdapter extends FragmentPagerAdapter {

        final int PAGE_COUNT = 5;
        private Context context;
        MyAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            int cnt=0;
            while(taskProgress==null&&cnt<1e99){cnt++; }
            if(taskProgress!=null) {
                Log.e("checkcheckCount",String.valueOf(taskProgress.size()));
                return taskProgress.size();
            }else{
                Toast.makeText(thisContext,"Не удалось загрузить тему",Toast.LENGTH_LONG).show();
                return 0;
            }
        }


        @NonNull
        @Override
        public Fragment getItem(int position) {
            Log.e("checkcheckItem","EEE "+String.valueOf(position));
            if(tema==null){
                Log.e("checkcheckTema","Null");
            }else{
                Log.e("checkcheckTema","Not Null");
            }
            task_1 = (ArrayList) tema.get("task" + String.valueOf(position));
            if (String.valueOf(task_1.get(1)).equals("theory")) {
                OlympTheory fragmentSend2 = new OlympTheory();
                Bundle bundle = new Bundle();
                bundle.putString("text", (task_1.get(0).toString()));
                bundle.putString("token", tokenTheme);
                bundle.putInt("position", position);
                fragmentSend2.setArguments(bundle);
                return fragmentSend2;
            } else {
                Log.e("checkcheckTaskTopic","New Task");
                fragmentSend = new OlympTask();
                Bundle bundle = new Bundle();
                bundle.putString("task", String.valueOf(task_1.get(0)));
                bundle.putString("answer", String.valueOf(task_1.get(1)));
                bundle.putString("solutionText", String.valueOf(task_1.get(3)));
                bundle.putString("token",tokenTheme);
                bundle.putInt("check", ((Long) ((ArrayList<Long>) Distrib.allInf.get(tokenTheme)).get(position)).intValue());
                bundle.putInt("solution", ((Long) ((ArrayList<Long>) Distrib.allInf.get(tokenTheme + "Solution")).get(position)).intValue());
                bundle.putInt("star", Integer.parseInt(task_1.get(2).toString()));
                bundle.putInt("position", position);
                Log.e("checkcheckTaskTopic","Middle");
                bundle.putInt("position_for_title", taskPosition.get(position));
                if (position == ((ArrayList<Long>) Distrib.allInf.get(tokenTheme)).size()-1) {
                    bundle.putBoolean("last", true);
                } else {
                    bundle.putBoolean("last", false);
                }
                bundle.putString("thisTheme", thisTheme);
                fragmentSend.setArguments(bundle);
            }
            Log.e("checkcheckTaskTopic","Finish");
            return fragmentSend;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }

    static Typeface type;

    FrameLayout llBottomSheet;

    static BottomSheetBehavior bottomSheetBehavior;
    boolean fullDraft=false;
    static int prevTheme;
    static AdRequest adRequest;
//    static AdRequest adRequest;
    static public Boolean  isdrawing = true;
    static FrameLayout linearLayoutItems;
    ImageButton imageButtonPen,imageButtonErase,imageButtonBin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chetnost1);


//        AdView mAdView = findViewById(R.id.adViewTask);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);



        Toolbar toolbar = findViewById(R.id.toolbar_task);
        setSupportActionBar(toolbar);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        linearLayoutItems=findViewById(R.id.linearLayoutItems);

        type = ResourcesCompat.getFont(getApplicationContext(), R.font.roboto_regular);



        viewPager = findViewById(R.id.viewpager3);
        taskProgress=null;
        taskSolution=null;
        thisTheme=getIntent().getStringExtra("thisTheme");
        tokenTheme=getIntent().getStringExtra("tokenTheme");

        Log.e("checkcheckTopicOlympStart","Start "+String.valueOf(tokenTheme));
        prevTheme=getIntent().getIntExtra("prevTheme",0);
        db = FirebaseFirestore.getInstance();
        thisContext = getApplicationContext();
        setTitle(thisTheme);
//        setContentView(R.layout.activity_draft
//        AppBarLayout top2 = (findViewById(R.id.appBar);
//        setSupportActionBar(top2);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getObject();
        mAuth= FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        taskProgress = (ArrayList) Distrib.allInf.get(tokenTheme);
        taskSolution = (ArrayList) Distrib.allInf.get(tokenTheme+"Solution");
        rewardedAd=createAndLoadRewardedAd();


        //-----------------------------------------

//        imageButtonBookmark=findViewById(R.id.imageButtonBookmark);
        ArrayList<String> bookmarkArray=(ArrayList<String>) Distrib.allInf.get("bookmark");
        for(int i=0;i<bookmarkArray.size();i++){
            Log.e("checkcheckBookmark",bookmarkArray.get(i));
            if(bookmarkArray.get(i).equals(thisTheme)){
                bookmark="Удалить из избранных";
            }
        }
        ArrayList<String> bookmarkLike=(ArrayList<String>) Distrib.allInf.get("like");
        for(int i=0;i<bookmarkLike.size();i++){
            Log.e("checkcheckBookmark",bookmarkLike.get(i));
            if(bookmarkLike.get(i).equals(thisTheme)){
                like="Удалить лайк";
            }
        }



        DocumentReference docRef = db.collection("olympiad").document(tokenTheme);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                String TAG = "errorCheck";
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Map<String, Object> inf = ((Map<String, Object>) documentSnapshot.getData());
                        tema=inf;
                        taskPosition.clear();
                        int number_task=0;
                        for (int i = 0; i < taskProgress.size(); i++) {
                            if(((ArrayList) tema.get("task" + String.valueOf(i))).get(1).toString().equals("null")){
                                number_task++;
                            }else if(((ArrayList) tema.get("task" + String.valueOf(i))).get(1).toString().equals("theory")){
                            }else {
                                number_task++;
                            }
                            taskPosition.add(number_task);
                            if(i==taskProgress.size()-1){
                                setAdap();
                            }
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with", task.getException());
                }
            }
        });

    }

    final String[] mChooseCats = { "09:00", "13:00", "18:00" };
    private final int IDD_THREE_BUTTONS = 0 , IDD_LISTVIEW_BUTTON=1,IDD_ALERTDIALOG=2;

    public RewardedAd createAndLoadRewardedAd() {
        RewardedAd rewardedAd = new RewardedAd(this,
                "ca-app-pub-3206990026084887/5708000678");
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
//                Toast.makeText(getApplicationContext(),"Рекалмам загружена",Toast.LENGTH_LONG).show();;
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                // Ad failed to load.
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
        return rewardedAd;
    }

//    @Override public boolean dispatchTouchEvent(MotionEvent event){
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            if (bottomSheetBehavior.getState()==BottomSheetBehavior.STATE_EXPANDED) {
//
//                Rect outRect = new Rect();
//                llBottomSheet.getGlobalVisibleRect(outRect);
//
//                if(!outRect.contains((int)event.getRawX(), (int)event.getRawY()))
//                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//            }
//        }
//
//        return super.dispatchTouchEvent(event);
//    }


    static void ShowAdMob(AdView mAdView){
        MobileAds.initialize(thisContext, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        Log.e("checkcheckShowAdMob","Show");
    }

    @Override
    public void onResume() {
        super.onResume();

    }
    String log="";
    View thisView;
    @Override
    protected void onPause() {
        super.onPause();
    }


    public void setParam(Map<String,Object> inf,Long all_task,String topic){
        tema=inf;
    }
    public void getObject(){

        //--------------------------------


//        int cnt=0;
//        while(taskProgress==null&&cnt<300000000){
//            cnt++;
//        }

        //-------------------------

        activity = findViewById(R.id.main_chetnost);
//        image_bottom=findViewById(R.id.imageButtonBottom);
        star1=findViewById(R.id.star1);
        star2=findViewById(R.id.star2);
        star3=findViewById(R.id.star3);
        send_answer=findViewById(R.id.send);
//        imageButtonLike=findViewById(R.id.imageButtonLike);
        editText=findViewById(R.id.editTextAnswer);
//        toolbar =(TextView) findViewById(R.id.toolbarTask);
//        imageButtonBookmark=findViewById(R.id.imageButtonBookmark);
    }
    public void setAdap(){
        tabLayout = findViewById(R.id.sliding_tabs2);
        MyAdapter adapter = new MyAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter); // устанавливаем адаптер
        viewPager.setCurrentItem(0); // выводим второй экран
        tabLayout.setupWithViewPager(viewPager);



        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                checkBookmark=false;
                ArrayList<String> bookmarkArray=(ArrayList<String>) Distrib.allInf.get("bookmark");
                for(int i=0;i<bookmarkArray.size();i++){
                    Log.e("checkcheckBookmark",bookmarkArray.get(i));
                    if(bookmarkArray.get(i).equals(thisTheme)){
                        checkBookmark=true;
                    }
                }
                checkLike=false;
                ArrayList<String> bookmarkLike=(ArrayList<String>) Distrib.allInf.get("like");
                for(int i=0;i<bookmarkLike.size();i++){
                    Log.e("checkcheckBookmark",bookmarkLike.get(i));
                    if(bookmarkLike.get(i).equals(thisTheme)){
                        checkLike=true;
                        break;
                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //--------------------------------------

        ArrayList taskRegistr = (ArrayList) Distrib.allInf.get(tokenTheme);
        for (int i = 0; i < taskProgress.size(); i++) {
            if(((ArrayList) tema.get("task" + String.valueOf(i))).get(1).toString().equals("null")){
                tabLayout.getTabAt(i).setIcon(R.drawable.star_evidence);
            }else if(((ArrayList) tema.get("task" + String.valueOf(i))).get(1).toString().equals("theory")){
                tabLayout.getTabAt(i).setIcon(R.drawable.triagle_gradient);
            }else {
                if (((Long) taskRegistr.get(i)).intValue() == 0) {
                    tabLayout.getTabAt(i).setIcon(R.drawable.mark_wrong);
                } else if (((Long) taskRegistr.get(i)).intValue() == 1) {
                    tabLayout.getTabAt(i).setIcon(R.drawable.question2);
                } else {
                    tabLayout.getTabAt(i).setIcon(R.drawable.mark_true);
                }
            }
        }

        //------------------------------------------


//        toolbar.setText(thisTheme);
//        toolbar.setSelected(true);
    }

    public  void draft(MenuItem view){
        Task.showDrafrTask();
//        Intent intent=new Intent(this,Draft.class);
//        if(positionTopic==0){
//            intent.putExtra("text", (tema.get("theory").toString()));
//        }else {
//            intent.putExtra("text", ((ArrayList) tema.get("task" + String.valueOf(positionTopic - 1))).get(0).toString());
//        }
//        startActivity(intent);
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

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.task, menu);
//        menu.findItem(R.id.action_like_task).setTitle(like);
//        menu.findItem(R.id.action_bookmark_task).setTitle(bookmark);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        Log.e("checkcheckItem","BAAD");
        Boolean r=true;
        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        final DocumentReference docRes=db.collection("account").document(user.getUid());
//        Toast.makeText(getApplicationContext(),item.getTitle().toString(),Toast.LENGTH_LONG).show();;
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
//                if()
                return true;
            case R.id.action_like_task:
                r=false;
                docRes.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot=task.getResult();
                            if(documentSnapshot.exists()){
                                Map<String,Object> inf=documentSnapshot.getData();
                                ArrayList arrayLike=(ArrayList)inf.get("like");
                                String snackbar;
                                if(arrayLike.contains(thisTheme)){
                                    item.setTitle("Поставить лайк");
//                                    Toast.makeText(getApplicationContext(),"gooog",Toast.LENGTH_LONG).show();
                                    LikeInf.put(thisTheme,13);
                                    arrayLike.remove(thisTheme);
                                    ((ArrayList) Distrib.allInf.get("like")).remove(thisTheme);
                                    final DocumentReference docRes2=db.collection("olympiad").document(tokenTheme);
                                    docRes2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                                            if(task.isSuccessful()){
                                                DocumentSnapshot documentSnapshot=task.getResult();
                                                if(documentSnapshot.exists()){
                                                    Map<String,Object> inf=documentSnapshot.getData();
                                                    inf.put("like",((Long)inf.get("like")).intValue()-1);
                                                    docRes2.set(inf);
                                                }
                                            }
                                        }
                                    });
                                }else{
                                    item.setTitle("Удалить лайк");
                                    arrayLike.add(thisTheme);
                                    LikeInf.put(thisTheme,(int)LikeInf.get(thisTheme)+1);
                                    ((ArrayList) Distrib.allInf.get("like")).add(thisTheme);
                                    like_param=true;
                                    final DocumentReference docRes2=db.collection("olympiad").document(tokenTheme);
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
                                docRes.set(inf);
                            }
                        }
                    }
                });
                Log.e("checkcheckItem","FIRST");
            case R.id.action_bookmark_task:
                if(r) {
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
                                        item.setTitle("Добавить в избранные");
                                    }else{
                                        item.setTitle("Удалить из избранных");
                                        arrayBookmark.add(thisTheme);
                                        ((ArrayList) Distrib.allInf.get("bookmark")).add(thisTheme);
                                    }
                                    inf.put("bookmark",arrayBookmark);
                                    docRes.set(inf);
                                }
                            }
                        }
                    });
                    Log.e("checkcheckItem", "SECOND");
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Distrib.marketing>=1100&& Distrib.marketing%2==1&&rewardedAd.isLoaded()) {
            RewardedAdCallback adCallback = new RewardedAdCallback() {
                @Override
                public void onRewardedAdOpened() {
                    // Ad opened.
                }

                @Override
                public void onRewardedAdClosed() {
                    // Ad closed.
                    Toast.makeText(getApplicationContext(),"Эта реклама помогает нам развиваться и оставаться бесплатным",Toast.LENGTH_LONG).show();
//                    rewardedAd = createAndLoadRewardedAd();
                }

                @Override
                public void onUserEarnedReward(@NonNull RewardItem reward) {
                    // User earned reward.
//                                                                    Toast.makeText(getActivity(), "ПОздравляю", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onRewardedAdFailedToShow(int errorCode) {
                    // Ad failed to display.
                }
            };
            rewardedAd.show(this, adCallback);
        }
        Distrib.marketing++;
    }



}