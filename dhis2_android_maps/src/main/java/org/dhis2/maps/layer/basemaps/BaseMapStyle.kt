package org.dhis2.maps.layer.basemaps

import com.google.gson.annotations.SerializedName

object BaseMapStyleBuilder {
    fun build(id: String, tileUrls: List<String>, attribution: String, isDefault: Boolean) = BaseMapStyle(
        version = 8,
        sources = StyleSources(
            rasterTiles = RasterTiles(
                type = "raster",
                tiles = tileUrls,
                tileSize = 256,
            ),
            attribution = attribution,
        ),
        layers = listOf(
            StyleLayers(
                id = "simple-tiles",
                type = "raster",
                source = "raster-tiles",
                minZoom = 0,
                maxZoom = 22,
            ),
        ),
        id = id,
        glyphs = DEFAULT_GLYPH_URL,
        isDefault = isDefault,
    )

    fun internalBaseMap(): BaseMapStyle {
        return build(
            id = OSM_LIGHT,
            tileUrls = listOf(
                DEFAULT_TILE_URL.replace("{s}", "a"),
                DEFAULT_TILE_URL.replace("{s}", "b"),
                DEFAULT_TILE_URL.replace("{s}", "c"),
                DEFAULT_TILE_URL.replace("{s}", "d"),
            ),
            attribution = DEFAULT_ATTRIBUTION,
            isDefault = true,
        )
    }
}

data class BaseMapStyle(
    val version: Int,
    val sources: StyleSources,
    val layers: List<StyleLayers>,
    val id: String,
    var glyphs: String,
    val isDefault: Boolean,
)

data class StyleSources(
    @SerializedName("raster-tiles") val rasterTiles: RasterTiles,
    val attribution: String,
)

data class RasterTiles(
    val type: String,
    val tiles: List<String>,
    val tileSize: Int,
)

data class StyleLayers(
    val id: String,
    val type: String,
    val source: String,
    val minZoom: Int,
    val maxZoom: Int,
)
