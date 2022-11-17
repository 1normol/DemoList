# CompletableFuture







## Future

​	在JDK8之前，要想使用多线程处理带返回值的任务，只能使用callable得到Future对象，Future对象可以监视目标线程调用call的情况，通过get方法阻塞获取结果。

### Futrue常用API

- cancel，取消Callable的执行，当Callable还没有完成时
- get，获得Callable的返回值
- isCanceled，判断是否取消了
- isDone，判断是否完成

### Futrue优缺点

- 一定程度上能够让线程池内的任务异步执行
- 传统回调最大的问题就是不能将控制流分离到不同的事件处理器中。例如主线程等待各个异步执行的线程返回的结果来做下一步操作，则必须阻塞在future.get()的地方等待结果返回。这时候又变成同步了

## CompletableFuture介绍

​	CompletableFuture在JDK8引入，它实现了Future和CompletionStage接口，在保留了Future的优点的同时，弥补了起不足。

![image-20221116141337142](C:\Users\35541\AppData\Roaming\Typora\typora-user-images\image-20221116141337142.png)

## CompletableFuture常用API

### 1. 创建异步任务的方法

- 提供**runAsync** 和 **supplyAsync**方法来创建一个异步任务，

- runAsync方法不支持返回值。

- supplyAsync可以支持返回值。

  ```java
  public static CompletableFuture<Void> runAsync(Runnable runnable)
  public static CompletableFuture<Void> runAsync(Runnable runnable, Executor executor)
  public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier)
  public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier, Executor executor)
  ```

### 	Demo示例

```java
//使用runAsync，无返回值。        
CompletableFuture.runAsync(()-> System.out.println(1+1));
//使用supplyAsync，有返回值
NetMall mall = CompletableFuture.supplyAsync(() -> new NetMall("京东")).get();
System.out.println(mall);
```



### 2. 计算结果完成时的回调方法

- 当一个**completableFuture**完成时，提供**whenComplete**方法，继续执行特定的Action。

- **whenComplete**将在执行当前任务的线程继续执行接下来的action。
- **whenCompleteAsync**后者将把接下来的action继续提交给线程池执行。

```java
public CompletableFuture<T> whenComplete(BiConsumer<? super T,? super Throwable> action)
public CompletableFuture<T> whenCompleteAsync(BiConsumer<? super T,? super Throwable> action)
public CompletableFuture<T> whenCompleteAsync(BiConsumer<? super T,? super Throwable> action, Executor executor)
public CompletableFuture<T> exceptionally(Function<Throwable,? extends T> fn)
```

#### Demo示例

```java
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
```



### 3. thenApply方法

- 当一个线程依赖另一个线程的结果时，可以使用thenApplay使这两个线程串行化

- Function<? super T,? extends U> T：上一个任务返回结果的类型 U：当前任务的返回值类型

```java
public <U> CompletableFuture<U> thenApply(Function<? super T,? extends U> fn)
public <U> CompletableFuture<U> thenApplyAsync(Function<? super T,? extends U> fn)
public <U> CompletableFuture<U> thenApplyAsync(Function<? super T,? extends U> fn, Executor executor)
```

#### Demo示例

```java
     Person person = CompletableFuture
                .supplyAsync(() -> new Person("小宝", 22, null))
                .thenApply(person1 -> {
                    person1.setState("阳光向上的");
                    return person1;
                })
                .get();
        System.out.println(person);
```

### 4. handle方法

- handler和thenApply方法处理方式和用途基本一样。
- handler在任务完成之后再执行，可以处理带异常的任务。thenApply只能执行正常的任务，任务出现异常不会继续执行。

```java
public <U> CompletionStage<U> handle(BiFunction<? super T, Throwable, ? extends U> fn);
public <U> CompletionStage<U> handleAsync(BiFunction<? super T, Throwable, ? extends U> fn);
public <U> CompletionStage<U> handleAsync(BiFunction<? super T, Throwable, ? extends U> fn,Executor executor);
```

#### Demo示例

```java
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
```



### 5. thenAccept 消费处理结果

- 接收上一个任务的结果，并消费，无返回结果。

```java
public CompletionStage<Void> thenRun(Runnable action);
public CompletionStage<Void> thenRunAsync(Runnable action);
public CompletionStage<Void> thenRunAsync(Runnable action,Executor executor);
```

#### Demo示例

```java
   CompletableFuture.supplyAsync(() -> new Person("小张", 0, "开心的"))
                .thenAccept(person -> {
                    person.setAge(18);
                    System.out.println(person);
                });
```



### 6. thenRun方法

- 与thenAccept方法相似，但不依赖上一个任务的结果。

```java
public CompletionStage<Void> thenRun(Runnable action);
public CompletionStage<Void> thenRunAsync(Runnable action);
public CompletionStage<Void> thenRunAsync(Runnable action,Executor executor);
```

#### Demo示例

```java
     Person person = new Person("小张", 11, null);
        CompletableFuture.supplyAsync(() -> PersonUtil.sleep(person))
                .thenRun(() -> System.out.println("run-run-run"));
```

### 7. thenCombine合并任务

- 该方法会把两个CompletionStage 任务的结果合并导thenCombine处理

```java
public <U,V> CompletionStage<V> thenCombine(CompletionStage<? extends U> other,BiFunction<? super T,? super U,? extends V> fn);
public <U,V> CompletionStage<V> thenCombineAsync(CompletionStage<? extends U> other,BiFunction<? super T,? super U,? extends V> fn);
public <U,V> CompletionStage<V> thenCombineAsync(CompletionStage<? extends U> other,BiFunction<? super T,? super U,? extends V> fn,Executor executor);
```

#### Demo示例

```java
 public void calculatePriceByCompletableFuture() throws Exception {
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
```



### 8.TODO



### 9.TODO



### 10.TODO



