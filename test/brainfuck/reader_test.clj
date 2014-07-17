(ns brainfuck.reader-test
  (:require [clojure.test :refer :all]
            [brainfuck.reader :refer :all]))

(deftest run-helloworld
  (testing "Executing Hello world Brainfuck source"
    (is (= (exec "resources/helloworld.txt") "Hello World!"))))

(exec "resources/helloworld.txt")
(run-all-tests)
