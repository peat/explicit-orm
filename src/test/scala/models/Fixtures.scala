package explicit.models

import java.util.Random

object Fixtures {

  def component: Component = Component( material = this.material, quantity = upTo(50) )

  def material: Material = {
    Material(
      partNumber = java.util.UUID.randomUUID().toString()
    )
  }

  // random integer, 1 to max, inclusive
  def upTo(max: Int): Int = (new Random).nextInt(max) + 1

}
