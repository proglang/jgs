This file provides small examples how to create Jimple code
==============================================================

Add fields
----------

```java
SootField f = new SootField("field_name", IntType.v());
SootClass c = Scene.v().getSootClass("main.Test").getFields().add(f);
```
