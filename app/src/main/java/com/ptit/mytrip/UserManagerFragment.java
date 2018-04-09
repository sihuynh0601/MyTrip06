package com.ptit.mytrip;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by Tien Si Huynh on 3/10/2018.
 */

public class UserManagerFragment extends Fragment {

    private static String TAG = UserManagerFragment.class.getSimpleName();
    private final int REQUEST_CODE = 2;
    private final static String STORAGGE_REF = "mytrip-33a4a.appspot.com";

    private ImageButton btnBack, btnAvatarChange;
    private ImageView imgProfileImage;
    private EditText edtName, edtEmail;
    private TextView txtUID;
    private Button btnEdit, btnSave;

    private String lastDisplayName = "";
    private String lastEmail = "";
    private String lastProfileUrl = "";
    private Bitmap bitmap;
    private FirebaseUser currentUser;
    private DatabaseReference mData;
    private FirebaseStorage storage;
    private StorageReference profileImageStorageRef;

    public UserManagerFragment(){
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mData = FirebaseDatabase.getInstance().getReferenceFromUrl(Constant.CHILD_USER_IMAGE_URL);
        storage = FirebaseStorage.getInstance();
        profileImageStorageRef = storage.getReferenceFromUrl(Constant.PROFILE_STORAGE__FOLDER_URL);

        lastDisplayName = currentUser.getDisplayName();
        lastEmail = currentUser.getEmail();
        lastProfileUrl = currentUser.getPhotoUrl().toString();

//        progressDialog = new ProgressDialog(getActivity());
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progressDialog.setMessage("Loading ...");
//        progressDialog.setTitle("Please wailt");
//        progressDialog.setIndeterminate(true);
//        Log.i(TAG, lastProfileUrl);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View userManagerView = inflater.inflate(R.layout.fragment_user_manager,
                container, false);
        Log.d(TAG, "onCreateView");
        addControls(userManagerView);
        if(currentUser != null){
            addEvents(userManagerView);
        }
        return userManagerView;
    }

    private void addControls(View userManagerView) {
        btnBack = userManagerView.findViewById(R.id.btnBack);
        btnAvatarChange = userManagerView.findViewById(R.id.btnAvatarChange);
        imgProfileImage = userManagerView.findViewById(R.id.profile_image);
        edtName = userManagerView.findViewById(R.id.edtUserName);
        edtEmail = userManagerView.findViewById(R.id.edtEmail);
        txtUID = userManagerView.findViewById(R.id.txtUid);
        btnEdit = userManagerView.findViewById(R.id.btnUserInfoChange);
        btnSave = userManagerView.findViewById(R.id.btnSaveUserInfo);
    }

    private void addEvents(View userManagerView) {
        edtName.setText(currentUser.getDisplayName());
        edtEmail.setText(currentUser.getEmail());
        txtUID.setText(currentUser.getUid());

        if(currentUser.getPhotoUrl()!= null){
            Glide.with(getActivity()).load(currentUser.getPhotoUrl().toString())
                    .into(imgProfileImage);
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsFragment settingsFragment = new SettingsFragment();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.fragmentContainer, settingsFragment).commit();
            }});

        btnAvatarChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Xử lý cập nhật ảnh đại diện.
                updateUserProfileImage();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnEdit.setEnabled(false);
                edtName.setEnabled(true);
                edtEmail.setEnabled(true);
                btnSave.setEnabled(true);
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSave.setEnabled(false);
                edtName.setEnabled(false);
                edtEmail.setEnabled(false);
                btnEdit.setEnabled(true);
                //TODO: Xử lý cập nhật thông tin user
                updateNameAndEmail();
            }
        });

    }

    private void updateNameAndEmail() {
        String hoTen = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();

        if(!hoTen.equals(lastDisplayName)){
            if (hoTen.isEmpty() || email.isEmpty()) {
                return;
            }
            if (hoTen.isEmpty()) {
                Log.e(TAG, "Họ tên bị bỏ trống !");
                Toast.makeText(getActivity(), "Họ tên bị bỏ trống !", Toast.LENGTH_SHORT).show();
                edtName.setText(currentUser.getDisplayName());
                return;
            }

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(hoTen)
                    .build();
            currentUser.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                lastDisplayName = currentUser.getDisplayName();
                                Log.d(TAG, "User profile updated: " + currentUser.getDisplayName());
                                Toast.makeText(getActivity(), "Đã cập nhật Tên.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "User profile updated: failed.");
                    Toast.makeText(getActivity(), "Cập nhật tên không thành công.",Toast.LENGTH_SHORT).show();
                }
            });
        }

        if(!email.equals(lastEmail)){
            if (email.isEmpty()) {
                Log.e(TAG, "Email tên bị bỏ trống !");
                Toast.makeText(getActivity(), "Email bị bỏ trống !", Toast.LENGTH_SHORT).show();
                edtEmail.setText(currentUser.getEmail());
                return;
            }
            currentUser.updateEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                lastEmail = currentUser.getEmail();
                                Log.d(TAG, "User email address updated: " + currentUser.getEmail());
                                Toast.makeText(getActivity(), "Đã cập nhật Email.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "User email address updated: failed.");
                    Toast.makeText(getActivity(),"Cập nhật email không thành công.", Toast.LENGTH_SHORT);
                }
            });
        }
    }

    private void updateUserProfileImage() {

        // Intent chọn ảnh từ gallery
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT,null);
        galleryIntent.setType("image/*");
        galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
        // Intent lấy ảnh từ camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Intent lựa chọn lấy ảnh camera hoặc gallery
        Intent chooser = new Intent(Intent.ACTION_CHOOSER);
        chooser.putExtra(Intent.EXTRA_INTENT, galleryIntent);
        chooser.putExtra(Intent.EXTRA_TITLE , "Select Image From: ");
        Intent[] intentArray =  {cameraIntent};
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

        startActivityForResult(chooser, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && data!= null){

            if(data.getData() != null){
                try
                {   // Xử lý lấy ảnh từ gallery
                    bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
                    imgProfileImage.setImageBitmap(bitmap);
                    upLoadProfileImageToFirebaseStorage();
                }catch (IOException e)
                {
                    e.printStackTrace();
                }
            }else{
                //Xử lý lấy ảnh từ camera
                bitmap = (Bitmap) data.getExtras().get("data");
                imgProfileImage.setImageBitmap(bitmap);
                upLoadProfileImageToFirebaseStorage();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void upLoadProfileImageToFirebaseStorage(){
        imgProfileImage.setDrawingCacheEnabled(true);
        imgProfileImage.buildDrawingCache();
        Bitmap bitmap = imgProfileImage.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        Calendar calendar = Calendar.getInstance();
        final String photoName = "image" + calendar.getTimeInMillis() + ".png";
        StorageReference imgRef = profileImageStorageRef.child(photoName);

        UploadTask uploadTask = imgRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.d(TAG, "Upload profile image to Firebase Storage: FAILED");
                Toast.makeText(getContext(), "Cập nhật ảnh đại diện thất bại",
                        Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Log.d(TAG, "Upload profile image to Firebase Storage: SUCCESS");
                final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                // cập nhật lại photoUrl của currentUser
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setPhotoUri(downloadUrl)
                        .build();
                currentUser.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User photo url updated: " + downloadUrl.toString());
                                    //Xóa ảnh đại diện cũ nếu nó được lưu trong FirebaseStorage
                                    final String uID = currentUser.getUid();
                                    if(lastProfileUrl.contains(STORAGGE_REF)){
                                        // Lấy tên ảnh đại diện được lưu trên database
                                        mData.child(uID).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                String deleteImageName = dataSnapshot.getValue().toString().trim();
                                                Log.d(TAG, "Last profile image name: "+ deleteImageName);
                                                // Xóa ảnh trong FirebaseStorage
                                                StorageReference deleteImageRef = profileImageStorageRef.child(deleteImageName);
                                                deleteImageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "Delete last profile image SUCCESSED.");
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.e(TAG, "Delete last profile image FAILED: " + e.toString());
                                                    }
                                                });
                                                //Cập nhật thông tin ảnh lên FirebaseDatabase.
                                                mData.child(uID).setValue(photoName);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }

                                }
                            }
                        });
                lastProfileUrl = currentUser.getPhotoUrl().toString();
                Toast.makeText(getContext(), "Đã cập nhật Avatar.",Toast.LENGTH_SHORT).show();
            }

        });

    }


}

