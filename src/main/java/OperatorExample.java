import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.functions.Predicate;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OperatorExample {

    String[] balls = {"1", "2", "3", "4", "5"};
    Function<String, String> getDiamond = ball -> ball + " : Ball";

    public void map() {
        Observable<String> source = Observable.fromArray(balls)
                .map(data -> data + ": ball");
        source.subscribe(System.out::println);
    }

    public void mapForFunction() {
        Observable<String> source = Observable.fromArray(balls)
                .map(getDiamond);
        source.subscribe(System.out::println);
    }

    public void mappingType() {
        Function<String, Integer> ballToIndex = ball -> {
            switch (ball) {
                case "RED":
                    return 1;
                case "BLUE":
                    return 2;
                case "GREEN":
                    return 3;
                case "BLACK":
                    return 4;
                default:
                    return 0;
            }
        };
        String[] balls = {"BLUE", "RED", "BLACK", "GREEN", "BLACK"};


        Observable source = Observable.fromArray(balls)
                .map(ballToIndex);

        source.subscribe(System.out::println);
    }

    Function<String, Observable<String>> getDoubleDiamonds =
            ball -> Observable.just(ball + "diamond", ball + "diamond");

    public void flatMap() {
        Observable source = Observable.fromArray(balls)
                .flatMap(getDoubleDiamonds);

        source.subscribe(System.out::println);
    }

    private int getNumber() {
        Scanner in = new Scanner(System.in);
        System.out.println("구구단");
        return Integer.parseInt(in.nextLine());
    }

    public void createGugudan() {
        int number = getNumber();

        for (int i = 1; i < 10; i++) {
            System.out.println(number + " X " + i + " = " + number * i);
        }
    }

    public void createGugudanRx1() {
        int number = getNumber();
        Function<Integer, String> function = i -> number + " X " + i + " = " + number * i;
        Integer[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        Observable<String> source = Observable.fromArray(numbers)
                .map(function);
        source.subscribe(System.out::println);
    }

    public void createGugudanRx2() {
        int number = getNumber();
        Function<Integer, Observable<String>> function = i -> Observable.just(number + " X " + i + " = " + number * i);
        Observable<String> source = Observable.range(1, 9)
                .flatMap(function);
        source.subscribe(System.out::println);
    }

    public void createGugudanRx3() {
        int number = getNumber();
        Observable<String> source = Observable.just(number)
                .flatMap(gugu -> Observable.range(1, 9),
                        (gugu, i) -> gugu + " X " + i + " = " + gugu * i);
        source.subscribe(System.out::println);
    }

    public void createGugudanRx4() {
        int number = getNumber();
        Observable source = Observable.range(1, 9).map(i -> number + " X " + i + " = " + number * i);
        source.subscribe(System.out::println);
    }

    public void createGugudanRx5() {
        int number = getNumber();
        Observable source = Observable.range(1, 9).map(i -> number + " X " + i + " = " + number * i);
        source.subscribe(System.out::println);
    }

    /**
     * first
     * last
     * take
     * takeLast
     * skip
     * skipLast
     */
    public void filter() {
        Predicate<String> filterCircle = obj -> obj.endsWith("CIRCLE");
        String[] objs = {"1 CIRCLE", "2 DIAMOND", "3 TRIANGLE"};
        Observable<String> source = Observable.fromArray(objs)
                .filter(obj -> obj.endsWith("CIRCLE"));
        source.subscribe(System.out::println);
    }

    public void reduce() {
        Maybe<String> source = Observable.fromArray(balls)
                .reduce((ball1, ball2) -> ball2 + "(" + ball1 + ")");

        source.subscribe(System.out::println);
    }

    public void query(){
        List<Pair<String, Integer>> list = new ArrayList<>();
        list.add(new Pair("TV", 2500));
        list.add(new Pair("CAMERA", 300));
        list.add(new Pair("TV", 1600));
        list.add(new Pair("PHONE", 800));
        list.add(new Pair("TV", 3000));

        @NonNull Maybe<Integer> source =
                Observable.fromIterable(list)
                .filter(it -> it.getKey().equals("TV"))
                .map(it -> it.getValue())
                .reduce((data1, data2) -> data2.intValue() + data1.intValue());

        source.subscribe(System.out::println);

    }
}
