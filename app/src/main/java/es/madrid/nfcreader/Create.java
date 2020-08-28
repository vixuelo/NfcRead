package es.madrid.nfcreader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import es.madrid.nfcreader.Utils.Coder.CoderDecoder;
import es.madrid.nfcreader.Utils.FbDB;
import es.madrid.nfcreader.Utils.Perfil.Perfil;

public class Create<Uri> extends AppCompatActivity {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef= storage.getReference().child("ImagesIds");
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView ImageTxt;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();
    int i =0;
    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    private static final String CERO = "0";
    private static final String BARRA = "/";
    Gson gson = new Gson();
    Date dateNac;
    Date dateCad;
    Date dateExp;
    Perfil perfil = new Perfil();
    EditText NombreTxt, FechaNacTxt, FechaExpTxt, FechaCadTxt, AptitudesTxt,AficionesTxt;
    ImageButton AptitudesBtn, AficionesBtn,ImagenPerfil;
    Button CrearBtn;
    FbDB fbDB =new FbDB();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        NombreTxt=findViewById(R.id.NombreTxt);
        FechaNacTxt=findViewById(R.id.FechaNacTxt);
        FechaNacTxt.setFocusable(false);
        ImageTxt = findViewById(R.id.ImageTxt);
        ImagenPerfil=findViewById(R.id.ImageProfile);
        ImagenPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        FechaNacTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog recogerFecha = new DatePickerDialog(Create.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                        final int mesActual = month + 1;
                        //Formateo el día obtenido: antepone el 0 si son menores de 10
                        String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                        //Formateo el mes obtenido: antepone el 0 si son menores de 10
                        String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                        //Muestro la fecha con el formato deseado
                        FechaNacTxt.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);
                        dateNac= new Date(year-1900,month,dayOfMonth);


                    }
                    //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
                    /**
                     *También puede cargar los valores que usted desee
                     */
                },anio, mes, dia);
                //Muestro el widget
                recogerFecha.show();


            }
        });
        FechaExpTxt=findViewById(R.id.FechaExpTxt);
        FechaExpTxt.setFocusable(false);
        FechaExpTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog recogerFecha = new DatePickerDialog(Create.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                        final int mesActual = month + 1;
                        //Formateo el día obtenido: antepone el 0 si son menores de 10
                        String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                        //Formateo el mes obtenido: antepone el 0 si son menores de 10
                        String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                        //Muestro la fecha con el formato deseado
                        FechaExpTxt.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);
                        dateExp= new Date(year-1900,month,dayOfMonth);


                    }
                    //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
                    /**
                     *También puede cargar los valores que usted desee
                     */
                },anio, mes, dia);
                //Muestro el widget
                recogerFecha.show();


            }
        });
        FechaCadTxt=findViewById(R.id.FechaCadTxt);
        FechaCadTxt.setFocusable(false);
        FechaCadTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    DatePickerDialog recogerFecha = new DatePickerDialog(Create.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                            final int mesActual = month + 1;
                            //Formateo el día obtenido: antepone el 0 si son menores de 10
                            String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                            //Formateo el mes obtenido: antepone el 0 si son menores de 10
                            String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                            //Muestro la fecha con el formato deseado
                            FechaCadTxt.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);
                            dateCad= new Date(year-1900,month,dayOfMonth);



                        }
                        //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
                        /**
                         *También puede cargar los valores que usted desee
                         */
                    },anio, mes, dia);
                    //Muestro el widget
                    recogerFecha.show();



            }
        });
        AptitudesTxt=findViewById(R.id.AptitudesTxt);

        AficionesTxt=findViewById(R.id.AficionesTxt);
        AptitudesBtn=findViewById(R.id.AptitudesBtn);
        AficionesBtn=findViewById(R.id.AficionesBtn);
        CrearBtn=findViewById(R.id.CreateBtn);
        AptitudesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                perfil.addAptitudes(AptitudesTxt.getText().toString());
                AptitudesTxt.setText("");
            }
        });
        AficionesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AficionesTxt.getText().toString()!=null&&!AficionesTxt.getText().toString().isEmpty()&&AficionesTxt.getText().toString()!="") {
                    perfil.addAficiones(AficionesTxt.getText().toString());
                    AficionesTxt.setText("");
                }else{
                    Toast.makeText(Create.this,"Para añadir aficiones rellene el campo", Toast.LENGTH_LONG).show();
                }
            }
        });
        CrearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FechaCadTxt.getText().toString();
                if(!FechaCadTxt.getText().toString().isEmpty() && !FechaCadTxt.getText().toString().equals("") && !FechaExpTxt.getText().toString().isEmpty() && !FechaExpTxt.getText().toString().equals("") && !FechaNacTxt.getText().toString().isEmpty() && !FechaNacTxt.getText().toString().equals("") && !NombreTxt.getText().toString().isEmpty() && !NombreTxt.getText().toString().equals("")&& !(imageUri ==null)) {
                    Date nac = c.getTime();
                    long dfNac =nac.compareTo(dateNac);
                    long dfExp = dateCad.compareTo(dateExp);
                    if ( dfNac> 0 && dfExp>0) {
                        perfil.setFechaCad(FechaCadTxt.getText().toString());
                        perfil.setNombre(NombreTxt.getText().toString());
                        perfil.setFechaExp(FechaExpTxt.getText().toString());
                        perfil.setFechaNac(FechaNacTxt.getText().toString());
                        CoderDecoder coder = new CoderDecoder();
                        coder.Coder(gson.toJson(perfil).toString(),true);
                        fbDB.crearRegistro((android.net.Uri) imageUri,perfil,Create.this);


                    } else {
                        Toast.makeText(Create.this,"La fecha de nacimiento/expedicion  es posterior a la actual/caducidad, compruebalo porfavor",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(Create.this,"Alguno de los campos no han sido rellenado, compruebalo porfavor",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = (Uri) data.getData();
            ImageTxt.setText(((android.net.Uri) imageUri).getPath());

        }
    }





}