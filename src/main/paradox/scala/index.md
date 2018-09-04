变量/不变量声明
```dtd
var i: Int = 0
val j: Int = 0
```

条件分支
```
def f(j: Int) : Int = if ( j < 0 ) - j else j
```

for"循环"
```
var i : Int = 0

for ( x <- List(1, 2, 3, 4) ) i += x

println(x)
```

递归函数形式
```dtd
def sum(xs: List[Int]): Int = xs match {
  case ::(head, tail) => head + sum(tail)
  case Nil => 0
}
```

求和尾递归函数
```dtd
def sum(xs: List[Int]): Int = {

  def go(acc: Int, ys: List[Int]) : Int = ys match {
    case ::(head, tail) => go(acc + head, tail) 
    case Nil => acc
  }  
  
  go(0, xs)  
}
```
类似的求积尾递归函数
```dtd
def product(xs: List[Int]): Int = {

  def go(acc: Int, ys: List[Int]) : Int = ys match {
    case ::(head, tail) => go(acc * head, tail) 
    case Nil => acc
  }  
  
  go(1, xs)  
}
```
等价的foldLeft
```dtd
xs.foldLeft(0)(_ + _)
xs.foldLeft(1)(_ * _)
```

Functional Set
```dtd
type Set = Int => Boolean
def contains(s: Set)(x: Int): Boolean = s(x)
def singleton(a: Int): Set = (x: Int) => a == x
def union(s0: Set, s1: Set): Set = (x: Int) => s0(x) || s1(x)
def intersect(s0: Set, s1: Set): Set = ??? //请自己实现一下

```
Function is Object
scala类库中包含一些类Function0, Function1, ..., Function22，所有的函数都是这些类的对象实例而已。
```dtd
// () => Int 等价于 Function0[Int]
Function1
// Int => Int 等价于 Function1[Int, Int]
Function2
// (Int, Int) => Int 等价于 Function2[Int, Int, Int]
```
不同的Function类有一些特定的方法. 常用的Function0, Function1, Function2
```dtd
trait Function0[+R] extends scala.AnyRef {
  def apply() : R
  ... 
}
```
对于一个Function0类型的对象```f: () => Int``` ```f()```等价于 ```f.apply()```

```
trait Function1[-T1, +R] extends{
  def apply(v1 : T1) : R
  def compose[A](g : scala.Function1[A, T1]) : scala.Function1[A, R] = ???
  def andThen[A](g : scala.Function1[R, A]) : scala.Function1[T1, A] = ???
  ...
}
```
对于一个Function1类型的对象
```
val f: Int => BigDecimal = x => BigDecimal(x)
val g: BigDecimal => String = x => x.toString

(f andThen g) == (g compose f) == (x: Int) => BigDecimal(x).toString
```
```
trait Function2[-T1, -T2, +R] extends scala.AnyRef {
  def apply(v1 : T1, v2 : T2) : R
  def curried : scala.Function1[T1, scala.Function1[T2, R]] = ??? 
  def tupled : scala.Function1[scala.Tuple2[T1, T2], R] = ???
  ...
}
```
Curry化
```dtd
//(Int, Int) => Int 等价于 Int => Int => Int
def multiply: (Int, Int) => Int = (x, y) = x * y
multiply(3, 4) // 12

val product = (x: Int) => (y: Int) => x * y
product(3)(4) // 12

//multiply 等价于 product

val double: Int => Int = product(2)
double(3) // 6
```

对于一个Function2类型的对象
```dtd
val f: (Int, Int) => Int = _ + _
val g: Int => Int => Int = f.curried
val h: ( (Int, Int) ) => Int = f.tupled
```

高阶函数(Higher Order Function)
```
List[Int].map[Int](f: Int => Int): List[Int] => List[Int]
```
```dtd
val xs = List(1, 2, 3, 4, 5)
xs.map(double)
//List(2, 4, 6, 8, 10)
xs.map(product(3))
//List(3, 6, 9, 12, 15) 

val lifted: List[Int] => List[Int] = _ map double
```  

二维数组求和
```dtd
val xss : List[List[Int]] = List(List(1, 2, 3), List(4, 5, 6), List(7, 8, 9, 10)
sum(xss map sum)
```