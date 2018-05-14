# What is Artoria
A toolkit only rely on the jdk. And provide aop, bean copy, bean to map, lock,
some usually codec, type converter, simple encapsulation jdk crypto, reflect,
file, io, http, serialize and more usually tools. These are only rely on jdk.
If have cglib will enhance aop, bean copy and bean to map. If have bouncycastle
will support more encryption algorithm.

# How to start using
In first, import this package in your project. If you using maven to package 
management tools, you can write following code in you "pom.xml" file.
```
<dependency>
    <groupId>com.github.kahlkn</groupId>
    <artifactId>artoria</artifactId>
    <version>0.5.31.beta</version>
</dependency>
```

Finally, you can using this package provide function now.

# Some code demo
```
// When bean copy
Student student = BeanUtils.beanToBean(person, Student.class);
System.out.println(JSON.toJSONString(student));

// When bean list copy
List<Student> students = BeanUtils.beanToBeanInList(persons, Student.class);
System.out.println(JSON.toJSONString(students));

// When bean to map
Map<String, Object> map = BeanUtils.beanToMap(person);
System.out.println(JSON.toJSONString(map));

// When using lock
// No1. declarative a lock
LockUtils.registerLock("Lock-Name", ReentrantLock.class);
// No2. try get lock, if don't get, will block
LockUtils.lock("Lock-Name");
// Or No2. try get lock, if don't get, will return false
boolean tryLock = LockUtils.tryLock("Lock-Name", 500, TimeUnit.MILLISECONDS);
```

# Why name is Artoria
It is come from "Artoria Pendragon", is an anime person. ([Saber in Wikipedia](https://en.wikipedia.org/wiki/Saber_(Fate/stay_night)))
![Saber](saber.jpg)
