package net.oikmo.engine.renderers.part;

import org.lwjgl.util.vector.Vector3f;

public enum BrickColor {
	
	
	White(1, new Vector3f(0.94902f, 0.952941f, 0.94902f)),
	Grey(2, new Vector3f(0.631373f, 0.647059f, 0.635294f)),
	LightYellow(3, new Vector3f(0.976471f, 0.913726f, 0.6f)),
	BrickYellow(5, new Vector3f(0.843137f, 0.772549f, 0.6f)),
	LightGreenMint(6, new Vector3f(0.760784f, 0.854902f, 0.721569f)),
	LightReddishViolet(9, new Vector3f(0.909804f, 0.729412f, 0.780392f)),
	PastelBlue(11, new Vector3f(0.501961f, 0.733333f, 0.858824f)),
	LightOrangeBrown(12, new Vector3f(0.796079f, 0.517647f, 0.258824f)),
	Nougat(18, new Vector3f(0.8f, 0.556863f, 0.407843f)),
	BrightRed(21, new Vector3f(0.768628f, 0.156863f, 0.105882f)),
	MedReddishViolet(22, new Vector3f(0.768628f, 0.439216f, 0.627451f)),
	BrightBlue(23, new Vector3f(0.0509804f, 0.411765f, 0.670588f)),
	BrightYellow(24, new Vector3f(0.960784f, 0.803922f, 0.184314f)),
	EarthOrange(25, new Vector3f(0.384314f, 0.278431f, 0.196078f)),
	Black(26, new Vector3f(0.105882f, 0.164706f, 0.203922f)),
	DarkGrey(27, new Vector3f(0.427451f, 0.431373f, 0.423529f)),
	DarkGreen(28, new Vector3f(0.156863f, 0.498039f, 0.27451f)),
	MediumGreen(29, new Vector3f(0.631373f, 0.768628f, 0.545098f)),
	LigYellowichOrange(36, new Vector3f(0.952941f, 0.811765f, 0.607843f)),
	BrightGreen(37, new Vector3f(0.294118f, 0.592157f, 0.290196f)),
	DarkOrange(38, new Vector3f(0.627451f, 0.372549f, 0.203922f)),
	LightBluishViolet(39, new Vector3f(0.756863f, 0.792157f, 0.870588f)),
	Transparent(40, new Vector3f(0.92549f, 0.92549f, 0.92549f)),
	TrRed(41, new Vector3f(0.803922f, 0.329412f, 0.294118f)),
	TrLgBlue(42, new Vector3f(0.756863f, 0.87451f, 0.941177f)),
	TrBlue(43, new Vector3f(0.482353f, 0.713726f, 0.909804f)),
	TrYellow(44, new Vector3f(0.968628f, 0.945098f, 0.552941f)),
	LightBlue(45, new Vector3f(0.705882f, 0.823529f, 0.890196f)),
	TrFluReddishOrange(47, new Vector3f(0.85098f, 0.521569f, 0.423529f)),
	TrGreen(48, new Vector3f(0.517647f, 0.713726f, 0.552941f)),
	TrFluGreen(49, new Vector3f(0.972549f, 0.945098f, 0.517647f)),
	PhosphWhite(50, new Vector3f(0.92549f, 0.909804f, 0.870588f)),
	LightRed(100, new Vector3f(0.933333f, 0.768628f, 0.713726f)),
	MediumRed(101, new Vector3f(0.854902f, 0.52549f, 0.47451f)),
	MediumBlue(102, new Vector3f(0.431373f, 0.6f, 0.788235f)),
	LightGrey(103, new Vector3f(0.780392f, 0.756863f, 0.717647f)),
	BrightViolet(104, new Vector3f(0.419608f, 0.196078f, 0.482353f)),
	BrYellowishOrange(105, new Vector3f(0.886275f, 0.607843f, 0.247059f)),
	BrightOrange(106, new Vector3f(0.854902f, 0.521569f, 0.25098f)),
	BrightBluishGreen(107, new Vector3f(1, 0.560784f, 0.607843f)),
	EarthYellow(108, new Vector3f(0.407843f, 0.360784f, 0.262745f)),
	BrightBluishViolet(110, new Vector3f(0.262745f, 0.329412f, 0.576471f)),
	TrBrown(111, new Vector3f(0.74902f, 0.717647f, 0.694118f)),
	MediumBluishViolet(112, new Vector3f(0.407843f, 0.454902f, 0.67451f)),
	TrMediReddishViolet(113, new Vector3f(0.894118f, 0.678431f, 0.784314f)),
	MedYellowishGreen(115, new Vector3f(0.780392f, 0.823529f, 0.235294f)),
	MedBluishGreen(116, new Vector3f(0.333333f, 0.647059f, 0.686275f)),
	LightBluishGreen(118, new Vector3f(0.717647f, 0.843137f, 0.835294f)),
	BrYellowishGreen(119, new Vector3f(0.643137f, 0.741176f, 0.27451f)),
	LigYellowishGreen(120, new Vector3f(0.85098f, 0.894118f, 0.654902f)),
	MedYellowishOrange(121, new Vector3f(0.905882f, 0.67451f, 0.345098f)),
	BrReddishOrange(123, new Vector3f(0.827451f, 0.435294f, 0.298039f)),
	BrightReddishViolet(124, new Vector3f(0.572549f, 0.223529f, 0.470588f)),
	LightOrange(125, new Vector3f(0.917647f, 0.721569f, 0.568627f)),
	TrBrightBluishViolet(126, new Vector3f(0.647059f, 0.647059f, 0.796079f)),
	Gold(127, new Vector3f(0.862745f, 0.737255f, 0.505882f)),
	DarkNougat(128, new Vector3f(0.682353f, 0.478431f, 0.34902f)),
	Silver(131, new Vector3f(0.611765f, 0.639216f, 0.658824f)),
	NeonOrange(133, new Vector3f(0.835294f, 0.45098f, 0.239216f)),
	NeonGreen(134, new Vector3f(0.847059f, 0.866667f, 0.337255f)),
	SandBlue(135, new Vector3f(0.454902f, 0.52549f, 0.611765f)),
	SandViolet(136, new Vector3f(0.529412f, 0.486275f, 0.564706f)),
	MediumOrange(137, new Vector3f(0.878431f, 0.596078f, 0.392157f)),
	SandYellow(138, new Vector3f(0.584314f, 0.541176f, 0.45098f)),
	EarthBlue(140, new Vector3f(0.12549f, 0.227451f, 0.337255f)),
	EarthGreen(141, new Vector3f(0.152941f, 0.27451f, 0.172549f)),
	TrFluBlue(143, new Vector3f(0.811765f, 0.886275f, 0.968628f)),
	SandBlueMetallic(145, new Vector3f(0.47451f, 0.533333f, 0.631373f)),
	SandVioletMetallic(146, new Vector3f(0.584314f, 0.556863f, 0.639216f)),
	SandYellowMetallic(147, new Vector3f(0.576471f, 0.529412f, 0.403922f)),
	DarkGreyMetallic(148, new Vector3f(0.341176f, 0.345098f, 0.341176f)),
	BlackMetallic(149, new Vector3f(0.0862745f, 0.113725f, 0.196078f)),
	LightGreyMetallic(150, new Vector3f(0.670588f, 0.678431f, 0.67451f)),
	SandGreen(151, new Vector3f(0.470588f, 0.564706f, 0.505882f)),
	SandRed(153, new Vector3f(0.584314f, 0.47451f, 0.462745f)),
	DarkRed(154, new Vector3f(0.482353f, 0.180392f, 0.184314f)),
	TrFluYellow(1, new Vector3f(1, 0.964706f, 0.482353f)),
	TrFluRed(158, new Vector3f(0.882353f, 0.643137f, 0.760784f)),
	GunMetallic(168, new Vector3f(0.458824f, 0.423529f, 0.384314f)),
	RedFlipFlop(176, new Vector3f(0.592157f, 0.411765f, 0.356863f)),
	YellowFlipFlop(178, new Vector3f(0.705882f, 0.517647f, 0.333333f)),
	SilverFlipFlop(179, new Vector3f(0.537255f, 0.529412f, 0.533333f)),
	Curry(180, new Vector3f(0.843137f, 0.662745f, 0.294118f)),
	FireYellow(190, new Vector3f(0.976471f, 0.839216f, 0.180392f)),
	FlameYellowishOrange(191, new Vector3f(0.909804f, 0.670588f, 0.176471f)),
	ReddishBrown(192, new Vector3f(0.411765f, 0.25098f, 0.152941f)),
	FlameReddishOrange(193, new Vector3f(0.811765f, 0.376471f, 0.141176f)),
	MediumStoneGrey(194, new Vector3f(0.639216f, 0.635294f, 0.643137f)),
	RoyalBlue(195, new Vector3f(0.27451f, 0.403922f, 0.643137f)),
	DarkRoyalBlue(196, new Vector3f(0.137255f, 0.278431f, 0.545098f)),
	BrightReddishLilac(198, new Vector3f(0.556863f, 0.258824f, 0.521569f)),
	DarkStoneGrey(199, new Vector3f(0.388235f, 0.372549f, 0.380392f)),
	LemonMetalic(200, new Vector3f(0.509804f, 0.541176f, 0.364706f)),
	LightStoneGrey(208, new Vector3f(0.898039f, 0.894118f, 0.870588f)),
	DarkCurry(209, new Vector3f(0.690196f, 0.556863f, 0.266667f)),
	FadedGreen(210, new Vector3f(0.439216f, 0.584314f, 0.470588f)),
	Turquoise(211, new Vector3f(0.47451f, 0.709804f, 0.709804f)),
	LightRoyalBlue(212, new Vector3f(0.623529f, 0.764706f, 0.913726f)),
	MediumRoyalBlue(213, new Vector3f(0.423529f, 0.505882f, 0.717647f)),
	Rust(216, new Vector3f(0.560784f, 0.298039f, 0.164706f)),
	Brown(217, new Vector3f(0.486275f, 0.360784f, 0.270588f)),
	ReddishLilac(218, new Vector3f(0.588235f, 0.439216f, 0.623529f)),
	Lilac(219, new Vector3f(0.419608f, 0.384314f, 0.607843f)),
	LightLilac(220, new Vector3f(0.654902f, 0.662745f, 0.807843f)),
	BrightPurple(221, new Vector3f(0.803922f, 0.384314f, 0.596078f)),
	LightPurple(222, new Vector3f(0.894118f, 0.678431f, 0.784314f)),
	LightPink(223, new Vector3f(0.862745f, 0.564706f, 0.584314f)),
	LightBrickYellow(224, new Vector3f(0.941177f, 0.835294f, 0.627451f)),
	WarmYellowishOrange(225, new Vector3f(0.921569f, 0.721569f, 0.498039f)),
	CoolYellow(226, new Vector3f(0.992157f, 0.917647f, 0.54902f)),
	DoveBlue(232, new Vector3f(0.490196f, 0.733333f, 0.866667f)),
	MediumLilac(268, new Vector3f(0.203922f, 0.168627f, 0.458824f));
	
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
