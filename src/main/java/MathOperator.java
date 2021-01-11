//수학 연산자
//    implementation "com.github.akarnokd:rxjava3-extensions:3.0.1"

import hu.akarnokd.rxjava3.math.MathFlowable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class MathOperator {

    public void execute() {
        Integer[] data = {1, 2, 3, 4};

        Single<Long> source = Observable.fromArray(data).count();
        source.subscribe(count -> Log.i("count is : " + count));


        Flowable.fromArray(data)
                .to(MathFlowable::max)
                .subscribe(max -> Log.i("max is " + max));

        Flowable.fromArray(data)
                .to(MathFlowable::min)
                .subscribe(max -> Log.i("min is " + max));

        Flowable.fromArray(data)
                .to(MathFlowable::averageFloat)
                .subscribe(max -> Log.i("averageFloat is " + max));

        Flowable.fromArray(data)
                .to(MathFlowable::sumInt)
                .subscribe(max -> Log.i("sumInt is " + max));

    }

}
