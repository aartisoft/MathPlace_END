package com.math4.user.mathplace;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static android.app.Activity.RESULT_OK;
import static com.math4.user.mathplace.Distrib.allInf;
import static com.math4.user.mathplace.MainActivity.searchItem;
import static com.math4.user.mathplace.MainActivity.settingsItem;
import static com.math4.user.mathplace.MainActivity.textLoad;

public class Account extends Fragment {
    SharedPreferences sPref;
    FirebaseAuth mAuth;
    Context context_this;
    static FirebaseFirestore db;
    FirebaseUser user_this;
    static double right=0,wrong=0;
    static boolean buv = false;
    static int solved = 0;
    StorageReference storageReference;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
//    ImageView imageUser;
    DatabaseReference reference;
    FirebaseUser fuser;
    private StorageTask uploadTask;
    ImageView ImageAdd;
    static int solved2,solvedAll3;
    TextView message,text_activity,user_name,submit;
    // OnDataPass dataPasser;
    static ArrayList<Subcategory> arrayList;
    static List<Subcategory> getRandomData() {
        final Random random = new Random();
        arrayList = new ArrayList<>();
        arrayList.add(new Account.Subcategory("ПЕРВАЯ КРОВЬ", "решите 1 задачу",1, R.drawable.smallachivoff));
        arrayList.add(new Account.Subcategory("НОВИЧОК", "решите 5 задач",1, R.drawable.mediumachivoff));
        arrayList.add(new Account.Subcategory("ЛЮБИТЕЛЬ", "решите 25 задач",1, R.drawable.mediumachivoff));
        arrayList.add(new Account.Subcategory("ПРОФИ", "решите 100 задач",1, R.drawable.mediumachivoff));
        arrayList.add(new Account.Subcategory( "ПЕРВАЯ ТЕМА", "решите целиком всю тему",1, R.drawable.heavyachivoff));
        arrayList.add(new Account.Subcategory( "ТРИ ТЕМЫ", "решите целиком 3 темы",1, R.drawable.heavyachivoff));
        arrayList.add(new Account.Subcategory( "АЛГЕБРА", "решите целиком всю алгебру",1, R.drawable.heavyachivoff));
        arrayList.add(new Account.Subcategory( "ГЕОМЕТРИЯ", "решите целиком всю геометрию",1, R.drawable.heavyachivoff));
        arrayList.add(new Account.Subcategory( "ВСЕ ТЕМЫ", "решите целиком все темы",1, R.drawable.heavyachivoff));
        return arrayList;
    }
    RecyclerView categoriesView;
    View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account, container, false);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        user_name = view.findViewById(R.id.textViewName);
        submit = view.findViewById(R.id.textViewSend);
        final TextView textViewRight, textViewPercent;
        if(searchItem!=null){
            searchItem.setVisible(false);
        }
        if(settingsItem!=null) {
            settingsItem.setVisible(true);
        }
        textLoad.setVisibility(View.INVISIBLE);
        textViewRight = view.findViewById(R.id.textViewRight);
        textViewPercent = view.findViewById(R.id.textViewPercent);
        textViewPercent.setText(String.valueOf((int) ((right / wrong) * 100)) + "%");
        //-------------------------------------

        mAuth = FirebaseAuth.getInstance();
        user_this = mAuth.getCurrentUser();
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("account").document(user_this.getUid());


        right = 0;
        wrong = 0;
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        final Map<String, Object> m = documentSnapshot.getData();
                        user_name.setText(m.get("name").toString());
                        submit.setText(String.valueOf(m.get("submit")));
                        textViewRight.setText(String.valueOf(m.get("right")));
                        db.collection("task2")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                                        if (task2.isSuccessful()) {
                                            for (QueryDocumentSnapshot documentTask : task2.getResult()) {//здесь мы бежим по всем темам
//                                    Log.e("checkcheckDocumentTask",documentTask.getId());
                                                wrong+=((Long)documentTask.getData().get("cnt_task")).intValue();
                                                textViewPercent.setText(String.valueOf(String.format("%.1f", (double) ((Long) m.get("right")).intValue() * 100 / (wrong)) + "%"));
                                            }
                                        }
                                    }
                                });
                        Log.e("checkcheckResult", String.valueOf(right));
                        List<Subcategory> categories = getRandomData();
                        categoriesView = view.findViewById(R.id.categories_listAchiv);
                        categoriesView.setHasFixedSize(true);
                        context_this = getActivity();
                        categoriesView.setLayoutManager(new LinearLayoutManager(
                                                                getActivity(),
                                                                RecyclerView.VERTICAL,
                                                                false) {
                                                            @Override
                                                            public boolean canScrollVertically() {
                                                                return false;
                                                            }
                                                        }
                        );
                        categoriesView.setAdapter(new SubcategoriesAdapter(categories));
                    }
                }
            }
        });

        //--------------------------------------------------------------------------------------------
        FirebaseStorage storage = FirebaseStorage.getInstance();
//      // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();
//
//// Create a reference to "mountains.jpg"
        StorageReference mountainsRef = storageRef.child("user.jpg");
        if (mountainsRef == null) {
            Log.e("checkcheckStorage", "null");
        } else {
            Log.e("checkcheckStorage", "not null");
        }

//        ImageAdd = view.findViewById(R.id.imageButtonphoto);
        MainActivity.imageUser = view.findViewById(R.id.imageViewAccount3);
        if(allInf.get("image")!=null){
            Log.e("checkcheckAddImage","Succes");
            Uri uri = Uri.parse(allInf.get("image").toString());
            Glide.with(getContext()).load(uri).into(MainActivity.imageUser);
        }else{
            Log.e("checkcheckAddImage","Failed");
            MainActivity.imageUser.setImageResource(R.drawable.account_new);
        }
        ImageButton uploadPhoto=view.findViewById(R.id.uploadPhoto);
        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
    }




    public void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    private void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Загрузка...");
        pd.show();

        if (imageUri != null){
            final  StorageReference fileReference = storageReference.child(System.currentTimeMillis()
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
                        final String mUri = downloadUri.toString();

                        Log.e("checkcheckImageUri",mUri);

                        reference = FirebaseDatabase.getInstance().getReference("account").child(fuser.getUid());
                        HashMap<String, Object> map = new HashMap<>();
                        Glide.with(getContext()).load(mUri).into(MainActivity.imageUser);
                        map.put("image", String.valueOf(mUri));
                        allInf.put("image",String.valueOf(mUri));
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
                        Toast.makeText(getContext(), "Не получилось", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        } else {
            Toast.makeText(getContext(), "Картинка не выбрана", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Toast.makeText(getActivity(),"ActivityResult",Toast.LENGTH_LONG).show();
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            imageUri = data.getData();

            if (uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(getContext(), "Upload in preogress", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }
        }
    }



    static class Subcategory {
        String text,title;
        int progress;
        int image;

        Subcategory(String title,String text, int progress,int image) {
            this.text = text;
            this.title=title;
            this.progress = progress;
            this.image=image;
        }
    }


    static class SubcategoriesAdapter extends RecyclerView.Adapter<SubcategoriesAdapter.ViewHolder> {

        static List<Subcategory> data;

        SubcategoriesAdapter(List<Subcategory> data2) {
            this.data = data2;
        }


        @Override
        public Account.SubcategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.subcategory_list_item_achiv, parent, false);
            return new Account.SubcategoriesAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final Account.SubcategoriesAdapter.ViewHolder holder, final int position) {
            final Account.Subcategory subcategory = data.get(position);
            ArrayList inf=(ArrayList) allInf.get("achiv");
            int image1,image2;
            if(position==0){
                image1= R.drawable.smallachivon;image2= R.drawable.smallachivoff;
            }else if(position>=1&&position<=3){
                image1= R.drawable.mediumachivon;image2= R.drawable.mediumachivoff;
            }else {
                image1= R.drawable.heavyachivon;image2= R.drawable.heavyachivoff;
            }
            if(position<4) {
                if (((Long) inf.get(position)).intValue() == 100) {
                    holder.subcategoryImage.setImageResource(image1);
                } else {
                    holder.subcategoryImage.setImageResource(image2);
                }
                holder.subcategoryProgress.setProgress(((Long) inf.get(position)).intValue());
            }else if (position==4){
                if (((Long) inf.get(position)).intValue() == 1) {
                    holder.subcategoryImage.setImageResource(image1);
                } else {
                    holder.subcategoryImage.setImageResource(image2);
                }
                holder.subcategoryProgress.setProgress(((Long) inf.get(position)).intValue()*100);
            }else if (position==5){
                if (((Long) inf.get(position)).intValue() == 3) {
                    holder.subcategoryImage.setImageResource(image1);
                } else {
                    holder.subcategoryImage.setImageResource(image2);
                }
                holder.subcategoryProgress.setProgress(((Long) inf.get(position)).intValue()*100/3);
            }else if (position==6){
                if (((Long) inf.get(position)).intValue() == Distrib.algebraNameTheme.size()) {
                    holder.subcategoryImage.setImageResource(image1);
                } else {
                    holder.subcategoryImage.setImageResource(image2);
                }
                holder.subcategoryProgress.setProgress(((Long) inf.get(position)).intValue()*100/ Distrib.algebraNameTheme.size());
            }else if (position==7){
                if (((Long) inf.get(position)).intValue() == Distrib.geometryNameTheme.size()) {
                    holder.subcategoryImage.setImageResource(image1);
                } else {
                    holder.subcategoryImage.setImageResource(image2);
                }
                holder.subcategoryProgress.setProgress(((Long) inf.get(position)).intValue()*100/ Distrib.geometryNameTheme.size());
            }
            holder.subcategoryText.setText(subcategory.text);
            holder.subcategoryTitle.setText(subcategory.title);

        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView subcategoryTitle,subcategoryText;
            ProgressBar subcategoryProgress;
            ImageView subcategoryImage;

            ViewHolder(View itemView) {
                super(itemView);
                subcategoryText=itemView.findViewById(R.id.textViewAchivText);
                subcategoryTitle = itemView.findViewById(R.id.textViewAchivName);
                subcategoryProgress = itemView.findViewById(R.id.progressBarAchiv);
                subcategoryImage = itemView.findViewById(R.id.imageViewmedalAchiv);
            }
        }
    }
}