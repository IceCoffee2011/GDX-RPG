package com.rpsg.rpg.view.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.rpsg.gdxQuery.$;
import com.rpsg.gdxQuery.GdxQuery;
import com.rpsg.gdxQuery.GdxQueryRunnable;
import com.rpsg.rpg.core.RPG;
import com.rpsg.rpg.core.Setting;
import com.rpsg.rpg.object.base.CustomRunnable;
import com.rpsg.rpg.object.base.ObjectRunnable;
import com.rpsg.rpg.object.base.items.Equipment;
import com.rpsg.rpg.object.rpg.Hero;
import com.rpsg.rpg.system.base.Res;
import com.rpsg.rpg.system.ui.CheckBox;
import com.rpsg.rpg.system.ui.CheckBox.CheckBoxStyle;
import com.rpsg.rpg.system.ui.DefaultIView;
import com.rpsg.rpg.system.ui.HeroImage;
import com.rpsg.rpg.system.ui.HoverView;
import com.rpsg.rpg.system.ui.Icon;
import com.rpsg.rpg.system.ui.Image;
import com.rpsg.rpg.system.ui.ImageButton;
import com.rpsg.rpg.system.ui.ImageList;
import com.rpsg.rpg.system.ui.Label;
import com.rpsg.rpg.utils.display.AlertUtil;
import com.rpsg.rpg.utils.game.GameUtil;
import com.rpsg.rpg.view.hover.ConfirmView;

public class EquipView extends DefaultIView{
	
	List<Hero> heros;
	Group inner,data,description;
	ImageList ilist;
	String currentFilter = Equipment.EQUIP_CLOTHES;
	ImageButton takeButton,throwButton;
	Image take=Res.get(Setting.IMAGE_MENU_NEW_EQUIP+"but_take.png"),off=Res.get(Setting.IMAGE_MENU_NEW_EQUIP+"but_off.png"),throwImg=Res.get(Setting.IMAGE_MENU_NEW_EQUIP+"but_remove.png");
	public EquipView init() {
		stage=new Stage(new ScalingViewport(Scaling.stretch, GameUtil.screen_width, GameUtil.screen_height, new OrthographicCamera()),MenuView.stage.getBatch());
		$.add(Res.get(Setting.UI_BASE_IMG)).setSize(137,79).setColor(0,0,0,0.52f).setPosition(240,470).appendTo(stage);
		$.add(Res.get(Setting.UI_BASE_IMG)).setSize(680,48).setColor(0,0,0,0.85f).setPosition(377,486).appendTo(stage);
		CheckBoxStyle cstyle=new CheckBoxStyle();
		cstyle.checkboxOff=Res.getDrawable(Setting.IMAGE_MENU_NEW_EQUIP+"info.png");
		cstyle.checkboxOn=Res.getDrawable(Setting.IMAGE_MENU_NEW_EQUIP+"info_p.png");// help button press
		$.add(new CheckBox("", cstyle, 1)).appendTo(stage).setPosition(880,486).run(new GdxQueryRunnable() {public void run(final GdxQuery self) {self.onClick(new Runnable() {public void run() {
			data.addAction(self.isChecked()?Actions.fadeIn(0.3f):Actions.fadeOut(0.3f));
		}});}});
		
		$.add(Res.get(Setting.UI_BASE_IMG)).setSize(755,282).setColor(.2f,.2f,.2f,0.85f).setPosition(240,178).appendTo(stage);
		
		$.add(Res.get(Setting.UI_BASE_IMG)).setSize(155,155).setColor(0,0,0,0.55f).setPosition(240,12).appendTo(stage);
		$.add(Res.get(Setting.UI_BASE_IMG)).setSize(597,103).setColor(0,0,0,0.55f).setPosition(398,64).appendTo(stage);
		
		$.add(takeButton=new ImageButton(Res.getDrawable(Setting.IMAGE_MENU_NEW_GLOBAL+"button.png"),Setting.UI_BUTTON).setFg(take)).appendTo(stage).setSize(297,49).setPosition(398, 12).getCell().prefSize(297,49);
		
		$.add(throwButton=new ImageButton(Res.getDrawable(Setting.IMAGE_MENU_NEW_GLOBAL+"button.png"),Setting.UI_BUTTON).setFg(throwImg)).appendTo(stage).setSize(297,49).setPosition(698, 12).getCell().prefSize(297,49);
		
		$.add(Res.get(Setting.IMAGE_MENU_NEW_GLOBAL+"m_right.png")).appendTo(stage).setScale(.8f).setPosition(367, 483).onClick(new Runnable() {public void run() {
			next();
		}}).addAction(Actions.fadeIn(0.2f)).setColor(1,1,1,0);
		$.add(Res.get(Setting.IMAGE_MENU_NEW_GLOBAL+"m_right.png")).setScale(.8f).setScaleX(-.8f).appendTo(stage).setPosition(196, 483).onClick(new Runnable() {public void run() {
			prev();
		}}).addAction(Actions.fadeIn(0.2f)).setColor(1,1,1,0);
		
		inner = (Group) $.add(new Group()).appendTo(stage).getItem();
		description = (Group) $.add(new Group()).appendTo(stage).getItem();
		data=(Group) $.add(new Group()).setColor(1,1,1,0).appendTo(stage).getItem();
		
		CheckBoxStyle cstyle2 = new CheckBoxStyle();
		cstyle2.checkboxOff=Res.getDrawable(Setting.IMAGE_MENU_NEW_GLOBAL+"button.png");
		cstyle2.checkboxOff.setMinHeight(47);
		cstyle2.checkboxOff.setMinWidth(75);
		cstyle2.checkboxOn=Res.getDrawable(Setting.IMAGE_MENU_NEW_GLOBAL+"menu_button_select.png");
		cstyle2.checkboxOn.setMinHeight(47);
		cstyle2.checkboxOn.setMinWidth(75);
		int offset = -1,padding = 54,_top = 400;
		ButtonGroup bg = new ButtonGroup(
			(Button)($.add(new CheckBox(Equipment.EQUIP_WEAPON, cstyle2, 0)).setPosition(250, _top-(++offset * padding)).getItem()),
			(Button)($.add(new CheckBox(Equipment.EQUIP_CLOTHES, cstyle2, 0)).setPosition(250, _top-(++offset * padding)).getItem()),
			(Button)($.add(new CheckBox(Equipment.EQUIP_SHOES, cstyle2, 0)).setPosition(250, _top-(++offset * padding)).getItem()),
			(Button)($.add(new CheckBox(Equipment.EQUIP_ORNAMENT1, cstyle2, 0)).setPosition(250, _top-(++offset * padding)).getItem()),
			(Button)($.add(new CheckBox(Equipment.EQUIP_ORNAMENT2, cstyle2, 0)).setPosition(250, _top-(++offset * padding)).getItem())
		);
		
		for(Button but:bg.getButtons()){
			stage.addActor(but);
			final CheckBox box =((CheckBox)but);
			box.setForeground(Res.get(Setting.IMAGE_MENU_NEW_EQUIP+box.getText()+".png").size(40, 40)).setFgOff(37).onClick(new Runnable(){
				public void run() {
					currentFilter = box.getText();
					generate();
				}
			});
			if(but.equals(bg.getButtons().get(0)))
				box.click().setChecked(true);
		}
		
//		stage.setDebugAll(true);
		
		return this;
	}
	
	private void generate() {
		float oldTop = 0f;
		if(ilist!=null)
			oldTop = ilist.getScrollPercentY();
		
		inner.clear();
		data.clear();
		description.clear();
		
		$.add(Res.get(Setting.IMAGE_MENU_NEW_EQUIP+"data.png").disableTouch()).setSize(187, 312).setPosition(838,174).appendTo(data);
		
		ilist=((ImageList) $.add(new ImageList(getEquips(currentFilter))).setSize(655, 266).setPosition(328, 185).appendTo(inner).getItem());
		
		Equipment currentHeroEquip = RPG.ctrl.item.getHeroEquip(parent.current, currentFilter);
		ilist.generate(new Icon().generateIcon(currentHeroEquip, true).setCurrent(true));
		
		ilist.onChange(new CustomRunnable<Icon>() {
			public void run(Icon t) {
				description.clear();
				
				String append = "";
				if(!t.enable){
					takeButton.setFg(take.a(.3f)).fgSelfColor(true).onClick(new Runnable(){public void run() {}});
					append += "当前角色无法使用此装备。";
				}else{
					if(t.current)
						takeButton.setFg(off).onClick(new Runnable(){public void run() {
							RPG.ctrl.item.takeOff(parent.current, currentFilter);
							generate();
						}});
					else
						takeButton.setFg(take.a(1)).fgSelfColor(true).onClick(new Runnable(){public void run() {
							useEquip();
						}});
				}
				
				if(!t.item.throwable || t.current){
					throwButton.setFg(throwImg.a(.3f)).fgSelfColor(true).onClick(new Runnable(){public void run() {}});
					append += "无法丢弃本装备。";
				}else{
					throwButton.setFg(throwImg.a(1)).fgSelfColor(true).onClick(new Runnable(){public void run() {
						removeEquip();
					}});
				}
				
				if(append.length()!=0)
					append = "\n<!-ff0000"+append+">";
				
				$.add(new Label("<!-ff6600"+t.item.name+">",30)).setPosition(395, 157).appendTo(description);
				$.add(new Label(t.item.illustration+append,16).setWidth(558).setYOffset(4)).setPosition(405, 124).appendTo(description);
				$.add(new Image(t)).setPosition(246,18).setSize(143,143).appendTo(description);
			}
		});
		
		ilist.setScrollPercentY(oldTop!=oldTop?0:oldTop);
		
		$.add(new HeroImage(parent.current,0)).appendTo(inner).setPosition(285, 480);
		$.add(new Label(parent.current.name,30)).setPosition(420, 522).appendTo(inner);
		$.add(new Label(parent.current.jname,24).setPad(-7)).setAlpha(.1f).setPosition(420, 503).appendTo(inner);
		
		$.add(Res.get(Setting.UI_BASE_PRO)).setSize((float)parent.current.prop.get("hp")/(float)parent.current.prop.get("maxhp")*161,20).setPosition(851, 456).appendTo(data).setColor(Color.valueOf("c33737"));
		$.add(Res.get(Setting.UI_BASE_PRO)).setSize((float)parent.current.prop.get("mp")/(float)parent.current.prop.get("maxmp")*161,20).setPosition(851, 429).appendTo(data).setColor(Color.valueOf("3762c3"));
		$.add(new Label(parent.current.prop.get("hp")+"/"+parent.current.prop.get("maxhp"),18).align(925, 473).setPad(-8)).setColor(Color.valueOf("2BC706")).appendTo(data);
		$.add(new Label(parent.current.prop.get("mp")+"/"+parent.current.prop.get("maxmp"),18).align(925, 445).setPad(-8)).setColor(Color.YELLOW).appendTo(data);
		
	}
	
	private List<Icon> getEquips(String equipType){
		List<Equipment> equips=RPG.ctrl.item.search(Equipment.class.getSimpleName(),Equipment.class);
		List<Icon> io= new ArrayList<>();
		for(Equipment e:equips){
			if(!e.equipType.equalsIgnoreCase(equipType))
				continue;
			Icon obj=new Icon().generateIcon(e, true);
			if(e.onlyFor!=null && !e.onlyFor.equals(parent.getClass()))//filter by hero class
				obj.enable=false;
			io.add(obj);
		}
		Collections.sort(io);//将无效的内容放在后面
		return io;
	}
	
	private void prev(){
		if(parent.prev())
			generate();
	}
	
	private void next(){
		if(parent.next())
			generate();
	}

	public void draw(SpriteBatch batch) {
		stage.draw();
	}
	
	private void useEquip(){
		if(ilist.getCurrent()!=null && !ilist.getCurrent().current && ilist.getCurrent().item!=null)
			if(!RPG.ctrl.item.use(ilist.getCurrent().item.setUser(parent.current)))
				RPG.putMessage("程序异常，装备失败。", AlertUtil.Red);
		generate();
	}
	
	private void removeEquip(){
		if(ilist.getCurrent()!=null && !ilist.getCurrent().current && ilist.getCurrent().item!=null)
		RPG.popup.add(ConfirmView.getDefault("确定要丢弃装备 <!-ff0000"+ilist.getCurrent().item.name+"> 么？", new ObjectRunnable() {
			public void run(Object view) {
				RPG.ctrl.item.remove(ilist.getCurrent().item);
				RPG.toast.add("丢弃成功。", AlertUtil.Green);
				((HoverView) view).disposed = true;
				generate();
			}
		}));
	}

	public void logic() {
		stage.act();
	}
	
	public void onkeyDown(int keyCode) {
		if(Keys.ESCAPE==keyCode || Keys.X==keyCode)
			this.disposed=true;
		stage.keyDown(keyCode);
	}

}
