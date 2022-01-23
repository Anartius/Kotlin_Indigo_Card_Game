data class Intern(val weeklyWorkload: Int) {
    val baseWorkload = 20

    class Salary {
        val basePay = 50
        val extraHoursPay = 2.8
    }

    val salary = Salary()
    val weeklySalary = salary.basePay + (weeklyWorkload - baseWorkload) * salary.extraHoursPay
}