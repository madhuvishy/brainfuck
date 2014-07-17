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

(def output-vector (atom []))

(defn sparse-state
  "If there is no value at pointer insert 0 at pointer location and return new state vector"
  [state pointer]
  (if (<  (count state) (inc pointer)) 
    (assoc state pointer 0)
    state))

(defn ascii 
  "Return the appropriate number/character for an input string"
  [str]
  (try  (Integer/parseInt str)  
        (catch NumberFormatException e (int (first (seq str))))))

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
  (let [state (sparse-state state pointer)]
    (assoc data :state (assoc state pointer (inc (nth state pointer))))))

(defn dec-byte
  "Decrement byte by 1"
  [{state :state pointer :pointer :as data}]
  (let [state (sparse-state state pointer)]
    (assoc data :state (assoc state pointer (dec (nth state pointer))))))

(defn output-byte
  "Print byte at pointer to console"
  [{state :state pointer :pointer :as data}]
  (do
    (swap! output-vector (fn [output] (conj output (char (nth state pointer)))))
    data))

(defn input-byte
  "Set byte at pointer to user input"
  [{state :state pointer :pointer :as data}]
  (let [read-value (read-line)]
    (print (ascii read-value))
    (assoc data :state (assoc state pointer (ascii read-value)))))

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
  [token-list]
  (loop [prog-data {:state [] :pointer 0 :stack [] :token-counter 0}]
    (let [{:keys [state pointer stack token-counter]} prog-data]
      (if (= (inc token-counter) (count token-list)) 
        (apply str @output-vector) 
        (recur ((ns-resolve 'brainfuck.interpreter (symbol (nth token-list token-counter))) 
                    (assoc prog-data :state (sparse-state state pointer) 
                                     :token-counter (inc token-counter))))))))

(defn execute
  "Given source code string interpret and print results"
  [sourcestring]
  (do
    (reset! output-vector [])
    (interpret (parse (seq sourcestring)))))
