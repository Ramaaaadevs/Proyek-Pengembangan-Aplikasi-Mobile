package com.example.noteai.data.repository

import com.example.noteai.data.remote.api.GeminiService
import com.example.noteai.domain.repository.AIRepository
import com.example.noteai.domain.repository.WritingStyle

class AIRepositoryImpl(
    private val geminiService: GeminiService
) : AIRepository {

    override suspend fun summarize(text: String): Result<String> {
        return geminiService.generateContent(
            prompt = text,
            systemPrompt = "Kamu adalah asisten yang ahli merangkum. Rangkum teks berikut menjadi poin-poin utama dalam Bahasa Indonesia."
        )
    }

    override suspend fun generateIdeas(topic: String): Result<List<String>> {
        return geminiService.generateContent(
            prompt = "Topik: $topic",
            systemPrompt = "Berikan 5 ide kreatif berdasarkan topik. Format: tiap ide di baris baru diawali angka (1. 2. dst). Gunakan Bahasa Indonesia."
        ).map { response ->
            response.lines()
                .filter { it.matches(Regex("^\\d+\\..*")) }
                .map { it.replaceFirst(Regex("^\\d+\\.\\s*"), "") }
                .ifEmpty { listOf(response) }
        }
    }

    override suspend fun improveWriting(text: String, style: WritingStyle): Result<String> {
        return geminiService.generateContent(
            prompt = text,
            systemPrompt = "${style.prompt}. Berikan HANYA hasil perbaikan tanpa penjelasan."
        )
    }

    override suspend fun translate(text: String, targetLanguage: String): Result<String> {
        return geminiService.generateContent(
            prompt = text,
            systemPrompt = "Terjemahkan teks berikut ke $targetLanguage. Berikan HANYA hasil terjemahan."
        )
    }

    override suspend fun chat(message: String): Result<String> {
        return geminiService.generateContent(prompt = message)
    }

    override suspend fun suggestTitle(content: String): Result<String> {
        return geminiService.generateContent(
            prompt = content,
            systemPrompt = "Sarankan 1 judul singkat (maks 7 kata) untuk konten berikut. Berikan HANYA judul, tanpa tanda kutip."
        )
    }

    suspend fun generateItinerary(
        destination: String,
        duration: Int,
        budget: Double,
        interests: String
    ): Result<String> {
        val prompt = """
            Buatkan rencana perjalanan (itinerary) ke $destination selama $duration hari.
            Budget: Rp ${budget.toLong()}
            Minat/preferensi: $interests
            
            Format output:
            Hari 1:
            - Pagi: [aktivitas]
            - Siang: [aktivitas + rekomendasi makan]
            - Sore: [aktivitas]
            - Malam: [aktivitas + rekomendasi makan]
            - Estimasi biaya hari ini: Rp [angka]
            
            (ulangi untuk setiap hari)
            
            Total estimasi biaya: Rp [angka]
            Tips: [2-3 tips berguna]
        """.trimIndent()

        return geminiService.generateContent(
            prompt = prompt,
            systemPrompt = "Kamu adalah travel planner profesional Indonesia. Buat itinerary yang detail, realistis, dan sesuai budget. Gunakan Bahasa Indonesia."
        )
    }
}
