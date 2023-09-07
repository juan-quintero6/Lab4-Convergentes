package co.edu.unipiloto.thestopwatch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class StopWatchMainActivity extends AppCompatActivity {
    //Determinar si el cronometro esta en ejecucion
    private boolean running;
    //Contar los segundos cuando el cronometro esta en ejecucion
    private int seconds = 0;
    private int contLap = 0;
    //ArrayList donde se van almacenando las vueltas
    private ArrayList <String> laps = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stop_watch_activity_main);
        if(savedInstanceState != null){
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
            laps = savedInstanceState.getStringArrayList("laps");
        }
        //Invocar el hilo de cronometrar
        runTimer();
    }

    //Metodos de los botones
    public void onClickStart(View view){running = true;}
    public void onClickStop(View view){running = false;}
    public void onClickReset(View view){
        running = true;
        seconds = 0;
        contLap = 0;
        laps.clear();
        updateLapsView();
    }
    public void onClickLap(View view){
        if(running){
            contLap++;
            String lapTime = getTime();
            laps.add("Lap " +contLap +":\t" +lapTime);
            updateLapsView();
            if(contLap == 5){
                running = false;
            }
        }
    }
    private String getTime(){
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;
        return String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs);
    }
    private void updateLapsView(){
        TextView lapView = (TextView) findViewById(R.id.laps_view);
        StringBuilder lapsText = new StringBuilder();
        for(String lap : laps){
            lapsText.append(lap).append('\n');
        }
        lapView.setText(lapsText.toString());
    }

    private void runTimer(){
        //Relacionar un objeto TextView con el elemento gráfico correspondiente
        TextView timeView = (TextView) findViewById(R.id.time_view);
        //Declarar handler
        Handler handler = new Handler();
        //Invocar el metodo post e instanciar el runnable
        handler.post(new Runnable() {
            @Override
            public void run() {
            int hours = seconds/3600;
            int minutes = (seconds%3600)/60;
            int secs = seconds%60;
            String time = String.format(Locale.getDefault(),"%d:%02d:%02d",hours,minutes,secs);
            timeView.setText(time);
            if(running)
                seconds++;
            handler.postDelayed(this,1000);
            }
        });

    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("seconds", seconds);
        savedInstanceState.putBoolean("running", running);
        savedInstanceState.putStringArrayList("laps", laps);
    }



}