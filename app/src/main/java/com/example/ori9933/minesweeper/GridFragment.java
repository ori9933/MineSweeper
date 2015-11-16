package com.example.ori9933.minesweeper;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class GridFragment extends Fragment {
    public GridFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_grid, container, false);
        GridView gridview = (GridView) rootView.findViewById(R.id.gridview);

        CustomGridAdapter adapter = new CustomGridAdapter(inflater);
        gridview.setAdapter(adapter);

        return rootView;
    }
}
