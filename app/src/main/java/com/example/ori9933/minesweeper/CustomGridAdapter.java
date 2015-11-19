package com.example.ori9933.minesweeper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.ArrayList;


public class CustomGridAdapter extends BaseAdapter {
    ArrayList<CellManager> cellManagers;
    private LayoutInflater inflater;


    public CustomGridAdapter(LayoutInflater inflater){
        this.inflater = inflater;
        this.cellManagers = new ArrayList<>();

        CellState[][] cells = GameManager.getInstance().GetCellsStates();
        for(int i = 0; i < GameManager.GAME_SIZE; i++){
            for (int j =0; j< GameManager.GAME_SIZE; j++){
                this.cellManagers.add(new CellManager(cells[i][j]));
            }
        }
    }


    @Override
    public int getCount() {
        return cellManagers.size();
    }

    @Override
    public Object getItem(int position) {
        return cellManagers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.cell_layout, null);
            cellManagers.get(position).setView(convertView);
        }



        return convertView;
    }
}
