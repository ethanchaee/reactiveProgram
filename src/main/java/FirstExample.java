import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Consumer;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import javax.crypto.interfaces.PBEKey;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FirstExample {
    private void emit() {
        Observable.just("Hello", "RxJava 3!!")
                .subscribe(System.out::println);
    }

    private void justEmit() {
        Observable.just(1, 2, 3, 4, 5, 6)
                .subscribe(System.out::println);
    }

    /**
     * create 사용시 주의사항
     * 1. Observable 이 구독해지 되었을 때, 등록된 콜백을 모두 삭제 -> 메모리 릭 발생할 수 있음
     * 2. 구독자가 구독하는 동안에만 onNext, onComplete 이벤트를 호출
     * 3. 에러가 발생했을 경우 오직 onError 이벤트로만 에러를 전달해야 함
     * 4. 배압(backPressure) 를 직접 처리해야
     */
    private void createEmit() {
        Observable<Integer> source = Observable.create(
                (ObservableEmitter<Integer> emitter) -> {
                    emitter.onNext(100);
                    emitter.onNext(200);
                    emitter.onNext(300);
                    emitter.onComplete();
                }
        );
        //메서드 레퍼런스 (in Java8)
        source.subscribe(System.out::println);
        //람다
        source.subscribe(data -> System.out.println(data));
        //익명 객체나 멤버 변수
        source.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Throwable {
                System.out.println(integer);
            }
        });
    }

    private void fromArrayEmit() {
        Integer[] arr = {100, 200, 300};
        Observable<Integer> source = Observable.fromArray(arr);
        source.subscribe(System.out::println);
    }

    //int 가 아니라 Integer 로 해야
    private Integer[] toIntegerArray(int[] arr) {
        return Arrays.stream(arr).boxed().toArray(Integer[]::new);
    }

    //interator 로 가능한 클래스 -> ArrayList, HashSEt, ArrayBolckingQueue, LinkedList, Stack, TreeSEtm Vector
    //HashMap 없음
    private void listExample() {
        List<String> names = new ArrayList<>();
        names.add("Jerry");
        names.add("William");
        names.add("Bob");

        Observable<String> source = Observable.fromIterable(names);
        source.subscribe(System.out::println);
    }

    private void setExample() {
        Set<String> names = new HashSet<>();
        names.add("Jerry set");
        names.add("William");
        names.add("William");

        Observable<String> source = Observable.fromIterable(names);
        source.subscribe(System.out::println);
    }

    private void fromCallable() {
        Callable<String> callable = () -> {
            Thread.sleep(1000);
            return "Hello callable";
        };
        Observable<String> source = Observable.fromCallable(callable);
        source.subscribe(System.out::println);
    }

    private void fromFuture() {
        Future<String> future = Executors.newSingleThreadExecutor().submit(() -> {
            Thread.sleep(1000);
            return "Hello Future";
        });

        Observable<String> source = Observable.fromFuture(future);
        source.subscribe(System.out::println);
    }

    private void fromPublisher() {
        //Java9 의 Flow API 의 일부
       Publisher<String> publisher = (Subscriber<? super String> s) -> {
            s.onNext("test publisher");
            s.onComplete();
        };
        Observable<String> source = Observable.fromPublisher(publisher);
        source.subscribe(System.out::println);
    }

    // Single : Observable 의 특수한 형태
    // 원래 Observable 은 데이터를 무한한게 발행할 수 있지만,
    // Single 은 오직 1개의 데이터만 발행하도록 한정함
    // 보통 결과가 유일한 서버 API 호출을 할 때 유용하게 사용할 수 있
    private void just() {
        Single<String> just = Single.just("just");
        just.subscribe(System.out::println);
    }

    private void observableToSingle(){
        Observable<String> source = Observable.just("Hello Single");
        Single.fromObservable(source).subscribe(System.out::println);

        Observable.just("Hello Single")
                .single("default value")
                .subscribe(System.out::println);

        String [] colors = {"RED", "BLUE", "GREEN"};
        Observable.fromArray(colors)
                .first("default value")
                .subscribe(System.out::println);

        Observable.empty()
                .single("default value")
                .subscribe(System.out::println);

    }

    public static void main(String[] args) {
        FirstExample firstExample = new FirstExample();

        firstExample.observableToSingle();
    }
}
