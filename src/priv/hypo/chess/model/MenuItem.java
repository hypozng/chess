package priv.hypo.chess.model;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/7/11.
 */
public class MenuItem {
    // 菜单文本
    private String text;

    // 动作
    private ActionListener action;

    private List<MenuItem> subMenus = new ArrayList<MenuItem>();

    public MenuItem() {

    }

    public MenuItem(String text) {
        this.text = text;
    }

    public MenuItem(String text, ActionListener action) {
        this.text = text;
        this.action = action;
    }

    public MenuItem(String text, List<MenuItem> subMenus) {
        this.text = text;
        this.subMenus = subMenus;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ActionListener getAction() {
        return action;
    }

    public void setAction(ActionListener action) {
        this.action = action;
    }

    public List<MenuItem> getSubMenus() {
        return subMenus;
    }

    public void setSubMenus(List<MenuItem> subMenus) {
        this.subMenus = subMenus;
    }
}
