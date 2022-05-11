package com.nicer.attiary.data.api.nlp.classification

import com.google.gson.annotations.SerializedName

data class Classification(
	@SerializedName("anger")
	val anger : Float,

	@SerializedName("anxiety")
	val anxiety : Float,

	@SerializedName("depression")
	val depression : Float,

	@SerializedName("hope")
	val hope : Float,

	@SerializedName("joy")
	val joy : Float,

	@SerializedName("neutrality")
	val neutrality : Float,

	@SerializedName("regret")
	val regret : Float,

	@SerializedName("sadness")
	val sadness : Float,

	@SerializedName("tiredness")
	val tiredness : Float

)