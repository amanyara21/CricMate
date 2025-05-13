package com.aman.cricmate.utils

import com.aman.cricmate.model.AddDetailsRequest
import com.aman.cricmate.model.AddDetailsResponse
import com.aman.cricmate.model.ApiResponse
import com.aman.cricmate.model.BallResponse
import com.aman.cricmate.model.Coach
import com.aman.cricmate.model.DailyExercise
import com.aman.cricmate.model.DateWiseStats
import com.aman.cricmate.model.Event
import com.aman.cricmate.model.EventRequest
import com.aman.cricmate.model.LoginRequest
import com.aman.cricmate.model.LoginResponse
import com.aman.cricmate.model.PlayerDetails
import com.aman.cricmate.model.PlayerReviewRequest
import com.aman.cricmate.model.PlayerReviewResponse
import com.aman.cricmate.model.SignupRequest
import com.aman.cricmate.model.StatsData
import com.aman.cricmate.model.TestResult
import com.aman.cricmate.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Date

interface ApiService {

//  Authentication Requests
    @GET("auth/getuser")
    suspend fun getUser(
        @Header("authentication") authToken: String
    ): Response<User>

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @Multipart
    @POST("auth/signup")
    suspend fun signup(
        @Part profilePhoto: MultipartBody.Part?,
       @Part("name") name: RequestBody,
       @Part("email") email: RequestBody,
       @Part("password") password: RequestBody,
       @Part("role") role: RequestBody
    ): Response<LoginResponse>

//    Coaches Request
    @GET("coaches")
    suspend fun getAllCoaches():Response<List<Coach>>

    @GET("coaches/players")
    suspend fun getAllPlayers(
        @Header("authentication") authToken: String
    ): Response<List<User>>

//    Players Request
    @POST("player/adddetails")
    suspend fun addDetail(
        @Header("authentication") authToken: String,
        @Body addDetailsRequest: AddDetailsRequest
    ): Response<AddDetailsResponse>

    @GET("player/getDetails/{userId}")
    suspend fun getDetails(@Path("userId") userId: String): Response<PlayerDetails>

//    Player Test Data Request
    @GET("test/get-data")
    suspend fun getPlayerTestData(
        @Query("fieldName") fieldName: String,
        @Query("filter") filter: String,
        @Query("userId") userId: String
    ): Response<TestResult>

    @POST("test/add-data")
    suspend fun addPlayerTestData(
        @Header("authentication") authToken: String,
        @Body testResultRequest: TestResult
    ): Response<ApiResponse>

//   Events
    @POST("event/create")
    suspend fun createEvent(
        @Header("authentication") authToken: String,
        @Body eventData: EventRequest
    ): Response<ApiResponse>

    @GET("event/getAll")
    suspend fun getAllEvents(): Response<List<Event>>

    @GET("event/getEvent/{eventId}")
    suspend fun getEventById(
        @Path("eventId") eventId:String
    ): Response<Event>

    @POST("event/{eventId}/apply")
    suspend fun applyToEvent(
        @Header("authentication") authToken: String,
        @Path("eventId") eventId: String
    ): Response<ApiResponse>

    @GET("event/{eventId}/applicants")
    suspend fun getApplicants(@Path("eventId") eventId: String): Response<List<User>>
//   Player Review
    @POST("review/add-review")
    suspend fun addPlayerReview(
        @Header("authentication") authToken: String,
        @Body playerReviewRequest: PlayerReviewRequest
    ): Response<ApiResponse>

    @GET("review/get-review/{filter}")
    suspend fun getPlayerReview(
        @Header("authentication") authToken: String,
        @Path("filter") filter: String
    ): Response<PlayerReviewResponse>

//    Exercises
    @GET("exercise/today")
    suspend fun getTodaysExercise(): Response<DailyExercise>


    @GET("firebase/data/{userId}/{sessionId}")
    suspend fun analyseBall(
        @Path("userId") userId: String,
        @Path("sessionId") sessionId: String
    ): Response<ApiResponse>

//    Ball API
    @GET("ball/{userId}/{date}")
    suspend fun getBallsByDate(
        @Path("userId") userId: String,
        @Path("date") date: Date
    ): Response<List<BallResponse>>

    @GET("ball/{userId}/stats/{date}")
    suspend fun getDateWiseStats(
        @Path("userId") userId: String,
        @Path("date") date: Date
    ): Response<StatsData>

    @GET("ball/{id}")
    suspend fun getBallById(
        @Path("id") id: String
    ): Response<BallResponse>

    @GET("ball/{userId}/stats/summary/{filter}")
    suspend fun getStatsByFilter(
        @Path("userId") userId: String,
        @Path("filter") filter: String
    ): Response<DateWiseStats>

}


// 4. Ball data

