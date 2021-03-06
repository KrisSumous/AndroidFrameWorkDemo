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
                // ????????????????????????Observable?????????????????????
            }.doOnLifecycle({
                // ?????????????????????????????????????????????????????????
            }, {

            }).doOnNext {
                // ????????????Observable???????????????????????????????????????????????????Consumer???????????????????????????
                // ???????????????subscribe???????????????????????????
            }.doOnEach {
                // ????????????Observable?????????????????????????????????????????????????????????onNext????????????onError???onCompleted
            }.doAfterNext {
                // ???onNext??????????????????doOnNext()??????onNext????????????
            }.doOnComplete {
                // ???????????????Observable?????????????????????onComplete???????????????
            }.doFinally {
                // ??????????????????Observable?????????????????????????????????????????????????????????????????????doFinally?????????
                // doAfterTerminate?????????
            }.doAfterTerminate {
                // ????????????Action, ???Observable??????onComplete???onError?????????
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
        // 1???publish????????? ????????????connect
        /*val observable = Observable.create<Long> {
            Observable.interval(10, TimeUnit.MILLISECONDS, Schedulers.computation())
                .take(100)
                .subscribe(it::onNext)
        }.observeOn(Schedulers.newThread()).publish()
        observable.connect()

        observable.subscribe(subscribe_1)
        observable.subscribe(subscribe_2)*/

        // 2???Subject(???????????????)/Processor(????????????)
        val observable = Observable.create<Long> {
            Observable.interval(10, TimeUnit.MILLISECONDS, Schedulers.computation())
                .take(100)
                .subscribe(it::onNext)
        }.observeOn(Schedulers.newThread())

        val subject = PublishSubject.create<Long>()
        subject.toSerialized() //??????????????????
        observable.subscribe(subject)

        subject.subscribe(subscribe_1)
        subject.subscribe(subscribe_2)

    }


    fun click_4(view: View) {
        /**
         * create: ????????????????????????????????????Observable
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
         * just: ?????????????????????????????????????????????????????????????????????observable
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
         * from: ?????????Iterable?????????Future?????????????????????????????????Observable
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
            Log.e("kris", "???????????????????????????")
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
         * defer: ????????????????????????????????????Observable????????????????????????????????????Observable
         */

        /**
         * range: ????????????????????????????????????????????????Observable
         */

        /**
         * interval: ????????????????????????????????????????????????????????????Observable
         */

        /**
         * timer: ??????????????????????????????????????????????????????Observable
         */

        /**
         * empty: ????????????????????????????????????????????????Observable
         */

        /**
         * error: ????????????????????????????????????????????????Observable
         */

        /**
         * never: ????????????????????????????????????Observable
         */


        /**
         * repeat: ?????????????????????????????????????????????Observable
         */
        /*Observable.just("Hello Repeat")
            .repeat(10)
            .subscribe {
                Log.e("kris", "Next: $it")
            }*/
        /**
         * repeatWhen?????????????????????????????????Observable??????????????????????????????????????????????????????????????????Observable
         */
        /*Observable.range(0, 9).repeatWhen {
            return@repeatWhen Observable.timer(10, TimeUnit.SECONDS)
        }.subscribe {
            Log.e("kris", it.toString())
        }*/
        /**
         * repeatUntil???????????????????????????????????????????????????
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