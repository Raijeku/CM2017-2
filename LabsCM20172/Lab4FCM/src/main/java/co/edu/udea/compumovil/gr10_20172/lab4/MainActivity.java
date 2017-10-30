package co.edu.udea.compumovil.gr10_20172.lab4;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ApartmentsFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener, AboutFragment.OnFragmentInteractionListener,SettingsFragment.OnFragmentInteractionListener, EditFragment.OnFragmentInteractionListener, DetailsFragment.OnFragmentInteractionListener, NewApartmentFragment.OnFragmentInteractionListener {

    public static final String HOST_IP = "http://192.168.1.66"; //Cuando se trabaja con Servidor

    /*
        Para ambos casos CodeAnyWhere o Local el complemento es el mismo
     */
    public static final String URL_USUARIOS_COMPLEMENTO = ":3000/api/Usuarios";
    public static final String URL_APARTAMENTOS_COMPLEMENTO = ":3000/api/Apartamentos";
    public static final String URL_CONTAINER_DOWN_COMPLEMENTO = ":3000/api/Containers/all/download/";
    public static final String URL_CONTAINER_UP_COMPLEMENTO = ":3000/api/Containers/all/upload";
    public static final String URL_APTCONTAINER_DOWN_COMPLEMENTO = ":3000/api/AptContainers/all/download/";
    public static final String URL_APTCONTAINER_UP_COMPLEMENTO = ":3000/api/AptContainers/all/upload";

    //Si se trabaja local se quita comentario de estas lineas y se comenta las de CodeAnyWhere
    public static final String URL_APARTAMENTOS = HOST_IP.concat(URL_APARTAMENTOS_COMPLEMENTO);
    public static final String URL_USUARIOS = HOST_IP.concat(URL_USUARIOS_COMPLEMENTO);
    public static final String URL_CONTAINER_DOWN = HOST_IP.concat(URL_CONTAINER_DOWN_COMPLEMENTO);
    public static final String URL_CONTAINER_UP = HOST_IP.concat(URL_CONTAINER_UP_COMPLEMENTO);
    public static final String URL_APTCONTAINER_DOWN = HOST_IP.concat(URL_APTCONTAINER_DOWN_COMPLEMENTO);
    public static final String URL_APTCONTAINER_UP = HOST_IP.concat(URL_APTCONTAINER_UP_COMPLEMENTO);

    String user,password;
    int id;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //dbHelper = new DatabaseHelper(this);

        SharedPreferences sharedPref = this.getSharedPreferences("SharedPref", Context.MODE_PRIVATE);
        user=sharedPref.getString("connected_user","");
        password=sharedPref.getString("connected_password","");
        id=sharedPref.getInt("connected_id",-1);
        Log.d("createmain",String.valueOf(id));

        auth=FirebaseAuth.getInstance();

        handleIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            //doMySearch(query);
        }
    }

    private void doMySearch(String query) {
        //SQLiteDatabase db= dbHelper.getWritableDatabase();
        //String consultaSQL = "select * from " + APARTMENT_TABLE_NAME;
        //Cursor cursor=db.rawQuery(consultaSQL,null);
        /*if (cursor!=null){
            cursor.moveToFirst();
        }
        while(cursor.moveToNext()){
            for (int i=1;i<cursor.getColumnCount();i++){
                switch (i){
                    case 1:
                        if (cursor.getString(i).contains(query)){

                        }
                        break;
                    case 2:
                        if (cursor.getString(i).contains(query)){

                        }
                        break;
                    case 3:
                        if (String.valueOf(cursor.getDouble(i)).contains(query)){

                        }
                        break;
                    case 4:
                        if (String.valueOf(cursor.getDouble(i)).contains(query)){

                        }
                        break;
                    case 5:
                        if (String.valueOf(cursor.getInt(i)).contains(query)){

                        }
                        break;
                    case 6:
                        if (String.valueOf(cursor.getInt(i)).contains(query)){

                        }
                        break;
                    case 7:
                        if (cursor.getString(i).contains(query)){

                        }
                        break;
                    case 8:
                        if (cursor.getString(i).contains(query)){

                        }
                        break;
                }
            }



        }*/
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        /*SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));*/

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int idop = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (idop == R.id.search) {
            return true;
        } else if (idop == R.id.exit){
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment genericFragment=null;
        //PreferenceFragment settingsFragment=null;
        //FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        int idn = item.getItemId();

        if (idn == R.id.nav_home) {

            genericFragment = new ApartmentsFragment();

        } else if (idn == R.id.nav_person) {

            Log.d("onprofileclick",String.valueOf(id));
            genericFragment = ProfileFragment.newInstance(user,password,id);

        } else if (idn == R.id.nav_manage) {

            genericFragment = new SettingsFragment();

        } else if (idn == R.id.nav_lock) {

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("connected_user", "");
            editor.putString("connected_password", "");
            editor.commit();
            auth.signOut();
            finish();

        } else if (idn == R.id.nav_info) {

            genericFragment = new AboutFragment();

        }

        if (genericFragment!=null){
            //fragmentManager.beginTransaction().replace(R.id.main_container,genericFragment).commit();
            fragmentTransaction.replace(R.id.main_container,genericFragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onItemSelected(Apartment apartment) {
        Log.d("Third","true");
        FrameLayout dFragment=(FrameLayout)findViewById(R.id.secondary_container);
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        DetailsFragment detailsFragment = DetailsFragment.newInstance(apartment.getDescription(),apartment.getLocation(),apartment.getAmountRooms(),apartment.getArea(),apartment.getName(),apartment.getImage(),apartment.getPropertyType(),apartment.getValue());
        if (dFragment==null){
            Log.d("Fourth","No está detailsfragment");
            ft.replace(R.id.main_container,detailsFragment);
            ft.commit();
        } else{
            Log.d("Fourth","Si está detailsfragment");
            ft.replace(R.id.secondary_container,detailsFragment);
            ft.commit();
        }
    }

    /*@Override
    public void onItemSelected(int apartmentIndex) {
        Log.d("Third","true");
        FrameLayout dFragment=(FrameLayout)findViewById(R.id.secondary_container);
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        DetailsFragment detailsFragment = DetailsFragment.newInstance(apartmentIndex+1);
        if (dFragment==null){
            Log.d("Fourth","No está detailsfragment");
            ft.replace(R.id.main_container,detailsFragment);
            ft.commit();
        } else{
            Log.d("Fourth","Si está detailsfragment");
            ft.replace(R.id.secondary_container,detailsFragment);
            ft.commit();
        }
    }*/

    @Override
    public void onAddClick() {
        Fragment genericFragment=null;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        genericFragment = new NewApartmentFragment();
        if (genericFragment!=null){
            fragmentTransaction.replace(R.id.main_container,genericFragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //Code for Profile Fragment
    }

    @Override
    public void onPreferenceInteraction() {
        Fragment genericFragment=null;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        genericFragment = EditFragment.newInstance(user,password,id);
        Log.d("onprefinteraction",String.valueOf(id));
        if (genericFragment!=null){
            fragmentTransaction.replace(R.id.main_container,genericFragment);
            fragmentTransaction.commit();
        }

    }

    @Override
    public void onEditInteraction() {
        Fragment genericFragment=null;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        genericFragment = new SettingsFragment();
        if (genericFragment!=null){
            fragmentTransaction.replace(R.id.main_container,genericFragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onNewApartmentInteraction() {
        Fragment genericFragment=null;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        genericFragment = new ApartmentsFragment();
        if (genericFragment!=null){
            fragmentTransaction.replace(R.id.main_container,genericFragment);
            fragmentTransaction.commit();
        }
    }
}
