package su.leffsu.sequencer.logic

import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONArray
import org.json.JSONObject

object NetworkController {

    // Ссылка
    val url = "http://172.20.10.4:3000/"

    // Инстанс сокета (раскомментируйте, если будет сокет, иначе краш)
    private var socket: Socket = IO.socket(url)

    // Кэшированный массив булинов
    private var cachedArray = arrayListOf<Boolean>(
        false, false, false, false,
        false, false, false, false,
        false, false, false, false,
        false, false, false, false,
        false, false, false
    )

    // Укороченный ивент
    private val socketEvent = "change"

    fun init(){
        socket.on("connection") {
            println("\r\n\r\nsocket connected")
        }

        // подключаемся
        socket.connect()

    }

    fun sendToServer(array: ArrayList<Boolean>, test: Boolean) {
        val debug = System.currentTimeMillis()
        val json = JSONArray()
        for (x in array.indices) {
            if (test) {
                val jsonObject: JSONObject = JSONObject()
                jsonObject.put("i", x)
                jsonObject.put("v", if (array[x]) 1 else 0)
                json.put(
                    jsonObject
                )
            } else {
                // если кэшированное и текущее состояние различается ->
                if (cachedArray[x] != array[x]) {
                    // кладем в json
                    val jsonObject: JSONObject = JSONObject()
                    jsonObject.put("i", x)
                    jsonObject.put("v", if (array[x]) 1 else 0)
                    json.put(
                        jsonObject
                    )
                }
            }
        }
        println("networkcontroller $json ${System.currentTimeMillis() - debug}")
        cachedArray = array
        socket.emit(socketEvent, json)
    }
}