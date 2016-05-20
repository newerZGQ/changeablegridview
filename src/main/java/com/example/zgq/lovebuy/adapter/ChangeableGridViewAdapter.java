package com.example.zgq.lovebuy.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.zgq.lovebuy.R;

import java.util.ArrayList;

/**
 * Created by 37902 on 2016/1/23.
 */
public class ChangeableGridViewAdapter extends ArrayAdapter {
    private ArrayList<String> list;
    private Context context;
    private String lableSelected;
    private OnGridViewChangeListener mListener;

    public ChangeableGridViewAdapter(Context context, ArrayList<String> list, OnGridViewChangeListener mListener) {
        super(context, 0);
        this.list = list;
        this.context = context;
        this.mListener = mListener;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.listitem_lables_grid, null);
        final TextView textView = (TextView) view.findViewById(R.id.lable_textview);
        textView.setText(list.get(position));
        if (list.get(position).equals("+")) {
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onAddChildView();
                }
            });

        } else {
            textView.setFocusableInTouchMode(true);
            textView.setClickable(true);
            textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        if (list.get(position) != "+")
                            mListener.onSelectedChange(list.get(position));
                    }
                }
            });
            textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mListener.onDeleteChildView(((TextView)v).getText().toString());
                    return true;
                }
            });
        }
        return view;
    }

    public String getLableSelected() {
        return lableSelected;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
    public interface OnGridViewChangeListener {
        public void onAddChildView();
        public void onDeleteChildView(String lable);
        public void onSelectedChange(String lable);
    }
}
