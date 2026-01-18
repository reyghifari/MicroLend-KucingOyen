package com.kucingoyen.core.theme

import androidx.compose.ui.graphics.Color

object BaseColor {

	object MicroLend {
		val BackgroundColor = Color(0xFFF3D2F0)
		val ButtonBlack = Color(0xFF191919)
		val TextPurple = Color(0xFF5A2D5A)
		val ButtonSecondaryBg = Color(0x33FFFFFF)
	}

	object Passion {
		val Plus70 = Color(0xFF2D4C1B)
		val Plus50 = Color(0xFF447228)
		val Plus20 = Color(0xFF5B9836)
		val Normal = Color(0xFF72BF44)
		val Minus20 = Color(0xFF95CF72)
		val Minus50 = Color(0xFFC6E5B4)
		val Minus70 = Color(0xFFF1F8EC)
	}

	object Irish {
		val Plus70 = Color(0xFF00441F)
		val Plus50 = Color(0xFF00662E)
		val Plus20 = Color(0xFF00883E)
		val Normal = Color(0xFF00AB4E)
		val Minus20 = Color(0xFF40BD63)
		val Minus50 = Color(0xFF99DCAB)
		val Minus70 = Color(0xFFE6F6EA)
	}

	object Emerald {
		val Plus70 = Color(0xFF001E1A)
		val Plus50 = Color(0xFF002E28)
		val Plus20 = Color(0xFF003D35)
		val Normal = Color(0xFF004D43)
		val Minus20 = Color(0xFF407972)
		val Minus50 = Color(0xFF99B7B3)
		val Minus70 = Color(0xFFE6EDEC)
	}

	object Crismon {
		val Plus70 = Color(0xFF67281B)
		val Plus50 = Color(0xFF953A29)
		val Plus20 = Color(0xFFC34D36)
		val Normal = Color(0xFFF15F43)
		val Minus20 = Color(0xFFF47F69)
		val Minus50 = Color(0xFFF9BFB4)
		val Minus70 = Color(0xFFFEEFEC)
	}

	object PumpkinOrange {
		val Plus70 = Color(0xFF59320F)
		val Plus50 = Color(0xFF864B16)
		val Plus20 = Color(0xFFB3641E)
		val Normal = Color(0xFFE07E26)
		val Minus20 = Color(0xFFE79E5C)
		val Minus50 = Color(0xFFF2CBA8)
		val Minus70 = Color(0xFFFBF2E9)
	}

	object ButterYellow {
		val Plus70 = Color(0xFF634F0B)
		val Plus50 = Color(0xFF957610)
		val Plus20 = Color(0xFFC69E16)
		val Normal = Color(0xFFF8C51B)
		val Minus20 = Color(0xFFF9D149)
		val Minus50 = Color(0xFFFCE8A4)
		val Minus70 = Color(0xFFFEF9E8)
	}

	object HazelGold {
		val Plus70 = Color(0xFF685F36)
		val Plus50 = Color(0xFF8D804D)
		val Plus20 = Color(0xFFB1A263)
		val Normal = Color(0xFFD6C47A)
		val Minus20 = Color(0xFFDED095)
		val Minus50 = Color(0xFFEFE7CA)
		val Minus70 = Color(0xFFFBF9F2)
	}

	object TealGreen {
		val Plus70 = Color(0xFF3B6357)
		val Plus50 = Color(0xFF528677)
		val Plus20 = Color(0xFF6AAA97)
		val Normal = Color(0xFF82CDB7)
		val Minus20 = Color(0xFF9BD7C5)
		val Minus50 = Color(0xFFCDEBE2)
		val Minus70 = Color(0xFFF2FAF8)
	}

	object OceanBlue {
		val Plus70 = Color(0xFF0C3239)
		val Plus50 = Color(0xFF114C55)
		val Plus20 = Color(0xFF176572)
		val Normal = Color(0xFF1D7E8E)
		val Minus20 = Color(0xFF69ADB8)
		val Minus50 = Color(0xFFBEE1E7)
		val Minus70 = Color(0xFFEDFBFD)
	}

	object JetBlack {
		val Normal = Color(0xFF323234)
		val Minus20 = Color(0xFF646467)
		val Minus40 = Color(0xFFB1B1B3)
		val Minus50 = Color(0xFFCBCBCD)
		val Minus60 = Color(0xFFCBCBCD)
		val Minus70 = Color(0xFFE0E0E1)
		val Minus80 = Color(0xFFEEEEEF)
		val Minus90 = Color(0xFFF8F8F8)
	}

	val Disable = JetBlack.Minus50
	val Foreground = JetBlack.Minus70
	val Component = Color(0xFFEFEFF0)
	val Background = JetBlack.Minus90
	val White = Color.White
	val Black = Color.Black
	val Error = Crismon.Minus20
	val Link = Irish.Normal
	val Primary = JetBlack.Normal
	val Secondary = JetBlack.Minus20
	val Tertiary = JetBlack.Minus40

	val TextField = JetBlack.Minus80
	val FocusTextField = JetBlack.Normal
	val LabelTextField = Emerald.Normal
	val PlaceHolderTextField = JetBlack.Minus40

	val ButtonGreenColorApp = Emerald.Normal
	val ButtonGrayColorDisableApp = JetBlack.Minus60
	val ButtonTextColorDisableApp = JetBlack.Minus20
}
