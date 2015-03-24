This file provides small examples how to create Jimple code
==============================================================
General Information
-------------------
The Classes can be accessed by 
```java
Scene.v().getClasses()
```

Each SootClass contains its Fields and Methods. You can access them by name or retrieve a list with all elements
```java
SootClass sclass = Scene.v().getSootClass("class_name");
SootMethod smethod = sclass.getMethodByName("method_name");
SootField sfield = sclass.getField("field_name");

List<SootMethod> methods = sclass.getMethods();
Chain<SootField> fields = sclass.getFields();
```


Add fields
----------

```java
SootField f = new SootField("field_name", IntType.v());
Scene.v().getSootClass("main.Test").getFields().add(f);
```
