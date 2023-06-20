package com.jhlee.cleantest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivity : AppCompatActivity(), MainContract.View {
    private lateinit var presenter: MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<AppCompatButton>(R.id.go_test_activity_btn).setOnClickListener {
            Toast.makeText(this, "test", Toast.LENGTH_SHORT).show()
            Intent(this, TestPresenterActivity::class.java).apply {
                startActivity(this)
            }
        }

        // Dependency Injection
        val userRepository = UserRepositoryImpl(UserApiImpl())
        val schedulerProvider = SchedulerProviderImpl()
        presenter = MainPresenter(this, userRepository, schedulerProvider)

        presenter.loadData()
    }

    override fun showData(data: List<User>) {
        // 데이터를 보여주는 로직 구현
    }
}

interface MainContract {
    interface View {
        fun showData(data: List<User>)
    }

    interface Presenter {
        fun loadData()
    }
}

class MainPresenter(
    private val view: MainContract.View,
    private val userRepository: UserRepository,
    private val schedulerProvider: SchedulerProvider
) : MainContract.Presenter {
    override fun loadData() {
        // 데이터를 가져오는 로직 구현
        val disposal = userRepository.getUsers().subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui()).subscribe({ users ->
                Log.d("jhlee", "view.showData(users) : " + users.toString())
                view.showData(users)
            }, { error -> /* 에러 처리 로직 구현 */ })
    }
}


// Domain Layer
interface UserRepository {
    fun getUsers(): Single<List<User>>
}

class UserRepositoryImpl(private val userApi: UserApi) : UserRepository {
    override fun getUsers(): Single<List<User>> {
        return userApi.getUsers()
    }
}


// Data Layer
interface UserApi {
    fun getUsers(): Single<List<User>>
}

class UserApiImpl : UserApi {
    override fun getUsers(): Single<List<User>> {
        val dummyUsers = listOf(
            User(1, "John", "john@example.com"),
            User(2, "Emma", "emma@example.com"),
            User(3, "Daniel", "daniel@example.com")
        )
        return Single.just(dummyUsers)
    }
}


// Model
data class User(
    val id: Int, val name: String, val email: String
)


// Utility
interface SchedulerProvider {
    fun io(): Scheduler
    fun ui(): Scheduler
}

class SchedulerProviderImpl : SchedulerProvider {
    override fun io(): Scheduler {
        return Schedulers.io()
    }

    override fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}