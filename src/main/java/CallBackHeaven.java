import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CallBackHeaven {
    private static final String FIRST_URL = "https://api.github.com/zen";
    private static final String SECOND_URL = CommonUtils.GITHUB_ROOT + "/samples/callback_hell";

    public void usingConcat(){
        CommonUtils.exampleStart();
        Observable<String> source = Observable.just(FIRST_URL)
                .subscribeOn(Schedulers.io())
                .map(OkHttpHelper::get)
                .concatWith(Observable.just(SECOND_URL).map(OkHttpHelper::get));

        source.subscribe(Log::it);
        CommonUtils.sleep(5000);
    }

    public void usingZip(){
        CommonUtils.exampleStart();

        Observable<String> first = Observable.just(FIRST_URL)
                .subscribeOn(Schedulers.io())
                .map(OkHttpHelper::get);
        Observable<String> second = Observable.just(SECOND_URL)
                .subscribeOn(Schedulers.io())
                .map(OkHttpHelper::get);

        Observable.zip(first, second, (a,b) -> ("\n>>" + a + "\n>>" + b))
                .subscribe(Log::it);

        CommonUtils.sleep(5000);
    }

    public static void main(String[] args) {
        CallBackHeaven demo = new CallBackHeaven();
        demo.usingConcat();
    }
}
