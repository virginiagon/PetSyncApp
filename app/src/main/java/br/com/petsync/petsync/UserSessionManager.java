package br.com.petsync.petsync;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.security.KeyManagementException;
import java.util.HashMap;

import br.com.petsync.petsync.model.Cliente;
import br.com.petsync.petsync.model.Usuario;

/**
 * Created by Virgínia on 21/09/2016.
 */
public class UserSessionManager {

    //SharedPreferences reference
    SharedPreferences pref;

    //Editor reference for shared preference
    SharedPreferences.Editor editor;

    //Context
    Context context;

    //Shared pref mode
    int PRIVATE_MODE = 0;

    //Sharedpref file name
    private static final String PREFER_NAME = "AndroidExamplePref";

    //All Shared Preference Keys
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";

    public static final String KEY_ID = "id";

    //User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    //Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    public static final String KEY_ENDERECO = "endereco";

    public static final String KEY_CIDADE = "cidade";

    public static final String KEY_ESTADO = "estado";

    public static final String KEY_CEP = "cep";

    //Construtor
    public UserSessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //Create login session
    //public void createUserLoginSession(String id, String name, String email) {
    public void createUserLoginSession(Cliente cliente, Usuario usuario) {
        //Storing login value as TRUE
        editor.putBoolean(IS_USER_LOGIN, true);

        //Storing id in pref
        editor.putString(KEY_ID, String.valueOf(cliente.getId()));

        //Storing name in pref
        editor.putString(KEY_NAME, cliente.getNome());

        //Storing email in pref
        editor.putString(KEY_EMAIL, usuario.getEmail());

        editor.putString(KEY_ENDERECO, cliente.getEndereco());

        editor.putString(KEY_CIDADE, cliente.getCidade());

        editor.putString(KEY_ESTADO, cliente.getEstado());

        //editor.putString(KEY_CEP, cliente.getCep());

        //commit
        editor.commit();
    }

    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else do anything
     */
    public boolean checkLogin() {
        //check login status
        if(!this.isUserLoggedIn()) {
            //user is not logged in redirect him to login activity
            Intent i = new Intent(context, LoginActivity.class);

            //Closing all the activities from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            //Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //staring login activity
            context.startActivity(i);

            return true;
        }
        return false;
    }

    /**
     * Get Stored session data
     */
    public HashMap<String, String> getUserDetails() {
        //Use hashmap to store use credentials
        HashMap<String, String> user = new HashMap<String, String>();

        //User name
        user.put(KEY_ID, pref.getString(KEY_ID, null));

        //User name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        //user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        user.put(KEY_ENDERECO, pref.getString(KEY_ENDERECO, null));

        user.put(KEY_CIDADE, pref.getString(KEY_CIDADE, null));

        user.put(KEY_ESTADO, pref.getString(KEY_ESTADO, null));

        user.put(KEY_CEP, pref.getString(KEY_CEP, null));

        return user;
    }

    public void logoutUser() {

        //Clearing all user data from Shared Preference
        editor.clear();
        editor.commit();

        //After logout redirect user to Login Activity
        Intent i = new Intent(context, LoginActivity.class);

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(i);
    }

    public boolean isUserLoggedIn() {
        return pref.getBoolean(IS_USER_LOGIN, false);
    }

    public void setCep(String cep) {
        editor.putString(KEY_CEP, cep);
        editor.commit();
    }

    public String getCep(){
        return pref.getString(KEY_CEP, null);
    }

}
