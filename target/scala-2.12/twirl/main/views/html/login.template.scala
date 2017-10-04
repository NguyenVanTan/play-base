
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

object login extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template0[play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/():play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*1.4*/(""" """),_display_(/*1.6*/main("Welcome to Play")/*1.29*/ {_display_(Seq[Any](format.raw/*1.31*/("""
"""),format.raw/*2.1*/("""<div id="wrapper">
	<div class="join">Join Today</div>
	<div class="lock"></div>
		<div class="clr"></div>
			<div class="login-options">Choose on of the following sign up methods.</div>
			<a class="facebook" href="#">Facebook</a>
			<a class="google" href="#">Google+</a>
		<div class="clr">
	<hr />
	</div>
	<div class="mail-text">Or sign in using your email address.</div>
	<div class="forms">
		<form action="" method="get" name="register">
			<input id="email" type="text" size="50" onClick="border: 1px solid #30a8da;" />
			<input id="password" type="password" size="50" onClick="border: 1px solid #30a8da;" />
		</form>
	</div>
	<a class="create-acc" href="#">Sign In  </a>
	
	<div class="sign-up"><a href="/register">Sign Up</a></div>
</div>

<div class="footer">Copyright by Framgia</div>

""")))}),format.raw/*26.2*/("""
"""))
      }
    }
  }

  def render(): play.twirl.api.HtmlFormat.Appendable = apply()

  def f:(() => play.twirl.api.HtmlFormat.Appendable) = () => apply()

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  DATE: Wed Oct 04 15:02:56 ICT 2017
                  SOURCE: C:/Users/TranQuoc/framgia-play-core/app/views/login.scala.html
                  HASH: b5d74596d35a5fe20e185fe7a83454e85b46879f
                  MATRIX: 941->1|1037->3|1064->5|1095->28|1134->30|1162->32|2018->858
                  LINES: 28->1|33->1|33->1|33->1|33->1|34->2|58->26
                  -- GENERATED --
              */
          