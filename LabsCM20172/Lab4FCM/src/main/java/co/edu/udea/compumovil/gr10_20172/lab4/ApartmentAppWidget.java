package co.edu.udea.compumovil.gr10_20172.lab4;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.bumptech.glide.request.target.AppWidgetTarget;
import com.bumptech.glide.request.target.NotificationTarget;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Implementation of App Widget functionality.
 */
public class ApartmentAppWidget extends AppWidgetProvider {

    static AppWidgetTarget appWidgetTarget;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.apartment_app_widget);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        views.setTextViewText(R.id.nombre_widget,sharedPref.getString("last_apartment_name","No encontrado"));
        views.setTextViewText(R.id.tipo_widget,sharedPref.getString("last_apartment_type", "No encontrado"));
        /*Picasso.with(context)
                .load(sharedPref.getString("last_apartment_type", ""))
                .into(views,R.id.foto_widget, new int[]{appWidgetId});*/
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(final Context context, AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        /*FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference apartmentsDatabaseReference=database.getReference().child("apartments");
        ChildEventListener childEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Apartment apartment = dataSnapshot.getValue(Apartment.class);
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.apartment_app_widget);
                views.setTextViewText(R.id.nombre_widget, apartment.getName());
                views.setTextViewText(R.id.tipo_widget,apartment.getPropertyType());
                //appWidgetTarget = new AppWidgetTarget(context, views, R.id.foto_widget, appWidgetIds);

                Picasso.with(context)
                        .load(apartment.getImage())
                        .into(views,R.id.foto_widget,appWidgetIds);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        apartmentsDatabaseReference.addChildEventListener(childEventListener);*/
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.apartment_app_widget);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        views.setTextViewText(R.id.nombre_widget,sharedPref.getString("last_apartment_name","No encontrado"));
        views.setTextViewText(R.id.tipo_widget,sharedPref.getString("last_apartment_type", "No encontrado"));
        /*Picasso.with(context)
                .load(sharedPref.getString())
                .into(views,R.id.foto_widget,appWidgetIds);*/
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.apartment_app_widget);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        views.setTextViewText(R.id.nombre_widget,sharedPref.getString("last_apartment_name","No encontrado"));
        views.setTextViewText(R.id.tipo_widget,sharedPref.getString("last_apartment_type", "No encontrado"));
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

