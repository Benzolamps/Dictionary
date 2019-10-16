package main;

import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * 可克隆的菜单是JMenu的子类，实现了Cloneable接口；
 * 一个菜单的引用只能被一个组件选中，需要使用结构一模一样的菜单，故开辟此方法
 *
 * @author Benzolamps
 */
public class CloneableMenu extends JMenu implements Cloneable {
    private static final long serialVersionUID = -7199663284269505961L;

    MainFrame frame; // 主界面

    public CloneableMenu(String name, MainFrame frame) {
        super(name);
        this.frame = frame;
    }

    /*
     * 重写Cloneable接口的clone()方法，当菜单中的子菜单全部采用该类时，通过递归的方法深度克隆该菜单
     */
    @Override
    public CloneableMenu clone() {
        CloneableMenu cm = new CloneableMenu(getText(), frame); // 克隆菜单名
        for (int i = 0; i < getItemCount(); i++) {
            if (getItem(i) == null) {
                cm.addSeparator(); // 加入分隔符
            } else {
                if (getItem(i).getClass().getName().equals("main.CloneableMenu")) { // 当菜单为该类的对象时，递归调用该方法
                    CloneableMenu temp = ((CloneableMenu) getItem(i)).clone();
                    temp.setMnemonic(getItem(i).getMnemonic()); // 克隆加速键
                    cm.add(temp);
                } else { // 当菜单为菜单项时，执行以下操作
                    JMenuItem temp = new JMenuItem(getItem(i).getText()); // 克隆菜单名
                    temp.setMnemonic(getItem(i).getMnemonic());
                    for (ActionListener a : getItem(i).getActionListeners()) { // 克隆事件监听器
                        temp.addActionListener(a);
                    }
                    cm.add(temp);
                }
            }
        }
        return cm;
    }
}
