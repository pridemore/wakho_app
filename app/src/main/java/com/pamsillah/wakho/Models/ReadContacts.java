package com.pamsillah.wakho.Models;


import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.pamsillah.wakho.MainActivity;
import com.pamsillah.wakho.MyApplication;
import com.pamsillah.wakho.Parser.SubscribersParser;
import com.pamsillah.wakho.R;
import com.pamsillah.wakho.Utils.DTransUrls;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by .Net Developer on 7/17/2017.
 */

public class ReadContacts extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    AlertDialog.Builder alertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.readcontacts);
        alertDialog = new AlertDialog.Builder(ReadContacts.this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.retshepile, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Loading Contacts");
        alertDialog.setIcon(R.drawable.logo);
        alertDialog.show();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method


        } else {


            gettContactList();
        }

    }


    public List<String> contList = new ArrayList<>();
    public static List<Subscriber> contactList = new ArrayList<>();


    public List<String> gettContactList() {


        ContentResolver cr = this.getContentResolver();
        Cursor cur;
        // Android version is lesser than 6.0 or the permission is already granted.
        cur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION, null, null, null);

        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION, null, null, null);
        if (cursor != null) {
            try {
                final int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                final int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                String name, number;
                while (cursor.moveToNext()) {
                    Subscriber contacts = new Subscriber();
                    name = cursor.getString(nameIndex);
                    number = cursor.getString(numberIndex);
                    contacts.setPhone(number);
                    contacts.setName(name.split(" ")[0]);
                    contactList.add(contacts);
                }
            } finally {
                cursor.close();
            }
        }

        List<Subscriber> lst = new ArrayList<>();
        lst = MyApplication.getinstance().getSubscribers();

        if (lst != null && contactList.size() > 0) {

            for (Subscriber s : lst
                    ) {
                for (Subscriber t : contactList
                        ) {
                    if (s.getPhone().replace("+263", "0").replace(" ", "")
                            .equals(t.getPhone().replace("+263", "0").replace(" ", ""))) {
                        contList.add(t.getName() + "  :  " + t.getPhone());
                        break;

                    }
                }

            }
            if (contList == null) {
                for (Subscriber ss : contactList) {
                    contList.add(ss.getName() + " : " + ss.getPhone());
                }


            }

        } else if (lst == null) {
            for (Subscriber ss : contactList) {
                contList.add(ss.getName() + "  :  " + ss.getPhone());
            }


        } else {
            subS();
        }


        MyApplication.getinstance().setContacts(contList);
        startActivity(new Intent(ReadContacts.this, MainActivity.class));

        return contList;

    }

    public List<Subscriber> subsList;

    public void subS() {


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, DTransUrls.Subscribers, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                subsList = SubscribersParser.parseFeed(response);
                MyApplication.getinstance().getSession().setSubs(subsList);
                MyApplication.getinstance().setSubscribers(subsList);
                for (Subscriber s : subsList
                        ) {
                    for (Subscriber t : contactList
                            ) {
                        if (s.getPhone().replace("+263", "0").replace(" ", "").equals(t.getPhone().replace("+263", "0").replace(" ", ""))) {
                            contList.add(t.getName() + " : " + t.getPhone());
                            break;

                        }
                    }

                }
                if (contList == null) {
                    for (Subscriber ss : contactList) {
                        contList.add(ss.getName() + " : " + ss.getPhone());
                    }


                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error : ", error.toString());
                //progressDialog.cancel();

            }
        });


        MyApplication.getinstance().addToRequestQueue(jsonArrayRequest);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (checkSelfPermission(android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {

                alertDialog = new AlertDialog.Builder(ReadContacts.this);
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.retshepile, null);
                alertDialog.setView(convertView);
                alertDialog.setTitle("Loading Contacts");
                alertDialog.setIcon(R.drawable.logo);
                alertDialog.show();
                gettContactList();
            }
        }
    }

    private static final String[] PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
    };


}
