package com.aelzohry.topsaleqatar.repository.remote

import android.text.TextUtils
import android.util.Log
import android.webkit.MimeTypeMap
import com.aelzohry.topsaleqatar.BuildConfig
import com.aelzohry.topsaleqatar.helper.Constants
import com.aelzohry.topsaleqatar.helper.Helper
import com.aelzohry.topsaleqatar.helper.toEnglishDigits
import com.aelzohry.topsaleqatar.model.Ad
import com.aelzohry.topsaleqatar.model.Banner
import com.aelzohry.topsaleqatar.model.CarMake
import com.aelzohry.topsaleqatar.model.CarModel
import com.aelzohry.topsaleqatar.model.Category
import com.aelzohry.topsaleqatar.model.CategoryType
import com.aelzohry.topsaleqatar.model.LocalStanderModel
import com.aelzohry.topsaleqatar.model.Not
import com.aelzohry.topsaleqatar.model.Region
import com.aelzohry.topsaleqatar.model.StanderModel1
import com.aelzohry.topsaleqatar.model.Subcategory
import com.aelzohry.topsaleqatar.model.TChannel
import com.aelzohry.topsaleqatar.model.TMessage
import com.aelzohry.topsaleqatar.model.User
import com.aelzohry.topsaleqatar.repository.Repository
import com.aelzohry.topsaleqatar.repository.remote.requests.EditCommentRequest
import com.aelzohry.topsaleqatar.repository.remote.requests.LoginRequest
import com.aelzohry.topsaleqatar.repository.remote.requests.NewChannelRequest
import com.aelzohry.topsaleqatar.repository.remote.requests.NewCommentRequest
import com.aelzohry.topsaleqatar.repository.remote.requests.NewMessageTextRequest
import com.aelzohry.topsaleqatar.repository.remote.requests.ReportAdRequest
import com.aelzohry.topsaleqatar.repository.remote.responses.AdResponse
import com.aelzohry.topsaleqatar.repository.remote.responses.AdsResponse
import com.aelzohry.topsaleqatar.repository.remote.responses.BannerSliderResponse
import com.aelzohry.topsaleqatar.repository.remote.responses.BannersResponse
import com.aelzohry.topsaleqatar.repository.remote.responses.CategoriesGroupd
import com.aelzohry.topsaleqatar.repository.remote.responses.CategoriesResponse
import com.aelzohry.topsaleqatar.repository.remote.responses.ChannelsResponse
import com.aelzohry.topsaleqatar.repository.remote.responses.CommentsResponse
import com.aelzohry.topsaleqatar.repository.remote.responses.EditCommentResponse
import com.aelzohry.topsaleqatar.repository.remote.responses.EditProfileResponse
import com.aelzohry.topsaleqatar.repository.remote.responses.GeneralResponse
import com.aelzohry.topsaleqatar.repository.remote.responses.HomeResponse
import com.aelzohry.topsaleqatar.repository.remote.responses.LoginResponse
import com.aelzohry.topsaleqatar.repository.remote.responses.MessagesResponse
import com.aelzohry.topsaleqatar.repository.remote.responses.NewAdResponse
import com.aelzohry.topsaleqatar.repository.remote.responses.NewChannelResponse
import com.aelzohry.topsaleqatar.repository.remote.responses.NewCommentResponse
import com.aelzohry.topsaleqatar.repository.remote.responses.NewMessageResponse
import com.aelzohry.topsaleqatar.repository.remote.responses.NotificationsResponse
import com.aelzohry.topsaleqatar.repository.remote.responses.ProfileResponse
import com.aelzohry.topsaleqatar.repository.remote.responses.RecentComments
import com.aelzohry.topsaleqatar.repository.remote.responses.RecentCommentsResponse
import com.aelzohry.topsaleqatar.repository.remote.responses.RegionsResponse
import com.aelzohry.topsaleqatar.repository.remote.responses.RelatedAdResponse
import com.aelzohry.topsaleqatar.repository.remote.responses.RepublishAdResponse
import com.aelzohry.topsaleqatar.repository.remote.responses.UnSeenNotificationsResponse
import com.aelzohry.topsaleqatar.repository.remote.responses.UserResponse
import com.aelzohry.topsaleqatar.repository.remote.responses.UsersListResponse
import com.aelzohry.topsaleqatar.utils.base.baseModel.BaseListModel
import com.aelzohry.topsaleqatar.utils.base.baseModel.BaseObjectModel
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

class RemoteRepository : Repository {

    private val api: WebService

    init {
        val httpClient = OkHttpClient.Builder()
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(interceptor)
            httpClient.addInterceptor(httpLoggingInterceptor)

        }

        val client = httpClient.connectTimeout(5, TimeUnit.MINUTES).writeTimeout(5, TimeUnit.MINUTES).readTimeout(5, TimeUnit.MINUTES).addInterceptor(HeaderInterceptor()).build()

        val retrofit = Retrofit.Builder().baseUrl(Constants.API_BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create()).build()

        api = retrofit.create(WebService::class.java)
    }

    override fun getNumberOfNotificationsNotSeen(callBack: (response: UnSeenNotificationsResponse?) -> Unit): Call<UnSeenNotificationsResponse> {
        val call = api.getNumberOfNotificationsNotSeen()
        call.enqueue(object : Callback<UnSeenNotificationsResponse> {
            override fun onFailure(call: Call<UnSeenNotificationsResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(null)
            }

            override fun onResponse(call: Call<UnSeenNotificationsResponse>, response: retrofit2.Response<UnSeenNotificationsResponse>) {
                if (call.isCanceled) return
                callBack(response.body())
            }
        })
        return call
    }

    override fun getHome(callBack: (response: HomeResponse?) -> Unit): Call<HomeResponse> {
        val call = api.getHome()
        call.enqueue(object : Callback<HomeResponse> {
            override fun onFailure(call: Call<HomeResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(null)
            }

            override fun onResponse(call: Call<HomeResponse>, response: retrofit2.Response<HomeResponse>) {
                if (call.isCanceled) return
                callBack(response.body())
            }
        })
        return call
    }

    override fun getTopSliders(callBack: (response: BannerSliderResponse?) -> Unit): Call<BannerSliderResponse> {
        val call = api.getTopSliders()
        call.enqueue(object : Callback<BannerSliderResponse> {
            override fun onFailure(call: Call<BannerSliderResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(null)
            }

            override fun onResponse(call: Call<BannerSliderResponse>, response: retrofit2.Response<BannerSliderResponse>) {
                if (call.isCanceled) return
                callBack(response.body())
            }
        })
        return call
    }

    override fun getCategories(callBack: (response: CategoriesResponse?) -> Unit): Call<CategoriesResponse> {
        val call = api.getCategories()
        call.enqueue(object : Callback<CategoriesResponse> {
            override fun onFailure(call: Call<CategoriesResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(null)
            }

            override fun onResponse(call: Call<CategoriesResponse>, response: retrofit2.Response<CategoriesResponse>) {
                if (call.isCanceled) return
                callBack(response.body())
            }
        })
        return call
    }

    override fun getCategoriesGroupd(callBack: (response: CategoriesGroupd?) -> Unit): Call<CategoriesGroupd> {
        val call = api.getCategoriesGroupd()
        call.enqueue(object : Callback<CategoriesGroupd> {
            override fun onFailure(call: Call<CategoriesGroupd>, t: Throwable) {
                if (call.isCanceled) return
                callBack(null)
            }

            override fun onResponse(call: Call<CategoriesGroupd>, response: retrofit2.Response<CategoriesGroupd>) {
                if (call.isCanceled) return
                callBack(response.body())
            }
        })
        return call
    }

    override fun getRegions(callBack: (response: RegionsResponse?) -> Unit): Call<RegionsResponse> {
        val call = api.getRegions()
        call.enqueue(object : Callback<RegionsResponse> {
            override fun onFailure(call: Call<RegionsResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(null)
            }

            override fun onResponse(call: Call<RegionsResponse>, response: retrofit2.Response<RegionsResponse>) {
                if (call.isCanceled) return
                callBack(response.body())
            }
        })
        return call
    }

    override fun getCities(callBack: (response: RegionsResponse?) -> Unit): Call<RegionsResponse> {
        val call = api.getCities()
        call.enqueue(object : Callback<RegionsResponse> {
            override fun onFailure(call: Call<RegionsResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(null)
            }

            override fun onResponse(call: Call<RegionsResponse>, response: retrofit2.Response<RegionsResponse>) {
                if (call.isCanceled) return
                Log.e("test_cities", response.toString())
                callBack(response.body())
            }
        })
        return call
    }

    override fun getRegion(callBack: (response: BaseListModel<LocalStanderModel>?) -> Unit): Call<BaseListModel<LocalStanderModel>> {
        val call = api.getRegion()
        call.enqueue(object : Callback<BaseListModel<LocalStanderModel>> {
            override fun onFailure(call: Call<BaseListModel<LocalStanderModel>>, t: Throwable) {
                if (call.isCanceled) return
                callBack(null)
            }

            override fun onResponse(call: Call<BaseListModel<LocalStanderModel>>, response: retrofit2.Response<BaseListModel<LocalStanderModel>>) {
                if (call.isCanceled) return
                callBack(response.body())
            }
        })
        return call
    }

//    override fun getCarMakes(callBack: (response: CarMakesResponse?) -> Unit): Call<CarMakesResponse> {
//        val call = api.getCarMakes()
//        call.enqueue(object : Callback<CarMakesResponse> {
//            override fun onFailure(
//                call: Call<CarMakesResponse>,
//                t: Throwable
//            ) {
//                if (call.isCanceled) return
//                callBack(null)
//            }
//
//            override fun onResponse(
//                call: Call<CarMakesResponse>,
//                response: retrofit2.Response<CarMakesResponse>
//            ) {
//                if (call.isCanceled) return
//                callBack(response.body())
//            }
//        })
//        return call
//    }

    override fun getCarMake(callBack: (response: BaseListModel<StanderModel1>?) -> Unit): Call<BaseListModel<StanderModel1>> {
        val call = api.getCarMake()
        call.enqueue(object : Callback<BaseListModel<StanderModel1>> {
            override fun onFailure(call: Call<BaseListModel<StanderModel1>>, t: Throwable) {
                if (call.isCanceled) return
                callBack(null)
            }

            override fun onResponse(call: Call<BaseListModel<StanderModel1>>, response: retrofit2.Response<BaseListModel<StanderModel1>>) {
                if (call.isCanceled) return
                callBack(response.body())
            }
        })
        return call
    }

//    override fun getCarModels(
//        make: CarMake,
//        callBack: (response: CarModelsResponse?) -> Unit
//    ): Call<CarModelsResponse> {
//        val call = api.getCarModels(make._id)
//        call.enqueue(object : Callback<CarModelsResponse> {
//            override fun onFailure(
//                call: Call<CarModelsResponse>,
//                t: Throwable
//            ) {
//                if (call.isCanceled) return
//                callBack(null)
//            }
//
//            override fun onResponse(
//                call: Call<CarModelsResponse>,
//                response: retrofit2.Response<CarModelsResponse>
//            ) {
//                if (call.isCanceled) return
//                callBack(response.body())
//            }
//        })
//        return call
//    }

    override fun getCarModel(makeId: String, callBack: (response: BaseListModel<StanderModel1>?) -> Unit): Call<BaseListModel<StanderModel1>> {
        val call = api.getCarModel(makeId)
        call.enqueue(object : Callback<BaseListModel<StanderModel1>> {
            override fun onFailure(call: Call<BaseListModel<StanderModel1>>, t: Throwable) {
                if (call.isCanceled) return
                callBack(null)
            }

            override fun onResponse(call: Call<BaseListModel<StanderModel1>>, response: retrofit2.Response<BaseListModel<StanderModel1>>) {
                if (call.isCanceled) return
                callBack(response.body())
            }
        })
        return call
    }

    override fun getCarSubModel(modelId: String, callBack: (response: BaseListModel<StanderModel1>?) -> Unit): Call<BaseListModel<StanderModel1>> {
        Log.e("test_params", modelId)
        val call = api.getCarSubModel(modelId)
        call.enqueue(object : Callback<BaseListModel<StanderModel1>> {
            override fun onFailure(call: Call<BaseListModel<StanderModel1>>, t: Throwable) {
                if (call.isCanceled) return
                callBack(null)
            }

            override fun onResponse(call: Call<BaseListModel<StanderModel1>>, response: retrofit2.Response<BaseListModel<StanderModel1>>) {
                if (call.isCanceled) return
                callBack(response.body())
            }
        })
        return call
    }

    override fun getAds(page: Int, category: Category?, type: CategoryType?, subcategory: Subcategory?, region: Region?, carMake: CarMake?, carModels: ArrayList<CarModel>?, carYears: ArrayList<String>?, keyword: String?, callBack: (response: AdsResponse?) -> Unit): Call<AdsResponse> {
        val call = api.getAds(page, category?._id, type?._id, subcategory?._id, region?._id, carMake?._id, carModels?.map { it._id }, carYears, keyword)
        call.enqueue(object : Callback<AdsResponse> {
            override fun onFailure(call: Call<AdsResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(null)
            }

            override fun onResponse(call: Call<AdsResponse>, response: retrofit2.Response<AdsResponse>) {
                if (call.isCanceled) return
                callBack(response.body())
            }
        })
        return call
    }

    override fun getUser(userId: String, callBack: (user: User?) -> Unit) {
        val call = api.getUser(userId)
        call.enqueue(object : Callback<BaseObjectModel<UserResponse>> {
            override fun onResponse(call: Call<BaseObjectModel<UserResponse>>, response: retrofit2.Response<BaseObjectModel<UserResponse>>) {
                if (call.isCanceled) return
                callBack(response.body()?.response?.user)
            }

            override fun onFailure(call: Call<BaseObjectModel<UserResponse>>, t: Throwable) {
                if (call.isCanceled) return
                callBack(null)
            }
        })
    }

    override fun getUserAds(userId: String, page: Int, callBack: (response: AdsResponse?) -> Unit): Call<AdsResponse> {
        val call = api.getUserAds(page, userId)
        call.enqueue(object : Callback<AdsResponse> {
            override fun onFailure(call: Call<AdsResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(null)
            }

            override fun onResponse(call: Call<AdsResponse>, response: retrofit2.Response<AdsResponse>) {
                if (call.isCanceled) return
                callBack(response.body())
            }
        })
        return call
    }

    override fun getRelatedAds(adId: String, callBack: (response: RelatedAdResponse?) -> Unit): Call<RelatedAdResponse> {
        val call = api.getRelatedAds(adId)
        call.enqueue(object : Callback<RelatedAdResponse> {
            override fun onFailure(call: Call<RelatedAdResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(null)
            }

            override fun onResponse(call: Call<RelatedAdResponse>, response: retrofit2.Response<RelatedAdResponse>) {
                if (call.isCanceled) return
                callBack(response.body())
            }
        })

        return call
    }

    override fun login(name: String, mobile: String, code: String?, pushId: String?, callBack: (response: LoginResponse?) -> Unit): Call<LoginResponse> {
        val request = LoginRequest(name, mobile, code, pushId)
        val call = api.login(request)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(null)
            }

            override fun onResponse(call: Call<LoginResponse>, response: retrofit2.Response<LoginResponse>) {
                if (call.isCanceled) return
                callBack(response.body())
            }
        })
        return call
    }

    override fun getProfile(callBack: (user: User?, message: String) -> Unit): Call<ProfileResponse> {
        val call = api.getProfile()
        call.enqueue(object : Callback<ProfileResponse> {
            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(null, t.localizedMessage ?: t.toString())
            }

            override fun onResponse(call: Call<ProfileResponse>, response: retrofit2.Response<ProfileResponse>) {
                if (call.isCanceled) return
                val body = response.body()
                callBack(body?.data?.user, body?.message ?: "Server Error")
            }
        })
        return call
    }

    override fun updateProfile(name: String, email: String?, bio: String?, image: String?, addressText: String?, latitude: Double?, longitude: Double?, callBack: (user: User?, message: String) -> Unit): Call<EditProfileResponse> {
        val parts: ArrayList<MultipartBody.Part> = arrayListOf()

        parts.add(MultipartBody.Part.createFormData("name", name))

        email?.let { parts.add(MultipartBody.Part.createFormData("email", it)) }
        bio?.let { parts.add(MultipartBody.Part.createFormData("bio", it)) }
        addressText?.let { parts.add(MultipartBody.Part.createFormData("address", it)) }
        latitude?.let { parts.add(MultipartBody.Part.createFormData("latitude", it.toString())) }
        longitude?.let { parts.add(MultipartBody.Part.createFormData("longitude", it.toString())) }
        if (TextUtils.isEmpty(image)) {
            parts.add(MultipartBody.Part.createFormData("image", "www."))
        } else {
            val file = File(image)
            val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension) ?: "image/*"
            val fileRequestBody = file.asRequestBody(mimeType.toMediaType())
            parts.add(MultipartBody.Part.createFormData("image", file.name.toEnglishDigits(), fileRequestBody))
        }

        val call = api.updateProfile(parts)
        call.enqueue(object : Callback<EditProfileResponse> {
            override fun onFailure(call: Call<EditProfileResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(null, t.localizedMessage ?: t.toString())
            }

            override fun onResponse(call: Call<EditProfileResponse>, response: retrofit2.Response<EditProfileResponse>) {
                if (call.isCanceled) return
                val body = response.body()
                callBack(body?.data?.user, body?.message ?: "Server Error")
            }
        })
        return call
    }

    override fun getFollowings(page: Int, userId: String, callBack: (users: ArrayList<User>?, message: String) -> Unit): Call<UsersListResponse> {
        val call = api.getFollowings(userId, page)
        call.enqueue(object : Callback<UsersListResponse> {
            override fun onFailure(call: Call<UsersListResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(null, t.localizedMessage ?: t.toString())
            }

            override fun onResponse(call: Call<UsersListResponse>, response: retrofit2.Response<UsersListResponse>) {
                if (call.isCanceled) return
                val body = response.body()
                callBack(body?.data?.users, body?.message ?: "Server Error")
            }
        })
        return call
    }

    override fun getCarShows(page: Int, callBack: (users: ArrayList<User>?, message: String) -> Unit): Call<UsersListResponse> {
        val call = api.getCarShows( page)
        call.enqueue(object : Callback<UsersListResponse> {
            override fun onFailure(call: Call<UsersListResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(null, t.localizedMessage ?: t.toString())
            }

            override fun onResponse(call: Call<UsersListResponse>, response: retrofit2.Response<UsersListResponse>) {
                if (call.isCanceled) return
                val body = response.body()
                callBack(body?.data?.users, body?.message ?: "Server Error")
            }
        })
        return call
    }

    override fun getFollowers(page: Int, userId: String, callBack: (users: ArrayList<User>?, message: String) -> Unit): Call<UsersListResponse> {
        val call = api.getFollowers(userId, page)
        call.enqueue(object : Callback<UsersListResponse> {
            override fun onFailure(call: Call<UsersListResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(null, t.localizedMessage ?: t.toString())
            }

            override fun onResponse(call: Call<UsersListResponse>, response: retrofit2.Response<UsersListResponse>) {
                if (call.isCanceled) return
                val body = response.body()
                callBack(body?.data?.users, body?.message ?: "Server Error")
            }
        })
        return call
    }

    override fun getBlockedList(page: Int, callBack: (users: ArrayList<User>?, message: String) -> Unit): Call<UsersListResponse> {
        val call = api.getBlockedList(page)
        call.enqueue(object : Callback<UsersListResponse> {
            override fun onFailure(call: Call<UsersListResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(null, t.localizedMessage ?: t.toString())
            }

            override fun onResponse(call: Call<UsersListResponse>, response: retrofit2.Response<UsersListResponse>) {
                if (call.isCanceled) return
                val body = response.body()
                callBack(body?.data?.users, body?.message ?: "Server Error")
            }
        })
        return call
    }

    override fun followUser(userId: String, callBack: (success: Boolean, message: String) -> Unit): Call<GeneralResponse> {
        val call = api.followUser(userId)
        call.enqueue(object : Callback<GeneralResponse> {
            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(false, t.localizedMessage ?: t.toString())
            }

            override fun onResponse(call: Call<GeneralResponse>, response: retrofit2.Response<GeneralResponse>) {
                if (call.isCanceled) return
                val body = response.body()
                callBack(body?.success ?: false, body?.message ?: "Server Error")
            }
        })
        return call
    }

    override fun unFollowUser(userId: String, callBack: (success: Boolean, message: String) -> Unit): Call<GeneralResponse> {
        val call = api.unFollowUser(userId)
        call.enqueue(object : Callback<GeneralResponse> {
            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(false, t.localizedMessage ?: t.toString())
            }

            override fun onResponse(call: Call<GeneralResponse>, response: retrofit2.Response<GeneralResponse>) {
                if (call.isCanceled) return
                val body = response.body()
                callBack(body?.success ?: false, body?.message ?: "Server Error")
            }
        })
        return call
    }

    override fun blockUser(userId: String, callBack: (success: Boolean, message: String) -> Unit): Call<GeneralResponse> {
        val call = api.blockUser(userId)
        call.enqueue(object : Callback<GeneralResponse> {
            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(false, t.localizedMessage ?: t.toString())
            }

            override fun onResponse(call: Call<GeneralResponse>, response: retrofit2.Response<GeneralResponse>) {
                if (call.isCanceled) return
                val body = response.body()
                callBack(body?.success ?: false, body?.message ?: "Server Error")
            }
        })
        return call
    }

    override fun unblockUser(userId: String, callBack: (success: Boolean, message: String) -> Unit): Call<GeneralResponse> {
        val call = api.unblockUser(userId)
        call.enqueue(object : Callback<GeneralResponse> {
            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(false, t.localizedMessage ?: t.toString())
            }

            override fun onResponse(call: Call<GeneralResponse>, response: retrofit2.Response<GeneralResponse>) {
                if (call.isCanceled) return
                val body = response.body()
                callBack(body?.success ?: false, body?.message ?: "Server Error")
            }
        })
        return call
    }

    override fun getFavoriteAds(page: Int, callBack: (response: AdsResponse?) -> Unit): Call<AdsResponse> {
        val call = api.getFavoriteAds(page)

        call.enqueue(object : Callback<AdsResponse> {
            override fun onFailure(call: Call<AdsResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(null)
            }

            override fun onResponse(call: Call<AdsResponse>, response: retrofit2.Response<AdsResponse>) {
                if (call.isCanceled) return
                callBack(response.body())
            }
        })
        return call

//        call.enqueue(object : Callback<AdsResponse> {
//            override fun onFailure(
//                call: Call<AdsResponse>,
//                t: Throwable
//            ) {
//                if (call.isCanceled) return
//                callBack(null)
//            }
//
//            override fun onResponse(
//                call: Call<AdsResponse>,
//                response: retrofit2.Response<AdsResponse>
//            ) {
//                if (call.isCanceled) return
//                callBack(response.body())
//            }
//        })
//        return call
    }

    override fun toggleFavoriteAd(adId: String, callBack: (success: Boolean, message: String) -> Unit): Call<GeneralResponse> {
        val call = api.toggleFavoriteAd(adId)
        call.enqueue(object : Callback<GeneralResponse> {
            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(false, t.localizedMessage ?: t.toString())
            }

            override fun onResponse(call: Call<GeneralResponse>, response: retrofit2.Response<GeneralResponse>) {
                if (call.isCanceled) return
                val body = response.body()
                callBack(body?.success ?: false, body?.message ?: "Server Error")
            }
        })
        return call
    }

    override fun toggleLike(adId: String, callBack: (success: Boolean, message: String) -> Unit): Call<GeneralResponse> {
        val call = api.toggleLike(adId)
        call.enqueue(object : Callback<GeneralResponse> {
            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(false, t.localizedMessage ?: t.toString())
            }

            override fun onResponse(call: Call<GeneralResponse>, response: retrofit2.Response<GeneralResponse>) {
                if (call.isCanceled) return
                val body = response.body()
                callBack(body?.success ?: false, body?.message ?: "Server Error")
            }
        })
        return call
    }

    override fun getMyAds(page: Int, callBack: (response: AdsResponse?) -> Unit): Call<AdsResponse> {
        val call = api.getMyAds(page)
        call.enqueue(object : Callback<AdsResponse> {
            override fun onFailure(call: Call<AdsResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(null)
            }

            override fun onResponse(call: Call<AdsResponse>, response: retrofit2.Response<AdsResponse>) {
                if (call.isCanceled) return
                callBack(response.body())
            }
        })
        return call
    }

    override fun reportAd(adId: String, text: String, callBack: (success: Boolean, message: String) -> Unit): Call<GeneralResponse> {
        val call = api.reportAd(adId, ReportAdRequest(text))
        call.enqueue(object : Callback<GeneralResponse> {
            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(false, t.localizedMessage ?: t.message ?: "Unknown error")
            }

            override fun onResponse(call: Call<GeneralResponse>, response: retrofit2.Response<GeneralResponse>) {
                if (call.isCanceled) return
                callBack(response.body()?.success ?: false, response.body()?.message ?: "Unknown error")
            }
        })
        return call
    }

    override fun viewAd(adId: String, callBack: (success: Boolean, message: String) -> Unit): Call<GeneralResponse> {
        val call = api.viewAd(adId)
        call.enqueue(object : Callback<GeneralResponse> {
            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(false, t.localizedMessage ?: t.message ?: "Unknown error")
            }

            override fun onResponse(call: Call<GeneralResponse>, response: retrofit2.Response<GeneralResponse>) {
                if (call.isCanceled) return
                callBack(response.body()?.success ?: false, response.body()?.message ?: "Unknown error")
            }
        })
        return call
    }

    override fun getRecentComments(adId: String, callBack: (response: RecentComments?) -> Unit): Call<RecentCommentsResponse?> {
        val call = api.getRecentComments(adId)
        call.enqueue(object : Callback<RecentCommentsResponse?> {
            override fun onFailure(call: Call<RecentCommentsResponse?>, t: Throwable) {
                if (call.isCanceled) return
                callBack(null)
            }

            override fun onResponse(call: Call<RecentCommentsResponse?>, response: retrofit2.Response<RecentCommentsResponse?>) {
                if (call.isCanceled) return
                callBack(response.body()?.data)
            }
        })

        return call
    }

    override fun newComment(adId: String, text: String, callBack: (response: NewCommentResponse?) -> Unit): Call<NewCommentResponse> {
        val call = api.newComment(NewCommentRequest(adId, text))
        call.enqueue(object : Callback<NewCommentResponse> {
            override fun onFailure(call: Call<NewCommentResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(null)
            }

            override fun onResponse(call: Call<NewCommentResponse>, response: retrofit2.Response<NewCommentResponse>) {
                if (call.isCanceled) return
                callBack(response.body())
            }
        })

        return call
    }

    override fun editComment(commentId: String, text: String, callBack: (response: EditCommentResponse?) -> Unit): Call<EditCommentResponse> {
        val call = api.editComment(commentId, EditCommentRequest(text))
        call.enqueue(object : Callback<EditCommentResponse> {
            override fun onFailure(call: Call<EditCommentResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(null)
            }

            override fun onResponse(call: Call<EditCommentResponse>, response: retrofit2.Response<EditCommentResponse>) {
                if (call.isCanceled) return
                callBack(response.body())
            }
        })

        return call
    }

    override fun deleteComment(commentId: String, callBack: (success: Boolean, message: String) -> Unit): Call<GeneralResponse> {
        val call = api.deleteComment(commentId)
        call.enqueue(object : Callback<GeneralResponse> {
            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(false, t.localizedMessage ?: t.message ?: "Unknown error")
            }

            override fun onResponse(call: Call<GeneralResponse>, response: retrofit2.Response<GeneralResponse>) {
                if (call.isCanceled) return
                callBack(response.body()?.success ?: false, response.body()?.message ?: "Server Error")
            }
        })

        return call
    }

    override fun getComments(adId: String, page: Int, callBack: (response: CommentsResponse?) -> Unit): Call<CommentsResponse> {
        val call = api.getComments(adId, page)
        call.enqueue(object : Callback<CommentsResponse> {
            override fun onFailure(call: Call<CommentsResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(null)
            }

            override fun onResponse(call: Call<CommentsResponse>, response: retrofit2.Response<CommentsResponse>) {
                if (call.isCanceled) return
                callBack(response.body())
            }
        })
        return call
    }

    override fun newAd(title: String, price: String, details: String, categoryId: String,
        space: String?, typeId: String?, subcategoryId: String?, regionId: String?,
        cityId: String?, carMakeId: String?, carModelId: String?, carSubModelId: String?,
        carYear: String?, motionVector: String?, engineSize: String?, km: String?,
        lat: String?, lng: String?, address: String?, rooms: String?, bathRooms: String?,
        engineDriveSystem: String?, fuelType: String?, carColor: String?,
        images: List<File>,videoPath:File?, isAllowComments: Boolean, callBack: (success: Boolean, message: String, ad: Ad?) -> Unit): Call<NewAdResponse> {

        val parts: ArrayList<MultipartBody.Part> = arrayListOf()
        parts.add(MultipartBody.Part.createFormData("title", title))
        parts.add(MultipartBody.Part.createFormData("price", price))
        parts.add(MultipartBody.Part.createFormData("category", categoryId))
        details.let { parts.add(MultipartBody.Part.createFormData("details", it)) }

        typeId?.let { parts.add(MultipartBody.Part.createFormData("type", it)) }

        space?.let { parts.add(MultipartBody.Part.createFormData("space", it)) }
        subcategoryId?.let { parts.add(MultipartBody.Part.createFormData("subcategory", it)) }
        regionId?.let { parts.add(MultipartBody.Part.createFormData("region", it)) }
        cityId?.let { parts.add(MultipartBody.Part.createFormData("city", it)) }
        carMakeId?.let { parts.add(MultipartBody.Part.createFormData("carMake", it)) }
        carModelId?.let { parts.add(MultipartBody.Part.createFormData("carModel", it)) }
        carSubModelId?.let { parts.add(MultipartBody.Part.createFormData("carSubModel", it)) }
        carYear?.let { parts.add(MultipartBody.Part.createFormData("carYear", it)) }
        motionVector?.let { parts.add(MultipartBody.Part.createFormData("gearBox", it)) }
        engineSize?.let { parts.add(MultipartBody.Part.createFormData("engineCapacity", it)) }
        km?.let { parts.add(MultipartBody.Part.createFormData("km", it)) }
        lat?.let { parts.add(MultipartBody.Part.createFormData("latitude", it)) }
        lng?.let { parts.add(MultipartBody.Part.createFormData("longitude", it)) }
        address?.let { parts.add(MultipartBody.Part.createFormData("address", it)) }
        rooms?.let { parts.add(MultipartBody.Part.createFormData("numberOfRooms", rooms)) }
        bathRooms?.let {
            parts.add(MultipartBody.Part.createFormData("numberOfBathroom", bathRooms))
        }

        fuelType?.let { parts.add(MultipartBody.Part.createFormData("fuelType", fuelType)) }
        parts.add(MultipartBody.Part.createFormData("iaAllowComments", isAllowComments.toString()))

        engineDriveSystem?.let {
            parts.add(MultipartBody.Part.createFormData("pushFatisType", engineDriveSystem))
        }
        carColor?.let { parts.add(MultipartBody.Part.createFormData("color", carColor)) }

        val imageParts: ArrayList<MultipartBody.Part> = arrayListOf()
        var videoPart:MultipartBody.Part? = null

//        /storage/emulated/0/DCIM/Camera/IMG_20230403_062355407.jpg

//        /storage/emulated/0/DCIM/Camera/IMG_20230403_042408.jpg

        images.forEachIndexed { index, item ->
            val file = File(item.absolutePath)
            val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension) ?: "image/*"
            val fileRequestBody = file.asRequestBody(mimeType.toMediaType())
            Log.e("test_3", item.absolutePath)
            imageParts.add(MultipartBody.Part.createFormData("photos", file.name.toEnglishDigits(), fileRequestBody))
        }

        videoPath?.let {
            val file = File(it.absolutePath)
            val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension) ?: "video/*"
            val fileRequestBody = file.asRequestBody(mimeType.toMediaType())
            Log.e("test_video_path", it.absolutePath)
            videoPart = MultipartBody.Part.createFormData("video", file.name.toEnglishDigits(), fileRequestBody)
        }



//        images.forEachIndexed { index, element ->
//            val itemPart = MultipartBody.Part.createFormData(
//                index.toString(),
//                System.currentTimeMillis().toString() + element.extension,
//                RequestBody.create("image/*".toMediaTypeOrNull(), File(element.path))
//            )
//            imageParts.add(itemPart)
//        }

//        images.forEach { file ->
        //val file = File(image)
//            val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension) ?: "image/*"
//            val fileRequestBody = file.asRequestBody(mimeType.toMediaType())
//            val imgPart = MultipartBody.Part.createFormData(
//                "photos", file.name.toEnglishDigits(), RequestBody.create(
//                    mimeType.toMediaType(), file.absolutePath
//                )
//            )

//            val requestFile = RequestBody.create(mimeType.toMediaType(), file)

//            val fileUrl: file.a
//            val body = prepareFilePart("photos", file.path)
//            parts.add(body)
//            parts.add(imgPart)

//            parts.add(
//                MultipartBody.Part.createFormData(
//                    "photos",
//                    file.name.toEnglishDigits(),
//                    requestFile
//                )
//            )

//            val itemPart = MultipartBody.Part.createFormData("photos", System.currentTimeMillis().toString() + file.extension, RequestBody.create("image/*".toMediaTypeOrNull(), File(file.path)))
//            imageParts.add(itemPart)

//        }

        val call = api.newAd(parts, imageParts,videoPart)
        call.enqueue(object : Callback<NewAdResponse> {
            override fun onFailure(call: Call<NewAdResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(false, t.localizedMessage ?: t.message ?: "Unkwon Error", null)
            }

            override fun onResponse(call: Call<NewAdResponse>, response: retrofit2.Response<NewAdResponse>) {
                if (call.isCanceled) return
                val res = response.body()
                callBack(res?.success ?: false, res?.message ?: "Server Error", res?.data?.ad)
            }
        })

        return call
    }

    fun prepareFilePart(partName: String, fileUri: String): MultipartBody.Part {
        val file = File(fileUri)
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension) ?: "image/*"
        val requestFile = RequestBody.create(mimeType.toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }

    override fun editAd(adId: String, title: String, price: String, details: String, categoryId: String, typeId: String?, subcategoryId: String?, regionId: String?, cityId: String?, carMakeId: String?, carModelId: String?, carSubModelId: String?, carYear: String?, motionVector: String?, engineSize: String?, km: String?, lat: String?, lng: String?, address: String?, rooms: String?, bathRooms: String?, images: List<File>, deletedPhotos: List<String>?, thumbnailType: String?, thumbnailId: String?, isAllowComments: Boolean, callBack: (success: Boolean, message: String, ad: Ad?) -> Unit): Call<NewAdResponse> {
        val parts: ArrayList<MultipartBody.Part> = arrayListOf()

        parts.add(MultipartBody.Part.createFormData("title", title))
//        parts.add(MultipartBody.Part.createFormData("details", details))
        parts.add(MultipartBody.Part.createFormData("price", price))
        parts.add(MultipartBody.Part.createFormData("category", categoryId))
        details.let { parts.add(MultipartBody.Part.createFormData("details", it)) }
        typeId?.let { parts.add(MultipartBody.Part.createFormData("type", it)) }
        lat?.let { parts.add(MultipartBody.Part.createFormData("latitude", it)) }
        lng?.let { parts.add(MultipartBody.Part.createFormData("longitude", it)) }
        address?.let { parts.add(MultipartBody.Part.createFormData("address", it)) }
        subcategoryId?.let { parts.add(MultipartBody.Part.createFormData("subcategory", it)) }
        regionId?.let { parts.add(MultipartBody.Part.createFormData("region", it)) }
        cityId?.let { parts.add(MultipartBody.Part.createFormData("city", it)) }
        carMakeId?.let { parts.add(MultipartBody.Part.createFormData("carMake", it)) }
        carModelId?.let { parts.add(MultipartBody.Part.createFormData("carModel", it)) }
        carSubModelId?.let { parts.add(MultipartBody.Part.createFormData("carSubModel", it)) }
        carYear?.let { parts.add(MultipartBody.Part.createFormData("carYear", it)) }
        motionVector?.let { parts.add(MultipartBody.Part.createFormData("gearBox", it)) }
        engineSize?.let { parts.add(MultipartBody.Part.createFormData("engineCapacity", it)) }
        km?.let { parts.add(MultipartBody.Part.createFormData("km", it)) }
        deletedPhotos?.forEach { parts.add(MultipartBody.Part.createFormData("deletePhotos", it)) }
        thumbnailType?.let { parts.add(MultipartBody.Part.createFormData("thumbnailType", it)) }
        thumbnailId?.let { parts.add(MultipartBody.Part.createFormData("thumbnailId", it)) }
        rooms?.let { parts.add(MultipartBody.Part.createFormData("numberOfRooms", rooms)) }
        bathRooms?.let {
            parts.add(MultipartBody.Part.createFormData("numberOfBathroom", bathRooms))
        }

        parts.add(MultipartBody.Part.createFormData("iaAllowComments", isAllowComments.toString()))


        images.forEach { file ->
            //val file = File(it)
            val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension) ?: "image/*"
            val fileRequestBody = file.asRequestBody(mimeType.toMediaType())
            parts.add(MultipartBody.Part.createFormData("photos", file.name.toEnglishDigits(), fileRequestBody))
        }

        val call = api.editAd(adId, parts)
        call.enqueue(object : Callback<NewAdResponse> {
            override fun onFailure(call: Call<NewAdResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(false, t.localizedMessage ?: t.message ?: "Unkwon Error", null)
            }

            override fun onResponse(call: Call<NewAdResponse>, response: retrofit2.Response<NewAdResponse>) {
                if (call.isCanceled) return
                val res = response.body()
                callBack(res?.success ?: false, res?.message ?: "Server Error", res?.data?.ad)
            }
        })

        return call
    }

    override fun deleteAd(adId: String, callBack: (success: Boolean, message: String) -> Unit): Call<GeneralResponse> {
        val call = api.deleteAd(adId)
        call.enqueue(object : Callback<GeneralResponse> {
            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(false, t.localizedMessage ?: t.message ?: "Unknown error")
            }

            override fun onResponse(call: Call<GeneralResponse>, response: retrofit2.Response<GeneralResponse>) {
                if (call.isCanceled) return
                callBack(response.body()?.success ?: false, response.body()?.message ?: "Server Error")
            }
        })

        return call
    }

    override fun republishAd(adId: String, callBack: (success: Boolean, message: String, updatedAd: Ad?) -> Unit): Call<RepublishAdResponse> {
        val call = api.republishAd(adId)
        call.enqueue(object : Callback<RepublishAdResponse> {
            override fun onFailure(call: Call<RepublishAdResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(false, t.localizedMessage ?: t.message ?: "Unknown error", null)
            }

            override fun onResponse(call: Call<RepublishAdResponse>, response: retrofit2.Response<RepublishAdResponse>) {
                if (call.isCanceled) return
                callBack(response.body()?.success ?: false, response.body()?.message ?: "Server Error", response.body()?.data?.ad)
            }
        })

        return call
    }

    override fun activateAd(adId: String, callBack: (success: Boolean, message: String, updatedAd: Ad?) -> Unit): Call<RepublishAdResponse> {
        val call = api.activateAd(adId)
        call.enqueue(object : Callback<RepublishAdResponse> {
            override fun onFailure(call: Call<RepublishAdResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(false, t.localizedMessage ?: t.message ?: "Unknown error", null)
            }

            override fun onResponse(call: Call<RepublishAdResponse>, response: retrofit2.Response<RepublishAdResponse>) {
                if (call.isCanceled) return
                callBack(response.body()?.success ?: false, response.body()?.message ?: "Server Error", response.body()?.data?.ad)
            }
        })

        return call
    }

    override fun deactivateAd(adId: String, callBack: (success: Boolean, message: String, updatedAd: Ad?) -> Unit): Call<RepublishAdResponse> {
        val call = api.deactivateAd(adId)
        call.enqueue(object : Callback<RepublishAdResponse> {
            override fun onFailure(call: Call<RepublishAdResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(false, t.localizedMessage ?: t.message ?: "Unknown error", null)
            }

            override fun onResponse(call: Call<RepublishAdResponse>, response: retrofit2.Response<RepublishAdResponse>) {
                if (call.isCanceled) return
                callBack(response.body()?.success ?: false, response.body()?.message ?: "Server Error", response.body()?.data?.ad)
            }
        })

        return call
    }

    override fun getNotifications(page: Int, callBack: (success: Boolean, message: String, notifications: ArrayList<Not>?) -> Unit): Call<NotificationsResponse> {
        val call = api.getNotifications(page)
        call.enqueue(object : Callback<NotificationsResponse> {
            override fun onFailure(call: Call<NotificationsResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(false, t.localizedMessage ?: t.message ?: "Unknown Error", null)
            }

            override fun onResponse(call: Call<NotificationsResponse>, response: retrofit2.Response<NotificationsResponse>) {
                if (call.isCanceled) return
                val body = response.body()
                callBack(body?.success ?: false, body?.message ?: "Server Error", body?.data?.notifications)
            }
        })

        return call
    }

    override fun getChannels(page: Int, callBack: (success: Boolean, message: String, channels: ArrayList<TChannel>?) -> Unit): Call<ChannelsResponse> {
        val call = api.getChannels(page)
        call.enqueue(object : Callback<ChannelsResponse> {
            override fun onFailure(call: Call<ChannelsResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(false, t.localizedMessage ?: t.message ?: "Unknown Error", null)
            }

            override fun onResponse(call: Call<ChannelsResponse>, response: retrofit2.Response<ChannelsResponse>) {
                if (call.isCanceled) return
                val body = response.body()
                callBack(body?.success ?: false, body?.message ?: "Server Error", body?.data?.channels)
            }
        })

        return call
    }

    override fun newChannel(adId: String?, userId: String?, callBack: (newChannel: TChannel?, message: String) -> Unit): Call<NewChannelResponse> {
        val call = api.newChannel(NewChannelRequest(adId, userId))
        call.enqueue(object : Callback<NewChannelResponse> {
            override fun onFailure(call: Call<NewChannelResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(null, t.localizedMessage ?: t.toString())
            }

            override fun onResponse(call: Call<NewChannelResponse>, response: retrofit2.Response<NewChannelResponse>) {
                if (call.isCanceled) return
                val res = response.body()
                callBack(res?.data?.channel, res?.message ?: "Server Error")
            }
        })

        return call
    }

    override fun getMessages(channelId: String, page: Int, callBack: (success: Boolean, messages: ArrayList<TMessage>?) -> Unit): Call<MessagesResponse> {
        val call = api.getMessages(channelId, page)
        call.enqueue(object : Callback<MessagesResponse> {
            override fun onFailure(call: Call<MessagesResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(false, null)
            }

            override fun onResponse(call: Call<MessagesResponse>, response: retrofit2.Response<MessagesResponse>) {
                if (call.isCanceled) return
                val body = response.body()
                callBack(body?.success ?: false, body?.data?.messages)
            }
        })

        return call
    }

    override fun newMessageText(channelId: String, body: String, callBack: (newMessage: TMessage?, message: String) -> Unit): Call<NewMessageResponse> {
        val call = api.newMessageText(channelId, NewMessageTextRequest(body))
        call.enqueue(object : Callback<NewMessageResponse> {
            override fun onFailure(call: Call<NewMessageResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(null, t.localizedMessage ?: t.toString())
            }

            override fun onResponse(call: Call<NewMessageResponse>, response: retrofit2.Response<NewMessageResponse>) {
                if (call.isCanceled) return
                val res = response.body()
                callBack(res?.data?.message, res?.message ?: "Server Error")
            }
        })

        return call
    }

    override fun newMessagePhoto(channelId: String, photoPath: String, callBack: (newMessage: TMessage?, message: String) -> Unit): Call<NewMessageResponse> {
        val parts: ArrayList<MultipartBody.Part> = arrayListOf()
        val file = File(photoPath)
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension) ?: "image/*"
        val fileRequestBody = file.asRequestBody(mimeType.toMediaType())
        parts.add(MultipartBody.Part.createFormData("attachment", file.name.toEnglishDigits(), fileRequestBody))

        val call = api.newMessagePhoto(channelId, parts)
        call.enqueue(object : Callback<NewMessageResponse> {
            override fun onFailure(call: Call<NewMessageResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(null, t.localizedMessage ?: t.toString())
            }

            override fun onResponse(call: Call<NewMessageResponse>, response: retrofit2.Response<NewMessageResponse>) {
                if (call.isCanceled) return
                val res = response.body()
                callBack(res?.data?.message, res?.message ?: "Server Error")
            }
        })

        return call
    }

    override fun newMessageVideo(channelId: String, videoPath: String, callBack: (newMessage: TMessage?, message: String) -> Unit): Call<NewMessageResponse> {
        val parts: ArrayList<MultipartBody.Part> = arrayListOf()
        val file = File(videoPath)
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension) ?: "video/*"
        val fileRequestBody = file.asRequestBody(mimeType.toMediaType())
        parts.add(MultipartBody.Part.createFormData("attachment", file.name.toEnglishDigits(), fileRequestBody))

        val call = api.newMessageVideo(channelId, parts)
        call.enqueue(object : Callback<NewMessageResponse> {
            override fun onFailure(call: Call<NewMessageResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(null, t.localizedMessage ?: t.toString())
            }

            override fun onResponse(call: Call<NewMessageResponse>, response: retrofit2.Response<NewMessageResponse>) {
                if (call.isCanceled) return
                val res = response.body()
                callBack(res?.data?.message, res?.message ?: "Server Error")
            }
        })

        return call
    }

    override fun newMessageAudio(channelId: String, audioPath: String, durationInSecond: Int, callBack: (newMessage: TMessage?, message: String) -> Unit): Call<NewMessageResponse> {
        val parts: ArrayList<MultipartBody.Part> = arrayListOf()
        parts.add(MultipartBody.Part.createFormData("duration", durationInSecond.toString()))

        val file = File(audioPath)
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension) ?: "audio/*"
        val fileRequestBody = file.asRequestBody(mimeType.toMediaType())
        parts.add(MultipartBody.Part.createFormData("attachment", file.name.toEnglishDigits(), fileRequestBody))

        val call = api.newMessageAudio(channelId, parts)
        call.enqueue(object : Callback<NewMessageResponse> {
            override fun onFailure(call: Call<NewMessageResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(null, t.localizedMessage ?: t.toString())
            }

            override fun onResponse(call: Call<NewMessageResponse>, response: retrofit2.Response<NewMessageResponse>) {
                if (call.isCanceled) return
                val res = response.body()
                callBack(res?.data?.message, res?.message ?: "Server Error")
            }
        })

        return call
    }

    override fun getBanners(callBack: (banners: ArrayList<Banner>?) -> Unit): Call<BannersResponse> {
        val call = api.getBanners()
        call.enqueue(object : Callback<BannersResponse> {
            override fun onFailure(call: Call<BannersResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(null)
            }

            override fun onResponse(call: Call<BannersResponse>, response: retrofit2.Response<BannersResponse>) {
                if (call.isCanceled) return
                callBack(response.body()?.data?.banners)
            }
        })
        return call
    }

    override fun getAd(adId: String, callBack: (ad: Ad?) -> Unit): Call<AdResponse> {
        Log.e("ad_id", adId)
        val call = api.getAd(adId)
        call.enqueue(object : Callback<AdResponse> {
            override fun onFailure(call: Call<AdResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(null)
            }

            override fun onResponse(call: Call<AdResponse>, response: retrofit2.Response<AdResponse>) {
                response.body()?.toString()?.let { Log.e("ad_details", it) }
                if (call.isCanceled) return
                callBack(response.body()?.data)
            }
        })
        return call
    }

    override fun registerPushToken(id: String, callBack: (success: Boolean) -> Unit): Call<GeneralResponse> {
        val call = api.registerPushToken(id)
        call.enqueue(object : Callback<GeneralResponse> {
            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(false)
            }

            override fun onResponse(call: Call<GeneralResponse>, response: retrofit2.Response<GeneralResponse>) {
                if (call.isCanceled) return
                callBack(response.body()?.success ?: false)
            }
        })
        return call
    }

    override fun deletePushToken(id: String, callBack: (success: Boolean) -> Unit): Call<GeneralResponse> {
        val call = api.deletePushToken(id)
        call.enqueue(object : Callback<GeneralResponse> {
            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                if (call.isCanceled) return
                callBack(false)
            }

            override fun onResponse(call: Call<GeneralResponse>, response: retrofit2.Response<GeneralResponse>) {
                if (call.isCanceled) return
                callBack(response.body()?.success ?: false)
            }
        })
        return call
    }

}

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {

        val request = request().newBuilder().addHeader("Accept-Language", Helper.languageCode).addHeader("platform", "android").addHeader("app-version", BuildConfig.VERSION_NAME).addHeader("device-id", "123")

        Log.i("test_header_platform", "android")
        Log.i("test_header_device-id", "123")

        Helper.authToken?.let {
            Log.i("test_header_Authorization", "Bearer $it")
            request.addHeader("Authorization", "Bearer $it")
        }


        proceed(request.build())
    }
}