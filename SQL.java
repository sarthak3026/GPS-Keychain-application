package com.example.gpskeychain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SQL extends SQLiteOpenHelper {

    DatabaseReference reff;

    private static final int DATABASE_VERSION=14 ;
    private static final String DATABASE_NAME="User.db";
    private static final String TABLE_USER="User";

    private static final String COLUMN_USER_ID="user_id";
    private static final String COLUMN_USER_FNAME="user_fname";
    private static final String COLUMN_USER_LNAME="user_lname";
    private static final String COLUMN_USER_USERNAME="user_username";
    private static final String COLUMN_USER_PASSWORD="user_password";

    private static final String COLUMN_KEY_ID="key_id";
    private static final String DATABASE_USER="key";
    private static final String CURRENT_LATITUDE="current_lat";
    private static final String CURRENT_LONGITUDE="current_longitude";

    private String CREATE_USER_TABLE="CREATE TABLE"+"'"+TABLE_USER+"'"+"("+ COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_FNAME + " TEXT ,"+ COLUMN_USER_LNAME + " TEXT,"+ COLUMN_USER_USERNAME + " TEXT," + COLUMN_USER_PASSWORD + " TEXT "+")";
    private static final String CREATE_KEY_TABLE="CREATE TABLE"+"'"+DATABASE_USER+"'"+"("+COLUMN_KEY_ID+ "INTEGER PRIMARY KEY," +COLUMN_USER_ID+ " INTEGER REFERENCES '"+TABLE_USER+"'("+COLUMN_USER_ID+") , " + CURRENT_LATITUDE + " REAL ," + CURRENT_LONGITUDE + " REAL " + ")";

    private String DROP_TABLE= "DROP TABLE IF EXISTS"+"'"+TABLE_USER+"'";
    private static final String DROP_KEY_TABLE="DROP TABLE IF EXISTS '"+DATABASE_USER+"'";

    public SQL(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_KEY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int OldVersion, int NewVersion) {
        db.execSQL(DROP_TABLE);
        db.execSQL(DROP_KEY_TABLE);
        onCreate(db);
    }
    public void addUser(UserClass user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USER_FNAME, user.getFname());
        cv.put(COLUMN_USER_LNAME, user.getLname());
        cv.put(COLUMN_USER_USERNAME, user.getUsername());
        cv.put(COLUMN_USER_PASSWORD, user.getPassword());

        db.insert(TABLE_USER, null, cv);
        db.close();
    }

    public List<UserClass> getAllUser(){
        String[] columns={
                COLUMN_USER_ID,
                COLUMN_USER_FNAME,
                COLUMN_USER_LNAME,
                COLUMN_USER_USERNAME,
                COLUMN_USER_PASSWORD
        };
        String sort_order=COLUMN_USER_FNAME+ " ASC ";
        List<UserClass> userlist=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.query(TABLE_USER, columns, null, null, null, null,sort_order);
        if (cursor.moveToFirst()){
            do{
                UserClass user=new UserClass();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.setFname(cursor.getString(cursor.getColumnIndex(COLUMN_USER_FNAME)));
                user.setLname(cursor.getString(cursor.getColumnIndex(COLUMN_USER_LNAME)));
                user.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_USER_USERNAME)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
                userlist.add(user);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userlist;
    }
    /*public void updateUser(UserClass user){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_USER_USERNAME,user.getUsername());
        cv.put(COLUMN_USER_PASSWORD,user.getPassword());
        db.update(TABLE_USER,cv,COLUMN_USER_ID+"=?",new String[]{String.valueOf(user.getId())});
        db.close();
    }
    public void deleteUser(UserClass user){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_USER,COLUMN_USER_ID+"=?",new String[]{String.valueOf(user.getId())});
        db.close();
    }*/
    public boolean checkUsername(String username){
        String[] columns={COLUMN_USER_ID};
        String selection=COLUMN_USER_USERNAME+"=?";
        String[] selecargs={username};
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.query(TABLE_USER,columns,selection,selecargs,null,null,null);
        int cursorcount=cursor.getCount();
        cursor.close();
        db.close();
        return cursorcount>0;
    }
    public boolean checkUser(String username,String password){
        String[] columns={COLUMN_USER_ID};
        String selection=COLUMN_USER_USERNAME+"=?"+ " AND " +COLUMN_USER_PASSWORD+"=?";
        String[] selcargs={username,password};
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.query(TABLE_USER,columns,selection,selcargs,null,null,null);
        int cursorcount=cursor.getCount();
        cursor.close();
        db.close();
        return cursorcount > 0;
    }
    public void addKey(UserClass user){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_KEY_ID,user.getKeyId());
        cv.put(COLUMN_USER_ID,user.getId());
        db.insert(DATABASE_USER,null,cv);
        db.close();
    }
    /*public int fetchCountKey(int user_id){
        String[] columns={COLUMN_KEY_ID};
        String selection=COLUMN_USER_ID + "=?";
        String id=Integer.toString(user_id);
        String[] selecargs={id};
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.query(DATABASE_USER,null,selection,selecargs,COLUMN_USER_ID,null,null);
        int count=cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }
    public int fetchId(String username){
        int id=0;
        String[] columns={COLUMN_USER_ID};
        String selection= COLUMN_USER_USERNAME + "=?";
        String[] selectionArgs={username};
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.query(TABLE_USER,columns,selection,selectionArgs,null,null,null);
        if(cursor.getCount()>0)
            id=cursor.getInt(0);
        cursor.close();
        db.close();
        return id;
    }*/
   /* void putLatLong(final UserClass user){
        reff= FirebaseDatabase.getInstance().getReference().child("GPS");
        reff.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user.setCurr_lat((double) snapshot.child("Latitude").getValue());
                user.setCurr_long((double)snapshot.child("Longitude").getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(CURRENT_LATITUDE,user.getCurr_lat());
        values.put(CURRENT_LONGITUDE,user.getCurr_long());
        db.update(DATABASE_USER,values,COLUMN_USER_ID+"=?",new String[]{String.valueOf(user.getId())});
        db.close();
    }
    void useLatLong(String username) {
        Integer id ;
        String[] column = {COLUMN_USER_ID};
        String selection = COLUMN_USER_USERNAME + "=?";
        String[] selectionArgs = {username};
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, column, selection, selectionArgs, null, null, null);
        if (cursor != null) {
            id = Integer.valueOf(cursor.getString(cursor.getColumnIndex("COLUMN_USER_ID")));
            UserClass user = new UserClass();
            user.setId(id);
            putLatLong(user);
        }
        cursor.close();
        db.close();
    }
    double fetchLat(String username) {
        double lat;
        String id;
        String[] column = {COLUMN_USER_ID};
        String selection = COLUMN_USER_USERNAME + "=?";
        String[] selectionArgs = {username};
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, column, selection, selectionArgs, null, null, null);
        if (cursor != null ) {
            id = cursor.getString(cursor.getColumnIndex(" COLUMN_USER_ID "));
            cursor.close();
            db.close();
            SQLiteDatabase _db = this.getReadableDatabase();
            String[] columnsReturn = {CURRENT_LATITUDE};
            String selections = COLUMN_USER_ID + "=?";
            String[] selecArgs = {id};
            Cursor cursor1=_db.query(DATABASE_USER,columnsReturn,selections,selecArgs,null,null,null,null);
            if(cursor!=null){
                lat=Double.parseDouble(cursor.getString(cursor.getColumnIndex("CURRENT_LATITUDE")));
                return lat;
            }
        }
        return 0;
    }

    double fetchLong(String username){
        double lng;
        String id;
        String[] column = {COLUMN_USER_ID};
        String selection = COLUMN_USER_USERNAME + "=?";
        String[] selectionArgs = {username};
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, column, selection, selectionArgs, null, null, null);
        if (cursor != null) {
            id = cursor.getString(cursor.getColumnIndex("COLUMN_USER_ID"));
            cursor.close();
            db.close();
            SQLiteDatabase _db = this.getReadableDatabase();
            String[] columnsReturn = {CURRENT_LONGITUDE};
            String selections = COLUMN_USER_ID + "=?";
            String[] selecArgs = {id};
            Cursor cursor1=_db.query(DATABASE_USER,columnsReturn,selections,selecArgs,null,null,null,null);
            if(cursor!=null){
                lng=Double.parseDouble(cursor.getString(cursor.getColumnIndex("CURRENT_LONGITUDE")));
                return lng;
            }
        }
        return 0;
    }*/
}



