package com.math4.user.mathplace;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.math4.user.mathplace.R;

public class AboutApp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        Toolbar toolbar = findViewById(R.id.toolbarBookmark);
        setSupportActionBar(toolbar);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("О приложение");

        TextView textViewFeedback=findViewById(R.id.reviewAboutApp);
        String textWithLink = "Написать отзыв вы можете по "+"<a href=\"https://play.google.com/store/apps/details?id=com.math4.user.mathplace&hl=ru\">ссылке</a>";
//Указываем с помощью Html.fromHtml, что у нас не просто текст:
        textViewFeedback.setText(Html.fromHtml(textWithLink, null, null));
////Указываем что разрешаем ссылки кликать:
        textViewFeedback.setLinksClickable(true);
        textViewFeedback.setMovementMethod(LinkMovementMethod.getInstance());
//Научаемся отлавливать клики пропустив текст через наш класс из пред. пункта.
        CharSequence text = textViewFeedback.getText();
        if (text instanceof Spannable)
        {
            textViewFeedback.setText(MakeLinksClicable.reformatText(text));
        }

        TextView textViewFeedback2=findViewById(R.id.helpAboutApp);
        String textWithLink2 = "Предложить свои задачи  вы можете по "+"<a href=\"http://ledokolpro.tilda.ws/\">ссылке</a>";
//Указываем с помощью Html.fromHtml, что у нас не просто текст:
        textViewFeedback2.setText(Html.fromHtml(textWithLink2, null, null));
////Указываем что разрешаем ссылки кликать:
        textViewFeedback2.setLinksClickable(true);
        textViewFeedback2.setMovementMethod(LinkMovementMethod.getInstance());
//Научаемся отлавливать клики пропустив текст через наш класс из пред. пункта.
        CharSequence text2 = textViewFeedback2.getText();
        if (text2 instanceof Spannable)
        {
            textViewFeedback2.setText(MakeLinksClicable.reformatText(text2));
        }


        TextView textViewFeedback3=findViewById(R.id.translateAboutApp);
        String textWithLink3 = "В переводе приложения нам помогла "+"<a href=\"https://vk.com/eleanor3030\">Элеонора</a>. Обращайтесь к ней за помощью в переводе, она всем будет рада!";
//Указываем с помощью Html.fromHtml, что у нас не просто текст:
        textViewFeedback3.setText(Html.fromHtml(textWithLink3, null, null));
////Указываем что разрешаем ссылки кликать:
        textViewFeedback3.setLinksClickable(true);
        textViewFeedback3.setMovementMethod(LinkMovementMethod.getInstance());
//Научаемся отлавливать клики пропустив текст через наш класс из пред. пункта.
        CharSequence text3 = textViewFeedback3.getText();
        if (text2 instanceof Spannable)
        {
            textViewFeedback3.setText(MakeLinksClicable.reformatText(text3));
        }


    }
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bookmark, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    public void backApp(View view){
        finish();
    }
}