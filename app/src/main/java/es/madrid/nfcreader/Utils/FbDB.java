package es.madrid.nfcreader.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.Serializable;

import es.madrid.nfcreader.Create;
import es.madrid.nfcreader.MostrarPerfil;
import es.madrid.nfcreader.Utils.Perfil.Perfil;

public class FbDB {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef= storage.getReference().child("ImagesIds");
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    int i=0;
    public void crearRegistro(final Uri imageUri, final Perfil perfil, final Context context){
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("DB", document.getId() + " => " + document.getData());
                        i++;
                    }
                    final StorageReference ref = storageRef.child(i+".jpg"); // Referencia a donde queres subir el archivo
                    UploadTask uploadTask = ref.putFile((android.net.Uri) imageUri);

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            // Continuamos con el task para obtener la url de descarga
                            return (Task<Uri>) ref.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                String urlFoto = downloadUri.toString();
                            } else {
                                // Manejar fa
                                // ...
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Fallo en la subida de foto "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    db.collection("users").document(String.valueOf(i)).set(perfil).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context, "Perfil creado correctamente", Toast.LENGTH_SHORT).show();
                            Finalizar(imageUri,perfil,context, String.valueOf(i));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Log.d("DB", "Error getting documents: ", task.getException());
                }
            }
        });
    }
    private void Finalizar(final Uri imageUri, final Perfil perfil, final Context context, String id ) {
        Intent intent = new Intent(context, MostrarPerfil.class);
        intent.putExtra("perfil", (Serializable) perfil);
        intent.putExtra("imagen", (Parcelable) imageUri);
        intent.putExtra("id", id);
        context.startActivity(intent);
        ((Activity)context).finish();
    }
    public void ModificarPerfil(final Uri imageUri, final Perfil perfil, final Context context, final String id ) {
        db.collection("users").document(id).set(perfil).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Perfil modificado correctamente", Toast.LENGTH_SHORT).show();
                final StorageReference ref = storageRef.child(id+".jpg"); // Referencia a donde queres subir el archivo
                UploadTask uploadTask = ref.putFile((android.net.Uri) imageUri);

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continuamos con el task para obtener la url de descarga
                        return (Task<Uri>) ref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            String urlFoto = downloadUri.toString();
                        } else {
                            // Manejar fa
                            // ...
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Fallo en la subida de foto "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                Finalizar(imageUri,perfil,context,id);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
