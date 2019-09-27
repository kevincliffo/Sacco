package com.example.android.sacco;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        simulateDayNight(/* DAY */ 0);
        try {
            Element adsElement = new Element();
//            adsElement.setTitle("Advertise with us");

            View aboutPage = new AboutPage(this)
                    .isRTL(false)
                    .setImage(R.drawable.dummy_image)
                    .addItem(new Element().setTitle("Version 1.10"))
//                    .addItem(adsElement)
                    .addGroup("Connect with us")
                    .addEmail("prisonsaccoKE@gmail.com")
                    .addWebsite("http://www.prisonsaccoKE.co.ke")
                    .addFacebook("prisonsaccoKE")
                    .addTwitter("prisonsaccoKE")
//                    .addYoutube("UCdPQtdWIsg7_pi4mrRu46vA")
//                    .addPlayStore("com.ideashower.readitlater.pro")
                    .addInstagram("prisonsaccoKE")
//                    .addGitHub("medyo")
                    .addItem(getCopyRightsElement())
                    .create();

            setContentView(aboutPage);
        }
        catch (Exception ex)
        {
            String szMessage;
            szMessage = ex.getMessage()
                      + ex.getLocalizedMessage()
                      + ex.getStackTrace().toString();
            Toast.makeText(AboutUsActivity.this, szMessage, Toast.LENGTH_LONG).show();
        }
    }

    Element getCopyRightsElement() {
        Element copyRightsElement = new Element();
        final String copyrights = String.format(getString(R.string.copy_right), Calendar.getInstance().get(Calendar.YEAR));
        copyRightsElement.setTitle(copyrights);
        copyRightsElement.setIconDrawable(R.drawable.about_icon_copy_right);
        copyRightsElement.setIconTint(R.color.about_item_icon_color);
        copyRightsElement.setIconNightTint(android.R.color.white);
        copyRightsElement.setGravity(Gravity.CENTER);
        copyRightsElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AboutUsActivity.this, copyrights, Toast.LENGTH_SHORT).show();
            }
        });
        return copyRightsElement;
    }

    void simulateDayNight(int currentSetting) {
        final int DAY = 0;
        final int NIGHT = 1;
        final int FOLLOW_SYSTEM = 3;

        int currentNightMode = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;
        if (currentSetting == DAY && currentNightMode != Configuration.UI_MODE_NIGHT_NO) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        } else if (currentSetting == NIGHT && currentNightMode != Configuration.UI_MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        } else if (currentSetting == FOLLOW_SYSTEM) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }
}
