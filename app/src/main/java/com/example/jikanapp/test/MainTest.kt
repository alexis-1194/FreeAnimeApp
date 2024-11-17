package com.example.jikanapp.test
import com.example.jikanapp.domain.model.full.Genre
import com.example.jikanapp.util.Utils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking



suspend fun mySuspendFunction() {
    delay(2000L) // simula una tarea asíncrona
    println("Terminada la tarea")
    val myFlow = flowOf(1, 2, 3, 4, 5)
        .filter { it % 2 == 0 } // Filtra los números pares
        .map { it * 2 }         // Multiplica por 2

    myFlow.collect { println(it) }
}


fun main() {
    println("Hello, world!!!")
//    runBlocking {
//        val job = launch {
//            mySuspendFunction()
//        }
//        job.join()
//    }
//    val latestMessages: Flow<List<String>> = flow {
//    }

    val list = ArrayList<String>()
    list.add("Dragon Ball")
    list.add("One piece")
    list.add("Naruto")

    println(listToStrings(list))
}

fun listToStrings(list: List<String>): String {
    return list.joinToString { it }
}


class MainTest{

    fun getClassName() : String{
        return javaClass.kotlin.simpleName.toString()
    }

    fun getMethodName() : String{
        return Thread.currentThread().stackTrace[1].methodName
    }

}


private fun playVideo(videoPath: String) {
    // Aquí llamamos a la función para reproducir el video usando MediaPlayer o VideoView
}

interface Shape {
    fun draw()
}

class Rectangle : Shape {
    override fun draw() {
        println("Inside Rectangle::draw() method.")
    }
}

class Square : Shape {
    override fun draw() {
        println("Inside Square::draw() method.")
    }
}

class Circle : Shape {
    override fun draw() {
        println("Inside Circle::draw() method.")
    }
}

class ShapeFactory {
    //use getShape method to get object of type shape
    fun getShape(shapeType: String?): Shape? {
        if (shapeType == null) {
            return null
        }
        if (shapeType.equals("CIRCLE", ignoreCase = true)) {
            return Circle()
        } else if (shapeType.equals("RECTANGLE", ignoreCase = true)) {
            return Rectangle()
        } else if (shapeType.equals("SQUARE", ignoreCase = true)) {
            return Square()
        }

        return null
    }
}

object FactoryPatternDemo {
    @JvmStatic
    fun main(args: Array<String>) {
        val shapeFactory = ShapeFactory()

        //get an object of Circle and call its draw method.
        val shape1 = shapeFactory.getShape("CIRCLE")

        //call draw method of Circle
        shape1!!.draw()

        //get an object of Rectangle and call its draw method.
        val shape2 = shapeFactory.getShape("RECTANGLE")

        //call draw method of Rectangle
        shape2!!.draw()

        //get an object of Square and call its draw method.
        val shape3 = shapeFactory.getShape("SQUARE")

        //call draw method of square
        shape3!!.draw()
    }
}


