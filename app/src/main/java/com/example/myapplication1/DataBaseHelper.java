package com.example.myapplication1;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String METAL = "metal";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_EXPANSION = "expansion";
    public static final String COLUMN_ID = "id";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "Metal", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + METAL + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TYPE + " TEXT, " + COLUMN_EXPANSION + " FLOAT)";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public boolean addmetal(MetalModel metalModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TYPE, metalModel.getType());
        cv.put(COLUMN_EXPANSION, metalModel.getExpansion());
        long insert = db.insert(METAL, null, cv);
        return insert != -1;
    }

    @SuppressLint("Recycle")
    public boolean deleteOne(MetalModel metalModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM metal WHERE id = " + metalModel.getId();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()){
            return false;
        }
        else{
            return true;
        }
    }

    public List<MetalModel> getAll() {
        String queryString = "SELECT * FROM " + METAL;
        List<MetalModel> returnList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            do{
                int id = cursor.getInt(0);
                String type = cursor.getString(1);
                Float expansion = cursor.getFloat(2);
                MetalModel newModel = new MetalModel(id,type,expansion);
                returnList.add(newModel);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return returnList;
    }

    public Float getExpansion(String type) {
        Float expansion = 0f;
        String queryString = "SELECT " + COLUMN_EXPANSION + " FROM " + METAL + " WHERE " + COLUMN_TYPE + " = '" + type + "' ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);

        if (cursor.moveToFirst()) {
            do{
                expansion = cursor.getFloat(0);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return expansion;
    }


    public List<String> getType() {
        List<String> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + METAL;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do{
                String type = cursor.getString(1);
                returnList.add(type);

            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return returnList;
    }

    public void populateFull() {

        List<String> type = Arrays.asList("Aluminium", "Brass","Carbon Steel", "Concrete", "Copper", "Diamond", "Glass", "Borosilicate Glass", "Gold", "Granite", "Iron", "Lead", "Nickel", "Platinum", "PolyPropylene", "PVC", "Sapphire", "Silicon Carbide", "Silicon", "Silver", "Stainless Steel", "Steel", "Titanium", "Tungsten");
        List<String> expansion = Arrays.asList("23.1", "19", "10.8", "12", "17", "1", "8.5", "3.3", "14", "35", "11.8", "29", "13", "9", "150", "52", "5.3", "2.77", "2.56", "18", "10.1", "11", "8.6", "4.5");
        MetalModel metalModel;

        for (int i = 0; i < type.size(); i++) {
            metalModel = new MetalModel(-1, type.get(i), Float.parseFloat(expansion.get(i)));
            addmetal(metalModel);
        }
    }

    public void populateMetal() {
        List<String> type = Arrays.asList("Aluminium", "Brass","Carbon Steel", "Copper", "Gold", "Iron", "Lead", "Nickel", "Platinum","Silicon Carbide", "Silver", "Stainless Steel", "Steel", "Titanium", "Tungsten");
        List<String> expansion = Arrays.asList("23.1", "19", "10.8", "17", "14", "11.8", "29", "13", "9","2.77", "18", "10.1", "11", "8.6", "4.5");
        MetalModel metalModel;

        for (int i = 0; i < type.size(); i++) {
            metalModel = new MetalModel(-1, type.get(i), Float.parseFloat(expansion.get(i)));
            addmetal(metalModel);
        }
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(METAL, null, null);
    }
}



