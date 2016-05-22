package com.example.zgq.changeablegridview.ui.activity;

import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.widget.GridView;
import android.widget.TextView;

import com.example.zgq.changeablegridview.R;
import com.example.zgq.changeablegridview.adapter.ChangeableGridViewAdapter;
import com.example.zgq.changeablegridview.ui.myUI.InputDetailDialog;
import com.example.zgq.changeablegridview.util.SharedPreferencesUtil;

import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ChangeableGridViewAdapter.OnGridViewChangeListener {

    private Toolbar toolbar;
    private GridView lablesGv;
    private TextView statusbar;
    private TextView textView;

    private ChangeableGridViewAdapter adapter;
    private ArrayList<String> costList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_consum);
        initView();
        try {
            initDate();
            setViewContent();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initDate() throws JSONException {
        SharedPreferencesUtil.initLablesSharedPf(this);
        costList = new ArrayList<>();
        costList = SharedPreferencesUtil.getCostLablesList(this);
        costList.add(getString(R.string.action_add_consum_lable));
    }

    public void initView() {
        statusbar = (TextView) findViewById(R.id.status_bar);
        if (Build.VERSION.SDK_INT < 21) {
            statusbar.setVisibility(View.GONE);
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        lablesGv = (GridView) findViewById(R.id.gv_lables);
        textView = (TextView) findViewById(R.id.tv);
    }


    public void setViewContent() throws JSONException {
        toolbar.setTitle(getResources().getString(R.string.add_consum_toolbar_title));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adapter = new ChangeableGridViewAdapter(this, costList, this);
        lablesGv.setClickable(false);
        lablesGv.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.confirm_toolbar_menu, menu);
        return true;
    }

    public void changeLablesList() throws JSONException {
        ArrayList<String> arrayList = SharedPreferencesUtil.getCostLablesList(this);
        costList.clear();
        for (String s : arrayList) {
            costList.add(s);
            costList.add(getString(R.string.action_add_consum_lable));
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onAddChildView() {
        final InputDetailDialog detailDialog = new InputDetailDialog(this, 0, getString(R.string.less_three_chart));
        detailDialog.setOnGetDetailListener(new InputDetailDialog.OnGetDetailListener() {
            @Override
            public void onGetDetail() {
                String newLable = detailDialog.getDetail();
                if (newLable.length() > 3 || newLable.length() == 0) return;
                SharedPreferencesUtil.putCostLablesList(MainActivity.this, newLable);
                ArrayList<String> arrayList = SharedPreferencesUtil.getCostLablesList(MainActivity.this);
                costList.clear();
                for (String s : arrayList) {
                    costList.add(s);
                }
                costList.add(getString(R.string.action_add_consum_lable));
                adapter = new ChangeableGridViewAdapter(MainActivity.this, costList, MainActivity.this);
                lablesGv.setAdapter(adapter);
            }
        });
        detailDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        detailDialog.show();
    }

    @Override
    public void onDeleteChildView(final String lable) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setMessage(R.string.yes_to_delete)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        SharedPreferencesUtil.deleteCostLablesList(MainActivity.this, lable);
                        ArrayList<String> arrayList = SharedPreferencesUtil.getCostLablesList(MainActivity.this);
                        costList.clear();
                        for (String s : arrayList) {
                            costList.add(s);
                        }
                        costList.add(getString(R.string.action_add_consum_lable));
                        adapter = new ChangeableGridViewAdapter(MainActivity.this, costList, MainActivity.this);
                        lablesGv.setAdapter(adapter);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                }).show();
    }

    @Override
    public void onSelectedChange(String lable) {
        textView.setText(lable);
    }
}
