(ns brainfuck.reader
  (:require [brainfuck.interpreter :as interpreter]))


(defn -main
  "Main reader method that reads a source file and calls the execute method"
  [source-file]
  (if source-file
    (println (interpreter/execute (slurp source-file)))))


