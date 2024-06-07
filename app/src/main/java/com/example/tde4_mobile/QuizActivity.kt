package com.example.tde4_mobile

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.app.AlertDialog
import android.content.Intent
import com.example.tde4_mobile.R

class QuizActivity : AppCompatActivity() {

    private var currentQuestionIndex = 0
    private var score = 0

    private lateinit var questionTextView: TextView
    private lateinit var answerRadioGroup: RadioGroup
    private lateinit var submitButton: Button
    private lateinit var feedbackTextView: TextView

    private val questions = listOf(
        Question("Quem pintou Mona Lisa?",
            listOf("Vincent van Gogh", "Leonardo da Vinci", "Pablo Picasso", "Michelangelo"), 1),
        Question("Qual o Rio mais longo do mundo?",
            listOf("Rio Amazonas", "Rio Nilo", "Rio Mississipi", "Rio Yangtze"), 0),
        Question("Quem é conhecido como o 'Pai da Física Moderna'?",
            listOf("Isaac Newton", "Galileo Galilei", "Albert Einstein", "Nikola Tesla"), 2),
        Question("Qual país é famoso por sua produção de tulipas?",
            listOf("Espanha", "França", "Holanda", "Itália"), 2),
        Question("Qual é o maior deserto do mundo?",
            listOf("Deserto do Saara", "Deserto de Gobi", "Deserto do Kalahari", "Deserto da Antártida"), 3),
        Question("Qual é a moeda oficial do Reino Unido?",
            listOf("Euro", "Dólar", "Libra Esterlina", "Franco"), 2),
        Question("Em que ano o homem pisou na Lua pela primeira vez?",
            listOf("1965", "1969", "1972", "1975"), 1),
        Question("Qual é o idioma oficial do Brasil?",
            listOf("Espanhol", "Inglês", "Francês", "Português"), 3),
        Question("Quem escreveu 'Romeu e Julieta'?",
            listOf("Charles Dickens", "William Shakespeare", "Jane Austen", "Mark Twain"), 1),
        Question("Qual é a montanha mais alta do mundo?",
            listOf("Monte Everest", "K2", "Kangchenjunga", "Lhotse"), 0)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        questionTextView = findViewById(R.id.responda)
        answerRadioGroup = findViewById(R.id.radio_group)
        submitButton = findViewById(R.id.buton_responder)
        feedbackTextView = findViewById(R.id.feedback_text)

        updateQuestion()

        submitButton.setOnClickListener {
            if (answerRadioGroup.checkedRadioButtonId == -1) {
                Toast.makeText(this, "Selecione uma opção antes de responder!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedButton = findViewById<RadioButton>(answerRadioGroup.checkedRadioButtonId)
            val selectedAnswer = selectedButton.text.toString()
            val correctAnswer = questions[currentQuestionIndex].answers[questions[currentQuestionIndex].correctAnswerIndex]

            if (selectedAnswer == correctAnswer) {
                score++
                feedbackTextView.text = "Correto!"
            } else {
                feedbackTextView.text = "Incorreta. A resposta correta é $correctAnswer."
            }

            feedbackTextView.visibility = View.VISIBLE

            if (currentQuestionIndex < questions.lastIndex) {
                currentQuestionIndex++
                updateQuestion()
            } else {
                submitButton.isEnabled = false
                showResults()
            }
        }
    }

    private fun updateQuestion() {
        val question = questions[currentQuestionIndex]
        questionTextView.text = question.text
        answerRadioGroup.clearCheck()
        answerRadioGroup.removeAllViews()
        feedbackTextView.visibility = View.GONE

        for ((index, answer) in question.answers.withIndex()) {
            val radioButton = RadioButton(this)
            radioButton.text = answer
            radioButton.id = View.generateViewId()
            answerRadioGroup.addView(radioButton)
        }
    }

    private fun showResults() {
        val totalQuestions = questions.size
        val percentage = score.toDouble() / totalQuestions * 100

        val feedbackMessage = when {
            percentage < 50 -> "Poxa, você não foi muito bem. Mais sorte na próxima tentativa!"
            percentage == 50.0 -> "Muito bem, você está no caminho certo, vamos melhorar!"
            else -> "Uau, você é espetacular. Me curvo a sua sabedoria!"
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Resultado do Quiz")
        builder.setMessage("Respostas corretas: $score.\nAcertou: ${"%.2f".format(percentage)}%.\n$feedbackMessage")
        builder.setPositiveButton("OK") { dialog, _ ->
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val dialog = builder.create()
        dialog.show()
    }
}

data class Question(val text: String, val answers: List<String>, val correctAnswerIndex: Int)
