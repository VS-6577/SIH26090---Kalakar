# KalaKriti Artisan Catalog — SIH 2026 (Problem Statement SIH26090)

> **"AI-Driven Market Linkage and Smart Cataloging Mobile Application for Marginalized Artisans"**

An **Offline-First**, production-quality Android mobile application built for marginalized rural artisans and micro-entrepreneurs who may have limited digital literacy and unreliable or no internet connectivity.

---

## 🎨 Visual Design System & Interaction Flow

The application reproduces the custom visual design and continuous 3-frame product-creation workflow:

- **Warm Off-White Background**: `#FBF9F5` for daylight contrast and clean aesthetics.
- **Terracotta / Rust-Orange Primary**: `#C05621` representing traditional Indian terracotta and pottery.
- **Soft Beige Content Cards**: `#F5EFEB` with subtle terracotta border `#DCC8B8`.
- **Artisan Green Action Buttons**: `#2E7D32` with high-elevation, thick touch targets (≥ 56dp) for low-literacy clarity.
- **Dark Brown Charcoal Typography**: `#2C2018` for optimal readability.
- **Animated Voice Recording Ripple**: `#C0392B` terracotta-red pulsing indicator.
- **Fair Wage Pricing Card**: Mint-green tinted card `#E8F5E9` displaying bold **₹450** and cost breakdowns.

### Continuous 3-Stage Workflow

```
FRAME 1: Add your Product (Empty Photo Card + "CLICK PHOTO" + "Hold to speak" Mic + "Voice Description")
   │
   ▼ [User taps "CLICK PHOTO" or Selects Sample]
FRAME 2: Photo Captured (Displays Studio photo + "CHANGE PHOTO" + Active Voice Transcription)
   │
   ▼ [User taps "Proceed to Step 2 →"]
FRAME 3: Review & Pricing
   ├── Studio Photos Carousel (Horizontal scroll, page indicators, Add/Remove photo)
   ├── Product Description & History (AI-generated title, description, craft heritage story)
   ├── "🔊 Listen to Details" (Text-to-Speech narration for low-literacy users)
   ├── "Fair Wage Pricing" Card (₹450 suggested price: Material ₹180 + Labor ₹170 + Margin ₹100)
   └── "Save Product" (Persists locally to Room DB with immediate haptic & audio feedback)
```

---

## 🛠️ Architecture & Tech Stack

- **Framework**: Jetpack Compose with Material 3
- **Language**: Kotlin with Coroutines & StateFlow
- **Architecture**: MVVM + Clean Architecture / Repository Pattern
- **Local Database**: Room DB (`ProductEntity`, `ProductDao`, `AppDatabase`)
- **Preferences**: Jetpack DataStore (`PreferenceManager` for language and demo preferences)
- **Camera**: CameraX (`PreviewView` + `ImageCapture` + Studio Image Processor)
- **Voice & Speech**:
  - Android `SpeechRecognizer` with `RecognitionListener`
  - Offline preset artisan voice simulator for emulators and no-network environments
- **Audio Feedback (TTS)**: Android `TextToSpeech` with bilingual `en-IN` and `hi-IN` support
- **Offline AI Engine**: `OfflineAIService` implementing `AIService` (keyword entity extractor, craft ontology, and natural language catalog generator)
- **Fair Wage Pricing Engine**: `OfflinePricingEngine` implementing `PricingEngine` (living-wage based labor rate, material cost, and artisan profit margin)
- **Studio Photo Enhancement**: `ImageEnhancementService` (offline brightness, contrast, studio vignette, and thumbnail generation)

---

## 💡 Key SIH26090 Features

### 1. 100% Offline-First
All core operations—camera capture, voice transcription, AI catalog generation, studio enhancement, fair-wage calculation, and catalog persistence—work in **Airplane Mode** without internet.

### 2. Fair Wage Living Pricing
Calculates fair, transparent prices protecting artisans from exploitation:
$$\text{Suggested Price} = \text{Material Cost} + (\text{Labor Hours} \times \text{Hourly Living Wage}) + \text{Artisan Margin}$$
**Demo Benchmark**:
- Material Cost = ₹180
- Labor Cost = ₹170 (2.5 hrs × ₹68/hr)
- Artisan Margin = ₹100
- **Suggested Price = ₹450**

### 3. Low-Literacy & Accessibility First
- Minimum touch target size 56dp on all primary buttons.
- Voice-first interaction with "Hold to speak".
- "🔊 Listen to Details" reads the complete catalog aloud in English or Hindi.
- Large, unmistakable iconography (Camera, Microphone, Speaker, Save Checkmark).

### 4. Bilingual Architecture (English & Hindi)
Instant one-tap toggle between English and हिन्दी with full string localization (`values/strings.xml` and `values-hi/strings.xml`).

---

## 🚀 How to Open and Run in Android Studio

1. Open **Android Studio** (Hedgehog, Iguana, Jellyfish, or newer).
2. Select **File → Open** and choose:
   `/Users/vedikasingh/.gemini/antigravity/scratch/artisan_catalog`
3. Allow Gradle Sync to finish.
4. Select your connected Android device or Emulator (API 24+).
5. Click **Run** (`Shift + F10`).

---

## 🧪 Running Unit Tests

Run the offline unit tests:
```bash
./gradlew test
```
Tests included:
- `OfflinePricingEngineTest`: Validates the ₹450 benchmark and living-wage calculations.
- `OfflineAIServiceTest`: Validates NLP keyword extraction and English/Hindi catalog generation.
- `ProductModelTest`: Validates Room entity mapping and domain persistence integrity.
