package chandu0101.scalajs.react.components.materialui


import chandu0101.scalajs.react.components.all._
import chandu0101.scalajs.react.components.materialui.styles.MaterialUICss._
import chandu0101.scalajs.react.components.mixins.ClickAwayable
import chandu0101.scalajs.react.components.util.CommonUtils
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.all._

import scala.scalajs.js



/**
 * Created by chandrasekharkode on 12/5/14.
 *  index: React.PropTypes.number.isRequired,
    text: React.PropTypes.string,
    menuItems: React.PropTypes.array.isRequired,
    zDepth: React.PropTypes.number,
    onItemClick: React.PropTypes.func
 */
object MuiNestedMenuItem {


  case class Props(menuItems: List[MuiMenu.Item], text: String, zDepth: Int, onItemClick: REventIIntStringUnit, classNames: CssClassType, index: Int)

  case class State(open: Boolean)

  class Backend(t: BackendScope[Props, State]) extends ClickAwayable {

    def positionNestedMenu() = {
      val el = t.getDOMNode()
      val nestedMenu = MuiMenu.theMenuRef(t).get.getDOMNode()
      nestedMenu.style.left = el.offsetWidth.toString.concat("px")
    }

    def onParentItemClick(e: ReactEventI, index: Int ,route : String) = {
      e.preventDefault()
      t.modState(s => State(!s.open))
    }

    def onMenuItemClick(e: ReactEventI, index: Int ,route :String = "") = {
      e.preventDefault()
      t.modState(s => State(open = !s.open))
      if (t.props.onItemClick != null) t.props.onItemClick(e, index,route)

    }

    override def onClickAway: Unit = t.modState(_.copy(open = false))

  }

  val component = ReactComponentB[Props]("nestedMenuItem")
    .initialState(State(open = false))
    .backend(new Backend(_))
    .render((P, S, B) => {
      div(classSetM(CommonUtils.cssMapM(P.classNames, mui_nested_menu_item -> true, mui_open -> S.open)))(
        MuiMenuItem(index = P.index, iconRight = mui_icon_arrow_drop_right, onClick = B.onParentItemClick)(
          P.text
        ),
        MuiMenu(ref = MuiMenu.theMenuRef, menuItems = P.menuItems, onItemClick = B.onMenuItemClick, hideable = true, visible = S.open, zDepth = P.zDepth + 1)
      )
    })
    .configure(ClickAwayable.mixin)
    .componentDidMount(scope => scope.backend.positionNestedMenu)
    .componentDidUpdate((scope,_,_) => scope.backend.positionNestedMenu)
    .build

  def apply(menuItems: List[MuiMenu.Item], text: String = "", zDepth: Int = 0, onItemClick: REventIIntStringUnit = null, classNames: CssClassType = Map(), index: Int,ref: js.UndefOr[String] = "", key: js.Any = {}) : ReactElement = {
    component.set(key,ref)(Props(menuItems, text, zDepth, onItemClick, classNames, index))
  }

}
