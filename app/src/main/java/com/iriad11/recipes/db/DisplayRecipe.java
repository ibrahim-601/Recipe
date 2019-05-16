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

import mrcrow.recipes.R;

public class DisplayRecipe extends AppCompatActivity {

    private DbHelper mydb ;

    TextView name ;
    TextView ingrdnt;
    TextView process;
    int id_To_Update = 0;
    private AdView mAdView1, mAdView2, mAdView3;
    private AdRequest adRequest;
    private InterstitialAd mInterstitialAd;
    private ImageView photo;
    private InterstitialAd mInterstitialAd1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recipe);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-8565635437354736/4750103654"); //robin
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                mInterstitialAd.show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });


        mInterstitialAd1 = new InterstitialAd(this);
        mInterstitialAd1.setAdUnitId("ca-app-pub-2675122866814450/3948055581"); //ibrahim
        mInterstitialAd1.loadAd(new AdRequest.Builder().build());
        mInterstitialAd1.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                mInterstitialAd1.loadAd(new AdRequest.Builder().build());
            }
        });


        name = (TextView) findViewById(R.id.name);
        ingrdnt = (TextView) findViewById(R.id.ingrdnt);
        process = (TextView) findViewById(R.id.process);
        photo = (ImageView) findViewById(R.id.img);

        mAdView1 = (AdView) findViewById(R.id.adView1);
        adRequest = new AdRequest.Builder().build();
        mAdView1.loadAd(adRequest);

        mAdView2 = (AdView) findViewById(R.id.adView2);
        adRequest = new AdRequest.Builder().build();
        mAdView2.loadAd(adRequest);

        mAdView3 = (AdView) findViewById(R.id.adView3);
        adRequest = new AdRequest.Builder().build();
        mAdView3.loadAd(adRequest);

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
        mInterstitialAd1.show();
        super.onBackPressed();
    }
}
