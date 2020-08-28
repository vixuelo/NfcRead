package es.madrid.nfcreader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.net.MediaType;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import es.madrid.nfcreader.Utils.FbDB;
import es.madrid.nfcreader.Utils.Perfil.Perfil;

public class MostrarPerfil extends AppCompatActivity {
    EditText Nombre, FechaNac, FechaCad,FechaExp;
    ImageButton imagenPerfil;
    Spinner sAptitudes,sAficiones;
    Button modificar;
    Boolean modify =false;
    Uri imageUri=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_perfil);
        imageUri=(Uri) getIntent().getParcelableExtra("imagen");
        final Perfil perfil= (Perfil) getIntent().getSerializableExtra("perfil");
        Nombre=findViewById(R.id.NombreTxtMostrarPerfil);
        imagenPerfil=findViewById(R.id.ImageProfileMostrarPerfil);
        FechaNac=findViewById(R.id.FechaNacTxtMostrarPerfil);
        FechaCad=findViewById(R.id.FechaCadTxtMostrarPerfil);
        FechaExp=findViewById(R.id.FechaExpTxtMostrarPerfil);
        sAficiones=findViewById(R.id.spinnerAficiones);
        sAptitudes=findViewById(R.id.spinnerAptitudes);
        modificar=findViewById(R.id.ModificarBtn);
        modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!modify){
                modify=true;
                    Nombre.setEnabled(true);
                    FechaNac.setEnabled(true);
                    FechaCad.setEnabled(true);
                    FechaExp.setEnabled(true);
            }else{
                 //Pasar a añadir/eliminar Aficiones/aptitudes
                }
            }
        });

        Nombre.setText(perfil.getNombre());
        FechaNac.setText(perfil.getFechaNac());
        FechaCad.setText(perfil.getFechaCad());
        FechaExp.setText(perfil.getFechaExp());
        ArrayAdapter<String> adapterAficiones = new ArrayAdapter<String>(MostrarPerfil.this,
                android.R.layout.simple_spinner_item,perfil.getAficiones());

        adapterAficiones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapterAptitudes = new ArrayAdapter<String>(MostrarPerfil.this,
                android.R.layout.simple_spinner_item,perfil.getAptitudes());

        adapterAptitudes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sAficiones.setAdapter(adapterAficiones);
        sAptitudes.setAdapter(adapterAptitudes);
    }


}