package com.example.cartsystem.DataBase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



import java.util.ArrayList;
import java.util.List;


public class SQLiteAdaptor extends SQLiteOpenHelper {


    Context context;
    public static final int Database_Version = 1;   // Database Version
    public static final String Database_Name = "DB";    // Database Name


    public static final String Table_Name3 = "Cart"; // Table Name

    public static final String product_id = "product_id";     // Column 1
    public static final String _ID = "_id";
    public static final String product_amount = "product_amount";
    public static final String product_quantity = "quantity";
    public static final String product_name = "name";
    public static final String product_url = "url";
    public static final String product_mrp = "mrp";
    public static final String product_price = "price";
    public static final String product_title2 = "title2";
    SQLiteDatabase db;
    public SQLiteAdaptor(Context context) {
        super(context, Database_Name, null, Database_Version);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        /**Create Table if not exists**/



        String query3 = "Create table if not exists " + Table_Name3 + "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + product_id + " String(100) ," + product_amount + " TEXT NOT NULL, " + product_quantity + " String(10), " + product_name+ " String(20),"+product_url+" String(100),"+product_mrp+" String(100),"+product_title2+" String(100))";
        db.execSQL(query3);




    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }



    public void insert(int count ,String product_ids, String amount,String name,String url) {

        db=getWritableDatabase();
        db=getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteAdaptor.product_id, product_ids);
        contentValues.put(SQLiteAdaptor.product_quantity, count);
        contentValues.put(SQLiteAdaptor.product_amount, amount);
        contentValues.put(SQLiteAdaptor.product_name, name);
        contentValues.put(SQLiteAdaptor.product_url, url);

       // contentValues.put(SQLiteAdaptor._ID, max);
        db.insert(SQLiteAdaptor.Table_Name3, null, contentValues);
    }



    public void updatecart(int quantity, String product_ids) {
        SQLiteDatabase sqLiteAdaptor = getWritableDatabase();
        sqLiteAdaptor.execSQL("update " + Table_Name3 + " set " + product_quantity + " = " + quantity + " where " + product_id + " = " + product_ids +";");
    }



    public boolean checkExistsss(String id) {
        SQLiteDatabase sqLiteAdaptor = getWritableDatabase();
        Cursor cur = sqLiteAdaptor.rawQuery("Select * from " + Table_Name3 + " where " + product_id + " = '" + id + "'", null);
        return cur.getCount() > 0 ? true : false;
    }






    @SuppressLint("Range")
    public List<SQLitePOJO> getProduct(){
        // @/** DataModel dataModel = new DataModel();
        List<SQLitePOJO> data=new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query="SELECT * FROM " + Table_Name3 +
                " ORDER BY "+_ID + " DESC";
        Cursor cursor = db.rawQuery(query,null);
        StringBuffer stringBuffer = new StringBuffer();
        SQLitePOJO dataModel = null;
        while (cursor.moveToNext()) {
            dataModel= new SQLitePOJO();
            String count = cursor.getString(cursor.getColumnIndex(product_quantity));
            String productid = cursor.getString(cursor.getColumnIndex(product_id));
            String price = cursor.getString(cursor.getColumnIndex(product_amount));
            String name = cursor.getString(cursor.getColumnIndex(product_name));
            String url = cursor.getString(cursor.getColumnIndex(product_url));
            String mrp = cursor.getString(cursor.getColumnIndex(product_mrp));
            String title2 = cursor.getString(cursor.getColumnIndex(product_title2));
            dataModel.setWeight(count);
            dataModel.setIds(Integer.parseInt((productid)));
            dataModel.setprices(price);
            dataModel.setproductname(name);
            dataModel.setimagesurl(url);
            dataModel.setMrp(mrp);
            dataModel.setTitle2(title2);

            stringBuffer.append(dataModel);
            data.add(dataModel);
        }
        return data;
    }

    public void RemoveCart(String name)
    {
        db=getWritableDatabase();
        db=getReadableDatabase();
        boolean check=false;
        db.delete(Table_Name3, "product_id=?", new String[]{name});
        db.close();

    }

    }




