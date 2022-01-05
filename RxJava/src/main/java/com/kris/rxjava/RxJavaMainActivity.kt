package com.kris.rxjava

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.disposables.DisposableContainer
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class RxJavaMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rxjava_activity_main)
    }

    fun click_1(view: View) {
        Observable.create(ObservableOnSubscribe<String> { emitter ->
            emitter.onNext("Hello World!")
        }).subscribe {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    fun click_2(view: View) {

        Observable.just("Hello World").subscribe({
            Log.e("kris", it)
        }, {
            Log.e("kris", it.message.toString())
        }, {
            Log.e("kris", "onComplete")
        }, object : DisposableContainer {
            override fun add(d: Disposable?): Boolean {
                Log.e("kris", "add")
                return true
            }

            override fun remove(d: Disposable?): Boolean {
                Log.e("kris", "remove")
                return true
            }

            override fun delete(d: Disposable?): Boolean {
                Log.e("kris", "delete")
                return true
            }

        })


        Observable.just("Hello World").subscribe(object : Observer<String> {
            override fun onSubscribe(d: Disposable?) {

            }

            override fun onNext(t: String?) {
            }

            override fun onError(e: Throwable?) {
            }

            override fun onComplete() {
            }

        })
        Observable.just("Hello World")
            .doOnSubscribe {
                // 一旦观察者订阅了Observable，他就会被调用
            }.doOnLifecycle({
                // 可以在观察者订阅之后，设置是否取消订阅
            }, {

            }).doOnNext {
                // 它产生的Observable每发射一项数据就会调用它一次，它的Consumer接受发射的数据项。
                // 一般用于在subscribe之前对数据进行处理
            }.doOnEach {
                // 它产生的Observable每发射一项数据就会调用它一次，不仅包括onNext，还包括onError和onCompleted
            }.doAfterNext {
                // 在onNext之后执行，而doOnNext()是在onNext之前执行
            }.doOnComplete {
                // 当他产生的Observable在正常终止调用onComplete时会被调用
            }.doFinally {
                // 在当它产生的Observable终止之后会被调用，无论是正常终止还是异常终止。doFinally优先于
                // doAfterTerminate的调用
            }.doAfterTerminate {
                // 注册一个Action, 当Observable调用onComplete或onError时触发
            }

    }

    fun click_3(view: View) {
        val subscribe_1 = Consumer<Long> {
            Log.e("kris", "subscribe_1 $it")
        }
        val subscribe_2 = Consumer<Long> {
            Log.e("kris", "subscribe_2 $it")
        }
        val subscribe_3 = Consumer<Long> {
            Log.e("kris", "subscribe_3 $it")
        }

        // Cold Observable
        /*val observable = Observable.create<Long> {
            Observable.interval(10, TimeUnit.MILLISECONDS, Schedulers.computation())
                .take(100)
                .subscribe(it::onNext)
        }.observeOn(Schedulers.newThread())

        observable.subscribe(subscribe_1)
        observable.subscribe(subscribe_2)*/

        // Cold Observable => Hot Observable
        // 1、publish操作符 需要调用connect
        /*val observable = Observable.create<Long> {
            Observable.interval(10, TimeUnit.MILLISECONDS, Schedulers.computation())
                .take(100)
                .subscribe(it::onNext)
        }.observeOn(Schedulers.newThread()).publish()
        observable.connect()

        observable.subscribe(subscribe_1)
        observable.subscribe(subscribe_2)*/

        // 2、Subject(不支持背压)/Processor(支持背压)
        val observable = Observable.create<Long> {
            Observable.interval(10, TimeUnit.MILLISECONDS, Schedulers.computation())
                .take(100)
                .subscribe(it::onNext)
        }.observeOn(Schedulers.newThread())

        val subject = PublishSubject.create<Long>()
        subject.toSerialized() //保证线程安全
        observable.subscribe(subject)

        subject.subscribe(subscribe_1)
        subject.subscribe(subscribe_2)

    }
}