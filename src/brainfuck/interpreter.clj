(ns brainfuck.interpreter)

(def tokens
  {\> "inc-pointer"
   \< "dec-pointer"
   \+ "inc-byte" 
   \- "dec-byte" 
   \. "output-byte" 
   \, "input-byte" 
   \[ "jump-forward" 
   \] "jump-backward"})

(def source1 "++++++++[>++++[>++>+++>+++>+<<<<-]
             >+>+>->>+[<]<-]>>.>---.+++++++..+++.>>.<-.<.
             +++.------.--------.>>+.>++.")

(def source "++++++++>++++>++>+++>+++>+<<<<-
             >+>+>->>+<<->>.>---.+++++++..+++.>>.<-.<.
             +++.------.--------.>>+.>++.")

(defn inc-pointer
  "Increments pointer by 1"
  [state pointer]
  {:state state :pointer (inc pointer)})

(defn dec-pointer
  "Decrements pointer by 1"
  [state pointer]
  {:state state :pointer (dec pointer)})

(defn inc-byte
  "Increment byte by 1"
  [state pointer]
  {:state (assoc state pointer (inc (nth state pointer))) :pointer pointer})

(defn dec-byte
  "Decrement byte by 1"
  [state pointer]
  {:state (assoc state pointer (dec (nth state pointer))) :pointer pointer})

(defn output-byte
  "Print byte at pointer to console"
  [state pointer]
  (do
    (print (nth state pointer))
    {:state state :pointer pointer}))

(defn input-byte
  "Set byte at pointer to user input"
  [state pointer]
  {:state (assoc state pointer (read-line)) :pointer pointer})

(defn parse
  [source]
  "Parse source to tokens"
  (filter identity (map #(tokens %) source)))

(defn interpret
  "Interpret source tokens"
  [tokens]
  (let [num-tokens (count tokens)]
    (loop [data {:state (vec (repeat 1042 0)) :pointer 0}
           call-stack []
           token-counter 0]
      (if (= token-counter num-tokens) 
        data
        (recur 
          ((resolve (symbol (nth tokens token-counter))) (:state data) (:pointer data)) 
          call-stack 
          (inc token-counter))))))


(interpret (parse (seq source)))
