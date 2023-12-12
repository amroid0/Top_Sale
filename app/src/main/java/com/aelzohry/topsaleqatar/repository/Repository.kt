package com.aelzohry.topsaleqatar.repository

import com.aelzohry.topsaleqatar.repository.remote.responses.CategoriesGroupd
import com.aelzohry.topsaleqatar.model.*
import com.aelzohry.topsaleqatar.repository.remote.responses.*
import com.aelzohry.topsaleqatar.utils.base.baseModel.BaseListModel
import retrofit2.Call
import java.io.File



interface Repository {
    fun getNumberOfNotificationsNotSeen(callBack: (response: UnSeenNotificationsResponse?) -> Unit): Call<UnSeenNotificationsResponse>
    fun getHome(callBack: (response: HomeResponse?) -> Unit): Call<HomeResponse>
    fun getTopSliders(callBack: (response: BannerSliderResponse?) -> Unit): Call<BannerSliderResponse>
    fun login(
        name: String,
        mobile: String,
        code: String? = null,
        pushId: String? = null,
        callBack: (response: LoginResponse?) -> Unit
    ): Call<LoginResponse>

    fun getCategories(callBack: (response: CategoriesResponse?) -> Unit): Call<CategoriesResponse>
    fun getCategoriesGroupd(callBack: (response: CategoriesGroupd?) -> Unit): Call<CategoriesGroupd>
    fun getRegions(callBack: (response: RegionsResponse?) -> Unit): Call<RegionsResponse>
    fun getCities(callBack: (response: RegionsResponse?) -> Unit): Call<RegionsResponse>
    fun getRegion(callBack: (response: BaseListModel<LocalStanderModel>?) -> Unit): Call<BaseListModel<LocalStanderModel>>
//    fun getCarMakes(callBack: (response: CarMakesResponse?) -> Unit): Call<CarMakesResponse>
    fun getCarMake(callBack: (response: BaseListModel<StanderModel1>?) -> Unit): Call<BaseListModel<StanderModel1>>
//    fun getCarModels(
//        make: CarMake,
//        callBack: (response: CarModelsResponse?) -> Unit
//    ): Call<CarModelsResponse>

    fun getCarModel(
        makeId: String,
        callBack: (response: BaseListModel<StanderModel1>?) -> Unit
    ): Call<BaseListModel<StanderModel1>>


    fun getCarSubModel(
        modelId: String,
        callBack: (response: BaseListModel<StanderModel1>?) -> Unit
    ): Call<BaseListModel<StanderModel1>>

    fun getAds(
        page: Int,
        category: Category? = null,
        type: CategoryType? = null,
        subcategory: Subcategory? = null,
        region: Region? = null,
        carMake: CarMake? = null,
        carModels: ArrayList<CarModel>? = null,
        carYears: ArrayList<String>? = null,
        keyword: String? = null,
        callBack: (response: AdsResponse?) -> Unit
    ): Call<AdsResponse>

    fun getAd(adId: String, callBack: (ad: Ad?) -> Unit): Call<AdResponse>
    fun getRelatedAds(
        adId: String,
        callBack: (response: RelatedAdResponse?) -> Unit
    ): Call<RelatedAdResponse>

    fun getProfile(callBack: (user: User?, message: String) -> Unit): Call<ProfileResponse>
    fun updateProfile(
        name: String,
        email: String?,
        bio: String?,
        image: String?,
        addressText: String?,
        latitude: Double?,
        longitude: Double?,
        callBack: (user: User?, message: String) -> Unit
    ): Call<EditProfileResponse>

    fun getFollowings(
        page: Int,
        userId: String,
        callBack: (users: ArrayList<User>?, message: String) -> Unit
    ): Call<UsersListResponse>

    fun getCarShows(
        page: Int,
        callBack: (users: ArrayList<User>?, message: String) -> Unit
    ): Call<UsersListResponse>

    fun followUser(
        userId: String,

        callBack: (success: Boolean, message: String) -> Unit
    ): Call<GeneralResponse>

    fun unFollowUser(
        userId: String,
        callBack: (success: Boolean, message: String) -> Unit
    ): Call<GeneralResponse>

    fun getFollowers(
        page: Int,
        userId: String,
        callBack: (users: ArrayList<User>?, message: String) -> Unit
    ): Call<UsersListResponse>

    fun getBlockedList(
        page: Int,
        callBack: (users: ArrayList<User>?, message: String) -> Unit
    ): Call<UsersListResponse>

    fun blockUser(
        userId: String,
        callBack: (success: Boolean, message: String) -> Unit
    ): Call<GeneralResponse>

    fun unblockUser(
        userId: String,
        callBack: (success: Boolean, message: String) -> Unit
    ): Call<GeneralResponse>

    fun getFavoriteAds(page: Int, callBack: (response: AdsResponse?) -> Unit): Call<AdsResponse>
    fun toggleFavoriteAd(
        adId: String,
        callBack: (success: Boolean, message: String) -> Unit
    ): Call<GeneralResponse>

    fun toggleLike(
        adId: String,
        callBack: (success: Boolean, message: String) -> Unit
    ): Call<GeneralResponse>

    fun getMyAds(page: Int, callBack: (response: AdsResponse?) -> Unit): Call<AdsResponse>
    fun reportAd(
        adId: String,
        text: String,
        callBack: (success: Boolean, message: String) -> Unit
    ): Call<GeneralResponse>

    fun viewAd(
        adId: String,
        callBack: (success: Boolean, message: String) -> Unit
    ): Call<GeneralResponse>

    fun getRecentComments(
        adId: String,
        callBack: (response: RecentComments?) -> Unit
    ): Call<RecentCommentsResponse?>

    fun getComments(
        adId: String,
        page: Int,
        callBack: (response: CommentsResponse?) -> Unit
    ): Call<CommentsResponse>

    fun newComment(
        adId: String,
        text: String,
        callBack: (response: NewCommentResponse?) -> Unit
    ): Call<NewCommentResponse>

    fun editComment(
        commentId: String,
        text: String,
        callBack: (response: EditCommentResponse?) -> Unit
    ): Call<EditCommentResponse>

    fun deleteComment(
        commentId: String,
        callBack: (success: Boolean, message: String) -> Unit
    ): Call<GeneralResponse>

    fun getUserAds(
        userId: String,
        page: Int,
        callBack: (response: AdsResponse?) -> Unit
    ): Call<AdsResponse>

    fun newAd(
        title: String,
        price: String,
        details: String,
        categoryId: String,
        space: String?,
        typeId: String?,
        subcategoryId: String?,
        regionId: String?,
        cityId: String?,
        carMakeId: String?,
        carModelId: String?,
        carSubModelId: String?,
        carYear: String?,
        motionVector: String?,
        engineSize: String?,
        km: String?,
        lat: String?,
        lng: String?,
        address: String?,
        rooms: String?,
        bathRooms: String?,
        engineDriveSystem: String?,fuelType: String?, carColor: String?,
        images: List<File>,
        videoPath:File?,
        isAllowComments :Boolean,
        callBack: (success: Boolean, message: String, ad: Ad?) -> Unit
    ): Call<NewAdResponse>

    fun editAd(
        adId: String,
        title: String,
        price: String,
        details: String,
        categoryId: String,
        typeId: String?,
        subcategoryId: String?,
        regionId: String?,
        cityId: String?,
        carMakeId: String?,
        carModelId: String?,
        carSubModelId: String?,
        carYear: String?,
        motionVector: String?,
        engineSize: String?,
        km: String?,
        lat: String?,
        lng: String?,
        address: String?,
        rooms: String?,
        bathRooms: String?,
        images: List<File>,
        deletedPhotos: List<String>?,
        thumbnailType: String?,
        thumbnailId: String?,
        isAllowComments :Boolean,
        callBack: (success: Boolean, message: String, ad: Ad?) -> Unit
    ): Call<NewAdResponse>

    fun deleteAd(
        adId: String,
        callBack: (success: Boolean, message: String) -> Unit
    ): Call<GeneralResponse>

    fun republishAd(
        adId: String,
        callBack: (success: Boolean, message: String, updatedAd: Ad?) -> Unit
    ): Call<RepublishAdResponse>

    fun activateAd(
        adId: String,
        callBack: (success: Boolean, message: String, updatedAd: Ad?) -> Unit
    ): Call<RepublishAdResponse>

    fun deactivateAd(
        adId: String,
        callBack: (success: Boolean, message: String, updatedAd: Ad?) -> Unit
    ): Call<RepublishAdResponse>

    fun getNotifications(
        page: Int,
        callBack: (success: Boolean, message: String, notifications: ArrayList<Not>?) -> Unit
    ): Call<NotificationsResponse>

    fun getChannels(
        page: Int,
        callBack: (success: Boolean, message: String, channels: ArrayList<TChannel>?) -> Unit
    ): Call<ChannelsResponse>

    fun newChannel(
        adId: String?,
        userId: String?,
        callBack: (newChannel: TChannel?, message: String) -> Unit
    ): Call<NewChannelResponse>

    fun getMessages(
        channelId: String,
        page: Int,
        callBack: (success: Boolean, messages: ArrayList<TMessage>?) -> Unit
    ): Call<MessagesResponse>

    fun newMessageText(
        channelId: String,
        body: String,
        callBack: (newMessage: TMessage?, message: String) -> Unit
    ): Call<NewMessageResponse>

    fun newMessagePhoto(
        channelId: String,
        photoPath: String,
        callBack: (newMessage: TMessage?, message: String) -> Unit
    ): Call<NewMessageResponse>

    fun newMessageVideo(
        channelId: String,
        videoPath: String,
        callBack: (newMessage: TMessage?, message: String) -> Unit
    ): Call<NewMessageResponse>

    fun newMessageAudio(
        channelId: String,
        audioPath: String,
        durationInSecond: Int,
        callBack: (newMessage: TMessage?, message: String) -> Unit
    ): Call<NewMessageResponse>

    fun getBanners(callBack: (banners: ArrayList<Banner>?) -> Unit): Call<BannersResponse>
    fun registerPushToken(id: String, callBack: (success: Boolean) -> Unit): Call<GeneralResponse>
    fun deletePushToken(id: String, callBack: (success: Boolean) -> Unit): Call<GeneralResponse>
    fun getUser(userId: String, callBack: (user: User?) -> Unit)
}