class Employee(val clothesSize: Int) {
    class Uniform {
        val uniformType = "suit"
        val uniformColor = "blue"
    }
    val uniform = Employee.Uniform()
    fun printUniformInfo() {
        println(
            "The employee wears a ${uniform.uniformColor} " +
                "${uniform.uniformType} in size $clothesSize"
        )
    }
}