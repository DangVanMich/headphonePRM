package com.example.headphones_ecommerce_store.ui.product;


import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.headphones_ecommerce_store.DAO.ProductDAO;
import com.example.headphones_ecommerce_store.R;
import com.example.headphones_ecommerce_store.databinding.ActivityAddEditProductBinding;
import com.example.headphones_ecommerce_store.model.Product;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class AddEditProductActivity extends AppCompatActivity {
    private ActivityAddEditProductBinding binding;
    private long productId = -1;

    private ProductDAO db;
    private Uri selectedImageUri = null;
    private String currentImagePath = null;

    private ActivityResultLauncher<Intent> pickImageLauncher;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    //private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 101;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEditProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = new ProductDAO(this);

        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                openImagePicker();
            } else {
                Toast.makeText(this, "Permission denied to read external storage", Toast.LENGTH_SHORT).show();
            }
        });

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            Glide.with(this)
                                    .load(selectedImageUri)
                                    .placeholder(R.drawable.ic_menu_gallery)
                                    .into(binding.ivProductPreview);
                        }
                    }
                }
        );

        if(getIntent().hasExtra("PRODUCT_ID")) {
            productId = getIntent().getLongExtra("PRODUCT_ID", -1);

            if(productId != -1) {
                setTitle("Edit Product");
                loadProductData();
                binding.btnDeleteProduct.setVisibility(View.VISIBLE);
            }

        } else {
            setTitle("Add New Product");
        }

        binding.btnChooseImage.setOnClickListener(v -> checkPermissionAndOpenImagePicker());
        binding.btnSaveProduct.setOnClickListener(v -> saveProduct());
        binding.btnDeleteProduct.setOnClickListener(v -> showDeleteConfirmationDialog());


    }

    private void loadProductData(){
        Product product = db.getProductById(productId);
        binding.etProductName.setText(product.getName());
        binding.etProductDescription.setText(product.getDescription());
        binding.etProductPrice.setText(String.valueOf(product.getPrice()));
        binding.etProductCategory.setText(product.getCategory());
        binding.etProductStockQuantity.setText(String.valueOf(product.getStockQuantity()));

        if(product.getFeatures() != null && !product.getFeatures().isEmpty()) {
            binding.etProductFeatures.setText(TextUtils.join(", ", product.getFeatures()));
        }

        if(product.getColorOptions() != null && !product.getColorOptions().isEmpty()) {
            binding.etProductColorOptions.setText(TextUtils.join(", ", product.getColorOptions()));
        }

        binding.etProductBrand.setText(product.getBrand());
    }

    private void saveProduct(){
        String name = binding.etProductName.getText().toString().trim();
        String description = binding.etProductDescription.getText().toString().trim();
        String brand = binding.etProductBrand.getText().toString().trim();
        String priceStr = binding.etProductPrice.getText().toString().trim();
        String imagePathToSave = currentImagePath;

        if (name.isEmpty() || description.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = 0.0;
        try {
            price = Double.parseDouble(binding.etProductPrice.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price", Toast.LENGTH_SHORT).show();
            return;
        }

        String category = binding.etProductCategory.getText().toString().trim();

        int stockQuantity = 0;

        try {
            stockQuantity = Integer.parseInt(binding.etProductStockQuantity.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid stock", Toast.LENGTH_SHORT).show();
        }

        String featureString = binding.etProductFeatures.getText().toString().trim();

        List<String> featureList = TextUtils.isEmpty(featureString) ?
                Collections.emptyList() :
                new ArrayList<>(Arrays.asList(featureString.split("\\s*,\\s*")));

        String colorOptionsString = binding.etProductColorOptions.getText().toString().trim();

        List<String> colorOptionsList = TextUtils.isEmpty(colorOptionsString) ?
                Collections.emptyList() :
                new ArrayList<>(Arrays.asList(colorOptionsString.split("\\s*,\\s*")));




        if (selectedImageUri != null) {
            // Check if the selected URI is different from the currently saved one (if any)
            // This avoids re-copying if the user picked the same image again (edge case)
            boolean shouldCopy = true;
            if (currentImagePath != null) {
                Uri currentImageUriFromPath = Uri.parse(currentImagePath);
                if (selectedImageUri.equals(currentImageUriFromPath)) {
                    shouldCopy = false; // It's the same image that was loaded
                }
            }

            if(shouldCopy) {
                imagePathToSave = copyImageToAppStorage(selectedImageUri);
                if (imagePathToSave == null) {
                    Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
                    return; // Don't proceed if image saving failed
                }
            } else {
                imagePathToSave = selectedImageUri.toString(); // Already a valid path/URI string
            }
        } else if (productId == -1 && currentImagePath == null) {
            imagePathToSave = "";
        }

        boolean success;
        Product product = new Product();

        product.setName(name);
        product.setDescription(description);
        product.setBrand(brand);
        product.setPrice(price);
        product.setCategory(category);
        product.setStockQuantity(stockQuantity);
        product.setFeatures(featureList);
        product.setColorOptions(colorOptionsList);
        product.setThumbnailImageUrl(imagePathToSave);

        if (productId == -1) {
            success = db.addProduct(product);
        } else {
            product.setId(productId);
            success = db.updateProduct(product);
        }

        if (success) {
            Toast.makeText(this, "Product saved successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error saving product", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDeleteConfirmationDialog(){
        new AlertDialog.Builder(this)
                .setTitle("Delete Product")
                .setMessage("Are you sure you want to delete this product?")
                .setPositiveButton("Delete", (dialog, which) -> deleteProduct())
                .setNegativeButton("Cancel", null)
                .show();
    }
    private void deleteProduct() {
        if (productId != -1) {
            boolean success = db.deleteProduct(productId);
            if (success) {
                Toast.makeText(this, "Product deleted", Toast.LENGTH_SHORT).show();
                finish(); // Go back to the product list
            } else {
                Toast.makeText(this, "Error deleting product", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }
    private void checkPermissionAndOpenImagePicker() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            openImagePicker();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // Show an explanation to the user *asynchronously*
            new AlertDialog.Builder(this)
                    .setTitle("Permission Needed")
                    .setMessage("This permission is needed to access images from your device.")
                    .setPositiveButton("OK", (dialog, which) -> requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE))
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .create().show();
        }
        else {
            // Directly request the permission
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String extension = mime.getExtensionFromMimeType(contentResolver.getType(uri));
//        if (extension == null) {
//            // Fallback: try to get it from the path
//            String path = uri.getPath();
//            if (path != null) {
//                int cut = path.lastIndexOf('.');
//                if (cut != -1) {
//                    extension = path.substring(cut + 1);
//                }
//            }
//        }
        return extension != null ? extension : "jpg";
    }
    private String copyImageToAppStorage(Uri sourceUri) {
        try {

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
            String imageFileName = "JPEG_" + timeStamp + "." + getFileExtension(sourceUri);

            // Get  internal file
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES); // Or getFilesDir() for internal
            if (storageDir != null && !storageDir.exists()) {
                if (!storageDir.mkdirs()) {
                    return null;
                }
            }

            File destinationFile = new File(storageDir, imageFileName);

            InputStream inputStream = getContentResolver().openInputStream(sourceUri);
            OutputStream outputStream = new FileOutputStream(destinationFile);

            if (inputStream != null) {
                byte[] buf = new byte[1024];
                int len;
                while ((len = inputStream.read(buf)) > 0) {
                    outputStream.write(buf, 0, len);
                }
                inputStream.close();
            }
            outputStream.close();

            return Uri.fromFile(destinationFile).toString();

        } catch (IOException e) {
            return null;
        }
    }
}

