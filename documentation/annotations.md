Annotations
===========

Static Analysis
---------------

### Field annotations
TODO explanation

examples:
* Field with a high security label
 
 ```java
  @FieldSecurity("high")
  int highField;
  ```
  
* Field with a low security label
 
 ```java
  @FieldSecurity("low")
  int lowField;
  ```
  
* Array field. The array itself has a low security label, its content has a high label

  ```java
  @FieldSecurity("low", "high")
  int[] array;
  ```
  
* Multidimensional array with a label for each dimension

  ```java
  @FieldSecurity({"low", "low", "low"})
  int[][] array;
  ```
  
### Program counter
TODO explanation

examples:
* method can be invoked if the program counter is high

```java
@Constraints({"high <= @pc"})
```

* low program counter

```java
@Constraints({"@pc <= low"}
```

### Parameter security
TODO explanation

examples:
* Methods accepts the first argument only if its security label is <= low
```java
@Constraints({"@0 <= low"})
```

* 

```java
@Constraints({"high <= @0","@1 <= low"})
```

* 

```java
@Constraints({"@0[ = high"})
```

### Return security
TODO explanation

examples:
* 

```java
@Constraints({"high <= @return"})
```

* 

```java
@Constraints({"@return <= low"})
```

* 

```java
@Constraints({"@return[ = high"})
```

* 

```java
@Constraints({"low = @return[["})
