package com.lml.demos.demo2;

import com.lml.demos.demo1.util.PriceResult;
import com.lml.demos.demo2.util.Person;
import com.lml.demos.demo2.util.PersonUtil;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import static com.lml.demos.demo1.service.ShopServiceImpl.computeRealPriceTT;
import static com.lml.demos.demo1.util.ShopUtil.getDisCount;
import static com.lml.demos.demo1.util.ShopUtil.getPrice;

/**
 * @author ：limaolin
 * @date ：Created in 2022/11/16 15:23
 * @description：CompletableFuture常用api练习
 * @modified By：
 */

public class ApiTest {

    /**
     * - 1.提供**runAsync** 和 **supplyAsync**方法来创建一个异步任务，
     * - runAsync方法不支持返回值。
     * - supplyAsync可以支持返回值。
     */
    @Test
    public void t1() throws ExecutionException, InterruptedException {
        //使用runAsync，无返回值。
        CompletableFuture.runAsync(() -> System.out.println(1 + 1));
        //使用supplyAsync，有返回值
        Person person = CompletableFuture.supplyAsync(() -> new Person("小李", 18, null)).get();
        System.out.println(person);
    }

    /**
     * 2. 计算结果完成时的回调方法
     * 比如一个人玩完游戏身上很脏，需要洗完澡才能上床睡觉
     */
    @Test
    public void t2() throws Exception {
        Person person = new Person("小宝", 22, null);
        ExecutorService threadPool = Executors.newSingleThreadExecutor();
        Person newPerson = CompletableFuture.supplyAsync(() -> {
                    System.out.println("当前线程是:" + Thread.currentThread().getName());
                    return PersonUtil.playGame(person);
                })
                .whenComplete((person1, throwable) -> {
                    System.out.println("当前线程是:" + Thread.currentThread().getName());
                    PersonUtil.takeShower(person1);
                }).get();

        System.out.println(newPerson);
        //指定whenCompleteAsync执行的线程池，可以看到action在我们自定义的线程池中执行
        Person newPerson1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("当前线程是:" + Thread.currentThread().getName());
            return PersonUtil.playGame(person);
        }).whenCompleteAsync((person1, throwable) -> {
            PersonUtil.takeShower(person1);
            System.out.println("当前线程是:" + Thread.currentThread().getName());
        }, threadPool).get();
        System.out.println(newPerson1);
    }

    /**
     * 3.当一个线程依赖另一个线程的结果时，可以使用thenApply使得两个线程串行
     */
    @Test
    public void t3() throws ExecutionException, InterruptedException {
        Person person = CompletableFuture
                .supplyAsync(() -> new Person("小宝", 22, null))
                .thenApply(person1 -> {
                    person1.setState("阳光向上的");
                    return person1;
                })
                .get();
        System.out.println(person);
    }

    /**
     * 4.- handler和thenApply方法处理方式和用途基本一样。
     * - handler在任务完成之后再执行，可以处理带异常的任务。thenApply只能执行正常的任务，任务出现异常不会继续执行。
     */
    @Test
    public void t4() throws ExecutionException, InterruptedException {
        Person person = CompletableFuture
                .supplyAsync(() -> new Person("小宝", 22, null))
                .handle((person1, throwable) -> {
                    if (throwable == null) {
                        person1.setState("健康的");
                        return person1;
                    } else {
                        System.out.println(throwable.getMessage());
                        return null;
                    }
                })
                .get();
        System.out.println(person);
    }

    /**
     * ### 5. thenAccept 消费处理结果
     * 接收上一个任务的结果，并消费，无返回结果。
     * -
     */
    @Test
    public void t5() {
        CompletableFuture.supplyAsync(() -> new Person("小张", 0, "开心的"))
                .thenAccept(person -> {
                    person.setAge(18);
                    System.out.println(person);
                });
    }

    /**
     * ### 6.thenRun方法
     * - 与thenAccept方法相似，但不依赖上一个任务的结果。
     */
    @Test
    public void t6() {
        Person person = new Person("小张", 11, null);
        CompletableFuture.supplyAsync(() -> PersonUtil.sleep(person))
                .thenRun(() -> System.out.println("run-run-run"));
    }

    @Test
    public void t7() throws ExecutionException, InterruptedException {
        //getPrice方法为得到价格 getDisCount为得到折扣。combine合并这两个结果并计算真实价格
        CompletableFuture<PriceResult> pddFuture = CompletableFuture
                .supplyAsync(() -> getPrice("PDD"))
                .thenCombine(CompletableFuture.supplyAsync(() -> getDisCount("PDD")), (price, disCount) -> computeRealPriceTT("PDD", disCount, price));

        CompletableFuture<PriceResult> jdFuture = CompletableFuture
                .supplyAsync(() -> getPrice("JD"))
                .thenCombine(CompletableFuture.supplyAsync(() -> getDisCount("JD")), (price, disCount) -> computeRealPriceTT("JD", disCount, price));

        CompletableFuture<PriceResult> tbFuture = CompletableFuture
                .supplyAsync(() -> getPrice("TB"))
                .thenCombine(CompletableFuture.supplyAsync(() -> getDisCount("TB")), (price, disCount) -> computeRealPriceTT("TB", disCount, price));

        PriceResult result = Stream.of(pddFuture.get(), jdFuture.get(), tbFuture.get()).min(((o1, o2) -> o1.getPrice().compareTo(o2.getPrice()))).get();
        System.out.println("最低价格平台为" + result.getName() + "价格为" + result.getPrice());
    }

    /**
     * 当两个CompletionStage都执行完成后，把结果一块交给thenAcceptBoth来进行消耗
     */
    @Test
    public void t8() throws ExecutionException, InterruptedException {
        CompletableFuture
                .supplyAsync(() -> getPrice("PDD"))
                .thenAcceptBoth(CompletableFuture.supplyAsync(() -> getDisCount("PDD")),
                        (price, disCount) -> System.out.println(price * disCount)).get();
    }


    @Test
    public void t9() throws ExecutionException, InterruptedException {
        CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("速度快的任务");
                Thread.sleep(1000);
                return 1;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).acceptEitherAsync(CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("速度慢的任务");
                Thread.sleep(2000);
                return 2;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }), integer -> System.out.println(integer * 3)).get();
    }


    @Test
    public void t10() throws ExecutionException, InterruptedException {
        CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("快的任务");
                return 1;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).runAfterEither(CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
                System.out.println("慢的任务");
                return 2;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }), () -> {
            System.out.println("执行runnable任务");
        }).get();
    }


    @Test
    public void t11() throws ExecutionException, InterruptedException {
        CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("快的任务");
                return 1;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).runAfterBoth(CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
                System.out.println("慢的任务");
                return 2;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }), () -> {
            System.out.println("执行runnable任务");
        }).get();
    }

    @Test
    public void t12() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> getPrice("PDD"));
        Double price = future.thenCompose(integer -> CompletableFuture.supplyAsync(() -> getDisCount("PDD") * integer)).get();
        System.out.println(price);
    }
}