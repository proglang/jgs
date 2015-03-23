Annotations
===========

Static Analysis
---------------

### Field annotations
TODO explanation

examples:
* Field with a high security label
* 
 ```java
  @FieldSecurity("high")
  int highField;
  ```
  
* Field with a low security label
* 
 ```java
  @FieldSecurity("low")
  int lowField;
  ```
  
* Array field. The array itself has a low security label, its content has a high label
* 
  ```java
  @FieldSecurity("low", "high")
  int[] array;
  ```
  
* Multidimensional array with a label for each dimension
* 
  ```java
  @FieldSecurity({"low", "low", "low"})
  int[][] array;
  ```
  
### Program counter

examples:
* @Constraints({"high <= @pc"})
* @Constraints({"@pc <= low"}

### Parameter security
examples:
* @Constraints({"@0 <= low"})
* @Constraints({"high <= @0","@1 <= low"})
* @Constraints({"@0[ = high"})


### Return security
examples:
* @Constraints({"high <= @return"})
* @Constraints({"@return <= low"})
* @Constraints({@return[ = high})
* @Constraints({"low = @return[["})
