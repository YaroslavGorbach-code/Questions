package yaroslavgorbach.questions.data.questions.model

data class Question(val text: String){

    companion object{
        val Test = Question("Is it a test question?")
    }
}