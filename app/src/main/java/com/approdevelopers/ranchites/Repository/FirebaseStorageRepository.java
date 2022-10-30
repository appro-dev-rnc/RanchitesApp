package com.approdevelopers.ranchites.Repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FirebaseStorageRepository {

    private final StorageReference mStorageReference;
    private static FirebaseStorageRepository storageRepositoryInstance = null;

    private FirebaseStorageRepository() {
        // References
        FirebaseStorage mFirebaseStorage = FirebaseStorage.getInstance();
        mStorageReference = mFirebaseStorage.getReference();
    }


    public static FirebaseStorageRepository getStorageRepositoryInstance() {
        if (storageRepositoryInstance == null) {
            storageRepositoryInstance = new FirebaseStorageRepository();
        }
        return storageRepositoryInstance;
    }

    public StorageReference getmStorageReference() {
        return mStorageReference;
    }
 public UploadTask uploadLoBannerImageByteArray(String locationId,byte[] bitmapArray) {


            String bannerImgPath = "Locations/" + locationId + "/BannerImage/";
            String bannerImgName = "BannerImage_" + locationId;
            StorageReference mBannerImageReference = mStorageReference.child(bannerImgPath + bannerImgName);

            return mBannerImageReference.putBytes(bitmapArray);

    }

    public UploadTask uploadUserProfDisplayWdByteArray(String userId, byte[] bitmapArray) {


        String profImgPath = "Users/" + userId + "/ProfileImage/";
        String profImgName = "Profile_" + userId;
        StorageReference mProfImageUploadRef = mStorageReference.child(profImgPath + profImgName);

        return mProfImageUploadRef.putBytes(bitmapArray);


    }public UploadTask uploadUserAccountMediaWdByteArray(String userId, byte[] bitmapArray) {

        String profImgPath = "Users/" + userId + "/MediaImages/";
        String profImgName = "Media_" + Timestamp.now().getSeconds();
        StorageReference mProfImageUploadRef = mStorageReference.child(profImgPath + profImgName);

        return mProfImageUploadRef.putBytes(bitmapArray);


    }
   public UploadTask uploadLocationMediaWdByteArray(String locationId, byte[] bitmapArray) {


        String profImgPath = "Locations/" + locationId + "/MediaImages/";
        String profImgName = String.valueOf(Timestamp.now().getSeconds());
        StorageReference mProfImageUploadRef = mStorageReference.child(profImgPath + profImgName);

        return mProfImageUploadRef.putBytes(bitmapArray);


    }

    public Task<Void> deleteUserMyProfileImage(String storagePath){
        StorageReference reference = mStorageReference.child(storagePath);
        return reference.delete();
    }




}
