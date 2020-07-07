package com.math4.user.mathplace.olymp;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.math4.user.mathplace.MainActivity;
import com.math4.user.mathplace.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.math4.user.mathplace.olymp.MainOlymp.AllTask;


public class AddMaterialTheory extends AppCompatActivity {


    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    Context context;
    String mUri;
    private StorageTask uploadTask;
    StorageReference storageReference;
    DatabaseReference reference;
    TextView addText,addImage;
    LinearLayout linearLayoutMain;
    ImageView imageView;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser user;
    int cnt=0;
    Animation animation2;
    LinearLayout.LayoutParams layoutParam;
    LinearLayout linearLayoutNew;
    ArrayList<Pair<View,String>> arrayList=new ArrayList<Pair<View,String>>();
    Boolean edit;
    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_material_theory);
        Bundle bundle=getIntent().getExtras();
        edit=bundle.getBoolean("edit",false);
        position=bundle.getInt("position",0);


        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();

        animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.olymp2);
        layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        //      // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();
        //
        //// Create a reference to "mountains.jpg"
        StorageReference mountainsRef = storageRef.child("user.jpg");

        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        linearLayoutMain = findViewById(R.id.linearLayoutMainTheory);
        addImage = findViewById(R.id.addImage);
        addText = findViewById(R.id.addText);


        context=this;

        Toolbar toolbar = findViewById(R.id.toolbarAddMaterialTheory);
        setSupportActionBar(toolbar);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        if(edit) {
            setTitle("Редактировать теорию");
            String text=AllTask.get(position).get(0);
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

        }else {
            setTitle("Добавить теорию");


            Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.olymp2);
            LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout linearLayoutNew = newLinearLayoutText();
            linearLayoutNew.startAnimation(animation2);
            int childCount = ((ViewGroup) linearLayoutMain).getChildCount();
            linearLayoutMain.addView(linearLayoutNew, childCount - 1, layoutParam);


        }




        addText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.olymp2);
                LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout linearLayoutNew = newLinearLayoutText();
                linearLayoutNew.startAnimation(animation2);
                int childCount = ((ViewGroup) linearLayoutMain).getChildCount();
                linearLayoutMain.addView(linearLayoutNew, childCount - 1, layoutParam);
            }
        });


        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newLinearLayoutImage();
            }
        });
    }

    View viewImage2;
    public void newLinearLayoutImage(){
// create a layout




        final View viewImage = LayoutInflater.from(this).inflate(R.layout.example_image_view, null);
        viewImage2=viewImage;
        linearLayoutNew=(LinearLayout) viewImage;
        imageView=viewImage.findViewById(R.id.roundedImageView);
// create a layout param for the layout
        openImage();

        ImageView imageViewDelete=viewImage.findViewById(R.id.deleteImage);
        imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.olymp3);
                viewImage.startAnimation(animation);
                for(int i=0;i<arrayList.size();i++){
                    if(arrayList.get(i).first==viewImage){
                        arrayList.remove(i);
                    }
                }
//                arrayList.remove(new Pair(viewImage,mUri));
                linearLayoutMain.removeView(viewImage);
            }
        });



    }


    public void addText(String text){
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        final View view = LayoutInflater.from(this).inflate(R.layout.example_text_view, null);
// create a layout param for the layout
        ImageView imageView=view.findViewById(R.id.imageView);
        arrayList.add(new Pair(view,"0"));
        cnt++;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.olymp3);
                view.startAnimation(animation);
                arrayList.remove(new Pair(view,"0"));
                linearLayoutMain.removeView(view);
            }
        });

        ((TextView)view.findViewById(R.id.textView)).setText(text);
        int childCount = ((ViewGroup) linearLayoutMain).getChildCount();
        linearLayoutMain.addView(view,childCount-1,layoutParam);
    }


    public void addImage(String uri){

        final View viewImage = LayoutInflater.from(this).inflate(R.layout.example_image_view, null);
        final ImageView imageView=viewImage.findViewById(R.id.roundedImageView);


        RelativeLayout.LayoutParams MyParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        MyParams.leftMargin=8;
        MyParams.rightMargin=8;
//        imageView.setLayoutParams(MyParams);
//        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        int childCount = ((ViewGroup)linearLayoutMain).getChildCount();
        linearLayoutMain.addView(viewImage,childCount-1,layoutParam);
        Glide.with(this).load(Uri.parse(uri)).into(imageView);
        ImageView imageViewDelete=viewImage.findViewById(R.id.deleteImage);
        imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.olymp3);
                viewImage.startAnimation(animation);
                for(int i=0;i<arrayList.size();i++){
                    if(arrayList.get(i).first==viewImage){
                        arrayList.remove(i);
                    }
                }
//                arrayList.remove(new Pair(viewImage,mUri));
                linearLayoutMain.removeView(viewImage);
            }
        });



//        final View viewImage = LayoutInflater.from(this).inflate(R.layout.example_image_view, null);
//        linearLayoutNew=(LinearLayout) viewImage;
//        imageView=viewImage.findViewById(R.id.roundedImageView);
//        Glide.with(context).load(uri).into(imageView);
//// create a layout param for the layout
//        Toast.makeText(context,uri,Toast.LENGTH_LONG).show();
//
//        ImageView imageViewDelete=viewImage.findViewById(R.id.deleteImage);
//        imageViewDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.olymp3);
//                viewImage.startAnimation(animation);
//                for(int i=0;i<arrayList.size();i++){
//                    if(arrayList.get(i).first==viewImage){
//                        arrayList.remove(i);
//                    }
//                }
////                arrayList.remove(new Pair(viewImage,mUri));
//                linearLayoutMain.removeView(viewImage);
//            }
//        });
//
//
//        int childCount = ((ViewGroup)linearLayoutMain).getChildCount();
//        linearLayoutMain.addView(linearLayoutNew,childCount-1,layoutParam);
    }


    public LinearLayout newLinearLayoutText(){
// create a layout
        final View view = LayoutInflater.from(this).inflate(R.layout.example_text_view, null);
// create a layout param for the layout
        TextInputLayout textInputLayout=view.findViewById(R.id.textField);
        ImageView imageView=view.findViewById(R.id.imageView);
        arrayList.add(new Pair(view,"0"));
        cnt++;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.olymp3);
                view.startAnimation(animation);
                arrayList.remove(new Pair(view,"0"));
                linearLayoutMain.removeView(view);
            }
        });
        return (LinearLayout)view;
    }



    public void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        Log.e("checkcheckImageStartStart","Good");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    private void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Загрузка...");
        pd.show();

        if (imageUri != null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    +"."+getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw  task.getException();
                    }

                    return  fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        mUri = downloadUri.toString();
                        arrayList.add(new Pair(viewImage2,mUri));

                        Log.e("checkcheckImageUri",mUri);


                        reference = FirebaseDatabase.getInstance().getReference("account").child(user.getUid());
                        HashMap<String, Object> map = new HashMap<>();
                        Glide.with(getApplicationContext()).load(mUri).into(imageView);
                        map.put("image", String.valueOf(mUri));


                        reference.updateChildren(map);

                        FirebaseAuth mAuth=FirebaseAuth.getInstance();
                        FirebaseUser user=mAuth.getCurrentUser();
                        final DocumentReference docRef = db.collection("account").document(user.getUid());
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if (documentSnapshot.exists()) {
                                        Map<String, Object> inf = documentSnapshot.getData();
                                        inf.put("image", mUri);
                                        docRef.set(inf);
                                    }
                                }
                            }
                        });

                        pd.dismiss();

                    } else {
                        Toast.makeText(context, "Не получилось", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        } else {
            Toast.makeText(context, "Картинка не выбрана", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("checkcheckImageStart","STart");
        Toast.makeText(context,"ActivityResult",Toast.LENGTH_LONG).show();
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            imageUri = data.getData();


            Toast.makeText(getApplicationContext(),String.valueOf(imageUri),Toast.LENGTH_LONG).show();
            Log.e("checkcheckImageMiddle",String.valueOf(imageUri));

            if (uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(context, "Upload in progress", Toast.LENGTH_SHORT).show();
            } else {
                Log.e("checkcheckImageMiddle","Upload");
                uploadImage();
                linearLayoutNew.startAnimation(animation2);
                int childCount = ((ViewGroup)linearLayoutMain).getChildCount();
                linearLayoutMain.addView(linearLayoutNew,childCount-1,layoutParam);
            }
        }else{
            Toast.makeText(getApplicationContext(),"Вы не выбрали картинку",Toast.LENGTH_LONG).show();;
        }
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
                String ans="";
                Boolean r=true;
                for(int i=0;i<arrayList.size();i++){
                    Pair<View,String> pair=arrayList.get(i);
                    if(pair.second.equals("0")){
                        String text=((TextInputEditText)pair.first.findViewById(R.id.textView)).getText().toString();
                        if(text.equals("")){
                            r=false;
                            ((TextInputLayout)pair.first.findViewById(R.id.textField)).setError("Условие пустое");
                        }
                        ans=ans+text;
                    }else{
                        ans=ans+"["+pair.second+"] ";
                    }
                }
                if(ans.length()>0&&r){
                    ArrayList arrayList2=new ArrayList();
                    arrayList2.add(ans);
                    arrayList2.add("theory");
                    arrayList2.add("null");
                    arrayList2.add("null");
                    if(edit){
                        AllTask.set(position,arrayList2);
                    }else {
                        AllTask.add(arrayList2);
                    }
                    this.finish();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}