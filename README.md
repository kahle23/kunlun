# What is Artoria
A toolkit only rely on the jdk. And provide aop, bean copy, bean to map, lock,
some usually codec, type converter, simple encapsulation jdk crypto, reflect,
file, io, http, serialize and more usually tools. These are only rely on jdk.

# How to start using
In first, import this package in your project. If you using maven to package 
management tools, you can write following code in you "pom.xml" file.
```
<dependency>
    <groupId>com.github.kahlkn</groupId>
    <artifactId>artoria</artifactId>
    <version>0.5.32.beta</version>
</dependency>
```
Finally, you can using this package provide function now.

# Bean copy demo
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
```

# Lock demo
```
// Declaration shared variable
private Integer num = 100;
// ...
// Consume shared variable
String threadName = Thread.currentThread().getName();
try {
    System.out.println(threadName);
    LockUtils.lock(lockName);
    if (num < 0) { continue; }
    System.out.println(threadName + " | " + (num--));
}
finally {
    System.out.println(threadName + " unlock");
    LockUtils.unlock(lockName);
}
```

# Why name is Artoria
It is come from "Artoria Pendragon", is an anime person. ([Saber in Wikipedia](https://en.wikipedia.org/wiki/Saber_(Fate/stay_night)))
![Artoria Pendragon](artoria.jpg)
