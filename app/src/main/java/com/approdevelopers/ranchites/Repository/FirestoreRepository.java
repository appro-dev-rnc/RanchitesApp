package com.approdevelopers.ranchites.Repository;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.approdevelopers.ranchites.Models.BestRatedLocation;
import com.approdevelopers.ranchites.Models.CategoriesModel;
import com.approdevelopers.ranchites.Models.CityProfessionalsModel;
import com.approdevelopers.ranchites.Models.CityUpdatesModel;
import com.approdevelopers.ranchites.Models.EditLocationModel;
import com.approdevelopers.ranchites.Models.FeaturedLocation;
import com.approdevelopers.ranchites.Models.ImagesModel;
import com.approdevelopers.ranchites.Models.LocationModel;
import com.approdevelopers.ranchites.Models.MostSearchedLocation;
import com.approdevelopers.ranchites.Models.PlacesBasicModel;
import com.approdevelopers.ranchites.Models.RecentlyViewedDetailedModel;
import com.approdevelopers.ranchites.Models.RecentlyViewedLocation;
import com.approdevelopers.ranchites.Models.ReportModel;
import com.approdevelopers.ranchites.Models.ReviewModel;
import com.approdevelopers.ranchites.Models.SearchResultModel;
import com.approdevelopers.ranchites.Models.User;
import com.approdevelopers.ranchites.Models.UserBasicInfo;
import com.approdevelopers.ranchites.Models.UserCompleteDetModel;
import com.approdevelopers.ranchites.Models.UserReviewModel;
import com.approdevelopers.ranchites.Models.UserUpdateModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Source;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

public class FirestoreRepository {

    //code constants

    private static final int REPORT_RESULT_SUCCESS = 1000;
    private static final int REPORT_RESULT_FAILED = 1050;

    //livedata variables
    private final MutableLiveData<List<User>> userListLiveData;
    private final MutableLiveData<List<SearchResultModel>> nearMeSearchResultsLive;


    private final FirebaseFirestore mFirestore;

    //collection reference variables for different paths
    private final CollectionReference mUserReference;
    private final CollectionReference mLocationReference;
    private final CollectionReference mReviewsAndRatingsReference;
    private final CollectionReference mUserReviewsAndRatingsReference;
    private final CollectionReference mPlacesSearchCountReference;
    private final CollectionReference mCityUpdatesReference;
    private final CollectionReference mCategoriesReference;
    private final CollectionReference mUserSavedLocationsReference;
    private final CollectionReference mPlacesReportsReference;
    private final CollectionReference mPlacesNamesReference;
    private final CollectionReference mPhoneNumberListReferece;
    private final CollectionReference mPlaceReviewReportsReference;
    private final CollectionReference mUserReviewReportsReference;
    private final CollectionReference mNameRefCollection;


    //Strings for different paths
    private static final String REVIEWS_AND_RATINGS_ID = "ReviewsAndRatings";
    private static final String LOCATIONS_ID = "Locations";
    private static final String USERS = "Users";
    private static final String PLACES_REVIEWS_ID = "Reviews";
    private static final String USER_REVIEWS_ID = "Reviews";
    private static final String PLACES_SEARCH_COUNT = "PlacesSearchCount";
    private static final String CITY_UPDATES_ID = "CityUpdates";
    private static final String CATEGORIES_ID = "Categories";
    private static final String USER_SAVED_LOCATIONS_ID = "UserSavedLocations";
    private static final String PLACES_REPORTS_ID = "PlacesReports";
    private static final String USER_REVIEW_AND_RATING = "UserReviewsAndRatings";
    private static final String PLACES_NAMES_REF = "PlacesNamesRef";
    private static final String PLACES_NAME_DOC_REF = "placeName";
    private static final String CATEGORY_NAME_DOC_REF = "categoryName";
    private static final String CATEGORY_LAST_MODIFIED = "lastModified";
    private static final String DOCUMENT_ID = "documentId";
    private static final String DOCUMENT_TYPE = "documentType";
    private static final String PROFILE_IMAGES = "ProfileImages";
    private static final String STORAGE_PATH = "storagePath";
    private static final String MEDIA_IMAGES = "MediaImages";
    private static final String SEARCH_BY_PROFESSION_ENABLED = "searchByProfessionEnabled";
    private static final String UID = "uId";
    private static final String USER_NAME = "userName";
    private static final String USER_PASSWORD = "userPassword";
    private static final String USER_PHONE = "userPhone";
    private static final String USER_ABOUT = "userAbout";
    private static final String USER_RATING = "userRating";
    private static final String USER_PROFILE_IMAGE_URL = "userProfileImageUrl";
    private static final String USER_PROFESSION = "userProfession";
    private static final String LOCATION_SHARE_ENABLED = "locationShareEnabled";
    private static final String RECENTLY_VIEWED = "RecentlyViewed";
    private static final String VIEW_TIMESTAMP = "viewTimestamp";
    private static final String TITLE = "title";
    private static final String IMAGE_URL = "imageUrl";
    private static final String OWNER_ID = "ownerId";
    private static final String SEARCH_COUNT = "searchCount";
    private static final String OVERALL_RATING = "overallRating";
    private static final String MEDIA = "Media";
    private static final String POSITION = "position";
    private static final String LIKES = "Likes";
    private static final String USER_ID = "userId";
    private static final String PLACE_ID = "placeId";
    private static final String DELETED_PLACE_RECORD = "DeletedPlaceRecord";
    private static final String DELETE_TIME_STAMP = "deleteTimestamp";
    private static final String SEARCH_KEYWORD = "searchKeyword";
    private static final String RECENT_SEARCHES = "RecentSearches";
    private static final String REPORTS_CATEGORY = "ReportsCategory";
    private static final String TIME_STAMP = "timeStamp";
    private static final String SAVED = "Saved";
    private static final String SEARCH_TIMESTAMP = "searchTimeStamp";
    private static final String RATING = "rating";
    private static final String LATEST_REVIEW = "latestReview";
    private static final String FEATURED_PLACE_ID = "featuredPlaceId";
    private static final String FEATURED_PLACE = "featuredPlace";
    private static final String PHONE_NUMBER_LIST = "PhoneNumberList";
    private static final String PLACE_REVIEW_REPORTS = "PlaceReviewReports";
    private static final String USER_REVIEW_REPORTS = "UserReviewReports";
    private static final String UPLOAD_TIME_STAMP = "uploadTimestamp";
    private static final String REVIEW_TIME_STAMP = "reviewTimestamp";
    private static final String REPORTS = "Reports";
    private static final String REPORTED = "reported";
    private static final String REPORT_COUNT = "reportCount";
    private static final String REVIEW_USER_ID = "reviewUserId";
    private static final String LAST_MODIFIED_COLLECTION = "LastModifiedCollection";
    private static final String NAME_REF_COLLECION = "NameRefCollection";


    //fireStore repo instance
    private static FirestoreRepository fireStoreRepositoryInstance = null;

    //private constructor for Firestore Repository class
    private FirestoreRepository() {
        //init fireStore instance
        this.mFirestore = FirebaseFirestore.getInstance();

        //init liveData variables
        this.userListLiveData = new MutableLiveData<>();
        this.nearMeSearchResultsLive = new MutableLiveData<>();


        //init CollectionReference Variables
        this.mUserReference = mFirestore.collection(USERS);
        this.mLocationReference = mFirestore.collection(LOCATIONS_ID);
        this.mReviewsAndRatingsReference = mFirestore.collection(REVIEWS_AND_RATINGS_ID);
        this.mPlacesSearchCountReference = mFirestore.collection(PLACES_SEARCH_COUNT);
        this.mCityUpdatesReference = mFirestore.collection(CITY_UPDATES_ID);
        this.mCategoriesReference = mFirestore.collection(CATEGORIES_ID);
        this.mUserSavedLocationsReference = mFirestore.collection(USER_SAVED_LOCATIONS_ID);
        this.mPlacesReportsReference = mFirestore.collection(PLACES_REPORTS_ID);
        this.mUserReviewsAndRatingsReference = mFirestore.collection(USER_REVIEW_AND_RATING);
        this.mPlacesNamesReference = mFirestore.collection(PLACES_NAMES_REF);
        this.mPhoneNumberListReferece = mFirestore.collection(PHONE_NUMBER_LIST);
        this.mPlaceReviewReportsReference = mFirestore.collection(PLACE_REVIEW_REPORTS);
        this.mUserReviewReportsReference = mFirestore.collection(USER_REVIEW_REPORTS);
        this.mNameRefCollection = mFirestore.collection(NAME_REF_COLLECION);


    }

    //method for getting single instance of FirestoreRepository Class
    public static FirestoreRepository getInstance() {
        if (fireStoreRepositoryInstance == null) {
            fireStoreRepositoryInstance = new FirestoreRepository();
        }
        return fireStoreRepositoryInstance;
    }

    public CollectionReference getmLocationReference() {
        return mLocationReference;
    }

    private final Source CACHE = Source.CACHE;
    private final Source SERVER = Source.SERVER;
    private static final String CATEGORIES_PREFERENCE = "CategoriesPreference";

    //getter for categories live data
    public MutableLiveData<List<CategoriesModel>> getCategoriesLiveData(Context context) {

        MutableLiveData<List<CategoriesModel>> categoriesLiveData = new MutableLiveData<>();

        SharedPreferences settings = context.getSharedPreferences(CATEGORIES_PREFERENCE, 0);
        int savedLastModified = settings.getInt(CATEGORY_LAST_MODIFIED, 0); //0 is the default value

        if (savedLastModified==0){
            mCategoriesReference.get(SERVER).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot result = task.getResult();
                    List<CategoriesModel> categories = new ArrayList<>();
                    if (result != null) {
                        for (QueryDocumentSnapshot snapshot : result) {
                            if (snapshot.getString(CATEGORY_NAME_DOC_REF) != null) {
                                categories.add(snapshot.toObject(CategoriesModel.class));
                            } else {
                                int lastModified = Objects.requireNonNull(snapshot.getLong(CATEGORY_LAST_MODIFIED)).intValue();
                                SharedPreferences settings1 = context.getSharedPreferences(CATEGORIES_PREFERENCE, 0);
                                SharedPreferences.Editor editor = settings1.edit();
                                editor.putInt(CATEGORY_LAST_MODIFIED, lastModified);
                                editor.apply();
                            }
                        }
                    }
                    categoriesLiveData.postValue(categories);
                }
            });


        }
        else {
            mCategoriesReference.orderBy(CATEGORY_LAST_MODIFIED).get(SERVER).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    int lastModifiedServer = -1;
                    QuerySnapshot taskResult = task.getResult();
                    for (QueryDocumentSnapshot snapshot : taskResult) {
                        lastModifiedServer = Objects.requireNonNull(snapshot.getLong(CATEGORY_LAST_MODIFIED)).intValue();
                    }

                    if (savedLastModified != lastModifiedServer) {
                        mCategoriesReference.get(SERVER).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                List<CategoriesModel> categories = new ArrayList<>();
                                QuerySnapshot taskResult1 = task1.getResult();
                                if (taskResult1 != null) {
                                    for (QueryDocumentSnapshot snapshot : taskResult1) {
                                        if (snapshot.getString(CATEGORY_NAME_DOC_REF) != null) {
                                            categories.add(snapshot.toObject(CategoriesModel.class));
                                        }
                                    }
                                }
                                categoriesLiveData.postValue(categories);
                            }
                        });
                    } else {
                        mCategoriesReference.get(CACHE).addOnCompleteListener(task12 -> {
                            if (task12.isSuccessful()) {
                                QuerySnapshot taskResult12 = task12.getResult();
                                List<CategoriesModel> categories = new ArrayList<>();
                                if (taskResult12 != null) {
                                    for (QueryDocumentSnapshot snapshot : taskResult12) {
                                        if (snapshot.getString(CATEGORY_NAME_DOC_REF) != null) {
                                            categories.add(snapshot.toObject(CategoriesModel.class));
                                        }
                                    }
                                }
                                categoriesLiveData.postValue(categories);

                            }
                        });
                    }
                }else {
                    mCategoriesReference.get(CACHE).addOnCompleteListener(task13 -> {
                        if (task13.isSuccessful()) {
                            QuerySnapshot taskResult = task13.getResult();
                            List<CategoriesModel> categories = new ArrayList<>();
                            if (taskResult != null) {
                                for (QueryDocumentSnapshot snapshot : taskResult) {
                                    if (snapshot.getString(CATEGORY_NAME_DOC_REF) != null) {
                                        categories.add(snapshot.toObject(CategoriesModel.class));
                                    }
                                }
                            }
                            categoriesLiveData.postValue(categories);

                        }
                    });
                }
            });
        }

        return categoriesLiveData;
    }


    //getter for categories live data
    public MutableLiveData<List<String>> getNearMeCategoriesLiveData() {

        MutableLiveData<List<String>> nearMeCategories = new MutableLiveData<>();
        List<String> categoryFinal = new ArrayList<>();
        List<String> categoriesList = new ArrayList<>();
        List<String> placeNamesList = new ArrayList<>();


        Task<QuerySnapshot> categoryTask = mCategoriesReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult() != null) {
                    categoriesList.clear();


                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
                        String category = snapshot.getString(CATEGORY_NAME_DOC_REF);
                        categoriesList.add(category);
                    }
                }
            }
        });
        Task<QuerySnapshot> placeNameTask = mPlacesNamesReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                placeNamesList.clear();
                if (task.getResult() != null) {
                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
                        String placeName = snapshot.getString(PLACES_NAME_DOC_REF);
                        placeNamesList.add(placeName);
                    }
                }
            }
        });


        Tasks.whenAllComplete(categoryTask, placeNameTask).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                categoryFinal.addAll(categoriesList);
                categoryFinal.addAll(placeNamesList);

                nearMeCategories.postValue(categoryFinal);

            }
        });

        return nearMeCategories;
    }


    //getter for current location detail live data

    public MutableLiveData<LocationModel> getCurrentLocationLiveData(String locationId) {
        MutableLiveData<LocationModel> currentLocLiveData = new MutableLiveData<>();
        if (locationId == null || locationId.equals("")) {
            currentLocLiveData.postValue(null);
            return currentLocLiveData;
        }
        mLocationReference.whereEqualTo(DOCUMENT_ID, locationId).addSnapshotListener((value, error) -> {
            LocationModel currentLocation = null;
            if (value != null) {
                for (QueryDocumentSnapshot snapshot : value)
                    currentLocation = snapshot.toObject(LocationModel.class);
            }
            currentLocLiveData.postValue(currentLocation);
        });
        return currentLocLiveData;
    }

    //getter for edit location data

    public MutableLiveData<EditLocationModel> getMyEditLocationModel(String locationDocId) {
        MutableLiveData<EditLocationModel> myEditLocationModel = new MutableLiveData<>();
        if (locationDocId == null || locationDocId.equals("")) {
            myEditLocationModel.postValue(null);
            return myEditLocationModel;
        }

        mLocationReference.whereEqualTo(DOCUMENT_ID, locationDocId).get().addOnSuccessListener(queryDocumentSnapshots -> {
            EditLocationModel model = null;
            if (queryDocumentSnapshots != null) {
                for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                    model = snapshot.toObject(EditLocationModel.class);
                }
            }
            myEditLocationModel.postValue(model);
        });
        return myEditLocationModel;
    }

    //getter for current user profile livedata

    public MutableLiveData<User> getCurrentUserProfileLiveData(String userId) {
        MutableLiveData<User> currentUserProfileLiveData = new MutableLiveData<>();
        if (userId == null || userId.equals("")) {
            currentUserProfileLiveData.postValue(null);
            return currentUserProfileLiveData;
        }

        mUserReference.document(userId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot != null) {
                currentUserProfileLiveData.postValue(documentSnapshot.toObject(User.class));
            }
        });
        return currentUserProfileLiveData;
    }

    public MutableLiveData<UserCompleteDetModel> getCurrentUserCompleteDetLive(String userId) {
        MutableLiveData<UserCompleteDetModel> currentUserComDet = new MutableLiveData<>();
        if (userId == null || userId.equals("")) {
            currentUserComDet.postValue(null);
            return currentUserComDet;
        }
        mUserReference.document(userId).addSnapshotListener((value, error) -> {
            if (value != null && value.exists()) {
                currentUserComDet.postValue(value.toObject(UserCompleteDetModel.class));
            } else {
                currentUserComDet.postValue(null);
            }
        });

        return currentUserComDet;
    }

    public MutableLiveData<UserCompleteDetModel> getUserComDetLiveData(String userId) {
        MutableLiveData<UserCompleteDetModel> userDetails = new MutableLiveData<>();

        if (userId == null || userId.equals("")) {
            userDetails.postValue(null);
            return userDetails;
        }

        mUserReference.document(userId).addSnapshotListener((value, error) -> {
            if (value != null && value.exists()) {
                userDetails.postValue(value.toObject(UserCompleteDetModel.class));
            }
        });
        return userDetails;
    }

    public MutableLiveData<List<ImagesModel>> getUserMediaImagesLiveData(String userId) {
        MutableLiveData<List<ImagesModel>> mediaImages = new MutableLiveData<>();
        if (userId == null || userId.equals("")) {
            mediaImages.postValue(null);
            return mediaImages;
        }
        mUserReference.document(userId).collection(PROFILE_IMAGES).addSnapshotListener((value, error) -> {
            List<ImagesModel> images = new ArrayList<>();
            if (value != null) {
                for (QueryDocumentSnapshot snapshot : value) {
                    images.add(snapshot.toObject(ImagesModel.class));
                }
            }
            mediaImages.postValue(images);
        });
        return mediaImages;
    }
    //getter for user profile images live data

    public MutableLiveData<List<ImagesModel>> getCurrentProfImages(String uId) {
        MutableLiveData<List<ImagesModel>> currentProfImages = new MutableLiveData<>();

        if (uId == null || uId.equals("")) {
            currentProfImages.postValue(null);
            return currentProfImages;
        }
        mUserReference.document(uId).collection(PROFILE_IMAGES).orderBy(STORAGE_PATH).addSnapshotListener((value, error) -> {
            List<ImagesModel> imagesModels = new ArrayList<>();
            if (value != null) {
                for (QueryDocumentSnapshot snapshot : value) {
                    imagesModels.add(snapshot.toObject(ImagesModel.class));
                }
            }
            currentProfImages.postValue(imagesModels);
        });
        return currentProfImages;
    }


    //getter for selected location owner details

    public MutableLiveData<UserBasicInfo> getSelectedLocationOwnerDetails(String userId) {
        MutableLiveData<UserBasicInfo> selectedLocationOwnerDetails = new MutableLiveData<>();
        if (userId == null || userId.equals("")) {
            selectedLocationOwnerDetails.postValue(null);
            return selectedLocationOwnerDetails;
        }

        mUserReference.document(userId).get().addOnCompleteListener(task -> {
            UserBasicInfo model = null;
            if (task.isSuccessful()) {
                model = task.getResult().toObject(UserBasicInfo.class);
            }
            selectedLocationOwnerDetails.postValue(model);
        });
        return selectedLocationOwnerDetails;
    }
    //getter for user details

    public MutableLiveData<UserBasicInfo> getUserInfo(String userId) {
        MutableLiveData<UserBasicInfo> userInfo = new MutableLiveData<>();

        if (userId == null || userId.equals("")) {
            userInfo.postValue(null);
            return userInfo;
        }
        mUserReference.document(userId).get().addOnCompleteListener(task -> {
            UserBasicInfo model = null;
            if (task.isSuccessful()) {
                model = task.getResult().toObject(UserBasicInfo.class);
            }
            userInfo.postValue(model);
        });
        return userInfo;
    }

    //getter for my location media images

    public MutableLiveData<List<ImagesModel>> getMyLocationMediaImagesData(String locationId) {
        MutableLiveData<List<ImagesModel>> myLocationMediaImages = new MutableLiveData<>();
        if (locationId == null || locationId.equals("")) {
            myLocationMediaImages.postValue(null);
            return myLocationMediaImages;
        }
        mLocationReference.document(locationId).collection(MEDIA_IMAGES)
                .limit(50)
                .addSnapshotListener((value, error) -> {
                    List<ImagesModel> models = new ArrayList<>();
                    if (value != null) {
                        for (QueryDocumentSnapshot snapshot : value) {
                            models.add(snapshot.toObject(ImagesModel.class));
                        }
                    }
                    myLocationMediaImages.postValue(models);
                });

        return myLocationMediaImages;
    }

    //method to get search results live data
    public MutableLiveData<List<SearchResultModel>> getSearchResultsLiveData(String queryKeyword) {
        MutableLiveData<List<SearchResultModel>> searchResultsLive = new MutableLiveData<>();
        if (queryKeyword == null || queryKeyword.equals("")) {
            searchResultsLive.postValue(null);
            return searchResultsLive;
        }
        Task<QuerySnapshot> searchLocForKeyword = mLocationReference.whereArrayContains("keywords",queryKeyword.trim()).limit(50).get();

        searchLocForKeyword.addOnCompleteListener(task -> {
            List<SearchResultModel> resultModels = new ArrayList<>();
            if (task.isSuccessful()){
                QuerySnapshot resultSnap = task.getResult();
                if (resultSnap!=null){
                    for (QueryDocumentSnapshot snapshot : resultSnap){
                        resultModels.add(snapshot.toObject(SearchResultModel.class));
                    }
                }

            }

           mUserReference.whereEqualTo(SEARCH_BY_PROFESSION_ENABLED,true).whereArrayContains("keywords",queryKeyword.trim()).limit(50).get().addOnCompleteListener(task1 -> {
               if (task1.isSuccessful()){
                   QuerySnapshot userResultSnap = task1.getResult();
                   if (userResultSnap!=null){
                       for (QueryDocumentSnapshot snapshot: userResultSnap){

                           SearchResultModel model = new SearchResultModel();
                           model.setDocumentId((String) snapshot.get(UID));
                           model.setCategory((String) snapshot.get(USER_PROFESSION));
                           model.setDesc((String) snapshot.get(USER_ABOUT));
                           model.setImageUrl((String) snapshot.get(USER_PROFILE_IMAGE_URL));
                           model.setDocumentType(2);
                           model.setTitle((String) snapshot.get(USER_NAME));
                           resultModels.add(model);
                       }
                   }
               }

               searchResultsLive.postValue(resultModels);

           });

        });

        return searchResultsLive;
    }

    private static final String APP_VERSION_COLLECTION ="AppVersion";
    private static final String LIVE_VERSION_DETAILS ="liveVersionDetails";
    public Task<DocumentSnapshot> getVersionDetails(){
        return mFirestore.collection(APP_VERSION_COLLECTION).document(LIVE_VERSION_DETAILS).get();
    }



    //query for near me locations
    public void searchNearMeForKeywordLive(String queryKeyword) {
        if (queryKeyword == null || queryKeyword.equals("")) {
            nearMeSearchResultsLive.postValue(null);
            return;
        }
        Task<QuerySnapshot> categoryTask = mLocationReference.whereEqualTo(LOCATION_SHARE_ENABLED, true).whereArrayContains("keywords",queryKeyword.trim()).get();
        categoryTask.addOnCompleteListener(task -> {
            List<SearchResultModel> searchResultModels = new ArrayList<>();
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (QueryDocumentSnapshot snapshot : querySnapshot) {
                        searchResultModels.add(snapshot.toObject(SearchResultModel.class));

                    }
                }
            }
            nearMeSearchResultsLive.postValue(searchResultModels);
        });
    }

    //getter for near me Search results live data
    public MutableLiveData<List<SearchResultModel>> getNearMeSearchResultsLive() {
        return nearMeSearchResultsLive;
    }

    //getter for current user locations live data
    public MutableLiveData<List<LocationModel>> getCurrentUserLocationsLiveData(String currentUserId) {
        MutableLiveData<List<LocationModel>> currentUserLocationsLiveData = new MutableLiveData<>();

        if (currentUserId == null || currentUserId.equals("")) {
            currentUserLocationsLiveData.postValue(null);
            return currentUserLocationsLiveData;
        }

        mLocationReference.whereEqualTo(OWNER_ID, currentUserId).addSnapshotListener((value, error) -> {
            List<LocationModel> currentUserLocList = new ArrayList<>();
            if (value != null) {
                for (QueryDocumentSnapshot snapshot : value) {
                    currentUserLocList.add(snapshot.toObject(LocationModel.class));
                }
            }
            currentUserLocationsLiveData.postValue(currentUserLocList);
        });
        return currentUserLocationsLiveData;
    }

    //getter for UsersListLiveData
    public MutableLiveData<List<User>> getUserListLiveData() {

        mUserReference.addSnapshotListener((value, error) -> {
            List<User> users = new ArrayList<>();
            if (value != null) {
                for (QueryDocumentSnapshot snapshots : value) {
                    if (snapshots != null) {
                        users.add(snapshots.toObject(User.class));

                    }
                }
            }
            userListLiveData.postValue(users);
        });
        return userListLiveData;
    }

    //getter for CityUpdatesLiveData
    private static final String CITY_UPDATES_PREFERENCE = "CityUpdatesPreference";
    private static final String CITY_UPDATES_LAST_MODIFIED = "cityUpdateLastModified";
    public MutableLiveData<List<CityUpdatesModel>> getCityUpdatesLiveData(Context context) {
        MutableLiveData<List<CityUpdatesModel>> cityUpdatesLiveData = new MutableLiveData<>();


        SharedPreferences settings = context.getSharedPreferences(CITY_UPDATES_PREFERENCE, 0);
        int cityUpdateLastModified = settings.getInt(CITY_UPDATES_LAST_MODIFIED, 0); //0 is the default value

        if (cityUpdateLastModified==0){
            mCityUpdatesReference.orderBy(POSITION, Query.Direction.ASCENDING)
                    .get(SERVER).addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            List<CityUpdatesModel> updatesList = new ArrayList<>();
                            QuerySnapshot result = task.getResult();
                            if (result!=null){
                                for (QueryDocumentSnapshot snapshot : result) {
                                    if (snapshot != null) {
                                        CityUpdatesModel model = snapshot.toObject(CityUpdatesModel.class);
                                        updatesList.add(model);
                                    }


                                }
                                cityUpdatesLiveData.postValue(updatesList);

                            }
                        }
                    });

            mCityUpdatesReference.orderBy(CITY_UPDATES_LAST_MODIFIED).get(SERVER).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    QuerySnapshot result = task.getResult();
                    if (result!=null){
                        int cityUpdateLastModified1 = 0;
                        for (QueryDocumentSnapshot snapshot: result){
                            cityUpdateLastModified1 = Objects.requireNonNull(snapshot.getLong(CITY_UPDATES_LAST_MODIFIED)).intValue();
                        }

                        SharedPreferences settings1 = context.getSharedPreferences(CITY_UPDATES_PREFERENCE, 0);
                        SharedPreferences.Editor editor = settings1.edit();
                        editor.putInt(CITY_UPDATES_LAST_MODIFIED, cityUpdateLastModified1);
                        editor.apply();

                    }
                }
            });
        }else {
            mCityUpdatesReference.orderBy(CITY_UPDATES_LAST_MODIFIED).get(SERVER).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    QuerySnapshot result = task.getResult();
                    if (result!=null){
                        int cityUpdateLastModifiedCurrent = 0;
                        for (QueryDocumentSnapshot snapshot: result){
                            cityUpdateLastModifiedCurrent = Objects.requireNonNull(snapshot.getLong(CITY_UPDATES_LAST_MODIFIED)).intValue();
                        }
                        Task<QuerySnapshot> queryCityUpdates;
                        if (cityUpdateLastModified!=cityUpdateLastModifiedCurrent){
                            queryCityUpdates = mCityUpdatesReference.orderBy(POSITION, Query.Direction.ASCENDING)
                                    .get(SERVER);
                        }else {
                            queryCityUpdates = mCityUpdatesReference.orderBy(POSITION, Query.Direction.ASCENDING)
                                    .get(CACHE);
                        }

                        queryCityUpdates.addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()){
                                List<CityUpdatesModel> updatesList = new ArrayList<>();
                                QuerySnapshot result1 = task1.getResult();
                                if (result1 !=null){
                                    for (QueryDocumentSnapshot snapshot : result1) {
                                        if (snapshot != null) {
                                            CityUpdatesModel model = snapshot.toObject(CityUpdatesModel.class);
                                            updatesList.add(model);
                                        }


                                    }
                                    cityUpdatesLiveData.postValue(updatesList);

                                }
                            }
                        });
                    }
                }

                else {
                    Task<QuerySnapshot> queryCityUpdates;

                    queryCityUpdates = mCityUpdatesReference.orderBy(POSITION, Query.Direction.ASCENDING)
                            .get(CACHE);

                    queryCityUpdates.addOnCompleteListener(task12 -> {
                        if (task12.isSuccessful()){
                            List<CityUpdatesModel> updatesList = new ArrayList<>();
                            QuerySnapshot result = task12.getResult();
                            if (result!=null){
                                for (QueryDocumentSnapshot snapshot : result) {
                                    if (snapshot != null) {
                                        CityUpdatesModel model = snapshot.toObject(CityUpdatesModel.class);
                                        updatesList.add(model);
                                    }


                                }
                                cityUpdatesLiveData.postValue(updatesList);

                            }
                        }
                    });
                }
            });

        }
        return cityUpdatesLiveData;
    }


    //getter for recentlyViewedLoc Livedata
    public MutableLiveData<List<RecentlyViewedLocation>> getRecentlyViewedLocLiveData(String currentUserId, int limit) {
        MutableLiveData<List<RecentlyViewedLocation>> recentlyViewedLocLiveData = new MutableLiveData<>();

        if (currentUserId == null || currentUserId.equals("")) {
            recentlyViewedLocLiveData.postValue(null);
            return recentlyViewedLocLiveData;
        }

        if (limit == 0) {
            mUserReference.document(currentUserId).collection(RECENTLY_VIEWED).orderBy(VIEW_TIMESTAMP, Query.Direction.DESCENDING).addSnapshotListener((value, error) -> {
                List<RecentlyViewedLocation> locations = new ArrayList<>();
                if (value != null) {
                    for (QueryDocumentSnapshot snapshot : value) {
                        if (snapshot.exists()) {
                            locations.add(snapshot.toObject(RecentlyViewedLocation.class));
                        }
                    }
                }
                recentlyViewedLocLiveData.postValue(locations);
            });
        } else {
            mUserReference.document(currentUserId).collection(RECENTLY_VIEWED).limit(limit).orderBy(VIEW_TIMESTAMP, Query.Direction.DESCENDING).addSnapshotListener((value, error) -> {
                List<RecentlyViewedLocation> locations = new ArrayList<>();
                if (value != null && !value.isEmpty()) {
                    for (QueryDocumentSnapshot snapshot : value) {
                        if (snapshot.exists()) {
                            locations.add(snapshot.toObject(RecentlyViewedLocation.class));
                        }
                    }
                }
                recentlyViewedLocLiveData.postValue(locations);
            });
        }

        return recentlyViewedLocLiveData;
    }

    public MutableLiveData<List<RecentlyViewedDetailedModel>> getRecentlyViewedDetailedLiveData(String currentUserId, List<RecentlyViewedLocation> recentList) {
        MutableLiveData<List<RecentlyViewedDetailedModel>> recentViewedDetail = new MutableLiveData<>();


        List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
        for (RecentlyViewedLocation recentItem : recentList) {


            if (recentItem.getDocumentType() == 1 && !recentItem.getDocumentId().equals("")) {
                Task<DocumentSnapshot> task = mLocationReference.document(recentItem.getDocumentId()).get();
                tasks.add(task);
            }
            if (recentItem.getDocumentType() == 2 && !recentItem.getDocumentId().equals("")) {
                Task<DocumentSnapshot> task = mUserReference.document(recentItem.getDocumentId()).get();
                tasks.add(task);
            }

        }
        Tasks.whenAllComplete(tasks).addOnCompleteListener(task -> {
            List<RecentlyViewedDetailedModel> recentDetailList = new ArrayList<>();
            List<Task<?>> resultTask = task.getResult();
            for (int i = 0; i < resultTask.size(); i++) {
                Task<?> taskComplete = resultTask.get(i);
                if (taskComplete.isSuccessful()) {
                    RecentlyViewedLocation item = recentList.get(i);
                    if (item.getDocumentType() == 1) {
                        DocumentSnapshot snapshot = (DocumentSnapshot) taskComplete.getResult();
                        if (snapshot != null && snapshot.exists()) {
                            RecentlyViewedDetailedModel model = new RecentlyViewedDetailedModel();
                            model.setTitle(snapshot.getString(TITLE));
                            model.setImageUrl(snapshot.getString(IMAGE_URL));
                            model.setDocumentId(snapshot.getString(DOCUMENT_ID));
                            model.setDocumentType(recentList.get(i).getDocumentType());
                            recentDetailList.add(model);
                        }

                    }
                    if (item.getDocumentType() == 2) {
                        DocumentSnapshot snapshot = (DocumentSnapshot) taskComplete.getResult();
                        if (snapshot != null && snapshot.exists()) {
                            RecentlyViewedDetailedModel model = new RecentlyViewedDetailedModel();
                            model.setTitle(snapshot.getString(USER_NAME));
                            model.setImageUrl(snapshot.getString(USER_PROFILE_IMAGE_URL));
                            model.setDocumentId(recentList.get(i).getDocumentId());
                            model.setDocumentType(recentList.get(i).getDocumentType());
                            recentDetailList.add(model);
                        }
                    }
                }

            }

            recentViewedDetail.postValue(recentDetailList);
        });


        return recentViewedDetail;
    }


    //getter for FeaturedLocLiveData
    public MutableLiveData<List<FeaturedLocation>> getFeaturedLocLiveData(int limit) {
        MutableLiveData<List<FeaturedLocation>> featuredPlacesIdsLive = new MutableLiveData<>();

            mLocationReference.whereEqualTo(FEATURED_PLACE,true).orderBy(POSITION, Query.Direction.ASCENDING).limit(limit).addSnapshotListener((value, error) -> {
                List<FeaturedLocation> featuredLocList = new ArrayList<>();
                if (value!=null){
                        for (QueryDocumentSnapshot snapshot : value) {
                            featuredLocList.add(snapshot.toObject(FeaturedLocation.class));
                        }

                }
                featuredPlacesIdsLive.postValue(featuredLocList);
                Log.i("featured", "getFeaturedLocLiveData: "+ featuredLocList);
            });

        return featuredPlacesIdsLive;
    }

    private static final String CATEGORIES_NAME_LIST = "categoryNameList";
    private static final String NAME_LIST = "nameList";

    public MutableLiveData<List<String>> getCategoriesNameList(){
        MutableLiveData<List<String>> categoriesList = new MutableLiveData<>();

        mNameRefCollection.document(NAME_LIST).get().addOnCompleteListener(task -> {
            List<String> categoriesNamesList = new ArrayList<>();
            if (task.isSuccessful()){
                DocumentSnapshot result = task.getResult();
                if (result.exists()){
                    categoriesNamesList = (List<String>) result.get(CATEGORIES_NAME_LIST);
                }

            }
            categoriesList.postValue(categoriesNamesList);

        });
        return categoriesList;
    }
    private static final String PROFESSION_NAME_LIST = "professionNameList";
    public MutableLiveData<List<String>> getProfessionNameList(){
        MutableLiveData<List<String>> professionList = new MutableLiveData<>();

        mNameRefCollection.document(NAME_LIST).get().addOnCompleteListener(task -> {
            List<String> professionNamesList = new ArrayList<>();
            if (task.isSuccessful()){

                DocumentSnapshot result = task.getResult();
                if (result.exists()){
                    professionNamesList = (List<String>) result.get(PROFESSION_NAME_LIST);
                }


            }
            professionList.postValue(professionNamesList);

        });
        return professionList;
    }

    private static final String PLACE_NAME_LIST = "placeNameList";
    public MutableLiveData<List<String>> getSearchKeywordsList(){
        MutableLiveData<List<String>> searchKeywords = new MutableLiveData<>();

        mNameRefCollection.document(NAME_LIST).get().addOnCompleteListener(task -> {
            List<String> searchKeywordsList = new ArrayList<>();
            if (task.isSuccessful()){

                DocumentSnapshot result = task.getResult();
                if (result.exists()){
                    List<String> categories = (List<String>) result.get(CATEGORIES_NAME_LIST);
                    List<String> placeNameList = (List<String>) result.get(PLACE_NAME_LIST);
                    List<String> professionNameList = (List<String>) result.get(PROFESSION_NAME_LIST);

                    if (categories != null) {
                        searchKeywordsList.addAll(categories);
                    }

                    if (placeNameList != null) {
                        searchKeywordsList.addAll(placeNameList);
                    } if (professionNameList != null) {
                        searchKeywordsList.addAll(professionNameList);
                    }
                }


            }
            searchKeywords.postValue(searchKeywordsList);

        });
        return searchKeywords;
    }

    public MutableLiveData<List<String>> getNearSearchKeywordsList(){
        MutableLiveData<List<String>> nearMeSearchKeywords = new MutableLiveData<>();

        mNameRefCollection.document(NAME_LIST).get().addOnCompleteListener(task -> {
            List<String> searchKeywordsList = new ArrayList<>();
            if (task.isSuccessful()){

                DocumentSnapshot result = task.getResult();
                if (result.exists()){
                    List<String> categories = (List<String>) result.get(CATEGORIES_NAME_LIST);
                    List<String> placeNameList = (List<String>) result.get(PLACE_NAME_LIST);

                    if (categories != null) {
                        searchKeywordsList.addAll(categories);
                    }

                    if (placeNameList != null) {
                        searchKeywordsList.addAll(placeNameList);
                    }
                }


            }
            nearMeSearchKeywords.postValue(searchKeywordsList);

        });
        return nearMeSearchKeywords;
    }

    //getter user profile icon
    public MutableLiveData<UserBasicInfo> getUserProfileData(String userId) {
        MutableLiveData<UserBasicInfo> userBasicInfo = new MutableLiveData<>();
        if (userId == null || userId.equals("")) {
            userBasicInfo.postValue(null);
            return userBasicInfo;
        }
        mUserReference.document(userId).addSnapshotListener((value, error) -> {
            if (value != null && value.exists()) {
                userBasicInfo.postValue(value.toObject(UserBasicInfo.class));
            } else {
                userBasicInfo.postValue(null);
            }

        });
        return userBasicInfo;

    }

    //getter for MostSearchedLocLiveData
    public MutableLiveData<List<MostSearchedLocation>> getMostSearchedLocLiveData() {
        MutableLiveData<List<MostSearchedLocation>> mostSearchedLocLiveData = new MutableLiveData<>();


        mLocationReference.orderBy(SEARCH_COUNT, Query.Direction.DESCENDING).limit(15).addSnapshotListener((value, error) -> {
            List<MostSearchedLocation> mostSearchedLocations = new ArrayList<>();
            if (value != null) {
                for (QueryDocumentSnapshot snapshot : value) {
                    if (snapshot != null && snapshot.exists()) {
                        mostSearchedLocations.add(snapshot.toObject(MostSearchedLocation.class));
                    }
                }
            }
            mostSearchedLocLiveData.postValue(mostSearchedLocations);
        });


        return mostSearchedLocLiveData;
    }


    //getter for bestRatedLoc Livedata
    public MutableLiveData<List<BestRatedLocation>> getBestRatedLocLiveData(int limit) {
        MutableLiveData<List<BestRatedLocation>> bestRatedLocLiveData = new MutableLiveData<>();


        if (limit == 0) {
            mLocationReference.whereGreaterThan(OVERALL_RATING, 4.0).orderBy(OVERALL_RATING, Query.Direction.DESCENDING).addSnapshotListener((value, error) -> {
                List<BestRatedLocation> bestRatedLocations = new ArrayList<>();
                if (value != null) {
                    for (QueryDocumentSnapshot snapshot : value) {
                        if (snapshot != null && snapshot.exists()) {
                            bestRatedLocations.add(snapshot.toObject(BestRatedLocation.class));
                        }
                    }
                }
                bestRatedLocLiveData.postValue(bestRatedLocations);
            });
        } else {
            mLocationReference.whereGreaterThan(OVERALL_RATING, 4.0).orderBy(OVERALL_RATING, Query.Direction.DESCENDING).limit(limit).addSnapshotListener((value, error) -> {
                List<BestRatedLocation> bestRatedlocations = new ArrayList<>();
                if (value != null) {
                    for (QueryDocumentSnapshot snapshot : value) {
                        if (snapshot != null && snapshot.exists()) {
                            bestRatedlocations.add(snapshot.toObject(BestRatedLocation.class));
                        }
                    }
                }
                bestRatedLocLiveData.postValue(bestRatedlocations);
            });
        }

        return bestRatedLocLiveData;
    }

    //getter for city professional live data

    public MutableLiveData<List<CityProfessionalsModel>> getCityProfessionalsLiveData(int limit) {
        MutableLiveData<List<CityProfessionalsModel>> cityProfessionalsLiveData = new MutableLiveData<>();


        if (limit == 0) {
            mUserReference.whereEqualTo(SEARCH_BY_PROFESSION_ENABLED, true).orderBy(USER_RATING, Query.Direction.DESCENDING)
                    .addSnapshotListener((value, error) -> {
                        List<CityProfessionalsModel> professionals = new ArrayList<>();
                        if (value != null) {
                            for (QueryDocumentSnapshot snapshot : value) {
                                professionals.add(snapshot.toObject(CityProfessionalsModel.class));
                            }
                        }
                        cityProfessionalsLiveData.postValue(professionals);
                    });
        } else {
            mUserReference.whereEqualTo(SEARCH_BY_PROFESSION_ENABLED, true).orderBy(USER_RATING, Query.Direction.DESCENDING)
                    .limit(limit).addSnapshotListener((value, error) -> {
                List<CityProfessionalsModel> professionals = new ArrayList<>();
                if (value != null) {
                    for (QueryDocumentSnapshot snapshot : value) {
                        professionals.add(snapshot.toObject(CityProfessionalsModel.class));
                    }
                }
                cityProfessionalsLiveData.postValue(professionals);
            });
        }

        return cityProfessionalsLiveData;
    }

    //getter for selected Location Reviews Live Data


    public MutableLiveData<List<ReviewModel>> getSelectedLocReviewLiveData(String placeId) {
        MutableLiveData<List<ReviewModel>> selectedLocReviewLiveData = new MutableLiveData<>();
        if (placeId == null || placeId.equals("")) {
            selectedLocReviewLiveData.postValue(null);
            return selectedLocReviewLiveData;
        }

        mReviewsAndRatingsReference.document(placeId).collection(PLACES_REVIEWS_ID).orderBy(UPLOAD_TIME_STAMP, Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    List<ReviewModel> models = new ArrayList<>();
                    if (value != null) {
                        for (QueryDocumentSnapshot snapshot : value) {
                            models.add(snapshot.toObject(ReviewModel.class));
                        }
                    }
                    selectedLocReviewLiveData.postValue(models);
                });
        return selectedLocReviewLiveData;
    }

    //getter for selected User Reviews Live Data
    public MutableLiveData<List<UserReviewModel>> getSelectedUserReviewLiveData(String uId) {
        MutableLiveData<List<UserReviewModel>> selectedUserReviewLiveData = new MutableLiveData<>();
        if (uId == null || uId.equals("")) {
            selectedUserReviewLiveData.postValue(null);
            return selectedUserReviewLiveData;
        }

        mUserReviewsAndRatingsReference.document(uId).collection(USER_REVIEWS_ID).orderBy(REVIEW_TIME_STAMP, Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    List<UserReviewModel> models = new ArrayList<>();
                    if (value != null) {
                        for (QueryDocumentSnapshot snapshot : value) {
                            models.add(snapshot.toObject(UserReviewModel.class));
                        }
                    }
                    selectedUserReviewLiveData.postValue(models);
                });
        return selectedUserReviewLiveData;
    }

    //method to add Location to fireStore database
    public Task<DocumentReference> addLocation(LocationModel locationModel) {
        if (locationModel != null)
            return mLocationReference.add(locationModel);

        else return null;
    }

    //method to update location Data to firestore database
    public Task<Void> updateLocationData(EditLocationModel editLocationModel) {
        if (editLocationModel != null)
            return mLocationReference.document(editLocationModel.getDocumentId()).set(editLocationModel, SetOptions.merge());

        else
            return null;
    }

    public void addKeywordsToPlaceDoc(String locId,ArrayList<String> keywords){
        if (locId==null || locId.equals("")){
            return;
        }
        Map<String,Object> keywordsMap = new HashMap<>();
        keywordsMap.put("keywords",keywords);
        mLocationReference.document(locId).set(keywordsMap,SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.i("keywords", "onComplete: ");
                }else {
                    Log.i("keywords", "onComplete: "+ task.getException());
                }
            }
        });
    }
    public Task<DocumentReference> updateUserAccountMediaImages(String userId, ImagesModel model) {
        if (userId == null || userId.equals("")) {
            return null;
        }
        return mUserReference.document(userId).collection(PROFILE_IMAGES).add(model);
    }

    public Task<DocumentReference> updateLocationMediaImages(String locationId, ImagesModel model) {
        if (locationId == null || locationId.equals("")) {
            return null;
        }
        return mLocationReference.document(locationId).collection(MEDIA_IMAGES).add(model);
    }

    //method to add Location Review to Firestore Database
    public void addLocationReview(ReviewModel reviewModel) {
        if (reviewModel == null) {
            return;
        }
        String placeId = reviewModel.getPlaceId();
        String userId = reviewModel.getUserId();
        Map<String, Object> dummyData = new HashMap<>();
        dummyData.put(LATEST_REVIEW, reviewModel.getReview());
        mReviewsAndRatingsReference.document(placeId).set(dummyData, SetOptions.merge());
        Task<Void> reviewTask = mReviewsAndRatingsReference.document(placeId)
                .collection(PLACES_REVIEWS_ID).document(userId).set(reviewModel);

        reviewTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                updateRatingCountInPlaceRef(placeId);
            }
        });
    }

    //method to add User Review to Firestore Database
    public void addUserReview(UserReviewModel reviewModel) {
        if (reviewModel == null) {
            return;
        }
        String uId = reviewModel.getuId();
        String userId = reviewModel.getUserId();
        Map<String, Object> dummyData = new HashMap<>();
        dummyData.put(LATEST_REVIEW, reviewModel.getReview());
        mUserReviewsAndRatingsReference.document(uId).set(dummyData, SetOptions.merge());
        Task<Void> reviewTask = mUserReviewsAndRatingsReference.document(uId)
                .collection(PLACES_REVIEWS_ID).document(userId).set(reviewModel);

        reviewTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                updateRatingCountInUserRef(uId);
            }
        });
    }

    //method to delte location review from firestore database
    public void deleteLocaitonReview(String placeId, String userId) {
        if (placeId == null || placeId.equals("") || userId == null || userId.equals("")) {
            return;
        }
        mReviewsAndRatingsReference.document(placeId).collection(PLACES_REVIEWS_ID).document(userId).delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                updateRatingCountInPlaceRef(placeId);
            }
        });

    }//method to delete user review from firestore database

    public void deleteUserReview(String uId, String userId) {
        if (uId == null || uId.equals("") || userId == null || userId.equals("")) {
            return;
        }
        mUserReviewsAndRatingsReference.document(uId).collection(USER_REVIEWS_ID).document(userId).delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                updateRatingCountInUserRef(uId);
            }
        });

    }

    public LiveData<ReviewModel> getSelectedLocCurrentUserReview(String placeId, String userId) {
        MutableLiveData<ReviewModel> selectedLocCurrentUserReview = new MutableLiveData<>(null);
        if (placeId == null || placeId.equals("") || userId == null || userId.equals("")) {
            selectedLocCurrentUserReview.postValue(null);
            return selectedLocCurrentUserReview;
        }
        mReviewsAndRatingsReference.document(placeId).collection(PLACES_REVIEWS_ID).document(userId).addSnapshotListener((value, error) -> {
            if (value != null) {
                if (value.exists()) {
                    selectedLocCurrentUserReview.postValue(value.toObject(ReviewModel.class));
                } else {
                    selectedLocCurrentUserReview.postValue(null);
                }
            }
        });

        return selectedLocCurrentUserReview;
    }

    public LiveData<UserReviewModel> getSelectedUserCurrentUserReview(String uId, String userId) {
        MutableLiveData<UserReviewModel> selectedLocCurrentUserReview = new MutableLiveData<>(null);
        if (uId == null || uId.equals("") || userId == null || userId.equals("")) {
            selectedLocCurrentUserReview.postValue(null);
            return selectedLocCurrentUserReview;

        }
        mUserReviewsAndRatingsReference.document(uId).collection(USER_REVIEWS_ID).document(userId).addSnapshotListener((value, error) -> {
            if (value != null) {
                if (value.exists()) {
                    selectedLocCurrentUserReview.postValue(value.toObject(UserReviewModel.class));
                } else {
                    selectedLocCurrentUserReview.postValue(null);
                }
            }
        });

        return selectedLocCurrentUserReview;
    }

    private void updateRatingCountInPlaceRef(String placeId) {
        if (placeId == null || placeId.equals("")) {
            return;
        }
        mReviewsAndRatingsReference.document(placeId).collection(PLACES_REVIEWS_ID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int ratingListSize = 0;
                double ratingSum = 0.0;
                QuerySnapshot resultSnap = task.getResult();
                if (resultSnap != null) {
                    ratingListSize = resultSnap.size();
                    for (QueryDocumentSnapshot snapshot : resultSnap) {
                        ratingSum = ratingSum + snapshot.getDouble(RATING);
                    }
                }


                double finalRating = 0.0;
                if (ratingListSize != 0) {
                    finalRating = ratingSum / ratingListSize;
                }

                String output = String.format("%.1f", finalRating);
                Map<String, Object> ratingMap = new HashMap<>();
                ratingMap.put(OVERALL_RATING, Double.valueOf(output));
                mLocationReference.document(placeId).set(ratingMap, SetOptions.merge());

            }
        });
    }

    private void updateRatingCountInUserRef(String uId) {
        if (uId == null || uId.equals("")) {
            return;
        }
        mUserReviewsAndRatingsReference.document(uId).collection(USER_REVIEWS_ID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int ratingListSize = 0;
                double ratingSum = 0.0;
                QuerySnapshot resultSnap = task.getResult();
                if (resultSnap != null) {
                    ratingListSize = resultSnap.size();
                    for (QueryDocumentSnapshot snapshot : resultSnap) {
                        ratingSum = ratingSum + snapshot.getDouble(RATING);
                    }
                }


                double finalRating = 0.0;
                if (ratingListSize != 0) {
                    finalRating = ratingSum / ratingListSize;
                }
                String output = String.format("%.1f", finalRating);
                Map<String, Object> ratingMap = new HashMap<>();
                ratingMap.put(USER_RATING, Double.valueOf(output));
                mUserReference.document(uId).set(ratingMap, SetOptions.merge());

            }
        });
    }


    //method to update user profile Data
    public Task<Void> updateUserProfileData(UserUpdateModel userUpdateModel) {
        if (userUpdateModel != null)
            return mUserReference.document(userUpdateModel.getuId()).set(userUpdateModel, SetOptions.merge());

        else return null;
    }

    public void updateUserProfileKeywords(String userId,ArrayList<String> keywords){
        if (userId==null || userId.equals("")){
            return;
        }
        Map<String,Object> keywordsMap = new HashMap<>();
        keywordsMap.put("keywords",keywords);
        mUserReference.document(userId).set(keywordsMap,SetOptions.merge());
    }


    //method to increase Search Count of a place in Firestore Database
    public void placesSearchCountIncrement(String placeId, String userId) {
        if (placeId == null || placeId.equals("") || userId == null || userId.equals("")) {
            return;
        }
        Timestamp currentTimeStamp = Timestamp.now();
        Map<String, Object> map = new HashMap<>();
        map.put(USER_ID, userId);
        map.put(SEARCH_TIMESTAMP, Timestamp.now());
        Task<DocumentSnapshot> searchCountTask = mPlacesSearchCountReference.document(placeId).collection("Users").document(userId).get();
        searchCountTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot snapshot = task.getResult();
                if (!snapshot.exists()) {
                    updateSearchCountData(placeId, userId, map);
                } else {
                    Timestamp dbTimestamp = snapshot.getTimestamp(SEARCH_TIMESTAMP);
                    if (dbTimestamp != null) {
                        String currentDateString = getDate(currentTimeStamp.getSeconds());
                        String dbDateString = getDate(dbTimestamp.getSeconds());
                        if (!currentDateString.equals(dbDateString)) {
                            updateSearchCountData(placeId, userId, map);
                        }
                    }

                }
            }
        });
    }

    private String getDate(long time) {
        Date date = new Date(time * 1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy "); // the format of your date
        sdf.setTimeZone(TimeZone.getDefault());

        return sdf.format(date);
    }

    private void updateSearchCountData(String placeId, String userId, Map<String, Object> map) {
        if (placeId == null || placeId.equals("") || userId == null || userId.equals("")) {
            return;
        }
        mPlacesSearchCountReference.document(placeId).collection(USERS).document(userId).set(map).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                mLocationReference.document(placeId).update(SEARCH_COUNT, FieldValue.increment(1));
            }
        });

    }

    public void addToSavedLocation(String placeId, String userId) {
        if (placeId == null || placeId.equals("") || userId == null || userId.equals("")) {
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put(SAVED_PLACES, FieldValue.arrayUnion(placeId));
        mUserSavedLocationsReference.document(userId).update(map);
    }

    public void removeFromSavedLocation(String placeId, String userID) {
        if (placeId == null || placeId.equals("") || userID == null || userID.equals("")) {
            return;
        }
        mUserSavedLocationsReference.document(userID).update(SAVED_PLACES,FieldValue.arrayRemove(placeId));

    }

    public MutableLiveData<Boolean> checkLocationSaved(String placeId, String userId) {

        MutableLiveData<Boolean> locationSavedInUserProf = new MutableLiveData<>(false);
        if (placeId == null || placeId.equals("") || userId == null || userId.equals("")) {
            locationSavedInUserProf.postValue(false);
            return locationSavedInUserProf;
        }
        mUserSavedLocationsReference.document(userId).addSnapshotListener((value, error) -> {
            if (value!=null){
                List<String> savedPlaces = (List<String>) value.get(SAVED_PLACES);
                if (savedPlaces != null) {
                    if (savedPlaces.contains(placeId)){
                        locationSavedInUserProf.postValue(true);
                    }else {
                        locationSavedInUserProf.postValue(false);
                    }
                }
            }
        });
        return locationSavedInUserProf;

    }

    private static final String SAVED_PLACES = "savedPlaces";
    public MutableLiveData<List<String>> getSavedPlacesIdList(String userId) {
        MutableLiveData<List<String>> userSavedLocations = new MutableLiveData<>();
        if (userId == null || userId.equals("")) {
            userSavedLocations.postValue(null);
            return userSavedLocations;
        }


        mUserSavedLocationsReference.document(userId).addSnapshotListener((value, error) -> {
            List<String> userSaved = new ArrayList<>();
            if (value != null) {
                userSaved = (List<String>) value.get(SAVED_PLACES);
            }
            userSavedLocations.postValue(userSaved);
        });



        return userSavedLocations;
    }

    public MutableLiveData<List<PlacesBasicModel>> getSavedLocationsDetails(List<String> places) {
        MutableLiveData<List<PlacesBasicModel>> placesList = new MutableLiveData<>();
        if (places == null || places.isEmpty()) {
            placesList.postValue(null);
            return placesList;
        }
        List<Task<DocumentSnapshot>> taskList = new ArrayList<>();
        for (int i = 0; i < places.size(); i++) {
            Task<DocumentSnapshot> task = mLocationReference.document(places.get(i)).get();
            taskList.add(task);
        }
        Tasks.whenAllComplete(taskList).addOnCompleteListener(task -> {
            List<PlacesBasicModel> places1 = new ArrayList<>();
            List<Task<?>> snapTask = task.getResult();
            for (Task<?> checkTask : snapTask) {
                if (checkTask.isSuccessful()) {
                    DocumentSnapshot snapshot = (DocumentSnapshot) checkTask.getResult();
                    if (snapshot.exists()) {
                        places1.add(snapshot.toObject(PlacesBasicModel.class));

                    }
                }
            }
            placesList.postValue(places1);
        });

        return placesList;
    }

    private static final String REPORTS_CAT = "reportsCat";
    private static final String PLACE_REPORT_CATEGORY = "placeReportCategory";
    private static final String PROFESSION_REPORT_CATEGORY = "professionReportsCategory";
    public MutableLiveData<List<String>> getReportsCategory(int documentType) {
        MutableLiveData<List<String>> reportsCategoryLive = new MutableLiveData<>();
        mFirestore.collection(REPORTS_CATEGORY).document(REPORTS_CAT).get().addOnCompleteListener(task -> {
            List<String> reportsCategories = new ArrayList<>();
            if (task.isSuccessful()){
                DocumentSnapshot snapshot = task.getResult();
                if (snapshot!=null && snapshot.exists()){
                    if (documentType==1){
                        reportsCategories = (List<String>) snapshot.get(PLACE_REPORT_CATEGORY);
                    }
                    if (documentType==2){
                        reportsCategories = (List<String>) snapshot.get(PROFESSION_REPORT_CATEGORY);

                    }
                }
            }
            reportsCategoryLive.postValue(reportsCategories);
        });


        return reportsCategoryLive;
    }

    public MutableLiveData<Integer> addToReports(ReportModel reportModel) {
        MutableLiveData<Integer> reportUpdate = new MutableLiveData<>();
        if (reportModel == null) {
            return null;
        }
        mPlacesReportsReference.add(reportModel).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                reportUpdate.postValue(REPORT_RESULT_SUCCESS);
            } else {
                reportUpdate.postValue(REPORT_RESULT_FAILED);
            }
        });
        return reportUpdate;
    }

    public void addToRecentlyViewed(RecentlyViewedLocation location, String userId) {
        if (userId == null || userId.equals("")) {
            return;
        }
        String locationId = location.getDocumentId();
        mUserReference.document(userId).collection(RECENTLY_VIEWED).document(locationId).set(location);
    }

    private static final String RECENT_SEARCH_KEYWORDS = "recentSearchKeywords";
    private static final String RECENT_SEARCH_DOC = "recentSearchDoc";


    public void addToRecentSearches(String searchKeyword, String userId) {
        if (searchKeyword == null || searchKeyword.equals("") || userId == null || userId.equals("")) {
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put(RECENT_SEARCH_KEYWORDS, FieldValue.arrayUnion(searchKeyword));

        Map<String, Object> mapRemoveKey = new HashMap<>();
        mapRemoveKey.put(RECENT_SEARCH_KEYWORDS, FieldValue.arrayRemove(searchKeyword));

        mUserReference.document(userId).collection(RECENT_SEARCHES).document(RECENT_SEARCH_DOC).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                DocumentSnapshot snapshot= task.getResult();
                if (snapshot!=null && snapshot.exists()){
                    List<String> recentSearches = (List<String>) snapshot.get(RECENT_SEARCH_KEYWORDS);

                    if (recentSearches != null && recentSearches.contains(searchKeyword)) {
                        snapshot.getReference().update(mapRemoveKey);
                    }

                    snapshot.getReference().update(map);
                }else {
                    if (snapshot != null) {
                        snapshot.getReference().set(map);
                    }
                }

            }
        });

    }

    public void removeFromRecentSearches(String searchKeyword, String userId) {
        if (userId == null || userId.equals("")) {
            return;
        }
        Map<String, Object> mapRemoveKey = new HashMap<>();
        mapRemoveKey.put(RECENT_SEARCH_KEYWORDS, FieldValue.arrayRemove(searchKeyword));

        mUserReference.document(userId).collection(RECENT_SEARCHES).document(RECENT_SEARCH_DOC).update(mapRemoveKey);
    }


    public MutableLiveData<List<String>> getRecentSearchesLive(String userId) {
        MutableLiveData<List<String>> searches = new MutableLiveData<>();
        if (userId == null || userId.equals("")) {
            searches.postValue(null);
            return searches;
        }
        mUserReference.document(userId).collection(RECENT_SEARCHES).document(RECENT_SEARCH_DOC).addSnapshotListener((value, error) -> {
            List<String> strings = new ArrayList<>();
            if (value != null) {
                strings = (List<String>) value.get(RECENT_SEARCH_KEYWORDS);
            }
            searches.postValue(strings);
        });

        return searches;
    }

    public Task<Void> deleteProfileImageRecord(String userId, String imageDocumentId) {
        if (userId == null || userId.equals("") || imageDocumentId == null || imageDocumentId.equals("")) {
            return null;
        }
        return mUserReference.document(userId).collection(PROFILE_IMAGES).document(imageDocumentId).delete();
    }

    public Task<Void> deletePlaceImageRecord(String placeId, String imageDocumentId) {
        if (placeId == null || placeId.equals("") || imageDocumentId == null || imageDocumentId.equals("")) {
            return null;
        }
        return mLocationReference.document(placeId).collection(MEDIA_IMAGES).document(imageDocumentId).delete();
    }

    public Task<Void> deletePlaceRecord(String placeId) {
        if (placeId == null || placeId.equals("")) {
            return null;
        }
        return mLocationReference.document(placeId).delete();
    }

    public void addToDeletedPlaceRecord(String placeId) {
        if (placeId == null || placeId.equals("")) {
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put(PLACE_ID, placeId);
        map.put(DELETE_TIME_STAMP, Timestamp.now());

        mFirestore.collection(DELETED_PLACE_RECORD).add(map);
    }

    public void likeCityUpdate(String cityUpdateId, String userId) {
        if (cityUpdateId.equals("") || userId.equals("")) {
            return;
        }
        Map<String, Object> likeMap = new HashMap<>();
        likeMap.put(USER_ID, userId);
        mCityUpdatesReference.document(cityUpdateId).collection(LIKES).document(userId).set(likeMap);
    }

    public void unLikeCityUpdate(String cityUpdateId, String userId) {
        if (cityUpdateId.equals("") || userId.equals("")) {
            return;
        }
        mCityUpdatesReference.document(cityUpdateId).collection(LIKES).document(userId).delete();
    }

    public MutableLiveData<Boolean> checkUserLike(String cityUpdateId, String userId) {
        MutableLiveData<Boolean> uesrLike = new MutableLiveData<>();
        if (cityUpdateId == null || cityUpdateId.equals("") || userId == null || userId.equals("")) {
            uesrLike.postValue(false);
            return uesrLike;
        }

        mCityUpdatesReference.document(cityUpdateId).collection(LIKES).addSnapshotListener((value, error) -> {
            if (value != null) {
                if (!value.isEmpty()) {
                    for (QueryDocumentSnapshot snapshot : value) {
                        if (snapshot.getReference().getId().equals(userId)) {
                            uesrLike.postValue(true);
                        }
                    }
                } else {
                    uesrLike.postValue(false);
                }

            } else {
                uesrLike.postValue(false);

            }
        });

        return uesrLike;
    }

    public MutableLiveData<Integer> getCityUpdateLikesCount(String cityUpdateId) {
        MutableLiveData<Integer> cityUpdateLikesCount = new MutableLiveData<>();

        if (cityUpdateId.equals("")) {
            cityUpdateLikesCount.postValue(0);
            return cityUpdateLikesCount;
        }

        mCityUpdatesReference.document(cityUpdateId).collection(LIKES).addSnapshotListener((value, error) -> {
            if (value != null) {

                int size = value.size();
                cityUpdateLikesCount.postValue(size);
            } else {
                cityUpdateLikesCount.postValue(0);

            }

        });

        return cityUpdateLikesCount;
    }

    public MutableLiveData<CityUpdatesModel> getCityUpdateDetail(String cityUpdateId) {
        MutableLiveData<CityUpdatesModel> cityUpdateLive = new MutableLiveData<>();
        if (cityUpdateId.equals("")) {
            cityUpdateLive.postValue(null);
            return cityUpdateLive;
        }
        mCityUpdatesReference.document(cityUpdateId).addSnapshotListener((value, error) -> {
            if (value != null && value.exists()) {
                cityUpdateLive.postValue(value.toObject(CityUpdatesModel.class));
            } else {
                cityUpdateLive.postValue(null);
            }
        });
        return cityUpdateLive;
    }

    public MutableLiveData<List<String>> cityUpdatesImagesList(String cityUpdateId) {
        MutableLiveData<List<String>> cityUpdateImagesList = new MutableLiveData<>();
        if (cityUpdateId.equals("")) {
            cityUpdateImagesList.postValue(null);
            return cityUpdateImagesList;
        }

        mCityUpdatesReference.document(cityUpdateId).collection(MEDIA).orderBy(POSITION).addSnapshotListener((value, error) -> {
            if (value != null) {
                List<String> imagesList = new ArrayList<>();
                if (!value.isEmpty()) {

                    for (QueryDocumentSnapshot snapshot : value) {
                        if (snapshot.exists()) {
                            imagesList.add(snapshot.getString(IMAGE_URL));
                        }
                    }

                }
                cityUpdateImagesList.postValue(imagesList);
            }
        });
        return cityUpdateImagesList;
    }

    public MutableLiveData<Boolean> checkPhoneNumberExists(String phoneNo) {
        MutableLiveData<Boolean> phoneNumberExists = new MutableLiveData<>();
        mPhoneNumberListReferece.whereEqualTo(USER_PHONE, phoneNo).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    for (QueryDocumentSnapshot snapshot : querySnapshot) {

                        if (snapshot != null && snapshot.exists()) {

                            phoneNumberExists.postValue(true);
                        } else {
                            phoneNumberExists.postValue(false);
                        }
                    }
                } else {

                    phoneNumberExists.postValue(false);
                }

            } else {

                phoneNumberExists.postValue(false);
            }

        });

        return phoneNumberExists;
    }

    public Task<Void> updateUserPasswordInFirestore(String userId, String newPassWord) {
        if (userId == null || userId.equals("") || newPassWord == null || newPassWord.equals("")) {
            return null;
        }
        Map<String, Object> passUpdate = new HashMap<>();
        passUpdate.put(USER_PASSWORD, newPassWord);
        return mUserReference.document(userId).update(passUpdate);
    }

    public Task<Void> uploadUser(User userData) {
        if (userData == null) {
            return null;
        }
        String documentId = userData.getuId();
        return mUserReference.document(documentId).set(userData);
    }

    public void addPhoneNumberList(String phoneNo) {
        if (phoneNo == null || phoneNo.equals("")) {
            return;
        }
        Map<String, Object> phoneData = new HashMap<>();
        phoneData.put(USER_PHONE, phoneNo);
        phoneData.put(TIME_STAMP, Timestamp.now());
        mPhoneNumberListReferece.add(phoneData);
    }


    public void reportPlaceReview(String placeId, String reviewUserId) {
        mPlaceReviewReportsReference.document(placeId).collection(REPORTS).document(reviewUserId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot snapshot = task.getResult();
                        if (snapshot.exists()) {
                            snapshot.getReference().update(REPORT_COUNT, FieldValue.increment(1));

                        } else {
                            Map<String, Object> reportDummy = new HashMap<>();
                            reportDummy.put(REPORTED, true);
                            mPlaceReviewReportsReference.document(placeId).set(reportDummy);

                            Map<String, Object> reportReview = new HashMap<>();
                            reportReview.put(REVIEW_USER_ID, reviewUserId);
                            reportReview.put(REPORT_COUNT, 1);
                            mPlaceReviewReportsReference.document(placeId).collection(REPORTS).document(reviewUserId).set(reportReview);
                        }
                    }
                });

    }

    public void reportUserReview(String userId, String reviewUserId) {

        mUserReviewReportsReference.document(userId).collection(REPORTS).document(reviewUserId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot snapshot = task.getResult();
                        if (snapshot.exists()) {
                            snapshot.getReference().update(REPORT_COUNT, FieldValue.increment(1));
                        } else {
                            Map<String, Object> reportDummy = new HashMap<>();
                            reportDummy.put(REPORTED, true);
                            mUserReviewReportsReference.document(userId).set(reportDummy);

                            Map<String, Object> reportReview = new HashMap<>();
                            reportReview.put(REVIEW_USER_ID, reviewUserId);
                            reportReview.put(REPORT_COUNT, 1);
                            mUserReviewReportsReference.document(userId).collection(REPORTS).document(reviewUserId).set(reportReview);
                        }
                    }
                });


    }
}
