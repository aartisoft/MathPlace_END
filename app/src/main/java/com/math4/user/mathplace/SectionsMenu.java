package com.math4.user.mathplace;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SectionsMenu extends Fragment {

    View view;
    int cnt2;
    static FirebaseFirestore db;
    static RecyclerView categoriesView;
    DocumentReference docRef1;
    DocumentReference docRef2;
    static long all_message;
    static DocumentSnapshot documentSnapshot4;
    static FirebaseAuth mAuth;
    static FirebaseUser userThis;
    static String chatName;
    static ArrayList<String> chat = new ArrayList<String>();
    SubcategoriesAdapter mAdapter;
    static ArrayList<String> arrayListThemes =new ArrayList<>();
    static List<Subcategory> getRandomData() {
        ArrayList<Subcategory> subcategoryGeometry = new ArrayList<>();
        for(int i = 0; i < arrayListThemes.size()-1; i+=2){
            subcategoryGeometry.add(new Subcategory(arrayListThemes.get(i), arrayListThemes.get(i+1)));
        }


        Log.e("SIZEEE", String.valueOf(subcategoryGeometry.size()));
        return  subcategoryGeometry;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sectionsmenu, container, false);
        db = FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();
        userThis=mAuth.getCurrentUser();
        arrayListThemes.clear();

        arrayListThemes.add("огэ");
        arrayListThemes.add(String.valueOf(Distrib.ogeNameTheme.size()));
        arrayListThemes.add("геометрия");
        arrayListThemes.add(String.valueOf(Distrib.geometryNameTheme.size()));
        arrayListThemes.add("алгебра");
        arrayListThemes.add(String.valueOf(Distrib.algebraNameTheme.size()));
        arrayListThemes.add("школа");
        arrayListThemes.add(String.valueOf(Distrib.schoolNameTheme.size()));
        arrayListThemes.add("комбинаторика");
        arrayListThemes.add(String.valueOf(Distrib.kombaNameTheme.size()));
        arrayListThemes.add("логика");
        arrayListThemes.add(String.valueOf(Distrib.logikaNameTheme.size()));
        arrayListThemes.add("графы");
        arrayListThemes.add(String.valueOf(Distrib.graphNameTheme.size()));

        List<Subcategory> CircleCategories = SectionsMenu.getRandomData();
        Log.e("DATA CIRCLE", String.valueOf(CircleCategories.size()));
        Log.e("DATA CIRCLE", String.valueOf(CircleCategories.get(0).name));
        Log.e("DATA CIRCLE", String.valueOf(CircleCategories.get(6).count));
        mAdapter = new SubcategoriesAdapter(CircleCategories);
        categoriesView = view.findViewById(R.id.themesList);
        categoriesView.setHasFixedSize(true);
        categoriesView.setLayoutManager(new LinearLayoutManager(
                getActivity(),
                RecyclerView.VERTICAL,
                false
        ));
        categoriesView.setAdapter(mAdapter);

        return this.view;
    }


    static class Subcategory {
        String name;
        String count;
        Subcategory(String name, String count){
            this.name = name;
            this.count = count;
        }
    }

    static final class SubcategoriesAdapter extends RecyclerView.Adapter<SubcategoriesAdapter.ViewHolder>{

        List<Subcategory> data;

        SubcategoriesAdapter(List<Subcategory> data){ this.data = data;
        Log.e("DATA", String.valueOf(this.data.size()));}

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sections_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final Subcategory subcategory = data.get(position);
            holder.themeCount.setText("Всего "+subcategory.count+" тем");
            if(subcategory.name.equals("школа"))
                holder.themeImage.setImageResource(R.drawable.school);
            else if(subcategory.name.equals("алгебра"))
                holder.themeImage.setImageResource(R.drawable.algebra);
            else if(subcategory.name.equals("геометрия"))
                holder.themeImage.setImageResource(R.drawable.geometry);
            else if(subcategory.name.equals("логика"))
                holder.themeImage.setImageResource(R.drawable.logic);
            else if(subcategory.name.equals("комбинаторика"))
                holder.themeImage.setImageResource(R.drawable.komba);
            else if(subcategory.name.equals("графы"))
                holder.themeImage.setImageResource(R.drawable.graph);
            else if(subcategory.name.equals("огэ"))
                holder.themeImage.setImageResource(R.drawable.examination);
            Log.e("SECTIONS VIEW", "ТУц");
            String name =  subcategory.name.substring(0,1).toUpperCase() + subcategory.name.substring(1).toLowerCase();
            holder.themeTitile.setText(name);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent( categoriesView.getContext() , ThemeActivity.class);
                    intent.putExtra("theme_name", subcategory.name );
                    categoriesView.getContext().startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return 7;
        }

        static class ViewHolder extends RecyclerView.ViewHolder{
            TextView themeTitile, themeCount;
            ImageView themeImage;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                themeTitile = itemView.findViewById(R.id.themeTitle);
                themeCount = itemView.findViewById(R.id.themeCount);
                themeImage = itemView.findViewById(R.id.themeImage);
            }
        }
    }
}
