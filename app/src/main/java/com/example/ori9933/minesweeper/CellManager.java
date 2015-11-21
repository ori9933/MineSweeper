package com.example.ori9933.minesweeper;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class CellManager implements ICellStateChangedListener {
    private CellState cellState;
    private Button button;
    private TextView textView;
    private View convertView;
    private ImageButton imageButton;
    private ImageView imageView;

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
        imageButton = (ImageButton) convertView.findViewById(R.id.cell_image);
        imageView = (ImageView) convertView.findViewById(R.id.cell_image_result);

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

        imageButton.setOnLongClickListener(new View.OnLongClickListener() {
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
                button.setVisibility(View.INVISIBLE);
                textView.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.GONE);
                imageButton.setVisibility(View.VISIBLE);
                imageButton.getLayoutParams().height = button.getHeight() + 20;
                imageButton.setEnabled(true);
                //button.setText("*");
                break;
            case Opened:
                button.setVisibility(View.INVISIBLE);
                imageButton.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
                String text = cellState.getValue() == 0 ? "" : String.valueOf(cellState.getValue());
                textView.setText(text);
                break;
            case Initial:
                button.setVisibility(View.VISIBLE);
                imageButton.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.GONE);
                textView.setVisibility(View.INVISIBLE);
                button.setEnabled(true);
                button.setText("");
                break;
        }
    }

    @Override
    public void onEndGame() {
        CellErrorStatus error = cellState.getError();
        if(error == CellErrorStatus.Mine){
            button.setVisibility(View.INVISIBLE);
            imageButton.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.INVISIBLE);
            imageView.setImageResource(R.drawable.mine);
            imageView.setBackgroundColor(Color.rgb(255, 0, 0));
        }
        else if(error == CellErrorStatus.InvalidMine){
            button.setVisibility(View.INVISIBLE);
            imageButton.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.INVISIBLE);
            imageView.setImageResource(R.drawable.minex);
            imageView.setBackgroundColor(Color.rgb(255, 255, 255));
        }
        else if(cellState.getStatus() == CellStatus.Initial && cellState.getValue() == CellState.MINE_VALUE){
            button.setVisibility(View.INVISIBLE);
            imageButton.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.INVISIBLE);
            imageView.setImageResource(R.drawable.mine);
            imageView.setBackgroundColor(Color.rgb(255, 255, 255));
        }
        else{
            imageButton.setEnabled(false);
            button.setEnabled(false);
        }

    }
}
