package com.sih2026.artisancatalog.domain.model

enum class CraftCategory(val displayNameEn: String, val displayNameHi: String) {
    POTTERY("Pottery & Terracotta", "मिट्टी के बर्तन व टेराकोटा"),
    EMBROIDERY("Textiles & Embroidery", "कपड़े और कढ़ाई"),
    WOODWORK("Woodcraft & Carving", "काष्ठ शिल्प"),
    JEWELRY("Handmade Jewelry", "हस्तनिर्मित आभूषण"),
    METALWORK("Metal Craft & Brass", "धातु व पीतल शिल्प"),
    LEATHER("Traditional Leather", "पारंपरिक चमड़ा शिल्प"),
    OTHER("Handicraft", "हस्तशिल्प")
}
