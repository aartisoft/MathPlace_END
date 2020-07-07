package com.math4.user.mathplace.olymp;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.math4.user.mathplace.Distrib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListData {
    public static HashMap<String, List<String>> loadData() {
        final HashMap<String, List<String>> expDetails = new HashMap<>();

        FirebaseFirestore db=FirebaseFirestore.getInstance();
        final Map<String, ArrayList> map=new HashMap<>();
        db.collection("task2")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                        if (task2.isSuccessful()) {
                            for (QueryDocumentSnapshot documentTask : task2.getResult()) {//здесь мы бежим по всем темам
//                                    Log.e("checkcheckDocumentTask",documentTask.getId());
//                                map[documentTask.getData().get("theme").toString()].add(documentTask.getId());
                            }
                        }
                    }
                });


//        expDetails.put("ОГЭ", Distrib.OGENameTheme);
//        expDetails.put("Геометрия", Distrib.GeometryNameTheme);
//        expDetails.put("Алгебра", Distrib.AlgebraNameTheme);
//        expDetails.put("Комбинаторика", Distrib.KombaNameTheme);
//        expDetails.put("Логика", Distrib.LogikaNameTheme);
//        expDetails.put("Графы", Distrib.GrafNameTheme);

        return expDetails;
    }
}
