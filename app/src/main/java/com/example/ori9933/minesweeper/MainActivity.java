package com.example.ori9933.minesweeper;

import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements IGameStatusListener {

    private TextView minesLeftTextView;
    private TextView gameStatusTextView;
    private GyroscopeSensorListener gyroscopeSensorListener;
    private LinearLayout recordContainer;
    private TextView scoreTextView;
    private EditText nameTextInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        minesLeftTextView = (TextView) findViewById(R.id.mines_left_text);
        gameStatusTextView = (TextView) findViewById(R.id.game_status_text);
        recordContainer = (LinearLayout) findViewById(R.id.record_container);
        scoreTextView = (TextView) findViewById(R.id.score_text);
        nameTextInput = (EditText) findViewById(R.id.name_text_input);

        GameManager.getInstance().register(this);
        gyroscopeSensorListener = new GyroscopeSensorListener((SensorManager) getSystemService(SENSOR_SERVICE));
        StartNewGame(false);

        Button newGameButton = (Button)findViewById(R.id.new_game_button);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartNewGame(true);
            }
        });

        Button saveRecordButton = (Button)findViewById(R.id.save_record_button);
        saveRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordContainer.setVisibility(View.GONE);
                UserRecordsManager.SaveRecord(GameManager.getInstance().getLastScore(), nameTextInput.getText().toString());
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
            case R.id.action_showRecordsView:
                Intent intent = new Intent(this,RecordsActivity.class);
                startActivity(intent);
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void StartNewGame(boolean notify){
        recordContainer.setVisibility(View.GONE);
        gyroscopeSensorListener.Reset();
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
        int score = GameManager.getInstance().getLastScore();
        if(isGameWon && UserRecordsManager.isNewHighScore(score)){
            scoreTextView.setText("Congrats! Your score " + score + " is one of the highest scores!");
            recordContainer.setVisibility(View.VISIBLE);
        }
    }
}
