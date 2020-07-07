package com.math4.user.mathplace.olymp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.math4.user.mathplace.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.math4.user.mathplace.olymp.MainOlymp.AllTask;

public class Olymp extends AppCompatActivity {

    static TextView textViewHead;
    static ImageView star3,star2;



    static LayoutInflater inflater2;
    RecyclerView categoriesView;

    TextInputEditText nameOlymp;
    TextInputLayout nameOlympLayout,typeOlympLayout,sectionOlympLayout,classOlympLayout;

    AutoCompleteTextView autoCompleteTextViewType,autoCompleteTextViewSection,autoCompleteTextViewClass;


    static List<Olymp.Subcategory> getRandomData() {
        final ArrayList<Olymp.Subcategory> arrayListReturn = new ArrayList<>();
        final ArrayList<Olymp.Subcategory> subcategoryList = new ArrayList<>();
        for(int i2 = 0; i2<AllTask.size(); i2++){
            final int i=i2;
//            subcategoryList.add(new MakeContest.Subcategory(nameTheme.toString()));
            arrayListReturn.add(new Olymp.Subcategory(AllTask.get(i).get(0),AllTask.get(i).get(1),AllTask.get(i).get(2),AllTask.get(i).get(3)));
        }
        return arrayListReturn;
    }

    static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olymp);
        AllTask.clear();
        Toolbar toolbar = findViewById(R.id.toolbarOlymp);
        inflater2 = getLayoutInflater();
        setSupportActionBar(toolbar);
        context=this;
        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Создать урок");

        nameOlymp=findViewById(R.id.textViewNameOlymp);
        nameOlympLayout=findViewById(R.id.textFieldName);
        nameOlymp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count>0){
                    nameOlympLayout.setError("");
                }else{
                    nameOlympLayout.setError("Имя пустое");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Button buttonAddTheory=findViewById(R.id.addMyContent);
        buttonAddTheory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog2();
            }
        });

        Button buttonAddMathPlaceContent=findViewById(R.id.addMathPlaceContent);
        buttonAddMathPlaceContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Olymp.this,MakeContest.class));
            }
        });



        typeOlympLayout=findViewById(R.id.typeOlympLayout);
        autoCompleteTextViewType = findViewById(R.id.typeOlymp);

        String[] arrayType = getResources().getStringArray(R.array.typeOlymp);
        List<String>  arrayTypeList = Arrays.asList(arrayType);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, R.layout.list_item, arrayTypeList);
        autoCompleteTextViewType.setAdapter(adapter);


        sectionOlympLayout=findViewById(R.id.sectionOlympLayout);
        autoCompleteTextViewSection = findViewById(R.id.sectionOlymp);

        String[] arraySection = getResources().getStringArray(R.array.sectionOlymp);
        List<String> arraySectionList = Arrays.asList(arraySection);
        ArrayAdapter<String> adapterSection = new ArrayAdapter<>(
                this, R.layout.list_item, arraySectionList);
        autoCompleteTextViewSection.setAdapter(adapterSection);


        classOlympLayout=findViewById(R.id.classOlympLayout);
        autoCompleteTextViewClass = findViewById(R.id.classOlymp);

        String[] arrayClass = getResources().getStringArray(R.array.classOlymp);
        List<String> arrayClassList = Arrays.asList(arrayClass);
        ArrayAdapter<String> adapterClass = new ArrayAdapter<>(
                this, R.layout.list_item, arrayClassList);
        autoCompleteTextViewClass.setAdapter(adapterClass);

    }

    @Override
    public void onResume(){
        super.onResume();
        categoriesView = findViewById(R.id.categories_listContentOlymp);
        List<Olymp.Subcategory> CircleCategories = Olymp.getRandomData();
        categoriesView.setHasFixedSize(true);
        categoriesView.setLayoutManager(new LinearLayoutManager(
                this,
                RecyclerView.VERTICAL,
                false
        ));
        categoriesView.setAdapter(new Olymp.SubcategoriesAdapter(CircleCategories));
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
                boolean r1=false,r2=false,r3=false,r4=false,r5=false;
                if(nameOlymp.getText().length()>0){
                    r1=true;
                }else{
                    nameOlympLayout.setError("Имя пустое");
                }
                if(autoCompleteTextViewClass.getText().length()>0){
                    r4=true;
                }else{
                    classOlympLayout.setError("Выберите вариант");
                }
                if(autoCompleteTextViewSection.getText().length()>0){
                    r3=true;
                }else{
                    sectionOlympLayout.setError("Выберите вариант");
                }
                if(autoCompleteTextViewType.getText().length()>0){
                    r2=true;
                }else{
                    typeOlympLayout.setError("Выберите вариант");
                }

                if(AllTask.size()>0){
                    r5=true;
                }else{
                    Toast.makeText(getApplicationContext(),"Не добавлен материал",Toast.LENGTH_LONG).show();
                }
                if(r1&&r2&&r3&&r4&&r5){
                    makeTheme();
                    Toast.makeText(getApplicationContext(),"Опубликовано",Toast.LENGTH_LONG).show();
                    this.finish();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public String getHash(){
        int n = 20;
        Random r = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < n; i++) {
            char code = (char) (r.nextInt(23    ) + 97);
            builder.append(Character.toString(code));
        }
        return builder.toString();
    }


    public void makeTheme() {
        FirebaseFirestore db= FirebaseFirestore.getInstance();
        FirebaseAuth auth=FirebaseAuth.getInstance();
        FirebaseUser user=auth.getCurrentUser();
        final DocumentReference docRef = db.collection("olympiad").document(getHash());
        //                            if(docRef==null) {
        final HashMap<String, Object> m3 = new HashMap<>();
        //                            m3.put("password",user.get);
        m3.put("like", 0);
        DocumentReference docRef2 = db.collection("account").document(user.getUid());
        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Map<String, Object> inf = documentSnapshot.getData();
                        m3.put("author",inf.get("name").toString());
                        if(autoCompleteTextViewType.getText().equals("Открытый")) {
                            m3.put("public", true);
                        }else{
                            m3.put("public", false);
                        }
                        m3.put("theme",autoCompleteTextViewSection.getText().toString().toLowerCase());
                        m3.put("class",autoCompleteTextViewClass.getText().toString().toLowerCase());
                        m3.put("name", nameOlymp.getText().toString());
                        int cnt=0,cnt_task = 0,items=0;
                        for (int i=0;i<AllTask.size();i++) {
                            HashMap<String, Object> m2 = new HashMap<>();
                            //                                m2.put(documentTask.getId()+"Progress",0);
                            if(!AllTask.get(i).get(1).equals("theory")){
                                cnt_task++;
                            }
                            items++;
                            ArrayList a = new ArrayList();
                            for (int j = 0; j < AllTask.get(i).size(); j++) {
                                a.add(AllTask.get(i).get(j));
                            }
                            m2.put("task"+String.valueOf(i), a);
                            docRef.set(m2, SetOptions.merge());
                            cnt += 1;
                        }
                        m3.put("cnt_task",cnt_task);
                        m3.put("items",items);
                        docRef.set(m3, SetOptions.merge());
                    }
                }
            }
        });


    }

    static class Subcategory {
        String text,name;
        String ans,hard,solution;
        boolean type;

        Subcategory(String text,String ans,String hard,String solution)
        {
            this.text = text; this.ans=ans;this.hard = hard;this.solution=solution;
        }
    }

    static class SubcategoriesAdapter extends RecyclerView.Adapter<Olymp.SubcategoriesAdapter.ViewHolder> {

        List<Olymp.Subcategory> data;

        SubcategoriesAdapter(List<Olymp.Subcategory> data) {
            this.data = data;
        }


        @Override
        public Olymp.SubcategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.subcategory_list_item_make_olymp, parent, false);
            return new Olymp.SubcategoriesAdapter.ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(final Olymp.SubcategoriesAdapter.ViewHolder holder, final int position) {
            final Olymp.Subcategory subcategory = data.get(position);
            Toast.makeText(context,subcategory.ans.toString(),Toast.LENGTH_LONG).show();
            if(subcategory.ans.equals("theory")){
                subcategory.name="Теория";
                holder.subcategoryName.setText("Теория");
                subcategory.type=false;
            }else{
                subcategory.name="Задача";
                subcategory.type=true;
                holder.subcategoryName.setText("Задача");
            }

            final FirebaseFirestore db=FirebaseFirestore.getInstance();

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopupMenu(v,position);
                }
            });


            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
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


                    final Boolean theory;

                    if(subcategory.ans.equals("theory")){
                        theory=true;
                    }else{
                        theory=false;
                    }
                    if(!theory) {
                        int stars = Integer.parseInt(subcategory.hard);
                        if (stars == 1) {
                            star3.setVisibility(View.INVISIBLE);
                            star2.setVisibility(View.INVISIBLE);
                        } else if (stars == 2) {
                            star3.setVisibility(View.INVISIBLE);
                        }
                        textViewHead.setText("Задача");
                    }
                    String text=(String)(subcategory.text);
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

                    // Create the alert dialog
                    final AlertDialog dialog = builder.create();

                    // Set positive/yes button click listener
                    btn_positive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            // Dismiss the alert dialog
//                            if(holder.checkBox.isChecked()) {
//                                Toast.makeText(context, "Вы её уже выбрали", Toast.LENGTH_LONG).show();
//                            }else{
//                                holder.checkBox.setChecked(true);
//                            }
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

        }


        private void showPopupMenu(View v,final int position) {
            Context wrapper = new ContextThemeWrapper(context, R.style.popupMenuStyle);
            PopupMenu popup = new PopupMenu(wrapper, v);
            PopupMenu popupMenu = new PopupMenu(wrapper, v);
            popupMenu.inflate(R.menu.popupmenu);

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menu1:
                                    if(AllTask.get(position).get(1).equals("theory")){
                                        Intent intent=new Intent(context,AddMaterialTheory.class);
                                        intent.putExtra("edit",true);
                                        intent.putExtra("position",position);
                                        context.startActivity(intent);
                                    }else{
                                        Intent intent=new Intent(context,AddMaterialTask.class);
                                        intent.putExtra("edit",true);
                                        intent.putExtra("position",position);
                                        context.startActivity(intent);
                                    }
//                                    Toast.makeText(getApplicationContext(),
//                                            "Вы выбрали PopupMenu 1",
//                                            Toast.LENGTH_SHORT).show();
                                    return true;
                                case R.id.menu2:
                                    AllTask.remove(position);
                                    data.remove(position);
                                    notifyDataSetChanged();
//                                    Toast.makeText(context,
//                                            "Вы выбрали PopupMenu 2",
//                                            Toast.LENGTH_SHORT).show();
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });

            popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
                @Override
                public void onDismiss(PopupMenu menu) {
//                    Toast.makeText(context, "onDismiss",
//                            Toast.LENGTH_SHORT).show();
                }
            });
            popupMenu.show();
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView subcategoryName,subcategoryText,subcategoryLike;
            LinearLayout linearLayout;
            ImageView imageView;

            ViewHolder(View itemView) {
                super(itemView);
                imageView=itemView.findViewById(R.id.popup);
                linearLayout=itemView.findViewById(R.id.linearLayoutOlymp);
//                subcategoryLike=itemView.findViewById(R.id.textViewLikeTheme);
                subcategoryName = itemView.findViewById(R.id.textViewOlymp);
//                subcategoryText = itemView.findViewById(R.id.textViewText);
//                subcategoryProgressBar = itemView.findViewById(R.id.progressBarTheme);
            }
        }
    }





    public void createDialog2() {
        View view = LayoutInflater.from(this).inflate(R.layout.fragment_bottom_sheet_material, null);//Главные функции
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
        TextView textView1=view.findViewById(R.id.MaterialTheory);
        TextView textView2=view.findViewById(R.id.MaterialTask);
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Olymp.this, AddMaterialTheory.class);
                intent.putExtra("edit",false);
                intent.putExtra("position",0);
                startActivity(intent);
                bottomSheetDialog.hide();
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Olymp.this, AddMaterialTask.class);
                intent.putExtra("edit",false);
                intent.putExtra("position",0);
                startActivity(intent);
                bottomSheetDialog.hide();
            }
        });

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
