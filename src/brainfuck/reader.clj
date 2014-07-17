(ns brainfuck.reader
  (:require [brainfuck.interpreter :as interpreter]))

(defn exec
  [source-file]
  (interpreter/execute (slurp source-file)))

(defn -main
  "Main reader method that reads a source file and calls the execute method"
  [source-file]
  (if source-file
    (println (exec source-file) )))


