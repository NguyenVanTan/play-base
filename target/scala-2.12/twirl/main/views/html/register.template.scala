
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

object register extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template0[play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/():play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*1.4*/(""" """),_display_(/*1.6*/main("Welcome to Play")/*1.29*/ {_display_(Seq[Any](format.raw/*1.31*/("""

"""),format.raw/*3.1*/("""<div id="signup_wrapper">
	<div class="join">Sign up</div>
	<div class="lock"></div>
	<div class="clr"></div>
	<div class="login-options">You need to fill out this form.</div>
	<div class="clr"></div>
	<div class="forms">
		<form action="" method="get">
			<table>
				<tr>
					<td><span class="login-options">Full Name:</span></td>
					<td><input id="name" type="text" size="30" onClick="border: 1px solid #30a8da;" /></td>
				</tr>
				<tr>
					<td><span class="login-options">Email Address:</span></td>
					<td><input id="email" type="text" size="30" onClick="border: 1px solid #30a8da;" /></td>
				</tr>
				<tr>
					<td><span class="login-options">Password:</span></td>
					<td><input id="password" type="password" size="30" onClick="border: 1px solid #30a8da;" /></td>
				</tr>
				<tr>
					<td><span class="login-options">Confirm Password:</span></td>
					<td><input id="confirmPassword" type="password" size="30" onClick="border: 1px solid #30a8da;" /></td>
				</tr>
				<tr>
					<td><span class="login-options">Phone Number:</span></td>
					<td><input id="mobile" type="text" size="30" onClick="border: 1px solid #30a8da;" /></td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input id="submit" type="submit" value = "Register" /></td>
				</tr>
			</table>
		</form>
	</div>
</div>
<div class="footer">Copyright by Framgia</div>

""")))}),format.raw/*41.2*/("""
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
                  DATE: Tue Oct 03 14:46:11 ICT 2017
                  SOURCE: C:/Users/TranQuoc/framgia-play-core/app/views/register.scala.html
                  HASH: 3a1262ed633c47ecd7892bdc35290ceb2b68bbb3
                  MATRIX: 944->1|1040->3|1067->5|1098->28|1137->30|1167->34|2600->1437
                  LINES: 28->1|33->1|33->1|33->1|33->1|35->3|73->41
                  -- GENERATED --
              */
          