package com.jhlee.cleantest

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class TestPresenterActivity : AppCompatActivity(), TestContract.View {

    private lateinit var presenter: TestContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_test)
        presenter = TestPresenter(this, BaseBallPlayerRepositoryImpl(BaseBallPlayerApiImpl()), TestSchedulerProviderImpl())
        presenter.loadData()
        Log.d("jhlee", "TestActivity")
    }

    override fun showData(data: List<BaseBallPlayer>) {

    }
}


// Model
data class BaseBallPlayer(val name: String, val position: String)

// Data Layer

interface BaseBallPlayerApi {
    fun getBaseBallPlayer(): Single<List<BaseBallPlayer>>
}

class BaseBallPlayerApiImpl : BaseBallPlayerApi {

    // Single 은 Single은 Observable의 변형된 형태로,
    // 오직 하나의 데이터 항목이나 오류를 방출하는 데이터 스트림을 나타냅니다.
    // Single.just() 연산자는 단일 항목을 방출하는 Single 객체를 생성합니다.

    override fun getBaseBallPlayer(): Single<List<BaseBallPlayer>> {
        val dummyPlayers = listOf(
            BaseBallPlayer("Bae", "SP"),
            BaseBallPlayer("Choi", "RF"),
            BaseBallPlayer("Lee", "1B"),
        )
        // 성공적 인 결과 통지 용도
        return Single.just(dummyPlayers)

        //  오류 상태를 통지 용도
        // return Single.error(Exception("Test"))
    }
}


// Domain Layer
interface BaseBallPlayerRepository {
    fun getBaseBallPlayer(): Single<List<BaseBallPlayer>>
}

class BaseBallPlayerRepositoryImpl(private val baseballApi: BaseBallPlayerApi) :
    BaseBallPlayerRepository {

    override fun getBaseBallPlayer(): Single<List<BaseBallPlayer>> = baseballApi.getBaseBallPlayer()
}

interface TestContract {
    interface View {
        fun showData(data: List<BaseBallPlayer>)
    }

    interface Presenter {
        fun loadData()
    }
}

class TestPresenter(
    private val view: TestContract.View,
    private val baseballRepository: BaseBallPlayerRepository,
    private val testSchedulerProvider: TestSchedulerProvider
) : TestContract.Presenter {

    override fun loadData() {
        // 데이터를 가져오는 로직 구현
        val disposal = baseballRepository.getBaseBallPlayer()
            .subscribeOn(testSchedulerProvider.io())
            .observeOn(testSchedulerProvider.ui())
            .subscribe({ users ->
                Log.d("jhlee", "view.showData(users) : $users")
                view.showData(users)
            }, { error -> Log.d("jhlee", "error : $error") })

        Log.d("jhlee", "TestPresenter loadData")
    }
}

// Utility
interface TestSchedulerProvider {
    fun io(): Scheduler
    fun ui(): Scheduler
}

class TestSchedulerProviderImpl : TestSchedulerProvider {
    override fun io(): Scheduler {
        return Schedulers.io()
    }

    override fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}