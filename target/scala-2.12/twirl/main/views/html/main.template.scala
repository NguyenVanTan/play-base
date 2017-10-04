
package views.html

import _root_.play.twirl.api.TwirlFeatureImports._
import _root_.play.twirl.api.TwirlHelperImports._
import _root_.play.twirl.api.Html
import _root_.play.twirl.api.JavaScript
import _root_.play.twirl.api.Txt
import _root_.play.twirl.api.Xml
import models._
import controllers._
import play.api.i18n._
import views.html._
import play.api.templates.PlayMagic._
import java.lang._
import java.util._
import scala.collection.JavaConverters._
import play.core.j.PlayMagicForJava._
import play.mvc._
import play.api.data.Field
import play.mvc.Http.Context.Implicit._
import play.data._
import play.core.j.PlayFormsMagicForJava._

object main extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template2[String,Html,play.twirl.api.HtmlFormat.Appendable] {

  /* * This template is called from the `index` template. This template 
 * handles the rendering of the page header and body tags. It takes
 * two arguments, a `String` for the title of the page and an `Html`
 * object to insert into the body of the page. 
*/
  def apply/*7.2*/(title: String)(content: Html):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*7.32*/("""

"""),format.raw/*9.1*/("""<!DOCTYPE html>
<html lang="en">
<head>
	"""),format.raw/*12.55*/("""
	"""),format.raw/*13.2*/("""<title>"""),_display_(/*13.10*/title),format.raw/*13.15*/("""</title>
	<link rel="stylesheet" media="screen" href=""""),_display_(/*14.47*/routes/*14.53*/.Assets.versioned("stylesheets/main.css")),format.raw/*14.94*/("""">
	<link rel="stylesheet" media="screen" href=""""),_display_(/*15.47*/routes/*15.53*/.Assets.versioned("stylesheets/login.css")),format.raw/*15.95*/("""">
	<link rel="stylesheet" media="screen" href=""""),_display_(/*16.47*/routes/*16.53*/.Assets.versioned("stylesheets/register.css")),format.raw/*16.98*/("""">
	<link rel="shortcut icon" type="image/png" href=""""),_display_(/*17.52*/routes/*17.58*/.Assets.versioned("images/favicon.png")),format.raw/*17.97*/("""">
</head>
<body>
	"""),format.raw/*20.81*/(""" 
	
	"""),_display_(/*22.3*/content),format.raw/*22.10*/("""

	"""),format.raw/*24.2*/("""<script src=""""),_display_(/*24.16*/routes/*24.22*/.Assets.versioned("javascripts/main.js")),format.raw/*24.62*/("""" type="text/javascript"></script>
	<script src=""""),_display_(/*25.16*/routes/*25.22*/.Assets.versioned("javascripts/login.js")),format.raw/*25.63*/("""" type="text/javascript"></script>
</body>
</html>
"""))
      }
    }
  }

  def render(title:String,content:Html): play.twirl.api.HtmlFormat.Appendable = apply(title)(content)

  def f:((String) => (Html) => play.twirl.api.HtmlFormat.Appendable) = (title) => (content) => apply(title)(content)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  DATE: Wed Oct 04 15:02:56 ICT 2017
                  SOURCE: C:/Users/TranQuoc/framgia-play-core/app/views/main.scala.html
                  HASH: 0497e835e59e61861eadfac2fea711ab49bf298c
                  MATRIX: 1210->268|1335->298|1365->302|1437->399|1467->402|1502->410|1528->415|1611->471|1626->477|1688->518|1765->568|1780->574|1843->616|1920->666|1935->672|2001->717|2083->772|2098->778|2158->817|2208->918|2242->926|2270->933|2302->938|2343->952|2358->958|2419->998|2497->1049|2512->1055|2574->1096
                  LINES: 32->7|37->7|39->9|42->12|43->13|43->13|43->13|44->14|44->14|44->14|45->15|45->15|45->15|46->16|46->16|46->16|47->17|47->17|47->17|50->20|52->22|52->22|54->24|54->24|54->24|54->24|55->25|55->25|55->25
                  -- GENERATED --
              */
          