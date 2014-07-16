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

(defn inc-pointer
  "Increments pointer by 1"
  [{pointer :pointer :as data}]
  (assoc data :pointer (inc pointer)))

(defn dec-pointer
  "Decrements pointer by 1"
  [{pointer :pointer :as data}]
  (assoc data :pointer (dec pointer)))

(defn inc-byte
  "Increment byte by 1"
  [{state :state pointer :pointer :as data}]
  (assoc data :state (assoc state pointer (inc (nth state pointer)))))

(defn dec-byte
  "Decrement byte by 1"
  [{state :state pointer :pointer :as data}]
  (assoc data :state (assoc state pointer (dec (nth state pointer)))))

(defn output-byte
  "Print byte at pointer to console"
  [{state :state pointer :pointer :as data}]
  (do
    (print (char (nth state pointer)))
    data))

(defn input-byte
  "Set byte at pointer to user input"
  [{state :state pointer :pointer :as data}]
  (assoc data :state (assoc state pointer (read-line))))

(defn jump-forward
  "Go to next instruction if data at pointer is not 0 else jump to closest ]"
  [{:keys [state pointer stack token-counter] :as data}]
  (if (not= (dec token-counter) (last stack)) 
    (assoc data :stack (conj stack (dec token-counter)))
    data))

(defn jump-backward
  "Go to next instruction if data at pointer is 0 else jump back to closest ["
  [{:keys [state pointer stack token-counter] :as data}]
  (if (> (nth state pointer) 0)
    (assoc data :token-counter (last stack)) 
    (assoc data :stack (pop stack))))

(defn parse
  [source]
  "Parse source to tokens"
  (filter identity (map #(tokens %) source)))

(defn interpret
  "Interpret source tokens"
  [tokens]
  (loop [prog-data {:state (vec (repeat 15 0)) :pointer 0 :stack [] :token-counter 0}]
    (let [token-counter (:token-counter prog-data)]
      (if (= token-counter (count tokens)) 
        prog-data 
        (recur ((resolve (symbol (nth tokens token-counter))) 
                    (assoc prog-data :token-counter (inc token-counter))))))))


(interpret (parse (seq source1)))
