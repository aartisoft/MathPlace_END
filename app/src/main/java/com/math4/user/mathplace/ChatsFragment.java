package com.math4.user.mathplace;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.math4.user.mathplace.olymp.MakeContest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import static com.math4.user.mathplace.MainActivity.searchItem;
import static com.math4.user.mathplace.MainActivity.settingsItem;
import static com.math4.user.mathplace.MainActivity.viewChats;
import static com.math4.user.mathplace.MainActivity.bottomSheetBehaviorUsers;

public class ChatsFragment extends Fragment {

    View view;
    int cnt2;
    static FirebaseFirestore db;
    static RecyclerView categoriesView,categoriesViewUsers;
    DocumentReference docRef1;
    DocumentReference docRef2;
    static long all_message;
    static DocumentSnapshot documentSnapshot4;
    static FirebaseAuth mAuth;
    static FirebaseUser userThis;
    static String chatName;
    static ArrayList<String> chat = new ArrayList<String>();
    static ArrayList list = new ArrayList();
    static ArrayList lasts = new ArrayList();
    static ArrayList messages = new ArrayList();
    ChatsFragment.SubcategoriesAdapter mAdapter;
    static ArrayList<Pair<String,String>> arrayListUsers=new ArrayList<>();
    static ArrayList<ArrayList<String>> arrayListsChats=new ArrayList<>();
    static ArrayList<Subcategory> subcategoryGeometry = new ArrayList<>();
    static List<Subcategory> getRandomData() {
        for(int i = 0;i < arrayListsChats.size();i++){
            subcategoryGeometry.add(new ChatsFragment.Subcategory(arrayListsChats.get(i).get(0),arrayListsChats.get(i).get(1),arrayListsChats.get(i).get(2),arrayListsChats.get(i).get(3)));
        }

        return  subcategoryGeometry;
    }

    static List<SubcategoryUsers> getRandomDataUsers() {
        ArrayList<SubcategoryUsers> subcategoryUsers = new ArrayList<>();
        for(int i = 0;i < arrayListUsers.size();i++){
            subcategoryUsers.add(new ChatsFragment.SubcategoryUsers(arrayListUsers.get(i).first,arrayListUsers.get(i).second));
        }
        Log.e("checkcheckUserData",String.valueOf(arrayListsChats.size()));

        return  subcategoryUsers;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        db = FirebaseFirestore.getInstance();
        searchItem.setVisible(false);
        settingsItem.setVisible(false);
        mAuth= FirebaseAuth.getInstance();
        userThis=mAuth.getCurrentUser();
        arrayListsChats.clear();
        subcategoryGeometry.clear();
        chat.clear();
        list.clear();
        lasts.clear();
        messages.clear();

//        final View viewChats = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_bottom_sheet_chats, null);//Комменты
//        final BottomSheetDialog bottomSheetDialogChats = new BottomSheetDialog(getActivity());
//        bottomSheetDialogChats.setContentView(viewChats);



        FloatingActionButton fab=view.findViewById(R.id.fabChat);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<SubcategoryUsers> CircleCategories = ChatsFragment.getRandomDataUsers();
                categoriesViewUsers = viewChats.findViewById(R.id.categories_list_themeUsers);
                categoriesViewUsers.setHasFixedSize(false);
                categoriesViewUsers.setLayoutManager(new LinearLayoutManager(
                        getActivity(),
                        RecyclerView.VERTICAL,
                        false
                ));
                Log.e("checkcheckUser", String.valueOf(arrayListUsers.size()));
                final ChatsFragment.SubcategoriesUsersAdapter mAdapterUsers = new ChatsFragment.SubcategoriesUsersAdapter(CircleCategories);
                categoriesViewUsers.setAdapter(mAdapterUsers);


                bottomSheetBehaviorUsers.setState(BottomSheetBehavior.STATE_EXPANDED);


                EditText editText=viewChats.findViewById(R.id.editText);
                editText.setText("");
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        Log.e("checkcheckUserCHange",s.toString());
                        ArrayList<SubcategoryUsers> subcategoryUsers = new ArrayList<>();
                        subcategoryUsers.clear();
                        String user=s.toString();
                        if (user.length() == 0) {
                            for(int i=0;i<arrayListUsers.size();i++){
                                subcategoryUsers.add(new ChatsFragment.SubcategoryUsers(arrayListUsers.get(i).first,arrayListUsers.get(i).second));
                            }
                        } else {
                            for(int i=0;i<arrayListUsers.size();i++){
                                if (arrayListUsers.get(i).second.toLowerCase(Locale.getDefault()).contains(user)) {
                                    subcategoryUsers.add(new ChatsFragment.SubcategoryUsers(arrayListUsers.get(i).first,arrayListUsers.get(i).second));
                                }
                            }
                        }
                        mAdapterUsers.setItems(subcategoryUsers);

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        });

        return this.view;
    }



    @Override
    public void onResume() {
        super.onResume();
        db.collection("account")
                .document(userThis.getUid())
                .collection("chat")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                        final int len = task2.getResult().size();
                        cnt2=0;

                        for (final QueryDocumentSnapshot documentTask : task2.getResult()) {
//                                ArrayList<Long> thisTask=(ArrayList<Long>)m.get(documentTask.getId());
                            /// cdcddcd
                            docRef1 = db.collection("account").document(userThis.getUid()).collection("chat").document(documentTask.getId());
                            docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot documentSnapshot = task.getResult();
                                        if (documentSnapshot.exists()) {
                                            messages.add(Long.valueOf(documentSnapshot.getData().get("all_message").toString()));
                                            all_message = Long.valueOf(documentSnapshot.getData().get("all_message").toString());
                                            Log.e("LASTMESSAGE+ OLD", documentSnapshot.getId());
                                            lasts.add((ArrayList)(documentSnapshot.getData().get("message"+(Long.valueOf((messages.get(messages.size()-1)).toString()) - 1))));
                                            Log.e("LASTMESSAGE+ OLD+ HACK", String.valueOf(lasts.size()));
                                            //documentSnapshot4 = documentSnapshot;
//                                            Log.e("LASTMESSAGE", list.get(0).toString());
                                            docRef2 = db.collection("account").document(documentSnapshot.getId());
                                            docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task3) {
                                                    if (task3.isSuccessful()) {
                                                        DocumentSnapshot documentSnapshot1 = task3.getResult();
                                                        all_message = Long.valueOf(messages.get(cnt2).toString());
                                                        Log.e("LASTMESSAGE ++ ALL", String.valueOf(messages.get(cnt2).toString()));
                                                        if (documentSnapshot1.exists() && all_message > 0) {
                                                            list = (ArrayList)lasts.get(cnt2);
                                                            chat = new ArrayList<>();
                                                            String lastMessage;
                                                            chat.add(documentTask.getId());
                                                            chatName = String.valueOf(documentSnapshot1.getData().get("name"));
                                                            Log.e("LASTMESSAGE+ OLD+ HACK", documentSnapshot1.getId());
                                                            Log.e("LASTMESSAGE+ OLD+ HACK", String.valueOf(cnt2));
                                                            Log.e("LASTMESSAGE+HACK + ALL", String.valueOf(all_message));
                                                            if(!String.valueOf(list.get(1)).equals(userThis.getUid())){
                                                                lastMessage = "Вы" + ": ";
                                                            }
                                                            else{
                                                                lastMessage = chatName + ": ";
                                                            }
                                                            lastMessage += String.valueOf(list.get(0));
                                                            chat.add(lastMessage);
                                                            chat.add(chatName);
                                                            String image = "default";
                                                            try {
                                                                image = String.valueOf(documentSnapshot1.getData().get("image"));
                                                            }
                                                            catch (Exception e){
                                                                image = "default";
                                                            }
                                                            chat.add(image);
                                                            arrayListsChats.add(chat);
                                                            Log.e("checkcheckRank", "GOOD " + String.valueOf(arrayListsChats.size()));
                                                            if (len - 1 == cnt2) {
                                                                //Collections.sort(arrayListsChats, comparator);
                                                                List<Subcategory> CircleCategories = ChatsFragment.getRandomData();
                                                                categoriesView = ChatsFragment.this.view.findViewById(R.id.categories_listRank);
                                                                categoriesView.setHasFixedSize(false);
                                                                categoriesView.setLayoutManager(new LinearLayoutManager(
                                                                        getActivity(),
                                                                        RecyclerView.VERTICAL,
                                                                        false
                                                                ));
                                                                Log.e("checkcheckRankRank", String.valueOf(arrayListsChats.size()));
                                                                mAdapter = new ChatsFragment.SubcategoriesAdapter(CircleCategories);
                                                                categoriesView.setAdapter(mAdapter);
                                                            }
                                                        }
                                                    }
                                                    cnt2++;
                                                }
                                            });
                                        }
                                    }
                                }
                            });
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        db.collection("account")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                        if (task2.isSuccessful()) {
                            for (QueryDocumentSnapshot documentTask : task2.getResult()) {//здесь мы бежим по всем темам
                                arrayListUsers.add(new Pair<String, String>(documentTask.getId(),documentTask.getData().get("name").toString()));
                            }
                        }
                    }
                });

    }

    static class Subcategory {
        String lastMessage;
        String chatName;
        String id;
        String image;
        int position;
        Subcategory(String id,String lastMessage,String chatName, String image){
            this.id = id;
            this.lastMessage = lastMessage;
            this.chatName = chatName;
            this.image = image;
        }
    }

    static final class SubcategoriesAdapter extends RecyclerView.Adapter<SubcategoriesAdapter.ViewHolder>{

        List<Subcategory> data;

        SubcategoriesAdapter(List<Subcategory> data){ this.data = data;}

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item, parent, false);
            return new ChatsFragment.SubcategoriesAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final ChatsFragment.Subcategory subcategory = data.get(position);
            holder.chatName.setText(subcategory.chatName);
            holder.lastMessage.setText(subcategory.lastMessage);
            Log.e("IMAGE", subcategory.image);
            if(!subcategory.image.equals("default") && !subcategory.image.equals("null")) {
                Glide.with(holder.itemView).load(subcategory.image).into(holder.chatImage);
                Log.e("IMAGE", "Image");
            }
            else {
                holder.chatImage.setImageResource(R.drawable.account_new);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent( categoriesView.getContext() , MessageActivity.class);
                    intent.putExtra("userid", subcategory.id );
                    categoriesView.getContext().startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder{
            TextView lastMessage, chatName;
            ImageView chatImage;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                lastMessage = itemView.findViewById(R.id.chatText);
                chatName = itemView.findViewById(R.id.chatName);
                chatImage = itemView.findViewById(R.id.chat_image);
            }
        }
    }








    static class SubcategoryUsers {
        String id,name;

        SubcategoryUsers(String id,String name){
            this.id = id;
            this.name = name;
        }
    }


    static final class SubcategoriesUsersAdapter extends RecyclerView.Adapter<SubcategoriesUsersAdapter.ViewHolder>{

        List<SubcategoryUsers> data;

        SubcategoriesUsersAdapter(List<SubcategoryUsers> data){ this.data = data;}

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.subcategory_list_item_user, parent, false);
            return new ChatsFragment.SubcategoriesUsersAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final ChatsFragment.SubcategoryUsers subcategory = data.get(position);
            holder.userName.setText(subcategory.name);


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent( categoriesViewUsers.getContext().getApplicationContext() , MessageActivity.class);
                    intent.putExtra("userid", subcategory.id );
                    categoriesViewUsers.getContext().getApplicationContext().startActivity(intent);
                }
            });
        }

        public void setItems(List<ChatsFragment.SubcategoryUsers> tweet) {
            data=tweet;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder{
            TextView userName;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                userName = itemView.findViewById(R.id.textViewUser);
            }
        }
    }
}

