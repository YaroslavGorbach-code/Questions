package yaroslavgorbach.questions.data.model

data class Question(val text: String){

    companion object{
        val Test = Question("Is it a test question?")
    }
}