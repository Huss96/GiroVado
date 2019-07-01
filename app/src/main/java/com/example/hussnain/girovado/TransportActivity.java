package com.example.hussnain.girovado;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransportActivity extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.transportType)
    Spinner transport;
    @BindView(R.id.companyName)
    EditText companyName;
    @BindView(R.id.link)
    EditText link;
    @BindView(R.id.addButton)
    Button add;
    @BindView(R.id.cancelButton)
    Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport);
        ButterKnife.bind(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.transport, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transport.setAdapter(adapter);

        add.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == add) {
            Leisure.getTransports().add(new Transport(transport.getSelectedItem().toString(), companyName.getText().toString(), link.getText().toString()));
            startActivity(new Intent(TransportActivity.this, SaveActivity.class));
        }
        if(v == cancel){
            startActivity(new Intent(TransportActivity.this, SaveActivity.class));
        }
    }
}