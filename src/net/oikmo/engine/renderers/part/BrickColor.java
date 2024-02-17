package net.oikmo.engine.renderers.part;

import org.lwjgl.util.vector.Vector3f;

public enum BrickColor {
	
	Black(26, new Vector3f(0,0,0)),
	BrightRed(21, new Vector3f(196,40,27)),
	BrightYellow(24, new Vector3f(245,205,47)),
	CoolYellow(226, new Vector3f(253, 234, 140)),
	SandBlue(135, new Vector3f(116,134,156)),
	BrightOrange(106, new Vector3f(218,133,64)),
	BrownishYellowishOrange(105, new Vector3f(226,155,63)),
	White(1, new Vector3f(242,243,242)),
	DarkWhite(199, new Vector3f(99,95,97)),
	LightWhite(208, new Vector3f(229,228,222)),
	MediumWhite(194, new Vector3f(163,162,164)),
	EarthGreen(141, new Vector3f(39,70,44)),
	DarkGreen(28, new Vector3f(40,127,70)),
	BrightGreen(37, new Vector3f(75,151,74)),
	BrownishYellowishGreen(119, new Vector3f(164,189,70)),
	MediumGreen(29, new Vector3f(161,196,139)),
	SandGreen(151, new Vector3f(120,144,129)),
	PastelBlue(11, new Vector3f(128,187,219)),
	MediumBlue(102, new Vector3f(110,153,201)),
	Cyan(107, new Vector3f(0,143,155)),
	BrightBlue(23, new Vector3f(13,105,171)),
	LightBlue(45, new Vector3f(180,210,227)),
	DarkOrange(38, new Vector3f(160,95,52)),
	ReddishBrown(192, new Vector3f(105,64,39)),
	BrightViolet(104, new Vector3f(107,50,123)),
	LightReddishViolet(9, new Vector3f(232,186,199)),
	MediumRed(101, new Vector3f(218,134,121)),
	BrickYellow(5, new Vector3f(215,197,153)),
	SandRed(153, new Vector3f(149,121,118)),
	Brown(217, new Vector3f(124,92,69)),
	Nougat(18, new Vector3f(204,142,104)),
	LightOrange(125, new Vector3f(234,184,145));
	
	private final int intValue;
	private final Vector3f value;
	BrickColor(int originalValue, Vector3f value) {
		this.intValue = originalValue;
		this.value = value;
	}
	
	public static BrickColor getEnumFromValue(int value) {
		for(int i = 0; i < values().length; i++) {
			if(values()[i].getIntValue() == value) {
				return values()[i];
			}
		}
		return Black;
	}
	
	public int getIntValue() {
		return intValue;
	}
	
	public Vector3f getValue() {
		return value;
	}
}
