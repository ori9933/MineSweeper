package com.example.ori9933.minesweeper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements IGameStatusListener {

    private TextView minesLeftTextView;
    private TextView gameStatusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        minesLeftTextView = (TextView) findViewById(R.id.mines_left_text);
        gameStatusTextView = (TextView) findViewById(R.id.game_status_text);

        GameManager.getInstance().register(this);
        StartNewGame(false);

        Button newGameButton = (Button)findViewById(R.id.new_game_button);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartNewGame(true);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_level_easy:
                GameManager.getInstance().setGamesLevel(GameLevel.Easy);
                StartNewGame(true);
                return true;
            case R.id.action_level_normal:
                GameManager.getInstance().setGamesLevel(GameLevel.Normal);
                StartNewGame(true);
                return true;
            case R.id.action_level_hard:
                GameManager.getInstance().setGamesLevel(GameLevel.Hard);
                StartNewGame(true);
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void StartNewGame(boolean notify){
        GameManager.getInstance().newGame(notify);
        gameStatusTextView.setText("");
    }

    @Override
    public void onMinesLeftChanged(int mines) {
        minesLeftTextView.setText("Mines Left: " + String.valueOf(mines));
    }

    @Override
    public void onGameOver(boolean isGameWon) {
        String message = isGameWon ? "Game Won!" : "Game Lost! :(";
        gameStatusTextView.setText(message);
    }
}
