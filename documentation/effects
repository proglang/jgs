### Effects

#### Write effects
* **assignment**:
	+ **static field**:  
	Results in a *write effect* to the *security level* of the static field, as well as in the *write effects* of the class which declares the static field.
	+ **instance field**:  
	Results in a *write effect* to the *security level* of the instance field.
	+ **array**:  
	Results in a *write effect* to the *security level* of the array (no matter if the array is a local variable or a field).
* **invocation**:
	+ **static method**:  
	Results in the *write effects* which are defined for the invoked method, as well as in the *write effects* of the class which declares the static method.
	+ **instance method**:  
	Results in the *write effects* which are defined for the invoked method.
	
On the one hand a *write effect* violation happens if the *write effect* annotation doesn't contain a calculated effect. On the other hand also a *write effect* violation occurs if a *write effect* takes place inside a context which is stronger or equals the affected *security level*.

### Security
* **update of level**:
	+ **local variable**:  
	no restrictions, i.e. no matter if the *security level* of the assigned value is weaker, equals or is stronger than the level of the local variable. The level of the local variable will be updated to this *security level*. Note: if the context is stronger than the level of the assigned value, the level of the local variable will be updated to this stronger program counter *security level*.
	+ **field**:  
	the *security level* of the assigned value has to be weaker or equal to the level of the field. The *security level* will not be updated. Note: the context will be considered, thus it is not possible to assign to a field if the the context is stronger than the level of the field.
	+ **array** (special case):  
	no matter which *security level* an array itself has, the values which should be stored in the array must have the weakest *security level*. Also the level of the index should be weaker than the *security level* of the array.
	
* **lookup of level**:
	+ **local variable**:   
	result will be the *security level* of the local.
	+ **field**:  
	result will be the *security level* of the field. If the field is an instance field and the instance has a stronger *security level* than the field level, then the result will be the stronger instance *security level*. 
	+ **array**:  
	result will be the *security level* of the stored value at the specified index (because of restrictions this is the weakest available *security level*). If the array has a stronger level, then the result will be this stronger *security level* of the array. Also if the *security level* of the index is stronger than the level of the array and the stored value, then the result will be the level of the index.
	+ **array length**:  
	the result will be the *security level* of the array.   
	+ **constant**:  
	result will be the weakest available *security level*.
	+ **library field**:  
	result will be the weakest available *security level*.
	+ **library method**:  
	result will be the strongest *security level* of the specified arguments or the weakest available *security level*, if no arguments are specified.
	+ **expression**:   
	result will be the strongest *security level* of the operands.
	+ **method**:  
	the result will be the return *security level* of the method. If the method is an instance method and the instance has a stronger *security level* than the return level, then the result will be the stronger instance *security level*. During the lookup also the level of arguments are compared with the *security level* of the parameters. The *security level* of the argument has to be weaker or equals than the corresponding parameter *security level*.
			
* **return level**:  
looks up the *security level* of the returned value and compares it with the expected return *security level*. The calculated level has to be weaker or equals than the expected return *security level*.

*Security level* violations are triggered by the following phenomena:

* Assignment of a value to a field, where the field has a weaker *security level* than the assigned value. Note that the *security level* of the assigned value also depends on the context.
* The calculated return *security level* is stronger than the expected return level (or the levels are not comparable, e.g. 'void'). Note that if the context is stronger than the *security level* of the returned value, then this stronger program counter *security level* is the calculated return *security level*.
* The *security level* of an argument of a method invocation is stronger than the expected level for this parameter.

