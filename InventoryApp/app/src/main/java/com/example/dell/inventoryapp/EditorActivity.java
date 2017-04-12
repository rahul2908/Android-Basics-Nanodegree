package com.example.dell.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dell.inventoryapp.data.ProductContract.ProductEntry;

import static android.view.View.GONE;


public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private EditText mProductNameEditText;

    private EditText mPriceEditText;

    private EditText mQuantityEditText;

    private EditText mMfgDateEditText;

    private ImageView mImageView;

    private static final int GET_PRODUCT_IMAGE = 1;

    private static int PRODUCT_LOADER = 1;

    String product_image = null;

    private boolean hasProductChanged = false;

    private Uri mCurrentProductUri = null;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            hasProductChanged = true;
            return false;
        }
    };

    Uri imageUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();

        Button deleteButton = (Button) findViewById(R.id.delete_button);

        if (mCurrentProductUri == null) {
            imageUri = Uri.parse("android.resource://" + this.getPackageName() + "/drawable/pic");
            setTitle(getString(R.string.editor_activity_title_new_product));
            deleteButton.setVisibility(GONE);
        } else {
            setTitle(getString(R.string.editor_activity_title_edit_product));
            getLoaderManager().initLoader(PRODUCT_LOADER, null, this);
        }

        mProductNameEditText = (EditText) findViewById(R.id.edit_product_name);
        mPriceEditText = (EditText) findViewById(R.id.edit_product_price);
        mQuantityEditText = (EditText) findViewById(R.id.edit_quantity);
        mMfgDateEditText = (EditText) findViewById(R.id.edit_product_mfg_date);
        mImageView = (ImageView) findViewById(R.id.image_view);

        mProductNameEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);
        mMfgDateEditText.setOnTouchListener(mTouchListener);

        Button addImageButton = (Button) findViewById(R.id.image_button);
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v ) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(Intent.createChooser(intent, "Select Image From"), GET_PRODUCT_IMAGE);
                }
            }
        });

        Button saveDataButton = (Button) findViewById(R.id.save_button);
        saveDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProduct();
                finish();
            }
        });


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog();
            }
        });

        Button addButton = (Button) findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int productQuantity;
                String orderQuantity = mQuantityEditText.getText().toString();
                if (TextUtils.isEmpty(orderQuantity)){
                    mQuantityEditText.setText("0");
                }
                productQuantity = Integer.parseInt(mQuantityEditText.getText().toString());
                productQuantity++;
                mQuantityEditText.setText("" + productQuantity);
            }
        });

        Button subButton = (Button) findViewById(R.id.sub_button);
        subButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int productQuantity;
                String orderQuantity = mQuantityEditText.getText().toString();
                if (TextUtils.isEmpty(orderQuantity)){
                    mQuantityEditText.setText("0");
                }
                productQuantity = Integer.parseInt(mQuantityEditText.getText().toString());
                if (productQuantity <= 0) {
                    Toast.makeText(EditorActivity.this, "First Buy some item", Toast.LENGTH_SHORT).show();
                } else {
                    productQuantity--;
                }
                mQuantityEditText.setText("" + productQuantity);
            }
        });
        final Button order = (Button) findViewById(R.id.order_button);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String orderName = mProductNameEditText.getText().toString();
                String orderQuantity = mQuantityEditText.getText().toString();
                Intent orderIntent = new Intent(Intent.ACTION_SENDTO);
                orderIntent.setData(Uri.parse("mailto:rahulsaini2908@gmail.com"));
                orderIntent.putExtra(Intent.EXTRA_TEXT, "Please order " + orderQuantity + " number of" + orderName);
                startActivity(orderIntent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == GET_PRODUCT_IMAGE && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            product_image = imageUri.toString();
            mImageView.setImageURI(imageUri);
        }
    }


    @Override
    public void onBackPressed() {

        if (!hasProductChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {ProductEntry._ID, ProductEntry.COLUMN_PRODUCT_NAME, ProductEntry.COLUMN_PRODUCT_PRICE, ProductEntry.COLUMN_PRODUCT_QUANTITY, ProductEntry.COLUMN_PRODUCT_IMAGE, ProductEntry.COLUMN_PRODUCT_MFG_DATE};
        return new CursorLoader(this, mCurrentProductUri, projection, null, null, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null || data.getCount() < 1) {
            return;
        }

        if (data.moveToFirst()) {

            int productNameColumnIndex = data.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
            int productPriceColumnIndex = data.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
            int productQuantityColumnIndex = data.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
            int productMFgDateColumnIndex = data.getColumnIndex(ProductEntry.COLUMN_PRODUCT_MFG_DATE);
            int productImageColumnIndex = data.getColumnIndex(ProductEntry.COLUMN_PRODUCT_IMAGE);

            String productName = data.getString(productNameColumnIndex);
            int productPrice = data.getInt(productPriceColumnIndex);
            int productQuantity = data.getInt(productQuantityColumnIndex);
            String productMfgDate = data.getString(productMFgDateColumnIndex);
            String productImage = data.getString(productImageColumnIndex);

            mProductNameEditText.setText(productName);
            mPriceEditText.setText(Integer.toString(productPrice));
            mQuantityEditText.setText(Integer.toString(productQuantity));
            mMfgDateEditText.setText(productMfgDate);
            imageUri = Uri.parse(productImage);
            mImageView = (ImageView) findViewById(R.id.image_view);
            mImageView.setImageURI(imageUri);

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mProductNameEditText.setText("");
        mPriceEditText.setText("");
        mQuantityEditText.setText("");
        mMfgDateEditText.setText("");
        mImageView.setImageResource(R.drawable.inventory_icon);
    }

    private void saveProduct() {

        String productName = mProductNameEditText.getText().toString().trim();
        String productPrice = mPriceEditText.getText().toString().trim();
        String productQuantity = mQuantityEditText.getText().toString().trim();
        String productMfgDate = mMfgDateEditText.getText().toString().trim();
        String productImage = imageUri.toString();

        if (mCurrentProductUri == null &&
                TextUtils.isEmpty(productName) || TextUtils.isEmpty(productPrice) ||
                TextUtils.isEmpty(productQuantity) || TextUtils.isEmpty(productImage) || TextUtils.isEmpty(productMfgDate)) {
            Toast.makeText(EditorActivity.this, "Fill All the Details", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(ProductEntry.COLUMN_PRODUCT_NAME, productName);
        int price = 0;
        if (!TextUtils.isEmpty(productPrice)) {
            price = Integer.parseInt(productPrice);
        }
        contentValues.put(ProductEntry.COLUMN_PRODUCT_PRICE, price);
        int quantity = 0;
        if (!TextUtils.isEmpty(productQuantity)) {
            quantity = Integer.parseInt(productQuantity);
        }
        contentValues.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity);
        contentValues.put(ProductEntry.COLUMN_PRODUCT_IMAGE, productImage);
        contentValues.put(ProductEntry.COLUMN_PRODUCT_MFG_DATE, productMfgDate);

        if (mCurrentProductUri == null) {

            Uri newUri = getContentResolver().insert(ProductEntry.CONTENT_URI, contentValues);

            if (newUri == null) {
                Toast.makeText(this, getString(R.string.editor_insert_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_insert_product_successful),
                        Toast.LENGTH_SHORT).show();
            }

        } else {
            int rowsAffected = getContentResolver().update(mCurrentProductUri, contentValues, null, null);

            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.editor_update_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_update_product_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

    }


    private void showDeleteConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteProduct();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteProduct() {

        if (mCurrentProductUri != null) {

            int rowsDeleted = getContentResolver().delete(mCurrentProductUri, null, null);

            if (rowsDeleted == 0) {

                Toast.makeText(this, getString(R.string.editor_delete_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(this, getString(R.string.editor_delete_product_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
