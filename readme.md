# MUA: Make-Up lAnguage

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
