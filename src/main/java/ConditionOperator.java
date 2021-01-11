import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Timed;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ConditionOperator {


    //amb : ambiguous
    public void amb() {
        String[] data1 = {"1", "3", "5"};
        String[] data2 = {"2-R", "5-R"};

        List<Observable<String>> sources = Arrays.asList(
                Observable.fromArray(data1)
                        .doOnComplete(() -> Log.d("Observable #1 : onComplete()")),
                Observable.fromArray(data2)
                        .doOnComplete(() -> Log.d("Observable #2 : onComplete()"))
        );

        Observable.amb(sources)
                .doOnComplete(() -> Log.d("result : onComplete()"))
                .subscribe(Log::i);

        CommonUtils.sleep(1000);
    }

    //takeUntil
    public void takeUntil() {
        String[] data = {"1", "2", "3", "4", "5", "6"};

        Observable<String> source = Observable.fromArray(data)
                .zipWith(Observable.interval(100L, TimeUnit.MILLISECONDS), (val, notUsed) -> val)
                .takeUntil(Observable.timer(500L, TimeUnit.MILLISECONDS));

        source.subscribe(Log::i);
        CommonUtils.sleep(1000);
    }

    //skipUntil
    public void skipUntil() {
        String[] data = {"1", "2", "3", "4", "5", "6"};

        Observable<String> source = Observable.fromArray(data)
                .zipWith(Observable.interval(100L, TimeUnit.MILLISECONDS), (val, notUsed) -> val)
                .skipUntil(Observable.timer(500L, TimeUnit.MILLISECONDS));

        source.subscribe(Log::i);
        CommonUtils.sleep(1000);
    }

    //all
    public void all() {
        String[] data = {"1", "2", "3", "4"};

        Single<Boolean> source = Observable.fromArray(data)
                .map(Shape::getShape)
                .all(Shape.BALL::equals);

        source.subscribe((Consumer<Boolean>) Log::i);
    }


    public void delay() {
        String[] data = {"1", "7", "2", "3", "4"};
        CommonUtils.exampleStart();
        Observable<String> source = Observable.fromArray(data)
                .delay(100L, TimeUnit.MILLISECONDS);
        source.subscribe(Log::it);
        CommonUtils.sleep(1000);
    }

    //timeInterval
    public void execute() {
        String[] data = {"1", "3", "7"};

        CommonUtils.exampleStart();
        Observable<Timed<String>> source = Observable.fromArray(data)
                .delay(item -> {
                    CommonUtils.doSomething();
                    return Observable.just(item);
                })
                .timeInterval();

        source.subscribe(Log::it);
        CommonUtils.sleep(1000);

    }

}
