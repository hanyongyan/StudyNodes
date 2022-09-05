

## Stream流

### Stream流的使用

Stream是一个接口，可以去java api中去查看

Stream流是Java8 API推出的新特性,极大的简化了我们遍历集合和筛选集合的操作

Stream API只能被消费一次，后续重复使用已建立的流会报异常！所以stream流是线程安全的！（IllegalStateException  爆出此异常时说明重复消费了）

- 生成流

  通过数据源（集合，数组等）生成流
  eg：list.stream()

- 中间操作 

  一个操作后面可以跟随零个或多个中间操作，其目的主要是打开流，做出某种程度的数据过滤/映射，然后返回一个新的流，交给下一个流操作

  eg：filter()

- 终结操作

  一个流只能有一个终结操作，当这个操作执行后，流就被用"光"了，无法再被操作。所以这必定使流的最后一个操作
  eg：forEach()

### stream流的生成方式

- Collection体系的集合接口可以使用默认方法 stream()  生成流

  eg：default Stream<E> stream();

- Map体系的集合简洁的生成流
- 数组可以通过Stream集团口的静态方法 of(T... Values) 生成流

```java
import java.util.*;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
//ColLection体系的集合可以使用默认方法stream()生成流List<String> list = new ArrayList<String>();
        List<String> list = new ArrayList<>();
        Stream<String> listStream = list.stream();
        Set<String> set = new HashSet<String>();
        Stream<String> setStream = set.stream();
        //Map体系的集合间接的生成流
        Map<String, Integer> map = new HashMap<String, Integer>();
        //Map体系的键流
        Stream<String> keyStream = map.keySet().stream();
        //Map体系的值流
        Stream<Integer> valueStream = map.values().stream();
        //键值对对象所对应的流
        Stream<Map.Entry<String, Integer>> entryStream = map.entrySet().stream();
   	 //数组可以通过stream接口的静态方法of (T... vaLues)生成流
        String[] strArray = {"he11o", "wor1d", "java"};
        Stream<String> strArrayStream = Stream.of(strArray);
        Stream<String> strArrayStream2 = Stream.of("hello", "world", "java");
        Stream<Integer> intStream = Stream.of(10, 20, 30);
    }
}

```

### Stream流的常见中间操作方法

- Stream<T> filter(Predicate predicate):用于对流中的数据进行过滤
  		Predicate接口中的方法：

  ​			Boolean test(T t):对给定的参数进行判断，返回一个布尔值

  ```java
  
  import java.util.ArrayList;
  import java.util.List;
  
  public class Main {
  
      public static void main(String[] args) {
          List<String> list = new ArrayList<>(List.of("张曼玉", "林青霞", "王祖贤", "柳岩", "张敏", "张无忌"));
          //需求1：把list集合中以张开头的元素在控制台输出
          list.stream().filter(s -> s.startsWith("张")).forEach(System.out::println);
          System.out.println("-------");
          //需求2：把list集合中长度为三的元素在控制台输出
          list.stream().filter(s -> s.length() == 3).forEach(System.out::println);
          System.out.println("-------");
          //需求3：把list集合中长度为三且以张为开头的元素输出
          list.stream().filter(s -> s.length() == 3).filter(s -> s.startsWith("张")).forEach(System.out::println);
      }
  }
  
  
  ```

- Stream<T> limit(long maxSize)：返回此流中的元素组成的流，截取前指定参数个数的数据

- Stream<T> skip(long n)：跳过指定参数个数的数据，返回由该流的剩余元素组成的流

```java
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>(List.of("张曼玉", "林青霞", "王祖贤", "柳岩", "张敏", "张无忌"));
        //需求1：取前三个数据在控制台输出
        list.stream().limit(3).forEach(System.out::println);
        System.out.println("-------");
        //需求2：跳过前2个元素，把剩下的元素在控制台输出
        list.stream().skip(2).forEach(System.out::println);
        System.out.println("-------");
        //需求3：跳过两个元素，把剩下的元素中前两个在控制台输出
        list.stream().skip(2).limit(2).forEach(System.out::println);
    }
}

```

- static <T> Stream<T> concat(Stream a, Stream b):合并a和b两个流为一个流
- Stream<T> distinct():返回由该流的不同元素（根据Objectequals(Object)）组成的流

```java
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Demo1 {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>(List.of("张曼玉", "林青霞", "王祖贤", "柳岩", "张敏", "张无忌"));
        //需求1：取前四个数据组成一个流
        Stream<String> limit = list.stream().limit(4);
        //需求2：跳过两个数据组成一个流
        Stream<String> skip = list.stream().skip(2);
        //需求3：合并需求1和需求2的得到的流，并把结果输出
        Stream.concat(limit,skip).forEach(System.out::println);
        //需求4：合并需求1和需求2的得到的流，并把结果输出，要求结果不能重复
        Stream.concat(limit,skip).distinct().forEach(System.out::println);
        //需求3和4 只能一个同时使用 因为均使用到了concat(limit,skip)  每一个流只能够消费一次
        //使用下面这个语句跟需求四得到的结果是相同的 而且可以和需求三一起执行   
        //因为是重新生成了流 而不是流的重复使用
        //Stream.concat(list.stream().limit(4),list.stream().skip(2)).distinct().forEach(System.out::println)
    }
}
```

- Stream<T> sorted()  ：返回由此流的元素组成的流，根据自然排序进行排序
- Stream<T> sorted(Comparator comparator):返回由该流的元素组成的流，根据提供的Comparator进行排序

```java

import java.util.ArrayList;
import java.util.List;

public class Demo1 {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>(List.of("AAAAAA", "BBBBB", "CCCC", "DDD", "EE", "F"));
        //需求1：按照自然排序进行输出
        list.stream().sorted().forEach(System.out::println);
        //需求2：按照字符串长度在控制台进行输出
        list.stream().sorted((o1, o2) -> {
           int num1= o1.length() - o2.length();
           int num2=num1==0?o1.compareTo(o2):num1;
            //如果长度一致时使得按照自然排序进行排序
           return num2;
        }).forEach(System.out::println);
    }
}
```

### 流的收集操作

```java
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Demo1 {

    public static void main(String[] args) {
        String[] str={"林青霞，54","周润发，55","周杰伦,44"};
        Map<String, String> map = Stream.of(str).collect(Collectors.toMap(s -> s.split("，")[0], s -> s.split("，")[1]));
    }
}

```

stream的方法collcet()  使用时里面的toList，toSet，toMap可以实现从流到集合的转化   toMap是一个典型

## 模块化



### 模块化的基本使用步骤

- 创建模块

- 在模块的src目录下心意建一个名为 module-info.java 的描述性文件，该文件专门定义模块名，访问权限，模块依赖等信息，描述性文件中使用模块导出和模块依赖来进行配置并使用。

- 模块中所有未导出的包都是模块私有的，他们是不能在模块之外被访问的

  模块导出格式：  exports 包名;

- 一个模块要是访问其他的模块，必须明确指定依赖哪些模块，为明确指定以来的模块不能访问

  模块依赖格式： requires 模块名；

  注意：写模块名报错，需要按下 Alt + Enter 提示，然后选择模块依赖。





Comparable和Comparator的区别

![元素排序Comparable和Comparator有什么区别？](https://sm-1301822562.cos.ap-nanjing.myqcloud.com/myTypora/57226868a3f0418c88be516c9e6f980f~tplv-k3u1fbpfcp-zoom-crop-mark:3024:3024:3024:1702.awebp)
