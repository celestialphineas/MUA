# MUA: Make-Up lAnguage

```
MUA (MakeUp lAnguage) Interpreter
Celestial Phineas (Yehang YIN) @ ZJU
usage: java MakeUpInterpreter [options] [filename]
Run in interactive mode by default.
Options:
        --help  : Show this help message.
        --out   : Output the value of the expressions.
        --lisp  : Output the expressions in LISP form.
        --silent: Hide all warnings and errors.
        --prompt: Show prompt.
        --tokens: Print tokens.
        --trace : Print stack trace.
```

MUA is a made-up programming language introduced in Weng Kai's *Principles of Programming Language* course. The `master` branch of this repo is an interpreter of MUA with a few modifications to the original definition. For the interpreter that allows the original definition to work, please check out the `homework` branch.

MUA is in some ways alike LOGO. And I added a bit stuff inspired by Mathematica.

I write this, for the assignment, also for fun (if I didn't think it's fun, I would not have added features to it and fixed some definitions). The implementation is still naive with low efficiency. If possible, I would consider to re-implement this in another language.

## What is MUA?

MUA is,

- Strongly and dynamicly typed
- Lexical scope
- Not very procedural, yet not typically functional...
- Everything is an expression
- Every expression is an atomic or a list
- Every list has a "head" (as the concept defined in Wolfram Language, i.e. Mathematica's definition)
- The *lists* (not the "lists" mentioned above, but expression lists with the head `list`) are to hold things.
- *Lists* are also scopes
- All scopes are namespaces

## Examples

See [examples](/examples) of some MUA scripts.

## Hello, world!

Run the MUA interpreter interactively, the code below prints a line `Hello,world!`

```
print "Hello,world!
```

```
Hello,world!
```

If you find there is no space in the output, try this:

```
print "Hello, + chspace + "world!
```

```
Hello, world!
```

`chspace` is short for *character space*, this is an operation simply returns a word object *space*.

## Things (objects) in MUA

### Atomic things

- Numbers (There is no difference between integer and float in MUA)
- Booleans (true/false)
- Words (Strings)
- Built-in operations

### Complex things

- Expression lists
- Namespaces

MUA does allow user defined operations (functions), but the custom operations themselves are lists. They come to life via a feature of MUA called *operationization*.

## How to write these things?

- You write a number by simply typing it in.
- You write a boolean with `true` and `false` (`true` and `false` are operators that take zero operands in fact)
- All words you write should start with a quote "
- For the operations you would like to use, simply type them in.

Here are some examples:

```
print 1
```

```
1.0
```
----
```
print true
```

```
true
```
----
```
print "apple
```

```
apple
```
- If you want a list, surround things with a pair of brackets
- You are welcomed to use the everyday operators, but please keep your intention clear enough for MUA to understand

`[1 2 3 4]`, this is fine.\
`[[1] 2 3 [true add 1 1 [2 * 3 - 6]]]`, this is also fine.

`add 1 -1`, MUA takes it as `1+(-1)`\
`add 1 - 1`, MUA cannoot understand what you want.

Traditionally in C, we write both:\
Good: `2*(-3)`\
Bad: `2*-3`, MUA does not recognize this

## Bind names with objects

Use `make`.

```
make "a 1
print thing "a
```

```
1.0
```

`thing` is an operation returns the object binded with the given name. You may also write `print :a` instead of `print thing "a`, they are equivalent.

MUA is a language with lexical scopes:

```
[
    make "b 1
    print :b
]
print :b
```

```
1.0
[Finding variable ERROR]           Undefined reference to "b". 
[Operation thing WARNING]          Undefined reference to "b".
```

The first line, `1.0` is printed by `print :b`. But refering to `b` in the global scope causes MUA to complain. This is to say, the `b` in brackets is not the `b` exposed.

But MUA provides such a way to bind a name to a namespace.

```
[
    namespace "inside
    make "darkness -1
    print :darkness
]

print :inside.darkness
```

```
-1.0
-1.0
```

## Defining operations

Define your own operation:

```
make "fun [["arg1 "arg2 "arg3]
[
    output :arg1 + :arg2 * :arg3
]]

fun 1 2 3
```

```
7.0
```

Recursion is also allowed. We recommend you `declare` before use. But if you write `make`, `"fun` and the argument list in the same line, `declare` can be omitted. MUA line preprocessor will automatically declare it for you.

```
make "fact [["n]
[
    test lt :n 1
    ift [output 1]
    iff [output :n * fact :n-1]
    //------Equivalence:------
    // ift 1
    // iff :n * fact :n-1
]]

[
    make "i 1
    repeat 10 [
        print fact :i
        make "i :i + 1
    ]
]
```

```
1.0
2.0
6.0
24.0
120.0
720.0
5040.0
40320.0
362880.0
3628800.0
```

So, what is the use of `declare`?

```
declare "undefined_function ["arg1 "arg2]
make "a hold undefined_function 1 2
make "undefined_function [["arg1 "arg2]
[
    output add :arg1 :arg2
]]
eval :a
```

```
3.0
```

`declare` tells the interpreter how many operands the undefined operator will take.

## Namespaces

Everything in a bracket is in a list, in a scope and in a namespace. A list is a scope, a scope is a namespace. The root scope is `global`, but unnamed by default.

MUA finds objects by finding it in the local namespace first, then its parent namespace, then the parent, until the global scope. Periods `.` are used to present the hierarchy of namespaces.

The `namespace "namespace_name` statement can be put anywhere in the list/namespace.

```
[
    [
        namespace "a
        make "obj1 1
    ]
    namespace "b
    [
        make "obj2 2
        namespace "c
    ]
    make "obj3 3
]
print :b.a.obj1
print :b.c.obj2
print :b.obj3
```

```
1.0
2.0
3.0
```

### Export & Expose

The `export` operation will bind the name and the object in the parent namespace. The `expose` operation will bind in the global namespace.

```
[
    namespace "a
    [
        namespace "inside
        make "b "Hello,
        make "c "world!
        export "b
        expose "c
        make "b :b + chspace + :c
        make "c :b + chspace + "MUA~
    ]
]

print :a.b
print :c
print :a.inside.b
print :a.inside.c

```

```
Hello,
world!
Hello, world!
Hello, world! MUA~
```

## Sugar?

So long as it won't cause any ambiguity of infix operators, you can add parentheses to whereever you like to improve readability.

```
print ("Hello, + chspace + "world!)
// Something looks like Lisp
(print "Hello, + chspace + "world!)
// Even more crazy
(print) "Hello + chspace + ("world!)
```

```
Hello, world!
Hello, world!
Hello, world!
```

## Built-in Operations

```java
// Arithmetic and numerical operations
    MUAadd.class, MUAsub.class, MUAmul.class, MUAdiv.class, MUAmod.class,
    MUAintervalrandom.class, MUAfloor.class,
    MUAsqrt.class, MUAsin.class,
// Logical operations
    MUAand.class, MUAor.class, MUAnot.class,
// Comparators
    MUAgt.class, MUAeq.class, MUAlt.class,
// Boolean values
    MUAtrue.class, MUAfalse.class,
// Character values
// These built-in operations take no arguments and return a single MUA word containing exactly the character
    // chplus +         chminus -       chaster *       chslash /
    // chlbrac [        chrbrac ]       chlparenth (    chrparenth )
    // chtab \t         chspace <space> chendl \n
    MUAchplus.class, MUAchminus.class, MUAchaster.class, MUAchslash.class,
    MUAchlbrac.class, MUAchrbrac.class, MUAchlparenth.class, MUAchrparenth.class,
    MUAchtab.class, MUAchspace.class, MUAchendl.class,
// List head
// Head of the lists, take two arguments by default
    MUAlist.class,
// List operations to do functional programming
    MUAflatten1level.class, MUAapply.class, MUAmap.class, MUApart.class, MUAlength.class,
// Name binding and recalling operations
    // declare [formal_par1, formal_par2, ...]
    //      Declare a function for later use, and thus MUA can know the prototype of some function and thus
    //      be able to do the lexical analysis for its parameters. We need this because MUA does
    //      one-directional scanning. This operation can be useful to do lazy evaluation, nested function
    //      calls and recursion.
    // make "name val
    //      Make, is the operation to bind the value to a name in the local namespace
    // erase "name
    //      Erase the name in the local namespace
    // thing "name      :name
    //      Operation that returns the value bound to the name
    MUAdeclare.class, MUAmake.class, MUAerase.class, MUAthing.class,
// Evaluation control operations
    // hold expr
    //      Hold the expression. Some built-in operations do evaluation to a certain slot in their arg list.
    //      The hold operation allows the held expression to be evaluated lazily
    // eval expr
    //      Evaluate the expression. (Forced eval twice)
    // silent expr
    //      Evaluate the expression but return nothing. Null in MUA is defined as nothing.
    //      In MUA, [ null ] you get []
    MUAhold.class, MUAeval.class, MUAsilent.class,
// Control flow and high order expression replacement
// These operations are full of side effects and are the evil side of MUA design.
// You can use them to do imperative programming.
    // test expr
    //      The test operation implementation in MUA provides a possibility to do imperative programming.
    //      The test operation causes side effects. It marks its result in the local scope and return
    //      nothing.
    // ift expr
    //      ift, aka "if true", checks if the local scope has tested something. If the test result in local
    //      scope is true, the operation evaluates its first slot and returns the value of the expression.
    // iff expr
    //      iff, "if false" then expr
    // stop
    //      Stop the evaluation of current expression, stop returns nothing.
    // output expr
    //      Stop the evaluation of current expression, use the expr on the operation's first slot to replace
    //      the evaluating expression. Amazing, urh?
    // repeat times expr
    //      Evaluate the expression for times.
    // if booleantest trueval falseval
    //      Functional if
    MUAtest.class, MUAift.class, MUAiff.class, MUAstop.class, MUAoutput.class, MUArepeat.class,
    MUAif.class,
// Namespace control
    // exportsymbol "name
    //      Export the local symbol with certain name to its upper level namespace
    // exportnamespace "name
    //      Export the namespace with certain name to its upper level namespace and thus make the inner
    //      namespace accessible to its outer level. The outer level may access its inner level with
    //      something like ":inner.name"
    // exportall
    //      Export all symbols defined in the local namespace to the upper level
    // exposesymbol "name
    //      Similar to exportsymbol, except that this expose the symbol to the global namespace
    // exposenamespace "name
    //      Similar to exportnamespace, except that this expose the namespace to the global namespace
    // exposeall
    //      Expose all symbols defined in the local namespace to the global namespace
    // clearlocal
    //      Clear all definitions in the local scope
    MUAexportsymbol.class, MUAexportnamespace.class, MUAexportall.class,
    MUAexposesymbol.class, MUAexposenamespace.class, MUAexposeall.class,
    MUAclearlocal.class, MUAnamespacelist.class, MUApparentnamespacelist.class,
// MUA core utils
    // clearglobal
    //      Operation that clears all user-defined names in global namespace and load back the default
    //      definitions of built-in operations
    // exit
    //      Exit MUA core
    // reloadcore
    //      Reload MUA core, this operation reload the MUA core (i.e. load back the default operation
    //      definitions) without clearing user-defined names
    // setcalldepth depth
    //      This operation set up the maximum depth of call stack, usually 4096 is a good choice, since MUA
    //      does not implement itself's call stack, rather, MUA use JVM's call stack to make things live.
    MUAclearglobal.class, MUAexit.class, MUAreloadcore.class, MUAsetcalldepth.class, MUAloadmodule.class,
// Predicates
    // isname "word
    //      Returns a boolean indicating if the word is an accessible expression in the current scope
    // isnumber, isword, islist, isbool, isempty
    //      These predicates all take one argument, and return a boolean object true or false accordingly.
    //      The predicates all evaluate its argument, and you will have to use "hold" for some desired
    //      effects.
    // isempty
    //      isempty evaluate the first argument and return a boolean to indicate if the expression is empty
    //      (with a head only) or the string is empty.
    MUAisname.class, MUAisnumber.class, MUAisword.class, MUAislist.class, MUAisbool.class, MUAisempty.class,
// IO and system
    MUAprint.class, MUAprintinteger.class, MUAread.class, MUAreadlist.class,
    MUAwait.class, MUAsave.class, MUAload.class, MUAunicode.class, MUAfromunicode.class
```

