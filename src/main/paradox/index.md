# 实时数据流处理快速学习指南

用Akka Stream与函数式编程技术构建数据处理链路

## 摘要

在数字化平台的时代背景下, 数据驱动决策是企业的重要核心能力之一。数据实时处理能力成为一种业务系统闭环的关键能力。
本文利用Akka Stream流式编程技术快速构建一个实时数据处理应用实例，尝试让读者体会到用其反向压力特性带来系统稳定性增益、编程模型的直观性、强大的模块化组合能力。
配合一点初步函数式编程基础便可以使得开发者关注于更纯粹更简单的数据处理本身。

完整学习完该教程，您将获得以下收益

* 掌握利用Akka Stream反向压力特性避免OOME(内存溢出)进而带来的系统稳定性增益
* 体会到Akka Stream表达数据处理过程的直观性
* 掌握Akka Stream流式编程的模块化组合能力
   * 描述蓝图与运行蓝图分离
   * 流式链路蓝图描述与业务处理子图分离
     * 构建可重用的绘图元件
     * 通过部分图API构建子图
     * 通过Flow API代码胶水粘接图与子图
   * 业务流式处理函数与业务函数分离
* 通过函数式编程将业务函数提升(Lift)为业务流式处理函数
* 了解一点这背后的范畴论基础
   * 范畴 Category
   * 函子 Functor
* 了解一点Scala
   * 进行函数式编程以获得FP组合能力
   * 使用trait在语言层面完成has-a的组合能力

这一切将从一个场景开始。


@@@ index

* [场景描述](story/index.md)
* [构思数据流图解决方案](solution/index.md)
* [环境准备](env/index.md)
* [使用Akka Stream绘制数据流图](akka-stream/index.md)
* [业务函数](scala/index.md)
* [使用函数式编程提升业务函数](functional-programming/index.md)
* [把他们拼起来](combine/index.md)
* [背后的范畴论](category-theory/index.md)
* [小结](summary/index.md)

@@@