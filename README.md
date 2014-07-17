# brainfuck

Brainfuck interpreter in Clojure

###Usage

####Interpret any source file
```
lein run <path to source text file>

Example:

lein run resources/helloworld.txt

Hello World!

```

####Test
```
lein test

Ran 1 tests containing 1 assertions.
0 failures, 0 errors.

lein test brainfuck.reader-test

Ran 1 tests containing 1 assertions.
0 failures, 0 errors.

```
