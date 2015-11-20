package com.cleysonph.calculadoraimc20;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.cleysonph.calculadoraimc20.domain.IMC;
import com.cleysonph.calculadoraimc20.domain.SQLiteHelper;

public class HistoricoActivity extends AppCompatActivity {

    private CursorAdapter dataSource;
    private SQLiteDatabase database;
    private static final String campos[] = {"peso", "altura", "resultado", "_id"};

    private ListView listView;
    private SQLiteHelper sqLiteHelper;

    private long intemSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CalculoImcActivity.class);
                startActivity(intent);
            }
        });

        listView = (ListView) findViewById(R.id.listView);
        sqLiteHelper = new SQLiteHelper(this);
        database = sqLiteHelper.getWritableDatabase();

        listarHistorico();

        listView.setOnItemClickListener(deletarItem());
    }

    private AdapterView.OnItemClickListener deletarItem() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                intemSelecionado = id;
                AlertDialog.Builder builder = new AlertDialog.Builder(HistoricoActivity.this);
                builder.setTitle("Pergunta");
                builder.setMessage("O que deseja fazer?");
                builder.setNegativeButton("Deletar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        IMC imc = new IMC();
                        imc.setId(intemSelecionado);

                        sqLiteHelper.deleteOne(imc);

                        Toast.makeText(HistoricoActivity.this, getString(R.string.toast_apagar_item), Toast.LENGTH_SHORT).show();
                        finish();

                        startActivity(new Intent(getApplicationContext(), HistoricoActivity.class));

                    }
                });
                builder.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String codigo;
                        IMC imc = new IMC();
                        Cursor cursor = database.query("imc", campos, null, null, null, null, null);
                        cursor.moveToPosition(position);
                        codigo = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                        Intent intent = new Intent(getApplicationContext(), AlterarImcActivity.class);
                        intent.putExtra("id", codigo);
                        startActivity(intent);
                        finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_historico, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.list_clean){
            sqLiteHelper.deleteAll();

            finish();
            Toast.makeText(this, getString(R.string.historico_foi_limpo), Toast.LENGTH_LONG).show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void listarHistorico(){
        Cursor cursor = database.query("imc", campos, null, null, null, null, null);
        if (cursor.getCount() > 0){
            dataSource = new SimpleCursorAdapter(HistoricoActivity.this, R.layout.row, cursor, campos, new int[] {R.id.txtPesoHistorico, R.id.txtAlturaHistorico, R.id.txtImcHistorico});
            listView.setAdapter(dataSource);
        } else {
            Toast.makeText(HistoricoActivity.this, getString(R.string.sem_registro), Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
