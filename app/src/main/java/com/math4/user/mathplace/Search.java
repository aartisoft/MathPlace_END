package com.math4.user.mathplace;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.math4.user.mathplace.Distrib.allInf;


public class Search extends Fragment {
    View view;
    EditText textSearch;
    Button buttonSearch;
    FirebaseFirestore db;
    int image= R.drawable.algebra,right=0,allTask=1;
    long then=0;
    final String[] mChooseCats = { "09:00", "13:00", "18:00" };
    @Override
    // Переопределяем метод onCreateView
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_search, container, false);
        ListView listView=view.findViewById(R.id.listView);
        listView.setEmptyView(view.findViewById(R.id.empty));
        final ArrayList<String> themeName=new ArrayList<String>();
//        themeName.add("Четность");
//        themeName.add("Теорема Пифагора");
        for(int i = 0; i< Distrib.allNameTheme.size(); i++){
            themeName.add(Distrib.allNameTheme.get(i));
        }
        db=FirebaseFirestore.getInstance();
        textSearch=view.findViewById(R.id.textSearch);
        buttonSearch=view.findViewById(R.id.add);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.list_item, themeName);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent,final  View view, int position, long id) {
                FirebaseAuth mAuth= FirebaseAuth.getInstance();
                FirebaseUser user=mAuth.getCurrentUser();
                final DocumentReference docRes=db.collection("account").document(user.getUid());
                right=0;allTask=0;
                docRes.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot=task.getResult();
                            if(documentSnapshot.exists()){
                                Map<String,Object> inf=documentSnapshot.getData();
                                ArrayList<Long> arrayList=(ArrayList<Long>) inf.get(((TextView)view).getText().toString());
                                for(int i=0;i<arrayList.size();i++){
                                    if(((Long)arrayList.get(i)).intValue()==2){
                                        right++;
                                    }
                                }
                                final DocumentReference docRes2=db.collection("task").document(((TextView)view).getText().toString());
                                docRes2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()){
                                            DocumentSnapshot documentSnapshot=task.getResult();
                                            if(documentSnapshot.exists()){
                                                Map<String,Object> inf=documentSnapshot.getData();
                                                allTask=((Long)inf.get("all_task")).intValue();
                                                String type=inf.get("theme").toString();
                                                if(type.equals("алгебра")){
                                                    image= R.drawable.algebra;
                                                }else if(type.equals("геометрия")){
                                                    image= R.drawable.geometry;
                                                }else if(type.equals("комбинаторика")){
                                                    image= R.drawable.komba;
                                                }else if(type.equals("идеи")){
                                                    image= R.drawable.idea;
                                                }else if(type.equals("графы")){
                                                    image= R.drawable.algebra;
                                                }else{
                                                    image= R.drawable.geometry;
                                                }
                                                new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme)
                                                        .setTitle(((TextView)view).getText())
                                                        .setMessage("Решено "+ String.valueOf(right)+" задач \nВсего "+ String.valueOf(allTask) +" задач")

                                                        .setPositiveButton(R.string.ok_decide, new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                Intent intent=new Intent(getActivity(), Topic.class);
                                                                intent.putExtra("thisTheme",((TextView)view).getText().toString());
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
                                                                                Map<String, Object> m = new HashMap<>();
                                                                                m.put("lastTheme",((TextView)view).getText().toString());
                                                                                docRef.set(m, SetOptions.merge());
                                                                            }
                                                                        }
                                                                    }
                                                                });
                                                                CurrentUser.setLastTheme(((TextView)view).getText().toString());
                                                                Menu.textViewStudy.setText(CurrentUser.lastTheme);
                                                                MainActivity.textLoad.setVisibility(View.VISIBLE);
                                                                startActivity(intent);
                                                            }
                                                        })

                                                        // A null listener allows the button to dismiss the dialog and take no further action.
                                                        .setNegativeButton(android.R.string.no, null)
                                                        .setIcon(image)
                                                        .show();
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View itemClicked, int position,
                                    long id) {
                final Intent intent=new Intent(getActivity(), Topic.class);
                intent.putExtra("thisTheme",((TextView)itemClicked).getText().toString());
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
                                Map<String, Object> m = new HashMap<>();
                                final String clickTheme=((TextView)itemClicked).getText().toString();
                                if(inf.get(clickTheme)==null){
                                    final DocumentReference docRefNewTheme = db.collection("task2").document(clickTheme);
                                    docRefNewTheme.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if(task.isSuccessful()){
                                                DocumentSnapshot documentSnapshot2=task.getResult();
                                                if(documentSnapshot2.exists()){
                                                    Map<String,Object> infTheme=documentSnapshot2.getData();
                                                    int themeTask=((Long)infTheme.get("items")).intValue();
                                                    ArrayList newThemeUser=new ArrayList();
                                                    for(int i3=0;i3<themeTask;i3++){
                                                        newThemeUser.add(1L);
                                                    }
                                                    HashMap<String, Object> m3 = new HashMap<>();
                                                    m3.put(clickTheme,newThemeUser);
                                                    m3.put(clickTheme+"Solution",newThemeUser);
                                                    docRef.set(m3,SetOptions.merge());
                                                    allInf.put(clickTheme,newThemeUser);
                                                    allInf.put(clickTheme+"Solution",newThemeUser);
                                                    startActivity(intent);
                                                }
                                            }
                                        }
                                    });

                                }else{
                                    startActivity(intent);
                                }
                                m.put("lastTheme",((TextView)itemClicked).getText().toString());
                                docRef.set(m, SetOptions.merge());
                            }
                        }
                    }
                });
                CurrentUser.setLastTheme(((TextView)itemClicked).getText().toString());
                Menu.textViewStudy.setText(CurrentUser.lastTheme);
                MainActivity.textLoad.setVisibility(View.VISIBLE);
            }
        });
//        listView.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View arg0, MotionEvent arg1) {
//                switch (arg1.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                          handel.postDelayed(run, 3000);
//                          break;
//
//                    default:
//                        handel.removeCallbacks(run);
//                        break;
//
//                }
//                return true;
//            }
//        });
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int len=textSearch.getText().toString().length();
                String search=textSearch.getText().toString();
                search=search.toLowerCase();
                themeName.clear();
                for(int i = 0; i< Distrib.allNameTheme.size(); i++){
                    for(int j = 0; j< Distrib.allNameTheme.get(i).length()-len; j++){
                        if(Distrib.allNameTheme.get(i).charAt(j)==' '){
                            if(Distrib.allNameTheme.get(i).toLowerCase().substring(j+1,j+1+len).equals(search)){
                                themeName.add(0, Distrib.allNameTheme.get(i));
                                break;
                            }
                        }else if(j==0){
                            if(Distrib.allNameTheme.get(i).length()>=len&& Distrib.allNameTheme.get(i).toLowerCase().substring(0,len).equals(search)){
                                themeName.add(0, Distrib.allNameTheme.get(i));
                                break;
                            }
                        }
                    }
                }
                adapter.notifyDataSetChanged();
//                textSearch.setText("");
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                textSearch.clearFocus();
            }
        });
        return view;
    }


}
