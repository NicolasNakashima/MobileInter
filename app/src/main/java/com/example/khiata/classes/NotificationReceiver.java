package com.example.khiata.classes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //Handle the notification click here; Essa classe define um comportamento caso o usuário clique na notificação
//        Toast.makeText(context, "Recebido", Toast.LENGTH_LONG).show();
    }
}
