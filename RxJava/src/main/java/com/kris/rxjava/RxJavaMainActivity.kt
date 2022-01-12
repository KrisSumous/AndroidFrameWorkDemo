package com.kris.rxjava

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.disposables.DisposableContainer
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.lang.Exception
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future
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


    fun click_4(view: View) {
        /**
         * create: 使用一个函数从头创建一个Observable
         */
        /*Observable.create(ObservableOnSubscribe<Int> {
            try {
                if (!it.isDisposed) {
                    for (i in 1..10) {
                        it.onNext(i)
                    }
                    it.onComplete()
                }
            } catch (e: Exception) {
                it.onError(e)
            }
        }).subscribe( {
            Log.e("kris", "Next $it")
        }, {
            Log.e("kris", "Error $it")
        }, {
            Log.e("kris", "Sequence Complete")
        })*/

        /**
         * just: 将一个或多个对象转换成发射这个或这些对象的一个observable
         */
        /*Observable.just("Hello just")
            .subscribe {
                Log.e("kris", it)
            }
        Observable.just(1, 2, 3, 4 , 5, 6, 7, 8, 9, 10)
            .subscribe({
                Log.e("kris", "Next $it")
            }, {
                Log.e("kris", "Error $it")
            }, {
                Log.e("kris", "Sequence Complete")
            })*/

        /**
         * from: 将一个Iterable、一个Future或者一个数组转换成一个Observable
         */

        /*Observable.fromArray("hello", "from")
            .subscribe {
                Log.e("kris", it)
            }

        val list = ArrayList<Int>()
        for (i in 1..10) {
            list.add(i)
        }
        Observable.fromIterable(list).subscribe({
            Log.e("kris", "Next $it")
        }, {
            Log.e("kris", "Error $it")
        }, {
            Log.e("kris", "Sequence Complete")
        })

        val executorService = Executors.newCachedThreadPool();
        val future = executorService.submit(Callable {
            Log.e("kris", "模拟一些耗时的任务")
            Thread.sleep(5000)
            return@Callable "ok"
        })

        Observable.fromFuture(future).subscribe {
            Log.e("kris", it)
        }

        Observable.fromFuture(future, 3, TimeUnit.SECONDS)
            .subscribe {
                Log.e("kris", it)
            }*/




        /**
         * defer: 只有当订阅者订阅时才创建Observable，为每个订阅创建一个新的Observable
         */

        /**
         * range: 创建一个发射指定范围的整数序列的Observable
         */

        /**
         * interval: 创建一个按照给定的时间间隔发射整数序列的Observable
         */

        /**
         * timer: 创建一个给定的延时之后发射单个数据的Observable
         */

        /**
         * empty: 创建一个什么都不做直接通知完成的Observable
         */

        /**
         * error: 创建一个什么都不做直接通知错误的Observable
         */

        /**
         * never: 创建一个不发射任何数据的Observable
         */


        /**
         * repeat: 创建一个发射特定数据重复多次的Observable
         */
        /*Observable.just("Hello Repeat")
            .repeat(10)
            .subscribe {
                Log.e("kris", "Next: $it")
            }*/
        /**
         * repeatWhen不是缓存和重放原始数据Observable的数据序列，而是有条件地重新订阅和发射原来的Observable
         */
        /*Observable.range(0, 9).repeatWhen {
            return@repeatWhen Observable.timer(10, TimeUnit.SECONDS)
        }.subscribe {
            Log.e("kris", it.toString())
        }*/
        /**
         * repeatUntil表示直到某个条件就不再重复发射数据
         */
        val startTimeMillis = System.currentTimeMillis()
        Observable.interval(500, TimeUnit.MILLISECONDS)
            .take(5)
            .repeatUntil {
                return@repeatUntil System.currentTimeMillis() - startTimeMillis > 5000
            }.subscribe {
                Log.e("kris", it.toString())
            }

    }


}