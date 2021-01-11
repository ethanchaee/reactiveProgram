
//For example - combineLatest

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.observables.ConnectableObservable;

import java.util.Scanner;

public class ReactiveSum {

    public void run(){
        ConnectableObservable<String> source = userInput();
        Observable<Integer> a = source
                .filter(str -> str.startsWith("a:"))
                .map(str -> str.replace("a:", ""))
                .map(Integer::parseInt);

        Observable<Integer> b = source
                .filter(str-> str.startsWith("b:"))
                .map(str -> str.replace("b:", ""))
                .map(Integer::parseInt);

        Observable.combineLatest(
                a,
                b,
                Integer::sum)
                .subscribe(res-> System.out.println("Result : " + res));
        source.connect();

    }

    private ConnectableObservable<String> userInput() {
        return Observable.create((ObservableEmitter<String> emitter) -> {
            Scanner in = new Scanner(System.in);
            while(true){
                System.out.println("Input:  ");
                String line = in.nextLine();
                emitter.onNext(line);
                if(line.contains("exit")){
                    in.close();
                    break;
                }
            }
        }).publish();
    }


}
