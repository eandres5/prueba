package com.example.prueba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.prueba.modulo.Activi;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class correcto extends AppCompatActivity {

    private List<Activi> listaAc = new ArrayList<Activi>();
    ArrayAdapter<Activi> arrayAdapter;

    private ArrayList<String> lAct;
    private ArrayAdapter<String> adaptador1;
    private ListView lv1;

    EditText actividad, estado;
    ListView listaActivi;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Activi actividadSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correcto);

        actividad = (EditText)findViewById(R.id.txtActividad);
        estado = (EditText)findViewById(R.id.txtEstado);


        listaActivi = findViewById(R.id.lvActividades);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        listaAct();

        listaActivi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                actividadSelected = (Activi) parent.getItemAtPosition(position);
                actividad.setText(actividadSelected.getNombre());
                estado.setText(actividadSelected.getEstado());

            }
        });


    }

    private void listaAct() {
        databaseReference.child("Activi").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaAc.clear();
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    Activi a = objSnapshot.getValue(Activi.class);
                    listaAc.add(a);

                    arrayAdapter = new ArrayAdapter<Activi>(correcto.this,android.R.layout.simple_expandable_list_item_1,listaAc);
                    listaActivi.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void agregar(View v){

        String act = actividad.getText().toString();
        String est = estado.getText().toString();

        Activi a = new Activi();

        a.setId(UUID.randomUUID().toString());
        a.setNombre(act);
        a.setEstado(est);

        databaseReference.child("Activi").child(a.getId()).setValue(a);

        actividad.setText("");
        estado.setText("");
    }

    public void actualizar (View v){

        Activi ac = new Activi();
        ac.setId(actividadSelected.getId());
        ac.setNombre(actividad.getText().toString().trim());
        ac.setEstado(estado.getText().toString().trim());
        databaseReference.child("Activi").child(ac.getId()).setValue(ac);
        actividad.setText("");
        estado.setText("");
    }

    public void eliminar(View v){

        Activi a = new Activi();
        a.setId(actividadSelected.getId());
        databaseReference.child("Activi").child(a.getId()).removeValue();
        actividad.setText("");
        estado.setText("");

    }


}
