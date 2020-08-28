package es.madrid.nfcreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {
    Button New, Read;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        New=(Button)findViewById(R.id.NewButton);
        New.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Nuevo();
            }
        });
        Read=(Button)findViewById(R.id.ReadButton);
        Read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Leer();
            }
        });
    }

    private void Leer() {
        Intent Leer = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(Leer);
    }

    private void Nuevo() {
        Intent Crear = new Intent(getApplicationContext(),Create.class);
        startActivity(Crear);
    }
}