
# Benchmark

BenchMark 又叫做基准测试，主要用来测试一些方法的性能，可以根据不同的参数以不同的单位进行计算（例如可以使用吞吐量为单位，也可以使用平均时间作为单位，在 BenchmarkMode 里面进行调整）

## 开始前的步骤

项目使用的是 Maven，因此只要对 pom.xml 添加依赖即可。

```xml
<dependency>
    <groupId>org.openjdk.jmh</groupId>
    <artifactId>jmh-core</artifactId>
    <version>1.19</version>
</dependency>
<dependency>
    <groupId>org.openjdk.jmh</groupId>
    <artifactId>jmh-generator-annprocess</artifactId>
    <version>1.19</version>
    <scope>provided</scope>
</dependency>
```

## 例子

记得之前和宿友讨论 ArrayList 和 LinkedList 的遍历的性能差别，当时以一种不太妥当的方法进行测试，导致无法得到比较好的结果，刚好这里可以使用这两个来进行比较。



## 代码

```java
cc.eabour.jmh.BenchMark
```



## 注解介绍

### @BenchmarkMode

Mode 表示 JMH 进行 Benchmark 时所使用的模式。通常是测量的维度不同，或是测量的方式不同。目前 JMH 共有四种模式：

1. Throughput: 整体吞吐量，例如“1秒内可以执行多少次调用”，单位是操作数/时间。
2. AverageTime: 调用的平均时间，例如“每次调用平均耗时xxx毫秒”，单位是时间/操作数。
3. SampleTime: 随机取样，最后输出取样结果的分布，例如“99%的调用在xxx毫秒以内，99.99%的调用在xxx毫秒以内”
4. SingleShotTime: 以上模式都是默认一次 iteration 是 1s，唯有 SingleShotTime 是只运行一次。往往同时把 warmup 次数设为0，用于测试冷启动时的性能

## @OutputTimeUnit

输出的时间单位。

## @Iteration

Iteration 是 JMH 进行测试的最小单位。在大部分模式下，一次 iteration 代表的是一秒，JMH 会在这一秒内不断调用需要 Benchmark 的方法，然后根据模式对其采样，计算吞吐量，计算平均执行时间等。

## @WarmUp

Warmup 是指在实际进行 Benchmark 前先进行预热的行为。

为什么需要预热？因为 JVM 的 JIT 机制的存在，如果某个函数被调用多次之后，JVM 会尝试将其编译成为机器码从而提高执行速度。为了让 Benchmark 的结果更加接近真实情况就需要进行预热。

## @State

类注解，JMH测试类必须使用 @State 注解，它定义了一个类实例的生命周期，可以类比 Spring Bean 的 Scope。由于 JMH 允许多线程同时执行测试，不同的选项含义如下：

1. Scope.Thread：默认的 State，每个测试线程分配一个实例；
2. Scope.Benchmark：所有测试线程共享一个实例，用于测试有状态实例在多线程共享下的性能；
3. Scope.Group：每个线程组共享一个实例；

## @Fork

进行 fork 的次数。如果 fork 数是2的话，则 JMH 会 fork 出两个进程来进行测试。

## @Meansurement

提供真正的测试阶段参数。指定迭代的次数，每次迭代的运行时间和每次迭代测试调用的数量(通常使用 @BenchmarkMode(Mode.SingleShotTime) 测试一组操作的开销——而不使用循环)

## @Setup

方法注解，会在执行 benchmark 之前被执行，正如其名，主要用于初始化。

## @TearDown

方法注解，与@Setup 相对的，会在所有 benchmark 执行结束以后执行，主要用于资源的回收等。

@Setup/@TearDown注解使用Level参数来指定何时调用fixture：

| 名称             |                           描述                            |
| ---------------- | :-------------------------------------------------------: |
| Level.Trial      |      默认level。全部benchmark运行(一组迭代)之前/之后      |
| Level.Iteration  |                一次迭代之前/之后(一组调用)                |
| Level.Invocation | 每个方法调用之前/之后(不推荐使用，除非你清楚这样做的目的) |

## @Benchmark

方法注解，表示该方法是需要进行 benchmark 的对象。

## @Param

成员注解，可以用来指定某项参数的多种情况。特别适合用来测试一个函数在不同的参数输入的情况下的性能。@Param 注解接收一个String数组，在 @Setup 方法执行前转化为为对应的数据类型。多个 @Param 注解的成员之间是乘积关系，譬如有两个用 @Param 注解的字段，第一个有5个值，第二个字段有2个值，那么每个测试方法会跑5*2=10次。



## 将结果输出到日志文件中

可以通过在`OptionsBuilder`中指定`output`属性，把测试结果输出到日志文件中。

```java
public static void main(String[] args) throws RunnerException {
    Options opt = new OptionsBuilder()
            .include(StringBenchmark.class.getSimpleName())
            .output("D:/string_benchmark.log")
            .build();

    new Runner(opt).run();
}
```



### 在Spring应用中使用JMH

大部分java应用会运行在**Spring容器**中，对于测试Spring应用，可以使用上述的`@Setup`和`@TearDown`注解来处理，如类似下面的代码片段：

```java
@State(Scope.Benchmark)
 public class MySpringBeanBenchmarkTest {
 
     private ClassPathXmlApplicationContext springContext;
 
     private MySpringBean mySpringBean;
 
     @Setup
     public void start(){
         springContext = new ClassPathXmlApplicationContext("classpath:spring-context.xml");
         springContext.start();
         mySpringBean = springContext.getBean(MySpringBean.class);
     }
 
     @TearDown
     public void destroy(){
         springContext.destroy();
     }
 
     @Benchmark
     @BenchmarkMode({Mode.Throughput, Mode.AverageTime, Mode.SampleTime})
     @OutputTimeUnit(value = TimeUnit.SECONDS)
     public void testExecute() throws InterruptedException {
         Map<String, Object> context = new HashMap<>();
         context.put("orderAmount", 500);
         mySpringBean.execute("TRADE_ORDER_0001", context);
     }
 
     public static void main(String[] args) throws RunnerException {
 
         Options opt = new OptionsBuilder()
                 .include(".*" + MySpringBeanBenchmarkTest.class.getSimpleName() + ".*")
                 .forks(1)
                 .threads(Runtime.getRuntime().availableProcessors())
                 .measurementIterations(5)
                 .build();
 
         new Runner(opt).run();
     }
 }
```