package com.example.ori9933.minesweeper;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class CellManager implements ICellStateChangedListener {
    private CellState cellState;
    private Button button;
    private TextView textView;
    private View convertView;

    public CellManager(CellState cellState){

        this.cellState = cellState;
        cellState.register(this);
    }

    public void setView(View convertView) {

        if(this.convertView != null)
            return;
        this.convertView = convertView;

        button = (Button) convertView.findViewById(R.id.cell_button);
        textView = (TextView) convertView.findViewById(R.id.cell_text);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameManager.getInstance().openCell(cellState);
            }
        });

        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                GameManager.getInstance().SetCellMine(cellState);
                return true;
            }
        });

        onChange();
    }

    @Override
    public void onChange() {
        CellStatus status = cellState.getStatus();
        switch (status){
            case Mine:
                button.setVisibility(View.VISIBLE);
                textView.setVisibility(View.INVISIBLE);
                button.setText("*");
                break;
            case Opened:
                button.setVisibility(View.INVISIBLE);
                textView.setVisibility(View.VISIBLE);
                String text = cellState.getValue() == 0 ? "" : String.valueOf(cellState.getValue());
                textView.setText(text);
                break;
            case Initial:
                button.setVisibility(View.VISIBLE);
                textView.setVisibility(View.INVISIBLE);
                button.setText("");
                break;
        }
    }

    @Override
    public void onEndGame() {

    }
}
