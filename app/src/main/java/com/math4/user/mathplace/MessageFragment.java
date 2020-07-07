package com.math4.user.mathplace;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MessageFragment extends Fragment {

    public static final int MSG_TYPE_LEFT = 0; // Сообщение получено
    public static final int MSG_TYPE_RIGHT = 1; // Сообщение отправлено
    static View view;
    static SubcategoriesAdapter mAdapter = new SubcategoriesAdapter(null);
    static long allMessage;
    static String to;
    static Context context;
    static Context mContext;
    static TextView textView;
    static View view2;
    static Context context1;
    static FragmentManager fragmentManager;
    static FirebaseUser userThis;
    FirebaseAuth mAuth;
    static RecyclerView categoriesView;
    static FirebaseFirestore db;
    static ArrayList<Integer> receivers = new ArrayList<>();
    static ArrayList<Subcategory> subcategoryGeometry = new ArrayList<>();
    DoneTimer timer = new DoneTimer(100000000, 2000); // объявляем таймер обновления чата
    static List<Subcategory> getRandomData() {
        long i = 0;
        while (i< allMessage){
            subcategoryGeometry.add(new MessageFragment.Subcategory(1," ","")); // Заполняем массив сообщений
            Log.e("CREATE", String.valueOf(subcategoryGeometry.size()));
            i++;
        }
        return subcategoryGeometry;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragmen_messages, container, false);

        view2= inflater.inflate(R.layout.chat_item_right, container, false);
        mContext = view2.getContext();
        textView = view2.findViewById(R.id.show_message);
        context = getContext();
        db= FirebaseFirestore.getInstance();
        fragmentManager = getFragmentManager();
        mAuth= FirebaseAuth.getInstance();
        userThis=mAuth.getCurrentUser();
        subcategoryGeometry.clear();
        receivers.clear();
        allMessage = 0;
        mAdapter = null;
        to = MessageActivity.userID;
        context1 = getContext();


        DocumentReference ddocRef = db.collection("account").document(userThis.getUid()).collection("chat").document(to);
        ddocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        final Map<String, Object> m = documentSnapshot.getData();
                        allMessage = Long.parseLong(m.get("all_message").toString());
                        DocumentReference docRef = db.collection("account").document(userThis.getUid()).collection("chat").document(to);
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if (documentSnapshot.exists()) {
                                        final Map<String, Object> m = documentSnapshot.getData();
                                        int i=0;
                                        while (i< allMessage) {
                                            if(((ArrayList) m.get("message" + i)).get(1).toString().equals(to)){
                                                receivers.add(MSG_TYPE_RIGHT);
                                            }
                                            else {
                                                receivers.add(MSG_TYPE_LEFT);
                                            }
                                            i++;
                                        }
                                        List<Subcategory> CircleCategories = MessageFragment.getRandomData();
                                        categoriesView = view.findViewById(R.id.recycler_view);
                                        categoriesView.setHasFixedSize(false);
                                        categoriesView.setLayoutManager(new LinearLayoutManager(
                                                getActivity(),
                                                RecyclerView.VERTICAL,
                                                false
                                        ));
                                        mAdapter = new SubcategoriesAdapter(CircleCategories);
                                        categoriesView.setAdapter(mAdapter);
                                        categoriesView.scrollToPosition((int) allMessage -1);
                                    }
                                }
                            }
                        });

                    }
                }
            }

        });

        timer.start();

        return view;
    }

    public static void update(){
        if(allMessage == 0) { // Добавляем первое сообщение чата
            allMessage++;
            receivers.add(MSG_TYPE_RIGHT);
            List<Subcategory> CircleCategories = MessageFragment.getRandomData();
            mAdapter = new SubcategoriesAdapter(CircleCategories);
            categoriesView = view.findViewById(R.id.recycler_view);
            categoriesView.setHasFixedSize(false);
            categoriesView.setLayoutManager(new LinearLayoutManager(
                    context1,
                    RecyclerView.VERTICAL,
                    false
            ));
            categoriesView.setAdapter(mAdapter);
        }
        else{
            mAdapter.refreshData("right"); // Пользователь отправил сообщение
        }
        categoriesView.scrollToPosition(mAdapter.getItemCount());
    }

    public static void updateNews(){
        DocumentReference ddocRef = db.collection("account").document(userThis.getUid()).collection("chat").document(to);
        ddocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        final Map<String, Object> m = documentSnapshot.getData();
                        if(allMessage < Long.parseLong(m.get("all_message").toString())) { // Проверяем пришли ли новые сообщения
                            while (allMessage < Long.parseLong(m.get("all_message").toString())) { // Обновляем чат
                                Log.e("HEh", Long.parseLong(m.get("all_message").toString()) + " " + allMessage);
                                if(allMessage == 0){
                                    update(); // Вызываем функцию с созданием чата
                                }
                                else {
                                    if (!((ArrayList) m.get("message" + (allMessage))).get(1).toString().equals("null")) {
                                        if (((ArrayList) m.get("message" + (allMessage))).get(1).toString().equals(userThis.getUid())) {
                                            Log.e("LEFT", userThis.getUid() + " " + ((ArrayList) m.get("message" + (allMessage - 1))).get(1).toString());
                                            Log.e("LEFT",  allMessage + " " + ((ArrayList) m.get("message" + (allMessage - 1))).get(0).toString());
                                            mAdapter.refreshData("left"); // Пользователь получил сообщение
                                        } else {
                                            Log.e("RIGHT", userThis.getUid() + " " + ((ArrayList) m.get("message" + (allMessage - 1))).get(1).toString());
                                            Log.e("RIGHT",  allMessage + " " + ((ArrayList) m.get("message" + (allMessage - 1))).get(0).toString());
                                            mAdapter.refreshData("right"); // Пользователь отправил сообщение
                                        }
                                    }
                                }
                            }
                            categoriesView.scrollToPosition((int) allMessage -1);
                        }
                    }
                }
            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    static class Subcategory {
        String message;
        int position;
        String id;

        Subcategory(int position, String id, String message) {
            this.position = position;
            this.id = id;
            this.message = message;
        }
    }


    //----------------------------------------------------------------------------------------------


    static final class SubcategoriesAdapter extends RecyclerView.Adapter<SubcategoriesAdapter.ViewHolderL> {
        List<Subcategory> data;

        SubcategoriesAdapter(List<Subcategory> data) {
            this.data = data;
        }


        public void refreshData(String type){
            allMessage++;
            if(type.equals("right")){
                receivers.add(MSG_TYPE_RIGHT); //Пользователь отправил сообщение
            }
            else if(type.equals("left")){
                receivers.add(MSG_TYPE_LEFT); // Пользователь получил сообщение
            }
            if(data==null){
                data = new ArrayList<>();
            }
            data.add(new MessageFragment.Subcategory(1," ","")); // Добавляем новое сообщение
            notifyItemInserted(data.size()-1); // Инициализируем обновление Адаптера
        }

        @Override
        public ViewHolderL onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == MSG_TYPE_RIGHT) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
                return new SubcategoriesAdapter.ViewHolderL(view);
            } else {
                View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
                return new SubcategoriesAdapter.ViewHolderL(view);
            }
        }



        @Override
        public void onBindViewHolder(final SubcategoriesAdapter.ViewHolderL holder, final int position) {
            final MessageFragment.Subcategory subcategory = data.get(position);
            final DocumentReference docRes3 = db.collection("account").document(userThis.getUid()).collection("chat").document(to);
            docRes3.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()) {
                            final Map<String, Object> m = documentSnapshot.getData();
                            if(MessageActivity.lAll == 0){
                                holder.subcategoryText.setText(MessageActivity.lMsg); // В случае если БД не успела добавить данные сообщения
                            }
                            else{
                                holder.subcategoryText.setText(((ArrayList) m.get("message" + position)).get(0).toString());
                            }
                            Timestamp timestamp = (Timestamp)(m.get("message"+position+"time")); // Получаем время отправки
                            String time;
                            if(timestamp != null) {
                                time = getDate(timestamp.getSeconds());
                            }
                            else {
                                Calendar cal = Calendar.getInstance(Locale.ENGLISH); // Получаем время в данный момент
                                time = DateFormat.format("HH:mm", cal).toString(); // Переводим время в данный момент в формат Часы:Минуты
                            }
                            holder.subcategoryTime.setText(time);
                        }
                    }
                }
            });
            holder.subcategoryPosition = subcategory.position;
        }

        private String getDate(long time) {
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(time * 1000);
            String date = DateFormat.format("HH:mm", cal).toString();
            return date;
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class ViewHolderL extends RecyclerView.ViewHolder {
            TextView subcategoryText;
            TextView subcategoryTime;
            ImageView subcategoryImage;
            int subcategoryPosition;
            RelativeLayout layout;

            ViewHolderL(View itemView) {
                super(itemView);
                subcategoryText = itemView.findViewById(R.id.show_message);
                subcategoryTime = itemView.findViewById(R.id.show_time);
                subcategoryImage = itemView.findViewById(R.id.profile_image);
                layout = itemView.findViewById(R.id.relativelayout);
            }
        }

        int pos;
        @Override
        public int getItemViewType(final int position) {
            if(receivers.get(position).equals(MSG_TYPE_LEFT)){
                pos = MSG_TYPE_LEFT;
            }
            else{
                pos = MSG_TYPE_RIGHT;
            }
            return pos;
        }

    }

    public void newTimer(){
        timer.cancel();
        timer = new DoneTimer(100000000, 2000);
    }

    class DoneTimer extends CountDownTimer {

        public DoneTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            updateNews();
        }

        @Override
        public void onFinish() {
            newTimer();
        }
    }

}
