package com.sih2026.artisancatalog.domain.ai

import com.sih2026.artisancatalog.domain.model.CraftCategory
import com.sih2026.artisancatalog.domain.model.ProductAiResult
import java.util.Locale

/**
 * Offline AI implementation using intelligent craft ontology, keyword entity extraction,
 * and rule-based NLP catalog generation.
 * This runs 100% on-device with zero network requests and can be seamlessly replaced
 * by a quantized local LLM (e.g., MediaPipe LLM Inference / Gemma on-device) later.
 */
class OfflineAIService : AIService {

    override val isOfflineOnly: Boolean = true

    override suspend fun generateProductDetails(
        voiceTranscription: String,
        targetLanguage: String
    ): ProductAiResult {
        val cleanInput = voiceTranscription.trim()
        val lower = cleanInput.lowercase(Locale.ROOT)

        // 1. Detect Category & Keywords
        val (category, detectedKeywords) = detectCategoryAndKeywords(lower)

        // 2. Generate Title, Description & Artisan History based on language
        return if (targetLanguage.startsWith("hi", ignoreCase = true) || containsHindiCharacters(cleanInput)) {
            generateHindiCatalog(cleanInput, category, detectedKeywords)
        } else {
            generateEnglishCatalog(cleanInput, category, detectedKeywords)
        }
    }

    override suspend fun translateText(text: String, targetLanguage: String): String {
        // Offline heuristic translation dictionary for demo
        return if (targetLanguage.startsWith("hi", ignoreCase = true)) {
            when {
                text.contains("Handmade Traditional Embroidered Cotton Bag", ignoreCase = true) ->
                    "हस्तनिर्मित पारंपरिक कढ़ाई वाला सूती थैला (बैग)"
                text.contains("Handmade", ignoreCase = true) -> "हस्तनिर्मित पारंपरिक शिल्प उत्पाद"
                else -> text
            }
        } else {
            text
        }
    }

    private fun detectCategoryAndKeywords(input: String): Pair<CraftCategory, List<String>> {
        val keywords = mutableListOf<String>()

        if (input.contains("pot") || input.contains("clay") || input.contains("terracotta") ||
            input.contains("kulhad") || input.contains("मिट्टी") || input.contains("कुल्हड़") || input.contains("मटका")) {
            keywords.addAll(listOf("Terracotta", "Natural Clay", "Wheel-thrown", "Eco-friendly"))
            return Pair(CraftCategory.POTTERY, keywords)
        }

        if (input.contains("bag") || input.contains("cotton") || input.contains("embroidery") ||
            input.contains("thread") || input.contains("cloth") || input.contains("suit") ||
            input.contains("saree") || input.contains("दुपट्टा") || input.contains("कढ़ाई") || input.contains("थैला")) {
            keywords.addAll(listOf("Handloom Cotton", "Zardozi / Kantha Stitch", "Artisan Embroidery", "Sustainable"))
            return Pair(CraftCategory.EMBROIDERY, keywords)
        }

        if (input.contains("wood") || input.contains("carv") || input.contains("teak") ||
            input.contains("लकड़ी") || input.contains("काष्ठ")) {
            keywords.addAll(listOf("Seasoned Wood", "Hand-carved", "Natural Polish", "Heirloom"))
            return Pair(CraftCategory.WOODWORK, keywords)
        }

        if (input.contains("brass") || input.contains("metal") || input.contains("copper") ||
            input.contains("घंटी") || input.contains("पीतल") || input.contains("तांबा")) {
            keywords.addAll(listOf("Dhokra / Brass Cast", "Hand-beaten", "Antique Finish", "Rust-resistant"))
            return Pair(CraftCategory.METALWORK, keywords)
        }

        if (input.contains("jewel") || input.contains("bead") || input.contains("earring") ||
            input.contains("necklace") || input.contains("माला") || input.contains("झुमका")) {
            keywords.addAll(listOf("Handmade Beads", "Traditional Motif", "Hypoallergenic", "Festive Wear"))
            return Pair(CraftCategory.JEWELRY, keywords)
        }

        if (input.contains("leather") || input.contains("jooti") || input.contains("mojari") ||
            input.contains("चमड़ा") || input.contains("जूती")) {
            keywords.addAll(listOf("Genuine Leather", "Hand-stitched", "Ethnic Mojari", "Durable"))
            return Pair(CraftCategory.LEATHER, keywords)
        }

        keywords.addAll(listOf("Authentic Craft", "Handmade", "Rural Artisan", "Eco-conscious"))
        return Pair(CraftCategory.OTHER, keywords)
    }

    private fun generateEnglishCatalog(
        input: String,
        category: CraftCategory,
        keywords: List<String>
    ): ProductAiResult {
        return when (category) {
            CraftCategory.EMBROIDERY -> {
                ProductAiResult(
                    title = "Handmade Traditional Embroidered Cotton Bag",
                    description = "A handcrafted cotton bag featuring intricate traditional embroidery. Lightweight, durable, reusable and suitable for everyday use with comfortable shoulder straps.",
                    craftHistory = "Created using centuries-old needlework techniques passed down through generations of rural artisan families. Supports fair livelihoods for local women weavers.",
                    suggestedCategory = category,
                    detectedKeywords = keywords,
                    language = "en"
                )
            }
            CraftCategory.POTTERY -> {
                ProductAiResult(
                    title = "Handcrafted Terracotta Clay Art Vessel",
                    description = "Molded from riverbed terracotta clay on a traditional potter's wheel, natural sun-dried and wood-fired. Completely chemical-free and porous for natural coolness.",
                    craftHistory = "Terracotta pottery traces its heritage to the ancient Indus Valley craft tradition. Each piece bears the unique fingerprint marks of the master potter.",
                    suggestedCategory = category,
                    detectedKeywords = keywords,
                    language = "en"
                )
            }
            CraftCategory.WOODWORK -> {
                ProductAiResult(
                    title = "Hand-Carved Artisan Wooden Decor Craft",
                    description = "Exquisitely hand-carved from seasoned natural wood by skilled carvers. Smooth beeswax finish enhancing the natural grain patterns.",
                    craftHistory = "Woodcarving has adorned Indian temples and royal havelis for centuries. The artisan uses hand chisels and mallet without modern automated tools.",
                    suggestedCategory = category,
                    detectedKeywords = keywords,
                    language = "en"
                )
            }
            CraftCategory.METALWORK -> {
                ProductAiResult(
                    title = "Traditional Brass Handcrafted Decorative Art",
                    description = "Cast using ancient non-ferrous metal technique with hand-beaten texture and durable vintage patina finish.",
                    craftHistory = "Originates from tribal lost-wax Dhokra metallurgy, one of the oldest known metal casting methods dating back over 4,000 years.",
                    suggestedCategory = category,
                    detectedKeywords = keywords,
                    language = "en"
                )
            }
            CraftCategory.JEWELRY -> {
                ProductAiResult(
                    title = "Artisanal Handcrafted Ethnic Ornament",
                    description = "Hand-assembled using natural materials, traditional filigree wirework, and ethnic motifs suitable for cultural and everyday adornment.",
                    craftHistory = "Rooted in rural tribal jewelry traditions celebrating regional folklore and nature-inspired geometry.",
                    suggestedCategory = category,
                    detectedKeywords = keywords,
                    language = "en"
                )
            }
            CraftCategory.LEATHER -> {
                ProductAiResult(
                    title = "Traditional Hand-Stitched Leather Craft",
                    description = "Crafted from vegetable-tanned leather, hand-cut and reinforced with durable cotton thread stitching.",
                    craftHistory = "Heritage leather craft perfected by hereditary cobbler artisans of Rajasthan and Punjab.",
                    suggestedCategory = category,
                    detectedKeywords = keywords,
                    language = "en"
                )
            }
            CraftCategory.OTHER -> {
                val title = if (input.isNotBlank() && input.length < 50) {
                    "Handcrafted ${input.replaceFirstChar { it.uppercase() }}"
                } else {
                    "Authentic Handcrafted Artisan Heritage Product"
                }
                ProductAiResult(
                    title = title,
                    description = "A meticulously handcrafted piece made by local rural artisans using sustainably sourced indigenous materials. Each unit is individually crafted with high attention to detail.",
                    craftHistory = "Reflects indigenous Indian handicraft traditions, preserving age-old community knowledge and fostering sustainable micro-entrepreneurship.",
                    suggestedCategory = category,
                    detectedKeywords = keywords,
                    language = "en"
                )
            }
        }
    }

    private fun generateHindiCatalog(
        input: String,
        category: CraftCategory,
        keywords: List<String>
    ): ProductAiResult {
        return when (category) {
            CraftCategory.EMBROIDERY -> {
                ProductAiResult(
                    title = "हस्तनिर्मित पारंपरिक कढ़ाई वाला सूती बैग",
                    description = "पारंपरिक हस्त-कढ़ाई से सुसज्जित शुद्ध सूती थैला। यह मजबूत, पर्यावरण-अनुकूल और रोजमर्रा के उपयोग के लिए बेहद उपयुक्त है।",
                    craftHistory = "यह उत्पाद ग्रामीण महिला बुनकरों द्वारा पीढ़ियों पुरानी पारंपरिक सुई-धागे की कला से तैयार किया गया है।",
                    suggestedCategory = category,
                    detectedKeywords = keywords,
                    language = "hi"
                )
            }
            CraftCategory.POTTERY -> {
                ProductAiResult(
                    title = "हस्तनिर्मित प्राकृतिक टेराकोटा मिट्टी का बर्तन",
                    description = "पारंपरिक कुम्हार के चाक पर नदी की शुद्ध मिट्टी से गढ़ा गया बर्तन। इसे प्राकृतिक रूप से धूप में सुखाकर भट्टी में पकाया गया है।",
                    craftHistory = "मिट्टी के बर्तन बनाने की कला भारत की 5000 वर्ष पुरानी सिंधु घाटी सभ्यता की विरासत है।",
                    suggestedCategory = category,
                    detectedKeywords = keywords,
                    language = "hi"
                )
            }
            else -> {
                ProductAiResult(
                    title = "पारंपरिक हस्तशिल्प कलाकृति",
                    description = "ग्रामीण शिल्पकारों द्वारा प्राकृतिक स्थानीय सामग्रियों से तैयार की गई विशिष्ट हस्तकला कृति।",
                    craftHistory = "यह शिल्प पीढ़ियों से चले आ रहे पारंपरिक भारतीय हुनर और कारीगरी का प्रतीक है।",
                    suggestedCategory = category,
                    detectedKeywords = keywords,
                    language = "hi"
                )
            }
        }
    }

    private fun containsHindiCharacters(str: String): Boolean {
        for (char in str) {
            val code = char.code
            if (code in 0x0900..0x097F) return true
        }
        return false
    }
}
