package com.kucingoyen.core.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.kucingoyen.core.R

private val Nunito = FontFamily(
	Font(R.font.nunito_light, FontWeight.Light),
	Font(R.font.nunito_extra_light, FontWeight.ExtraLight),
	Font(R.font.nunito_regular, FontWeight.Normal),
	Font(R.font.nunito_medium, FontWeight.Medium),
	Font(R.font.nunito_bold, FontWeight.Bold),
	Font(R.font.nunito_semi_bold, FontWeight.SemiBold),
	Font(R.font.nunito_extra_bold, FontWeight.ExtraBold),
)

object BaseFont {
	val headlineExtraLarge = TextStyle(
		fontFamily = Nunito,
		fontWeight = FontWeight.Bold,
		fontSize = 56.sp,
		lineHeight = 78.sp
	)
	val headlineLarge = TextStyle(
		fontFamily = Nunito,
		fontWeight = FontWeight.Bold,
		fontSize = 40.sp,
		lineHeight = 44.sp
	)
	val headlineMediumBold = TextStyle(
		fontFamily = Nunito,
		fontWeight = FontWeight.Bold,
		fontSize = 32.sp,
		lineHeight = 38.sp
	)
	val headlineMedium = TextStyle(
		fontFamily = Nunito,
		fontWeight = FontWeight.Bold,
		fontSize = 40.sp,
		lineHeight = 44.sp
	)
	val headlineSmall = TextStyle(
		fontFamily = Nunito,
		fontWeight = FontWeight.Normal,
		fontSize = 26.sp,
		lineHeight = 32.sp
	)
	val titleLargeBold = TextStyle(
		fontFamily = Nunito,
		fontWeight = FontWeight.Bold,
		fontSize = 23.sp,
		lineHeight = 36.sp
	)
	val titleLarge = TextStyle(
		fontFamily = Nunito,
		fontWeight = FontWeight.SemiBold,
		fontSize = 24.sp,
		lineHeight = 36.sp
	)
	val titleMediumBold = TextStyle(
		fontFamily = Nunito,
		fontWeight = FontWeight.Bold,
		fontSize = 18.sp,
		lineHeight = 26.sp
	)
	val titleMedium = TextStyle(
		fontFamily = Nunito,
		fontWeight = FontWeight.SemiBold,
		fontSize = 18.sp,
		lineHeight = 26.sp
	)
	val titleSmallBold = TextStyle(
		fontFamily = Nunito,
		fontWeight = FontWeight.Bold,
		fontSize = 16.sp,
		lineHeight = 24.sp
	)
	val titleSmall = TextStyle(
		fontFamily = Nunito,
		fontWeight = FontWeight.SemiBold,
		fontSize = 16.sp,
		lineHeight = 24.sp
	)
	val bodyLargeBold = TextStyle(
		fontFamily = Nunito,
		fontWeight = FontWeight.Bold,
		fontSize = 16.sp,
		lineHeight = 22.sp
	)
	val bodyLarge = TextStyle(
		fontFamily = Nunito,
		fontWeight = FontWeight.SemiBold,
		fontSize = 16.sp,
		lineHeight = 22.sp
	)
	val bodyMediumBold = TextStyle(
		fontFamily = Nunito,
		fontWeight = FontWeight.Bold,
		fontSize = 14.sp,
		lineHeight = 20.sp
	)
	val bodyMedium = TextStyle(
		fontFamily = Nunito,
		fontWeight = FontWeight.SemiBold,
		fontSize = 14.sp,
		lineHeight = 20.sp
	)
	val bodySmallBold = TextStyle(
		fontFamily = Nunito,
		fontWeight = FontWeight.Bold,
		fontSize = 12.sp,
		lineHeight = 18.sp
	)
	val bodySmall = TextStyle(
		fontFamily = Nunito,
		fontWeight = FontWeight.SemiBold,
		fontSize = 12.sp,
		lineHeight = 18.sp
	)
	val labelLarge = TextStyle(
		fontFamily = Nunito,
		fontWeight = FontWeight.SemiBold,
		fontSize = 12.sp,
		lineHeight = 18.sp
	)
	val labelMedium = TextStyle(
		fontFamily = Nunito,
		fontWeight = FontWeight.SemiBold,
		fontSize = 10.sp,
		lineHeight = 16.sp
	)
	val labelSmall = TextStyle(
		fontFamily = Nunito,
		fontWeight = FontWeight.SemiBold,
		fontSize = 8.sp,
		lineHeight = 14.sp
	)
	val labelTextField = TextStyle(
		fontFamily = Nunito,
		fontWeight = FontWeight.Normal,
		fontSize = 12.sp,
		lineHeight = 14.sp
	)
	val placeholderTextField = TextStyle(
		fontFamily = Nunito,
		fontWeight = FontWeight.Normal,
		fontSize = 14.sp,
		lineHeight = 24.sp
	)
	val valueTextField = TextStyle(
		color = BaseColor.FocusTextField,
		fontFamily = Nunito,
		fontWeight = FontWeight.Normal,
		fontSize = 14.sp,
		lineHeight = 24.sp
	)
	val errorTextField = TextStyle(
		fontFamily = Nunito,
		fontWeight = FontWeight.Normal,
		fontSize = 12.sp,
		lineHeight = 14.sp
	)
	val buttonText = TextStyle(
		fontFamily = Nunito,
		fontWeight = FontWeight.Bold,
		fontSize = 16.sp,
		lineHeight = 24.sp
	)
}

