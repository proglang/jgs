Annotations
===========

Static Analysis
---------------

### Field annotations
TODO explanation

examples:
* @FieldSecurity("high")

  int highField;
* @FieldSecurity("low")

  int lowField;
  
* @FieldSecurity("low", "high")

  int[] array;
  
* @FieldSecurity({"low", "low", "low"})

  int[][] array;
  
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
