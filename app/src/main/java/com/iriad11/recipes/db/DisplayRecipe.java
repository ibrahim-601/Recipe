package com.iriad11.recipes.db;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import com.iriad11.recipes.R;

public class DisplayRecipe extends AppCompatActivity {

    private DbHelper mydb ;

    TextView name ;
    TextView ingrdnt;
    TextView process;
    int id_To_Update = 0;

    private ImageView photo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recipe);


        name = (TextView) findViewById(R.id.name);
        ingrdnt = (TextView) findViewById(R.id.ingrdnt);
        process = (TextView) findViewById(R.id.process);
        photo = (ImageView) findViewById(R.id.img);


        mydb = new DbHelper(this);

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            int Value = extras.getInt("id");

            if(Value>0){
                //means this is the view part not the add contact part.
                Cursor rs = mydb.getData(Value);
                id_To_Update = Value;
                rs.moveToFirst();

                String nam = rs.getString(rs.getColumnIndex(DbHelper.RECIPE_COLUMN_NAME));
                String ing = rs.getString(rs.getColumnIndex(DbHelper.RECIPE_COLUMN_INGRDNT));
                String pro = rs.getString(rs.getColumnIndex(DbHelper.RECIPE_COLUMN_PROCESS));
                String link = rs.getString(rs.getColumnIndex(DbHelper.RECIPE_COLUMN_LINK));

                if (!rs.isClosed())  {
                    rs.close();
                }


                name.setText(nam);

                ingrdnt.setText(ing);

                process.setText(pro);

                RequestOptions requestOptions = new RequestOptions();;
                requestOptions.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                Glide.with(this).
                        load(link).
                        apply(requestOptions).
                        into(photo);

            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
