
// @GENERATOR:play-routes-compiler
// @SOURCE:C:/Users/TranQuoc/framgia-play-core/conf/routes
// @DATE:Tue Oct 03 13:19:57 ICT 2017


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
