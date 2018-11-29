(ns clojure-osrs.core
  (:require [clojure.core.strint :refer [<<]])
  (:import (java.util.jar JarFile)
           (jdk.internal.org.objectweb.asm ClassReader)
           (jdk.internal.org.objectweb.asm.tree ClassNode)
           (java.util Collections)
           (org.apache.commons.io IOUtils)))

;;returns a byte array from a jarfile and a jar entry
(defn to-bytes [jar, entry]
  (let [stream (.getInputStream jar entry)]
    (IOUtils/toByteArray stream)))

;;returns a classnode from a byte array
(defn to-node [bytes]
  (let [class-node (ClassNode.)]
    (doto (ClassReader. bytes) (.accept class-node ClassReader/EXPAND_FRAMES))
    class-node))

;;returns a map<string, classnode> from supplied path
(defn load-jar [path]
  (let [jar-file (JarFile. path)]
    (let [entries (.entries jar-file)]
      (for [entry (Collections/list entries)]
        (let [node (to-node (to-bytes jar-file entry))]
          {:key (.-name node) :value node})))))

(defn -main
  [& _]
  (println (load-jar "C:\\Users\\TimD\\Documents\\clojure-osrs\\resources\\gamepack_172.jar")))