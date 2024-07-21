package com.example.openin_app.model

import java.io.Serializable

data class MainResponseData(
    val recent_links: List<LinkResponse>,
    val top_links: List<LinkResponse>,
    val favourite_links: List<LinkResponse>,
    val overall_url_chart: Map<String, Int>
):Serializable
