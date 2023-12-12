package com.aelzohry.topsaleqatar.repository.remote

import com.aelzohry.topsaleqatar.model.Ad
import com.aelzohry.topsaleqatar.model.LocalStanderModel
import com.aelzohry.topsaleqatar.model.StanderModel1
import com.aelzohry.topsaleqatar.repository.remote.requests.*
import com.aelzohry.topsaleqatar.repository.remote.responses.*
import com.aelzohry.topsaleqatar.utils.base.baseModel.BaseListModel
import com.aelzohry.topsaleqatar.utils.base.baseModel.BaseObjectModel
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


interface WebService {
    @GET("home")
    fun getHome(): Call<HomeResponse>

    @GET("top-sliders")
    fun getTopSliders(): Call<BannerSliderResponse>


    @GET("getNumberOfNotificationsNotSeen")
    fun getNumberOfNotificationsNotSeen(): Call<UnSeenNotificationsResponse>

    @POST("login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("profile")
    fun getProfile(): Call<ProfileResponse>

    @Multipart
    @PUT("profile/update")
    fun updateProfile(@Part parts: List<MultipartBody.Part>): Call<EditProfileResponse>

    @GET("categories")
    fun getCategories(): Call<CategoriesResponse>

    @GET("getCategoriesGroupd")
    fun getCategoriesGroupd(): Call<CategoriesGroupd>

    @GET("regions")
    fun getRegions(): Call<RegionsResponse>

    @GET("regions")
    fun getRegion(): Call<BaseListModel<LocalStanderModel>>

    @GET("cities")
    fun getCities(): Call<RegionsResponse>

//    @GET("car-makes")
//    fun getCarMakes(): Call<CarMakesResponse>

    @GET("car-makes")
    fun getCarMake(): Call<BaseListModel<StanderModel1>>

//    @GET("car-makes/{id}/car-models")
//    fun getCarModels(@Path("id") carMakeId: String): Call<CarModelsResponse>

    @GET("car-makes/{id}/car-models")
    fun getCarModel(@Path("id") carMakeId: String): Call<BaseListModel<StanderModel1>>

    @GET("car-models/{id}/car-sub-models")
    fun getCarSubModel(@Path("id") carMakeId: String): Call<BaseListModel<StanderModel1>>




    @GET("ads")
    fun getAds(
        @Query("page") page: Int,
        @Query("category") categoryId: String?,
        @Query("type") typeId: String?,
        @Query("subcategory") subcategoryId: String?,
        @Query("region") regionId: String?,
        @Query("carMake") carMakeId: String?,
        @Query("carModels") carModelIds: List<String>?,
        @Query("carYears") carYears: ArrayList<String>?,
        @Query("text") keyword: String?
    ): Call<AdsResponse>

    @GET("ads")
    @JvmSuppressWildcards
    fun getAds(@Query("page") page: Int, @QueryMap prams: Map<String, Any>): Call<AdsResponse>

    @GET("arrange-by-date")
    fun getAdsByDate(@Query("page") page: Int): Call<BaseListModel<Ad>>

    @GET("arrange-by-high-price")
    fun getAdsByHighPrice(@Query("page") page: Int): Call<BaseListModel<Ad>>

    @GET("arrange-by-low-price")
    fun getAdsByLowPrice(@Query("page") page: Int): Call<BaseListModel<Ad>>

    @GET("arrange-by-location")
    fun getAdsByLocation(@Query("page") page: Int): Call<BaseListModel<Ad>>

    @GET("arrange-by-model")
    fun getAdsByModel(@Query("page") page: Int): Call<BaseListModel<Ad>>

    @GET("arrange-by-miles")
    fun getAdsByMiles(@Query("page") page: Int): Call<BaseListModel<Ad>>


    @GET("ads/{id}")
    fun getAd(@Path("id") adId: String): Call<AdResponse>

    @GET("users/{user_id}")
    fun getUser(@Path("user_id") userId: String): Call<BaseObjectModel<UserResponse>>

    @GET("ads")
    fun getUserAds(@Query("page") page: Int, @Query("user") userId: String): Call<AdsResponse>

    @GET("ads/{id}/related")
    fun getRelatedAds(@Path("id") adId: String): Call<RelatedAdResponse>

//    @GET("followings-list")
//    fun getFollowings(@Query("page") page: Int): Call<UsersListResponse>

//    @GET("followers-list")
//    fun getFollowers(@Query("page") page: Int): Call<UsersListResponse>

    @GET("users/followers/{id}")
    fun getFollowers(@Path("id") userId: String,@Query("page") page: Int): Call<UsersListResponse>

    @GET("users/showrooms")
    fun getCarShows(@Query("page") page: Int): Call<UsersListResponse>

    @GET("users/Followings/{id}")
    fun getFollowings(@Path("id") userId: String,@Query("page") page: Int): Call<UsersListResponse>

    @POST("follow-user/{id}")
    fun followUser(@Path("id") userId: String): Call<GeneralResponse>

    @POST("unfollow-user/{id}")
    fun unFollowUser(@Path("id") userId: String): Call<GeneralResponse>



    @GET("block-list")
    fun getBlockedList(@Query("page") page: Int): Call<UsersListResponse>

    @POST("block-user/{id}")
    fun blockUser(@Path("id") userId: String): Call<GeneralResponse>

    @POST("unblock-user/{id}")
    fun unblockUser(@Path("id") userId: String): Call<GeneralResponse>

    @GET("my-favourite")
    fun getFavoriteAds(@Query("page") page: Int): Call<AdsResponse>

    @GET("ads/{id}/recent-comments")
    fun getRecentComments(@Path("id") adId: String): Call<RecentCommentsResponse?>

    @POST("comments")
    fun newComment(@Body request: NewCommentRequest): Call<NewCommentResponse>

    @PUT("comments/{id}")
    fun editComment(
        @Path("id") id: String,
        @Body request: EditCommentRequest
    ): Call<EditCommentResponse>

    @DELETE("comments/{id}")
    fun deleteComment(@Path("id") id: String): Call<GeneralResponse>

    @GET("ads/{id}/comments")
    fun getComments(@Path("id") adId: String, @Query("page") page: Int): Call<CommentsResponse>

    @POST("ads/{id}/toggle-favourite")
    fun toggleFavoriteAd(@Path("id") adId: String): Call<GeneralResponse>

    @POST("ads/{id}/toggle-like")
    fun toggleLike(@Path("id") adId: String): Call<GeneralResponse>

    @GET("my-ads")
    fun getMyAds(@Query("page") page: Int): Call<AdsResponse>

    @POST("ads/{id}/report")
    fun reportAd(@Path("id") adId: String, @Body request: ReportAdRequest): Call<GeneralResponse>

    @POST("ads/{id}/new-view")
    fun viewAd(@Path("id") adId: String): Call<GeneralResponse>

    @Multipart
    @POST("my-ads")
    fun newAd(@Part parts: List<MultipartBody.Part>,@Part imgParts: List<MultipartBody.Part>,@Part videoPart: MultipartBody.Part?): Call<NewAdResponse>

    @Multipart
    @PUT("my-ads/{id}")
    fun editAd(@Path("id") adId: String, @Part parts: List<MultipartBody.Part>): Call<NewAdResponse>

    @PUT("my-ads/{id}/republish")
    fun republishAd(@Path("id") adId: String): Call<RepublishAdResponse>

    @PUT("my-ads/{id}/activate")
    fun activateAd(@Path("id") adId: String): Call<RepublishAdResponse>

    @PUT("my-ads/{id}/deactivate")
    fun deactivateAd(@Path("id") adId: String): Call<RepublishAdResponse>

    @DELETE("my-ads/{id}")
    fun deleteAd(@Path("id") adId: String): Call<GeneralResponse>

    @GET("notifications")
    fun getNotifications(@Query("page") page: Int): Call<NotificationsResponse>

    @GET("channels")
    fun getChannels(@Query("page") page: Int): Call<ChannelsResponse>

    @POST("channels")
    fun newChannel(@Body request: NewChannelRequest): Call<NewChannelResponse>

    @GET("channels/{id}/messages")
    fun getMessages(@Path("id") channelId: String, @Query("page") page: Int): Call<MessagesResponse>

    @POST("channels/{id}/new-message-text")
    fun newMessageText(
        @Path("id") channelId: String,
        @Body request: NewMessageTextRequest
    ): Call<NewMessageResponse>

    @Multipart
    @POST("channels/{id}/new-message-photo")
    fun newMessagePhoto(
        @Path("id") channelId: String,
        @Part parts: List<MultipartBody.Part>
    ): Call<NewMessageResponse>

    @Multipart
    @POST("channels/{id}/new-message-video")
    fun newMessageVideo(
        @Path("id") channelId: String,
        @Part parts: List<MultipartBody.Part>
    ): Call<NewMessageResponse>

    @Multipart
    @POST("channels/{id}/new-message-audio")
    fun newMessageAudio(
        @Path("id") channelId: String,
        @Part parts: List<MultipartBody.Part>
    ): Call<NewMessageResponse>

    @GET("banners")
    fun getBanners(): Call<BannersResponse>

    @POST("register-token/{id}")
    fun registerPushToken(@Path("id") deviceId: String): Call<GeneralResponse>

    @DELETE("delete-token/{id}")
    fun deletePushToken(@Path("id") deviceId: String): Call<GeneralResponse>
}