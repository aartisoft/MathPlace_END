package com.math4.user.mathplace;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.math4.user.mathplace.Task.clickPositionTheme;
import static com.math4.user.mathplace.Topic.bottomSheetBehavior;
import static com.math4.user.mathplace.Topic.rewardedAd;


public class BottomSheetFragment extends Fragment {

    private View imgArrow;
    LinearLayout linearLayoutSolv,linearLayoutDraft,linearLayoutAddBookmark;
    ImageView imageViewLike;
    View view;
    FirebaseFirestore db;
    FirebaseUser user;
    Boolean checkSolution=false;
    LinearLayout linearLayoutMain;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
        linearLayoutMain=view.findViewById(R.id.linearLayoutBottomSheet);
        createObjects();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        imgArrow = view.findViewById(R.id.imgArrow);
    }


    @Override
    public void onStart() {
        super.onStart();
//        createObjects();
//        linearLayoutSolv=view.findViewById(R.id.linearLayoutSolv);
//        linearLayoutDraft=view.findViewById(R.id.linearLayoutDraft);
//        linearLayoutAddBookmark=view.findViewById(R.id.linearLayoutAddBookmark);
//        imageViewLike=view.findViewById(R.id.imageViewLike);
//        db=FirebaseFirestore.getInstance();
//        FirebaseAuth mAuth=FirebaseAuth.getInstance();
//        user=mAuth.getCurrentUser();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        linearLayoutMain=view.findViewById(R.id.linearLayoutBottomSheet);
//        createObjects();;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public void createObjects(){
        linearLayoutMain.removeAllViews();
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,30,0,0);
        LinearLayout.LayoutParams layoutParamsTop=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamsTop.setMargins(0,60,0,0);
        LinearLayout.LayoutParams layoutParamsBottom=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutParams.setMargins(0,30,0,0);

        LinearLayout linearLayoutSolv = new LinearLayout(getActivity());
        linearLayoutSolv.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutSolv.setLayoutParams(layoutParamsTop);
        linearLayoutSolv.addView(addImage(R.drawable.lock));
        linearLayoutSolv.addView(addText("Открыть решение"));
        LinearLayout linearLayoutDraft = new LinearLayout(getActivity());
        linearLayoutDraft.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutDraft.setLayoutParams(layoutParams);
        linearLayoutDraft.addView(addImage(R.drawable.pen));
        linearLayoutDraft.addView(addText("Открыть черновик"));
        linearLayoutDraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Draft.class);
                startActivity(intent);
            }
        });
        LinearLayout linearLayoutAddBookmark = new LinearLayout(getActivity());
        linearLayoutAddBookmark.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutAddBookmark.setLayoutParams(layoutParams);
        linearLayoutAddBookmark.addView(addImage(R.drawable.bookmark));
        linearLayoutAddBookmark.addView(addText("Добавить в закладки"));
        LinearLayout linearLayoutLike = new LinearLayout(getActivity());
        linearLayoutLike.setOrientation(LinearLayout.VERTICAL);
        linearLayoutLike.setLayoutParams(layoutParamsBottom);
        linearLayoutLike.setVerticalGravity(LinearLayout.VERTICAL);
        linearLayoutLike.setGravity(View.TEXT_ALIGNMENT_CENTER);
        linearLayoutLike.addView(addImage2(R.drawable.like));
        linearLayoutLike.addView(addText2("Поставить лайк этой теме"));

        linearLayoutMain.addView(linearLayoutSolv);
        linearLayoutMain.addView(linearLayoutDraft);
        linearLayoutMain.addView(linearLayoutAddBookmark);
        linearLayoutMain.addView(linearLayoutLike);
    }

    public ImageView addImage(int image){
        ImageView imageView=new ImageView(getActivity());
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(100,100);
        layoutParams.setMargins(70,0,0,0);
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setBackgroundResource(image);
        return imageView;
    }


    public TextView addText(String text){
        TextView textView=new TextView(getActivity());
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(100,0,0,0);
        textView.setLayoutParams(layoutParams);
        textView.setText(text);
        textView.setTextSize(20);
        textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextColor(getResources().getColor(R.color.colorPrimary));
        return textView;
    }

    public ImageView addImage2(int image){
        ImageView imageView=new ImageView(getActivity());
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(400,400);
        layoutParams.setMargins(70,0,0,0);
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        imageView.setBackgroundResource(image);
        return imageView;
    }


    public TextView addText2(String text){
        TextView textView=new TextView(getActivity());
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(100,0,0,20);
        textView.setLayoutParams(layoutParams);
        textView.setText(text);
        textView.setTextSize(20);

        textView.setTextColor(getResources().getColor(R.color.colorPrimary));
        return textView;
    }

    void setOpenProgress(float progress) {
        // Rotate arrow in 1/3 of the screen to 180
        // meaning that after 1/3 we assume sheet to be opened
//        imgArrow.setRotation(Math.min(180 * progress * 3, 180));
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


}