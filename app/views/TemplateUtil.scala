package views

import play.api.i18n.{Lang, Messages}

object TemplateUtil {
  def i(key: String, args:String*)(implicit lang: Lang): String = Messages(key, args.toArray:_*)

  def isAuthenticated(implicit session: play.mvc.Http.Session): Boolean = session.get("username") != null

  def usernameOption(implicit session: play.mvc.Http.Session): Option[String] = Option(session.get("username"))

  def username(implicit session: play.mvc.Http.Session): String = session.get("username")
}