package com.intentfuzzert.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.intentfuzzert.R;
import com.intentfuzzert.bean.MainMenuAdapter;

public class MainActivity extends AppCompatActivity {


    private GridView gridView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        gridView=(GridView)findViewById(R.id.gridview);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridView.setAdapter(new MainMenuAdapter(this));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            public void onItemClick(AdapterView<?> parent, View view, int position, long id){

                if(position==0){

                    Intent intent=new Intent(MainActivity.this,AppBrowserActivity.class);
                    intent.putExtra("type",1);
                    startActivity(intent);
                }

                if(position==1){

                    Intent intent=new Intent(MainActivity.this,AppBrowserActivity.class);
                    intent.putExtra("type",1);
                    startActivity(intent);
                }

                if(position==2){
                    Intent intent=new Intent(MainActivity.this,AppBrowserActivity.class);
                    intent.putExtra("type",2);
                    startActivity(intent);

                }

                if(position==3){
                    Intent intent=new Intent(MainActivity.this,AppBrowserActivity.class);
                    intent.putExtra("type",3);
                    startActivity(intent);


                }

            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


}
