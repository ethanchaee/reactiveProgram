import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observables.GroupedObservable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 생성연산자 -> 데이터의 흐름을 만드는 것
 * just(), fromXXX(), create(), interval(), range(),
 * timer(), intervalRange(), defer(), repeat()
 */

public class OperatorAdvancedExample {

    public void interval() {
        CommonUtils.exampleStart();
        Observable<Long> source = Observable.interval(0, 100L, TimeUnit.MILLISECONDS)
                .map(data -> (data + 1) * 100)
                .take(5);

        source.subscribe(Log::it);
        CommonUtils.sleep(1000);
    }

    public void timer() {
        CommonUtils.exampleStart();
        Observable<Long> source = Observable.timer(1000L, TimeUnit.MILLISECONDS)
                .map(data -> (data + 1) * 100);

        source.subscribe(Log::it);
        CommonUtils.sleep(1000);
    }

    public void range() {
        @NonNull Observable<Integer> source = Observable.range(1, 4)
                .filter(number -> number % 2 == 0);

        source.subscribe(Log::it);
    }

    public void intervalRange() {

        @NonNull Observable<Long> source = Observable.intervalRange(1, 10, 0, 1000L, TimeUnit.MICROSECONDS);
        source.subscribe(Log::it);
        CommonUtils.sleep(1000);
    }

    public void intervalRangeCustom() {
        CommonUtils.exampleStart();
        Observable<Long> source =
                Observable.interval(100L, TimeUnit.MILLISECONDS)
                        .map(i -> i + 1)
                        .take(5);
        source.subscribe(Log::it);
        CommonUtils.sleep(1000);
    }

    Iterator<String> colors = Arrays.asList("1", "3", "5", "6").iterator();

    //defer
    public void defer() {
        Callable<Observable<String>> supplier = () -> getObservable();
        //Observable source = Observable.defer(supplier);
        Observable source = getObservable();

        source.subscribe(val -> System.out.println("Subscriber #1 " + val));
        source.subscribe(val -> System.out.println("Subscriber #2 " + val));

        CommonUtils.sleep(1000);
    }

    private Observable<String> getObservable() {
        if (colors.hasNext()) {
            String color = colors.next();
            return Observable.just(
                    Shape.getString(color, Shape.BALL),
                    Shape.getString(color, Shape.RECTANGLE),
                    Shape.getString(color, Shape.PENTAGON));
        }
        return Observable.empty();
    }

    public void repeat() {
        String[] balls = {"1", "3", "5"};
        Observable<String> source = Observable.fromArray(balls)
                .repeat(3);

        source.doOnComplete(() -> System.out.println("onComplete"))
                .subscribe(Log::i);
    }

    public void repeatForServer() {
        CommonUtils.exampleStart();
        String serverUrl = "https://api.github.com/zen";
        Observable.timer(2, TimeUnit.SECONDS)
                .map(val -> serverUrl)
                .map(OkHttpHelper::get)
                .repeat()
                .subscribe(res -> Log.it("Ping result : " + res));
        CommonUtils.sleep(10000);
    }

    public void contactMap() {
        CommonUtils.exampleStart();
        String[] balls = {"1", "3", "5"};

        Observable<String> source = Observable.interval(100L, TimeUnit.MILLISECONDS)
                .map(Long::intValue)
                .map(idx -> balls[idx])
                .take(balls.length)
                .concatMap(ball -> Observable.interval(200L, TimeUnit.MILLISECONDS)
                        .map(notUsed -> ball + "()")
                        .take(2)
                );

        source.subscribe(Log::it);
        CommonUtils.sleep(2000);

    }

    public void compareContactMapWithFlatmap() {
        CommonUtils.exampleStart();
        String[] balls = {"1", "3", "5"};

        Observable<String> source = Observable.interval(100L, TimeUnit.MILLISECONDS)
                .map(Long::intValue)
                .map(idx -> balls[idx])
                .take(balls.length)
                .flatMap(ball -> Observable.interval(200L, TimeUnit.MILLISECONDS)
                        .map(notUsed -> ball + "()")
                        .take(2)
                );

        source.subscribe(Log::it);
        CommonUtils.sleep(2000);

    }

    public void switchMap() {
        CommonUtils.exampleStart();
        String[] balls = {"1", "3", "5"};

        Observable<String> source = Observable.interval(50L, TimeUnit.MILLISECONDS)
                .map(Long::intValue)
                .map(idx -> balls[idx])
                .take(balls.length)
                .doOnNext(Log::dt)
                .switchMap(ball -> Observable.interval(10, TimeUnit.MILLISECONDS)
                        .map(notUsed -> ball + "()")
                        .take(2)
                );

        source.subscribe(Log::it);
        CommonUtils.sleep(2000);
    }

    public void groupBy() {
        String[] objs = {"6", "4", "2-T", "2", "6-T", "4-T"};
        Observable<GroupedObservable<String, String>> source
                = Observable.fromArray(objs).groupBy(Shape::getShape);
        source.subscribe(obj -> {
            obj.subscribe(
                    val -> System.out.println("GROUP : " + obj.getKey() + "\t\t value:" + val)
            );
        });
    }

    public void groupByWithFilter() {
        String[] objs = {"6", "4", "2-T", "2", "6-T", "4-T"};
        Observable<GroupedObservable<String, String>> source
                = Observable.fromArray(objs).groupBy(Shape::getShape)
                .filter(it -> it.getKey().equals(Shape.BALL));
        source.subscribe(obj -> {
            obj.subscribe(
                    val -> System.out.println("GROUP : " + obj.getKey() + "\t\t value:" + val)
            );
        });
    }

    //scan
    public void scan() {
        String[] balls = {"1", "3", "5"};
        Observable<String> source = Observable.fromArray(balls)
                .scan((data1, data2) -> data2 + "(" + data1 + ")");

        source.subscribe(System.out::println);
    }


}
