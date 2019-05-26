# 第六章

## 6.1 接口

### 6.1.1 接口概念

* 接口不是类，而是对类的一组需求描述，这些类需要遵从接口描述的同一格式进行定义

* 如果类实现了某接口就必须包含的所有方法

* 提供实例域和方法实现的任务由实现接口的类来完成

  1. 将类声明为实现给定的接口
  2. 对接口中的所有方法进行定义，实现时必须声明为public

* 让一个类实现排序需要实现compareTo方法，为什么不直接在类中提供一个compareTo方法，而是要实现Comparable接口

  主要原因在于 Java 程序设计语言是一种强类型 （ strongly typed) 语言。在调用方法的时候， 编译器将会检查这个方法是否存在。在 sort 方法中可能存在下面这样的语句 

  ```java
  if (a[i].compareTo(a[j]) > 0){
  	// rearrange a[i] and a[j]
      ...
  }
  ```

  为此， 编译器必须确认 a[i]—定有 compareTo 方法。 如果 a 是一个 Comparable 对象的数组， 就可以确保拥有 compareTo 方法，因为每个实现 Comparable 接口的类都必须提供这个方法的定义。 

* 考虑到实例中的Employee和Manager，考虑到对称的问题如 果 x 是 一 个 Employee 对 象，y 是 一 个 Manager 对 象，调 用 x.compareTo(y) 不 会 抛 出 异 常， 它 只 是 将 x 和 y 都 作 为 雇 员 进 行 比 较。 但 是 反 过 来，y.compareTo(x) 将 会 抛 出 一 个 ClassCastException 。这 种 情 况 与 第 5 章 中 讨 论 的 equals 方 法 一 样， 修 改 的 方 式 也 一 样 ， 有两 种 不 同 的情 况。如 果 子 类 之 间 的 比 较 含 义 不 一 样， 那 就 属 于 不 同 类 对 象 的 非 法 比 较。 每 个compareTo 方 法 都 应 该 在 开 始 时 进 行 下 列 检 测：
  if (getClass()!= other.getClass()) throw new ClassCastExceptio()； 

### 6.1.2 接口的特性 

* 接口不是类，不能使用new运算符实例化一个接口。但是可以声明接口变量

  `x = new Comparable(...)   //ERROR`

  `Comparable x;	//OK`

* 接口变量必须引用实现了接口的类对象，同使用instanceof检查一个对象是否属于某个特定类一样，可以使用instanceof检查一个对象是否实现了某个接口

  ```java
  x = new Employee(...);	// OK provided Employee implements Comparable
  if (anObject instanceof Comparable) {...};
  ```

* 和建立类的继承关系一样，接口也可以被扩展。允许存在多条从具有较高通用性的接口到较高专用性接口的链。例如假设有一个Moveable的接口，可以以它为基础扩展一个Powered的接口。

  ```java
  public interface Moveable {
      void move(double x, double y);
  }
  
  public interface Powered extends Moveable {
      double milesPerGallon();
  }
  ```

* 接口中虽然不能包含实力域和静态方法，但是可以包含常量。与接口中的方法自动被设置为public一样，接口中的域自动被设置为public static final。虽然可以手动标记为public 或public static final,但编程规范不建议如此。

### 6.1.3 接口和抽象类

采用接口而非抽象类可以克服java每个类只能扩展于一个类的局限，java不支持多重继承，接口可以提供多重继承的大多数好处，同时也能避免多重继承的复杂性和低效性。

### 6.1.4 静态方法

在 Java SE 8 中，允许在接口中增加静态方法。理论上讲，没有任何理由认为这是不合法的。 只是这有违于将接口作为抽象规范的初衷。 

### 6.1.5 默认方法

默认方法可以带来便利，如果一个接口包含多个方法，将其中方法设置为默认方法，可以是程序员只关注与自身需要的方法，其余的使用默认方法。第二个默认方法的作用是用于“接口演化”

### 6.1.6 解决默认方法冲突

如果现在一个接口中将一个方法定义为默认方法，又在其他超类或是接口中定义了同样的方法，就会产生默认方法冲突。解决默认方法规则如下：

1. 接口冲突

   如果与一个超接口提供了一个默认方法，另一个接口提供了同名且参数类型相同的方法，必须覆盖这个方法解决冲突。

   ```java
   public interface Named {
       default String getName() {
           return getClass.getName + "_" + hashCode;
       }
   }
   
   public interface Person {
       default String getName() {
           ...
       }
   }
   
   public Student implements Named, Person {
       ...				//存在冲突
   }
   类会继承 Person 和 Named 接口提供的两个不一致的 getName 方法。并不是从中选择一个，Java 编译器会报告一个错误，让程序员来解决这个二义性。只需要在 Student 类中提供一个 getName 方法。在这个方法中，可以选择两个冲突方法中的一个。
   public Student implements Named, Person {
       public String getName () {
       	return Person.super.getName();
       }
   }
   ```

   注：即使其中一个的getName没有提供默认实现，也需要覆盖解决冲突，但是两个都没有默认实现则不用了。

2. 超类冲突

   如果一个类扩展了一个超类，同时实现了一个接口，并从超类和接口继承了相同的方法，这种情况超类优先，只会考虑超类的默认方法，接口所有的默认方法都会被忽略。

## 6.2 接口示例

### 6.2.1 接口与回调

回调（ callback) 是一种常见的程序设计模式。在这种模式中， 可以指出某个特定事件发生时应该采取的动作。 例如，可以指出在按下鼠标或选择某个菜单项时应该采取什么行动。 

### 6.2.2 Comparator 接口

比较器（Comparator）是实现了Comparator接口的类的实例。

```java
public interface Comparator<T> {
	int compare (T first, T second);
}
要实现按长度比较字符串，可以如下定义一个实现了Comparator<String> 的类
class LengthComparator implements Comparator<String> {
	public int compare (String first, String second) {
		return first.length() - second.length();
	}
}
具体完成比较时，需要建立一个实例：
Comparator comp = new LengthComparato();
if(comp.compare(word[i], word[j]) > 0) ...
注：这个compare方法在在 Comparator对象上调用，而非字符串本身上调用
```

注：尽管LengthComparator对象本身没有状态，但因为compare不是一个静态方法，所以我们还是需要实例化一个LengthComparator对象，以此来调用compare对象。

```java
String[] friends = {"peter", "celery", "link"};
Arrays.sort(friends, new LengthCompare());
```

### 6.2.3 对象克隆

* clone方法是Object的一个protected方法，这意味着不能直接调用这个方法，只有Employee类可以克隆Employee对象。

* Cloneable 接口的出现与接口的正常使用并没有关系。具体来说，它没有指定clone 方法，这个方法是从 Object 类继承的。这个接口只是作为一个标记，指示类设计者了解克隆过程。对象对于克隆很“ 偏执”， 如果一个对象请求克隆， 但没有实现这个接口， 就会生成一个受査异常。 

  注：Cloneable 接口是 Java 提供的一组标记接口 ( tagging interface ) 之一。（有些程序员称之为记号接口 ( marker interface））。 应该记得， Comparable 等接口的通常用途是确保一个类实现一个或一组特定的方法。 标记接口不包含任何方法； 它唯一的作用就是允许
  在类型查询中使用 instanceof:

  if (obj instanceof Cloneable) . . .  

* 使 clone 的默认（浅拷贝）实现能够满足要求， 还是需要实现 Cloneable 接口， 将 clone重新定义为 public， 再调用 super.clone(。) 下面给出一个例子 ：

  ```java
  class Employee implements Cloneable {
      public Employee clone() throw CloneNotSupportedException {
          return (Employee) super.clone();
      }
  }
  ```

* 与 Objectxkme 提供的浅拷贝相比， 前面看到的 clone 方法并没有为它增加任何功能。这里只是让这个方法是公有的。 要建立深拷贝， 还需要做更多工作，克隆对象中可变的实例域。下面来看创建深拷贝的 done 方法的一个例子： 

  ```java
  class Emplyee implements Cloneable {
      public Employee clone() throw CloneNotSupportedException {
          // call Object.clone()
          Employee cloned = (Employee) super.clone();
          
          // clone mutable fields
          cloned.hireDay = (Date) hireDay.clone();
          
          return cloned;
      }
  }
  如果在一个对象上调用 clone, 但这个对象的类并没有实现 Cloneable 接口， Object 类的 clone 方法就会拋出一个 CloneNotSupportedExceptionD 当然， Employee 和 Date 类实现了Cloneable 接口， 所以不会抛出这个异常。 
  也可以捕获这个异常
  public Employee implement Cloneable {
      public Employee clone() {
          try {
                  // call Object.clone()
                  Employee cloned = (Employee) super.clone();
  
                  // clone mutable fields
                  cloned.hireDay = (Date) hireDay.clone();
  
                  return cloned;
              } catch (CloneNotSupportedException e){
              	return null;
              }
      }
  }
  
  ```

## 6.3 lambda表达式

### 6.3.1 lambda表达式的语法

参数， 箭头（->) 以及一个表达式。如果代码要完成的计算无法放在一个表达式中，就可以像写方法一样，把这些代码放在 {}中，并包含显式的 return 语句。 

```java
(String first, String second) ->{
    if (first.length() < second.length()) return -1;
    else if (first.length() > second.length()) return 1;
    else return 0;
}
即使lambda表达式没有参数也要提供空括号，就像无参数方法一样。
() -> {
	for (int i = 100; i >= 0; i--) 
		System.out.println(i);
}
如果可以推导出一个lambda表达式的参数类型，则可以忽略其参数类型：
Comparator<String> comp 
	= (first, second) 
    	->first.length() - second.length();
如果方法只有一个参数，而且参数的类型可以推到得出，甚至可以省略小括号：
ActionListener listen = event -> 
    System.out.println("The time is " + new Date());	//Instead of (event) -> ... or (ActionListener event) ->...
无需指定lambda表达式的返回类型，lambda表达式的返回值总是会由上下文推到得出，例如：
(String first, String second) -> first.length() - second.length();
此表达式可以在需要int类型结果时使用。
注：如果一个 lambda 表达式只在某些分支返回一个值， 而在另外一些分支不返回值，这是不合法的。 例如，（int x)-> { if (x >= 0) return 1; } 就不合法。
```

### 6.3.2 函数式接口

对于只有一个抽象方法的接口， 需要这种接口的对象时， 就可以提供一个 lambda 表达式。这种接口称为函数式接口 （functional interface )。 

为了展示如何转换为函数式接口， 下面考虑 Arrays.sort 方法。它的第二个参数需要一个Comparator 实例， Comparator 就是只有一个方法的接口， 所以可以提供一个 lambda 表达式 ：

```java
Arrays.sort(word, 
           (first, second) -> first.length - second.length());
```

### 6.3.3 方法引用

方法引用就是直接传递方法。例如：

```java
Timer t = new Timer(1000, System.out::println);
```

表达式 System.out::println 是一个方法引用（ method reference ), 它等价于 lambda 表达式x ->System.out.println(x) 。

再来看一个例子， 假设你想对字符串排序， 而不考虑字母的大小写。可以传递以下方法表达式：
Arrays.sort(strings，String::conpareToIgnoreCase) 

从这些例子可以看出， 要用::操作符分隔方法名与对象或类名。主要有 3 种情况： 

* object::instaceMethod

* class::staticMethod

* class:instaceMethod

  在前 2 种情况中， 方法引用等价于提供方法参数的 lambda 表达式。前面已经提到，System.out::println 等价于 x -> System.out.println(x) 类似地， Math::pow 等价于（x，y)->Math.pow(x, y) 

  对于第 3 种情况， 第 1 个参数会成为方法的目标。例如，String::compareToIgnoreCase 等同于 (x, y)-> x.compareToIgnoreCase(y) ;

可以在方法引用中使用 this 参数。 例如， this::equals 等同于 x-> this.equals(x)。 使用super 也是合法的。下面的方法表达式super::instanceMethod 



### 6.3.4 构造器引用

构造器引用与方法引用很类似，只不过方法名为 new。例如， Person::new 是 Person 构造器的一个引用。哪一个构造器呢？ 这取决于上下文。 

可以用数组类型建立构造器引用。例如， int[]::new 是一个构造器引用，它有一个参数：即数组的长度。这等价于 lambda 表达式 x-> new int[x]。

Java 有一个限制，无法构造泛型类型 T 的数组。数组构造器引用对于克服这个限制很有用。表达式 new T[n] 会产生错误，因为这会改为 new Object[n] 

例如，假设我们需要一个 Person 对象数组。 Stream 接口有一个 toArray 方法可以返回 Object 数组： 

```java
Object[] people = stream.toArray();
不过，这并不让人满意。用户希望得到一个 Person 引用数组，而不是 Object 引用数组。流库利用构造器引用解决了这个问题。可以把 Person[]::new 传入 toArray 方法：
Person[] people = stream.toArray(Person::new);
```

### 6.3.5 变量作用域

lambda表达式一共有3个部分

* 一个代码块
* 参数
* 自由变量，不是参数，也不是在代码块中定义的变量

```java
public static void repeatMessage (String text, int delay) {
    ActionListener listener = event -> {
        System.out.println(text);
        Toolkit.getDefaultToolkit().beep.();
        
    }
    
    new Timer(delay, listener).start();
}
```

这个 lambda 表达式有 1 个自由变量 text。 表示 lambda 表达式的数据结构必须存储自由变量的值， 在这里就是字符串 "Hello"。 我们说它被 lambda 表达式捕获 (captured)

* lambda 表达式可以捕获外围作用域中变量的值。 在 Java 中， 要确保所捕获的值是明确定义的，这里有一个重要的限制。在 lambda 表达式中， 只能引用值不会改变的变量。 

  ```java
  public static void countDown(int start, int delay){
      ActionListener listener = event ->{
          start-- ; // Error: Can't mutate captured variable
          System.out.println(start);
      }；
      new Timer(del ay, listener) ,start();
  }
  ```

  

* 在 lambda 表达式中引用变量， 而这个变量可能在外部改变，这也是不合法的。 

* lambda 表达式中捕获的变量必须实际上是最终变量 ( effectivelyfinal)实际上的最终变量是指， 这个变量初始化之后就不会再为它赋新值。 

  ```java
  public static void repeat(String text, int count){
      for (int i = 1; i <= count; i++){
          ActionListener listener = event ->{
              System.out.print1n(i + ": " + text);
              // Error: Cannot refer to changing i
          }；
          new Timer(1000, listener).start();
      }
  }
  ```

  

* lambda 表达式的体与嵌套块有相同的作用域。这里同样适用命名冲突和遮蔽的有关规则。在 lambda 表达式中声明与一个局部变量同名的参数或局部变量是不合法的。 

  ```java
  Path first = Paths.get("usr/bin");
  Couparator<String> comp =
  (first, second) -> first.length() - second.length() ;
  // Error: Variable first already defined
  ```

  

* 在一个 lambda 表达式中使用 this 关键字时， 是指创建这个 lambda 表达式的方法的 this参数 

  ```java
  public class ApplicationO{
      public void init(){
      	ActionListener listener = event ->{
      		System.out.print n(thi s.toStringO);
          }
      }
  }
  表达式 this.toStringO 会调用 Application 对象的 toString方法， 而不是 ActionListener 实例的方法。在 lambda 表达式中， this 的使用并没有任何特殊之处。lambda 表达式的作用域嵌套在 init 方法中，与出现在这个方法中的其他位置一样， lambda 表达式中 this 的含义并没有变化。
  ```

### 6.3.6 处理lambda表达式

使用 lambda 表达式的重点是延迟执行（ deferred execution ) 毕竟， 如果想耍立即执行代码，完全可以直接执行， 而无需把它包装在一个丨ambda 表达式中。之所以希望以后再执行代码， 这有很多原因， 如： 

* 在一个单独的线程中运行代码； 
* 多次运行代码； 
* 在算法的适当位置运行代码 （例如， 排序中的比较操作；) 
* 发生某种情况时执行代码 （如， 点击了一个按钮， 数据到达， 等等；) 
* 只在必要时才运行代码 

下面来看一个简单的例子。 假设你想要重复一个动作 n 次。 将这个动作和重复次数传递到一个 repeat 方法： 

```java
repeat(10, () -> System.out.println("Hello, word!!!"));
```

要接受这个 lambda 表达式， 需要选择（偶尔可能需要提供）一个函数式接口。 表 6-1 列出了 Java API 中提供的最重要的函数式接口。在这里， 我们可以使用 Runnable 接口： 

![](C:\celery\java核心技术学习笔记\第六章 接口、lambda表达式和内部类\常用函数式接口.png)

```java
public static void repeat(int n, Runnable action){
	for (int i = 0;i < n; i++) action.run();
}
```

需要说明，调用 action.run() 时会执行这个 lambda 表达式的主体。 

现在让这个例子更复杂一些。我们希望告诉这个动作它出现在哪一次迭代中。 为此，需要选择一个合适的函数式接口，其中要包含一个方法， 这个方法有一个 int 参数而且返回类型为 void: 处理 int 值的标准接口如下： 

```java
public interface IntConsumer{
	void accept(int value);
}
public static void repeat(int n, IntConsumer action){
	for (inti = 0; i < n;i++) action.accept(i);
}
可以如下调用：
repeat(10,i -> System.out.println("Countdown: " + (9 - i)));
```

表 6-2 列出了基本类型 int、 long 和 double 的 34 个可能的规范。 最好使用这些特殊化规范来减少自动装箱。 出于这个原因， 我在上一节的例子中使用了 IntConsumer 而不是Consume<lnteger >。

![](C:\celery\java核心技术学习笔记\第六章 接口、lambda表达式和内部类\基本类型的函数式接口.png)

### 6.3.7 再谈Comparator

**注：**暂时感觉实用性不强

静态 comparing 方法取一个“ 键提取器” 函数， 它将类型 T 映射为一个可比较的类型( 如 String )。 对要比较的对象应用这个函数， 然后对返回的键完成比较。 例如， 假设有一个Person 对象数组，可以如下按名字对这些对象排序：  

```JAVA
Arrays.sort(people, Comparator.comparing(Person::getName()));
按姓相同，使用thenComparing
Arrays.sort(people, Comparator.comparing(Person::getLastNanme())
           .thenComparing(Person::getFirstName()));
可以为comparing和thenComparing提取的键指定一个比较器，例如按照人名长度完成排序：
Arrays.sort(people, Comparator.comparing(Person::getName,
(s, t) -> Integer.compare(s.1ength(), t.length（)))；
另外， comparing 和 thenComparing 方法都有变体形式，可以避免 int、 long 或 double 值的装箱。要完成前一个操作， 还有一种更容易的做法：
Arrays.sort(people, Comparator.comparinglnt(p -> p.getNameO -length()));
```

如果键函数可以返回 null, 可 能 就 要 用 到 nullsFirst 和 nullsLast适配器。 这些静态方法会修改现有的比较器，从而在遇到 null 值时不会抛出异常， 而是将这个值标记为小于或大于正常值。例如， 假设一个人没有中名时 getMiddleName 会返回一个 null, 就 可 以 使 用Comparator.comparing(Person::getMiddleName(), Comparator.nullsFirst(…));

nullsFirst 方法需要一个比较器， 在这里就是比较两个字符串的比较器。 naturalOrder 方法可以为任何实现了 Comparable 的类建立一个比较器。在这里， Comparator.<String>naturalOrder()正是我们需要的。下面是一个完整的调用， 可以按可能为 null 的中名进行排序。这里使用了一个静态导人Arrays jaVa.util .sort .*以便理解这个表达式 。注意 naturalOrder的类型可以推到得出。

```java
Arrays.sort(people, comparing(Person::getMiddleName, nullsFirst(naturalOrder())));
```

静态 reverseOrder 方法会提供自然顺序的逆序。要让比较器逆序比较， 可以使用 reversed实例方法 。例如 naturalOrder().reversed() 等同于 reverseOrder()。 

## 6.4 内部类

















