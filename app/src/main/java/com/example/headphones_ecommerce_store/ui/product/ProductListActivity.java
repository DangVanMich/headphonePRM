package com.example.headphones_ecommerce_store.ui.product;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.example.headphones_ecommerce_store.DAO.ProductDAO;
import com.example.headphones_ecommerce_store.R;
import com.example.headphones_ecommerce_store.adapters.ProductAdapter;
import com.example.headphones_ecommerce_store.controller.BottomNavHelper;
import com.example.headphones_ecommerce_store.database.DBHelper;
import com.example.headphones_ecommerce_store.databinding.ActivityProductListBinding;
import com.example.headphones_ecommerce_store.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {
    private static final String TAG = "ProductListActivity";
    private ActivityProductListBinding binding;
    private ProductDAO db;

    private ProductAdapter adapter;

    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        BottomNavHelper.setupBottomNav(this, 0);
        db = new ProductDAO(this);

        productList = new ArrayList<>();

        setupRecyclerView();

        binding.fabAddProduct.setOnClickListener(v -> {
            // TODO
             Intent intent = new Intent(ProductListActivity.this, AddEditProductActivity.class);
             startActivity(intent);
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        // Load all products by default when activity resumes
        loadProducts(null);

    }

    private void setupRecyclerView(){
        binding.rvProducts.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter(this, productList, product -> {
            // TODO: Handle item click -> Go to detail/edit screen
             Intent intent = new Intent(ProductListActivity.this, AddEditProductActivity.class);
             intent.putExtra("PRODUCT_ID", product.getId());
             startActivity(intent);
        });

        binding.rvProducts.setAdapter(adapter);

    }

    private void loadProducts(String sortBy) {
        Log.d(TAG, "Loading products with sortBy: " + sortBy);
        Cursor cursor = db.getAllProducts(sortBy);

        List<Product> newItems = extractProducts(cursor);

        if (cursor != null) {
            cursor.close();
        }

        productList.clear();
        productList.addAll(newItems);

        if (adapter != null) {
            adapter.notifyDataSetChanged();

        }
    }

    private List<Product> extractProducts(Cursor cursor) {
        List<Product> list = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            Log.d(TAG, "extractProducts - Cursor has " + cursor.getCount() + " items.");
            do {
                try {
                    long id = cursor.getLong(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PRODUCT_ID));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PRODUCT_NAME));
                    String desc = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PRODUCT_DESC));
                    double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PRODUCT_PRICE));
                    String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PRODUCT_THUMBNAIL_IMAGE_URL));
                    list.add(new Product(id, name, desc, price, imageUrl));
                } catch (IllegalArgumentException e) {
                    Log.e(TAG, "extractProducts - Column not found: " + e.getMessage());
                }
            } while (cursor.moveToNext());
        } else {
            Log.d(TAG, "extractProducts - Cursor is null or empty.");
        }
        return list;
    };

    private void searchProducts(String query) {
        Cursor cursor = db.searchProducts(query);
        List<Product> searchedItems = extractProducts(cursor);
        if (cursor != null) {
            cursor.close();
        }

        productList.clear();
        productList.addAll(searchedItems);

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchProducts(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchProducts(newText);
                return true;
            }
        });

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         int id = item.getItemId();
//         if(id == R.id.action_sort_price_asc) {
//             loadProducts("price_asc");
//             return true;
//         }else if(id == R.id.action_sort_price_desc) {
//             loadProducts("price_desc");
//             return true;
//         }

         return super.onOptionsItemSelected(item);
    }
}
