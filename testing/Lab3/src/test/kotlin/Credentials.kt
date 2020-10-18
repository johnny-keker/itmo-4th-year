class Credentials {
    companion object {
        val login: String
        val password: String
        val wrongPassword: String

        init {
            val data = Credentials::class.java.getResource("/credentials").readText().split("\n")
            login = data[0].replace("\\s".toRegex(), "")
            password = data[1].replace("\\s".toRegex(), "")
            wrongPassword = data[2].replace("\\s".toRegex(), "")
        }
    }
}