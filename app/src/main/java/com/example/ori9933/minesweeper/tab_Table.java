package com.example.ori9933.minesweeper;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;


public class tab_Table extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_tab__table, container, false);

        TableLayout tableLayout = (TableLayout) rootView.findViewById(R.id.records_table_layout);
        TextView textView = (TextView) rootView.findViewById(R.id.scores_name_text);
        LinearLayout.LayoutParams paramsText = (LinearLayout.LayoutParams)textView.getLayoutParams();
        TableRow tableRow = (TableRow) rootView.findViewById(R.id.scores_tablerow);
        LinearLayout.LayoutParams paramsTablerow = (LinearLayout.LayoutParams)tableRow.getLayoutParams();


        ArrayList<Record> records = UserRecordsManager.GetAllRecords();

        for (Record record: records) {
            TableRow row = CreateTableRow(paramsTablerow);

            TextView nameTv = CreateTextView(paramsText);
            nameTv.setText(record.getName());

            TextView scoreTv = CreateTextView(paramsText);
            scoreTv.setText(String.valueOf(record.getScore()));

            row.addView(nameTv);
            row.addView(scoreTv);
            tableLayout.addView(row);
        }

        return rootView;
    }

    private TableRow CreateTableRow(LinearLayout.LayoutParams params){
        TableRow row= new TableRow(getContext());
        row.setLayoutParams(params);
        row.setBackgroundColor(Color.WHITE);
        return row;
    }

    private TextView CreateTextView(LinearLayout.LayoutParams params){
        TextView tv = new TextView(getContext());
        tv.setLayoutParams(params);
        return tv;
    }
}
