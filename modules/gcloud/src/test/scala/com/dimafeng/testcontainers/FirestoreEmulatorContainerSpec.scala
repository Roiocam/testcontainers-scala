package com.dimafeng.testcontainers

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import scala.collection.JavaConverters._

class FirestoreEmulatorContainerSpec extends AnyWordSpecLike with Matchers with ForAllTestContainer {

  override val container: FirestoreEmulatorContainer = FirestoreEmulatorContainer()

  "Firestore emulator container" should {

    "be started" in {
      val firestore = container.firestoreOptions.getService
      try {
        val users = firestore.collection("users")
        val docRef = users.document("alovelace")
        val data = Map[String, Any]("first" -> "Ada", "last" -> "Lovelace")
        val eventualInit = docRef.set(data.asJava)
        eventualInit.get()

        val eventualSnapshot = users.get()
        val snapshot = eventualSnapshot.get()

        snapshot.getDocuments.get(0).getData.get("first") shouldEqual "Ada"
      } finally firestore.close()
    }

  }

}
