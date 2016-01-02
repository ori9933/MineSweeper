package com.example.ori9933.minesweeper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class CellManager implements ICellStateChangedListener {
    private CellState cellState;
    private Activity context;
    private Button button;
    private TextView textView;
    private View convertView;
    private ImageButton imageButton;
    private ImageView imageView;

    public CellManager(CellState cellState, Activity context){

        this.cellState = cellState;
        this.context = context;
        cellState.register(this);
    }

    public void setView(View view) {

        if(this.convertView != null)
            return;
        this.convertView = view;

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
                imageButton.setEnabled(true);
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
    public void onEndGame(boolean isGameWon, CellState errorCell) {
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

        if(isGameWon == false)
            GameLostAnimation(errorCell);
        else
            GameWonAnimation();

    }

    private void GameWonAnimation() {

        RotateAnimation rotate = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);

        rotate.setDuration(2000);
        convertView.setAnimation(rotate);
    }

    private void GameLostAnimation(CellState errorCell) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;


        int dx= 60;
        int dy=dx;

        if(errorCell.getRow() > cellState.getRow())
            dy*=-1;
        else if(errorCell.getRow() == cellState.getRow())
            dy = 0;

        if(errorCell.getCol() > cellState.getCol())
            dx*=-1;
        else if(errorCell.getCol() == cellState.getCol())
            dx = 0;

        //explosion animation
        AnimationSet as = new AnimationSet(true);
        TranslateAnimation animation = new TranslateAnimation(0, dx, 0, dy);
        animation.setDuration(500);
        as.addAnimation(animation);

        //falling down animation
        TranslateAnimation animation1 = new TranslateAnimation(0, width - convertView.getX() -dx, 0, height - convertView.getY() -dy);
        animation1.setDuration(1000);
        animation1.setStartOffset(1500);
        as.addAnimation(animation1);

        convertView.startAnimation(as);
    }
}
