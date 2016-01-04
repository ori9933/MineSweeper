package com.example.ori9933.minesweeper;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity implements IGameStatusListener{

    private TextView minesLeftTextView;
    private TextView gameStatusTextView;
    private LinearLayout recordContainer;
    private TextView scoreTextView;
    private EditText nameTextInput;
    private LocationProvider locationProvider;

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
                Location location = locationProvider.getLastLocation();
                double latitude = 0, longitude= 0;
                if(location != null){
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
                UserRecordsManager.getInstance().SaveRecord(GameManager.getInstance().getLastScore(), nameTextInput.getText().toString(),latitude,longitude);
            }
        });

        locationProvider = new LocationProvider((LocationManager) getSystemService(Context.LOCATION_SERVICE), getBaseContext());

        Intent intent = new Intent(this, GyroscopeSensorListener.class);
        bindService(intent, myConnection, Context.BIND_AUTO_CREATE);
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
        if(isBound)
            gyroscopeService.Reset();
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
        boolean isHighScore = UserRecordsManager.getInstance().isNewHighScore(score);
        if(isGameWon && isHighScore){
            scoreTextView.setText("Congrats! Your score " + score + " is one of the highest scores!");
            recordContainer.setVisibility(View.VISIBLE);
        }
    }



    GyroscopeSensorListener gyroscopeService;
    boolean isBound = false;

    private ServiceConnection myConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            GyroscopeSensorListener.MyLocalBinder binder = (GyroscopeSensorListener.MyLocalBinder) service;
            gyroscopeService = binder.getService();
            isBound = true;
        }

        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };




}
