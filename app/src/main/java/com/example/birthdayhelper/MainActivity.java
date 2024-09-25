package com.example.birthdayhelper_danielavila;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Toast;

import com.example.birthdayhelper_danielavila.databinding.ActivityMainBinding;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;


public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    ActivityMainBinding binding;
    SQLiteDatabase db;
    List<Contactos> contactList = new ArrayList<>();
    private RecyclerView recy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        comprobar();

    }

    public void comprobar(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_CONTACTS},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }





        listarContactos();
        base();
        añadir();

    }

    public void recycler(){

        this.recy = (RecyclerView) findViewById(R.id.rcvContact);

        Adaptador adaptador = new Adaptador(contactList, getApplicationContext());
        recy.setAdapter(adaptador);
        recy.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    public void base(){
        db = openOrCreateDatabase("MisCumples", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS miscumples(\n" +
                " ID integer,\n" +
                " TipoNotif char(1),\n" +
                " Mensaje VARCHAR(160),\n" +
                " Telefono VARCHAR(15),\n" +
                " FechaNacimiento VARCHAR(15),\n" +
                " Nombre VARCHAR(128)\n" +
                ");");

        System.out.println("base hecha.");
    }

    public void añadir(){

        for(Contactos conta : contactList) {

            int id = Integer.valueOf(conta.getId());
            String noti = conta.getNotif();
            String mens = conta.getCumple();
            String fecha = conta.getFecha();
            String nombre = conta.getNombre();

            List<String> telef = conta.getNúmero();
            String numeros ="";
            int i = 0;
            for(String telefono : telef){
               numeros = numeros + telefono+"//";
            }

            String sentencia="INSERT INTO miscumples VALUES (" +
                    id +",'" +
                    noti + "','"+
                    mens +"','"+
                    numeros+"','"+
                    fecha +"','"+
                    nombre+"')";

            db.execSQL(sentencia);
            i++;
            System.out.println(sentencia);
        }

        recycler();
    }

    public void listarContactos(){
        Contactos contacto = new Contactos();

        String[] projection = {ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.HAS_PHONE_NUMBER,
                ContactsContract.Contacts.PHOTO_URI, ContactsContract.CommonDataKinds.Event.START_DATE};
        String selection = ContactsContract.Contacts.HAS_PHONE_NUMBER ;
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
        String args_filtro[] = {"%" + contacto + "%"};

        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        int i = 0;

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                @SuppressLint("Range") String photoUri = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
                Bitmap photo = null;
                if (photoUri != null) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(Uri.parse(photoUri));
                        photo = BitmapFactory.decodeStream(inputStream);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                String birthday = getCumple(id, contentResolver);

                List<String> phoneNumbers = new ArrayList<>();
                Cursor phoneCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                if (phoneCursor != null) {
                    while (phoneCursor.moveToNext()) {
                        @SuppressLint("Range") String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        phoneNumbers.add(phoneNumber);
                    }
                    phoneCursor.close();
                }
                String notif = "Aviso: Solo notificación";
                String cump = "Feliz Cumple "+ name;

                Contactos contact = new Contactos(name, phoneNumbers, birthday, id, photo, notif, cump);
                contactList.add(contact);

                i++;
                System.out.println("contact" + i);
            }
            cursor.close();
        }
    }

    private String getCumple(String id, ContentResolver contentResolver) {
        String where = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
        String[] params = new String[]{id, ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE};
        Cursor cursor = contentResolver.query(ContactsContract.Data.CONTENT_URI, null, where, params, null);
        int bDay = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE);
        try {
            cursor.moveToNext();
            String birthday = cursor.getString(bDay);
            return birthday;
        }
        catch(Exception e){
            return null;
        }


    }


}