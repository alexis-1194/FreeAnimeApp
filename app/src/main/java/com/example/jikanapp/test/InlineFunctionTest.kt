package com.example.jikanapp.test

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

suspend fun <T> List<T>.normalForEach(action: suspend (T) -> Unit) {
    for (item in this) {
        action(item)
    }
}

inline fun <T> List<T>.inlineForEach(action: (T) -> Unit) {
    for (item in this) {
        action(item)
    }
}



fun <T> printElement(element: T) {
    if (element is String) {
        println("It's a String")
    }
}

abstract class Car{

    fun start() {}

}

class Audi : Car()

class Mercedes : Car()

class CarMechanic<out T: Car> {
    fun repair(car: @UnsafeVariance T) { //Si no usamos la anotación no es posible utilizar el generico como parametro

    }
}

class WorkShop {
//    fun <T : Car> addMechanic(mechanic: CarMechanic<T>) {}
    fun addMechanic(mechanic: CarMechanic<Car>) {}
}

fun main() {
//    val list = (1..20).toList()
////    CoroutineScope(Dispatchers.Default).launch {
////        list.inlineForEach {
////            delay(1000)
////            print(it)
////        }
////    }


    val sum = { x: Int, y: Int, z: Int, w: Int -> x + y + z + w }

    val mechanic = CarMechanic<Audi>()
    val workshop = WorkShop()

        mechanic.repair(Audi())
    workshop.addMechanic(mechanic)

}

fun <T> isOfType(value: Any, clazz: Class<T>): Boolean {
    return clazz.isInstance(value)
}

// Llamada
val result = isOfType("Hello", String::class.java) // true

inline fun <reified T> T.printClassName() {
    println(T::class.simpleName)
}