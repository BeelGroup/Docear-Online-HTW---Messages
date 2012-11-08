package views

import play.api.i18n.{Lang, Messages}

object TemplateUtil {
  def i(key: String, args:String*)(implicit lang: Lang): String = Messages(key, args.toArray:_*)
}