package com.xtraqueur.mzom.xtraqueur;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.graphics.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        LinearLayout mainlayout = (LinearLayout) findViewById(R.id.mainLayout);

        mainlayout.removeAllViews();

        setTitle("Din historikk");

        SharedPreferences countStorage = PreferenceManager.getDefaultSharedPreferences(this);
        String histStorage = countStorage.getString("histStorage","");

        final List<String> histList = new ArrayList<>(Arrays.asList(histStorage.split(",")));

        if(histList.size() == 0 || histStorage.equals("")){
            TextView dynamicTextView = new TextView(this);
            dynamicTextView.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
            dynamicTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            dynamicTextView.setText("\n" + "Ingen historikk");
            dynamicTextView.setLayoutParams(new TableRow.LayoutParams(1));

            mainlayout = (LinearLayout) findViewById(R.id.mainLayout);

            mainlayout.addView(dynamicTextView);

            TextView clearHist = (TextView) findViewById(R.id.clearHist);
            clearHist.setVisibility(View.INVISIBLE);

        }else{
            for(int t=0;t<histList.size();t++){
                if(!histList.get(t).equals("")){
                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                            ActionBar.LayoutParams.MATCH_PARENT,
                            ActionBar.LayoutParams.MATCH_PARENT
                    );
                    param.weight = 1;
                    param.setMargins(50, 20, 50, 20);

                    TextView dynamicTextView = new TextView(this);
                    dynamicTextView.setLayoutParams(param);
                    dynamicTextView.setGravity(Gravity.CENTER);
                    String text = histList.get(t);
                    dynamicTextView.setText(text);
                    dynamicTextView.setId(t);
                    dynamicTextView.setTextSize(17);
                    dynamicTextView.setBackgroundColor(Color.parseColor("#323232"));
                    dynamicTextView.setTextColor(Color.parseColor("#eeeeee"));

                    dynamicTextView.setPadding(50, 50, 50, 50);

                    mainlayout = (LinearLayout) findViewById(R.id.mainLayout);

                    mainlayout.addView(dynamicTextView);

                    final TextView clearHist = (TextView) findViewById(R.id.clearHist);
                    clearHist.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            clearHistory();
                        }
                    });
            }}
            for(int k=0;k<histList.size();k++){
                final int clearNum = k;
                final String id = String.valueOf(clearNum);
                TextView button = (TextView) findViewById(getResources().getIdentifier(id, "id", getPackageName()));
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        showMore(clearNum);
                    }
                });
            }
        }
    }

    private void showMore(final int infoNum){
        final SharedPreferences countStorage = PreferenceManager.getDefaultSharedPreferences(this);
        String histDetailStorage = countStorage.getString("histDetailStorage","");
        final List<String> histDetailList = new ArrayList<>(Arrays.asList(histDetailStorage.split(",")));

        Context context = this;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle("Info om oppgave");
        builder.setMessage(histDetailList.get(infoNum));
        builder.setPositiveButton(("Slett"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        clearSolo(infoNum);
                    }
                });
        builder.setNegativeButton("Lukk", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        Button dP = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        dP.setTextColor(Color.parseColor("#f44242"));
    }

    private void clearSolo(int clearNum){

        SharedPreferences countStorage = PreferenceManager.getDefaultSharedPreferences(this);
        String histStorage = countStorage.getString("histStorage",null);

        List<String> histList = new ArrayList<>(Arrays.asList(histStorage.split(",")));

        histList.remove(clearNum);

        histStorage = "";

        for(int k=0;k<histList.size();k++) {
            if (k == 0) {
                histStorage = histList.get(k);
            } else {
                histStorage = histStorage + "," + histList.get(k);
            }
        }

        String histDetailStorage = countStorage.getString("histDetailStorage",null);

        List<String> histDetailList = new ArrayList<>(Arrays.asList(histDetailStorage.split(",")));

        histDetailList.remove(clearNum);

        histDetailStorage = "";

        for(int k=0;k<histDetailList.size();k++) {
            if (k == 0) {
                histDetailStorage = histDetailList.get(k);
            } else {
                histDetailStorage = histDetailStorage + "," + histDetailList.get(k);
            }
        }

        SharedPreferences.Editor editor = countStorage.edit();
        editor.putString("histStorage",histStorage);
        editor.putString("histDetailStorage",histDetailStorage);
        editor.apply();

        finish();
        overridePendingTransition( R.anim.slide_in_right, R.anim.slide_out_left );

    }

    public void clearHistory(){
        final SharedPreferences countStorage = PreferenceManager.getDefaultSharedPreferences(this);
        final Context context = this;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle("Er du sikker?");
        builder.setMessage((R.string.confirmDialogB));
        builder.setPositiveButton((R.string.dialogJa),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SharedPreferences.Editor editor = countStorage.edit();
                        editor.putString("histStorage","");
                        editor.apply();

                        LinearLayout mainlayout = (LinearLayout) findViewById(R.id.mainLayout);

                        mainlayout.removeAllViews();

                        TextView clearHist = (TextView) findViewById(R.id.clearHist);
                        clearHist.setVisibility(View.INVISIBLE);

                        finish();
                    }
                });
        builder.setNegativeButton(R.string.dialogNei, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        Button dP = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        dP.setTextColor(getResources().getColor(R.color.deny));

    }
}
