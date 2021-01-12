import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

import java.util.concurrent.TimeUnit;

public class Debug {
    //basic
    public void basic(){
        String[] orgs = {"1", "3", "5"};
        Observable<String> source = Observable.fromArray(orgs);

        source.doOnNext(data -> Log.d("onNext",data))
                .doOnComplete(()-> Log.d("onComplete"))
                .doOnError(e -> Log.e("onError()", e.getMessage()))
                .subscribe(Log::i);
    }
    //withError
    public void withError(){
        Integer[] divider = {10, 5, 0};

        Observable.fromArray(divider)
                .map(div -> 1000/div)
                .doOnNext(data -> Log.d("onNext()", data))
                .doOnComplete(()-> Log.d("onComplete()"))
                .doOnError(e -> Log.e("onError()", e.getMessage()))
                .subscribe(Log::i);
    }
    //doOnEach
    public void doOnEach(){
        String [] data = {"ONE", "TWO", "THREE"};

        Observable<String> source = Observable.fromArray(data);

        source.doOnEach(noti -> {
            if (noti.isOnNext()) Log.d("onNext()", noti.getValue());
            if (noti.isOnComplete()) Log.d("doOnComplete()");
            if (noti.isOnError()) Log.e("doOnError()", noti.getError().getMessage());
        })
                .subscribe(System.out::println);
    }
    //doOnEachObserver
    public void doOnEachObserver(){

        String[] orgs = {"1", "3", "5"};
        Observable<String> source = Observable.fromArray(orgs);

        source.doOnEach(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                //doOnEach 에서는 onSubscribe 함수가 호출되지 않습니다.
            }

            @Override
            public void onNext(@NonNull String s) {
                Log.d("onNext()", s);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("onError()", e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d("onComplete()");
            }
        }).subscribe(Log::i);
    }

    //doOnSubscribeAndDispose
    public void doOnSubscribeAndDispose(){
        String[] orgs = {"1", "3", "5", "2", "6"};
        Observable<String> source = Observable.fromArray(orgs)
                .zipWith(Observable.interval(100L, TimeUnit.MILLISECONDS), (a,b)-> a)
                .doOnSubscribe(d -> Log.d("onSubscribe()"))
                .doOnDispose(()-> Log.d("onDispose()"));

        Disposable d = source.subscribe(Log::i);

        CommonUtils.sleep(200);
        d.dispose();
        CommonUtils.sleep(300);
    }

    //doOnLifeCycle
    public void doOnLifeCycle(){
        String[] orgs = {"1", "3", "5", "2", "6"};
        Observable<String> source = Observable.fromArray(orgs)
                .zipWith(Observable.interval(100L, TimeUnit.MILLISECONDS), (a,b)->a)
                .doOnLifecycle(
                        d-> Log.d("onSubscribe()"), ()-> Log.d("onDispose()")
                );
        Disposable disposable = source.subscribe(Log::i);

        CommonUtils.sleep(200);
        disposable.dispose();
        CommonUtils.sleep(300);
    }

    //doOnTerminate
    public void doOnTerminate(){
        String[] orgs = {"1", "3", "5"};
        Observable<String> source = Observable.fromArray(orgs);

        source.doOnTerminate(() -> Log.d("onTerminate"))
                .doOnComplete(()-> Log.d("onComplete()"))
                .doOnError(e -> Log.e("onError()", e.getMessage()))
                .subscribe(Log::i);
    }
    
    public void execute(){

    }
    public static void main(String[] args){
        Debug debug = new Debug();
        debug.execute();
    }
}
