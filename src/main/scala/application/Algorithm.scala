package application

import org.apache.spark.rdd.RDD

trait Algorithm {
  def getKeyValuesWithOddOccurrences(keyValues: RDD[KeyValue]): RDD[KeyValueWithOddOccurrences]
}

object DefaultAlgorithm extends Algorithm {
  def getKeyValuesWithOddOccurrences(keyValues: RDD[KeyValue]): RDD[KeyValueWithOddOccurrences] = {
    keyValues
      .sortBy(_._1)
      .groupBy(_._1)
      .mapValues { keyValues =>
        keyValues.map(_._2)
          .groupBy(identity(_))
          .view.mapValues(_.size)
          .toMap.find(_._2 % 2 == 1)
      }
  }
}
