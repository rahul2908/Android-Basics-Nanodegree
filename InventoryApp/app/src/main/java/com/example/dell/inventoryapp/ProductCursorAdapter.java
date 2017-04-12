package com.example.dell.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.inventoryapp.data.ProductContract.ProductEntry;

/**
 * Created by dell on 3/18/2017.
 */

public class ProductCursorAdapter extends CursorAdapter {

    public ProductCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        TextView productNameTextView = (TextView) view.findViewById(R.id.product_name);
        TextView productPriceTextView = (TextView) view.findViewById(R.id.price_text_view);
        final TextView productQuantityTextView = (TextView) view.findViewById(R.id.quantity_left);
        TextView productMfgdateTextView = (TextView) view.findViewById(R.id.mfg_date_text_view);
        final int product_Id = cursor.getInt(cursor.getColumnIndexOrThrow(ProductEntry._ID));

        int productNameCoulumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        int productPriceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
        int productQuantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
        int productMfgdateColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_MFG_DATE);

        String product_Name = cursor.getString(productNameCoulumnIndex);
        final int product_Price = cursor.getInt(productPriceColumnIndex);
        final int product_Quantity = cursor.getInt(productQuantityColumnIndex);
        String product_Mfgdate = cursor.getString(productMfgdateColumnIndex);

        Button soldButton = (Button) view.findViewById(R.id.sold_button);
        soldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int leftquantity = product_Quantity;

                if (leftquantity <= 0) {
                    Toast.makeText(context, "Sorry,Product is Out Of Stock", Toast.LENGTH_SHORT).show();
                } else {
                    leftquantity--;
                }

                ContentValues contentValues = new ContentValues();
                contentValues.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, leftquantity);

                Uri uri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, product_Id);

                context.getContentResolver().update(uri, contentValues, null, null);

                productQuantityTextView.setText(product_Quantity + " Products Still Left");

            }
        });

        productNameTextView.setText("Product Name is " + product_Name);
        productPriceTextView.setText("Rs." + product_Price);
        productQuantityTextView.setText(product_Quantity + " Products Still Left");
        productMfgdateTextView.setText(product_Mfgdate);


    }

}
