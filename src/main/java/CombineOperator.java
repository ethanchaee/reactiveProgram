import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Action;
import javafx.util.Pair;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class CombineOperator {

    public void zip() {
        String[] shapes = {"BALL", "PENTAGON", "STAR"};
        String[] colorTriangles = {"2-T", "6-T", "4-T"};

        Observable<String> source = Observable.zip(
                Observable.fromArray(shapes).map(Shape::getSuffix),
                Observable.fromArray(colorTriangles).map(Shape::getColor),
                (suffix, color) -> color + suffix);
        source.subscribe(Log::i);
    }

    //combineNumbers
    public void combineNumbers() {
        Observable<Integer> source = Observable.zip(
                Observable.just(100, 200, 300),
                Observable.just(1, 2, 3),
                Observable.just(10, 20, 30), (a, b, c) -> a + b + c);

        source.subscribe(System.out::println);
    }

    //combineInterval
    public void combineInterval() {
        CommonUtils.exampleStart();
        Observable<Integer> source = Observable.zip(
                Observable.just(100, 200, 300),
                Observable.interval(1000L, TimeUnit.MILLISECONDS),
                (value, i) -> value);

        source.subscribe(Log::it);
        CommonUtils.sleep(10000);
    }

    //combineCalculate
    public void combineCalculate() {
        Integer[] data = {
                100, //
                300   //
        };

        Observable<Integer> basePrice = Observable.fromArray(data)
                .map(val -> {
                    if (val <= 200) return 910;
                    if (val <= 400) return 1_600;
                    return 7_300;
                });
        Observable<Integer> usagePrice = Observable.fromArray(data)
                .map(val -> {
                            double series1 = Math.min(200, val) * 93.3;
                            double series2 = Math.min(200, Math.max(val - 200, 0)) * 187.9;
                            double series3 = Math.min(0, Math.max(val - 400, 0)) * 280.65;
                            double sum = series1 + series2 + series3;
                            return (int) sum;
                        }
                );

        Observable<Integer> source = Observable.zip(
                basePrice,
                usagePrice,
                (v1, v2) -> (v1 + v2)
        );
        source.map(val -> new DecimalFormat("#,###").format(val))
                .subscribe(val -> {
                            int index = 0;
                            StringBuilder sb = new StringBuilder();
                            sb.append("Usage : " + data[index] + " kwh =>");
                            sb.append("Price : " + val + "원");
                            Log.i(sb.toString());

                            index++;
                        }
                );

    }

    public void zipAdvanced() {
        Integer[] data = {
                100, //
                300   //
        };

        Observable<Integer> basePrice = Observable.fromArray(data)
                .map(val -> {
                    if (val <= 200) return 910;
                    if (val <= 400) return 1_600;
                    return 7_300;
                });
        Observable<Integer> usagePrice = Observable.fromArray(data)
                .map(val -> {
                            double series1 = Math.min(200, val) * 93.3;
                            double series2 = Math.min(200, Math.max(val - 200, 0)) * 187.9;
                            double series3 = Math.min(0, Math.max(val - 400, 0)) * 280.65;
                            double sum = series1 + series2 + series3;
                            return (int) sum;
                        }
                );



        @NonNull Observable<Pair> source = Observable.zip(
                basePrice,
                usagePrice,
                Observable.fromArray(data),
                (v1, v2, i) -> new Pair(i, v1 + v2));


        source.subscribe(
                val -> {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Usage : " + val.getKey() + " kwh =>");
                    sb.append("Price : " + new DecimalFormat("#,###").format(val.getValue()) + "원");
                    Log.i(sb.toString());
                }
        );
    }

    //zipWithNumber
    public void zipWithNumber(){
        Observable<Integer> source = Observable.zip(
                Observable.just(100, 200, 300),
                Observable.just(10, 20, 30),
                (a,b) -> a+b)
                .zipWith(Observable.just(1,2,3), (ab, c) -> ab + c);
        source.subscribe(Log::i);
    }

    //combineLatest
    public void combineLatest(){
        String[] data1 = {"6", "7", "4", "2"};
        String[] data2 = {"DIAMOND", "STAR", "PENTAGON"};

        Observable<String> source = Observable.combineLatest(
                Observable.fromArray(data1)
                        .zipWith(Observable.interval(100L, TimeUnit.MILLISECONDS),
                                (shape, notUsed) -> Shape.getColor(shape)),
                Observable.fromArray(data2)
                        .zipWith(Observable.interval(150L, TimeUnit.MILLISECONDS),
                                (shape, notUsed) -> Shape.getSuffix(shape)), (v1, v2) -> v1 + v2);
        source.subscribe(Log::i);
        CommonUtils.sleep(1000);
    }

    //merge
    public void merge(){
        String [] data1 = {"1","3"};
        String [] data2 = {"2","4", "6"};

        Observable<String> source1 = Observable.interval(0L, 100L, TimeUnit.MICROSECONDS)
                .map(Long::intValue)
                .map(idx-> data1[idx])
                .take(data1.length);
        Observable<String> source2 = Observable.interval(50L, TimeUnit.MILLISECONDS)
                .map(Long::intValue)
                .map(idx-> data2[idx])
                .take(data2.length);

        Observable<String> source = Observable.merge(source1, source2);

        source.subscribe(Log::i);
        CommonUtils.sleep(1000);


    }
    //conCat
    public void conCat(){
        Action onCompleteAction = ()-> Log.d("onComplete()");
        String [] data1 = {"1","3", "5"};
        String [] data2 = {"2","4", "6"};

        Observable<String> source1 = Observable.fromArray(data1)
                .doOnComplete(onCompleteAction);
        Observable<String> source2 = Observable.interval(100L, TimeUnit.MILLISECONDS)
                .map(Long::intValue)
                .map(idx-> data2[idx])
                .take(data2.length)
                .doOnComplete(onCompleteAction);

        Observable<String>
                source = Observable.concat(source1, source2)
                .doOnComplete(onCompleteAction);
        source.subscribe(Log::i);
        CommonUtils.sleep(1000);
    }
}
